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
package cz.cuni.mff.ksi.jinfer.iss.utils;

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Quality;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import cz.cuni.mff.ksi.jinfer.iss.options.ISSPanel;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbPreferences;

/**
 * Attribute statistics module utils.
 *
 * @author vektor
 */
public final class Utils {

  private Utils() {

  }

  /**
   * Formatter that formats numbers to a maximum of 5 fraction digits.
   */
  public static final Format FORMAT = NumberFormat.getInstance();
  static {
    ((NumberFormat)FORMAT).setMaximumFractionDigits(5);
  }
  public static final String NA = "N/A";

  /**
   * Converts a boolean to {@link String} in form yes/no.
   *
   * @param b Boolean to be converted.
   * @return "yes" or "no".
   */
  public static String boolToString(final boolean b) {
    return b ? "yes" : "no";
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
    IdSet bestSolution = IdSet.EMPTY;
    Quality maxQuality = Quality.ZERO;
    for (final IdSet solution : solutions) {
      final Quality quality = experiment.getQualityMeasurement().measure(experiment, solution);
      if (quality.getScalar() >= maxQuality.getScalar()) {
        maxQuality = quality;
        bestSolution = solution;
      }
    }
    return new ImmutablePair<IdSet, Quality>(bestSolution, maxQuality);
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

  /**
   * Returns a new list, constructed from the provided list with the provided
   * element appended at the end.
   *
   * @param <T> Type parameter.
   * @param list List to append to.
   * @param element Element to be appended.
   * @return New appended list.
   */
  public static <T> List<T> append(final List<T> list, final T element) {
    final List<T> ret = new ArrayList<T>(list);
    ret.add(element);
    return ret;
  }

  /**
   * Returns the current time in milliseconds. Useless in absolute context, only
   * in relative, when measuring time spent in some operation.
   *
   * @return Current time in milliseconds.
   */
  public static long time() {
    return System.nanoTime() / 1000000;
  }

  /**
   * Returns the number of milliseconds passed since the specified time (again
   * in milliseconds).
   *
   * @param from Time from which to calculate the difference.
   * @return How many milliseconds have passed since the specified time.
   */
  public static long delta(final long from) {
    return time() - from;
  }

  /**
   * Gets the number of iterations from NB options.
   */
  public static int getIterations() {
    return NbPreferences.forModule(ISSPanel.class).getInt(ISSPanel.ITERATIONS_PROP, ISSPanel.ITERATIONS_DEFAULT);
  }

}
