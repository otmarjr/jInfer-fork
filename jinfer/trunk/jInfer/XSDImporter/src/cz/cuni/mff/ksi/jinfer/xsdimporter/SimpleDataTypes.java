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

package cz.cuni.mff.ksi.jinfer.xsdimporter;

/**
 *
 * @author reseto
 */
public class SimpleDataTypes {

  // ALL TYPES ARE CASE SENSITIVE!!!
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

  // these types should be lowercase, but it conflicts with Java
  public enum TrickyNumericType {
    BYTE,
    INT,
    LONG,
    SHORT,
  }

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
  
  public enum TrickyMiscType {
    BOOLEAN,
    DOUBLE,
    FLOAT;
  }

  public enum MiscType {
    anyURI,
    base64Binary,
    hexBinary,
    NOTATION,
    QName;
  }

  public static boolean isStringType(String type) {
    try {
      StringType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }
  
  public static boolean isDateType(String type) {
    try {
      DateType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }

  public static boolean isNumericType(String type) {
    try {
      NumericType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }
  
  public static boolean isTrickyNumericType(String type) {
    return (TrickyNumericType.BYTE.toString().toLowerCase().equals(trimNS(type))
            || TrickyNumericType.INT.toString().toLowerCase().equals(trimNS(type))
            || TrickyNumericType.LONG.toString().toLowerCase().equals(trimNS(type))
            || TrickyNumericType.SHORT.toString().toLowerCase().equals(trimNS(type)));
  }

  public static boolean isTrickyMiscType(String type) {
    return (TrickyMiscType.BOOLEAN.toString().toLowerCase().equals(trimNS(type))
            || TrickyMiscType.DOUBLE.toString().toLowerCase().equals(trimNS(type))
            || TrickyMiscType.FLOAT.toString().toLowerCase().equals(trimNS(type)));
  }

  public static boolean isMiscType(String type) {
    try {
      MiscType.valueOf(trimNS(type));
      return true;
    } catch (IllegalArgumentException exc) {
      return false;
    }
  }

  public static boolean isSimpleDataType(String type) {
    return (isStringType(type)
            || isDateType(type)
            || isNumericType(type)
            || isTrickyNumericType(type)
            || isTrickyMiscType(type)
            || isMiscType(type));
  }

  private static String trimNS(String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1);
  }
}
