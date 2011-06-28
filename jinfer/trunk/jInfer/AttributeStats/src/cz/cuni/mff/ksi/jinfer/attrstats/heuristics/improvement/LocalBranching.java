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
package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class LocalBranching implements ImprovementHeuristic {

  @Override
  public void start(final Experiment experiment, final List<IdSet> feasiblePool,
        final HeuristicCallback callback) throws InterruptedException {
    // TODO vektor Implement

    // Add a constrain - new solution cannot be far from the incumbent
    // Parameter k - max. Hamming distance

    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getName() {
    return "LocalBranching";
  }

  @Override
  public String getDisplayName() {
    return "Local Branching";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

}