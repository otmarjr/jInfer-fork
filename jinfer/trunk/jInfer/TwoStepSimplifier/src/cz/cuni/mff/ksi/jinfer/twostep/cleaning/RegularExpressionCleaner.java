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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

/**
 * Interface for all regular expression cleaners.
 * 
 * Cleaner take one regular expression tree and outputs
 * another one, with equal language, but simpler in some sort.
 *
 * @author anti
 */
public interface RegularExpressionCleaner<T> {

  /**
   * Clean given regular expression.
   *
   * Returns new regexp which should be nicer in some way (no empty constructs, or
   * no shorter form). But which has to express same language.
   * 
   * @param regexp regexp to clean
   * @return cleaned regexp.
   */
  Regexp<T> cleanRegularExpression(final Regexp<T> regexp);
}
