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
package cz.cuni.mff.ksi.jinfer.base.objects.xsd;

/**
 * A representation of XSD built-in atomic types.
 * @author rio
 */
public enum XSDBuiltinAtomicType {

  DURATION("duration"),
  DATE_TIME("dateTime"),
  TIME("time"),
  DATE("date"),
  G_YEAR_MONTH("gYearMonth"),
  G_YEAR("gYear"),
  G_MONTH_DAY("gMonthDay"),
  G_DAY("gDay"),
  G_MONTH("gMonth"),
  BASE64_BINARY("base64Binary"),
  HEX_BINARY("hexBinary"),
  ANY_URI("anyURI"),
  Q_NAME("QName"),
  NOTATION("NOTATION"),
  STRING("string"),
  NORMALIZED_STRING("normalizedString"),
  TOKEN("token"),
  LANGUAGE("language"),
  NAME("Name"),
  NMTOKEN("NMTOKEN"),
  NCNAME("NCName"),
  ID("ID"),
  IDREF("IDREF"),
  ENTITY("ENTITY"),
  FLOAT("float"),
  DOUBLE("double"),
  DECIMAL("decimal"),
  INTEGER("integer"),
  LONG("long"),
  INT("int"),
  SHORT("short"),
  BYTE("byte"),
  NON_POSITIVE_INTEGER("nonPositiveInteger"),
  NEGATIVE_INTEGER("negativeInteger"),
  NON_NEGATIVE_INTEGER("nonNegativeInteger"),
  POSITIVE_INTEGER("positiveIntegeer"),
  UNSIGNED_LONG("unsignedLong"),
  UNSIGNED_BYTE("unsignedByte"),
  UNSIGNED_INT("unsignedInt"),
  UNSIGNED_SHORT("unsignedShort"),
  BOOLEAN("boolean");
  private final String name;

  private XSDBuiltinAtomicType(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * Returns {@link XSDBuiltinAtomicType} for the specified name of a type.
   * @param atomicTypeName
   * @return {@link XSDBuiltinAtomicType}
   */
  public static XSDBuiltinAtomicType nameToType(final String atomicTypeName) {
    final int collonPos = atomicTypeName.lastIndexOf(':');
    String trimmedAtomicTypeName;
    if (collonPos > -1) {
      trimmedAtomicTypeName = atomicTypeName.substring(collonPos + 1);
    } else {
      trimmedAtomicTypeName = atomicTypeName;
    }

    if (trimmedAtomicTypeName.equalsIgnoreCase("duration")) {
      return XSDBuiltinAtomicType.DURATION;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("dateTime")) {
      return XSDBuiltinAtomicType.DATE_TIME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("time")) {
      return XSDBuiltinAtomicType.TIME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("date")) {
      return XSDBuiltinAtomicType.DATE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gYearMonth")) {
      return XSDBuiltinAtomicType.G_YEAR_MONTH;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gYear")) {
      return XSDBuiltinAtomicType.G_YEAR;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gMonthDay")) {
      return XSDBuiltinAtomicType.G_MONTH_DAY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gDay")) {
      return XSDBuiltinAtomicType.G_DAY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("gMonth")) {
      return XSDBuiltinAtomicType.G_MONTH;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("base64Binary")) {
      return XSDBuiltinAtomicType.BASE64_BINARY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("hexBinary")) {
      return XSDBuiltinAtomicType.HEX_BINARY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("anyURL")) {
      return XSDBuiltinAtomicType.ANY_URI;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("QName")) {
      return XSDBuiltinAtomicType.Q_NAME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("NOTATION")) {
      return XSDBuiltinAtomicType.NOTATION;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("normalizedString")) {
      return XSDBuiltinAtomicType.NORMALIZED_STRING;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("token")) {
      return XSDBuiltinAtomicType.TOKEN;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("language")) {
      return XSDBuiltinAtomicType.LANGUAGE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("Name")) {
      return XSDBuiltinAtomicType.NAME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("NMToken")) {
      return XSDBuiltinAtomicType.NMTOKEN;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("NCName")) {
      return XSDBuiltinAtomicType.NCNAME;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("ID")) {
      return XSDBuiltinAtomicType.ID;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("IDREF")) {
      return XSDBuiltinAtomicType.IDREF;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("ENTITY")) {
      return XSDBuiltinAtomicType.ENTITY;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("float")) {
      return XSDBuiltinAtomicType.FLOAT;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("double")) {
      return XSDBuiltinAtomicType.DOUBLE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("decimal")) {
      return XSDBuiltinAtomicType.DECIMAL;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("integer")) {
      return XSDBuiltinAtomicType.INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("long")) {
      return XSDBuiltinAtomicType.LONG;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("int")) {
      return XSDBuiltinAtomicType.INT;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("short")) {
      return XSDBuiltinAtomicType.SHORT;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("byte")) {
      return XSDBuiltinAtomicType.BYTE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("nonPositiveInteger")) {
      return XSDBuiltinAtomicType.NON_POSITIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("negativeInteger")) {
      return XSDBuiltinAtomicType.NEGATIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("nonNegativeInteger")) {
      return XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("positiveInteger")) {
      return XSDBuiltinAtomicType.POSITIVE_INTEGER;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("unsignedLong")) {
      return XSDBuiltinAtomicType.UNSIGNED_LONG;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("unsignedByte")) {
      return XSDBuiltinAtomicType.UNSIGNED_BYTE;
    } else if (trimmedAtomicTypeName.equalsIgnoreCase("string")) {
      return XSDBuiltinAtomicType.STRING;
    }

    assert (false);
    return null;
  }
  
}
