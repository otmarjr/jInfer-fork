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
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax.Fidax;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Hungry;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment determining whether FIDAX can be improved with Hungry.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class FidaxWithHungry extends AbstractExperimentSet {

  @Override
  public String getName() {
    return "FIDAX with Hungry";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> empty = Arrays.<ImprovementHeuristic>asList(new Identity());
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Hungry());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (final TestData data : OfficialTestData.values()) {
      ret.add(new ExperimentParameters(data.getFile(), 1, 1, 1,
              data.getKnownOptimum(), new Fidax(), empty, new Weight(), TimeIterations.NULL));
      ret.add(new ExperimentParameters(data.getFile(), 1, 1, 1,
              data.getKnownOptimum(), new Fidax(), improvement, new Weight(), new TimeIterations(1)));
    }

    return ret;
  }

  private void writeHeader() {
    final StringBuilder sb = new StringBuilder();
    for (final TestData data : OfficialTestData.values()) {
      sb.append("FI-")
        .append(data.getFile().getName())
        .append('\t')
        .append("FH-")
        .append(data.getFile().getName())
        .append('\t');
    }
    FileUtils.writeString(sb.toString(), resultCsv);
  }

  @Override
  protected void notifyStart() {
    writeHeader();
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    super.notifyFinished(e, iteration);
    final int numColumns = OfficialTestData.values().length * 2;
    final StringBuilder sb = new StringBuilder();
    sb.append((iteration % numColumns) == 0 ? '\n': '\t')
      .append(e.getHighestQuality().getScalar());
    FileUtils.appendString(sb.toString(), resultCsv);
  }
}
