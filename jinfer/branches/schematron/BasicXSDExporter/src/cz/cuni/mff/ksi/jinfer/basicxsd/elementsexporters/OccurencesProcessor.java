/*
 *  Copyright (C) 2011 rio
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

package cz.cuni.mff.ksi.jinfer.basicxsd.elementsexporters;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;

/**
 * Helper class to process <code>minOccurs</code> and <code>maxOccurs</code>
 * attributes using a given {@see RegexpInterval}.
 * @author rio
 */
final class OccurencesProcessor {
  
  private static final int MINOCCURS_DEFAULT = 1;
  private static final int MAXOCCURS_DEFAULT = 1;

  private OccurencesProcessor() {
  }

  /**
   * Creates a string representing {@see RegexpInterval} which can be directly
   * inserted into an XSD element definition.
   * @param interval Interval to create XSD representation for.
   * @return String representing {@see RegexpInterval} in XSD.
   */
  public static String processOccurrences(final RegexpInterval interval) {
    final StringBuilder resultBuilder = new StringBuilder();
    final int minOccurs = interval.getMin();
    if (minOccurs != MINOCCURS_DEFAULT) {
      resultBuilder.append(" minOccurs=\"");
      resultBuilder.append(Integer.toString(minOccurs));
      resultBuilder.append("\"");
    }

    if (interval.isUnbounded()) {
      resultBuilder.append(" maxOccurs=\"unbounded\"");
    } else {
      final int maxOccurs = interval.getMax();
      if (maxOccurs != MAXOCCURS_DEFAULT) {
        resultBuilder.append(" maxOccurs=\"");
        resultBuilder.append(Integer.toString(maxOccurs));
        resultBuilder.append("\"");
      }
    }

    return resultBuilder.toString();
  }

}
