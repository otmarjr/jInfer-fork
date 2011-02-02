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
public class XSDBuiltInDataTypesTest {

  // ------------------------------------------------------------------ STRINGS
  @Test(expected=NullPointerException.class)
  public void testIsStringType0() {
    System.out.println("isStringType0(null)");
    String type = null;
    XSDBuiltInDataTypes.isStringType(type);
  }

  @Test
  public void testIsStringType1() {
    System.out.println("isStringType1(xs:string)");
    String type = "xs:string";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType2() {
    System.out.println("isStringType2(XS:QName)");
    String type = "XS:QName";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType3() {
    System.out.println("isStringType3(empty)");
    String type = "";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType4() {
    System.out.println("isStringType4(xs:xs:)");
    String type = "xs:xs:";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType5() {
    System.out.println("isStringType5(definedType)");
    String type = "definedType";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsStringType6() {
    System.out.println("isStringType6(double)");
    String type = "double";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isStringType(type);
    assertEquals(expResult, result);
  }
  // --------------------------------------------------------------- DATE TYPES

  @Test(expected=NullPointerException.class)
  public void testIsDateType0() {
    System.out.println("isDateType0(null)");
    String type = null;
    XSDBuiltInDataTypes.isDateType(type);
  }

  @Test
  public void testIsDateType1() {
    System.out.println("isDateType1(xs:date)");
    String type = "xs:date";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsDateType2() {
    System.out.println("isDateType2(XS:gYearMonth)");
    String type = "XS:gYearMonth";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsDateType3() {
    System.out.println("isDateType3(empty)");
    String type = "";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }
  
  @Test
  public void testIsDateType4() {
    System.out.println("isDateType4(xs:string)");
    String type = "xs:string";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isDateType(type);
    assertEquals(expResult, result);
  }

  // ------------------------------------------------------------ NUMERIC TYPES

  @Test(expected=NullPointerException.class)
  public void testIsNumericType0() {
    System.out.println("isNumericType0(null)");
    String type = null;
    XSDBuiltInDataTypes.isNumericType(type);
  }

  @Test
  public void testIsNumericType1() {
    System.out.println("isNumericType1(xs:integer)");
    String type = "xs:integer";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType2() {
    System.out.println("isNumericType2(XS:unsignedInt)");
    String type = "XS:unsignedInt";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType3() {
    System.out.println("isNumericType3(empty)");
    String type = "";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNumericType4() {
    System.out.println("isNumericType4(XS:gYearMonth)");
    String type = "XS:gYearMonth";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isNumericType(type);
    assertEquals(expResult, result);
  }

  // ----------------------------------------------------- TRICKY NUMERIC TYPES

  @Test(expected=NullPointerException.class)
  public void testIsTrickyNumericType0() {
    System.out.println("isTrickyNumericType0(null)");
    String type = null;
    XSDBuiltInDataTypes.isTrickyNumericType(type);
  }

  @Test
  public void testIsTrickyNumericType1() {
    System.out.println("isTrickyNumericType1(xs:byte)");
    String type = "xs:byte";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType2() {
    System.out.println("isTrickyNumericType2(XS:int)");
    String type = "XS:int";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType3() {
    System.out.println("isTrickyNumericType3(empty)");
    String type = "";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType4() {
    System.out.println("isTrickyNumericType4(XS:)");
    String type = "XS:";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyNumericType5() {
    System.out.println("isTrickyNumericType5(XS:boolean)");
    String type = "XS:";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isTrickyNumericType(type);
    assertEquals(expResult, result);
  }

  // -------------------------------------------------------- TRICKY MISC TYPES
  
  @Test(expected=NullPointerException.class)
  public void testIsTrickyMiscType0() {
    System.out.println("isTrickyMiscType0(null)");
    String type = null;
    XSDBuiltInDataTypes.isTrickyNumericType(type);
  }

  @Test
  public void testIsTrickyMiscType1() {
    System.out.println("isTrickyMiscType1(xs:boolean)");
    String type = "xs:boolean";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType2() {
    System.out.println("isTrickyMiscType2(XS:float)");
    String type = "XS:float";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType3() {
    System.out.println("isTrickyMiscType3(empty)");
    String type = "";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsTrickyMiscType4() {
    System.out.println("isTrickyMiscType4(XS:BOOLEAN)");
    String type = "XS:BOOLEAN";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isTrickyMiscType(type);
    assertEquals(expResult, result);
  }

  // --------------------------------------------------------------- MISC TYPES

  @Test(expected=NullPointerException.class)
  public void testIsMiscType0() {
    System.out.println("isMiscType0(null)");
    String type = null;
    XSDBuiltInDataTypes.isMiscType(type);
  }

  @Test
  public void testIsMiscType1() {
    System.out.println("isMiscType1(xs:NOTATION)");
    String type = "xs:NOTATION";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType2() {
    System.out.println("isMiscType2(XS:base64Binary)");
    String type = "XS:base64Binary";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType3() {
    System.out.println("isMiscType3(empty)");
    String type = "";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsMiscType4() {
    System.out.println("isMiscType4(xs:gYearMonth)");
    String type = "xs:gYearMonth";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isMiscType(type);
    assertEquals(expResult, result);
  }

  // -------------------------------------------------------------- everything
    
  @Test(expected=NullPointerException.class)
  public void testIsSimpleDataType0() {
    System.out.println("isSimpleDataType0(null)");
    String type = null;
    XSDBuiltInDataTypes.isSimpleDataType(type);
  }

  @Test
  public void testIsSimpleDataType1() {
    System.out.println("isSimpleDataType1(xs:token)");
    String type = "xs:token";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isSimpleDataType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsSimpleDataType2() {
    System.out.println("isSimpleDataType2(XS:base64Binary)");
    String type = "XS:base64Binary";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isSimpleDataType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsSimpleDataType3() {
    System.out.println("isMiscType3(empty)");
    String type = "";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isSimpleDataType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsSimpleDataType4() {
    System.out.println("isSimpleDataType4(xs:myType)");
    String type = "xs:myType";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isSimpleDataType(type);
    assertEquals(expResult, result);
  }

  public void testIsSimpleDataType5() {
    System.out.println("isSimpleDataType5(xs:BOOLEAN)");
    String type = "xs:BOOLEAN";
    boolean expResult = false;
    boolean result = XSDBuiltInDataTypes.isSimpleDataType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsSimpleDataType6() {
    System.out.println("isSimpleDataType6(xs:string)");
    String type = "xs:string";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isSimpleDataType(type);
    assertEquals(expResult, result);
  }

  @Test
  public void testIsSimpleDataType7() {
    System.out.println("isSimpleDataType7(xs:int)");
    String type = "xs:int";
    boolean expResult = true;
    boolean result = XSDBuiltInDataTypes.isSimpleDataType(type);
    assertEquals(expResult, result);
  }
}