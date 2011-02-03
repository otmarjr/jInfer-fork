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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
@SuppressWarnings("PMD")
public class XSDBuiltInDataTypesTest {

  // ------------------------------------------------------------------ STRINGS
  @Test
  public void testIsStringType0() {
    System.out.println("isStringType0(null)");
    final String type = null;
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType1() {
    System.out.println("isStringType1(string)");
    final String type = "string";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType2() {
    System.out.println("isStringType2(QName)");
    final String type = "QName";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType3() {
    System.out.println("isStringType3(empty)");
    final String type = "";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType4() {
    System.out.println("isStringType4(xs:xs:)");
    final String type = "xs:xs:";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType5() {
    System.out.println("isStringType5(definedType)");
    final String type = "definedType";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType6() {
    System.out.println("isStringType6(double)");
    final String type = "double";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType7() {
    System.out.println("isStringType7(xs:string)");
    final String type = "xs:string";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType8() {
    System.out.println("isStringType8(XS:QName)");
    final String type = "XS:QName";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }
  // --------------------------------------------------------------- DATE TYPES

  @Test
  public void testIsDateType0() {
    System.out.println("isDateType0(null)");
    final String type = null;
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsDateType1() {
    System.out.println("isDateType1(date)");
    final String type = "date";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsDateType2() {
    System.out.println("isDateType2(gYearMonth)");
    final String type = "gYearMonth";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsDateType3() {
    System.out.println("isDateType3(empty)");
    final String type = "";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }
  
  @Test
  public void testIsDateType4() {
    System.out.println("isDateType4(xs:string)");
    final String type = "xs:string";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsDateType5() {
    System.out.println("isDateType5(xs:date)");
    final String type = "xs:date";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsDateType6() {
    System.out.println("isDateType6(XS:gYearMonth)");
    final String type = "XS:gYearMonth";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  // ------------------------------------------------------------ NUMERIC TYPES

  @Test
  public void testIsNumericType0() {
    System.out.println("isNumericType0(null)");
    final String type = null;
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType1() {
    System.out.println("isNumericType1(integer)");
    final String type = "integer";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType2() {
    System.out.println("isNumericType2(unsignedInt)");
    final String type = "unsignedInt";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType3() {
    System.out.println("isNumericType3(empty)");
    final String type = "";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType4() {
    System.out.println("isNumericType4(XS:gYearMonth)");
    final String type = "XS:gYearMonth";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType5() {
    System.out.println("isNumericType5(xs:integer)");
    final String type = "xs:integer";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType6() {
    System.out.println("isNumericType6(xs:unsignedInt)");
    final String type = "xs:unsignedInt";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  // ----------------------------------------------------- TRICKY NUMERIC TYPES

  @Test
  public void testIsTrickyNumericType0() {
    System.out.println("isTrickyNumericType0(null)");
    final String type = null;
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType1() {
    System.out.println("isTrickyNumericType1(byte)");
    final String type = "byte";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType2() {
    System.out.println("isTrickyNumericType2(int)");
    final String type = "int";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType3() {
    System.out.println("isTrickyNumericType3(empty)");
    final String type = "";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType4() {
    System.out.println("isTrickyNumericType4(XS:)");
    final String type = "XS:";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType5() {
    System.out.println("isTrickyNumericType5(XS:boolean)");
    final String type = "XS:";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType6() {
    System.out.println("isTrickyNumericType6(xs:byte)");
    final String type = "xs:byte";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType7() {
    System.out.println("isTrickyNumericType7(XS:int)");
    final String type = "XS:int";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  // -------------------------------------------------------- TRICKY MISC TYPES
  
  @Test
  public void testIsTrickyMiscType0() {
    System.out.println("isTrickyMiscType0(null)");
    final String type = null;
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType1() {
    System.out.println("isTrickyMiscType1(boolean)");
    final String type = "boolean";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType2() {
    System.out.println("isTrickyMiscType2(float)");
    final String type = "float";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType3() {
    System.out.println("isTrickyMiscType3(empty)");
    final String type = "";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType4() {
    System.out.println("isTrickyMiscType4(XS:BOOLEAN)");
    final String type = "XS:BOOLEAN";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType5() {
    System.out.println("isTrickyMiscType5(xs:boolean)");
    final String type = "xs:boolean";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType6() {
    System.out.println("isTrickyMiscType6(XS:float)");
    final String type = "XS:float";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  // --------------------------------------------------------------- MISC TYPES

  @Test
  public void testIsMiscType0() {
    System.out.println("isMiscType0(null)");
    final String type = null;
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType1() {
    System.out.println("isMiscType1(NOTATION)");
    final String type = "NOTATION";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType2() {
    System.out.println("isMiscType2(base64Binary)");
    final String type = "base64Binary";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType3() {
    System.out.println("isMiscType3(empty)");
    final String type = "";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType4() {
    System.out.println("isMiscType4(xs:gYearMonth)");
    final String type = "xs:gYearMonth";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType5() {
    System.out.println("isMiscType5(xs:NOTATION)");
    final String type = "xs:NOTATION";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType6() {
    System.out.println("isMiscType6(XS:base64Binary)");
    final String type = "XS:base64Binary";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  // -------------------------------------------------------------- everything else FALSE
    
  @Test
  public void testIsBuiltInType0() {
    System.out.println("IsBuiltInType0(null)");
    final String type = null;
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType1() {
    System.out.println("IsBuiltInType1(xs:token)");
    final String type = "xs:token";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType2() {
    System.out.println("IsBuiltInType2(XS:base64Binary)");
    final String type = "XS:base64Binary";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType3() {
    System.out.println("isMiscType3(empty)");
    final String type = "";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType4() {
    System.out.println("IsBuiltInType4(xs:myType)");
    final String type = "xs:myType";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType5() {
    System.out.println("IsBuiltInType5(xs:BOOLEAN)");
    final String type = "xs:BOOLEAN";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType6() {
    System.out.println("IsBuiltInType6(xs:string)");
    final String type = "xs:string";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType7() {
    System.out.println("IsBuiltInType7(xs:int)");
    final String type = "xs:int";
    final boolean expResult = false;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  // ------------------------------------------------------------- everything else TRUE

  @Test
  public void testIsBuiltInType11() {
    System.out.println("IsBuiltInType1(token)");
    final String type = "token";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType12() {
    System.out.println("IsBuiltInType2(base64Binary)");
    final String type = "base64Binary";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType14() {
    System.out.println("IsBuiltInType4(unsignedInt)");
    final String type = "unsignedInt";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType15() {
    System.out.println("IsBuiltInType5(boolean)");
    final String type = "boolean";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType16() {
    System.out.println("IsBuiltInType6(string)");
    final String type = "string";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsBuiltInType17() {
    System.out.println("IsBuiltInType7(int)");
    final String type = "int";
    final boolean expResult = true;
    final boolean result = XSDBuiltInDataTypes.isBuiltInType(type);
    assertEquals(expResult, result);
  }

}