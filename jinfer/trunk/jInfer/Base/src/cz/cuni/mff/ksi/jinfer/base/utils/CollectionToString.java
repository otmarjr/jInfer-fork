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

import java.util.Collection;

/**
 * Helper class for collection presentation.
 *
 * @author vektor
 */
public class CollectionToString {

  /**
   * Library class.
   */
  private CollectionToString() {
  }

  /*
   * Interface defining a strategy how to convert an object to a string.
   */
  public interface ToString<T> {

    /**
     * Converts the parameter to a string.
     * @param t Parameter to be converted to a string.
     * @return String representation of the parameter.
     */
    String toString(T t);
  }
  /**
   * A trivial toString strategy: simply returns the input String.
   */
  public static final ToString<String> IDEMPOTENT = new ToString<String>() {

    @Override
    public String toString(final String t) {
      return t;
    }
  };

  /**
   * Converts a collection to a string looking like this: (a,b,c,d).
   *
   * Separator and strategy of turning collection elements to string is configurable.
   *
   * @param <T> Type on which we work.
   * @param collection Collection to be turned into a string.
   * @param separator A character that will separate the elements.
   * @param toStringStrategy Strategy how to convert an element of the
   *  collection to a string.
   * @return String representing the collection.
   */
  public static <T> String colToString(final Collection<T> collection,
          final String separator, final ToString<T> toStringStrategy) {
    return colToString(collection, separator, toStringStrategy, "(", ")");
  }

  /**
   * Converts a collection to a string looking like this: (a,b,c,d).
   *
   * Separator and strategy of turning collection elements to string is configurable.
   *
   * @param <T> Type on which we work.
   * @param collection Collection to be turned into a string.
   * @param separator A character that will separate the elements.
   * @param toStringStrategy Strategy how to convert an element of the
   *  collection to a string.
   * @param braceOpen How to opening brace should look like (empty string is OK).
   * @param braceClose How to closing brace should look like (empty string is OK).
   * @return String representing the collection.
   */
  public static <T> String colToString(final Collection<T> collection,
          final String separator, final ToString<T> toStringStrategy,
          final String braceOpen, final String braceClose) {
    final StringBuilder ret = new StringBuilder();
    ret.append(braceOpen);
    boolean first = true;
    for (final T child : collection) {
      if (!first) {
        ret.append(separator);
      }
      first = false;
      ret.append(toStringStrategy.toString(child));
    }
    ret.append(braceClose);
    return ret.toString();
  }
}
