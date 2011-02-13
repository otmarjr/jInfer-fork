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

package cz.cuni.mff.ksi.jinfer.xsdimportsax.utils;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test {@link SAXDocumentElement } class.
 * @author reseto
 */
@SuppressWarnings("PMD")
public class SAXDocumentElementTest {

  @Test
  public void testGetAttrs0() {
    System.out.println("getAttrs0");
    final SAXDocumentElement instance = new SAXDocumentElement("testElement");
    final int expResult = 0;
    final Map<String, SAXAttributeData> result = instance.getAttrs();
    assertEquals(expResult, result.size());
  }

  @Test
  public void testGetAttrs1() {
    System.out.println("getAttrs1");
    final SAXDocumentElement instance = new SAXDocumentElement("testElement");
    final String name = "myqname";
    final String value = "!IMPORTANT!_VaLuE";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", "myQName", "someTyPe", value);
    instance.getAttrs().put(name, data);
    final Map<String, SAXAttributeData> result = instance.getAttrs();
    final String storedValue = result.get(name).getValue();
    assertEquals(value, storedValue);
  }

  /**
   * Test of getName method, of class SAXDocumentElement.
   */
  @Test(expected=IllegalArgumentException.class)
  public void testGetName0() {
    System.out.println("getName");
    final SAXDocumentElement instance = new SAXDocumentElement(null);
    instance.getName();
  }

  @Test(expected=IllegalArgumentException.class)
  public void testGetName1() {
    System.out.println("getName");
    final SAXDocumentElement instance = new SAXDocumentElement("");
    instance.getName();
  }

  @Test
  public void testGetName2() {
    System.out.println("getName");
    final String expResult = "xs:OMG__what+A&^$%NAME--";
    final SAXDocumentElement instance = new SAXDocumentElement("xs:OMG__what+A&^$%NAME--");
    final String result = instance.getName();
    assertFalse(expResult.equals(result));
  }

  @Test
  public void testGetName3() {
    System.out.println("getName");
    final String expResult = "OMG__what+A&^$%NAME--";
    final SAXDocumentElement instance = new SAXDocumentElement("xs:OMG__what+A&^$%NAME--");
    final String result = instance.getName();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNamedComplexType0() {
    System.out.println("isNamedComplexType0");
    final SAXDocumentElement instance = new SAXDocumentElement("xs:complexType");
    final String name = "name";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "NO this is not a named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = true;
    final boolean result = (instance.isComplexType() && instance.hasAttribute(XSDAttribute.NAME));
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNamedComplexType1() {
    System.out.println("isNamedComplexType1");
    final SAXDocumentElement instance = new SAXDocumentElement("complexType");
    final String name = "name";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = true;
    final boolean result = (instance.isComplexType() && instance.hasAttribute(XSDAttribute.NAME));
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNamedComplexType2() {
    System.out.println("isNamedComplexType2");
    final SAXDocumentElement instance = new SAXDocumentElement("xs:complextype");
    final String name = "name";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "NO this is not a named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = false;
    final boolean result = (instance.isComplexType() && instance.hasAttribute(XSDAttribute.NAME));
    assertEquals(expResult, result);
  }

  @Test
  public void testIsNamedComplexType3() {
    System.out.println("isNamedComplexType3");
    final SAXDocumentElement instance = new SAXDocumentElement("complextype");
    final String name = "name";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = false;
    final boolean result = (instance.isComplexType() && instance.hasAttribute(XSDAttribute.NAME));
    assertEquals(expResult, result);
  }

  @Test
  public void testIsComplexType0() {
    System.out.println("isNamedComplexType0");
    // check is case sensitive
    final SAXDocumentElement instance = new SAXDocumentElement("xs:complexType");
    final String name = "notNamed";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "NO this is not a named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = true;
    final boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsComplexType1() {
    System.out.println("isNamedComplexType1");
    // check is case sensitive
    final SAXDocumentElement instance = new SAXDocumentElement("complexType");
    final String name = "notNamed";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = true;
    final boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsComplexType2() {
    System.out.println("isNamedComplexType2");
    // check is case sensitive
    final SAXDocumentElement instance = new SAXDocumentElement("xs:complextype");
    final String name = "notNamed";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "NO this is not a named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = false;
    final boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsComplexType3() {
    System.out.println("isNamedComplexType3");
    // check is case sensitive
    final SAXDocumentElement instance = new SAXDocumentElement("complextype");
    final String name = "notNamed";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = false;
    final boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsCTypeButNotNamed() {
    System.out.println("isNamedComplexType but not named");
    final SAXDocumentElement instance = new SAXDocumentElement("complexType");
    final String name = "notNamed";
    final SAXAttributeData data = new SAXAttributeData("", "/te%rrible/myQName", name, "someTyPe", "yes this is named CType");
    instance.getAttrs().put(name, data);
    final boolean expResult = true;
    final boolean result = instance.isComplexType();
    assertEquals(expResult, result);
  }

  @Test(expected=XSDException.class)
  public void testAssociateWithUnnamedCType() {
    System.out.println("associateWithUnnamedCType");
    final SAXDocumentElement instance = new SAXDocumentElement("complexType");
    boolean expResult = false;
    boolean result = instance.isAssociated();
    assertEquals(expResult, result);
    instance.associate();
    expResult = true;
    result = instance.isAssociated();
    assertEquals(expResult, result);
  }

  @Test
  public void testAssociateChoice() {
    System.out.println("test associate choice");
    final SAXDocumentElement instance = new SAXDocumentElement("choice");
    boolean expResult = false;
    boolean result = instance.isAssociated();
    assertEquals(expResult, result);
    instance.associate();
    expResult = true;
    result = instance.isAssociated();
    assertEquals(expResult, result);
  }

  @Test
  public void testAssociateSequence() {
    System.out.println("test associate sequence");
    final SAXDocumentElement instance = new SAXDocumentElement("sequence");
    boolean expResult = false;
    boolean result = instance.isAssociated();
    assertEquals(expResult, result);
    instance.associate();
    expResult = true;
    result = instance.isAssociated();
    assertEquals(expResult, result);
  }

  @Test
  public void testAssociateAll() {
    System.out.println("test associate all");
    final SAXDocumentElement instance = new SAXDocumentElement("all");
    boolean expResult = false;
    boolean result = instance.isAssociated();
    assertEquals(expResult, result);
    instance.associate();
    expResult = true;
    result = instance.isAssociated();
    assertEquals(expResult, result);
  }

  @Test
  public void testDetermineRegexpType() {
    System.out.println("test determine type of all");
    final SAXDocumentElement instance = new SAXDocumentElement("all");
    final RegexpType expected = RegexpType.PERMUTATION;
    final RegexpType result = instance.determineRegexpType();
    assertEquals(expected, result);
  }

  @Test
  public void testDetermineRegexpType2() {
    System.out.println("test determine type of sequence");
    final SAXDocumentElement instance = new SAXDocumentElement("xs:sequence");
    final RegexpType expected = RegexpType.CONCATENATION;
    final RegexpType result = instance.determineRegexpType();
    assertEquals(expected, result);
  }

  @Test
  public void testDetermineRegexpType3() {
    System.out.println("test determine type of all");
    final SAXDocumentElement instance = new SAXDocumentElement("element");
    final RegexpType expected = null;
    final RegexpType result = instance.determineRegexpType();
    assertEquals(expected, result);
  }

  @Test
  public void testSchema() {
    System.out.println("test is schema");
    final SAXDocumentElement instance = new SAXDocumentElement("xsd:schema");
    final boolean expected = true;
    final boolean result = instance.isSchema();
    assertEquals(expected, result);
  }

  @Test
  public void testSchemaNot() {
    System.out.println("test is schema");
    final SAXDocumentElement instance = new SAXDocumentElement("schEma");
    final boolean expected = false;
    final boolean result = instance.isSchema();
    assertEquals(expected, result);
  }
}