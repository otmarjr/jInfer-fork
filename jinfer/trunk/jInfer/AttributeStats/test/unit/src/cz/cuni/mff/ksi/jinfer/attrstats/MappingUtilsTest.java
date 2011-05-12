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

import cz.cuni.mff.ksi.jinfer.attrstats.logic.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
public class MappingUtilsTest {

  private final static Random RND = new Random();
  private static final int ITERATIONS = 100;

  private static final AttributeMappingId TARGET_MAPPING = new AttributeMappingId("element", "attribute");
  private static final Element OTHER_MAPPING = TestUtils.getElementWithAttribute("otherElement", "attribute", "value");

  @Test(expected = IllegalArgumentException.class)
  public void testSupportEmpty1() {
    System.out.println("supportEmpty1");
    final AttributeMappingId targetMapping = null;
    final AMModel model = new AMModel(Arrays.asList(TestUtils.getElement("e")));
    MappingUtils.support(targetMapping, model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSupportEmpty2() {
    System.out.println("supportEmpty2");
    final AttributeMappingId targetMapping = new AttributeMappingId(null, null);
    final AMModel model = null;
    MappingUtils.support(targetMapping, model);
  }

  @Test
  public void testSupport1() {
    System.out.println("support1");
    final List<Element> grammar = new ArrayList<Element>(10);
    for (int i = 0; i < 10; i++) {
      grammar.add(TestUtils.getElementWithAttribute("element", "attribute", "value"));
    }
    final double expResult = 1.0;
    final double result = MappingUtils.support(TARGET_MAPPING, new AMModel(grammar));
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testSupport2() {
    System.out.println("support2");
    final List<Element> grammar = new ArrayList<Element>(10);
    grammar.add(TestUtils.getElementWithAttribute("element", "attribute", "value"));
    for (int i = 0; i < 10; i++) {
      grammar.add(TestUtils.getElementWithAttribute("element2", "attribute2", "value2"));
    }
    final double expResult = (double)1 / 11;
    final double result = MappingUtils.support(TARGET_MAPPING, new AMModel(grammar));
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testSupport3() {
    System.out.println("support3");
    final Element e1 = TestUtils.getElementWithAttribute("element", "attribute", "value");
    final Element e2 = TestUtils.getElementWithAttribute("element2", "attribute2", "value2");
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Element> grammar = new ArrayList<Element>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        grammar.add(RND.nextBoolean() ? e1 : e2);
      }
      final double result = MappingUtils.support(TARGET_MAPPING, new AMModel(grammar));
      assertTrue("Support of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty1() {
    System.out.println("coverageEmpty1");
    final AttributeMappingId targetMapping = null;
    final AMModel model = new AMModel(Arrays.asList(TestUtils.getElement("e")));
    MappingUtils.coverage(targetMapping, model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoverageEmpty2() {
    System.out.println("coverageEmpty2");
    final AttributeMappingId targetMapping = new AttributeMappingId(null, null);
    final AMModel model = new AMModel(Arrays.asList(TestUtils.getElement("e")));
    MappingUtils.coverage(targetMapping, model);
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
    final double result = MappingUtils.coverage(targetMapping, new AMModel(grammar));
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
    final double result = MappingUtils.coverage(targetMapping, new AMModel(grammar));
    assertEquals(expResult, result, 0.0);
  }

  @Test
  public void testCoverage3() {
    System.out.println("coverage3");
    final Element mapping = TestUtils.getElementWithAttribute("element", "attribute", "value");
    for (int j = 0; j < ITERATIONS; j++) {
      final List<Element> grammar = new ArrayList<Element>(ITERATIONS);
      for (int i = 0; i < ITERATIONS; i++) {
        grammar.add(RND.nextBoolean() ? OTHER_MAPPING : mapping);
      }
      final double result = MappingUtils.coverage(new AttributeMappingId("element", "attribute"), new AMModel(grammar));
      assertTrue("Coverage of an attribute mapping must be between zero and one.", result >= 0.0 && result <= 1.0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCandidateEmpty1() {
    System.out.println("candidateEmpty1");
    final AttributeMappingId targetMapping = null;
    MappingUtils.isCandidateMapping(targetMapping, new AMModel(Arrays.asList(TestUtils.getElement("e"))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCandidateEmpty2() {
    System.out.println("candidateEmpty2");
    AttributeMappingId targetMapping = new AttributeMappingId(null, null);
    MappingUtils.isCandidateMapping(targetMapping, null);
  }

  private static final AMModel MODEL_FROM_ARTICLE = new AMModel(Arrays.asList(
          TestUtils.getElementWithAttribute("a", "x", "1"),
          TestUtils.getElementWithAttribute("b", "y", "1"),
          TestUtils.getElementWithAttribute("a", "x", "2"),
          TestUtils.getElementWithAttribute("a", "z", "p"),
          TestUtils.getElementWithAttribute("c", "w", "1"),
          TestUtils.getElementWithAttribute("c", "w", "p"),
          TestUtils.getElementWithAttribute("c", "w", "p")
          ));
  private static final AttributeMappingId EXAMPLE_MAPPING_1 = new AttributeMappingId("a", "x");
  private static final AttributeMappingId EXAMPLE_MAPPING_2 = new AttributeMappingId("a", "z");
  private static final AttributeMappingId EXAMPLE_MAPPING_3 = new AttributeMappingId("b", "y");
  private static final AttributeMappingId EXAMPLE_MAPPING_4 = new AttributeMappingId("c", "w");

  @Test
  public void testCandidate() {
    System.out.println("candidate");
    assertTrue("a->x must be a candidate mapping.", MappingUtils.isCandidateMapping(EXAMPLE_MAPPING_1, MODEL_FROM_ARTICLE));
    assertTrue("a->z must be a candidate mapping.", MappingUtils.isCandidateMapping(EXAMPLE_MAPPING_2, MODEL_FROM_ARTICLE));
    assertTrue("b->y must be a candidate mapping.", MappingUtils.isCandidateMapping(EXAMPLE_MAPPING_3, MODEL_FROM_ARTICLE));
    assertTrue("c->w must not be a candidate mapping.", !MappingUtils.isCandidateMapping(EXAMPLE_MAPPING_4, MODEL_FROM_ARTICLE));
  }

  @Test
  public void testIDset() {
    System.out.println("IDset");
    assertTrue("a->x must be an ID set.", MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_1), MODEL_FROM_ARTICLE));
    assertTrue("a->z must be an ID set.", MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_2), MODEL_FROM_ARTICLE));
    assertTrue("b->y must be an ID set.", MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_3), MODEL_FROM_ARTICLE));
    assertTrue("{a->z, b->y} must be an ID set.", MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_2, EXAMPLE_MAPPING_3), MODEL_FROM_ARTICLE));
    assertTrue("c->w must not be an ID set.", !MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_4), MODEL_FROM_ARTICLE));
    assertTrue("{a->x, a->z} must not be an ID set.", !MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_2), MODEL_FROM_ARTICLE));
    assertTrue("{a->x, a->x} must not be an ID set.", !MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_1), MODEL_FROM_ARTICLE));
    assertTrue("{a->x, c->w} must not be an ID set.", !MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_4), MODEL_FROM_ARTICLE));
    assertTrue("All mappings together must not be an ID set.", !MappingUtils.isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_2, EXAMPLE_MAPPING_3, EXAMPLE_MAPPING_4), MODEL_FROM_ARTICLE));
  }
}
