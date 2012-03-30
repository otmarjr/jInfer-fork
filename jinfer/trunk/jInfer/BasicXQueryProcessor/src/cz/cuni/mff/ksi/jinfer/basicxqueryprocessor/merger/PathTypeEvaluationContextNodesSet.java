/*
 * Copyright (C) 2012 rio
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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.merger;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A data structure holding two sets. A set of nodes and a set attributes.
 * 
 * Its purpose is to represent a context set in evaluation of a path upon a grammar.
 * 
 * @author rio
 */
public class PathTypeEvaluationContextNodesSet {
  
  private final List<AbstractStructuralNode> nodes = new ArrayList<AbstractStructuralNode>();
  private final List<Attribute> attributes = new ArrayList<Attribute>();
  
  public void addNode(final AbstractStructuralNode node) {
    nodes.add(node);
  }
  
  public void addAttribute(final Attribute att) {
    attributes.add(att);
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public List<AbstractStructuralNode> getNodes() {
    return nodes;
  }
  
  public void addNodes(final Collection<AbstractStructuralNode> nodes) {
    this.nodes.addAll(nodes);
  }
  
  public void addAttributes(final Collection<Attribute> attributes) {
    this.attributes.addAll(attributes);
  }
  
}
