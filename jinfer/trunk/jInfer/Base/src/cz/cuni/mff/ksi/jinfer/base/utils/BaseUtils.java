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
package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Various utility functions for jInfer. Library class.
 * 
 * @author vektor
 */
public final class BaseUtils {

  private BaseUtils() {
  }
  
  /**
   * Interface describing an object that decides whether the predicate
   * (described by this object) appliest to an argument or not.
   */
  public interface Predicate<T> {

    boolean apply(final T argument);
  }

  /**
   * Filters the collection, leaving only elements where predicate
   * <cite>applies</cite>.
   *
   * @param <T>
   * @param target Collection to be filtered.
   * @param predicate Predicate determining whether to accept current element.
   * @return A new collection with selected elements.
   */
  public static <T> List<T> filter(final List<T> target, final Predicate<T> predicate) {
    final List<T> result = new ArrayList<T>();
    for (final T element : target) {
      if (predicate.apply(element)) {
        result.add(element);
      }
    }
    return result;
  }

  /**
   * Checks whether the collection is empty, that means NULL or empty as in "no elements within".
   * 
   * @param <T>
   * @param c Collection to check.
   * @return True if the collection is NULL or contains no elements.
   */
  public static <T> boolean isEmpty(final Collection<T> c) {
    return c == null || c.isEmpty();
  }

  /**
   * Checks whether the string is empty, that means NULL or equal to "".
   *
   * @param s String to check.
   * @return True if the string is NULL or has no characters.
   */
  public static boolean isEmpty(final String s) {
    return s == null || "".equals(s);
  }

  /**
   * Decides whether the two provided tokens are "the same".
   *
   * <p>
   * Two tokens are considered to be same iff:
   *
   * <ul>
   *   <li>If they are both simple data, or</li>
   *   <li>If they are both elements of the same name (case <strong>insensitive</strong>), or</li>
   *   <li>If they are both attributes of the same name (case <strong>insensitive</strong>).</li>
   * </ul>
   * </p>
   *
   * @param t1 First regexp - token.
   * @param t2 Second regexp - token.
   * @return True, if tokens are equal in the sense described above. False otherwise.
   * @throws IllegalArgumentException When one of the regexps is not a token.
   */
  public static boolean equalTokens(final Regexp<AbstractNode> t1,
          final Regexp<AbstractNode> t2) {
    if (!t1.isToken() || !t2.isToken()) {
      throw new IllegalArgumentException();
    }
    if (t1.getContent().isSimpleData()
            && t2.getContent().isSimpleData()) {
      return true;
    }
    if (t1.getContent().isElement()
            && t2.getContent().isElement()
            && t1.getContent().getName().equalsIgnoreCase(t2.getContent().getName())) {
      return true;
    }
    if (t1.getContent().isAttribute()
            && t2.getContent().isAttribute()
            && t1.getContent().getName().equalsIgnoreCase(t2.getContent().getName())) {
      return true;
    }
    return false;
  }
}
