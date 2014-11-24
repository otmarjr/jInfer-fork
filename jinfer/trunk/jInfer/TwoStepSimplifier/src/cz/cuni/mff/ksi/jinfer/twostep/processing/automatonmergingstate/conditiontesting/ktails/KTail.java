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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.ktails;


import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class KTail<T> {

  private Deque<Step<T>> str;
  private double probability = 1.0;

  public KTail() {
    this.str = new LinkedList<Step<T>>();
    this.probability = 1.0;
  }

  public KTail(Step<T> firstStep, double probability) {
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
  
  public boolean equalsToSequence(T... sequence){
      if (sequence == null)
          return false;
      
      if (sequence.length != this.str.size())
          return false;
      
      List<T> seq = Arrays.asList(sequence);
      List<T> steps = str.stream().map(s -> s.getAcceptSymbol()).collect(Collectors.toList());
      
      return seq.equals(steps);
  }
  
  public int size() {
      return this.str.size();
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
    final KTail<T> other = (KTail<T>) obj;
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
        hash = 97 * hash + t.getAcceptSymbol().hashCode();
      }
    }
    return hash;
  }

  @Override
  public String toString() {
    return "K-Tail{" + "str=" + str + ", probability=" + probability + '}';
  }
}
