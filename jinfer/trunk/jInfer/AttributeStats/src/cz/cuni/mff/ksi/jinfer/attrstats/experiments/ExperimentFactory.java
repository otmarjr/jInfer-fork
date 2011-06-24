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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.RandomRemove;
import java.util.Arrays;

/**
 * @author vektor
 */
public final class ExperimentFactory {

  private ExperimentFactory() {
  }

  public static Experiment createExperiment(final String fileName) {
    return new Experiment(
            new InputFile(fileName),
            3, 1, 1,
            new Glpk(1),
            //Arrays.<ImprovementHeuristic>asList(new Identity()),
            Arrays.<ImprovementHeuristic>asList(new RandomRemove(0.1), new Mutation(0.1, 1), new RandomRemove(0.4), new Crossover(0.3, 1)),
            new Weight(),
            new TimeIterations(100, 10000));
  }

}
