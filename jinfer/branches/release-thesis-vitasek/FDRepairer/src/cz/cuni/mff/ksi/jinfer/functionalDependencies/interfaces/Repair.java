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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.NodeValue;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Node;

/**
 * Interface representing Repair.
 * @author sviro
 */
public interface Repair {
  
  /**
   * Check if this repair contains reliability modification of the XML tree node.
   * @return true if contains reliability modification.
   */
  boolean hasReliabilityRepair();
  
  /**
   * Check ig this repair contains value modification of the XML tree node.
   * @return true if contains value modification.
   */
  boolean hasValueRepair();
  
  /**
   * Get the root node which was marked as unreliable by this repair. If this 
   * repair is not a reliability repair, null is returned.
   * @return The root node which is marked as unreliable.
   */
  Node getUnreliableNode();
  
  /**
   * Get set of all nodes marked as unreliable by this repair. If this repair
   * is not a reliability repair, empty set is returned.
   * @return Set of all nodes marked as unreliable.
   */
  Set<Node> getUnreliableNodes();
  
  /**
   * Get map of all nodes and theirs new values that have been modified by this repair.
   * If this repair is not a value repair, empty map is returned.
   * @return Map of all nodes and theirs new values.
   */
  Map<Node, NodeValue> getValueNodes();
  
  /**
   * Add node that will be marked as unreliable by this repair.
   * @param node Node to be marked as unreliable.
   */
  void addUnreliableNode(Node node);
  
  /**
   * Add set of nodes that will be marked as unreliable by this repair.
   * @param nodes Set of nodes to be marked as unreliable.
   */
  void addUnreliableNodes(Set<Node> nodes);
  
  /**
   * Add node and the new node value to this repair.
   * @param node Node to be changed by this repair.
   * @param value New value of the changed node.
   */
  void addValueNode(Node node, NodeValue value);
  
  /**
   * Check if provided node value is modified to the newly generated one/
   * @param node Node to be checked if its new value is generated.
   * @return true if node has newly generated value.
   */
  boolean isNewValue(Node node);
}
