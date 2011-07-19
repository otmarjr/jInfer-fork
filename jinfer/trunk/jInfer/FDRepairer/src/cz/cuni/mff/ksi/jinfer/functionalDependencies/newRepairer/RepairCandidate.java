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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.NodeValue;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.Tuple;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repair;
import java.io.StringWriter;
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
public class RepairCandidate implements Repair {

  private static final Logger LOG = Logger.getLogger(RepairCandidate.class);
  private Node unreliableNode = null;
  private Set<Node> unreliableNodes = null;
  private Map<Node, NodeValue> valueNodes;
  private double weight = -1;
  private RepairGroup repairGroup;
  private RXMLTree tree;
  private double coeffK;
  private Set<String> nodePaths;
  private boolean isNewValue;
  private Pair<Tuple, Tuple> tuplePair;

  public RepairCandidate(final Pair<Tuple, Tuple> tuplePair) {
    this.tuplePair = tuplePair;
    this.valueNodes = new HashMap<Node, NodeValue>();
    this.nodePaths = new HashSet<String>();
  }

  public RepairCandidate(final Pair<Tuple, Tuple> tuplePair, final Node unreliableNode, final RXMLTree tree, final double coeffK, final String path) {
    this(tuplePair);
    this.unreliableNode = unreliableNode;
    this.tree = tree;
    this.coeffK = coeffK;
    nodePaths.add(path);
  }

  public RepairCandidate(final Pair<Tuple, Tuple> tuplePair, final Node valueNode, final String changedValue, final RXMLTree tree, final double coeffK, final String path, boolean isNewValue) {
    this(tuplePair);
    valueNodes.put(valueNode, new NodeValue(changedValue, isNewValue));
    this.tree = tree;
    this.coeffK = coeffK;
    this.isNewValue = isNewValue;
    nodePaths.add(path);
  }

  @Override
  public boolean hasReliabilityRepair() {
    return !hasValueRepair();
  }

  @Override
  public Set<Node> getUnreliableNodes() {
    if (unreliableNodes == null) {
      unreliableNodes = new HashSet<Node>();
      addUnreliableChildren(unreliableNode, nodePaths, nodePaths.iterator().next());
    }
    return unreliableNodes;
  }

  @Override
  public Node getUnreliableNode() {
    return unreliableNode;
  }

  @Override
  public Map<Node, NodeValue> getValueNodes() {
    return valueNodes;
  }

  @Override
  public void addUnreliableNodes(Set<Node> nodes) {
    getUnreliableNodes().addAll(nodes);
  }

  @Override
  public boolean hasValueRepair() {
    return !valueNodes.isEmpty();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Repair)) {
      return false;
    }

    RepairCandidate repair = (RepairCandidate) obj;

    return this.getUnreliableNodes().equals(repair.getUnreliableNodes()) && this.getValueNodes().equals(repair.getValueNodes());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + (this.unreliableNodes != null ? this.unreliableNodes.hashCode() : 0);
    hash = 59 * hash + (this.valueNodes != null ? this.valueNodes.hashCode() : 0);
    return hash;
  }

  @Override
  public void addUnreliableNode(Node node) {
    getUnreliableNodes().add(node);
  }

  @Override
  public void addValueNode(Node node, NodeValue value) {
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
      builder.append("\t\t").append("node: ").append(node.toString()).append(" value: ").append(valueNodes.get(node).getChangedValue()).append("\n");

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
          setNewValue(cloneNode, valueNodes.get(node).getChangedValue(), true, node.getNodeName());
          builder.append(transformNodeToXML(cloneNode)).append("\n");
        } else {
          Node cloneNode = ((Text) node).getParentNode().cloneNode(true);

          builder.append(transformNodeToXML(cloneNode)).append(" -> ");
          setNewValue(cloneNode, valueNodes.get(node).getChangedValue(), false, null);
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

  @Override
  public boolean isNewValue() {
    return isNewValue;
  }

  @Override
  public boolean isNewValue(Node node) {
    if (!valueNodes.containsKey(node)) {
      throw new IllegalArgumentException("The node " + node + "is not associated any value");
    }
    return valueNodes.get(node).isNewValue();
  }

  public Pair<Tuple, Tuple> getTuplePair() {
    return tuplePair;
  }
  
}
