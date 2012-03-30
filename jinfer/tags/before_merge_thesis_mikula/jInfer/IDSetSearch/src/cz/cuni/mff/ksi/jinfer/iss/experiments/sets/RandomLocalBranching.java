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

import cz.cuni.mff.ksi.jinfer.iss.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.LocalBranching;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class RandomLocalBranching extends AbstractExperimentSet {

  @Override
  public String getName() {
    return "Random and Local Branching";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(
            new LocalBranching(0.1, 0));

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < 3; i++) {
      ret.add(new ExperimentParameters(OfficialTestData.GRAPH_100_200.getFile(),
              POOL_SIZE, 1, 1, OfficialTestData.GRAPH_100_200.getKnownOptimum(),
              new Random(), improvement, new Weight(), new TimeIterations(1, 10000)));
    }

    return ret;
  }

}
