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
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import java.util.List;

/**
 * Interface encapsulating the decision whether to stop running the metaheuristics.
 *
 * This is based on the total time already spent as well as any properties of
 * the solutions found so far.
 *
 * @author vektor
 */
public interface TerminationCriterion {

  /**
   * Returns a flag whether to terminate the iterations of the metaheuristic.
   *
   * @param experiment Experiment in context of which to measure the quality.
   * @param time Total time taken so far.
   * @param solutions The pool solutions that were produced in the last run.
   * @return The first item of the pair is <code>true</code> if the
   * metaheuristic should be terminated, <code>false<code> otherwise. The second
   * is a string describing why the metaheuristics was terminated, in
   * user-friendly language.
   */
  Pair<Boolean, String> terminate(final Experiment experiment, final long time, final List<IdSet> solutions);

}
