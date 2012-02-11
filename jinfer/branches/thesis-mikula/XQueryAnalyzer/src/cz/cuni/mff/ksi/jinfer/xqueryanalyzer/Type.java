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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.*;

/**
 * TODO rio
 * @author rio
 */
public class Type {
  
  public enum Category {
    BUILT_IN,
    NODE,
    PATH;
  }
  
  private final Category category;
  private final Cardinality cardinality;
  
  private String buildinTypeName;
  
  public Type(final String buildinTypeName, final Cardinality cardinality) {
    category = Category.BUILT_IN;
    this.buildinTypeName = buildinTypeName;
    this.cardinality = cardinality;
  }
  
  public Type(final TypeNode typeNode) {
    cardinality = typeNode.getCardinality();
    
    final ItemTypeNode itemTypeNode = typeNode.getItemTypeNode();
    if (itemTypeNode == null) {
      // TODO rio what is this type?
      category = Category.BUILT_IN;
      buildinTypeName = "xs:anyType";
    } else {
      if (itemTypeNode.getClass().equals(AtomicTypeNode.class)) {
        category = Category.BUILT_IN;
        final AtomicTypeNode itomicTypeNode = (AtomicTypeNode)itemTypeNode;
        buildinTypeName = itomicTypeNode.getTypeName();
      } else if (itemTypeNode.getClass().equals(KindTestNode.class)) {
        // TODO rio vymysliet, preskumat
        category = Category.NODE;
      } else {
        // TODO rio ostavaju este 2 typy: AnyItemNode, NameTestNode - preskumat
        category = Category.BUILT_IN;
      }
    }
    
  }
}
