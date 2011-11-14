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

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkOutputParser;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkRunner;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * A slightly more complex improvement heuristic. From the pool of feasible
 * solutions picks a fraction at random, finds all common AMs, fixes them and
 * runs the GLPK optimization on the resulting sub-problem. Its solution is
 * then added to the pool and returned.
 *
 * @author vektor
 */
public class Crossover implements ImprovementHeuristic {

  private final double ratio;
  private final int timeLimit;

  /**
   * Constructs an instance of this heuristic.
   *
   * @param ratio Fraction of the solution pool to be scanned for common AMs.
   * @param timeLimit Time limit in seconds of the GLPK run.
   */
  public Crossover(final double ratio, final int timeLimit) {
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
    final List<IdSet> selectedSets = BaseUtils.rndSubset(feasiblePool, ratio);

    final List<AttributeMappingId> all = new ArrayList<AttributeMappingId>(model.getAMs().keySet());
    final List<AttributeMappingId> common = new ArrayList<AttributeMappingId>(all);
    for (final AttributeMappingId mapping : all) {
      boolean inAll = true;
      for (final IdSet selectedSet : selectedSets) {
        if (!selectedSet.getMappings().contains(mapping)) {
          inAll = false;
          break;
        }
      }
      if (!inAll) {
        common.remove(mapping);
      }
    }

    final IdSet improved = GlpkOutputParser.getIDSet(
            GlpkRunner.run(model, common, experiment.getAlpha(), experiment.getBeta(), timeLimit),
            model,
            !BaseUtils.isEmpty(common));
    final List<IdSet> ret = new ArrayList<IdSet>(feasiblePool);
    ret.add(improved);
    callback.finished(ret);
  }

  @Override
  public String getName() {
    return "Crossover";
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
