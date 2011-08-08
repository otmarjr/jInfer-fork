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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax;

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import java.util.Arrays;

/**
 * Construction heuristic - encapsulation of the heuristic proposed in the
 * "Finding ID Attributes in XML Documents" article.
 * 
 * Note that the pool created by this heuristic contains only one solution
 * ({@link IdSet}).
 *
 * @author vektor
 */
public class Fidax implements ConstructionHeuristic {

  @Override
  public void start(final Experiment experiment,
        final HeuristicCallback callback) throws InterruptedException {
    final IdSet solution = FidaxAlgorithm.findIDSet(experiment.getModel(), experiment.getAlpha(), experiment.getBeta());
    callback.finished(Arrays.asList(solution));
  }

  @Override
  public String getName() {
    return "FIDAX";
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

}
