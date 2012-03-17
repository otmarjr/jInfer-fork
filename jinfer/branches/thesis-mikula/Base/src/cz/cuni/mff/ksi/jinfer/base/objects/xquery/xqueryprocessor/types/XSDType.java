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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Cardinality;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.LiteralType;

/**
 * A representation of XSD built-in atomic types.
 * @author rio
 */
public class XSDType extends CardinalityType {
  
  public enum XSDAtomicType {
    DURATION ("duration"),
    DATE_TIME ("dateTime"),
    TIME ("time"),
    DATE ("date"),
    G_YEAR_MONTH ("gYearMonth"),
    G_YEAR ("gYear"),
    G_MONTH_DAY ("gMonthDay"),
    G_DAY ("gDay"),
    G_MONTH ("gMonth"),
    BASE64_BINARY ("base64Binary"),
    HEX_BINARY ("hexBinary"),
    ANY_URI ("anyURI"),
    Q_NAME ("QName"),
    NOTATION ("NOTATION"),
    STRING ("string"),
    NORMALIZED_STRING ("normalizedString"),
    TOKEN ("token"),
    LANGUAGE ("language"),
    NAME ("Name"),
    NMTOKEN ("NMTOKEN"),
    NCNAME ("NCName"),
    ID ("ID"),
    IDREF ("IDREF"),
    ENTITY ("ENTITY"),    
    FLOAT ("float"),
    DOUBLE ("double"),
    DECIMAL ("decimal"),
    INTEGER ("integer"),
    LONG ("long"),
    INT ("int"),
    SHORT ("short"),
    BYTE ("byte"),
    NON_POSITIVE_INTEGER ("nonPositiveInteger"),
    NEGATIVE_INTEGER ("negativeInteger"),
    NON_NEGATIVE_INTEGER ("nonNegativeInteger"),
    POSITIVE_INTEGER ("positiveIntegeer"),
    UNSIGNED_LONG ("unsignedLong"),
    UNSIGNED_BYTE ("unsignedByte"),
    UNSIGNED_INT ("unsignedInt"),
    UNSIGNED_SHORT ("unsignedShort"),
    BOOLEAN ("boolean");
    
    private final String name;
    
    private XSDAtomicType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

  }
  
  private final XSDAtomicType atomicType;

  /**
   * A basic constructor.
   */
  public XSDType(final XSDAtomicType atomicType, final Cardinality cardinality) {
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
    this.atomicType = atomicNameToType(atomicTypeName);
  }
  
  /**
   * A constructor from an instance of {@link LiteralType}.
   * @param literalType 
   */
  public XSDType(final LiteralType literalType) {
    super(Cardinality.ONE);
    switch(literalType) {
      case DECIMAL:
        atomicType = XSDAtomicType.DECIMAL;
        break;
      case DOUBLE:
        atomicType = XSDAtomicType.DOUBLE;
        break;
      case INTEGER:
        atomicType = XSDAtomicType.INTEGER;
        break;
      case STRING:
        atomicType = XSDAtomicType.STRING;
        break;
      default:
        throw new IllegalStateException();
    }
  }
  
  @Override
  public Category getCategory() {
    return Category.XSD_BUILT_IN;
  }
  
  public XSDAtomicType getAtomicType() {
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
  
  private static XSDAtomicType atomicNameToType(final String atomicTypeName) {
    final int collonPos = atomicTypeName.lastIndexOf(':');
    String trimmedAtomicTypeName;
    if (collonPos > -1) {
      trimmedAtomicTypeName = atomicTypeName.substring(collonPos + 1);
    } else {
      trimmedAtomicTypeName = atomicTypeName;
    }
    
    if (trimmedAtomicTypeName.equalsIgnoreCase("duration")) {
      return XSDAtomicType.DURATION;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("dateTime")) {
      return XSDAtomicType.DATE_TIME;
    }  else if (trimmedAtomicTypeName.equalsIgnoreCase("time")) {
      return XSDAtomicType.TIME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("date")) {
      return XSDAtomicType.DATE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gYearMonth")) {
      return XSDAtomicType.G_YEAR_MONTH;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gYear")) {
      return XSDAtomicType.G_YEAR;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gMonthDay")) {
      return XSDAtomicType.G_MONTH_DAY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gDay")) {
      return XSDAtomicType.G_DAY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gMonth")) {
      return XSDAtomicType.G_MONTH;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("base64Binary")) {
      return XSDAtomicType.BASE64_BINARY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("hexBinary")) {
      return XSDAtomicType.HEX_BINARY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("anyURL")) {
      return XSDAtomicType.ANY_URI;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("QName")) {
      return XSDAtomicType.Q_NAME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("NOTATION")) {
      return XSDAtomicType.NOTATION;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("normalizedString")) {
      return XSDAtomicType.NORMALIZED_STRING;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("token")) {
      return XSDAtomicType.TOKEN;
    }  else if (trimmedAtomicTypeName.equalsIgnoreCase("language")) {
      return XSDAtomicType.LANGUAGE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("Name")) {
      return XSDAtomicType.NAME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("NMToken")) {
      return XSDAtomicType.NMTOKEN;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("NCName")) {
      return XSDAtomicType.NCNAME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("ID")) {
      return XSDAtomicType.ID;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("IDREF")) {
      return XSDAtomicType.IDREF;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("ENTITY")) {
      return XSDAtomicType.ENTITY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("float")) {
      return XSDAtomicType.FLOAT;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("double")) {
      return XSDAtomicType.DOUBLE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("decimal")) {
      return XSDAtomicType.DECIMAL;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("integer")) {
      return XSDAtomicType.INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("long")) {
      return XSDAtomicType.LONG;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("int")) {
      return XSDAtomicType.INT;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("short")) {
      return XSDAtomicType.SHORT;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("byte")) {
      return XSDAtomicType.BYTE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("nonPositiveInteger")) {
      return XSDAtomicType.NON_POSITIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("negativeInteger")) {
      return XSDAtomicType.NEGATIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("nonNegativeInteger")) {
      return XSDAtomicType.NON_NEGATIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("positiveInteger")) {
      return XSDAtomicType.POSITIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("unsignedLong")) {
      return XSDAtomicType.UNSIGNED_LONG;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("unsignedByte")) {
      return XSDAtomicType.UNSIGNED_BYTE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("string")) {
      return XSDAtomicType.STRING;
    }
    
    assert(false);
    return null;
  }

  @Override
  public boolean isXsdBuiltinType() {
    return true;
  }
  
}
