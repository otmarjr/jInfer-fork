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

import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public final class GlpkOutputParser {

  private GlpkOutputParser() {
  }

  /**
   * TODO vektor Comment!
   *
   * @param output
   * @param model
   * @return
   */
  public static List<AttributeMappingId> getIDSet(final String output, final AMModel model) {
    final Set<AttributeMappingId> ret = new HashSet<AttributeMappingId>();

    final Scanner s = new Scanner(output);

    s.findWithinHorizon("------ ------------    ------------- ------------- -------------", 0);

    while (s.findWithinHorizon("\\[", 0) != null) {
      s.findWithinHorizon("([^\\]]*)\\]", 0);
      final String id = s.match().group(1);
      s.findWithinHorizon("(\\d+)", 0);
      final boolean used = "1".equals(s.match().group(1));

      if (used) {
        for (final AttributeMappingId mapping : model.getAMs().keySet()) {
          final String mappingName = GlpkUtils.getName(mapping);
          if (mappingName.equals(id)) {
            ret.add(mapping);
          }
        }
      }
    }

    return new ArrayList<AttributeMappingId>(ret);
  }

}
