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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction;

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import cz.cuni.mff.ksi.jinfer.iss.utils.WeightedRandomGenerator;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This construction heuristics builds up the solution gradually by adding AMs.
 * AMs to be added are picked at weighted random, based on their weight.
 *
 * @author vektor
 */
public class Fuzzy implements ConstructionHeuristic {

  @Override
  public void start(final Experiment experiment,
        final HeuristicCallback callback) throws InterruptedException {

    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(experiment.getModel());
    final List<Double> weights = new ArrayList<Double>(candidates.size());
    for (final AttributeMappingId mapping : candidates) {
      weights.add(Double.valueOf(experiment.getModel().weight(mapping, experiment.getAlpha(), experiment.getBeta())));
    }

    final List<IdSet> ret = new ArrayList<IdSet>();
    for (int i = 0; i < experiment.getPoolSize(); i++) {
      ret.add(new IdSet(
              createFuzzy(experiment,
                new ArrayList<AttributeMappingId>(candidates),
                new ArrayList<Double>(weights))));
    }
    callback.finished(ret);
  }

  private List<AttributeMappingId> createFuzzy(final Experiment experiment,
          final List<AttributeMappingId> candidates, final List<Double> weights) {
    final List<AttributeMappingId> ret = new ArrayList<AttributeMappingId>();

    while (!BaseUtils.isEmpty(candidates)) {
      final double[] weights_ = new double[weights.size()];
      for (int i = 0; i < weights.size(); i++) {
        weights_[i] = weights.get(i).doubleValue();
      }

      final int chosen = new WeightedRandomGenerator(weights_).next();
      final AttributeMappingId candidate = candidates.get(chosen);
      if (MappingUtils.isIDset(Utils.append(ret, candidate), experiment.getModel())) {
        ret.add(candidate);
        candidates.remove(chosen);
        weights.remove(chosen);
      }

      for (int i = candidates.size() - 1; i >= 0; i--) {
        if (!MappingUtils.isIDset(Utils.append(ret, candidates.get(i)), experiment.getModel())) {
          candidates.remove(i);
          weights.remove(i);
        }
      }
    }
    return ret;
  }

  @Override
  public String getName() {
    return "Fuzzy";
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
