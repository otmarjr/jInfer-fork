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

import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkUtils;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import java.util.List;

/**
 * Utility class to get the "graph representation" info from an AM model.
 *
 * @author vektor
 */
public final class GraphUtils {

  private GraphUtils() {
  }

  /**
   * Returns the graph representation info for the provided model.
   *
   * @param model AM model to get info from.
   * @return A pair containing the number of vertices in the first item
   *   and the number of edges in the second.
   */
  public static Pair<Integer, Integer> getGraphRepresentation(final AMModel model) {
    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(model);

    int edgeCount = 0;

    for (final AttributeMappingId mapping1 : candidates) {
      for (final AttributeMappingId mapping2 : candidates) {

        if (MappingUtils.mappingsCollide(mapping1, mapping2, model)
                && mapping1.compareTo(mapping2) < 0) {
          edgeCount++;
        }
      }
    }

    return new ImmutablePair<Integer, Integer>(Integer.valueOf(candidates.size()), Integer.valueOf(edgeCount));
  }

  /**
   * Returns a graph representation of the specified model in GraphViz language.
   *
   * The GraphViz input generated will be as follows:
   * <ul>
   *   <li>Every candidate mapping will be represent as a vertex.</li>
   *   <li>Every pair of candidate mappings that collide
   *     ({@see MappingUtils#mappingsCollide()}) will be represented as an
   *     edge connecting these two vertices.</li>
   * </ul>
   *
   * @param model AM model to create GraphViz input from.
   *
   * @return String representation of the GraphViz input.
   */
  public static String getGraphVizInput(final AMModel model) {
    final StringBuilder ret = new StringBuilder("graph {\n\n");

    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(model);

    for (final AttributeMappingId mapping : candidates) {
      ret.append("  ")
          .append(GlpkUtils.getName(mapping, '_'))
          .append(";\n");
    }

    ret.append('\n');

    for (final AttributeMappingId mapping1 : candidates) {
      for (final AttributeMappingId mapping2 : candidates) {

        if (MappingUtils.mappingsCollide(mapping1, mapping2, model)
                && mapping1.compareTo(mapping2) < 0) {
          ret.append("  ")
              .append(GlpkUtils.getName(mapping1, '_'))
              .append(" -- ")
              .append(GlpkUtils.getName(mapping2, '_'))
              .append(";\n");
        }
      }
    }

    ret.append("\n}\n");
    return ret.toString();
  }

}
