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
  
  public enum XSDAtomicType {
    FLOAT,
    DOUBLE,
    DECIMAL,
    INTEGER,
    LONG,
    INT,
    SHORT,
    BYTE,
    NON_POSITIVE_INTEGER,
    NEGATIVE_INTEGER,
    NON_NEGATIVE_INTEGER,
    POSITIVE_INTEGER,
    UNSIGNED_LONG,
    UNSIGNED_BYTE,
    STRING,
    BOOLEAN,
    ANY_TYPE; // TODO rio toto existuje?
    // TODO rio doplnit
  }
  
  private final Category category;
  private final Cardinality cardinality;
  
  private XSDAtomicType buildinTypeName;
  
  public Type(final XSDAtomicType buildinTypeName, final Cardinality cardinality) {
    category = Category.BUILT_IN;
    this.buildinTypeName = buildinTypeName;
    this.cardinality = cardinality;
  }
  
  public Type(final LiteralType literalType) {
    category = Category.BUILT_IN;
    cardinality = Cardinality.ONE;
    switch(literalType) {
      case DECIMAL:
        buildinTypeName = XSDAtomicType.DECIMAL;
        break;
      case DOUBLE:
        buildinTypeName = XSDAtomicType.DOUBLE;
        break;
      case INTEGER:
        buildinTypeName = XSDAtomicType.INT;
        break;
      case STRING:
        buildinTypeName = XSDAtomicType.STRING;
    }
  }
  
  public Type(final TypeNode typeNode) {
    cardinality = typeNode.getCardinality();
    
    final ItemTypeNode itemTypeNode = typeNode.getItemTypeNode();
    if (itemTypeNode == null) {
      // TODO rio what is this type?
      category = Category.BUILT_IN;
      buildinTypeName = XSDAtomicType.ANY_TYPE;
      assert(false);
    } else {
      if (itemTypeNode.getClass().equals(AtomicTypeNode.class)) {
        category = Category.BUILT_IN;
        final AtomicTypeNode itomicTypeNode = (AtomicTypeNode)itemTypeNode;
        buildinTypeName = Type.atomicNameToType(itomicTypeNode.getTypeName());
      } else if (itemTypeNode.getClass().equals(KindTestNode.class)) {
        // TODO rio vymysliet, preskumat
        category = Category.NODE;
        assert(false);
      } else {
        // TODO rio ostavaju este 2 typy: AnyItemNode, NameTestNode - preskumat
        category = Category.BUILT_IN;
        assert(false);
      }
    } 
  }
  
  private static XSDAtomicType atomicNameToType(final String atomicTypeName) {
    if (atomicTypeName.equals("xs:float")) {
      return XSDAtomicType.FLOAT;
    } else if (atomicTypeName.equals("xs:double")) {
      return XSDAtomicType.DOUBLE;
    } else if (atomicTypeName.equals("xs:decimal")) {
      return XSDAtomicType.DECIMAL;
    } else if (atomicTypeName.equals("xs:integer")) {
      return XSDAtomicType.INTEGER;
    } else if (atomicTypeName.equals("xs:long")) {
      return XSDAtomicType.LONG;
    } else if (atomicTypeName.equals("xs:int")) {
      return XSDAtomicType.INT;
    } else if (atomicTypeName.equals("xs:short")) {
      return XSDAtomicType.SHORT;
    } else if (atomicTypeName.equals("xs:byte")) {
      return XSDAtomicType.BYTE;
    } else if (atomicTypeName.equals("xs:nonPositiveInteger")) {
      return XSDAtomicType.NON_POSITIVE_INTEGER;
    } else if (atomicTypeName.equals("xs:negativeInteger")) {
      return XSDAtomicType.NEGATIVE_INTEGER;
    } else if (atomicTypeName.equals("xs:nonNegativeInteger")) {
      return XSDAtomicType.NON_NEGATIVE_INTEGER;
    } else if (atomicTypeName.equals("xs:positiveInteger")) {
      return XSDAtomicType.POSITIVE_INTEGER;
    } else if (atomicTypeName.equals("xs:unsignedLong")) {
      return XSDAtomicType.UNSIGNED_LONG;
    } else if (atomicTypeName.equals("xs:unsignedByte")) {
      return XSDAtomicType.UNSIGNED_BYTE;
    } else if (atomicTypeName.equals("xs:string")) {
      return XSDAtomicType.STRING;
    } else if (atomicTypeName.equals("xs:anyType")) {
      return XSDAtomicType.ANY_TYPE;
    }
    
    assert(false);
    return null;
  }
  
  public boolean isNumeric() {
    if (category == Category.BUILT_IN) {
      switch (buildinTypeName) {
        case FLOAT:
        case DOUBLE:
        case DECIMAL:
        case INTEGER:
        case LONG:
        case SHORT:
        case BYTE:
        case NON_POSITIVE_INTEGER:
        case NEGATIVE_INTEGER:
        case NON_NEGATIVE_INTEGER:
        case POSITIVE_INTEGER:
        case UNSIGNED_LONG:
        case UNSIGNED_BYTE:
          return true;
        default:
          return false;
      }
    }
    
    return false;
  }
}
