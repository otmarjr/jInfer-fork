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

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Cardinality;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.LiteralType;
import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;

/**
 * A representation of XSD built-in atomic types.
 * @author rio
 */
public class XSDType extends CardinalityType {
  
  private final XSDBuiltinAtomicType atomicType;

  /**
   * A basic constructor.
   */
  public XSDType(final XSDBuiltinAtomicType atomicType, final Cardinality cardinality) {
    super(cardinality);
    this.atomicType = atomicType;
  }
  
  /**
   * A constructor from a name of a type.
   * @param atomicTypeName
   * @param cardinality 
   */
  public XSDType(final String atomicTypeName, final Cardinality cardinality) {
    super(cardinality);
    this.atomicType = XSDBuiltinAtomicType.nameToType(atomicTypeName);
  }
  
  /**
   * A constructor from an instance of {@link LiteralType}.
   * @param literalType 
   */
  public XSDType(final LiteralType literalType) {
    super(Cardinality.ONE);
    switch(literalType) {
      case DECIMAL:
        atomicType = XSDBuiltinAtomicType.DECIMAL;
        break;
      case DOUBLE:
        atomicType = XSDBuiltinAtomicType.DOUBLE;
        break;
      case INTEGER:
        atomicType = XSDBuiltinAtomicType.INTEGER;
        break;
      case STRING:
        atomicType = XSDBuiltinAtomicType.STRING;
        break;
      default:
        throw new IllegalStateException();
    }
  }
  
  @Override
  public Category getCategory() {
    return Category.XSD_BUILT_IN;
  }
  
  public XSDBuiltinAtomicType getAtomicType() {
    return atomicType;
  }
  
  @Override
  public boolean isNumeric() {
    switch (atomicType) {
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
  
  @Override
  public boolean isXsdBuiltinType() {
    return true;
  }
  
}
