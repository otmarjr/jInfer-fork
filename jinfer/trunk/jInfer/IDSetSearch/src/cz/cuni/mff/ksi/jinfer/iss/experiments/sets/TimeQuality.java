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
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.SizeTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
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
 * Experiment measuring quality depending on how long we let the GLPK
 * construction run. This can also be used to compare different versions of GLPK.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class TimeQuality extends AbstractExperimentSet {

  private static final int BEGIN = 1;
  private static final int END = 50;
  private static final int STEP = 2;

  private final File finalCsv = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result.txt");

  @Override
  public String getName() {
    return "Time versus quality";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int j = 0; j < Constants.ITERATIONS; j++) {
      for (int time = BEGIN; time < END; time += STEP) {
          ret.add(new ExperimentParameters(SizeTestData.GRAPH_100_500.getFile(),
                  1, 1, 1, SizeTestData.GRAPH_100_500.getKnownOptimum(),
                  new Glpk(time), improvement, new Weight(), TimeIterations.NULL));
      }
    }

    return ret;
  }

  @Override
  protected void notifyStart() {
    final StringBuilder sb = new StringBuilder();
    for (int time = BEGIN; time < END; time += STEP) {
      sb.append("t-")
        .append(time)
        .append('\t');
    }
    FileUtils.writeString(sb.toString(), finalCsv);
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    final int numColumns = Math.round((END - BEGIN) / (float)STEP);
    final StringBuilder sb = new StringBuilder();
    sb.append((iteration % numColumns) == 0 ? '\n': '\t')
      .append(e.getConstructionResult().getQuality().getScalar());
    FileUtils.appendString(sb.toString(), finalCsv);
  }
}
