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
package cz.cuni.mff.ksi.jinfer.iss.objects;

import java.util.Random;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.ArrayList;
import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtilsTest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class AMModelTest {

  private final static Random RND = new Random();
  private static final int ITERATIONS = 100;
  private static final AMModel TRIVIAL_MODEL = new AMModel(Arrays.asList(TestUtils.getElement("e")));
  private static final AttributeMappingId NULL_MAPPING = new AttributeMappingId(null, null);
  private static final AttributeMappingId TARGET_MAPPING = new AttributeMappingId("element", "attribute");
  private static final AttributeMappingId OTHER_MAPPING = new AttributeMappingId("otherElement", "attribute");
  private static final Element TARGET_ELEMENT = TestUtils.getElementWithAttribute("element", "attribute", "value");
  private static final Element OTHER_ELEMENT = TestUtils.getElementWithAttribute("otherElement", "attribute", "value");

  @Test
  public void testGetFlat() {
    System.out.println("getFlat");
    final AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
    final List<Triplet> expResult = Arrays.asList(
            new Triplet("a", "x", "1"),
            new Triplet("a", "x", "2"),
            new Triplet("a", "z", "p"),
            new Triplet("b", "y", "1"),
            new Triplet("c", "w", "1"),
            new Triplet("c", "w", "p"),
            new Triplet("c", "w", "p"));
    final List<Triplet> result = instance.getFlat();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetTree() {
    System.out.println("getTree");
    final AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
    final TreeNode result = instance.getTree();
    assertTrue(result instanceof DefaultMutableTreeNode);
    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) result;
    assertEquals(7, node.getChildCount());
  }

  @Test
  public void testGetAMs() {
    System.out.println("getAMs");
    final AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
    final Map<AttributeMappingId, AttributeMapping> result = instance.getAMs();
    assertEquals(4, result.keySet().size());
    assertEquals(4, result.values().size());

    assertTrue(result.containsKey(MappingUtilsTest.EXAMPLE_MAPPING_1));
    assertTrue(result.containsKey(MappingUtilsTest.EXAMPLE_MAPPING_2));
    assertTrue(result.containsKey(MappingUtilsTest.EXAMPLE_MAPPING_3));
    assertTrue(result.containsKey(MappingUtilsTest.EXAMPLE_MAPPING_4));

    final AttributeMapping mapping1 = result.get(MappingUtilsTest.EXAMPLE_MAPPING_1);
    assertEquals(2, mapping1.size());
    assertTrue(mapping1.getImage().contains("1"));
    assertTrue(mapping1.getImage().contains("2"));

    final AttributeMapping mapping2 = result.get(MappingUtilsTest.EXAMPLE_MAPPING_2);
    assertEquals(1, mapping2.size());
    assertTrue(mapping2.getImage().contains("p"));

    final AttributeMapping mapping3 = result.get(MappingUtilsTest.EXAMPLE_MAPPING_3);
    assertEquals(1, mapping3.size());
    assertTrue(mapping3.getImage().contains("1"));

    final AttributeMapping mapping4 = result.get(MappingUtilsTest.EXAMPLE_MAPPING_4);
    assertEquals(3, mapping4.size());
    assertTrue(mapping4.getImage().contains("1"));
    assertTrue(mapping4.getImage().contains("p"));
  }

  @Test
  public void testSize1() {
    System.out.println("size1");
    final AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
    final int expResult = 7;
    final int result = instance.size();
    assertEquals(expResult, result);
  }

  @Test
  public void testSize2() {
    System.out.println("size2");
    final AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
    final int expResult = instance.getFlat().size();
    final int result = instance.size();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetTypes() {
    System.out.println("getTypes");
    final AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
    final Set<String> expResult = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> result = instance.getTypes();
    assertEquals(expResult, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSupportEmpty() {
    System.out.println("supportEmpty");
    TRIVIAL_MODEL.support(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSupportNotFound() {
    System.out.println("supportNotFound");
    final List<Element> grammar = new ArrayList<Element>(10);
    for (int i = 0; i < 10; i++) {
      grammar.add(TARGET_ELEMENT);
    }
    new AMModel(grammar).support(OTHER_MAPPING);
  }

  @Test
  public void testSupport1() {
    System.out.println("support1");
    final List<Element> grammar = new ArrayList<Element>(10);
    for (int i = 0; i < 10; i++) {
      grammar.add(TARGET_ELEMENT);
    }
    final double expResult = 1.0;
    final double result = new AMModel(grammar).support(TARGET_MAPPING);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testSupport2() {
    System.out.println("support2");
    final List<Element> grammar = new ArrayList<Element>(10);
    grammar.add(TARGET_ELEMENT);
    for (int i = 0; i < 10; i++) {
      grammar.add(OTHER_ELEMENT);
    }
    final double expResult = (double) 1 / 11;
    final double result = new AMModel(grammar).support(TARGET_MAPPING);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testSupport3() {
    System.out.println("support3");
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Element> grammar = new ArrayList<Element>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        grammar.add(RND.nextBoolean() ? TARGET_ELEMENT : OTHER_ELEMENT);
      }
      final double result = new AMModel(grammar).support(TARGET_MAPPING);
      assertTrue("Support of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty1() {
    System.out.println("coverageEmpty1");
    final AttributeMappingId targetMapping = null;
    TRIVIAL_MODEL.coverage(targetMapping);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty2() {
    System.out.println("coverageEmpty2");
    TRIVIAL_MODEL.coverage(NULL_MAPPING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageNotFound() {
    System.out.println("coverageNotFound");
    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "e"));
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "f"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "g"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "g"));
    new AMModel(grammar).coverage(TARGET_MAPPING);
  }

  @Test
  public void testCoverage0() {
    System.out.println("coverage0");
    final AttributeMappingId targetMapping = new AttributeMappingId("e", "a");
    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(TestUtils.getElementWithAttribute("e", "a", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "e"));
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "f"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "g"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "g"));
    final double expResult = 0.0;
    final double result = new AMModel(grammar).coverage(targetMapping);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testCoverageMax() {
    System.out.println("coverageMax");
    final AttributeMappingId targetMapping = new AttributeMappingId("e", "a");
    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(TestUtils.getElementWithAttribute("e", "a", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e3", "a2", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e3", "a2", "d"));
    final double expResult = (double) 3 / 7;
    final double result = new AMModel(grammar).coverage(targetMapping);
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testCoverage3() {
    System.out.println("coverage3");
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Element> grammar = new ArrayList<Element>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        grammar.add(RND.nextBoolean() ? TARGET_ELEMENT : OTHER_ELEMENT);
      }
      final double result = new AMModel(grammar).coverage(TARGET_MAPPING);
      assertTrue("Coverage of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }
}
