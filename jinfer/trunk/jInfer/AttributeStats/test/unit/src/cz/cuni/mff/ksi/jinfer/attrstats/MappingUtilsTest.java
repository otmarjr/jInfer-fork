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

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
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

  private static final Pair<String, String> TARGET_MAPPING = new Pair<String, String>("element", "attribute");
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
    Pair<String, String> targetMapping = null;
    List<Triplet> allMappings = new ArrayList<Triplet>();
    MappingUtils.support(targetMapping, allMappings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSupportEmpty2() {
    System.out.println("supportEmpty2");
    Pair<String, String> targetMapping = new Pair<String, String>(null, null);
    List<Triplet> allMappings = null;
    MappingUtils.support(targetMapping, allMappings);
  }

  @Test
  public void testSupport1() {
    System.out.println("support1");
    final List<Triplet> allMappings = new ArrayList<Triplet>(10);
    for (int i = 0; i < 10; i++) {
      allMappings.add(new Triplet("element", "attribute", "value"));
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
    final Triplet mapping = new Triplet("element", "attribute", "value");
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Triplet> allMappings = new ArrayList<Triplet>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        allMappings.add(RND.nextBoolean() ? OTHER_MAPPING : mapping);
      }
      final double result = MappingUtils.support(TARGET_MAPPING, allMappings);
      assertTrue("Support of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty1() {
    System.out.println("coverageEmpty1");
    Pair<String, String> targetMapping = null;
    List<Triplet> allMappings = new ArrayList<Triplet>();
    MappingUtils.coverage(targetMapping, allMappings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty2() {
    System.out.println("coverageEmpty2");
    Pair<String, String> targetMapping = new Pair<String, String>(null, null);
    List<Triplet> allMappings = null;
    MappingUtils.coverage(targetMapping, allMappings);
  }

  @Test
  public void testCoverage0() {
    System.out.println("coverage0");
    final Pair<String, String> targetMapping = new Pair<String, String>("e", "a");
    final List<Triplet> allMappings = new ArrayList<Triplet>();
    allMappings.add(new Triplet("e", "a", "d"));
    allMappings.add(new Triplet("e1", "a1", "e"));
    allMappings.add(new Triplet("e1", "a1", "f"));
    allMappings.add(new Triplet("e2", "a2", "g"));
    allMappings.add(new Triplet("e2", "a2", "g"));
    final double expResult = 0.0;
    final double result = MappingUtils.coverage(targetMapping, allMappings);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testCoverageMax() {
    System.out.println("coverageMax");
    final Pair<String, String> targetMapping = new Pair<String, String>("e", "a");
    final List<Triplet> allMappings = new ArrayList<Triplet>();
    allMappings.add(new Triplet("e", "a", "d"));
    allMappings.add(new Triplet("e1", "a1", "d"));
    allMappings.add(new Triplet("e1", "a1", "d"));
    allMappings.add(new Triplet("e2", "a2", "d"));
    allMappings.add(new Triplet("e2", "a2", "d"));
    allMappings.add(new Triplet("e3", "a2", "d"));
    allMappings.add(new Triplet("e3", "a2", "d"));
    final double expResult = 0.75;
    final double result = MappingUtils.coverage(targetMapping, allMappings);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testCoverage3() {
    System.out.println("coverage3");
    final Triplet mapping = new Triplet("element", "attribute", "value");
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Triplet> allMappings = new ArrayList<Triplet>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        allMappings.add(RND.nextBoolean() ? OTHER_MAPPING : mapping);
      }
      final double result = MappingUtils.coverage(TARGET_MAPPING, allMappings);
      assertTrue("Coverage of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }
}
