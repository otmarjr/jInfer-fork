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
package cz.cuni.mff.ksi.jinfer.attrstats.objects;

import cz.cuni.mff.ksi.jinfer.base.objects.MutablePair;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class DeletableList<T> {

  private final List<MutablePair<T, Boolean>> model;

  private int index = 0;

  /**
   * TODO vektor Comment!
   *
   * @param data
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
   * TODO vektor Comment!
   *
   * @return
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
   * TODO vektor Comment!
   *
   * @return
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
   * TODO vektor Comment!
   *
   * @param t
   */
  public void remove(final T t) {
    for (final MutablePair<T, Boolean> p : model) {
      if (p.getFirst().equals(t)) {
        p.setSecond(Boolean.FALSE);
      }
    }
  }

  /**
   * TODO vektor Comment!
   *
   * @param c
   */
  public void removeAll(final Collection<T> c) {
    for (final MutablePair<T, Boolean> p : model) {
      if (c.contains(p.getFirst())) {
        p.setSecond(Boolean.FALSE);
      }
    }
  }

  /**
   * TODO vektor Comment!
   * 
   * @return
   */
  public List<T> getLive() {
    final List<T> ret = new ArrayList<T>();

    for (final MutablePair<T, Boolean> p : model) {
      if (p.getSecond().booleanValue()) {
        ret.add(p.getFirst());
      }
    }

    return ret;
  }

}
