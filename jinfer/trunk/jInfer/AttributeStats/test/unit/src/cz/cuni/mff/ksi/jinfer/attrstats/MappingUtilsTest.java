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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.tree.TreeNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
public class MappingUtilsTest {

  //@Test
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
  public void testCreateTree() {
    System.out.println("createTree");
    List<Element> grammar = null;
    TreeNode expResult = null;
    TreeNode result = MappingUtils.createTree(grammar);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testSupportEmpty1() {
    System.out.println("supportEmpty1");
    Triplet targetMapping = null;
    List<Triplet> allMappings = new ArrayList<Triplet>();
    MappingUtils.support(targetMapping, allMappings);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testSupportEmpty2() {
    System.out.println("supportEmpty2");
    Triplet targetMapping = new Triplet(null, null, null);
    List<Triplet> allMappings = null;
    MappingUtils.support(targetMapping, allMappings);
  }

  @Test
  public void testSupport1() {
    System.out.println("support1");
    final Triplet targetMapping = new Triplet("element", "attribute", "value");
    final List<Triplet> allMappings = new ArrayList<Triplet>(10);
    for (int i = 0; i < 10; i++) {
      allMappings.add(targetMapping);
    }
    final double expResult = 1.0;
    final double result = MappingUtils.support(targetMapping, allMappings);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testSupport2() {
    System.out.println("support2");
    final Triplet targetMapping = new Triplet("element", "attribute", "value");
    final Triplet otherMapping = new Triplet("otherElement", "attribute", "value");
    final List<Triplet> allMappings = new ArrayList<Triplet>(10);
    for (int i = 0; i < 10; i++) {
      allMappings.add(otherMapping);
    }
    final double expResult = 0.0;
    final double result = MappingUtils.support(targetMapping, allMappings);
    assertEquals(expResult, result, 0.0);
  }

  private static final int ITERATIONS = 100;

  @Test
  public void testSupport3() {
    System.out.println("support3");
    final Triplet targetMapping = new Triplet("element", "attribute", "value");
    final Triplet otherMapping = new Triplet("otherElement", "attribute", "value");

    final Random random = new Random();
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Triplet> allMappings = new ArrayList<Triplet>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        allMappings.add(random.nextBoolean() ? otherMapping : targetMapping);
      }
      final double result = MappingUtils.support(targetMapping, allMappings);
      assertTrue("Support of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }

  //@Test
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
}
