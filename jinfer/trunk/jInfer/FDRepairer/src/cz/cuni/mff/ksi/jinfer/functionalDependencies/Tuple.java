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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;

/**
 *
 * @author sviro
 */
public class Tuple {
  private final int tupleId;
  private final RXMLTree tree;
  private List<Node> nodes = null;

  public Tuple(RXMLTree tree, final int tupleId) {
    this.tree = tree;
    this.tupleId = tupleId;
  }

  public int getId() {
    return tupleId;
  }

  public boolean contains(Tuple tuple) {
    List<Node> tupleNodes = tuple.getNodes();
    if (this.getNodes().containsAll(tupleNodes)) {
      return true;
    }
    
    return false;
  }

  public List<Node> getNodes() {
    if (nodes == null) {
      nodes = computeNodes();
    }
    return nodes;
  }

  private List<Node> computeNodes() {
    List<Node> result = new ArrayList<Node>();
    
    Map<Node, NodeAttribute> nodesMap = tree.getNodesMap();
    for (Node node : nodesMap.keySet()) {
      if (nodesMap.get(node).isInTuple(this)) {
        result.add(node);
      }
    }
    
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Tuple)) {
      return false;
    }
    
    Tuple tuple = (Tuple) obj;
    
    return this.getId() == tuple.getId();
  }

  @Override
  public String toString() {
    return "Tuple:" + tupleId;
  }
  
  
  
}
