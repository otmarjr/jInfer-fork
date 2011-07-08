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
package cz.cuni.mff.ksi.jinfer.attrstats.utils;

import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
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

    return new Pair<Integer, Integer>(Integer.valueOf(candidates.size()), Integer.valueOf(edgeCount));
  }


}
