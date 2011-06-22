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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments.termination;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.TerminationCriterion;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class TimeIterations implements TerminationCriterion {

  private final int maxIterations;
  private int iterations = 0;
  private final long maxTime;

  public TimeIterations(final int maxIterations, final long maxTime) {
    this.maxIterations = maxIterations;
    this.maxTime = maxTime;
  }

  @Override
  public boolean terminate(final long time, final List<IdSet> solutions) {
    iterations++;
    if (iterations > maxIterations || time > maxTime) {
      return true;
    }
    return false;
  }

}
