/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.basicdtd.utils;

import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class DomainUtilsTest {

  private static final int THRESHOLD = 3;
  private static final double DOMINANCE_RATIO = 0.75;

  @Test(expected = NullPointerException.class)
  public void testGetDomainNull() {
    System.out.println("testGetDomainNull");
    DomainUtils.getDomain(null);
  }

  @Test
  public void testGetDomainEmpty() {
    System.out.println("testGetDomainEmpty");
    final Attribute a = TestUtils.getAttribute("a");
    if (!DomainUtils.getDomain(a).isEmpty()) {
      fail();
    }
  }

  @Test
  public void testGetDomainSimple() {
    System.out.println("testGetDomainSimple");
    final Attribute a = new Attribute(TestUtils.EMPTY_CONTEXT, "a",
            TestUtils.EMPTY_METADATA, null, Arrays.asList("a", "b", "c"));
    final Map<String, Integer> domain = DomainUtils.getDomain(a);
    assertEquals(domain.size(), 3);
    assertEquals(Integer.valueOf(1), domain.get("a"));
    assertEquals(Integer.valueOf(1), domain.get("b"));
    assertEquals(Integer.valueOf(1), domain.get("c"));
  }

  @Test
  public void testGetDomainMoreComplex() {
    System.out.println("testGetDomainMoreComplex");
    final Attribute a = new Attribute(TestUtils.EMPTY_CONTEXT, "a",
            TestUtils.EMPTY_METADATA, null, Arrays.asList("a", "a", "b", "a", "c", "c"));
    final Map<String, Integer> domain = DomainUtils.getDomain(a);
    assertEquals(domain.size(), 3);
    assertEquals(Integer.valueOf(3), domain.get("a"));
    assertEquals(Integer.valueOf(1), domain.get("b"));
    assertEquals(Integer.valueOf(2), domain.get("c"));
  }

  @Test
  public void testGetDomainComplex() {
    System.out.println("testGetDomainComplex");
    final Attribute a = new Attribute(TestUtils.EMPTY_CONTEXT, "a",
            TestUtils.EMPTY_METADATA, null, Arrays.asList("c", "a", "a", "d", "b", "a", "c", "c"));
    final Map<String, Integer> domain = DomainUtils.getDomain(a);
    assertEquals(domain.size(), 4);
    assertEquals(Integer.valueOf(3), domain.get("a"));
    assertEquals(Integer.valueOf(1), domain.get("b"));
    assertEquals(Integer.valueOf(3), domain.get("c"));
    assertEquals(Integer.valueOf(1), domain.get("d"));
  }

  @Test(expected = NullPointerException.class)
  public void testGetAttributeTypeNull() {
    System.out.println("testGetAttributeTypeNull");
    DomainUtils.getAttributeType(null, THRESHOLD);
  }

  @Test
  public void testGetAttributeTypeEmpty() {
    System.out.println("testGetAttributeTypeEmpty");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    final String result = DomainUtils.getAttributeType(domain, THRESHOLD);
    assertEquals(DomainUtils.ATTRIBUTE_CDATA, result);
  }

  @Test
  public void testGetAttributeTypeOneOfThree() {
    System.out.println("testGetAttributeTypeOneOfThree");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    final String result = DomainUtils.getAttributeType(domain, THRESHOLD);
    assertEquals(" (a) ", result);
  }

  @Test
  public void testGetAttributeTypeThreeOfThree() {
    System.out.println("testGetAttributeTypeThreeOfThree");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    domain.put("b", Integer.valueOf(2));
    domain.put("c", Integer.valueOf(3));
    final String result = DomainUtils.getAttributeType(domain, THRESHOLD);
    assertEquals(" (a|b|c) ", result);
  }

  @Test
  public void testGetAttributeTypeFourOfThree() {
    System.out.println("testGetAttributeTypeFourOfThree");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    domain.put("b", Integer.valueOf(2));
    domain.put("c", Integer.valueOf(3));
    domain.put("d", Integer.valueOf(4));
    final String expResult = DomainUtils.ATTRIBUTE_CDATA;
    final String result = DomainUtils.getAttributeType(domain, THRESHOLD);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetAttributeTypeSpace() {
    System.out.println("testGetAttributeTypeSpace");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("space space", Integer.valueOf(1));
    domain.put("space_space", Integer.valueOf(1));
    final String expResult = DomainUtils.ATTRIBUTE_CDATA;
    final String result = DomainUtils.getAttributeType(domain, THRESHOLD);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetAttributeTypeSpaceOk() {
    System.out.println("testGetAttributeTypeSpaceOk");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("space_space", Integer.valueOf(1));
    final String result = DomainUtils.getAttributeType(domain, THRESHOLD);
    assertEquals(" (space_space) ", result);
  }

  @Test(expected = NullPointerException.class)
  public void testGetDefaultNull() {
    System.out.println("testGetDefaultNull");
    DomainUtils.getDefault(null, DOMINANCE_RATIO);
  }

  @Test
  public void testGetDefaultEmpty() {
    System.out.println("testGetDefaultEmpty");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    final String expResult = DomainUtils.ATTRIBUTE_IMPLIED;
    final String result = DomainUtils.getDefault(domain, DOMINANCE_RATIO);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDefaultNoDominance() {
    System.out.println("testGetDefaultNoDominance");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    domain.put("b", Integer.valueOf(2));
    domain.put("c", Integer.valueOf(3));
    domain.put("d", Integer.valueOf(4));
    final String expResult = DomainUtils.ATTRIBUTE_IMPLIED;
    final String result = DomainUtils.getDefault(domain, DOMINANCE_RATIO);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDefaultDominance() {
    System.out.println("testGetDefaultDominance");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    domain.put("b", Integer.valueOf(2));
    domain.put("c", Integer.valueOf(3));
    domain.put("d", Integer.valueOf(50));
    final String result = DomainUtils.getDefault(domain, DOMINANCE_RATIO);
    assertEquals("\"d\"", result);
  }
  
  @Test
  public void testGetDefaultDominanceTie() {
    System.out.println("testGetDefaultDominanceTie");
    final Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    domain.put("b", Integer.valueOf(50));
    domain.put("d", Integer.valueOf(50));
    final String result = DomainUtils.getDefault(domain, 0.2);
    if (!"\"b\"".equals(result) && !"\"d\"".equals(result)) {
      fail();
    }
  }
}
