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
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.SizeTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.TestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * Runs all the official test data to see how long it takes them to communicate
 * with GLPK (which is used as CH, no time limit). No IH is employed thereafter.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class GlpkInterfaceTiming extends AbstractExperimentSet {

  @Override
  public String getName() {
    return "GLPK Interface Timing";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (int i = 0; i < Utils.getIterations(); i++) {
      for (final TestData data : OfficialTestData.values()) {
        ret.add(new ExperimentParameters(data.getFile(), 1, 1, 1, data.getKnownOptimum(),
                new Glpk(), improvement, new Weight(), TimeIterations.NULL));
      }

      for (final TestData data : SizeTestData.values()) {
        ret.add(new ExperimentParameters(data.getFile(), 1, 1, 1, data.getKnownOptimum(),
                new Glpk(), improvement, new Weight(), TimeIterations.NULL));
      }
    }

    return ret;
  }

  @Override
  protected void notifyStart() {
    final EnhancedPatternLayout fileLayout = new EnhancedPatternLayout("%m%n");
    try {
      Logger.getRootLogger().addAppender(new FileAppender(fileLayout, Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result.txt"));
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }
}
