/*
 *  Copyright (C) 2010 anti
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.base.regexp;

/**
 * Class representing interval of each regexp. As in character regexp, which
 * can have intervals for occurrences in form:
 * {m, n} - at least m, at most n
 * {m, } - at least m to infinity
 * {, n} - zero to at most n
 * Every regexp has an interval. Every interval has minimum as a natural number.
 * Maximum can be unbounded and for this case, calling 'isUnbounded' will reveal.
 *
 * Calling getMax when interval is unbounded causes exception.
 * There are some helper functions for common cases (*;+;?;{m,};{1,1})
 *
 * @author anti
 */
public final class RegexpInterval {
  /** Minimum value */
  private final int min;
  /** Maximum value */
  private final int max;
  /** If it is unbounded, in case of unbouded, maximum value has no sense */
  private final boolean unbounded;

  /** Create bounded interval with min/max */
  private RegexpInterval(final int min, final int max) {
    this.min = min;
    this.max = max;
    this.unbounded = false;
  }

  /** Create unbouded interval with min */
  private RegexpInterval(final int min) {
    this.min = min;
    this.max = -1;
    this.unbounded = true;
  }

  /** Clone existing interval\
   *
   * @return copy of interval
   */
  public RegexpInterval getCopy() {
    if (unbounded) {
      return RegexpInterval.getUnbounded(min);
    }
    return RegexpInterval.getBounded(min, max);
  }

  /**
   * Get a bounded interval, specify min, max integers
   * 
   * @param min interval min
   * @param max interval max
   * @return new interval
   */
  public static RegexpInterval getBounded(final int min, final int max) {
    return new RegexpInterval(min, max);
  }

  /**
   * Get right unbounded interval, specify only minimum, maximum is considered
   * unbounded
   * @param min interval min
   * @return new interval
   */
  public static RegexpInterval getUnbounded(final int min) {
    return new RegexpInterval(min);
  }

  /**
   * Get interval with exactly once meaning, that is min=max=1
   * @return new interval
   */
  public static RegexpInterval getOnce() {
    return new RegexpInterval(1, 1);
  }

  /**
   * Get ? quantification, that is zero or once. min = 0, max = 1
   * @return new interval
   */
  public static RegexpInterval getOptional() {
    return new RegexpInterval(0, 1);
  }

  /**
   * Get KleeneStar *, asterisk. That is zero or more, unbounded. min =0, max is
   * unbounded.
   * @return new interval
   */
  public static RegexpInterval getKleeneStar() {
    return new RegexpInterval(0);
  }

  /**
   * Get KleeneCross, that is + quantification, min= 1, max is unbounded
   * @return new interval
   */
  public static RegexpInterval getKleeneCross() {
    return new RegexpInterval(1);
  }

  /**
   * Whether interval is unbouded.
   * @return true when interval is unbounded
   */
  public boolean isUnbounded() {
    return unbounded;
  }

  /**
   * Whether interval is bounded {1,1}.
   * @return true when interval is "exactly once", false otherwise
   */
  public boolean isOnce() {
    return (!unbounded) && (min == 1) && (max == 1);
  }

  /**
   * Whether interval is ?
   * @return true when interval is {0,1}
   */
  public boolean isOptional() {
    return (!unbounded) && (min == 0) && (max == 1);
  }

  /**
   * Whether interval is *
   * @return true when interval is unbounded {0,}
   */
  public boolean isKleeneStar() {
    return (unbounded) && (min == 0);
  }

  /**
   * Whether interval is +
   * @return true when interval is unbounded {1,}
   */
  public boolean isKleeneCross() {
    return (unbounded) && (min == 1);
  }

  /**
   * Get the maximum value, throws exception on unbounded interval (ask using {@link isUnbounded()} before calling getMax().
   * @return max value
   */
  public int getMax() {
    if (this.unbounded) {
      throw new IllegalStateException("Cannot return regexp bound max, as this is unbounded. Should verify by a call to isUnbounded() before trying to get max.");
    }
    return max;
  }

  /**
   * Get minimum value
   * @return min value
   */
  public int getMin() {
    return min;
  }

  /**
   * Create an interval that is within both intervals provided as arguments.
   * Method is symmetrical, so the arguments can be exchanged.
   * @param first First of the intervals.
   * @param second Second of the intervals.
   * @return Intersection of intervals or null.
   * @throws IllegalArgumentException When either of the arguments is null, or intervals have no intersection.
   */
  public static RegexpInterval intersectIntervals(
          final RegexpInterval first, final RegexpInterval second) throws IllegalArgumentException {
    int lower, upper;
    if (first == null || second == null) {
      throw new IllegalArgumentException("Both intervals must not be null");
    }
    if (first.isUnbounded() && second.isUnbounded()) {
      // both unbounded, lower bound is the larger of the two mins, and at least 0
      lower = Math.max(0, Math.max(first.getMin(), second.getMin()));
      return RegexpInterval.getUnbounded(lower);
    }
    checkBounds(first);
    checkBounds(second);
    if(first.isUnbounded()) {
      // unbounded first max -> use max of second, it cannot be unbounded now
      upper = second.getMax();
    } else if (second.isUnbounded()) {
      // unbounded second max -> use max of first, it cannot be unbounded now
      upper = first.getMax();
    } else {
      // neither is unbounded, use the smaller one, but make sure it is at least zero
      upper = Math.min(Math.max(0, first.getMax()), Math.max(0, second.getMax()));
    }

    if (upper < first.getMin() || upper < second.getMin()) {
      throw new IllegalArgumentException("Intervals have no intersection.");
    }
    // lower limit is the larger of the two, but at most equal to the upper bound
    lower = Math.min(upper, Math.max(first.getMin(), second.getMin()));
    return RegexpInterval.getBounded(lower, upper);
  }

  private static void checkBounds(final RegexpInterval interval) throws IllegalArgumentException {
    if (!interval.isUnbounded() && (interval.getMin() > interval.getMax()) ) {
      throw new IllegalArgumentException("Upper limit of interval must be greater or equal to the lower limit.");
    }
  }

  @Override
  public String toString() {
    if (isOnce()) {
      return "";
    } else if (isOptional()) {
      return "?";
    } else if (isKleeneStar()) {
      return "*";
    } else if (isKleeneCross()) {
      return "+";
    } else if (isUnbounded()) {
      return "{" + min + ",}";
    } else {
      return "{" + min + "," + max + "}";
    }
  }
}
