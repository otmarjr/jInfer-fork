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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.Quality;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.List;

/**
 * Utility class for experiments.
 *
 * @author vektor
 */
public final class ExperimentalUtils {

  private ExperimentalUtils() {

  }

  /**
   * Returns the best solution from the provided pool in the context of the
   * provided experiment.
   *
   * @param experiment Experiment, in context of which to find the best solution.
   * @param solutions List of solutions among which to find the best one.
   * @return Best solution along with its {@link Quality quality}.
   */
  public static Pair<IdSet, Quality> getBest(final Experiment experiment,
          final List<IdSet> solutions) {
    IdSet bestSolution = null;
    Quality maxQuality = Quality.ZERO;
    for (final IdSet solution : solutions) {
      final Quality quality = experiment.getQualityMeasurement().measure(experiment, solution);
      if (quality.getScalar() >= maxQuality.getScalar()) {
        maxQuality = quality;
        bestSolution = solution;
      }
    }
    return new Pair<IdSet, Quality>(bestSolution, maxQuality);
  }

  /**
   * Returns the worst solution from the provided pool in the context of the
   * provided experiment.
   *
   * @param experiment Experiment, in context of which to find the worst solution.
   * @param solutions List of solutions among which to find the worst one.
   * @return Worst solution.
   */
  public static IdSet getWorst(final Experiment experiment,
          final List<IdSet> solutions) {
    IdSet worstSolution = null;
    Quality minQuality = null;
    for (final IdSet solution : solutions) {
      final Quality quality = experiment.getQualityMeasurement().measure(experiment, solution);
      if (minQuality == null
              || quality.getScalar() < minQuality.getScalar()) {
        minQuality = quality;
        worstSolution = solution;
      }
    }
    return worstSolution;
  }

}