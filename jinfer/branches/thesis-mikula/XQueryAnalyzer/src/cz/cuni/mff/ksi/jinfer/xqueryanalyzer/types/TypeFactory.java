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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer.types;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.*;

/**
 * TODO rio comment
 * @author rio
 */
public class TypeFactory {
  
  public static Type createType(final TypeNode typeNode) {
    final ItemTypeNode itemTypeNode = typeNode.getItemTypeNode();
    if (itemTypeNode == null) {
      return new UnknownType();
    } else {
      final Cardinality cardinality = typeNode.getCardinality();
      if (AtomicTypeNode.class.isInstance(itemTypeNode)) {
        final AtomicTypeNode itomicTypeNode = (AtomicTypeNode)itemTypeNode;
        final String typeName = itomicTypeNode.getTypeName();
        return new XSDType(typeName, cardinality);
      } else if (KindTestNode.class.isInstance(itemTypeNode)) {
        // TODO rio vymysliet, preskumat
        assert(false);
        return null;
      } else {
        // TODO rio ostavaju este 2 typy: AnyItemNode, NameTestNode - preskumat
        assert(false);
        return null;
      }
    }
  }
  
  public static Type createForBoundType(final Type forBindingExprType) {
    switch (forBindingExprType.getCategory()) {
      case UNKNOWN:
        return new UnknownType();
        
      case PATH:
        return new ForBoundPathType((PathType)forBindingExprType);
      
      case BUILT_IN:
        assert(((XSDType)forBindingExprType).getCardinality() != Cardinality.ONE);
        assert(((XSDType)forBindingExprType).getCardinality() != Cardinality.ZERO);
        final XSDType xsdType = (XSDType)forBindingExprType;
        final XSDType.XSDAtomicType atomicType = xsdType.getAtomicType();
        return new XSDType(atomicType, Cardinality.ONE);
        
      default:
        throw new IllegalStateException(); // TODO rio
    }
  }
  
}
