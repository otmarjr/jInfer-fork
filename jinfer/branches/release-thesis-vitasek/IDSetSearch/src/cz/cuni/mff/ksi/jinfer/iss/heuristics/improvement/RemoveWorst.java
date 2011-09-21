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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement;

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.util.List;

/**
 * Simple "improvement" heuristic that works by removing the worst solution
 * ({@link IdSet}) from the feasible pool.
 *
 * @author vektor
 */
public class RemoveWorst implements ImprovementHeuristic {

  @Override
  public void start(final Experiment experiment, final List<IdSet> feasiblePool,
        final HeuristicCallback callback) throws InterruptedException {
    final IdSet worst = Utils.getWorst(experiment, feasiblePool);
    feasiblePool.remove(worst);
    callback.finished(feasiblePool);
  }

  @Override
  public String getName() {
    return "RemoveWorst";
  }

  @Override
  public String getDisplayName() {
    return "Remove Worst";
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

}
