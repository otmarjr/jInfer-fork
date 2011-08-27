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
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.SizeTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * This experimental set compares Random and Glpk as CHs for Mutation IH.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class CHForMutation extends AbstractExperimentSet {

  private static final List<TestData> TEST_DATA = Arrays.<TestData>asList(
          SizeTestData.GRAPH_80_320, SizeTestData.GRAPH_90_405,
          SizeTestData.GRAPH_100_500, OfficialTestData.GRAPH_100_100,
          OfficialTestData.GRAPH_100_200, OfficialTestData.GRAPH_100_1000);

  private static final int POOL_SIZE = 10;

  @Override
  public String getName() {
    return "CH for Mutation";
  }


  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Mutation(0.1, 1));

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < Utils.getIterations(); i++) {
      for (final TestData data : TEST_DATA) {
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(), new Random(), improvement, new Weight(), new TimeIterations(1)));
        ret.add(new ExperimentParameters(data.getFile(), POOL_SIZE, 1, 1,
                data.getKnownOptimum(), new Glpk(1), improvement, new Weight(), new TimeIterations(1)));
      }
    }

    return ret;
  }


}
