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

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;

/**
 * Interface representing a construction heuristic. Its responsibility is to
 * create one or more starting solutions from the model provided in the
 * experiment to be optimized later in improvement heuristics.
 *
 * Note that the solution(s) created by this heuristic might already be optimal.
 * In this case it is up to the experiment to recognize this and stop the
 * optimizations.
 *
 * Note that heuristics use the callback "design pattern", meaning that the
 * result of this heuristic is returned via a callback provided while invoking it.
 *
 * @author vektor
 */
public interface ConstructionHeuristic extends Heuristics {

  /**
   * Start the heuristic run. Use the information in the provided experiment to
   * create a pool (possibly only one) of start solutions.
   *
   * @param experiment Experiment in context of which to run the heuristic.
   * @param callback Callback to be invoked when finished.
   */
  void start(final Experiment experiment, final HeuristicCallback callback)
          throws InterruptedException;

}
