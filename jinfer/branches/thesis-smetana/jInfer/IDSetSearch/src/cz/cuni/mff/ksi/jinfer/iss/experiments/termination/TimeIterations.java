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
package cz.cuni.mff.ksi.jinfer.iss.experiments.termination;

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Quality;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.TerminationCriterion;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import java.util.List;

/**
 * An implementation of the {@link TerminationCriterion} based on the total time
 * spent running the metaheuristics and the number of iterations - improvement
 * heuristics runs. Also, as soon as known optimum is reached, this criterion
 * is met.
 *
 * @author vektor
 */
public class TimeIterations implements TerminationCriterion {

  private static final double THRESHOLD = 0.0000001;
  private final int maxIterations;
  private int iterations = 0;
  private final long maxTime;

  /**
   * Constructor. Initializes this criterion to run at most the specified
   * number of iterations, max time is set to "infinity".
   *
   * @param maxIterations Maximum number of iterations - improvement
   * heuristics runs allowed.
   */
  public TimeIterations(final int maxIterations) {
    this(maxIterations, Long.MAX_VALUE);
  }

  /**
   * Full constructor. Initializes this criterion to run at most the specified
   * time or number of iterations, whichever comes first.
   *
   * @param maxIterations Maximum number of iterations - improvement
   * heuristics runs allowed.
   * @param maxTime Maximal time allowed, in milliseconds.
   */
  public TimeIterations(final int maxIterations, final long maxTime) {
    this.maxIterations = maxIterations;
    this.maxTime = maxTime;
  }

  /** Instance terminating immediately, without running any IHs. */
  public static final TimeIterations NULL = new TimeIterations(0);

  private static final Pair<Boolean, String> FALSE = new ImmutablePair<Boolean, String>(Boolean.FALSE, null);

  @Override
  public Pair<Boolean, String> terminate(final Experiment experiment,
        final long time, final List<IdSet> solutions) {
    // since this is the most relevant and interesting criterion, we test for it first
    if (experiment.getKnownOptimum() != null) {
      final Pair<IdSet, Quality> best = Utils.getBest(experiment, solutions);
      if (Math.abs(experiment.getKnownOptimum().doubleValue() - best.getSecond().getScalar()) < THRESHOLD) {
        return new ImmutablePair<Boolean, String>(Boolean.TRUE, "Known optimum reached.");
      }
    }

    iterations++;
    if (iterations > maxIterations) {
      return new ImmutablePair<Boolean, String>(Boolean.TRUE, "Maximum iterations exceeded.");
    }
    if (time > maxTime) {
      return new ImmutablePair<Boolean, String>(Boolean.TRUE, "Maximum time exceeded.");
    }

    return FALSE;
  }

}
