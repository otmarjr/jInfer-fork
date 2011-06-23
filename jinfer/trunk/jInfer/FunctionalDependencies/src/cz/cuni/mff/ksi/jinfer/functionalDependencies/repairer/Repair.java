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
 *
 * @author sviro
 */
public class Repair implements Comparable<Repair> {
  
  public static final int COMPARE_SMALLER = -1;
  public static final int COMPARE_EQUAL = 0;
  public static final int COMPARE_GREATER = 1;
  public static final int COMPARE_UNAVAILABLE = 2;
  

  private Set<Node> unreliableNodes;
  private Map<Node, String> valueNodes;

  public Repair() {
    this.unreliableNodes = new HashSet<Node>();
    this.valueNodes = new HashMap<Node, String>();
  }

  public Repair(Node unreliableNode) {
    this();
    addUnreliableChildren(unreliableNode);
  }

  public Repair(Node valueNode, String changedValue) {
    this();
    valueNodes.put(valueNode, changedValue);
  }

  boolean hasReliabilityRepair() {
    return !unreliableNodes.isEmpty();
  }

  public Set<Node> getUnreliableNodes() {
    return unreliableNodes;
  }

  public Map<Node, String> getValueNodes() {
    return valueNodes;
  }

  public void addUnreliableNodes(Set<Node> nodes) {
    unreliableNodes.addAll(nodes);
  }

  boolean hasValueRepair() {
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

  void addUnreliableNode(Node node) {
    unreliableNodes.add(node);
  }

  void addValueNode(Node node, String value) {
    valueNodes.put(node, value);
  }

  private void addUnreliableChildren(Node unreliableNode) {
    unreliableNodes.add(unreliableNode);
    
    NamedNodeMap attributes = unreliableNode.getAttributes();
    if (attributes != null) {
      for (int i = 0; i < attributes.getLength(); i++) {
        addUnreliableChildren(attributes.item(i));
      }
    }
    
    NodeList childNodes = unreliableNode.getChildNodes();
    if (childNodes != null) {
      for (int i = 0; i < childNodes.getLength(); i++) {
        addUnreliableChildren(childNodes.item(i));
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
    return unreliableNodes;
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
    for (Node node : unreliableNodes) {
      builder.append("\t\t").append(node.toString()).append("\n");
    }
    builder.append("\t").append("Value nodes:\n");
    for (Node node : valueNodes.keySet()) {
      builder.append("\t\t").append("node: ").append(node.toString()).append(" value: ").append(valueNodes.get(node)).append("\n");
      
    }
    
    return builder.toString();
  }
  
  
}
