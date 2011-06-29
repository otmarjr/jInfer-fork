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

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.FileCharacteristics;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.InputFile;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.Fuzzy;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.RandomRemove;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.RemoveWorst;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Experiment comparing Random and Fuzzy construction heuristics.
 *
 * @author vektor
 */
public class RandomVsFuzzy extends AbstractExperimentSet {

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(
            new RandomRemove(0.2),
            new Mutation(0.1, 1),
            new RandomRemove(0.2),
            new Crossover(0.2, 1),
            new RemoveWorst());
    final InputFile file = new InputFile("C:\\Users\\vitasek\\Documents\\Soukrome\\test-xml\\graph.xml", FileCharacteristics.ARTIFICIAL);

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < 3; i++) {
      ret.add(new ExperimentParameters(file, 10, 1, 1, 0.2429268293, new Random(), improvement, new Weight(), new TimeIterations(100, 10000)));
    }

    for (int i = 0; i < 3; i++) {
      ret.add(new ExperimentParameters(file, 10, 1, 1, 0.2429268293, new Fuzzy(), improvement, new Weight(), new TimeIterations(100, 10000)));
    }

    return ret;
  }

}
