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
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TrightSidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer.RepairGroup;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer.Repair;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author sviro
 */
public class RXMLTree {
  
  private static final double LEAF_WEIGHT = 0.2;
  private static final double NODE_WEIGHT = 0.4;

  private static final Logger LOG = Logger.getLogger(RXMLTree.class);
  private final Document document;
  private List<Path> paths = null;
  private Map<Node, NodeAttribute> nodesMap;
  private List<Tuple> tuples;
  private int tupleID;
  private List<RepairGroup> repairGroups;

  public RXMLTree(final Document document) {
    this.document = document;
    nodesMap = new HashMap<Node, NodeAttribute>();
    this.repairGroups = new ArrayList<RepairGroup>();
    tupleID = 0;
    nodesMap = createNodesMap(document);
  }

  public boolean isSatisfyingFD(FD fd) {
    return isTreeSatisfyingFD(fd) || isReliabilitySatisfyFD(fd);
  }

  private boolean isTreeSatisfyingFD(FD fd) {
    if (tuples == null) {
      tuples = TupleFactory.createTuples(this);
    }
    List<Pair<Tuple, Tuple>> tuplePairs = TupleFactory.getTuplePairs(tuples);
    if (tuplePairs == null || tuplePairs.isEmpty()) {
      throw new RuntimeException("List of tuple pairs can't be null or empty.");
    }

    for (Pair<Tuple, Tuple> tuplePair : tuplePairs) {
      if (!isTuplePairSatisfyingFD(tuplePair, fd)) {
        return false;
      }
    }

    return true;
  }

  public boolean isTuplePairSatisfyingFD(Pair<Tuple, Tuple> tuplePair, FD fd) {
    Tuple tuple1 = tuplePair.getFirst();
    Tuple tuple2 = tuplePair.getSecond();

    List<PathAnswer> tuple1LeftSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple1, fd.getLeftSidePaths());
    List<PathAnswer> tuple2LeftSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple2, fd.getLeftSidePaths());
    List<PathAnswer> tuple1RightSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple1, fd.getRightSidePaths());
    List<PathAnswer> tuple2RightSideAnswers = TupleFactory.getFDSidePathAnswers(this, tuple2, fd.getRightSidePaths());

    boolean leftSideEqual = areTuplePathAnswersEqual(tuple1LeftSideAnswers, tuple2LeftSideAnswers);
    boolean tuple1PathAnswersNotEmpty = isTuplePathAnswersNotEmpty(tuple1LeftSideAnswers);
    boolean rightSideEqual = areTuplePathAnswersEqual(tuple1RightSideAnswers, tuple2RightSideAnswers);

    if (leftSideEqual && tuple1PathAnswersNotEmpty && !rightSideEqual) {
      return false;
    }
    return true;
  }

  private boolean areTuplePathAnswersEqual(final List<PathAnswer> tuple1Answers, final List<PathAnswer> tuple2Answers) {
    if (tuple1Answers.size() != tuple2Answers.size()) {
      return false;
    }

    for (int i = 0; i < tuple1Answers.size(); i++) {
      PathAnswer tuple1Answer = tuple1Answers.get(i);
      PathAnswer tuple2Answer = tuple2Answers.get(i);

      if (!tuple1Answer.equals(tuple2Answer)) {
        return false;
      }
    }

    return true;
  }

  private boolean isTuplePathAnswersNotEmpty(List<PathAnswer> tuple1LeftSideAnswers) {
    for (PathAnswer pathAnswer : tuple1LeftSideAnswers) {
      if (pathAnswer.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private boolean isReliabilitySatisfyFD(FD fd) {
    if (tuples == null) {
      tuples = TupleFactory.createTuples(this);
    }

    List<Pair<Tuple, Tuple>> tuplePairs = TupleFactory.getTuplePairs(tuples);
    for (Pair<Tuple, Tuple> tuplePair : tuplePairs) {
      boolean isUnreliable = isUnreliableSide(tuplePair, fd.getLeftSidePaths()) || isUnreliableSide(tuplePair, fd.getRightSidePaths());
      if (!isUnreliable) {
        return false;
      }
    }

    return true;
  }

  private boolean isUnreliableSide(final Pair<Tuple, Tuple> tuplePair, SidePaths side) {
    return isUnreliableTuple(tuplePair.getFirst(), side) || isUnreliableTuple(tuplePair.getSecond(), side);
  }

  private boolean isUnreliableTuple(Tuple tuple, SidePaths side) {
    List<PathAnswer> pathAnswers = TupleFactory.getFDSidePathAnswers(this, tuple, side);
    for (PathAnswer pathAnswer : pathAnswers) {
      if (!nodesMap.get(pathAnswer.getTupleNodeAnswer()).isReliable()) {
        return true;
      }
    }

    return false;
  }

  public Document getDocument() {
    return document;
  }

  @Override
  public String toString() {
    try {
      TransformerFactory transfac = TransformerFactory.newInstance();
      Transformer trans = transfac.newTransformer();
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.setOutputProperty(OutputKeys.INDENT, "yes");

      //create string from xml tree
      StringWriter sw = new StringWriter();
      StreamResult result = new StreamResult(sw);
      DOMSource source = new DOMSource(document);
      trans.transform(source, result);
      return sw.toString();
    } catch (TransformerException ex) {
      LOG.error(ex);
    }

    return null;
  }

  public List<Path> getPaths() {
    return paths;
  }

  public void setPaths(List<Path> paths) {
    this.paths = paths;
  }

  public String pathsToString() {
    StringBuilder builder = new StringBuilder();
    for (Path path : paths) {
      builder.append(path.getPathValue()).append("\n");
    }

    return builder.toString();
  }

  public PathAnswer getPathAnswer(Path path) {
    return getPathAnswerForTuple(path, null);
  }

  public PathAnswer getPathAnswerForCreatingTuple(final Path path, final Tuple tuple) {
    return getGenericPathAnswerForTuple(path, tuple, true);
  }

  public PathAnswer getPathAnswerForTuple(final Path path, final Tuple tuple) {
    return getGenericPathAnswerForTuple(path, tuple, false);
  }

  /**
   * 
   * @param path
   * @param tuple
   * @param isCreatingTuple
   * @return
   */
  public PathAnswer getGenericPathAnswerForTuple(final Path path, final Tuple tuple, final boolean isCreatingTuple) {
    if (path != null) {
      XPathFactory xpathFactory = XPathFactory.newInstance();
      XPath xPath = xpathFactory.newXPath();
      XPathExpression xPathExpression;

      List<Node> result = new ArrayList<Node>();
      try {
        xPathExpression = xPath.compile(path.getPathValue());
        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        if (tuple != null) {
          for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (nodesMap.get(node).isInTuple(tuple)) {
              result.add(node);
            }
          }

          if (!isCreatingTuple && result.size() > 1) {
            throw new RuntimeException("Tuple must have max 1 answer");
          }

          return new PathAnswer(result, path.isStringPath());
        }

        return new PathAnswer(nodeList, path.isStringPath());
      } catch (XPathExpressionException ex) {
        LOG.error("Path " + path + " cannot be compiled or evaluated.", ex);
      }
    }

    return null;
  }

  public List<Tuple> getTuples() {
    return tuples;
  }

  public int getNewTupleID() {
    return tupleID++;
  }

  public Map<Node, NodeAttribute> getNodesMap() {
    return nodesMap;
  }

  public void applyRepair(Repair repair) {
    LOG.debug(repair.toString());
    
    for (Node node : repair.getUnreliableNodes()) {
      setNodeUnreliable(node);
    }
    
    for (Node node : repair.getValueNodes().keySet()) {
      String newValue = repair.getValueNodes().get(node);
      if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
        Attr attribute = (Attr) node;
        attribute.setValue(newValue);
      } else if (node.getNodeType() == Node.TEXT_NODE) {
        Text textnode = (Text) node;
        textnode.setNodeValue(newValue);
      }
    }
    
  }
  
  private void setNodeUnreliable(Node node) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        element.setAttribute("unreliable", "true");
      }
  }

  private static Map<Node, NodeAttribute> createNodesMap(final Document document) {
    Map<Node, NodeAttribute> result = new HashMap<Node, NodeAttribute>();
    traverseTree(document.getDocumentElement(), result);

    return result;
  }

  private static void traverseTree(Node node, Map<Node, NodeAttribute> nodesMap) {
    if (!nodesMap.containsKey(node)) {
      nodesMap.put(node, new NodeAttribute());
    }
    NodeAttribute nodeAttribute = nodesMap.get(node);
    short nodeType = node.getNodeType();
    if (nodeType == Node.ATTRIBUTE_NODE || nodeType == Node.TEXT_NODE) {
      nodeAttribute.setWeight(LEAF_WEIGHT);
    } else {
      nodeAttribute.setWeight(NODE_WEIGHT);
    }

    //check attributes
    NamedNodeMap attributes = node.getAttributes();
    if (attributes != null) {
      for (int j = 0; j < attributes.getLength(); j++) {
        traverseTree(attributes.item(j), nodesMap);
      }
    }

    if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
      NodeList childNodes = node.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node child = childNodes.item(i);
        traverseTree(child, nodesMap);
      }
    }
  }

  public boolean isFDDefinedForTree(List<FD> functionalDependencies) {
    if (paths == null) {
      throw new RuntimeException("Paths defined for XML tree cannot be null");
    }

    for (FD fd : functionalDependencies) {
      for (Path path : fd.getLeftSidePaths().getPaths()) {
        if (!isPathDefined(path)) {
          return false;
        }
      }

      if (!isPathDefined(fd.getRightSidePaths().getPathObj())) {
        return false;
      }
    }

    return true;
  }

  private boolean isPathDefined(final Path path) {
    return paths.contains(path);
  }
  
  public void addRepairGroup(final RepairGroup repairGroup) {
    repairGroups.add(repairGroup);
  }

  public List<RepairGroup> getRepairGroups() {
    return repairGroups;
  }
}
