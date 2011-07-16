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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer.RepairGroup;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer.UserNodeSelection;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
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
public class Repair implements Comparable<Repair> {

  private static final Logger LOG = Logger.getLogger(Repair.class);
  public static final int COMPARE_SMALLER = -1;
  public static final int COMPARE_EQUAL = 0;
  public static final int COMPARE_GREATER = 1;
  public static final int COMPARE_UNAVAILABLE = 2;
  private Node unreliableNode = null;
  private Set<Node> unreliableNodes = null;
  private Map<Node, String> valueNodes;
  private double weight = -1;
  private RepairGroup repairGroup;
  private RXMLTree tree;
  private double coeffK;
  private Set<String> nodePaths;

  public Repair() {
    this.valueNodes = new HashMap<Node, String>();
    this.nodePaths = new HashSet<String>();
  }

  public Repair(final Node unreliableNode) {
    this();
    //TODO sviro check for null
    this.unreliableNode = unreliableNode;
  }

  public Repair(final Node valueNode, final String changedValue) {
    this();
    //TODO sviro check for null
    valueNodes.put(valueNode, changedValue);
  }

  public Repair(final Node unreliableNode, final RXMLTree tree, final double coeffK, final String path) {
    this(unreliableNode);
    this.tree = tree;
    this.coeffK = coeffK;
    nodePaths.add(path);
  }

  public Repair(final Node valueNode, final String changedValue, final RXMLTree tree, final double coeffK, final String path) {
    this(valueNode, changedValue);
    this.tree = tree;
    this.coeffK = coeffK;
    nodePaths.add(path);
  }

  public boolean hasReliabilityRepair() {
    return unreliableNode != null;
  }

  public Set<Node> getUnreliableNodes() {
    if (unreliableNodes == null) {
      unreliableNodes = new HashSet<Node>();
      addUnreliableChildren(unreliableNode, nodePaths, nodePaths.iterator().next());
    }
    return unreliableNodes;
  }

  public Node getUnreliableNode() {
    return unreliableNode;
  }

  public Map<Node, String> getValueNodes() {
    return valueNodes;
  }

  public void addUnreliableNodes(Set<Node> nodes) {
    getUnreliableNodes().addAll(nodes);
  }

  public boolean hasValueRepair() {
    return !valueNodes.isEmpty();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Repair)) {
      return false;
    }

    Repair repair = (Repair) obj;

    return this.getUnreliableNodes().equals(repair.getUnreliableNodes()) && this.getValueNodes().equals(repair.getValueNodes());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + (this.unreliableNodes != null ? this.unreliableNodes.hashCode() : 0);
    hash = 59 * hash + (this.valueNodes != null ? this.valueNodes.hashCode() : 0);
    return hash;
  }

  public void addUnreliableNode(Node node) {
    getUnreliableNodes().add(node);
  }

  public void addValueNode(Node node, String value) {
    valueNodes.put(node, value);
  }

  private void addUnreliableChildren(Node unreliableNode, Set<String> paths, String parentPath) {
    if (unreliableNode != null) {
      unreliableNodes.add(unreliableNode);
      String newParentPath = parentPath;
      
      if (this.unreliableNode != unreliableNode) {
        StringBuilder builder = new StringBuilder();
        builder.append(parentPath).append("/");
        if (unreliableNode.getNodeType() == Node.ATTRIBUTE_NODE) {
          builder.append("@");
        }

        builder.append(unreliableNode.getNodeName());

        newParentPath = builder.toString();
        paths.add(newParentPath);
      }

      NamedNodeMap attributes = unreliableNode.getAttributes();
      if (attributes != null) {
        for (int i = 0; i < attributes.getLength(); i++) {
          addUnreliableChildren(attributes.item(i), paths, newParentPath);
        }
      }
      if (unreliableNode.getNodeType() != Node.ATTRIBUTE_NODE) {
        NodeList childNodes = unreliableNode.getChildNodes();
        if (childNodes != null) {
          for (int i = 0; i < childNodes.getLength(); i++) {
            addUnreliableChildren(childNodes.item(i), paths, newParentPath);
          }
        }
      }
    }
  }

  /**
   * Compare this Repair with repair in parameter and returns -1, 0, 1 or 2 if
   * this repair is smaller, equal, greater or unable to comapre with another repair.
   * @param repair The repair with compare to.
   * @return 
   */
  @Override
  public int compareTo(Repair repair) {
    if (isSmaller(this, repair) && isSmaller(repair, this)) {
      return 0;
    }

    if (isSmaller(this, repair)) {
      return -1;
    }

    if (isSmaller(repair, this)) {
      return 1;
    }

    return 2;
  }

  /**
   * 
   * @param modified1
   * @param modified2
   * @param false1
   * @param false2
   * @return 
   */
  private static boolean isSmaller(Repair repair1, Repair repair2) {
    Collection<Node> modifiedNodes1 = repair1.getModifiedNodes();
    Collection<Node> modifiedNodes2 = repair2.getModifiedNodes();
    Collection<Node> falseNodes1 = repair1.getFalseNodes();
    Collection<Node> falseNodes2 = repair2.getFalseNodes();

    return isSubset(modifiedNodes1, modifiedNodes2) && isSubset(falseNodes1, falseNodes2);
  }

  /**
   * Determines if set1 is subset of set2.
   * @param set1
   * @param set2
   * @return true if set1 is subset of set2, otherwise return false.
   */
  private static boolean isSubset(Collection<Node> set1, Collection<Node> set2) {
    return set2.containsAll(set1);
  }

  public Collection<Node> getUpdatedNodes() {
    return valueNodes.keySet();
  }

  public Collection<Node> getFalseNodes() {
    return getUnreliableNodes();
  }

  public Collection<Node> getModifiedNodes() {
    Collection<Node> result = new ArrayList<Node>(getUpdatedNodes().size() + getFalseNodes().size());

    result.addAll(getUpdatedNodes());
    result.addAll(getFalseNodes());

    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append("Repair:\n");
    builder.append("\t").append("Unreliable nodes:\n");
    for (Node node : getUnreliableNodes()) {
      builder.append("\t\t").append(node.toString()).append("\n");
    }
    builder.append("\t").append("Value nodes:\n");
    for (Node node : valueNodes.keySet()) {
      builder.append("\t\t").append("node: ").append(node.toString()).append(" value: ").append(valueNodes.get(node)).append("\n");

    }

    return builder.toString();
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public double getWeight() {
    if (weight == -1) {
      weight = computeWeight();
    }
    return weight;
  }

  public void addToRepairGroup(final RepairGroup repairGroup) {
    this.repairGroup = repairGroup;
  }

  private double computeWeight() {
    double result = 0d;
    for (Node node : getUnreliableNodes()) {
      result += tree.getNodesMap().get(node).getWeight() * coeffK;
    }

    for (Node node : valueNodes.keySet()) {
      result += tree.getNodesMap().get(node).getWeight();
    }

    return result;
  }

  public String getContentAfterRepair() throws InterruptedException {
    if (hasReliabilityRepair()) {
      Node cloneNode = getUnreliableNode().cloneNode(true);
      setNodeUnreliable(cloneNode);

      return transformNodeToXML(cloneNode);
    } else {
      StringBuilder builder = new StringBuilder();
      for (Node node : valueNodes.keySet()) {
        if (node instanceof Attr) {
          Node cloneNode = ((Attr) node).getOwnerElement().cloneNode(true);
          builder.append(transformNodeToXML(cloneNode)).append(" -> ");
          setNewValue(cloneNode, valueNodes.get(node), true, node.getNodeName());
          builder.append(transformNodeToXML(cloneNode)).append("\n");
        } else {
          Node cloneNode = ((Text) node).getParentNode().cloneNode(true);

          builder.append(transformNodeToXML(cloneNode)).append(" -> ");
          setNewValue(cloneNode, valueNodes.get(node), false, null);
          builder.append(transformNodeToXML(cloneNode)).append("\n");
        }
      }

      return builder.toString();
    }
  }

  private void setNodeUnreliable(Node node) {
    if (node instanceof Element) {
      Element element = (Element) node;
      element.setAttribute("unreliable", "true");

      NodeList childNodes = element.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        setNodeUnreliable(childNodes.item(i));
      }
    }
  }

  private String transformNodeToXML(final Node node) {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = builder.newDocument();
      document.adoptNode(node);


      document.appendChild(node);

      TransformerFactory transfac = TransformerFactory.newInstance();
      transfac.setAttribute("indent-number", new Integer(4));
      Transformer trans = transfac.newTransformer();
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.setOutputProperty(OutputKeys.INDENT, "yes");
      trans.setOutputProperty(OutputKeys.METHOD, "xml");

      //create string from xml tree
      StringWriter sw = new StringWriter();
      StreamResult result = new StreamResult(sw);
      DOMSource source = new DOMSource(document);
      trans.transform(source, result);
      return sw.toString();
    } catch (ParserConfigurationException ex) {
      LOG.error(ex);
      throw new RuntimeException("Something goes wrong while transforming tree into XML.");
    } catch (TransformerException ex) {
      LOG.error(ex);
      throw new RuntimeException("Something goes wrong while transforming tree into XML.");
    }
  }

  private void setNewValue(Node node, String newValue, boolean isAttr, String attributeName) throws InterruptedException {
    if (!(node instanceof Element)) {
      LOG.error("Node must be element.");
      throw new InterruptedException("node must be element.");
    }

    if (isAttr) {
      Attr attribute = ((Element) node).getAttributeNode(attributeName);
      attribute.setValue(newValue);
    } else {
      NodeList childNodes = node.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node child = childNodes.item(i);
        if (child.getNodeType() == Node.TEXT_NODE) {
          Text textnode = (Text) child;
          textnode.setNodeValue(newValue);
          return;
        }
      }
    }
  }

  public Collection<String> getNodePaths() {
    return nodePaths;
  }

  public FD getFD() {
    return repairGroup.getFunctionalDependency();
  }

  public int getNodeSize() {
    return nodePaths.size();
  }

  public boolean hasSubsetPaths(UserNodeSelection userSelection) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  
}
