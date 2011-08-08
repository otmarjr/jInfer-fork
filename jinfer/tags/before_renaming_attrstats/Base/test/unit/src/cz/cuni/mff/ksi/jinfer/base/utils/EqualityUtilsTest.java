/*
 *  Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.base.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class EqualityUtilsTest {

  @Test(expected = IllegalArgumentException.class)
  public void testSameElementsNull1() {
    System.out.println("sameElementsNull1");
    final Element e1 = null;
    final Element e2 = TestUtils.getElement("e");
    final int ignore = 0;
    EqualityUtils.sameElements(e1, e2, ignore);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSameElementsNull2() {
    System.out.println("sameElementsNull2");
    final Element e1 = TestUtils.getElement("e");
    final Element e2 = null;
    final int ignore = 0;
    EqualityUtils.sameElements(e1, e2, ignore);
  }

  @Test
  public void testSameElementsNoIgnore1() {
    System.out.println("sameElementsNoIgnore1");
    final Element e1 = TestUtils.getElement("e");
    final Element e2 = TestUtils.getElement("e");
    final int ignore = 0;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsNoIgnore2() {
    System.out.println("sameElementsNoIgnore2");
    final Regexp<AbstractStructuralNode> subnodes1 = TestUtils.getToken(TestUtils.getElement("e1"));
    final Regexp<AbstractStructuralNode> subnodes2 = TestUtils.getToken(TestUtils.getElement("e1"));

    final Element e1 = TestUtils.getElement("e", subnodes1);
    final Element e2 = TestUtils.getElement("e", subnodes2);
    final int ignore = 0;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsNoIgnore3() {
    System.out.println("sameElementsNoIgnore3");
    final Element e1 = TestUtils.getElement("e1");
    final Element e2 = TestUtils.getElement("e2");
    final int ignore = 0;
    final boolean expResult = false;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsNoIgnore4() {
    System.out.println("sameElementsNoIgnore4");
    final Regexp<AbstractStructuralNode> subnodes1 = TestUtils.getToken(TestUtils.getElement("e1"));
    final Regexp<AbstractStructuralNode> subnodes2 = TestUtils.getToken(TestUtils.getElement("e2"));

    final Element e1 = TestUtils.getElement("e", subnodes1);
    final Element e2 = TestUtils.getElement("e", subnodes2);
    final int ignore = 0;
    final boolean expResult = false;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsIgnore1() {
    System.out.println("sameElementsIgnore1");
    final Element e1 = TestUtils.getElement("e");
    final Element e2 = TestUtils.getElement("e");
    final int ignore = EqualityUtils.IGNORE_NAME;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsIgnore2() {
    System.out.println("sameElementsIgnore2");
    final Regexp<AbstractStructuralNode> subnodes1 = TestUtils.getToken(TestUtils.getElement("e1"));
    final Regexp<AbstractStructuralNode> subnodes2 = TestUtils.getToken(TestUtils.getElement("e1"));

    final Element e1 = TestUtils.getElement("e", subnodes1);
    final Element e2 = TestUtils.getElement("e", subnodes2);
    final int ignore = EqualityUtils.IGNORE_SUBNODES;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsIgnore3() {
    System.out.println("sameElementsIgnore3");
    final Element e1 = TestUtils.getElement("e1");
    final Element e2 = TestUtils.getElement("e2");
    final int ignore = EqualityUtils.IGNORE_NAME;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsIgnore4() {
    System.out.println("sameElementsIgnore4");
    final Regexp<AbstractStructuralNode> subnodes1 = TestUtils.getToken(TestUtils.getElement("e1"));
    final Regexp<AbstractStructuralNode> subnodes2 = TestUtils.getToken(TestUtils.getElement("e2"));

    final Element e1 = TestUtils.getElement("e", subnodes1);
    final Element e2 = TestUtils.getElement("e", subnodes2);
    final int ignore = EqualityUtils.IGNORE_SUBNODES;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameElementsCompleteIgnore() {
    System.out.println("sameElementsCompleteIgnore");
    final Regexp<AbstractStructuralNode> subnodes1 = TestUtils.getToken(TestUtils.getElement("e1"));
    final Regexp<AbstractStructuralNode> subnodes2 = TestUtils.getToken(TestUtils.getElement("e2"));

    final Map<String, Object> metadata1 = new HashMap<String, Object>();
    metadata1.put("a", "a");
    final Map<String, Object> metadata2 = new HashMap<String, Object>();
    metadata2.put("b", "b");

    final List<Attribute> attributes1 = new ArrayList<Attribute>();
    attributes1.add(TestUtils.getAttribute("a"));
    final List<Attribute> attributes2 = new ArrayList<Attribute>();
    attributes2.add(TestUtils.getAttribute("b"));

    final Element e1 = new Element(Arrays.<String>asList("a"), "e1", metadata1, subnodes1, attributes1);
    final Element e2 = new Element(Arrays.<String>asList("b"), "e2", metadata2, subnodes2, attributes2);
    final int ignore = EqualityUtils.IGNORE_ATTRIBUTES
            | EqualityUtils.IGNORE_NAME
            | EqualityUtils.IGNORE_METADATA
            | EqualityUtils.IGNORE_CONTEXT
            | EqualityUtils.IGNORE_SUBNODES;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameElements(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSameSimpleDataNull1() {
    System.out.println("sameSimpleDataNull1");
    final SimpleData e1 = null;
    final SimpleData e2 = TestUtils.getSimpleData("e");
    final int ignore = 0;
    EqualityUtils.sameSimpleData(e1, e2, ignore);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSameSimpleDataNull2() {
    System.out.println("sameSimpleDataNull2");
    final SimpleData e1 = TestUtils.getSimpleData("e");
    final SimpleData e2 = null;
    final int ignore = 0;
    EqualityUtils.sameSimpleData(e1, e2, ignore);
  }

  @Test
  public void testSameSimpleDataNoIgnore1() {
    System.out.println("sameSimpleDataNoIgnore1");
    final SimpleData e1 = TestUtils.getSimpleData("e");
    final SimpleData e2 = TestUtils.getSimpleData("e");
    final int ignore = 0;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameSimpleData(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameSimpleDataNoIgnore3() {
    System.out.println("sameSimpleDataNoIgnore3");
    final SimpleData e1 = TestUtils.getSimpleData("e1");
    final SimpleData e2 = TestUtils.getSimpleData("e2");
    final int ignore = 0;
    final boolean expResult = false;
    final boolean result = EqualityUtils.sameSimpleData(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameSimpleDataIgnore1() {
    System.out.println("sameSimpleDataIgnore1");
    final SimpleData e1 = TestUtils.getSimpleData("e");
    final SimpleData e2 = TestUtils.getSimpleData("e");
    final int ignore = EqualityUtils.IGNORE_CONTENT;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameSimpleData(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameSimpleDataIgnore3() {
    System.out.println("sameSimpleDataIgnore3");
    final SimpleData e1 = TestUtils.getSimpleData("e1");
    final SimpleData e2 = TestUtils.getSimpleData("e2");
    final int ignore = EqualityUtils.IGNORE_CONTENT;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameSimpleData(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameSimpleDataCompleteIgnore() {
    System.out.println("sameSimpleDataCompleteIgnore");
    final Map<String, Object> metadata1 = new HashMap<String, Object>();
    metadata1.put("a", "a");
    final Map<String, Object> metadata2 = new HashMap<String, Object>();
    metadata2.put("b", "b");

    final List<Attribute> attributes1 = new ArrayList<Attribute>();
    attributes1.add(TestUtils.getAttribute("a"));
    final List<Attribute> attributes2 = new ArrayList<Attribute>();
    attributes2.add(TestUtils.getAttribute("b"));

    final SimpleData e1 = new SimpleData(Arrays.<String>asList("a"), "e1", metadata1, "t1", Arrays.<String>asList("c1"));
    final SimpleData e2 = new SimpleData(Arrays.<String>asList("b"), "e2", metadata2, "t2", Arrays.<String>asList("c2"));
    final int ignore = EqualityUtils.IGNORE_ATTRIBUTES
            | EqualityUtils.IGNORE_NAME
            | EqualityUtils.IGNORE_METADATA
            | EqualityUtils.IGNORE_CONTEXT
            | EqualityUtils.IGNORE_CONTENT
            | EqualityUtils.IGNORE_CONTENT_TYPE;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameSimpleData(e1, e2, ignore);
    assertEquals(expResult, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSameIntervalsNull() {
    System.out.println("same intevals null");
    final RegexpInterval i1 = null;
    final RegexpInterval i2 = null;
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameIntervalsUnbounded() {
    System.out.println("same intevals unbounded");
    int min = 5;
    final RegexpInterval i1 = RegexpInterval.getUnbounded(min);
    final RegexpInterval i2 = RegexpInterval.getUnbounded(min);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameIntervalsUnbounded2() {
    System.out.println("same intevals unbounded 2");
    final RegexpInterval i1 = RegexpInterval.getUnbounded(2);
    final RegexpInterval i2 = RegexpInterval.getUnbounded(333);
    final boolean expResult = false;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameIntervalsUnbounded3() {
    System.out.println("same intevals unbounded 3");
    final int min = 5;
    final RegexpInterval i1 = RegexpInterval.getBounded(min, 100);
    final RegexpInterval i2 = RegexpInterval.getUnbounded(min);
    final boolean expResult = false;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameIntervalsUnbounded4() {
    System.out.println("same intevals unbounded 4");
    final RegexpInterval i1 = RegexpInterval.getKleeneCross();
    final RegexpInterval i2 = RegexpInterval.getUnbounded(1);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }
  
  @Test
  public void testSameIntervalsOnce() {
    System.out.println("same intevals once");
    final RegexpInterval i1 = RegexpInterval.getOnce();
    final RegexpInterval i2 = RegexpInterval.getOnce();
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameIntervalsInstance() {
    System.out.println("same intevals same instance");
    final RegexpInterval i1 = RegexpInterval.getOnce();
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i1);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameIntervalsBounded() {
    System.out.println("same intevals bounded");
    final int min = 42;
    final int max = 42;
    final RegexpInterval i1 = RegexpInterval.getBounded(min, max);
    final RegexpInterval i2 = RegexpInterval.getBounded(min, max);
    final boolean expResult = true;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }

  @Test
  public void testSameIntervalsBounded2() {
    System.out.println("same intevals bounded 2");
    final int min = 42;
    final int max = 42;
    final RegexpInterval i1 = RegexpInterval.getBounded(min, max);
    final RegexpInterval i2 = RegexpInterval.getBounded(min, max+max);
    final boolean expResult = false;
    final boolean result = EqualityUtils.sameIntervals(i1, i2);
    assertEquals(expResult, result);
  }
}
