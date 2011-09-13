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
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.RemoveWorst;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.io.File;
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
public class ChainedIHs2 extends AbstractExperimentSet {

  private File file;

  @Override
  public String getName() {
    return "Chained IHs 2";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(
            new Crossover(0.1, 1),
            new RemoveWorst(),
            new Mutation(0.1, 1));

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (final TestData data : OfficialTestData.values()) {
      for (int i = 0; i < Utils.getIterations(); i++) {
        ret.add(new ExperimentParameters(data.getFile(),
                POOL_SIZE, 1, 1, data.getKnownOptimum(),
                new Random(), improvement, new Weight(),
                new TimeIterations(10000, 10000)));
      }
    }

    return ret;
  }

    @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    file = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result-" +
           OfficialTestData.values()[iteration / Utils.getIterations()].getFile().getName() + ".txt");

    FileUtils.appendString(e.getTerminationReason().getSecond() + e.getCsv() + "\n", file);
  }

}
