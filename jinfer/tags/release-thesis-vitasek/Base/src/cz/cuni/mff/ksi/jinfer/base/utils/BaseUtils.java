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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
   * (described by this object) applies to an argument or not.
   */
  public interface Predicate<T> {

    /**
     * This method represents the fact whether the predicate representing
     * this object applies to a specific argument.
     *
     * @param argument Argument to decide whether this predicate applies.
     * @return True if the predicate represented by this object applies to the
     * specified argument. False otherwise.
     */
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
    if (target == null || predicate == null) {
      throw new IllegalArgumentException("Target list and the predicate must not be null.");
    }
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
   * Creates a list containing the specified list N times in a row.
   *
   * @param l List to be cloned. Must not be <code>null</code>.
   * @param n How many times should the list be cloned. Must be non-negative.
   * @return Flat list containing the input list N times in a row. If the input
   * list is empty, or N = 0, the result is an empty list.
   */
  public static <T> List<T> cloneList(final List<T> l, final int n) {
    if (l == null) {
      throw new IllegalArgumentException("The list to be cloned must not be null.");
    }
    if (n < 0) {
      throw new IllegalArgumentException("When cloning a list, N must be non-negative (" + n + ")");
    }
    if (n == 0 || isEmpty(l)) {
      return Collections.emptyList();
    }

    final List<T> ret = new ArrayList<T>();
    for (int i = 0; i < n; i++) {
      ret.addAll(l);
    }
    return ret;
  }

  /**
   * Comparator for {@link AbstractNamedNode}s, based on their
   * {@link AbstractNamedNode#name}.
   * Potential <code>null</code> values will be sorted last.
   */
  public static final Comparator<AbstractNamedNode> NAMED_NODE_COMPARATOR =
          new Comparator<AbstractNamedNode>() {

    @Override
    public int compare(final AbstractNamedNode o1, final AbstractNamedNode o2) {
      if (o1 == null && o2 == null) {
        return 0;
      }
      if (o1 == null) {
        return 1;
      }
      if (o2 == null) {
        return -1;
      }
      return o1.getName().compareTo(o2.getName());
    }
  };

  /**
   * Calculates and returns the intersection of the two specified sets.
   *
   * @param set1 First set to interset, must not be <code>null</code>.
   * @param set2 Second set to interset, must not be <code>null</code>.
   * @return A new set containing the intersection of these two sets. Might be
   * empty, if the two parameters have no intersection.
   */
  public static <T> Set<T> intersect(final Set<T> set1, final Set<T> set2) {
    if (set1 == null || set2 == null) {
      throw new IllegalArgumentException("Sets to crosssect must not be null.");
    }
    final Set<T> ret = new HashSet<T>(set1);
    ret.retainAll(set2);
    return ret;
  }

  private static final Random RND = new Random();

  /**
   * Returns a random subset of the specified collection, so that its size is a
   * specified ratio of the original collection size.
   *
   * @param <T> Type parameter.
   * @param c Collection to pick from.
   * @param ratio Requested ratio.
   * @return List of randomly picked elements of the collection.
   */
  public static <T> List<T> rndSubset(final Collection<T> c, final double ratio) {
    return rndSubset(c, (int)Math.round(c.size() * ratio));
  }

  /**
   * Returns a random subset of the specified collection of requested size.
   *
   * @param <T> Type parameter.
   * @param c Collection to pick from.
   * @param count Requested size of the subset.
   * @return List of randomly picked elements of the collection.
   */
  public static <T> List<T> rndSubset(final Collection<T> c, final int count) {
    if (c == null) {
      throw new IllegalArgumentException("Expecting non-null parameter");
    }
    if (count < 0) {
      throw new IllegalArgumentException("Size of the subset cannot be negative: " + count);
    }
    if (count > c.size()) {
      throw new IllegalArgumentException("Subset cannot be bigger than the original set: " + count);
    }

    final List<T> ret = new ArrayList<T>(count);
    final List<T> tmp = new ArrayList<T>(c);

    for (int i = 0; i < count; i++) {
      final int toRemove = RND.nextInt(tmp.size());
      ret.add(tmp.get(toRemove));
      tmp.remove(toRemove);
    }

    return ret;
  }

  /**
   * Checks whether the first collection provided is a subset of the second
   * collection provided.
   *
   * @param <T> Type of the collections.
   * @param l1 First collection.
   * @param l2 Second collection.
   *
   * @return <code>true</code> if the first collection is a subset of the second
   *   (every item of the first is contained in the second), <code>false</code>
   *   otherwise.
   */
  public static <T> boolean isSubset(final Collection<T> l1, final Collection<T> l2) {
    return l2.containsAll(l1);
  }

  /**
   * Checks whether two objects are equal in the following sense.
   * <ul>
   *   <li>If both are <code>null</code>, they are <strong>equal</strong>.</li>
   *   <li>If one of them is <code>null</code> and the other is not, they are
   *       <strong>not equal</strong>.</li>
   *   <li>If both are not <code>null</code>, their equality is decided by the
   *       call to their usual {@link Object#equals(Object)}.</li>
   * </ul>
   * @param <X> Type of both objects to be checked.
   * @param arg1 First object to be checked.
   * @param arg2 Second object to be checked.
   * @return <code>True</code> if the specified objects are equal in the sense
   * described above, <code>false</code> otherwise.
   */
  public static <X> boolean equal(final X arg1, final X arg2) {
    if (arg1 == null && arg2 == null) {
      return true;
    }
    if (arg1 == null) {
      return false;
    }
    return arg1.equals(arg2);
  }
}
