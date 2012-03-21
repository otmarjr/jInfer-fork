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
package cz.cuni.mff.ksi.jinfer.iss.utils;

import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class MappingUtilsTest {

  private static final AMModel TRIVIAL_MODEL = new AMModel(Arrays.asList(TestUtils.getElement("e")));
  private static final AttributeMappingId NULL_MAPPING = new AttributeMappingId(null, null);
  private static final AttributeMappingId TARGET_MAPPING = new AttributeMappingId("element", "attribute");
  private static final AttributeMappingId OTHER_MAPPING = new AttributeMappingId("otherElement", "attribute");
  private static final Element TARGET_ELEMENT = TestUtils.getElementWithAttribute("element", "attribute", "value");

  public static final AMModel MODEL_FROM_ARTICLE = new AMModel(Arrays.asList(
          TestUtils.getElementWithAttribute("a", "x", "1"),
          TestUtils.getElementWithAttribute("b", "y", "1"),
          TestUtils.getElementWithAttribute("a", "x", "2"),
          TestUtils.getElementWithAttribute("a", "z", "p"),
          TestUtils.getElementWithAttribute("c", "w", "1"),
          TestUtils.getElementWithAttribute("c", "w", "p"),
          TestUtils.getElementWithAttribute("c", "w", "p")));
  public static final AttributeMappingId EXAMPLE_MAPPING_1 = new AttributeMappingId("a", "x");
  public static final AttributeMappingId EXAMPLE_MAPPING_2 = new AttributeMappingId("a", "z");
  public static final AttributeMappingId EXAMPLE_MAPPING_3 = new AttributeMappingId("b", "y");
  public static final AttributeMappingId EXAMPLE_MAPPING_4 = new AttributeMappingId("c", "w");

  @Test(expected = IllegalArgumentException.class)
  public void testCandidateEmpty1() {
    System.out.println("candidateEmpty1");
    isCandidateMapping(null, TRIVIAL_MODEL);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCandidateEmpty2() {
    System.out.println("candidateEmpty2");
    isCandidateMapping(NULL_MAPPING, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCandidateNotFound() {
    System.out.println("candidateNotFound");
    isCandidateMapping(TARGET_MAPPING, MODEL_FROM_ARTICLE);
  }

  @Test
  public void testCandidate() {
    System.out.println("candidate");
    assertTrue("a->x must be a candidate mapping.", isCandidateMapping(EXAMPLE_MAPPING_1, MODEL_FROM_ARTICLE));
    assertTrue("a->z must be a candidate mapping.", isCandidateMapping(EXAMPLE_MAPPING_2, MODEL_FROM_ARTICLE));
    assertTrue("b->y must be a candidate mapping.", isCandidateMapping(EXAMPLE_MAPPING_3, MODEL_FROM_ARTICLE));
    assertFalse("c->w must not be a candidate mapping.", isCandidateMapping(EXAMPLE_MAPPING_4, MODEL_FROM_ARTICLE));
  }

  @Test
  public void testIDset() {
    System.out.println("IDset");
    assertTrue("a->x must be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_1), MODEL_FROM_ARTICLE));
    assertTrue("a->z must be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_2), MODEL_FROM_ARTICLE));
    assertTrue("b->y must be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_3), MODEL_FROM_ARTICLE));
    assertTrue("{a->z, b->y} must be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_2, EXAMPLE_MAPPING_3), MODEL_FROM_ARTICLE));
    assertFalse("c->w must not be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_4), MODEL_FROM_ARTICLE));
    assertFalse("{a->x, a->z} must not be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_2), MODEL_FROM_ARTICLE));
    assertFalse("{a->x, a->x} must not be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_1), MODEL_FROM_ARTICLE));
    assertFalse("{a->x, c->w} must not be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_4), MODEL_FROM_ARTICLE));
    assertFalse("All mappings together must not be an ID set.", isIDset(Arrays.asList(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_2, EXAMPLE_MAPPING_3, EXAMPLE_MAPPING_4), MODEL_FROM_ARTICLE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImagesIntersectEmpty1() {
    System.out.println("imagesIntersectEmpty1");
    imagesIntersect(null, NULL_MAPPING, TRIVIAL_MODEL);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImagesIntersectEmpty2() {
    System.out.println("imagesIntersectEmpty2");
    imagesIntersect(NULL_MAPPING, null, TRIVIAL_MODEL);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImagesIntersectEmpty3() {
    System.out.println("imagesIntersectEmpty3");
    imagesIntersect(NULL_MAPPING, NULL_MAPPING, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImagesIntersectNotFound1() {
    System.out.println("imagesIntersectNotFound1");
    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(TARGET_ELEMENT);
    imagesIntersect(OTHER_MAPPING, TARGET_MAPPING, new AMModel(grammar));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImagesIntersectNotFound2() {
    System.out.println("imagesIntersectNotFound2");
    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(TARGET_ELEMENT);
    imagesIntersect(TARGET_MAPPING, OTHER_MAPPING, new AMModel(grammar));
  }

  @Test
  @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
  public void testImagesIntersect() {
    System.out.println("imagesIntersect");

    assertFalse(imagesIntersect(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_2, MODEL_FROM_ARTICLE));
    assertTrue (imagesIntersect(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_3, MODEL_FROM_ARTICLE));
    assertTrue (imagesIntersect(EXAMPLE_MAPPING_1, EXAMPLE_MAPPING_4, MODEL_FROM_ARTICLE));

    assertFalse(imagesIntersect(EXAMPLE_MAPPING_2, EXAMPLE_MAPPING_1, MODEL_FROM_ARTICLE));
    assertFalse(imagesIntersect(EXAMPLE_MAPPING_2, EXAMPLE_MAPPING_3, MODEL_FROM_ARTICLE));
    assertTrue (imagesIntersect(EXAMPLE_MAPPING_2, EXAMPLE_MAPPING_4, MODEL_FROM_ARTICLE));

    assertTrue (imagesIntersect(EXAMPLE_MAPPING_3, EXAMPLE_MAPPING_1, MODEL_FROM_ARTICLE));
    assertFalse(imagesIntersect(EXAMPLE_MAPPING_3, EXAMPLE_MAPPING_2, MODEL_FROM_ARTICLE));
    assertTrue (imagesIntersect(EXAMPLE_MAPPING_3, EXAMPLE_MAPPING_4, MODEL_FROM_ARTICLE));

    assertTrue (imagesIntersect(EXAMPLE_MAPPING_4, EXAMPLE_MAPPING_1, MODEL_FROM_ARTICLE));
    assertTrue (imagesIntersect(EXAMPLE_MAPPING_4, EXAMPLE_MAPPING_2, MODEL_FROM_ARTICLE));
    assertTrue (imagesIntersect(EXAMPLE_MAPPING_4, EXAMPLE_MAPPING_3, MODEL_FROM_ARTICLE));
  }
}
