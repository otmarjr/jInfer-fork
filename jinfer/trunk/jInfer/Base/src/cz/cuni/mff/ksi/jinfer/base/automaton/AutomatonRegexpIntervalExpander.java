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
package cz.cuni.mff.ksi.jinfer.base.automaton;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for recursive expanding of all intervals of regexps, that cannot be
 * represented in DTD format. That is, interval in form {m, n}, where m > 0
 * or n is not infinity (DTD has only ?, *, + intervals).
 *
 * Expansion is done recursively, every regexp in form
 * a{m > 0, n} is replaced by regexp in form
 * (a,a,a,a, ... (m-1 times),a{1,infinity})
 * that is declaration
 * <ELEMENT xxx (a,a,a,a, ... (m-1 times),a+)>
 * in DTD language.
 *
 * @author anti
 */
public final class AutomatonRegexpIntervalExpander {

  private AutomatonRegexpIntervalExpander() {
    
  }

  private static boolean isSafeInterval(final RegexpInterval interval) {
    if (interval.isOnce()
            || interval.isOptional()
            || interval.isKleeneStar()
            || interval.isKleeneCross()) {
      return true;
    }
    return false;
  }

  public static <T> Regexp<T> expandIntervalsRegexp(
          final Regexp<T> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return regexp;
      case TOKEN:
          if (isSafeInterval(regexp.getInterval())) {
            return regexp;
          }

          final List<Regexp<T>> l = new ArrayList<Regexp<T>>();
          for (int i = 0; i < regexp.getInterval().getMin(); i++) {
            l.add(Regexp.<T>getToken(regexp.getContent()));
          }
          l.add(Regexp.<T>getToken(regexp.getContent(), RegexpInterval.getKleeneStar()));
          return Regexp.<T>getConcatenation(l);
      case ALTERNATION:
      case CONCATENATION:
      case PERMUTATION:
        final List<Regexp<T>> children = new ArrayList<Regexp<T>>(regexp.getChildren().size());
        for (final Regexp<T> child : regexp.getChildren()) {
          children.add(expandIntervalsRegexp(child));
        }

        if (isSafeInterval(regexp.getInterval())) {
          return new Regexp<T>(null, children, regexp.getType(), regexp.getInterval());
        }

        final List<Regexp<T>> m = new ArrayList<Regexp<T>>();
        for (int i = 0; i < regexp.getInterval().getMin(); i++) {
          m.add(new Regexp<T>(null, children, regexp.getType(), RegexpInterval.getOnce()));
        }
        m.add(new Regexp<T>(null, children, regexp.getType(), RegexpInterval.getKleeneStar()));
        return Regexp.<T>getConcatenation(m);
      default:
        throw new IllegalArgumentException("Unknown regexp type" + regexp.getType());
    }
  }
}
