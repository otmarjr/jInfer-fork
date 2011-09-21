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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.nestedconcatenation;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;

/**
 * Replaces nested concatenations is resulting regular expressions.
 * For example expression: (a, (b, c)) will be transformed to nicer expression: (a, b, c).
 *
 * @author anti
 */
public class NestedConcatenation<T> implements RegularExpressionCleaner<T> {

  @Override
  public Regexp<T> cleanRegularExpression(final Regexp<T> regexp) {
    switch (regexp.getType()) {
      case TOKEN:
      case LAMBDA:
        return regexp;
      case ALTERNATION:
      case PERMUTATION:
        final Regexp<T> newRegexp = Regexp.<T>getMutable();
        newRegexp.setInterval(regexp.getInterval());
        newRegexp.setType(regexp.getType());
        for (Regexp<T> child : regexp.getChildren()) {
          newRegexp.getChildren().add(cleanRegularExpression(child));
        }
        newRegexp.setImmutable();
        return newRegexp;
      case CONCATENATION:
        final Regexp<T> newRegexp2 = Regexp.<T>getMutable();
        newRegexp2.setInterval(regexp.getInterval());
        newRegexp2.setType(regexp.getType());
        for (Regexp<T> child : regexp.getChildren()) {
          if (child.isConcatenation() && child.getInterval().isOnce()) {
            newRegexp2.getChildren().addAll(
                    cleanRegularExpression(child).getChildren());
          } else {
            newRegexp2.getChildren().add(cleanRegularExpression(child));
          }
        }
        newRegexp2.setImmutable();
        return newRegexp2;
      default:
        throw new IllegalArgumentException("Unknown regexp type: " + regexp.getType());
    }
  }
}
