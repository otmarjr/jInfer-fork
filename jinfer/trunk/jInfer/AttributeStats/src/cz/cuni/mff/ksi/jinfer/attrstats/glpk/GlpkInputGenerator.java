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
package cz.cuni.mff.ksi.jinfer.attrstats.glpk;

import cz.cuni.mff.ksi.jinfer.attrstats.logic.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public final class GlpkInputGenerator {

  private GlpkInputGenerator() {

  }

  private static final String CONSTRAINT = "s.t. c{index}: x['{mapping1}'] + x['{mapping2}'] <= 1;";

  /**
   * TODO vektor Comment!
   *
   * @param model
   * @return
   */
  public static String generateGlpkInput(final AMModel model) {
    final List<AttributeMappingId> candidates = new ArrayList<AttributeMappingId>();
    for (final AttributeMappingId mapping : model.getAMs().keySet()) {
      if (MappingUtils.isCandidateMapping(mapping, model)) {
        candidates.add(mapping);
      }
    }

    final StringBuilder mappings = new StringBuilder();
    final StringBuilder weights = new StringBuilder();

    for (final AttributeMappingId mapping : candidates) {
      final String name = GlpkUtils.getName(mapping);
      mappings.append(name).append('\n');
      weights.append(name).append(' ').append(MappingUtils.weight(mapping, model)).append('\n');
    }

    try {
      final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("cz/cuni/mff/ksi/jinfer/attrstats/glpk/GlpkInputTemplate.txt");
      final BufferedReader br = new BufferedReader(new InputStreamReader(is));
      final StringBuilder ret = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        ret.append(line).append('\n');
      }

      return ret.toString()
              .replace("{constraints}", getConstraints(candidates, model))
              .replace("{mappings}", mappings)
              .replace("{weights}", weights);
    } catch (final IOException ex) {
      return null;
    }
  }

  private static CharSequence getConstraints(
          final List<AttributeMappingId> candidates, final AMModel model) {
    final StringBuilder ret = new StringBuilder();

    int index = 0;
    for (final AttributeMappingId mapping1 : candidates) {
      for (final AttributeMappingId mapping2 : candidates) {
        if (mapping1.equals(mapping2)) {
          continue;
        }

        if (mappingsCollide(mapping1, mapping2, model)) {
          index++;
          final String constraint = CONSTRAINT
                  .replace("{index}", String.valueOf(index))
                  .replace("{mapping1}", GlpkUtils.getName(mapping1))
                  .replace("{mapping2}", GlpkUtils.getName(mapping2));
          ret.append(constraint).append('\n');
        }
      }
    }

    return ret;
  }

  private static boolean mappingsCollide(final AttributeMappingId mapping1,
          final AttributeMappingId mapping2, final AMModel model) {
    if (mapping1.getElement().equals(mapping2.getElement())) {
      return true;
    }
    return MappingUtils.imagesIntersect(mapping1, mapping2, model);
  }

}
