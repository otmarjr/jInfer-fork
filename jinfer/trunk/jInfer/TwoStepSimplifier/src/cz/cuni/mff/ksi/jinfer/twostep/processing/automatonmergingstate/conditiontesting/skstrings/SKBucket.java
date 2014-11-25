/*
 * Copyright (C) 2011 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings;

import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class SKBucket<T> {

  private List<SKString<T>> skstrings;

  public SKBucket() {
    this.skstrings = new LinkedList<SKString<T>>();
  }

  public void preceede(Step<T> step, double probability) {
    for (SKString<T> str : this.skstrings) {
      str.preceede(step, probability);
    }
  }

  public void add(Step<T> step, double probability) {
    this.skstrings.add(new SKString<T>(step, probability));
  }

  public void add(SKString<T> str) {
    this.skstrings.add(str);
  }

  public void addAll(SKBucket<T> anotherBucket) {
    this.skstrings.addAll(anotherBucket.getSKStrings());
  }

  public List<SKString<T>> getSKStrings() {
    return Collections.unmodifiableList(this.skstrings);
  }

  public SKBucket<T> getMostProbable(final double s) {
    double intersum = 0.0;
    SKBucket<T> result = new SKBucket<T>();
    Collections.sort(this.skstrings, new Comparator<SKString<T>>() {

      @Override
      public int compare(SKString<T> o1, SKString<T> o2) {
        if (o1.getProbability() > o2.getProbability()) {
          return -1;
        } else if (o1.getProbability() < o2.getProbability()) {
          return 1;
        }
        return 0;
      }
    });
    Iterator<SKString<T>> it = this.skstrings.iterator();
    while ((intersum < s) && (it.hasNext())) {
      SKString<T> t = it.next();
      result.add(t);
      intersum += t.getProbability();
    }
    return result;
  }

  public boolean areSubset(SKBucket<T> anotherBucket) {
    if (this.skstrings.isEmpty() && !anotherBucket.getSKStrings().isEmpty()) {
      return false;
    }
    return (anotherBucket.getSKStrings().containsAll(this.skstrings));
  }

  @Override
  public String toString() {
    return "SKBucket{" + "skstrings=" + skstrings + '}';
  }
}
