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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments.sets;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.RandomRemove;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.RemoveWorst;
import cz.cuni.mff.ksi.jinfer.attrstats.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Experiment comparing effects of various values of parameters alpha and beta.
 *
 * @author vektor
 */
public class VariousBetas extends AbstractExperimentSet {

  private static final double[] alphas = new double[] {0.1, 0.5, 1.0};
  private static final double[] betas = new double[] {0.1, 0.5, 1.0};

  @Override
  public String getName() {
    return "Various Betas";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(
            new RandomRemove(0.1),
            new Mutation(0.1, 1),
            new RandomRemove(0.4),
            new Crossover(0.3, 1),
            new RemoveWorst());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(alphas.length * betas.length);

    for (final double alpha : alphas) {
      for (final double beta : betas) {
        ret.add(new ExperimentParameters(Constants.GRAPH, 10, alpha, beta, new Random(), improvement, new Weight(), new TimeIterations(100, 10000)));
      }
    }

    return ret;
  }

}
