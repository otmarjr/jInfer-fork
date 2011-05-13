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
package cz.cuni.mff.ksi.jinfer.attrstats.objects;

import cz.cuni.mff.ksi.jinfer.attrstats.MappingUtilsTest;
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
public class AMModelTest {

  @Test
  public void testGetFlat() {
    System.out.println("getFlat");
    AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
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
    AMModel instance = MappingUtilsTest.MODEL_FROM_ARTICLE;
    final Set<String> expResult = new HashSet<String>(Arrays.asList("a", "b", "c"));
    final Set<String> result = instance.getTypes();
    assertEquals(expResult, result);
  }
}
