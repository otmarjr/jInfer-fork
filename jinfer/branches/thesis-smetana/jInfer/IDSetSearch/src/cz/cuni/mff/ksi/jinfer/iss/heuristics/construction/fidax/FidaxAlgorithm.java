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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax;

import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.DeletableList;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.ImageSizeComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the heuristic algorithm for finding ID set as described
 * in the "Finding ID Attributes in XML Documents" article.
 *
 * @author vektor
 */
public final class FidaxAlgorithm {

  private FidaxAlgorithm() {
  }

  /**
   * Finds the "best" ID set in the specified attribute mapping model. For
   * detailed information on the algorithm itself, please refer to the article.
   *
   * @param model Model to work on.
   * @param alpha Weight of the attribute mapping <cite>support</cite> in its total weight.
   * @param beta Weight of the attribute mapping <cite>coverage</cite> in its total weight.
   *
   * @return List of attribute mappings constituting the ID set found.
   */
  public static IdSet findIDSet(final AMModel model,
          final double alpha, final double beta) {

    // 1.  M := all AMs - hidden here

    // 1.  C := all candidate AMs sorted by decreasing size

    final List<AttributeMappingId> C = MappingUtils.getCandidates(model);

    Collections.sort(C, new ImageSizeComparator(model));

    // 2.  compute weight for each m in C

    final Map<AttributeMappingId, Double> weights = new HashMap<AttributeMappingId, Double>();
    final Set<String> types = new HashSet<String>();

    for (final AttributeMappingId mapping : C) {
      types.add(mapping.getElement());
      weights.put(mapping, Double.valueOf(model.weight(mapping, alpha, beta)));
    }

    // 3.  for each type (element name) t do
    final List<AttributeMappingId> C1 = new ArrayList<AttributeMappingId>();

    for (final String type : types) {

    // 4.    m := highest-weight mapping of type t in C

      final AttributeMappingId m = findMaxWeight(type, weights);

    // 5.    remove from C all mappings of type t except m

      C1.add(m);

    // 6.  end for

    }

    // 7.  for m in C do

    final DeletableList<AttributeMappingId> C2 = new DeletableList<AttributeMappingId>(C1);

    while (C2.hasNext()) {

      final AttributeMappingId m = C2.next();

    // 8.    S := all mappings in C whose images intersect that of m

      final List<AttributeMappingId> conflicts = new ArrayList<AttributeMappingId>();
      double conflicsWeight = 0;
      for (final AttributeMappingId c : C2.getLive()) {
        if (!c.equals(m) && MappingUtils.imagesIntersect(m, c, model)) {
          conflicsWeight += weights.get(c);
          conflicts.add(c);
        }
      }

    // 9.    if weight(m) > SUM_p in S [ weight(p) ], remove S, else remove m from C

      if (weights.get(m) > conflicsWeight) {
        C2.removeAll(conflicts);
      }
      else {
        C2.remove(m);
      }

    // 10. end for

    }

    // 11. return C
    return new IdSet(C2.getLive());
  }

  private static AttributeMappingId findMaxWeight(final String type,
          final Map<AttributeMappingId, Double> weights) {
    double maxWeight = 0;
    AttributeMappingId max = null;
    for (final Map.Entry<AttributeMappingId, Double> e : weights.entrySet()) {
      final double weight = e.getValue().doubleValue();
      if (e.getKey().isType(type)
            && weight > maxWeight) {
        maxWeight = weight;
        max = e.getKey();
      }
    }
    if (max == null) {
      throw new IllegalStateException("No mappings of type " + type + " found");
    }
    return max;
  }

}
