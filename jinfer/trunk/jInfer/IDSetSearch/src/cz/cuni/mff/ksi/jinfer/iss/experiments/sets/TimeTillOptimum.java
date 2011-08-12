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
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.SizeTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment measuring how long it takes to reach the optimum, based on the
 * input size. This can also be used to compare different versions of GLPK.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class TimeTillOptimum extends AbstractExperimentSet {

  private final File finalCsv = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result.txt");

  @Override
  public String getName() {
    return "Time till optimum";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < Constants.ITERATIONS; i++) {
      for (final TestData data : SizeTestData.values()) {
        ret.add(new ExperimentParameters(data.getFile(),
                1, 1, 1, data.getKnownOptimum(),
                new Glpk(), improvement, new Weight(), TimeIterations.NULL));
      }
    }

    return ret;
  }

  @Override
  protected void notifyStart() {
    final StringBuilder sb = new StringBuilder();
    for (final TestData data : SizeTestData.values()) {
      sb.append(data.getFile().getName())
        .append('\t');
    }
    FileUtils.writeString(sb.toString(), finalCsv);
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    final int numColumns = SizeTestData.values().length;
    final StringBuilder sb = new StringBuilder();
    sb.append((iteration % numColumns) == 0 ? '\n': '\t')
      .append(e.getConstructionResult().getTime());
    FileUtils.appendString(sb.toString(), finalCsv);
  }
}
