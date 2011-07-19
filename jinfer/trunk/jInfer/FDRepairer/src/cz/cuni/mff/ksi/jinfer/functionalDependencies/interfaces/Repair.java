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
 *
 * @author sviro
 */
public interface Repair {
  
  boolean hasReliabilityRepair();
  boolean hasValueRepair();
  
  Node getUnreliableNode();
  Set<Node> getUnreliableNodes();
  Map<Node, NodeValue> getValueNodes();
  
  void addUnreliableNode(Node node);
  void addUnreliableNodes(Set<Node> nodes);
  void addValueNode(Node node, NodeValue value);
  
  boolean isNewValue();
  boolean isNewValue(Node node);
}
