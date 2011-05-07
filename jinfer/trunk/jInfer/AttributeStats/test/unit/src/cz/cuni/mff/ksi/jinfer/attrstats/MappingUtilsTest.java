/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.attrstats;

import java.util.Set;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.swing.tree.TreeNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
public class MappingUtilsTest {

  private final static Random RND = new Random();
  private static final int ITERATIONS = 100;

  private static final Triplet TARGET_MAPPING = new Triplet("element", "attribute", "value");
  private static final Triplet OTHER_MAPPING = new Triplet("otherElement", "attribute", "value");

  //@Test
  // TODO vektor empty test

  public void testExtractFlat() {
    System.out.println("extractFlat");
    List<Element> grammar = null;
    List expResult = null;
    List result = MappingUtils.extractFlat(grammar);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  //@Test
  // TODO vektor empty test

  public void testCreateTree() {
    System.out.println("createTree");
    List<Element> grammar = null;
    TreeNode expResult = null;
    TreeNode result = MappingUtils.createTree(grammar);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSupportEmpty1() {
    System.out.println("supportEmpty1");
    Triplet targetMapping = null;
    List<Triplet> allMappings = new ArrayList<Triplet>();
    MappingUtils.support(targetMapping, allMappings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSupportEmpty2() {
    System.out.println("supportEmpty2");
    Triplet targetMapping = new Triplet(null, null, null);
    List<Triplet> allMappings = null;
    MappingUtils.support(targetMapping, allMappings);
  }

  @Test
  public void testSupport1() {
    System.out.println("support1");
    final List<Triplet> allMappings = new ArrayList<Triplet>(10);
    for (int i = 0; i < 10; i++) {
      allMappings.add(TARGET_MAPPING);
    }
    final double expResult = 1.0;
    final double result = MappingUtils.support(TARGET_MAPPING, allMappings);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testSupport2() {
    System.out.println("support2");
    final List<Triplet> allMappings = new ArrayList<Triplet>(10);
    for (int i = 0; i < 10; i++) {
      allMappings.add(OTHER_MAPPING);
    }
    final double expResult = 0.0;
    final double result = MappingUtils.support(TARGET_MAPPING, allMappings);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testSupport3() {
    System.out.println("support3");
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Triplet> allMappings = new ArrayList<Triplet>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        allMappings.add(RND.nextBoolean() ? OTHER_MAPPING : TARGET_MAPPING);
      }
      final double result = MappingUtils.support(TARGET_MAPPING, allMappings);
      assertTrue("Support of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty1() {
    System.out.println("coverageEmpty1");
    Triplet targetMapping = null;
    List<Triplet> allMappings = new ArrayList<Triplet>();
    MappingUtils.coverage(targetMapping, allMappings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty2() {
    System.out.println("coverageEmpty2");
    Triplet targetMapping = new Triplet(null, null, null);
    List<Triplet> allMappings = null;
    MappingUtils.coverage(targetMapping, allMappings);
  }

  // TODO vektor test coverage 0 and 1!

  public void testCoverage() {
    System.out.println("coverage");
    Triplet targetMapping = null;
    List<Triplet> allMappings = null;
    double expResult = 0.0;
    double result = MappingUtils.coverage(targetMapping, allMappings);
    assertEquals(expResult, result, 0.0);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  @Test
  public void testCoverage3() {
    System.out.println("coverage3");

    for (int j = 0; j < ITERATIONS; j++) {
      final List<Triplet> allMappings = new ArrayList<Triplet>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        allMappings.add(RND.nextBoolean() ? OTHER_MAPPING : TARGET_MAPPING);
      }
      final double result = MappingUtils.coverage(TARGET_MAPPING, allMappings);
      assertTrue("Coverage of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossectionNull() {
    System.out.println("crossectionNull");
    MappingUtils.crossection(null, null);
  }

  @Test
  public void testCrossectionEmpty() {
    System.out.println("crossectionEmpty");
    final Set<String> arg1 = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> arg2 = new HashSet<String>();

    final Set<String> ret1 = MappingUtils.crossection(arg1, arg2);
    assertEquals("Expecting empty crossection", arg2, ret1);

    final Set<String> ret2 = MappingUtils.crossection(arg2, arg1);
    assertEquals("Expecting empty crossection", arg2, ret2);
  }

  @Test
  public void testCrossection1() {
    System.out.println("crossection1");
    final Set<String> arg1 = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> arg2 = new HashSet<String>(Arrays.asList("b", "c", "d"));
    final Set<String> expected = new HashSet<String>(Arrays.asList("b", "c"));

    final Set<String> ret = MappingUtils.crossection(arg1, arg2);
    assertEquals(expected, ret);
  }

  @Test
  public void testCrossection2() {
    System.out.println("crossection2");
    final Set<String> arg1 = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> arg2 = new HashSet<String>(Arrays.asList("d", "e", "f"));
    final Set<String> expected = new HashSet<String>();

    final Set<String> ret = MappingUtils.crossection(arg1, arg2);
    assertEquals(expected, ret);
  }

}
