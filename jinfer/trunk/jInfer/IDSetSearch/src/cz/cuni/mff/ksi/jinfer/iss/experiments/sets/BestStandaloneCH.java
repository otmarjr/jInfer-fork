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
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.ConstructionHeuristics;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Fuzzy;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax.Fidax;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment comparing all the CHs, without any IHs.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class BestStandaloneCH extends AbstractExperimentSet {

  private File finalCsv = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result.txt");

  private static final int POOL_SIZE = 10;

  @Override
  public String getName() {
    return "Best Standalone CH";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (final TestData data : OfficialTestData.values()) {
      for (int i = 0; i < Constants.ITERATIONS; i++) {
        for (final ConstructionHeuristics heu : ConstructionHeuristics.values()) {
          ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                  data.getKnownOptimum(), new Random(),
                  improvement, new Weight(), TimeIterations.NULL));
        }
      }
    }

    return ret;
  }

  private void writeHeader() {
    final StringBuilder sb = new StringBuilder();
    for (final ConstructionHeuristics heu : ConstructionHeuristics.values()) {
      try {
        // TODO this won't work, not everybody has a non-parametric constructor
        sb.append(heu.getClazz().newInstance().getName())
          .append('\t');
      } catch (final InstantiationException ex) {
        Exceptions.printStackTrace(ex);
      } catch (final IllegalAccessException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    FileUtils.writeString(sb.toString(), finalCsv);
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
    FileUtils.appendString(sbQuality.toString(), finalCsv);
  }
}
