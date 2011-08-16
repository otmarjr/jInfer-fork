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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement;

import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;

/**
 *
 * @author vitasek
 */
public enum ImprovementHeuristics {

  IDENTITY(Identity.class),
  HUNGRY(Hungry.class),
  RANDOM_REMOVE(RandomRemove.class),
  REMOVE_WORST(RemoveWorst.class),
  MUTATION(Mutation.class),
  CROSSOVER(Crossover.class),
  LOCAL_BRANCHING(LocalBranching.class);

  private final Class<? extends ImprovementHeuristic> clazz;

  private ImprovementHeuristics(final Class<? extends ImprovementHeuristic> clazz) {
    this.clazz = clazz;
  }

  public Class<? extends ImprovementHeuristic> getClazz() {
    return clazz;
  }

}
