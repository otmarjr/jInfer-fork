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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces;

import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public interface TerminationCriterion {

  /**
   * Returns a flag whether to terminate the iterations of the metaheuristic.
   *
   * @param time Total time taken so far.
   * @param solutions The pool solutions that were produced in the last run.
   * @return <code>True</code> if the metaheuristic should be terminated,
   * <code>false<code> otherwise.
   */
  boolean terminate(final long time, final List<IdSet> solutions);

}
