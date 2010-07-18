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
}
