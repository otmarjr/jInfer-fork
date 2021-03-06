/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.basicigg.expansion;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A few useful functions for complex regexp expansion. Library class.
 * 
 * @author vektor
 */
public final class ExpansionHelper {

  private ExpansionHelper() {
  }

  /**
   * Applies the regexp interval on the set of words. For this function,
   * word is a {@see List} of its letters (see type parameter).
   * The actual list of generated words depends on the interval in following way:
   *
   * <ul>
   *   <li>"once": result is the original set of words.</li>
   *   <li>Optional (<code>?</code>): result is the original set of words + an empty word.</li>
   *   <li>Kleene cross (<code>+</code>): for each original word <code>W</code>,
   *        the result contains <code>W</code> and <code>WWW</code>
   *        (concatenation of 3 <code>W</code>'s).</li>
   *   <li>Kleene star (<code>*</code>): result is an empty word + what would
   *        Kleene cross generate.</li>
   *   <li>Interval (<code>{m, n}</code>): for each original word <code>W</code>,
   *        the result contains a concatenation of <code>m</code>
   *        <code>W</code>'s and a concatenation of <code>n</code>
   *        <code>W</code>'s.</li>
   * </ul>
   *
   * @param <T> Type of the letters of words in this function.
   * @param input List of words (lists of letters) to be processed.
   * @param ri Regexp interval to be applied on the words.
   * @return List of resulting words.
   */
  public static <T> List<List<T>> applyInterval(
          final List<List<T>> input,
          final RegexpInterval ri) {
    if (ri == null) {
      throw new IllegalArgumentException("Regexp interval must not be null.");
    }
    if (input == null) {
      throw new IllegalArgumentException("Input list of words must not be null.");
    }

    final List<List<T>> ret = new ArrayList<List<T>>();

    if (ri.isOnce()) {
      return input;
    } else if (ri.isOptional()) {
      ret.add(Collections.<T>emptyList());
      ret.addAll(input);
    } else if (ri.isKleeneCross()) {
      for (final List<T> l : input) {
        ret.add(l);
        ret.add(BaseUtils.cloneList(l, 3));
      }
    } else if (ri.isKleeneStar()) {
      ret.add(Collections.<T>emptyList());
      for (final List<T> l : input) {
        ret.add(l);
        ret.add(BaseUtils.cloneList(l, 3));
      }
    } else {
      for (final List<T> l : input) {
        ret.add(BaseUtils.cloneList(l, ri.getMin()));
        ret.add(BaseUtils.cloneList(l, ri.getMax()));
      }
    }

    return ret;
  }

  /**
   * Returns flag if the provided regexp is a simple concatenation.
   * Regexp is a simple concatenation if all these conditions hold:
   * <ul>
   *  <li>Its type is {@see RegexpType#CONCATENATION}.</li>
   *  <li>Its interval is "once" ({@see RegexpInterval#isOnce()}).</li>
   *  <li>
   *   For each of its children holds:
   *    <ul>
   *      <li>The child is {@see RegexpType#TOKEN}</li>
   *      <li>The child's interval is "once" ({@see RegexpInterval#isOnce()}).</li>
   *    </ul>
   *  </li>
   * </ul>
   *
   * @param r Regexp to be examined.
   * @return True if the regexp is a simple concatenation, false otherwise.
   */
  public static boolean isSimpleConcatenation(
          final Regexp<AbstractStructuralNode> r) {
    if (r == null) {
      throw new IllegalArgumentException("Regexp must not be null.");
    }

    if (!r.isConcatenation()) {
      return false;
    }
    if (!r.getInterval().isOnce()) {
      return false;
    }
    for (final Regexp<AbstractStructuralNode> child : r.getChildren()) {
      if (!child.isToken()) {
        return false;
      }
      if (!child.getInterval().isOnce()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns an empty, immutable concatenation with interval "once".
   */
  public static <T> Regexp<T> getEmptyConcat() {
    final Regexp<T> ret = Regexp.getMutable();
    ret.setType(RegexpType.CONCATENATION);
    ret.setInterval(RegexpInterval.getOnce());
    ret.setImmutable();
    return ret;
  }
}
