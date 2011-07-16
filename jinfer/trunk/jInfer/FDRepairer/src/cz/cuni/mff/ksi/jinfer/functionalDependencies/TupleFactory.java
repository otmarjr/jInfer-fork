/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.SidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TleftSidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TrightSidePaths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sviro
 */
public final class TupleFactory {

  public static boolean isTuple(RXMLTree tree, Tuple tuple) {
    List<Tuple> allTuples = tree.getTuples();

    for (Tuple tuple1 : allTuples) {
      if (!tuple1.equals(tuple) && tuple1.contains(tuple)) {
        return false;
      }
    }

    return true;
  }

  public static void removeTuple(RXMLTree tree, Tuple tuple) {
    unmarkNodesFromTuple(tree, tuple);

    tree.removeTuple(tuple);
  }

  public static void removeTuples(RXMLTree tree, Set<Tuple> tuplesToRemove) {
    for (Tuple tuple : tuplesToRemove) {
      removeTuple(tree, tuple);
    }
  }

  private TupleFactory() {
  }

  public static List<Tuple> createTuples(RXMLTree tree) {
    List<Tuple> result = new ArrayList<Tuple>();
    List<Path> paths = tree.getPaths();
    Queue<Tuple> lastAddedTuples = new ArrayDeque<Tuple>();

    int firstTupleId = tree.getNewTupleID();
    //this is first "tuple" which is whole tree
    Tuple firstTuple = markNodesToTuple(tree, null, firstTupleId, null, null);
    lastAddedTuples.add(firstTuple);

    for (Path path : paths) {
      Queue<Tuple> newAddedTuples = new ArrayDeque<Tuple>();
      while (!lastAddedTuples.isEmpty()) {
        Tuple lastTuple = lastAddedTuples.poll();

        PathAnswer pathAnswer = tree.getPathAnswerForCreatingTuple(path, lastTuple);
        if (pathAnswer == null) {
          throw new RuntimeException("PathAnswer can't be null.");
        }
        if (!pathAnswer.hasMaxOneElement()) {
          for (Node node : pathAnswer.getNodeAnswers()) {
            int newTupleID = tree.getNewTupleID();
            Tuple tuple = markNodesToTuple(tree, node, newTupleID, lastTuple, pathAnswer.getNodeAnswers());
            newAddedTuples.add(tuple);
          }
          unmarkNodesFromTuple(tree, lastTuple);
        } else {
          newAddedTuples.add(lastTuple);
        }
      }
      lastAddedTuples.addAll(newAddedTuples);
    }

    result.addAll(lastAddedTuples);

    return result;
  }

  public static List<Pair<Tuple, Tuple>> getTuplePairs(List<Tuple> tuples) {
    if (tuples == null) {
      return null;
    }

    List<Pair<Tuple, Tuple>> result = new ArrayList<Pair<Tuple, Tuple>>();
    for (Tuple tuple : tuples) {
      for (Tuple tuple1 : tuples) {
        if (!tuple.equals(tuple1) && !result.contains(new ImmutablePair<Tuple, Tuple>(tuple1, tuple))) {
          result.add(new ImmutablePair<Tuple, Tuple>(tuple, tuple1));
        }
      }
    }

    return result;
  }

  public static List<Pair<Tuple, Tuple>> getTuplePairNotSatisfyingFDThesis(final RXMLTree tree, final FD fd) {
    return getTuplePairNotSatisfyingFDGeneral(tree, fd, true);
  }

  public static List<Pair<Tuple, Tuple>> getTuplePairNotSatisfyingFD(RXMLTree tree, FD fd) {
    return getTuplePairNotSatisfyingFDGeneral(tree, fd, false);
  }

  public static List<Pair<Tuple, Tuple>> getTuplePairNotSatisfyingFDGeneral(final RXMLTree tree, final FD fd, final boolean isThesis) {
    List<Pair<Tuple, Tuple>> notSatisfyingTuples = new ArrayList<Pair<Tuple, Tuple>>();

    List<Pair<Tuple, Tuple>> tuplePairs = getTuplePairs(tree.getTuples());
    if (tuplePairs == null || tuplePairs.isEmpty()) {
      throw new RuntimeException("List of tuple pairs can't be null or empty.");
    }

    for (Pair<Tuple, Tuple> tuplePair : tuplePairs) {
      if (!tree.isTuplePairSatisfyingFDGeneral(tuplePair, fd, isThesis)) {
        notSatisfyingTuples.add(tuplePair);
      }
    }

    return notSatisfyingTuples;
  }

  public static List<PathAnswer> getFDSidePathAnswers(final RXMLTree tree, final Tuple tuple, final SidePaths sidePaths, final boolean isThesis) {
    if (tuple == null) {
      throw new RuntimeException("Tuple can't be null.");
    }

    if (sidePaths == null) {
      throw new RuntimeException("Side paths can't be null.");
    }

    List<PathAnswer> result = new ArrayList<PathAnswer>();
    if (sidePaths instanceof TleftSidePaths) {
      TleftSidePaths leftSide = (TleftSidePaths) sidePaths;

      for (Path path : leftSide.getPaths()) {
        PathAnswer pathAnswer = tree.getPathAnswerForTuple(path, tuple, isThesis);
        result.add(pathAnswer);
      }
    } else if (sidePaths instanceof TrightSidePaths) {
      TrightSidePaths rightSide = (TrightSidePaths) sidePaths;
      PathAnswer pathAnswer = tree.getPathAnswerForTuple(rightSide.getPathObj(), tuple, isThesis);
      result.add(pathAnswer);
    }

    return result;
  }

  private static Tuple markNodesToTuple(final RXMLTree tree, final Node cuttingNode, final int tupleId, final Tuple actualTuple, final List<Node> tupleCut) {
    Tuple result = new Tuple(tree, tupleId);

    traverseTree(tree.getDocument().getDocumentElement(), tree.getNodesMap(), cuttingNode, actualTuple, result, tupleCut, false);

    return result;
  }

  private static void unmarkNodesFromTuple(RXMLTree tree, Tuple lastTuple) {
    traverseTree(tree.getDocument().getDocumentElement(), tree.getNodesMap(), null, null, lastTuple, null, true);
  }

  public static Collection<Tuple> unmarkNodeFromAllTuples(RXMLTree tree, Node node) {
    NodeAttribute nodeAttribute = tree.getNodesMap().get(node);
    Set<Tuple> result = new HashSet<Tuple>(nodeAttribute.getTuples());

    nodeAttribute.removeFromAllTuples();

    return result;
  }

  /**
   * Traverse the XML tree tuple and add or remove node into/form {@code currentTuple}. If the {@code currentTuple} is added,
   * this means that {@code actualTuple} will be divided into new tuples where each new tuple is represented
   * by one node of {@code tupleCut}(the {@code cuttingNode} is representative for this tuple).
   * @param node Node to be added into {@code currentTuple}
   * @param nodesMap Map holding information for each Node to which tuple belongs to.
   * @param cuttingNode Representative for tuple division. If it is {@code null}, there is no dividing of tuple.
   * @param actualTuple The tuple which is actually traversed. If it is {@code null}, whole xml tree is traversed.
   * @param currentTuple Tuple to which are nodes added or removed from.
   * @param tupleCut List of nodes for which dividing of node is provided.
   * @param remove Flag indicationg if {@code currentTuple} is added or removed.
   */
  private static void traverseTree(Node node, Map<Node, NodeAttribute> nodesMap, Node cuttingNode, Tuple actualTuple, Tuple currentTuple, List<Node> tupleCut, boolean remove) {
    if (!nodesMap.containsKey(node)) {
      throw new RuntimeException("There must be a reference for all nodes in a tree.");
    }

    if (actualTuple == null || nodesMap.get(node).isInTuple(actualTuple)) {
      boolean wasRemoved = false;
      if (remove) {
        wasRemoved = nodesMap.get(node).removeFromTuple(currentTuple);
      } else {
        if (cuttingNode != null && tupleCut != null && tupleCut.contains(node) && !node.equals(cuttingNode)) {
          return;
        }
        nodesMap.get(node).addToTuple(currentTuple);
      }

      if (remove && !wasRemoved) {
        return;
      }
      //check attributes
      NamedNodeMap attributes = node.getAttributes();
      if (attributes != null) {
        for (int j = 0; j < attributes.getLength(); j++) {
          traverseTree(attributes.item(j), nodesMap, cuttingNode, actualTuple, currentTuple, tupleCut, remove);
        }
      }

      if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
          Node child = childNodes.item(i);
          traverseTree(child, nodesMap, cuttingNode, actualTuple, currentTuple, tupleCut, remove);
        }
      }
    }
  }
}
