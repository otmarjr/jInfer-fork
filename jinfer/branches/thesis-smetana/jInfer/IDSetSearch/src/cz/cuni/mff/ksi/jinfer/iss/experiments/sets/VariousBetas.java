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
package cz.cuni.mff.ksi.jinfer.iss.experiments.sets;

import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment comparing effects of various values of parameters alpha and beta.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class VariousBetas extends AbstractExperimentSet {

  private static final double[] alphas = new double[]{0.1, 0.25, 0.5, 0.75, 1.0};
  private static final double[] betas = new double[]{0.1, 0.25, 0.5, 0.75, 1.0};
  private static final List<String> EXPERIMENTNAMES = new ArrayList<String>();

  @Override
  public String getName() {
    return "Various Betas";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(alphas.length * betas.length);

    EXPERIMENTNAMES.clear();
    for (final double alpha : alphas) {
      for (final double beta : betas) {
        for (final TestData data : OfficialTestData.values()) {
          ret.add(new ExperimentParameters(data.getFile(),
                  1, alpha, beta, data.getKnownOptimum(),
                  new Glpk(), improvement, new Weight(), TimeIterations.NULL));
          EXPERIMENTNAMES.add("set=" + data.getFile().getName() + ", alpha=" + alpha + ", beta=" + beta);
        }
      }
    }

    return ret;
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    FileUtils.appendString("\n" + EXPERIMENTNAMES.get(iteration) + e.getWinner(), resultCsv);
  }
}
