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

import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.RandomRemove;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.RemoveWorst;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Simply runs the same experiment ten times.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class TenIterations extends AbstractExperimentSet {

  @Override
  public String getName() {
    return "Ten Iterations";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(
            new RandomRemove(0.2),
            new Mutation(0.1, 1),
            new RandomRemove(0.2),
            new Crossover(0.2, 1),
            new RemoveWorst());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < 10; i++) {
      ret.add(new ExperimentParameters(OfficialTestData.GRAPH_100_200.getFile(),
              10, 1, 1, OfficialTestData.GRAPH_100_200.getKnownOptimum(),
              new Random(), improvement, new Weight(), new TimeIterations(10, 10000)));
    }

    return ret;
  }

}
