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

import cz.cuni.mff.ksi.jinfer.attrstats.logic.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.Triplet;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public final class Algorithm {

  private Algorithm() {

  }

  public static List<Pair<String, String>> findIDSet(final List<Triplet> allMappings) {

    // 1.  M := all AMs

    final List<Pair<String, String>> M = new ArrayList<Pair<String, String>>();
    for (final Triplet mapping : allMappings) {
      final Pair<String, String> p = new Pair<String, String>(mapping.getElement(), mapping.getAttribute());
      if (!M.contains(p)) {
        M.add(p);
      }
    }

    // 1.  C := all candidate AMs sorted by decreasing size

    final List<Pair<String, String>> C = new ArrayList<Pair<String, String>>();
    for (final Pair<String, String> mapping : M) {
      if (MappingUtils.isCandidateMapping(mapping, allMappings)) {
        C.add(mapping);
      }
    }

    // TODO sort by size

    // TODO how is "size" defined?

    // 2.  compute weight for each m in C

    // 3.  for each type (element name) t do

    // 4.    m := highest-weight mapping of type t in C

    // 5.    remove from C all mappings of type t except m

    // 6.  end for

    // 7.  for m in C do

    // 8.    S := all mappings in C whose images intersect that of m

    // 9.    if weight(m) > SUM_p in S [ weight(p) ], remove S, else remove m from C

    // 10. end for

    // 11. return C
    return null;
  }

}
