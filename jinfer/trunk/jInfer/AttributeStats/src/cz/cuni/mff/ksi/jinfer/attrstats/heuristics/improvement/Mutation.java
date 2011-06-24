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
package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.ExperimentalUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.Quality;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.glpk.GlpkOutputParser;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.glpk.GlpkRunner;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Mutation implements ImprovementHeuristic {

  private final double ratio;
  private final int timeLimit;

  public Mutation(final double ratio, final int timeLimit) {
    super();
    if (ratio < 0 || ratio >= 1) {
      throw new IllegalArgumentException("Invalid ratio: " + ratio);
    }
    this.ratio = ratio;
    this.timeLimit = timeLimit;
  }

  @Override
  public void start(final Experiment experiment, final List<IdSet> feasiblePool,
        final HeuristicCallback callback) throws InterruptedException {
    final AMModel model = experiment.getModel();
    // Fix a few to their current values and run again
    // Parameter k - fraction to be fixed

    // TODO vektor Get quality from somewhere else!..
    final Pair<IdSet, Quality> incumbent = ExperimentalUtils.getBest(experiment, feasiblePool);

    final List<AttributeMappingId> mappings = incumbent.getFirst().getMappings();
    final List<AttributeMappingId> fixed = BaseUtils.rndSubset(mappings, ratio);

    final IdSet improved = GlpkOutputParser.getIDSet(GlpkRunner.run(model, new ArrayList<AttributeMappingId>(fixed), experiment.getAlpha(), experiment.getBeta(), timeLimit), model);
    final List<IdSet> ret = new ArrayList<IdSet>(feasiblePool);
    ret.add(improved);
    callback.finished(ret);
  }

  @Override
  public String getName() {
    return "Mutation";
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getModuleDescription() {
    return getName() + ", ratio = " + ratio + ", limit = " + timeLimit + " s";
  }

}
