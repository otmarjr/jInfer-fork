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
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Fuzzy;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax.Fidax;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment comparing Random, Fuzzy and FIDAX construction heuristics, only
 * their first steps.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class RandomVsFuzzyVsFidaxStart extends AbstractExperimentSet {

  private final File finalCsvQuality = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result-quality.txt");
  private final File finalCsvTime = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result-time.txt");

  @Override
  public String getName() {
    return "Random vs Fuzzy vs Fidax first step only";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < Utils.getIterations(); i++) {
      for (final TestData data : OfficialTestData.values()) {
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(), new Random(), improvement, new Weight(), TimeIterations.NULL));
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(), new Fuzzy(), improvement, new Weight(), TimeIterations.NULL));
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(), new Fidax(), improvement, new Weight(), TimeIterations.NULL));
      }
    }

    return ret;
  }

  private void writeHeader() {
    final StringBuilder sb = new StringBuilder();
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
    FileUtils.writeString(sb.toString(), finalCsvQuality);
    FileUtils.writeString(sb.toString(), finalCsvTime);
  }

  @Override
  protected void notifyStart() {
    writeHeader();
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    final int numColumns = OfficialTestData.values().length * 3;
    final StringBuilder sbQuality = new StringBuilder();
    sbQuality.append((iteration % numColumns) == 0 ? '\n': '\t')
      .append(e.getHighestQuality().getScalar());
    FileUtils.appendString(sbQuality.toString(), finalCsvQuality);

    final StringBuilder sbTime = new StringBuilder();
    sbTime.append((iteration % numColumns) == 0 ? '\n': '\t')
      .append(e.getConstructionResult().getTime());
    FileUtils.appendString(sbTime.toString(), finalCsvTime);
  }
}
