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
package cz.cuni.mff.ksi.jinfer.trivialdtd.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class DomainUtilsTest {

  private static final int THRESHOLD = 3;

  @Test(expected = NullPointerException.class)
  public void testGetDomainNull() {
    System.out.println("testGetDomainNull");
    DomainUtils.getDomain(null);
  }

  @Test
  public void testGetDomainEmpty() {
    System.out.println("testGetDomainEmpty");
    final Attribute a = new Attribute(null, "a", null, null, null);
    if (!DomainUtils.getDomain(a).isEmpty()) {
      fail();
    }
  }

  @Test
  public void testGetDomainSimple() {
    System.out.println("testGetDomainSimple");
    final Attribute a = new Attribute(null, "a", null, null, Arrays.asList("a", "b", "c"));
    final Map<String, Integer> domain = DomainUtils.getDomain(a);
    assertEquals(domain.size(), 3);
    assertEquals(Integer.valueOf(1), domain.get("a"));
    assertEquals(Integer.valueOf(1), domain.get("b"));
    assertEquals(Integer.valueOf(1), domain.get("c"));
  }

  @Test
  public void testGetDomainMoreComplex() {
    System.out.println("testGetDomainMoreComplex");
    final Attribute a = new Attribute(null, "a", null, null, Arrays.asList("a", "a", "b", "a", "c", "c"));
    final Map<String, Integer> domain = DomainUtils.getDomain(a);
    assertEquals(domain.size(), 3);
    assertEquals(Integer.valueOf(3), domain.get("a"));
    assertEquals(Integer.valueOf(1), domain.get("b"));
    assertEquals(Integer.valueOf(2), domain.get("c"));
  }

  @Test
  public void testGetDomainComplex() {
    System.out.println("testGetDomainComplex");
    final Attribute a = new Attribute(null, "a", null, null, Arrays.asList("c", "a", "a", "d", "b", "a", "c", "c"));
    final Map<String, Integer> domain = DomainUtils.getDomain(a);
    assertEquals(domain.size(), 4);
    assertEquals(Integer.valueOf(3), domain.get("a"));
    assertEquals(Integer.valueOf(1), domain.get("b"));
    assertEquals(Integer.valueOf(3), domain.get("c"));
    assertEquals(Integer.valueOf(1), domain.get("d"));
  }

  @Test(expected = NullPointerException.class)
  public void testGetDomainTypeNull() {
    System.out.println("testGetDomainTypeNull");
    DomainUtils.getDomainType(null, THRESHOLD);
  }

  @Test
  public void testGetDomainTypeEmpty() {
    System.out.println("testGetDomainTypeEmpty");
    Map<String, Integer> domain = new HashMap<String, Integer>();
    String expResult = " () ";
    String result = DomainUtils.getDomainType(domain, THRESHOLD);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDomainTypeOneOfThree() {
    System.out.println("testGetDomainTypeOneOfThree");
    Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    String expResult = " (a) ";
    String result = DomainUtils.getDomainType(domain, THRESHOLD);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDomainTypeThreeOfThree() {
    System.out.println("testGetDomainTypeThreeOfThree");
    Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    domain.put("b", Integer.valueOf(2));
    domain.put("c", Integer.valueOf(3));
    String expResult = " (a|b|c) ";
    String result = DomainUtils.getDomainType(domain, THRESHOLD);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDomainTypeFourOfThree() {
    System.out.println("testGetDomainTypeFourOfThree");
    Map<String, Integer> domain = new HashMap<String, Integer>();
    domain.put("a", Integer.valueOf(1));
    domain.put("b", Integer.valueOf(2));
    domain.put("c", Integer.valueOf(3));
    domain.put("d", Integer.valueOf(4));
    String expResult = " CDATA ";
    String result = DomainUtils.getDomainType(domain, THRESHOLD);
    assertEquals(expResult, result);
  }
}
