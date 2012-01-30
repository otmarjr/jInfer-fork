/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.iss.objects;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.MutablePair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class representing a "list" that can be traversed even as items are removed
 * from it. The iteration should be done using the {@link DeletableList#hasNext()}
 * and {@link DeletableList#next()} methods. Note that after one pass using these
 * methods the list cannot be traversed anymore.
 *
 * @author vektor
 */
public class DeletableList<T> {

  private final List<MutablePair<T, Boolean>> model;

  private int index = 0;

  /**
   * Full constructor.
   *
   * @param data List of items this object will represent.
   */
  public DeletableList(final List<T> data) {
    model = new ArrayList<MutablePair<T, Boolean>>(data.size());
    for (final T t : data) {
      if (t == null) {
        throw new IllegalArgumentException("Items of the list must not be null");
      }
      model.add(new MutablePair<T, Boolean>(t, Boolean.TRUE));
    }
  }

  /**
   * Checks whether there are more items to iterate.
   *
   * @return <code>true</code> if there are more items to iterate,
   * <code>false</code> otherwise.
   */
  public boolean hasNext() {
    int idx = index;
    while (idx < model.size()) {
      if (model.get(idx).getSecond().booleanValue()) {
        return true;
      }
      idx++;
    }
    return false;
  }

  /**
   * Returns the next item of the list.
   *
   * @return The next element in the iteration or <code>null</code> if there are
   * no left.
   */
  public T next() {
    while (index < model.size()) {
      if (model.get(index).getSecond().booleanValue()) {
        index++;
        return model.get(index - 1).getFirst();
      }
      index++;
    }
    return null;
  }

  /**
   * Removes all occurences of the specified item from the list. Uses
   * {@link Object#equals(java.lang.Object) } to check for equality.
   *
   * @param t The object to remove.
   */
  public void remove(final T t) {
    for (final MutablePair<T, Boolean> p : model) {
      if (p.getFirst().equals(t)) {
        p.setSecond(Boolean.FALSE);
      }
    }
  }

  /**
   * Removes from this object all occurences of all items in the specified
   * collection. Uses {@link Object#equals(java.lang.Object) } to check for
   * equality.
   *
   * @param c Collection containing items to be removed.
   */
  public void removeAll(final Collection<T> c) {
    if (c == null) {
      throw new IllegalArgumentException("Expecting non-null collection");
    }
    for (final MutablePair<T, Boolean> p : model) {
      if (c.contains(p.getFirst())) {
        p.setSecond(Boolean.FALSE);
      }
    }
  }

  /**
   * Returns a list of items that are still "alive" in this object, i.e. those
   * that were not removed.
   *
   * @return List of all "live" items in this list.
   */
  public List<T> getLive() {
    final List<T> ret = new ArrayList<T>();

    for (final Pair<T, Boolean> p : model) {
      if (p.getSecond().booleanValue()) {
        ret.add(p.getFirst());
      }
    }

    return ret;
  }

}
