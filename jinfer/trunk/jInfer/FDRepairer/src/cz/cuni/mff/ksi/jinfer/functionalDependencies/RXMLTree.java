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
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.SidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer.RepairCandidate;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer.RepairGroup;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer.UserNodeSelection;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.weights.Tweight;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Class representing R-XML tree.
 * @author sviro
 */
public class RXMLTree {

  private static final double NODE_WEIGHT = 0.4d;
  private static final Logger LOG = Logger.getLogger(RXMLTree.class);
  private final Document document;
  private List<Path> paths = null;
  private final Map<Node, NodeAttribute> nodesMap;
  private List<Tuple> tuples;
  private final Map<Tuple, SideAnswers> tupleSideAnswers;
  private final Map<Path, NodeList> pathNodeListResults;
  private int tupleID;
  private final List<RepairGroup> repairGroups;
  private final List<UserNodeSelection> savedUserSelections;
  private List<Pair<Tuple, Tuple>> tuplePairs = null;
  private double thresholdT;
  private final XPathFactory xpathFactory;
  private int newValueId = 0;

  /**
   * Constructor of RXMLtree. As a parameter gets DOM representation of XML data.
   * @param document DOM representation of XML data.
   */
  public RXMLTree(final Document document) {
    this.document = document;
    this.repairGroups = new ArrayList<RepairGroup>();
    this.savedUserSelections = new ArrayList<UserNodeSelection>();
    tupleID = 0;
    nodesMap = createNodesMap(document);
    xpathFactory = XPathFactory.newInstance();
    this.tupleSideAnswers = new HashMap<Tuple, SideAnswers>();
    this.pathNodeListResults = new HashMap<Path, NodeList>();
  }

  /**
   * Check if tree satisfies provided functional dependency.
   * @param fd Functional dependency to be check against.
   * @return true if tree satisfies provided functional dependency.
   * @throws InterruptedException if user interrupts repairing.
   */
  public boolean isSatisfyingFD(final FD fd) throws InterruptedException {
    return isSatisfyingFDGeneral(fd, false);
  }

  /**
   * Check if tree satisfies provided functional dependency. This method is suitable
   * for thesis algorithm.
   * @param fd Functional dependency to be check against.
   * @return true if tree satisfies provided functional dependency.
   * @throws InterruptedException if user interrupts repairing.
   */
  public boolean isSatisfyingFDThesis(final FD fd) throws InterruptedException {
    return isSatisfyingFDGeneral(fd, true);
  }

  private boolean isSatisfyingFDGeneral(final FD fd, final boolean isThesis) throws InterruptedException {
    return isTreeSatisfyingFD(fd, isThesis) || isReliabilitySatisfyFD(fd);
  }

  private boolean isTreeSatisfyingFD(final FD fd, final boolean isThesis) throws InterruptedException {
    LOG.info("Start checking tree satisfaction");
    if (tuples == null) {
      tuples = TupleFactory.createTuples(this);
    }

    if (tuplePairs == null) {
      tuplePairs = TupleFactory.getTuplePairs(tuples);
    }

    LOG.debug("Start checking pairs for satisfiability");
    for (Pair<Tuple, Tuple> tuplePair : tuplePairs) {
      if (!isTuplePairSatisfyingFDGeneral(tuplePair, fd, isThesis)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Check if tuple pair satisfies functional dependency. This method is suitable 
   * for thesis algorithm.
   * @param tuplePair Tuple pair to be checked.
   * @param fd Functional dependency to be ckeck against.
   * @return true if tuple pair satisfies functional dependency.
   */
  public boolean isTuplePairSatisfyingFDThesis(final Pair<Tuple, Tuple> tuplePair, final FD fd) {
    return isTuplePairSatisfyingFDGeneral(tuplePair, fd, true);
  }

  /**
   * Check if tuple pair satisfies functional dependency.
   * @param tuplePair Tuple pair to be checked.
   * @param fd Functional dependency to be ckeck against.
   * @return true if tuple pair satisfies functional dependency.
   */
  public boolean isTuplePairSatisfyingFD(final Pair<Tuple, Tuple> tuplePair, final FD fd) {
    return isTuplePairSatisfyingFDGeneral(tuplePair, fd, false);
  }

  private boolean isTuplePairSatisfyingFDGeneral(final Pair<Tuple, Tuple> tuplePair, final FD fd, final boolean isThesis) {
    final Tuple tuple1 = tuplePair.getFirst();
    final Tuple tuple2 = tuplePair.getSecond();

    final List<PathAnswer> tuple1LeftSideAnswers = getTupleSideAnswer(tuple1, true, fd.getLeftSidePaths(), isThesis);
    final List<PathAnswer> tuple2LeftSideAnswers = getTupleSideAnswer(tuple2, true, fd.getLeftSidePaths(), isThesis);
    final List<PathAnswer> tuple1RightSideAnswers = getTupleSideAnswer(tuple1, false, fd.getRightSidePaths(), isThesis);
    final List<PathAnswer> tuple2RightSideAnswers = getTupleSideAnswer(tuple2, false, fd.getRightSidePaths(), isThesis);

    final boolean leftSideEqual = areTuplePathAnswersEqual(tuple1LeftSideAnswers, tuple2LeftSideAnswers);
    final boolean tuple1PathAnswersNotEmpty = isTuplePathAnswersNotEmpty(tuple1LeftSideAnswers);
    final boolean rightSideEqual = areTuplePathAnswersEqual(tuple1RightSideAnswers, tuple2RightSideAnswers);

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
      final PathAnswer tuple1Answer = tuple1Answers.get(i);
      final PathAnswer tuple2Answer = tuple2Answers.get(i);

      if (!tuple1Answer.equals(tuple2Answer)) {
        return false;
      }
    }

    return true;
  }

  private boolean isTuplePathAnswersNotEmpty(final List<PathAnswer> tuple1LeftSideAnswers) {
    for (PathAnswer pathAnswer : tuple1LeftSideAnswers) {
      if (pathAnswer.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private boolean isReliabilitySatisfyFD(final FD fd) throws InterruptedException {
    if (tuples == null) {
      tuples = TupleFactory.createTuples(this);
    }

    if (tuplePairs == null) {
      tuplePairs = TupleFactory.getTuplePairs(tuples);
    }

    for (Pair<Tuple, Tuple> tuplePair : tuplePairs) {
      final boolean isUnreliable = isUnreliableSide(tuplePair, fd.getLeftSidePaths()) || isUnreliableSide(tuplePair, fd.getRightSidePaths());
      if (!isUnreliable) {
        return false;
      }
    }

    return true;
  }

  private boolean isUnreliableSide(final Pair<Tuple, Tuple> tuplePair, final SidePaths side) {
    return isUnreliableTuple(tuplePair.getFirst(), side) || isUnreliableTuple(tuplePair.getSecond(), side);
  }

  private boolean isUnreliableTuple(final Tuple tuple, final SidePaths side) {
    final List<PathAnswer> pathAnswers = TupleFactory.getFDSidePathAnswers(this, tuple, side, false);
    for (PathAnswer pathAnswer : pathAnswers) {
      if (pathAnswer.isEmpty()) {
        continue;
      }
      if (!nodesMap.get(pathAnswer.getTupleNodeAnswer()).isReliable()) {
        return true;
      }
    }

    return false;
  }

  /**
   * Get DOM representation of this tree.
   * @return DOM representation of this tree.
   */
  public Document getDocument() {
    return document;
  }

  @Override
  public String toString() {
    try {
      final TransformerFactory transfac = TransformerFactory.newInstance();
      final Transformer trans = transfac.newTransformer();
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.setOutputProperty(OutputKeys.INDENT, "yes");

      //create string from xml tree
      final StringWriter sw = new StringWriter();
      final StreamResult result = new StreamResult(sw);
      final DOMSource source = new DOMSource(document);
      trans.transform(source, result);
      return sw.toString();
    } catch (TransformerException ex) {
      LOG.error(ex);
    }

    return null;
  }

  /**
   * Get list of paths defined for this tree.
   * @return List of paths defined for this tree.
   */
  public List<Path> getPaths() {
    return paths;
  }

  /**
   * Set paths defined for this tree.
   * @param paths Paths defined for this tree.
   */
  public void setPaths(final List<Path> paths) {
    this.paths = paths;
  }

  /**
   * Get string representation of paths defined for this tree.
   * @return String representation of paths defined for this tree.
   */
  public String pathsToString() {
    final StringBuilder builder = new StringBuilder();
    for (Path path : paths) {
      builder.append(path.getPathValue()).append("\n");
    }

    return builder.toString();
  }

  /**
   * Get path answer for provided path.
   * @param path Path to be the answer returned.
   * @return path answer for provided path.
   */
  public PathAnswer getPathAnswer(final Path path) {
    return getPathAnswerForTuple(path, null, false);
  }

  /**
   * Get path answer for provided tuple. This answer must contain max one node.
   * @param path Path to be answer returned.
   * @param tuple Tuple for which is returned the answer.
   * @return path answer for provided path.
   */
  public PathAnswer getPathAnswerForCreatingTuple(final Path path, final Tuple tuple) {
    return getGenericPathAnswerForTuple(path, tuple, true, false);
  }

  /**
   * Get path answer for provided tuple. This answer can contains more then one node.
   * @param path Path to be answer returned.
   * @param tuple Tuple for which is returned the answer.
   * @return path answer for provided path.
   */
  public PathAnswer getPathAnswerForTuple(final Path path, final Tuple tuple, final boolean isThesis) {
    return getGenericPathAnswerForTuple(path, tuple, false, isThesis);
  }

  private PathAnswer getGenericPathAnswerForTuple(final Path path, final Tuple tuple, final boolean isCreatingTuple, final boolean isThesis) {
    if (path != null) {
      final List<Node> result = new ArrayList<Node>();
      try {
        final NodeList nodeList = getNodeListForPath(path);

        if (tuple != null) {
          for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);
            final NodeAttribute nodeAttribute = nodesMap.get(node);
            if (nodeAttribute.isInTuple(tuple) && (!isThesis || (isThesis && nodeAttribute.isReliable()))) {
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

  /**
   * Get list of tuples associated with this tree.
   * @return List of tuples.
   */
  public List<Tuple> getTuples() {
    return tuples;
  }

  /**
   * Get new unique tuple ID. 
   * @return New unique tuple ID.
   */
  public int getNewTupleID() {
    return tupleID++;
  }

  /**
   * Get map of nodes of this tree and its attributes.
   * @return Map of nodes and attributes.
   */
  public Map<Node, NodeAttribute> getNodesMap() {
    return nodesMap;
  }

  /**
   * Apply provided repair to this tree.
   * @param repair Repair to be applied.
   */
  public void applyRepair(final Repair repair) {
    LOG.debug("Applying repair:");
    LOG.debug(repair.toString());

    for (Node node : repair.getUnreliableNodes()) {
      setNodeUnreliable(node);
    }

    for (Node node : repair.getValueNodes().keySet()) {
      final NodeValue nodeValue = repair.getValueNodes().get(node);
      String newValue = nodeValue.getChangedValue();
      if (nodeValue.isNewValue()) {
        newValue += newValueId++;
      }

      if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
        final Attr attribute = (Attr) node;
        attribute.setValue(newValue);
      } else if (node.getNodeType() == Node.TEXT_NODE) {
        final Text textnode = (Text) node;
        textnode.setNodeValue(newValue);
      }
    }

  }

  private void setNodeUnreliable(final Node node) {
    nodesMap.get(node).setReliability(false);

    if (node.getNodeType() == Node.ELEMENT_NODE) {
      final Element element = (Element) node;
      element.setAttribute("unreliable", "true");
    }
  }

  private static Map<Node, NodeAttribute> createNodesMap(final Document document) {
    final Map<Node, NodeAttribute> result = new HashMap<Node, NodeAttribute>();
    traverseTree(document.getDocumentElement(), result);

    return result;
  }

  private static void traverseTree(final Node node, final Map<Node, NodeAttribute> nodesMap) {
    if (!nodesMap.containsKey(node)) {
      nodesMap.put(node, new NodeAttribute());
    }
    final NodeAttribute nodeAttribute = nodesMap.get(node);
    nodeAttribute.setWeight(NODE_WEIGHT);

    //check attributes
    final NamedNodeMap attributes = node.getAttributes();
    if (attributes != null) {
      for (int j = 0; j < attributes.getLength(); j++) {
        traverseTree(attributes.item(j), nodesMap);
      }
    }

    if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
      final NodeList childNodes = node.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        final Node child = childNodes.item(i);
        traverseTree(child, nodesMap);
      }
    }
  }

  /**
   * Check if provided functional dependencies can be used with this tree.
   * @param functionalDependencies List of functional dependencies to be checked.
   * @return true if paths from which are functional dependecies created are defined for this tree.
   */
  public boolean isFDDefinedForTree(final List<FD> functionalDependencies) {
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

  /**
   * Associate repair group with this tree.
   * @param repairGroup Repair group to be associated with this tree.
   */
  public void addRepairGroup(final RepairGroup repairGroup) {
    repairGroups.add(repairGroup);
  }

  /**
   * Get list of all repair groups associated with this tree.
   * @return List of all associated repair groups.
   */
  public List<RepairGroup> getRepairGroups() {
    if (!repairGroups.isEmpty()) {
      Collections.sort(repairGroups, new Comparator<RepairGroup>() {

        @Override
        public int compare(final RepairGroup o1, final RepairGroup o2) {
          final double result = o1.getWeight() - o2.getWeight();
          if (result < 0) {
            return -1;
          }

          if (result > 0) {
            return 1;
          }
          return 0;
        }
      });
    }
    return repairGroups;
  }

  /**
   * Check if this tree is inconsistent agains provided functional dependencies.
   * @param functionalDependencies List of functional dependencies to be check inconsistency.
   * @return true if this tree is inconsistent against functional dependencies.
   * @throws InterruptedException if user interrupt repairing.
   */
  public boolean isInconsistent(final List<FD> functionalDependencies) throws InterruptedException {
    for (FD fd : functionalDependencies) {
      if (!isSatisfyingFDThesis(fd)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Get repair group with the lowest cost.
   * @return Repair group with the lowest cost.
   */
  public RepairGroup getMinimalRepairGroup() {
    if (!repairGroups.isEmpty()) {

      return getRepairGroups().get(0);
    }

    return null;
  }

  /**
   * Invalidate tuples which are no longer tuples, because of application of repairs.
   * Checks only tuples which are repaired by provided repair. If clearRG
   * flag is true, repair groups associated with this tree are cleared.
   * @param repair Repair for which are tuples checked.
   * @param clearRG flag defining if repair groups will be cleared.
   */
  public void clearRepairs(final RepairCandidate repair, final boolean clearRG) {
    final Set<Tuple> tuplesToCheck = new HashSet<Tuple>();

    for (Node node : repair.getUnreliableNodes()) {
      tuplesToCheck.addAll(TupleFactory.unmarkNodeFromAllTuples(this, node));
    }

    final Set<Tuple> tuplesToRemove = new HashSet<Tuple>();
    for (Tuple tuple : tuplesToCheck) {
      tupleSideAnswers.remove(tuple);
      if (!TupleFactory.isTuple(this, tuple)) {
        tuplesToRemove.add(tuple);
      }
    }

    TupleFactory.removeTuples(this, tuplesToRemove);
    if (clearRG) {
      repairGroups.clear();
    }
  }

  /**
   * Invalidate tuple of this tree.
   */
  public void removeTuple(final Tuple tuple) {
    tuples.remove(tuple);
  }

  /**
   * Set weights of nodes of this tree.
   * @param weights List of {@link Tweight} to be set.
   */
  public void setWeights(final List<Tweight> weights) {
    final XPath xPath = xpathFactory.newXPath();
    XPathExpression xPathExpression;

    for (Tweight weight : weights) {
      if (!weight.isInInterval()) {
        LOG.warn("The weight " + weight.getValue().doubleValue() + " is not in a range [0,1]. It will be ignored.");
        continue;
      }
      try {
        xPathExpression = xPath.compile(weight.getPath());
        final NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodeList.getLength(); i++) {
          final Node node = nodeList.item(i);
          nodesMap.get(node).setWeight(weight.getValue().doubleValue());
        }
      } catch (XPathExpressionException ex) {
        LOG.error("Path " + weight.getPath() + " cannot be compiled or evaluated.", ex);
      }

    }

  }

  /**
   * Save previous user selected repair candidate.
   * @param pickedRepair Repair candidate to be saved.
   */
  public void saveUserSelection(final RepairCandidate pickedRepair) {
    savedUserSelections.add(new UserNodeSelection(pickedRepair));
  }

  /**
   * Get list of all saved repair candidates by the user.
   * @return List of all saved repair candidates by the user.
   */
  public List<UserNodeSelection> getSavedUserSelections() {
    return savedUserSelections;
  }

  /**
   * Get threshold t associated with this tree.
   * @return Threshold t associated with this tree.
   */
  public double getThresholdT() {
    return thresholdT;
  }

  /**
   * Set threshold t to be associated with this tree.
   * @param thresholdT Threshold t to be associated with this tree.
   */
  public void setThresholdT(final double thresholdT) {
    this.thresholdT = thresholdT;
  }

  /**
   * Get list of all pairs created from all tuples associated with this tree.
   * @return List of all pairs created from all tuples associated with this tree.
   * @throws InterruptedException if user interrupted repairing.
   */
  public List<Pair<Tuple, Tuple>> getTuplePairs() throws InterruptedException {
    if (tuplePairs == null) {
      tuplePairs = TupleFactory.getTuplePairs(tuples);
    }

    return tuplePairs;
  }

  private List<PathAnswer> getTupleSideAnswer(final Tuple tuple, final boolean isLeft, final SidePaths sidePaths, final boolean isThesis) {
    if (tupleSideAnswers.containsKey(tuple)) {
      if (tupleSideAnswers.get(tuple).getSide(isLeft) == null) {
        final List<PathAnswer> fDSidePathAnswers = TupleFactory.getFDSidePathAnswers(this, tuple, sidePaths, isThesis);
        tupleSideAnswers.get(tuple).setSide(fDSidePathAnswers, isLeft);
      }
    } else {
      final List<PathAnswer> fDSidePathAnswers = TupleFactory.getFDSidePathAnswers(this, tuple, sidePaths, isThesis);

      final SideAnswers sideAnswers = new SideAnswers();
      sideAnswers.setSide(fDSidePathAnswers, isLeft);

      tupleSideAnswers.put(tuple, sideAnswers);
    }

    return tupleSideAnswers.get(tuple).getSide(isLeft);
  }

  private NodeList getNodeListForPath(final Path path) throws XPathExpressionException {
    if (!pathNodeListResults.containsKey(path)) {
      final XPath xPath = xpathFactory.newXPath();
      XPathExpression xPathExpression;

      xPathExpression = xPath.compile(path.getPathValue());
      final NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

      pathNodeListResults.put(path, nodeList);
    }

    return pathNodeListResults.get(path);
  }

  /**
   * Invalidate side answers for provided tuple. 
   * @param tuple Tuple for which side paths will be invalidated.
   */
  public void invalidateSidePathAnswers(final Tuple tuple) {
    tupleSideAnswers.remove(tuple);
  }

  /**
   * Remove provided repair group from all repair groups associated with this tree.
   * @param repairGroup Repair group to be removed. 
   */
  public void removeRG(final RepairGroup repairGroup) {
    repairGroups.remove(repairGroup);
  }

  /**
   * Check if there is any repair group associated with this tree.
   * @return true if there is no repair group associated with this tree.
   */
  public boolean isRGEmpty() {
    return repairGroups.isEmpty();
  }
}