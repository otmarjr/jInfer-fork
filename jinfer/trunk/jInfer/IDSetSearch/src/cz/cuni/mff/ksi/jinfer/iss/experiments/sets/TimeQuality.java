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
import cz.cuni.mff.ksi.jinfer.iss.experiments.SizeTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Experiment measuring quality depending on how long we let the GLPK
 * construction run.
 *
 * @author vektor
 */
public class TimeQuality extends AbstractExperimentSet {

  @Override
  public String getName() {
    return "Time versus quality";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int time = 1; time < 50; time += 2) {
      for (int j = 0; j < 10; j++) {
        ret.add(new ExperimentParameters(SizeTestData.GRAPH_100_500.getFile(),
                1, 1, 1, SizeTestData.GRAPH_100_500.getKnownOptimum(),
                new Glpk(time), improvement, new Weight(), new TimeIterations(1)));
      }
    }

    return ret;
  }

}
