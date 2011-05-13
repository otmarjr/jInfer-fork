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

import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMapping;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
   * Please see the "Finding ID Attributes in XML Documents" article for the
   * definition of this function.
   *
   * @param targetMapping Mapping for which <cite>support</cite> should be calculated.
   * @param allMappings All the attribute mappings of the grammar.
   * @return Support of the specified mapping.
   */
  public static double support(final AttributeMappingId targetMapping, final AMModel model) {
    if (targetMapping == null || model == null) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }

    // TODO vektor JUnit test this!
    if (!model.getAMs().containsKey(targetMapping)) {
      throw new IllegalArgumentException("Target mapping not found in the model.");
    }

    return (double)model.getAMs().get(targetMapping).size() / model.size();
  }

  /**
   * Please see the "Finding ID Attributes in XML Documents" article for the
   * definition of this function.
   *
   * @param targetMapping Mapping for which <cite>coverage</cite> should be calculated.
   * @param allMappings All the attribute mappings of the grammar.
   * @return Coverage of the specified mapping.
   */
  public static double coverage(final AttributeMappingId targetMapping, final AMModel model) {
    if (targetMapping == null || model == null) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }

    // TODO vektor JUnit test this!
    if (!model.getAMs().containsKey(targetMapping)) {
      throw new IllegalArgumentException("Target mapping not found in the model.");
    }

    double sum1 = 0;

    for (final Map.Entry<AttributeMappingId, AttributeMapping> mapping : model.getAMs().entrySet()) {
      if (!mapping.getKey().equals(targetMapping)) {
        sum1 += BaseUtils.intersect(
                new HashSet<String>(mapping.getValue().getImage()),
                new HashSet<String>(model.getAMs().get(targetMapping).getImage())).size();
      }
    }

    return sum1 / model.size();
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
    if (targetMapping == null || model == null) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
    }

    // TODO vektor JUnit test this!
    if (!model.getAMs().containsKey(targetMapping)) {
      throw new IllegalArgumentException("Target mapping not found in the model.");
    }

    final AttributeMapping mapping = model.getAMs().get(targetMapping);

    final Set<String> domain = new HashSet<String>(mapping.getImage());

    return domain.size() == mapping.getImage().size();
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
  public static boolean isIDset(final List<AttributeMappingId> mappings, final AMModel model) {
    if (BaseUtils.isEmpty(mappings) || model == null) {
      throw new IllegalArgumentException("Expecting non-null, non empty parameters");
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
}
