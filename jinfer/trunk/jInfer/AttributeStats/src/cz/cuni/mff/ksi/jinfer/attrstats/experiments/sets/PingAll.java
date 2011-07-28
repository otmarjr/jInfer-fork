/*
 * Copyright (C) 2011 vitasek
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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments.sets;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.Identity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author vitasek
 */
public class PingAll extends AbstractExperimentSet {

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);

    for (final OfficialTestData data : OfficialTestData.values()) {
      ret.add(new ExperimentParameters(data.getFile(), 1, 1, 1, data.getKnownOptimum(),
              new Glpk(0), improvement, new Weight(), new TimeIterations(1, 10000)));
    }

    return ret;
  }

}
