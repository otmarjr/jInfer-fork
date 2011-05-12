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
package cz.cuni.mff.ksi.jinfer.attrstats.logic;

import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeTreeNode;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.Triplet;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Library class containing utility functions for attribute mapping.
 *
 * Please see the article "Finding ID Attributes in XML Documents" for reference.
 *
 * @author vektor
 */
public final class MappingUtils {

  private MappingUtils() {

  }

  /**
   * Extracts the attribute mapping from the specified grammar as a flat list
   * of {@link Triplet}s <code>(element name, attribute name, attribute content)</code>.
   *
   * <p>Note that the original content of the attribute is split into tokens on spaces.
   * For example an element <code>e</code> with attribute <code>a</code> that
   * contains the string <code>"1 p p"</code> in the original document will
   * produce 3 {@link Triplet}s, containing the <code>attribute content</code>
   * <code>1</code>, <code>p</code> and <code>p</code> respectively.</p>
   *
   * @param grammar Grammar from which the attribute mapping should be retrieved.
   * @return Flat representation of the attribute mapping contained in the
   * specified grammar. Resulting list of {@link Triplet}s is sorted
   * (see {@link Triplet#compareTo(Triplet)}).
   */
  public static List<Triplet> extractFlat(final List<Element> grammar) {
    final List<Triplet> ret = new ArrayList<Triplet>();
    for (final Element e : grammar) {
      if (!BaseUtils.isEmpty(e.getAttributes())) {
        for (final Attribute a : e.getAttributes()) {
          for (final String c : a.getContent()) {
            final String[] values = c.split(" ");
            for (final String value : values) {
              ret.add(new Triplet(e.getName(), a.getName(), value));
            }
          }
        }
      }
    }

    Collections.sort(ret);

    return ret;
  }

  /**
   * Extracts the attribute mapping from the specified grammar as a tree ready
   * to be used in a {@link JTree}.
   *
   * <p>Please note that the attribute content is split into tokens on spaces,
   * see {@link MappingUtils#extractFlat } for details.</p>
   *
   * @param grammar Grammar from which the attribute mapping should be retrieved.
   * @return Tree representation of the attribute mapping contained in the
   * specified grammar. Under the root node there are nodes representing each
   * {@link Element} in the grammar. The element nodes then contain nodes for
   * each of the {@link Attribute}s they contain.
   */
  public static TreeNode createTree(final List<Element> grammar) {
    Collections.sort(grammar, BaseUtils.NAMED_NODE_COMPARATOR);

    final DefaultMutableTreeNode ret = new DefaultMutableTreeNode("");
    for (final Element e : grammar) {
      if (!BaseUtils.isEmpty(e.getAttributes())) {
        final DefaultMutableTreeNode elementNode = new DefaultMutableTreeNode(e.getName());
        final List<Attribute> attributes = new ArrayList<Attribute>(e.getAttributes());
        Collections.sort(attributes, BaseUtils.NAMED_NODE_COMPARATOR);
        for (final Attribute a : attributes) {
          final List<String> content = new ArrayList<String>();
          for (final String oneContent : a.getContent()) {
            final String[] values = oneContent.split(" ");
            content.addAll(Arrays.asList(values));
          }

          final AttributeTreeNode attributeNode =
                  new AttributeTreeNode(e.getName(), a.getName(), content);
          elementNode.add(attributeNode);
        }
        ret.add(elementNode);
      }
    }

    return ret;
  }

  /**
   * Please see the "Finding ID Attributes in XML Documents" article for the
   * definition of this function.
   *
   * @param targetMapping Mapping for which <cite>support</cite> should be calculated.
   * @param allMappings All the attribute mappings of the grammar.
   * @return Support of the specified mapping.
   */
  public static double support(final Pair<String, String> targetMapping, final List<Triplet> allMappings) {
    if (targetMapping == null || BaseUtils.isEmpty(allMappings)) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }

    int mappingSize = 0;
    for (final Triplet triplet : allMappings) {
      if (triplet.getElement().equals(targetMapping.getFirst())
              && triplet.getAttribute().equals(targetMapping.getSecond())) {
        mappingSize++;
      }
    }
    return (double)mappingSize / allMappings.size();
  }

  /**
   * Please see the "Finding ID Attributes in XML Documents" article for the
   * definition of this function.
   *
   * @param targetMapping Mapping for which <cite>coverage</cite> should be calculated.
   * @param allMappings All the attribute mappings of the grammar.
   * @return Coverage of the specified mapping.
   */
  public static double coverage(final Pair<String, String> targetMapping, final List<Triplet> allMappings) {
    if (targetMapping == null || BaseUtils.isEmpty(allMappings)) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }

    // this will be a mapping (element, attribute) -> {domain}
    final Map<Pair<String, String>, Set<String>> map = new HashMap<Pair<String, String>, Set<String>>();

    for (final Triplet triplet : allMappings) {
      final Pair<String, String> key = new Pair<String, String>(triplet.getElement(), triplet.getAttribute());
      if (!map.containsKey(key)) {
        map.put(key, new HashSet<String>());
      }
      map.get(key).add(triplet.getValue());
    }

    if (!map.containsKey(targetMapping)) {
      throw new IllegalArgumentException("All of the attribute mappings must contain the mapping for which the coverage is calculated.");
    }

    final Set<String> targetImage = map.get(targetMapping);

    double sum1 = 0;

    for (final Map.Entry<Pair<String, String>, Set<String>> mapping : map.entrySet()) {
      if (!mapping.getKey().equals(targetMapping)) {
        sum1 += BaseUtils.intersect(mapping.getValue(), targetImage).size();
      }
    }

    double sumImageSizes = 0;
    for (final Set<String> s : map.values()) {
      sumImageSizes += s.size();
    }

    return sum1 / sumImageSizes;
  }

  /**
   * Verifies whether the specified attribute mapping is a candidate mapping.
   *
   * <p>An attribute mapping is a <cite>candidate mapping</cite>, iff it is
   * an injective function: if its every value is unique.</p>
   *
   * @param targetMapping Mapping to be verified.
   * @param allMappings All the attribute mappings of the grammar.
   * @return True if the specified mapping is a candidate, false otherwise.
   */
  public static boolean isCandidateMapping(final Pair<String, String> targetMapping, final List<Triplet> allMappings) {
    if (targetMapping == null || BaseUtils.isEmpty(allMappings)) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }

    final Set<String> domain = new HashSet<String>();

    for (final Triplet triplet : allMappings) {
      if (triplet.getElement().equals(targetMapping.getFirst())
              && triplet.getAttribute().equals(targetMapping.getSecond())) {
        if (domain.contains(triplet.getValue())) {
          return false;
        }
        domain.add(triplet.getValue());
      }
    }

    return true;
  }

  /**
   * Determines whether a list of attribute mappings constitutes an ID set.
   *
   * Three conditions must hold for a set of mappings to be an ID set:
   * <ol>
   *  <li>All the attribute mappings must be <cite>candidate mappings</cite>
   *  (see {@link MappingUtils#isCandidateMapping}).</li>
   *  <li>The <cite>types</cite> of all the mappings (= their respective
   *  element names) must be distinct.</li>
   *  <li>The <cite>domains</cite> of all the mappings (= sets of their values)
   *  must be distincs (no overlaps).</li>
   * </ol>
   *
   * @param mappings List of the attribute mappings to verify.
   * @param allMappings All the attribute mappings of the grammar.
   * @return True if the specified list consitutes an ID set, false otherwise.
   */
  public static boolean isIDset(final List<Pair<String, String>> mappings, final List<Triplet> allMappings) {
    if (BaseUtils.isEmpty(mappings) || BaseUtils.isEmpty(allMappings)) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }

    final Set<String> types = new HashSet<String>();
    for (final Pair<String, String> mapping : mappings) {
      // 1. every single mapping must be a candidate mapping
      if (!isCandidateMapping(mapping, allMappings)) {
        return false;
      }
      // 2. all the types (= element names) must be distinct
      if (types.contains(mapping.getFirst())) {
        return false;
      }
      types.add(mapping.getFirst());
    }

    // 3. all the domains must be distinct
    final Set<String> values = new HashSet<String>();

    for (final Triplet triplet : allMappings) {
      final Pair<String, String> p = new Pair<String, String>(triplet.getElement(), triplet.getAttribute());
      if (mappings.contains(p)) {
        if (values.contains(triplet.getValue())) {
          return false;
        }
        values.add(triplet.getValue());
      }
    }

    return true;
  }
}
