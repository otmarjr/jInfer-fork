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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk;

import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Utility class for parsing the GLPK optimization result into a set of attribute
 * mappings from the "optimal" ID set.
 *
 * @author vektor
 */
public final class GlpkOutputParser {

  private static final Logger LOG = Logger.getLogger(GlpkOutputParser.class);

  private GlpkOutputParser() {
  }

  /**
   * Parses the string representing the GLPK optimization result and returns
   * a list of attribute mappings belonging to the "optimal" ID set.
   *
   * @param output String representation of the GLPK result. Basically the file
   * that GLPK produces loaded into a string.
   * @param model Model to which all the AMs belong.
   *
   * @return List of AMs belonging to the "optimal" ID set.
   */
  public static IdSet getIDSet(final String output, final AMModel model) {
    return getIDSet(output, model, false);
  }

  /**
   * Parses the string representing the GLPK optimization result and returns
   * a list of attribute mappings belonging to the "optimal" ID set.
   *
   * @param output String representation of the GLPK result. Basically the file
   * that GLPK produces loaded into a string.
   * @param model Model to which all the AMs belong.
   * @param isFixed Flag whether there were any fixed variables. If
   * <code>true</code>, optimum possibly found by GLPK is not an optimal ID set.
   *
   * @return List of AMs belonging to the "optimal" ID set.
   */
  public static IdSet getIDSet(final String output, final AMModel model,
          final boolean isFixed) {
    final long start = Utils.time();

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

    final IdSet result = new IdSet(
            new ArrayList<AttributeMappingId>(ret),
            !isFixed && output.contains("INTEGER OPTIMAL SOLUTION FOUND"));

    LOG.debug("GLPK output parsing took " + Utils.delta(start) + " ms.");

    return result;
  }

}
