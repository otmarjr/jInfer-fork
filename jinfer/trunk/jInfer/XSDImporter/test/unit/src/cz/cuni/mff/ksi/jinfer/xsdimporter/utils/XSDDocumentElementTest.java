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

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
public class XSDDocumentElementTest {

  @Test
  public void testGetAssociatedCTypeName() {
    System.out.println("getAssociatedCTypeName");
    XSDDocumentElement instance = new XSDDocumentElement("testElement");
    assertEquals("", instance.getAssociatedCTypeName()); // default in constructor
    instance.setAssociatedCTypeName("myHorrendouslyNamedCType");
    String expResult = "myHorrendouslyNamedCType";
    String result = instance.getAssociatedCTypeName();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetAttrs0() {
    System.out.println("getAttrs0");
    XSDDocumentElement instance = new XSDDocumentElement("testElement");
    int expResult = 0;
    Map result = instance.getAttrs();
    assertEquals(expResult, result.size());
  }

  @Test
  public void testGetAttrs1() {
    System.out.println("getAttrs1");
    XSDDocumentElement instance = new XSDDocumentElement("testElement");
    String name = "myqname";
    String value = "!IMPORTANT!_VaLuE";
    SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", "myQName", "someTyPe", value);
    instance.getAttrs().put(name, data);
    Map result = instance.getAttrs();
    String storedValue = ((SAXAttributeData) result.get(name)).getValue();
    assertEquals(value, storedValue);
  }

  /**
   * Test of getName method, of class XSDDocumentElement.
   */
  @Test(expected=IllegalArgumentException.class)
  public void testGetName0() {
    System.out.println("getName");
    XSDDocumentElement instance = new XSDDocumentElement(null);
    instance.getName();
  }

  @Test(expected=IllegalArgumentException.class)
  public void testGetName1() {
    System.out.println("getName");
    XSDDocumentElement instance = new XSDDocumentElement("");
    instance.getName();
  }

  @Test
  public void testGetName2() {
    System.out.println("getName");
    String expResult = "xs:OMG__what+A&^$%NAME--";
    XSDDocumentElement instance = new XSDDocumentElement("xs:OMG__what+A&^$%NAME--");
    String result = instance.getName();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNamedComplexType0() {
    System.out.println("isNamedComplexType0");
    XSDDocumentElement instance = new XSDDocumentElement("xs:complexType");
    String name = "name";
    SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "NO this is not a named CType");
    instance.getAttrs().put(name, data);
    boolean expResult = false;
    boolean result = instance.isNamedComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNamedComplexType1() {
    System.out.println("isNamedComplexType1");
    XSDDocumentElement instance = new XSDDocumentElement("complexType");
    String name = "name";
    SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    boolean expResult = true;
    boolean result = instance.isNamedComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsComplexType0() {
    System.out.println("isNamedComplexType0");
    XSDDocumentElement instance = new XSDDocumentElement("xs:complexType");
    String name = "notNamed";
    SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "NO this is not a named CType");
    instance.getAttrs().put(name, data);
    boolean expResult = false;
    boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsComplexType1() {
    System.out.println("isNamedComplexType1");
    XSDDocumentElement instance = new XSDDocumentElement("complexType");
    String name = "notNamed";
    SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    boolean expResult = true;
    boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsCTypeButNotNamed() {
    System.out.println("isNamedComplexType1");
    XSDDocumentElement instance = new XSDDocumentElement("complexType");
    String name = "notNamed";
    SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    boolean expResult = true;
    boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testAssociateWithUnnamedCType() {
    System.out.println("associateWithUnnamedCType");
    XSDDocumentElement instance = new XSDDocumentElement("complexType");
    boolean expResult = false;
    boolean result = instance.isAssociated();
    assertEquals(expResult, result);
    instance.associateWithUnnamedCType();
    expResult = true;
    result = instance.isAssociated();
    assertEquals(expResult, result);
  }
}