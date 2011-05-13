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
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.DeletableList;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Implementation of the heuristic algorithm for finding ID set as described
 * in the "Finding ID Attributes in XML Documents" article.
 *
 * @author vektor
 */
public final class Algorithm {

  private static final double ALPHA = 1.0d;
  private static final double BETA = 1.0d;

  private static final Logger LOG = Logger.getLogger(Algorithm.class);

  private Algorithm() {

  }

  /**
   * Finds the "best" ID set in the specified attribute mapping model. For
   * detailed information on the algorithm itself, please refer to the article.
   *
   * @param model Model to work on.
   * @return List of attribute mappings constituting the ID set found.
   */
  public static List<AttributeMappingId> findIDSet(final AMModel model) {

    // 1.  M := all AMs

    final List<AttributeMappingId> M = new ArrayList<AttributeMappingId>(model.getAMs().keySet());

    LOG.info("M: " + M.toString());

    // 1.  C := all candidate AMs sorted by decreasing size

    final List<AttributeMappingId> C = new ArrayList<AttributeMappingId>();
    for (final AttributeMappingId mapping : M) {
      if (MappingUtils.isCandidateMapping(mapping, model)) {
        C.add(mapping);
      }
    }

    Collections.sort(C, new Comparator<AttributeMappingId>() {

      @Override
      public int compare(final AttributeMappingId o1, final AttributeMappingId o2) {
        final Integer size1 = Integer.valueOf(model.getAMs().get(o1).size());
        final Integer size2 = Integer.valueOf(model.getAMs().get(o2).size());
        return -size1.compareTo(size2);
      }

    });

    LOG.info("C: " + C.toString());

    // 2.  compute weight for each m in C

    final Map<AttributeMappingId, Double> weights = new HashMap<AttributeMappingId, Double>();
    final Set<String> types = new HashSet<String>();

    for (final AttributeMappingId mapping : C) {
      types.add(mapping.getElement());
      weights.put(mapping, weight(mapping, model));
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

    LOG.info("C1: " + C1.toString());

    // 7.  for m in C do

    final DeletableList<AttributeMappingId> C2 = new DeletableList<AttributeMappingId>(C1);

    while (C2.hasNext()) {

      final AttributeMappingId m = C2.next();

    // 8.    S := all mappings in C whose images intersect that of m

      final List<AttributeMappingId> conflicts = new ArrayList<AttributeMappingId>();
      double conflicsWeight = 0;
      for (final AttributeMappingId c : C2.getLive()) {
        if (!c.equals(m) && imagesIntersect(m, c, model)) {
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

    LOG.info("C2: " + C2.getLive().toString());

    // 11. return C
    return C2.getLive();
  }

  private static boolean imagesIntersect(final AttributeMappingId am1,
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
    final List<String> image1 = model.getAMs().get(am1).getImage();
    final List<String> image2 = model.getAMs().get(am2).getImage();
    return !BaseUtils.intersect(
            new HashSet<String>(image1),
            new HashSet<String>(image2)).isEmpty();
  }

  private static Double weight(final AttributeMappingId mapping, final AMModel model) {
    return Double.valueOf(
            ALPHA * MappingUtils.support(mapping, model)
            + BETA * MappingUtils.coverage(mapping, model));
  }

  private static AttributeMappingId findMaxWeight(final String type,
          final Map<AttributeMappingId, Double> weights) {
    double maxWeight = 0;
    AttributeMappingId max = null;
    for (final Map.Entry<AttributeMappingId, Double> e : weights.entrySet()) {
      if (e.getKey().getElement().equals(type)
            && e.getValue().doubleValue() > maxWeight) {
        maxWeight = e.getValue().doubleValue();
        max = e.getKey();
      }
    }
    if (max == null) {
      throw new IllegalStateException("No mappings of type " + type + " found");
    }
    return max;
  }
}
