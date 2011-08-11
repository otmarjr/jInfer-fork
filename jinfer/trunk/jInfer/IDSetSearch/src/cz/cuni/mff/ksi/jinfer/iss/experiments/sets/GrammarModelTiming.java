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
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.SizeTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Null;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkUtils;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Runs all the official test data to see how long it takes them to extract
 * grammar and create model. No actual heuristic is employed thereafter.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class GrammarModelTiming extends AbstractExperimentSet {

  private final StringBuilder sb = new StringBuilder();

  public GrammarModelTiming() {
    for (final TestData data : OfficialTestData.values()) {
      sb.append("gt-")
        .append(data.getFile().getName())
        .append('\t')
        .append("mt-")
        .append(data.getFile().getName())
        .append('\t');
    }
    for (final TestData data : SizeTestData.values()) {
      sb.append("gt-")
        .append(data.getFile().getName())
        .append('\t')
        .append("mt-")
        .append(data.getFile().getName())
        .append('\t');
    }
  }

  @Override
  public String getName() {
    return "Grammar and Model Timing";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < Constants.ITERATIONS; i++) {
      for (final TestData data : OfficialTestData.values()) {
        ret.add(new ExperimentParameters(data.getFile(), 1, 1, 1, data.getKnownOptimum(),
                new Null(), improvement, new Weight(), TimeIterations.NULL));
      }

      for (final TestData data : SizeTestData.values()) {
        ret.add(new ExperimentParameters(data.getFile(), 1, 1, 1, data.getKnownOptimum(),
                new Null(), improvement, new Weight(), TimeIterations.NULL));
      }
    }

    return ret;
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    final int numColumns = OfficialTestData.values().length + SizeTestData.values().length;
    sb.append((iteration % numColumns) == 0 ? '\n': '\t')
      .append(e.getGrammarTime())
      .append('\t')
      .append(e.getModelTime());
  }

  @Override
  protected void notifyFinishedAll() {
    // TODO vektor Move this assertion into writeInput
    final File rootDir = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName());
    if (!rootDir.exists()) {
      rootDir.mkdirs();
    }
    final File finalCsv = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result.txt");
    GlpkUtils.writeInput(finalCsv, sb.toString());
  }
}
