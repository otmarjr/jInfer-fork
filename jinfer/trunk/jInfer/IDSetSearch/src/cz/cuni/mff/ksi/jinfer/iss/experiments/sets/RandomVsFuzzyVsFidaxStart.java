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
import cz.cuni.mff.ksi.jinfer.iss.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Fuzzy;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax.Fidax;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment comparing Random and Fuzzy construction heuristics, only their
 * first steps.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class RandomVsFuzzyVsFidaxStart extends AbstractExperimentSet {

  private final StringBuilder sb = new StringBuilder();

  public RandomVsFuzzyVsFidaxStart() {
    for (final TestData data : OfficialTestData.values()) {
      sb.append("r-")
        .append(data.getFile().getName())
        .append('\t')
        .append("f-")
        .append(data.getFile().getName())
        .append('\t')
        .append("F-")
        .append(data.getFile().getName())
        .append('\t');
    }
  }

  @Override
  public String getName() {
    return "Random vs Fuzzy vs Fidax first step only";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < Constants.ITERATIONS; i++) {
      for (final TestData data : OfficialTestData.values()) {
        ret.add(new ExperimentParameters(data.getFile(), 10, 1, 1,
                data.getKnownOptimum(), new Random(), improvement, new Weight(), TimeIterations.NULL));
        ret.add(new ExperimentParameters(data.getFile(), 10, 1, 1,
                data.getKnownOptimum(), new Fuzzy(), improvement, new Weight(), TimeIterations.NULL));
        ret.add(new ExperimentParameters(data.getFile(), 10, 1, 1,
                data.getKnownOptimum(), new Fidax(), improvement, new Weight(), TimeIterations.NULL));
      }
    }

    return ret;
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    sb.append((iteration % (3 * OfficialTestData.values().length)) == 0 ? '\n': '\t')
      .append(e.getHighestQuality().getScalar());
  }

  @Override
  protected void notifyFinishedAll() {
    final File finalCsv = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result.txt");
    FileUtils.writeString(sb.toString(), finalCsv);
  }

}
