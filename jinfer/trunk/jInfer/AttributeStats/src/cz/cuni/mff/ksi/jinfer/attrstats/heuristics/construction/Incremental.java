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
package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction;

import cz.cuni.mff.ksi.jinfer.attrstats.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.ImageSizeComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Incremental implements ConstructionHeuristic {

  @Override
  public void start(final AMModel model, final int poolSize, final HeuristicCallback callback)
          throws InterruptedException {

    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(model);

    Collections.sort(candidates, new ImageSizeComparator(model));

    final List<AttributeMappingId> ret = new ArrayList<AttributeMappingId>();

    for (final AttributeMappingId id : candidates) {
      final List<AttributeMappingId> potential = new ArrayList<AttributeMappingId>(ret);
      potential.add(id);
      if (MappingUtils.isIDset(potential, model)) {
        ret.add(id);
      }
    }

    final IdSet solution = new IdSet(ret);
    callback.finished(Arrays.asList(solution));
  }

  @Override
  public String getName() {
    return "Incremental";
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

}
