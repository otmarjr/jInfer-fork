/*
 *  Copyright (C) 2010 reseto
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

/**
 * Class holding all known built-in data types supported by XSD Schema.
 * Every type in Schema is case sensitive, but some of them conflict with Java internal types.
 * Therefore, the conflicting types are named "tricky" and are defined in uppercase here,
 * although they always occur in lowercase in the Schema.
 * @author reseto
 */
public final class XSDBuiltInDataTypes {

  private XSDBuiltInDataTypes() {}

  // ALL TYPES ARE CASE SENSITIVE!!!
  /**
   * Non-conflicting types for names, references and other strings.
   */
  public enum StringType {

    ENTITIES,
    ENTITY,
    ID,
    IDREF,
    IDREFS,
    language,
    Name,
    NCName,
    NMTOKEN,
    NMTOKENS,
    normalizedString,
    QName,
    string,
    token;
  }

  /**
   * Non-conflicting types for date or time.
   */
  public enum DateType {

    date,
    dateTime,
    duration,
    gDay,
    gMonth,
    gMonthDay,
    gYear,
    gYearMonth,
    time;
  }

  /**
   * Conflicting types of numeric nature.
   * These types should be lowercase in Schema, but it conflicts with Java.
   */ 
  public enum TrickyNumericType {
    
    BYTE,
    INT,
    LONG,
    SHORT,
  }

  /**
   * Non-conflicting types of numeric nature.
   */
  public enum NumericType {

    decimal,
    integer,
    negativeInteger,
    nonNegativeInteger,
    nonPositiveInteger,
    positiveInteger,
    unsignedLong,
    unsignedInt,
    unsignedShort,
    unsignedByte;
  }

  /**
   * Other conflicting types.
   * These types should be lowercase in Schema, but it conflicts with Java.
   */
  public enum TrickyMiscType {
    BOOLEAN,
    DOUBLE,
    FLOAT;
  }

  /**
   * Other non-conflicting types.
   */
  public enum MiscType {

    anyURI,
    base64Binary,
    hexBinary,
    NOTATION;
  }

  /**
   * Check if type falls into {@link StringType } category.
   * @param type Type to be checked.
   * @return True if the enum contains given type, false otherwise.
   */
  public static boolean isStringType(final String type) {
    try {
      StringType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }

  /**
   * Check if type falls into {@link DateType } category.
   * @param type Type to be checked.
   * @return True if the enum contains given type, false otherwise.
   */
  public static boolean isDateType(final String type) {
    try {
      DateType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }

  /**
   * Check if type falls into {@link NumericType } category.
   * @param type Type to be checked.
   * @return True if the enum contains given type, false otherwise.
   */
  public static boolean isNumericType(final String type) {
    try {
      NumericType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }

  /**
   * Check if type falls into {@link TrickyNumericType } category.
   * @param type Type to be checked.
   * @return True if the enum contains given type, false otherwise.
   */
  public static boolean isTrickyNumericType(final String type) {
    return (TrickyNumericType.BYTE.toString().toLowerCase().equals(trimNS(type))
            || TrickyNumericType.INT.toString().toLowerCase().equals(trimNS(type))
            || TrickyNumericType.LONG.toString().toLowerCase().equals(trimNS(type))
            || TrickyNumericType.SHORT.toString().toLowerCase().equals(trimNS(type)));
  }

  /**
   * Check if type falls into {@link TrickyMiscType } category.
   * @param type Type to be checked.
   * @return True if the enum contains given type, false otherwise.
   */
  public static boolean isTrickyMiscType(final String type) {
    return (TrickyMiscType.BOOLEAN.toString().toLowerCase().equals(trimNS(type))
            || TrickyMiscType.DOUBLE.toString().toLowerCase().equals(trimNS(type))
            || TrickyMiscType.FLOAT.toString().toLowerCase().equals(trimNS(type)));
  }

  /**
   * Check if type falls into {@link MiscType } category.
   * @param type Type to be checked.
   * @return True if the enum contains given type, false otherwise.
   */
  public static boolean isMiscType(final String type) {
    try {
      MiscType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }

  /**
   * Check if type falls into any of the built in data types defined in XSD Schema.
   * @param type Type to be checked.
   * @return True if eny of the enums contain given type, false otherwise.
   */
  public static boolean isSimpleDataType(final String type) {
    return (isStringType(type)
            || isDateType(type)
            || isNumericType(type)
            || isTrickyNumericType(type)
            || isTrickyMiscType(type)
            || isMiscType(type));
  }

  private static String trimNS(final String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1);
  }
}
