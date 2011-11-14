/*
 *  Copyright (C) 2011 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;

/**
 * Helper class for creating intervals from string representation of occurrences.
 * @author reseto
 */
public final class XSDOccurences {
  private XSDOccurences() {}

  /**
   * According to XSD Schema specification, the default values for min and max are 1.
   */
  private static final int DEFAULT = 1;

  /**
   * Internal representation of no limit for upper bound.
   */
  private static final int INFINITY = -1;

  /**
   * String specifying that there is no limit of maximum occurrences.
   * According to XSD Schema specification, it is the word 'unbounded' in lowercase.
   */
  private static final String UNBOUNDED = "unbounded";
  
  /**
   * Create interval from arguments.
   * If any of the arguments is empty, or invalid, a default value is used.
   * Default values of min and max occurrences are 1, as defined by XSD Schema specification.
   * Hence, default interval is ({@link RegexpInterval#getOnce() }).
   * In case the parsed value of lower limit exceeds the upper, both bounds of the interval
   * are set to the upper limit value (that is the smaller of those two).
   * @param minOccurence String containing the value of lower limit of occurrences, which will be parsed.
   * @param maxOccurence String containing the value of upper limit of occurrences, which will be parsed.
   * @return Valid interval determined by the parsed values.
   */
  public static RegexpInterval createInterval(final String minOccurence, final String maxOccurence) {
    return makeInterval(minOccurence, maxOccurence, false);
  }

  /**
   * Create interval from arguments.
   * If any of the arguments is empty, or invalid, a default value is used.
   * Default values of min and max occurrences are 1, as defined by XSD Schema specification.
   * This method should not throw any exceptions.
   * @param minOccurence String containing the value of lower limit of occurrences, which will be parsed.
   * @param maxOccurence String containing the value of upper limit of occurrences, which will be parsed.
   * @param minHasPriority Set priority to either upper limit
   * (if <code>false</code>) or lower limit (if <code>true</code>), in case the lower limit exceeds the upper.
   * @return Valid interval determined by the parsed values.
   */
  public static RegexpInterval createInterval(final String minOccurence, final String maxOccurence, final boolean minHasPriority) {
    return makeInterval(minOccurence, maxOccurence, minHasPriority);
  }

  /**
   * Implementation of {@link #createInterval(java.lang.String, java.lang.String, boolean) }.
   * @return Valid interval.
   */
  private static RegexpInterval makeInterval(final String minOccurence, final String maxOccurence, final boolean minHasPriority) {
    RegexpInterval interval;
    int min = getMin(minOccurence);
    int max = getMax(maxOccurence, min);

    if (max != INFINITY && min > max) {         // limits are mismatched
      if (minHasPriority) {
        max = min;                              // lower limit has priority
      } else {
        min = max;                              // upper limit has priority
      }
    }
    if (max == INFINITY) {
      interval = RegexpInterval.getUnbounded(min);
    } else {
      interval = RegexpInterval.getBounded(min, max);
    }
    return interval;
  }

  private static int getMin(final String minOccurence) {
    int min = DEFAULT;
    if (!BaseUtils.isEmpty(minOccurence)) {
      try {
        min = Integer.parseInt(minOccurence);

        if (min < 0) {                          // if parsed value is negative, restore the default
          min = DEFAULT;
        }
      } catch (NumberFormatException e) {       // if parsing fails, the default is already set
        min = DEFAULT;
      }
    }
    return min;
  }

  private static int getMax(final String maxOccurence, final int min) {
    int max = DEFAULT;
    if (!BaseUtils.isEmpty(maxOccurence)) {
      try {
        if (UNBOUNDED.equals(maxOccurence)) {
          max = INFINITY;
        } else {
          max = Integer.parseInt(maxOccurence);
          if (max < 0) {                        // parsed value is negative
            max = min;                          // min is either default, or some good value
          }
        }
      } catch (NumberFormatException e) {
        max = min;                              // min is either default, or some good value
      }
    }
    return max;
  }
}
