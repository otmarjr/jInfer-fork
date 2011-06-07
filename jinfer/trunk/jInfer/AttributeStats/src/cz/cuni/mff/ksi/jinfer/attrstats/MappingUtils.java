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

import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMapping;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
   * Verifies whether the specified attribute mapping is a candidate mapping.
   *
   * <p>An attribute mapping is a <cite>candidate mapping</cite>, iff it is
   * an injective function: if its every value is unique.</p>
   *
   * @param targetMapping Mapping to be verified.
   * @param allMappings All the attribute mappings of the grammar.
   * @return True if the specified mapping is a candidate, false otherwise.
   */
  public static boolean isCandidateMapping(final AttributeMappingId targetMapping, final AMModel model) {
    verify(targetMapping, model);
    final AttributeMapping mapping = model.getAMs().get(targetMapping);
    final Set<String> domain = new HashSet<String>(mapping.getImage());
    return domain.size() == mapping.getImage().size();
  }

  private static void verify(final AttributeMappingId targetMapping, final AMModel model) {
    if (targetMapping == null || model == null) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }
    if (!model.getAMs().containsKey(targetMapping)) {
      throw new IllegalArgumentException("Target mapping not found in the model.");
    }
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
   * @return <code>True</code> if the specified list consitutes an ID set
   * (an empty set is ID set too), <code>false</code> otherwise.
   */
  public static boolean isIDset(final List<AttributeMappingId> mappings, final AMModel model) {
    if (mappings == null || model == null) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }
    if (BaseUtils.isEmpty(mappings)) {
      return true;
    }

    final Set<String> types = new HashSet<String>();
    for (final AttributeMappingId mapping : mappings) {
      // 1. every single mapping must be a candidate mapping
      if (!isCandidateMapping(mapping, model)) {
        return false;
      }
      // 2. all the types (= element names) must be distinct
      if (types.contains(mapping.getElement())) {
        return false;
      }
      types.add(mapping.getElement());
    }

    // 3. all the domains must be distinct
    final Set<String> values = new HashSet<String>();

    for (final AttributeMapping mapping : model.getAMs().values()) {
      if (mappings.contains(mapping.getId())) {
        for (final String value : mapping.getImage()) {
          if (values.contains(value)) {
            return false;
          }
          values.add(value);
        }
      }
    }

    return true;
  }

  /**
   * Checks whether the images of the two specified attribute mappings in the
   * specified model intersect.
   *
   * @param am1 ID of the first mapping to check.
   * @param am2 ID of the second mapping to check.
   * @param model Attribute mapping model to check intersection in.
   *
   * @return <code>true</code> if the two attribute mapping images intersect
   * (have at least one common value), <code>false</code> otherwise.
   */
  public static boolean imagesIntersect(
          final AttributeMappingId am1,
          final AttributeMappingId am2, final AMModel model) {
    if (am1 == null || am2 == null || model == null) {
      throw new IllegalArgumentException("Expecting non-null parameters");
    }
    if (!model.getAMs().containsKey(am1)) {
      throw new IllegalArgumentException("Mapping " + am1 + " not found in the model");
    }
    if (!model.getAMs().containsKey(am2)) {
      throw new IllegalArgumentException("Mapping " + am2 + " not found in the model");
    }
    return !BaseUtils.intersect(
            new HashSet<String>(model.getAMs().get(am1).getImage()),
            new HashSet<String>(model.getAMs().get(am2).getImage()))
            .isEmpty();
  }
}
