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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.emptychildren;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import java.util.ArrayList;
import java.util.List;

/**
 * Cleans empty children and children with one child.
 * 
 * For example regular expression: ((), a, (b)), gets cleaned to (a, b).
 * First empty concatenation () is removed, and concatenation (b)
 * with only one child is replaced by token b.
 *
 * @author anti
 */
public class EmptyChildren<T> implements RegularExpressionCleaner<T> {

  @Override
  public Regexp<T> cleanRegularExpression(final Regexp<T> regexp) {
    return cleanRecursive(regexp, true);
  }

  private Regexp<T> cleanRecursive(final Regexp<T> regexp, final boolean root) {
    switch (regexp.getType()) {
      case TOKEN:
      case LAMBDA:
        return regexp;
      case ALTERNATION:
      case PERMUTATION:
      case CONCATENATION:
        final List<Regexp<T>> newChildren = new ArrayList<Regexp<T>>();
        for (Regexp<T> child : regexp.getChildren()) {
          final Regexp<T> cleanChild = cleanRecursive(child, false);
          if (cleanChild != null) {
            newChildren.add(cleanChild);
          }
        }
        if (newChildren.isEmpty()) {
          if (root) {
            return Regexp.<T>getLambda();
          }
          return null;
        }
        if (newChildren.size() == 1) {
          return newChildren.get(0);
        }
        final Regexp<T> newRegexp = Regexp.<T>getMutable();
        newRegexp.setInterval(regexp.getInterval());
        newRegexp.setType(regexp.getType());
        newRegexp.getChildren().addAll(newChildren);
        newRegexp.setImmutable();
        return newRegexp;
      default:
        throw new IllegalArgumentException("Unknown regexp type: " + regexp.getType());
    }
  }
}
