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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class SKString<T> {

  private Deque<Step<T>> str;
  private double probability = 1.0;

  public SKString() {
    this.str = new LinkedList<Step<T>>();
    this.probability = 1.0;
  }

  public SKString(Step<T> firstStep, double probability) {
    this();
    this.preceede(firstStep, probability);
  }

  public final void preceede(Step<T> step, double probability) {
    this.str.addFirst(step);
    this.probability *= probability;
  }

  public double getProbability() {
    return probability;
  }

  public Deque<Step<T>> getStr() {
    return str;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    @SuppressWarnings("unchecked")
    final SKString<T> other = (SKString<T>) obj;
    Deque<Step<T>> otherStr = other.getStr();
    if (this.str.size() != otherStr.size()) {
      return false;
    }
    Iterator<Step<T>> myIt = this.str.iterator();
    Iterator<Step<T>> otherIt = other.getStr().iterator();
    while (myIt.hasNext() && otherIt.hasNext()) {
      if (!myIt.next().getAcceptSymbol().equals(otherIt.next().getAcceptSymbol())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    if (this.str != null) {
      for (Step<T> t : this.str) {
        hash = 41 * hash + t.getAcceptSymbol().hashCode();
      }
    }
    return hash;
  }

  @Override
  public String toString() {
    return "SKString{" + "str=" + str + ", probability=" + probability + '}';
  }
}
