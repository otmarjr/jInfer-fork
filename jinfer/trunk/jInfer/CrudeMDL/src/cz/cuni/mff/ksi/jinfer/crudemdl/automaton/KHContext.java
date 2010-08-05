/*
 *  Copyright (C) 2010 anti
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

package cz.cuni.mff.ksi.jinfer.crudemdl.automaton;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class KHContext<T> {
  private Deque<State<T>> states;
  private Deque<Step<T>> steps;
  private Integer k;
  private Integer h;

  public KHContext(final Integer k, final Integer h) {
    this.states= new LinkedList<State<T>>();
    this.steps= new LinkedList<Step<T>>();
    this.k= k;
    this.h= h;
  }

  public void addStepLast(final Step<T> step) {
    this.getSteps().addLast(step);
  }

  public void addStateLast(final State<T> state) {
    this.getStates().addLast(state);
  }

  public boolean isEquivalent(final KHContext<T> another) {
    if (another.getK() != this.k) {
      return false;
    }

    if (another.getH() != this.h) {
      return false;
    }

    final Deque<Step<T>> anotherSteps= another.getSteps();

    assert (another.getStates().size() == this.states.size());
    assert (anotherSteps.size() == this.steps.size());

    if (this.steps.size() < this.k) {
      return false;
    }
    if (anotherSteps.size() < this.k) {
      return false;
    }

    final Iterator<Step<T>> anotherStepsIterator= anotherSteps.iterator();
    final Iterator<Step<T>> myStepsIterator= this.steps.iterator();
    boolean equivalent= true;
    while ((equivalent)&&(anotherStepsIterator.hasNext())) {
      final Step<T> myStep= myStepsIterator.next();
      final Step<T> anotherStep= anotherStepsIterator.next();
      if (!myStep.getAcceptSymbol().equals(
              anotherStep.getAcceptSymbol()
              )) {
        equivalent= false;
      }
    }
// TODO 2-context only
    if (another.getStates().getLast().equals(this.getStates().getLast())) {
      equivalent= false;
    }
    return equivalent;
  }



  /**
   * @return the states
   */
  public Deque<State<T>> getStates() {
    return states;
  }

  /**
   * @param states the states to set
   */
  public void setStates(final Deque<State<T>> states) {
    this.states = states;
  }

  /**
   * @return the steps
   */
  public Deque<Step<T>> getSteps() {
    return steps;
  }

  /**
   * @param steps the steps to set
   */
  public void setSteps(final Deque<Step<T>> steps) {
    this.steps = steps;
  }

  /**
   * @return the k
   */
  public Integer getK() {
    return k;
  }

  /**
   * @param k the k to set
   */
  public void setK(final Integer k) {
    this.k = k;
  }

  /**
   * @return the h
   */
  public Integer getH() {
    return h;
  }

  /**
   * @param h the h to set
   */
  public void setH(final Integer h) {
    this.h = h;
  }
}
