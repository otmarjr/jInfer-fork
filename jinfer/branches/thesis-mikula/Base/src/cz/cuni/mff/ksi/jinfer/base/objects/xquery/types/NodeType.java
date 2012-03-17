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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.types;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.CardinalityType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Cardinality;

/**
 * A representation of so called node type. For possible categories of node types,
 * see {@link NodeTypeCategory}
 * @author rio
 */
public class NodeType extends CardinalityType {
  
  public enum NodeTypeCategory {
    ANY_KIND,
    ATTRIBUTE,
    COMMENT,
    DOCUMENT,
    ELEMENT,
    PROCESSING_INSTRUCTION,
    SCHEMA_ATTRIBUTE,
    SCHEMA_ELEMENT,
    TEXT; 
  }
  
  private final NodeTypeCategory nodeTypeCategory;
  
  public NodeType(final NodeTypeCategory nodeTypeCategory, final Cardinality cardinality) {
    super(cardinality);
    this.nodeTypeCategory = nodeTypeCategory;
  }

  @Override
  public Category getCategory() {
    return Category.NODE;
  }
  
  @Override
  public boolean isNodeType() {
    return true;
  }
  
  public NodeTypeCategory getNodeTypeCategory() {
    return nodeTypeCategory;
  }
  
}
