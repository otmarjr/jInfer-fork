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
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.RandomRemove;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.RemoveWorst;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment tweaking settings in chained IHs scenario 1.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class ChainedIHs1Tweak extends AbstractExperimentSet {

  @Override
  public String getName() {
    return "Chained IHs 1 Tweak";
  }

  public static final List<TestData> TEST_DATA = Arrays.<TestData>asList(
          OfficialTestData.GRAPH_100_100, OfficialTestData.GRAPH_100_200,
          OfficialTestData.GRAPH_100_1000, OfficialTestData.OVA1,
          OfficialTestData.OVA2, OfficialTestData.OVA3);

  private static final double[] RR_RATIOS = new double[] {0, 0.05, 0.1, 0.2, 0.5};
  private static final double[] MUT_RATIOS = new double[] {0.05, 0.1, 0.2};
  private static final double[] CX_RATIOS = new double[] {0.05, 0.1, 0.2};

  private List<ImprovementHeuristic> getIHs(final double randomRemoveRatio,
          final double mutationRatio, final double crossoverRatio) {
    return Arrays.<ImprovementHeuristic>asList(
            new RandomRemove(randomRemoveRatio),
            new Mutation(mutationRatio, 1),
            new RandomRemove(randomRemoveRatio),
            new Crossover(crossoverRatio, 1),
            new RemoveWorst());
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (final double randomRemoveRatio : RR_RATIOS) {
      for (final double mutationRatio : MUT_RATIOS) {
        for (final double crossoverRatio : CX_RATIOS) {
          final List<ImprovementHeuristic> improvement = getIHs(randomRemoveRatio, mutationRatio, crossoverRatio);

          for (final TestData data : TEST_DATA) {
            for (int i = 0; i < Utils.getIterations(); i++) {
              ret.add(new ExperimentParameters(data.getFile(),
                      POOL_SIZE, 1, 1, data.getKnownOptimum(),
                      new Random(), improvement, new Weight(),
                      new TimeIterations(10000, 10000)));
            }
          }
        }
      }
    }

    return ret;
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    // TODO vektor Better approach to this is the list of experiment names, see VariousBetas
    final int iter1 = iteration / Utils.getIterations(); // get rid of the innermost loop
    final TestData data = TEST_DATA.get(iter1 % TEST_DATA.size());
    final int iter2 = iter1 / TEST_DATA.size(); // get rid of the data loop
    final double cxRatio = CX_RATIOS[iter2 % CX_RATIOS.length];
    final int iter3 = iter2 / CX_RATIOS.length; // get rid of the CX loop
    final double mutRatio = MUT_RATIOS[iter3 % MUT_RATIOS.length];
    final int iter4 = iter3 / MUT_RATIOS.length; // get rid of the MUT loop
    final double rrRatio = RR_RATIOS[iter4 % RR_RATIOS.length];

    FileUtils.appendString(rrRatio + "\t" +
            mutRatio + "\t" + cxRatio + "\t" + e.getTotalTime() + "\t" +
            data.getFile().getName() + "\t" +
            e.getTerminationReason().getSecond() + "\n", resultCsv);
  }

}
