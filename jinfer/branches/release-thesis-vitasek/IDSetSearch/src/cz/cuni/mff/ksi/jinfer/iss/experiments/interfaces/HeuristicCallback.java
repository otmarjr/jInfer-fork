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
package cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces;

import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import java.util.List;

/**
 * Callback interface used by heuristics to report their results to
 * their invoker.
 *
 * @author vektor
 */
public interface HeuristicCallback {

  /**
   * Report that a heuristic has finished and get the pool of solutions from
   * the argument.
   *
   * @param feasiblePool List of feasible solutions found by this run of the
   * heuristic.
   */
  void finished(final List<IdSet> feasiblePool);

}
