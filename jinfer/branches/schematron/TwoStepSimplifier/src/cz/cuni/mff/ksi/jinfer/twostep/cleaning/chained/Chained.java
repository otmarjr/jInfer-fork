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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.chained;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleanerFactory;
import java.util.List;

/**
 * Chains one or more cleaners in a row, pipelining output from first one
 * to input of second one, and so on. Resulting in sequentially applying
 * cleaners selected in preferences.
 *
 * @author anti
 */
public class Chained<T> implements RegularExpressionCleaner<T> {

  private final List<RegularExpressionCleanerFactory> cleanerFactories;

  /**
   * Create with list of factories of cleaner to use in a chain.
   *
   * @param cleanerFactories list of factories of other cleaners to call.
   */
  public Chained(final List<RegularExpressionCleanerFactory> cleanerFactories) {
    this.cleanerFactories = cleanerFactories;
  }

  @Override
  public Regexp<T> cleanRegularExpression(final Regexp<T> regexp) {
    Regexp<T> newRegexp = regexp;
    for (RegularExpressionCleanerFactory f : cleanerFactories) {
      newRegexp = f.<T>create().cleanRegularExpression(newRegexp);
    }
    return newRegexp;
  }
}
