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

import cz.cuni.mff.ksi.jinfer.functionalDependencies.NodeValue;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementation of the Repair interface for the original algorithm.
 * @author sviro
 */
public class RepairImpl implements Repair, Comparable<RepairImpl> {

  public static final int COMPARE_SMALLER = -1;
  public static final int COMPARE_EQUAL = 0;
  public static final int COMPARE_GREATER = 1;
  public static final int COMPARE_UNAVAILABLE = 2;
  private Node unreliableNode = null;
  private Set<Node> unreliableNodes = null;
  private Map<Node, NodeValue> valueNodes;
  private Collection<Node> modifiedNodes = null;

  /**
   * Default constructor.
   */
  public RepairImpl() {
    this.valueNodes = new HashMap<Node, NodeValue>();
  }

  /**
   * Constructor creating repair that mark node as unreliable.
   * @param unreliableNode Node to be marked as unreliable.
   */
  public RepairImpl(final Node unreliableNode) {
    this();
    //TODO sviro check for null
    this.unreliableNode = unreliableNode;
  }

  /**
   * Constructor creating a repair that modifies node value.
   * @param valueNode Node to be modified.
   * @param changedValue New value of the node.
   * @param isNewValue flag indicating that the value is newly generated.
   */
  public RepairImpl(final Node valueNode, final String changedValue, final boolean isNewValue) {
    this();
    //TODO sviro check for null
    valueNodes.put(valueNode, new NodeValue(changedValue, isNewValue));
  }

  @Override
  public boolean hasReliabilityRepair() {
    return !hasValueRepair();
  }

  @Override
  public Set<Node> getUnreliableNodes() {
    if (unreliableNodes == null) {
      unreliableNodes = new HashSet<Node>();
      addUnreliableChildren(unreliableNode);
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
    if (obj == null || !(obj instanceof RepairImpl)) {
      return false;
    }

    final RepairImpl repair = (RepairImpl) obj;

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

  private void addUnreliableChildren(final Node unreliableNode) {
    if (unreliableNode != null) {
      unreliableNodes.add(unreliableNode);

      final NamedNodeMap attributes = unreliableNode.getAttributes();
      if (attributes != null) {
        for (int i = 0; i < attributes.getLength(); i++) {
          addUnreliableChildren(attributes.item(i));
        }
      }
      if (unreliableNode.getNodeType() != Node.ATTRIBUTE_NODE) {
        final NodeList childNodes = unreliableNode.getChildNodes();
        if (childNodes != null) {
          for (int i = 0; i < childNodes.getLength(); i++) {
            addUnreliableChildren(childNodes.item(i));
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
  public int compareTo(final RepairImpl repair) {
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
  private static boolean isSmaller(final RepairImpl repair1, final RepairImpl repair2) {
    final Collection<Node> modifiedNodes1 = repair1.getModifiedNodes();
    final Collection<Node> modifiedNodes2 = repair2.getModifiedNodes();
    final Collection<Node> falseNodes1 = repair1.getFalseNodes();
    final Collection<Node> falseNodes2 = repair2.getFalseNodes();

    return isSubset(modifiedNodes1, modifiedNodes2) && isSubset(falseNodes1, falseNodes2);
  }

  /**
   * Determines if set1 is subset of set2.
   * @param set1
   * @param set2
   * @return true if set1 is subset of set2, otherwise return false.
   */
  private static boolean isSubset(final Collection<Node> set1, final Collection<Node> set2) {
    return set2.containsAll(set1);
  }

  /**
   * Get all nodes that the value is changed.
   * @return Collection of all nodes that the value is changed. 
   */
  public Collection<Node> getUpdatedNodes() {
    return valueNodes.keySet();
  }

  /**
   * Get all nodes that have been marked as unreliable.
   * @return Collection of all nodes that have been marked as unreliable. 
   */
  public Collection<Node> getFalseNodes() {
    return getUnreliableNodes();
  }

  /**
   * Get all nodes that have been marked as unreliable and the value have been changed.
   * @return Collection of all nodes that have been marked as unreliable and the value have been changed. 
   */
  public Collection<Node> getModifiedNodes() {
    if (modifiedNodes == null) {
      modifiedNodes = new ArrayList<Node>(getUpdatedNodes().size() + getFalseNodes().size());
      modifiedNodes.addAll(getUpdatedNodes());
      modifiedNodes.addAll(getFalseNodes());
    }

    return modifiedNodes;
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

  @Override
  public boolean isNewValue(final Node node) {
    if (!valueNodes.containsKey(node)) {
      throw new IllegalArgumentException("The node " + node + "is not associated any value");
    }
    return valueNodes.get(node).isNewValue();
  }
  
  
  
}
