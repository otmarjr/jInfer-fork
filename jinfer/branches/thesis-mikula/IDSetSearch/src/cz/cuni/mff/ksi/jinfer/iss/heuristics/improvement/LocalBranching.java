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
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Quality;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkInputGenerator;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkOutputParser;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkRunner;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This heuristic works by creating a constrained sub-problem and then solving
 * it. The constraint is as follows: the maximum Hamming distance between the
 * old and new solution is limited to a constant <code>k</code>.
 *
 * Some implementation details:
 * <ul>
 *  <li>When working with AMs and ID sets, the Hamming distance is defined
 *    as follows: sum over all AMs of (1 if solutions differ at this AM,
 *    0 otherwise).</li>
 *  <li><code>k</code> is fixed to be a ratio of the total AM count.</li>
 * </ul>
 *
 * Local branching always works on the incumbent solution, and adds the new
 * solution to the pool.
 *
 * @author vektor
 */
public class LocalBranching implements ImprovementHeuristic {

  private static final Logger LOG = Logger.getLogger(LocalBranching.class);

  private final double ratio;
  private final int timeLimit;

  /**
   * Constructs an instance of this heuristic.
   *
   * @param ratio The total number of AMs is multiplied by this fraction, result
   *   is the parameter <code>k</code> - maximum Hamming distance between the
   *   old and the new solution.
   * @param timeLimit Time limit in seconds of the GLPK run.
   */
  public LocalBranching(final double ratio, final int timeLimit) {
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
    final Pair<IdSet, Quality> incumbent = Utils.getBest(experiment, feasiblePool);

    final long k = Math.round(model.getAMs().size() * ratio);

    try {
      final String glpkInput = GlpkInputGenerator.generateGlpkInput(
              model, experiment.getAlpha(), experiment.getBeta(), incumbent.getFirst().getMappings(), k);
      final IdSet improved = GlpkOutputParser.getIDSet(GlpkRunner.run(glpkInput, timeLimit), model, true);

      final List<IdSet> ret = new ArrayList<IdSet>(feasiblePool);
      ret.add(improved);
      callback.finished(ret);
    }
    catch (final IOException e) {
      LOG.error("Problem creating GLPK input.", e);
      callback.finished(Collections.<IdSet>emptyList());
    }
  }

  @Override
  public String getName() {
    return "LocalBranching";
  }

  @Override
  public String getDisplayName() {
    return "Local Branching";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

}
