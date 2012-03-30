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
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.SizeTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Hungry;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.LocalBranching;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment finding the best IH for GLPK as CH.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class BestIHForGlpk extends AbstractExperimentSet {

  public static final List<TestData> TEST_DATA = Arrays.<TestData>asList(
          SizeTestData.GRAPH_80_320, SizeTestData.GRAPH_90_405,
          SizeTestData.GRAPH_100_500, OfficialTestData.GRAPH_100_100,
          OfficialTestData.GRAPH_100_200, OfficialTestData.GRAPH_100_1000);

  private File file;

  @Override
  public String getName() {
    return "Best IH for GLPK";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (final TestData data : TEST_DATA) {
      for (int i = 0; i < Utils.getIterations(); i++) {
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(),
                new Glpk(1), Arrays.<ImprovementHeuristic>asList(new Crossover(0.1, 1)),
                new Weight(), new TimeIterations(1)));
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(),
                new Glpk(1), Arrays.<ImprovementHeuristic>asList(new Hungry()),
                new Weight(), new TimeIterations(1)));
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(),
                new Glpk(1), Arrays.<ImprovementHeuristic>asList(new LocalBranching(0.1, 1)),
                new Weight(), new TimeIterations(1)));
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(),
                new Glpk(1), Arrays.<ImprovementHeuristic>asList(new Mutation(0.1, 1)),
                new Weight(), new TimeIterations(1)));
      }
    }

    return ret;
  }

  private void writeHeader() {
    FileUtils.writeString("Crossover-b\tCrossover-a\tHungry-b\tHungry-a\tLocalBranching-b\tLocalBranching-a\tMutation-b\tMutation-a", file);
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    final int numColumns = 4;

    file = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result-" +
           TEST_DATA.get(iteration / (Utils.getIterations() * numColumns)).getFile().getName() + ".txt");

    if ((iteration % (Utils.getIterations() * numColumns)) == 0) {
      writeHeader();
    }

    final StringBuilder sb = new StringBuilder();
    sb.append((iteration % numColumns) == 0 ? '\n': '\t')
      .append(e.getConstructionResult().getQuality().getScalar())
      .append('\t')
      .append(e.getHighestQuality().getScalar());
    FileUtils.appendString(sb.toString(), file);
  }

}
