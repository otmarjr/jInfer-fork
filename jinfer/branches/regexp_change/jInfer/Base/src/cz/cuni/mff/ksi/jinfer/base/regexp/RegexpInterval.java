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
public class RegexpInterval {
  private final int min;
  private final int max;
  private final boolean unbounded;

  public RegexpInterval(final int min, final int max) {
    this.min= min;
    this.max= max;
    this.unbounded= false;
  }

  public RegexpInterval(final int min) {
    this.min= min;
    this.max= -1;
    this.unbounded= true;
  }

  /**
   * get a bounded interval, specify min, max integers
   * @param min
   * @param max
   * @return
   */
  public static RegexpInterval getBounded(final int min, final int max) {
    return new RegexpInterval(min, max);
  }

  /**
   * get right unbounded interval, specify only minimum, maximum is considered
   * unbounded
   * @param min
   * @return
   */
  public static RegexpInterval getUnbounded(final int min) {
    return new RegexpInterval(min);
  }

  /**
   * get interval with exactly once meaning, that is min=max=1
   * @return
   */
  public static RegexpInterval getOnce() {
    return new RegexpInterval(1,1);
  }

  /**
   * get ? quantification, that is zero or once. min = 0, max = 1
   */
  public static RegexpInterval getOptional() {
    return new RegexpInterval(0, 1);
  }

  /**
   * get KleeneStar *, asterisk. That is zero or more, unbounded. min =0, max is
   * unbounded.
   * @return
   */
  public static RegexpInterval getKleeneStar() {
    return new RegexpInterval(0);
  }

  /**
   * get KleeneCross, that is + quantification, min= 1, max is unbounded
   * @return
   */
  public static RegexpInterval getKleeneCross() {
    return new RegexpInterval(1);
  }

  public boolean isUnbounded() {
    return unbounded;
  }

  public boolean isOnce() {
    return (!unbounded) && (min == 1) && (max == 1);
  }

  public boolean isOptional() {
    return (!unbounded) && (min == 0) && (max == 1);
  }

  public boolean isKleeneStar() {
    return (unbounded) && (min == 0);
  }

  public boolean isKleeneCross() {
    return (unbounded) && (min == 1);
  }

  public int getMax() {
    if (this.unbounded) {
      throw new IllegalStateException("Cannot return regexp bound max, as this is unbounded. Should verify by a call to isUnbounded() before trying to get max.");
    }
    return max;
  }

  public int getMin() {
    return min;
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