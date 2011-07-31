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
 * Implementation of the Repair interface for the thesis algorithm. This class
 * represents repair candidate.
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
  private Pair<Tuple, Tuple> tuplePair;

  /**
   * Constructor of the repair candidate. Provided argument represents tuple pair
   * for which is this candidate created.
   * @param tuplePair Tuple pair for which is this candidate created.
   */
  public RepairCandidate(final Pair<Tuple, Tuple> tuplePair) {
    this.tuplePair = tuplePair;
    this.valueNodes = new HashMap<Node, NodeValue>();
    this.nodePaths = new HashSet<String>();
  }

  /**
   * This constructor creates a repair candidate which marks provided node as unreliable. 
   * 
   * @param tuplePair Tuple pair for which is this candidate created.
   * @param unreliableNode Node to be marked as unreliable.
   * @param tree Tree for which is candidate created.
   * @param coeffK coefficient k from thesis algorithm.
   * @param path Path of the modified node for user selection purpose.
   */
  public RepairCandidate(final Pair<Tuple, Tuple> tuplePair, final Node unreliableNode, final RXMLTree tree, final double coeffK, final String path) {
    this(tuplePair);
    this.unreliableNode = unreliableNode;
    this.tree = tree;
    this.coeffK = coeffK;
    nodePaths.add(path);
  }

  /**
   * This constructor creates a repair candidate which modifies node value. 
   * @param tuplePair Tuple pair for which is this candidate created.
   * @param valueNode Node to be modified.
   * @param changedValue New value of the modified node.
   * @param tree Tree for which is candidate created.
   * @param coeffK coefficient k from thesis algorithm.
   * @param path Path of the modified node for user selection purpose.
   * @param isNewValue flag indicating if the new value is generated.
   */
  public RepairCandidate(final Pair<Tuple, Tuple> tuplePair, final Node valueNode, final String changedValue, final RXMLTree tree, final double coeffK, final String path, final boolean isNewValue) {
    this(tuplePair);
    valueNodes.put(valueNode, new NodeValue(changedValue, isNewValue));
    this.tree = tree;
    this.coeffK = coeffK;
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
  public void addUnreliableNodes(final Set<Node> nodes) {
    getUnreliableNodes().addAll(nodes);
  }

  @Override
  public boolean hasValueRepair() {
    return !valueNodes.isEmpty();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof Repair)) {
      return false;
    }

    final RepairCandidate repair = (RepairCandidate) obj;

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
  public void addUnreliableNode(final Node node) {
    getUnreliableNodes().add(node);
  }

  @Override
  public void addValueNode(final Node node, final NodeValue value) {
    valueNodes.put(node, value);
  }

  private void addUnreliableChildren(final Node unreliableNode, final Set<String> paths, final String parentPath) {
    if (unreliableNode != null) {
      unreliableNodes.add(unreliableNode);
      String newParentPath = parentPath;

      if (this.unreliableNode != unreliableNode) {
        final StringBuilder builder = new StringBuilder();
        builder.append(parentPath).append("/");
        if (unreliableNode.getNodeType() == Node.ATTRIBUTE_NODE) {
          builder.append("@");
        }

        builder.append(unreliableNode.getNodeName());

        newParentPath = builder.toString();
        paths.add(newParentPath);
      }

      final NamedNodeMap attributes = unreliableNode.getAttributes();
      if (attributes != null) {
        for (int i = 0; i < attributes.getLength(); i++) {
          addUnreliableChildren(attributes.item(i), paths, newParentPath);
        }
      }
      if (unreliableNode.getNodeType() != Node.ATTRIBUTE_NODE) {
        final NodeList childNodes = unreliableNode.getChildNodes();
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
    final StringBuilder builder = new StringBuilder();

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

  /**
   * Set weight of the repair candidate.
   * @param weight Weight to be set.
   */
  public void setWeight(final double weight) {
    this.weight = weight;
  }

  /**
   * Get weight of the repair candidate.
   * @return Weight of the repair candidate.
   */
  public double getWeight() {
    if (weight == -1) {
      weight = computeWeight();
    }
    return weight;
  }

  /**
   * Adds this candidate to the provided repair group.
   * @param repairGroup Repair group to be added this candidate.
   */
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

  /**
   * Get the string representation of the modified part of the tree by this repair.
   * @return String representation of the modified part.
   * @throws InterruptedException 
   */
  public String getContentAfterRepair() throws InterruptedException {
    if (hasReliabilityRepair()) {
      final Node cloneNode = getUnreliableNode().cloneNode(true);
      setNodeUnreliable(cloneNode);

      return transformNodeToXML(cloneNode);
    } else {
      final StringBuilder builder = new StringBuilder();
      for (Node node : valueNodes.keySet()) {
        if (node instanceof Attr) {
          final Node cloneNode = ((Attr) node).getOwnerElement().cloneNode(true);
          builder.append(transformNodeToXML(cloneNode)).append(" -> ");
          setNewValue(cloneNode, valueNodes.get(node).getChangedValue(), true, node.getNodeName());
          builder.append(transformNodeToXML(cloneNode)).append("\n");
        } else {
          final Node cloneNode = ((Text) node).getParentNode().cloneNode(true);

          builder.append(transformNodeToXML(cloneNode)).append(" -> ");
          setNewValue(cloneNode, valueNodes.get(node).getChangedValue(), false, null);
          builder.append(transformNodeToXML(cloneNode)).append("\n");
        }
      }

      return builder.toString();
    }
  }

  private void setNodeUnreliable(final Node node) {
    if (node instanceof Element) {
      final Element element = (Element) node;
      element.setAttribute("unreliable", "true");

      final NodeList childNodes = element.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        setNodeUnreliable(childNodes.item(i));
      }
    }
  }

  private String transformNodeToXML(final Node node) {
    try {
      final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      final Document document = builder.newDocument();
      document.adoptNode(node);


      document.appendChild(node);

      final TransformerFactory transfac = TransformerFactory.newInstance();
      transfac.setAttribute("indent-number", Integer.valueOf(4));
      final Transformer trans = transfac.newTransformer();
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.setOutputProperty(OutputKeys.INDENT, "yes");
      trans.setOutputProperty(OutputKeys.METHOD, "xml");

      //create string from xml tree
      final StringWriter sw = new StringWriter();
      final StreamResult result = new StreamResult(sw);
      final DOMSource source = new DOMSource(document);
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

  private void setNewValue(final Node node, final String newValue, final boolean isAttr, final String attributeName) throws InterruptedException {
    if (!(node instanceof Element)) {
      LOG.error("Node must be element.");
      throw new InterruptedException("node must be element.");
    }

    if (isAttr) {
      final Attr attribute = ((Element) node).getAttributeNode(attributeName);
      attribute.setValue(newValue);
    } else {
      final NodeList childNodes = node.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        final Node child = childNodes.item(i);
        if (child.getNodeType() == Node.TEXT_NODE) {
          final Text textnode = (Text) child;
          textnode.setNodeValue(newValue);
          return;
        }
      }
    }
  }

  /**
   * Get paths of all nodes that are modified by this candidate.
   * @return Collection of paths of all nodes modified by this candidate.
   */
  public Collection<String> getNodePaths() {
    return nodePaths;
  }

  /**
   * Get functional dependency this candidate is repairing validation for.
   * @return Functional dependency this candidate is repairing validation for.
   */
  public FD getFD() {
    return repairGroup.getFunctionalDependency();
  }

  /**
   * Get number of nodes this repair is modifying.
   * @return Number of nodes this repair is modifying.
   */
  public int getNodeSize() {
    return nodePaths.size();
  }

  @Override
  public boolean isNewValue(final Node node) {
    if (!valueNodes.containsKey(node)) {
      throw new IllegalArgumentException("The node " + node + "is not associated any value");
    }
    return valueNodes.get(node).isNewValue();
  }

  /**
   * Get tuple pair this candidate is defined for.
   * @return Tuple pair this candidate is defined for.
   */
  public Pair<Tuple, Tuple> getTuplePair() {
    return tuplePair;
  }
  
}
