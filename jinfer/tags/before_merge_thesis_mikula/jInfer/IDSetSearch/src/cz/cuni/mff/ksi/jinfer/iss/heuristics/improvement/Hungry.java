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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement;

import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.WeightComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tries to improve every solution in the pool by including as many good
 * mappings as possible.
 *
 * @author vektor
 */
public class Hungry implements ImprovementHeuristic {

  @Override
  public void start(final Experiment experiment, final List<IdSet> feasiblePool,
        final HeuristicCallback callback) throws InterruptedException {
    final List<IdSet> improved = new ArrayList<IdSet>(feasiblePool.size());
    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(experiment.getModel());
    Collections.sort(candidates, new WeightComparator(experiment.getModel(), experiment.getAlpha(), experiment.getBeta()));
    for (final IdSet solution : feasiblePool) {
      improved.add(improve(experiment.getModel(), solution, candidates));
    }

    callback.finished(improved);
  }

  /**
   * Try improving this ID set by adding as many heavy AMs as possible.
   */
  private static IdSet improve(final AMModel model, final IdSet idSet,
          final List<AttributeMappingId> candidates) {
    final List<AttributeMappingId> ret = new ArrayList<AttributeMappingId>(idSet.getMappings());

    for (final AttributeMappingId id : candidates) {
      if (!ret.contains(id)) {
        final List<AttributeMappingId> potential = new ArrayList<AttributeMappingId>(ret);
        potential.add(id);
        if (MappingUtils.isIDset(potential, model)) {
          ret.add(id);
        }
      }
    }

    return new IdSet(ret);
  }

  @Override
  public String getName() {
    return "Hungry";
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
