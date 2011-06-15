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

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.SidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TleftSidePaths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author sviro
 */
public class TupleFactory {

  public static List<Tuple> createTuples(RXMLTree tree) {
    List<Tuple> result = new ArrayList<Tuple>();
    List<String> paths = tree.getPaths();
    Queue<Tuple> lastAddedTuples = new ArrayDeque<Tuple>();

    int firstTupleId = tree.getNewTupleID();
    //this is first "tuple" which is whole tree
    Tuple firstTuple = markNodesToTuple(tree, null, firstTupleId, null, null);
    lastAddedTuples.add(firstTuple);

    for (String path : paths) {
      Queue<Tuple> newAddedTuples = new ArrayDeque<Tuple>();
      while (!lastAddedTuples.isEmpty()) {
        Tuple lastTuple = lastAddedTuples.poll();

        PathAnswer pathAnswer = tree.getPathAnswerForTuple(path, lastTuple);
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

  //TODO sviro unit test
  public static List<Pair<Tuple, Tuple>> getTuplePairs(List<Tuple> tuples) {
    List<Pair<Tuple, Tuple>> result = new ArrayList<Pair<Tuple, Tuple>>();
    for (Tuple tuple : tuples) {
      for (Tuple tuple1 : tuples) {
        if (!tuple.equals(tuple1) && !result.contains(new Pair<Tuple, Tuple>(tuple1, tuple))) {
          result.add(new Pair<Tuple, Tuple>(tuple, tuple1));
        }
      }
    }

    return result;
  }

  public static List<Pair<Tuple, Tuple>> getTuplePairNotSatisfyingFD(List<Tuple> tuples, FD fd) {
    List<Tuple> notSatisfyingTuples = new ArrayList<Tuple>();
    for (Tuple tuple : tuples) {
    }



    return getTuplePairs(notSatisfyingTuples);
  }

  public static List<PathAnswer> getFDSidePathAnswers(RXMLTree tree, Tuple tuple, SidePaths sidePaths) {
    //TODO sviro implement
    List<PathAnswer> result = new ArrayList<PathAnswer>();
    for (String path : sidePaths.getPath()) {
      PathAnswer pathAnswer = tree.getPathAnswerForTuple(path, tuple);
      result.add(pathAnswer);
    }

    return result;
  }

  private static Tuple markNodesToTuple(final RXMLTree tree, final Node cuttingNode, final int tupleId, final Tuple actualTuple, final List<Node> tupleCut) {
    Tuple result = new Tuple(tupleId);

    traverseTree(tree.getDocument().getDocumentElement(), tree.getTuplesMap(), cuttingNode, actualTuple, result, tupleCut, false);

    return result;
  }

  private static void unmarkNodesFromTuple(RXMLTree tree, Tuple lastTuple) {
    traverseTree(tree.getDocument().getDocumentElement(), tree.getTuplesMap(), null, null, lastTuple, null, true);
  }

  /**
   * Traverse the XML tree tuple and add or remove node into/form {@code currentTuple}. If the {@code currentTuple} is added,
   * this means that {@code actualTuple} will be divided into new tuples where each new tuple is represented
   * by one node of {@code tupleCut}(the {@code cuttingNode} is representative for this tuple). 
   * @param node Node to be added into {@code currentTuple}
   * @param tuplesMap Map holding information for each Node to which tuple belongs to.
   * @param cuttingNode Representative for tuple division. If it is {@code null}, there is no dividing of tuple.
   * @param actualTuple The tuple which is actually traversed. If it is {@code null}, whole xml tree is traversed. 
   * @param currentTuple Tuple to which are nodes added or removed from.
   * @param tupleCut List of nodes for which dividing of node is provided.
   * @param remove Flag indicationg if {@code currentTuple} is added or removed.
   */
  private static void traverseTree(Node node, Map<Node, Set<Tuple>> tuplesMap, Node cuttingNode, Tuple actualTuple, Tuple currentTuple, List<Node> tupleCut, boolean remove) {
    if (!tuplesMap.containsKey(node)) {
      tuplesMap.put(node, new HashSet<Tuple>());
    }

    if (actualTuple == null || tuplesMap.get(node).contains(actualTuple)) {
      boolean wasRemoved = false;
      if (remove) {
        wasRemoved = tuplesMap.get(node).remove(currentTuple);
      } else {
        if (cuttingNode != null && tupleCut != null && tupleCut.contains(node) && !node.equals(cuttingNode)) {
          return;
        }
        tuplesMap.get(node).add(currentTuple);
      }

      if (remove && !wasRemoved) {
        return;
      }
      //check attributes
      NamedNodeMap attributes = node.getAttributes();
      if (attributes != null) {
        for (int j = 0; j < attributes.getLength(); j++) {
          traverseTree(attributes.item(j), tuplesMap, cuttingNode, actualTuple, currentTuple, tupleCut, remove);
        }
      }

      NodeList childNodes = node.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node child = childNodes.item(i);
        traverseTree(child, tuplesMap, cuttingNode, actualTuple, currentTuple, tupleCut, remove);
      }
    }
  }
}
