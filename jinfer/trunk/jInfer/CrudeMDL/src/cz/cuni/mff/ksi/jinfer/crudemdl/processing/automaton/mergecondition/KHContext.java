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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.mergecondition;

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.State;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.Step;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class KHContext<T> {
  private List<Step<T>> steps;
  private Integer k;
  private Integer h;

  public KHContext(final Integer k, final Integer h) {
    this.steps= new ArrayList<Step<T>>();
    this.k= k;
    this.h= h;
  }

  public void addStepLast(final Step<T> step) {
    this.steps.add(step);
  }

  public List<Pair<State<T>, State<T>>> getMergeableStates(final KHContext<T> another) {
    if (another.getK() != this.k) {
      throw new IllegalArgumentException();
    }
    if (another.getH() != this.h) {
      throw new IllegalArgumentException();
    }

    final List<Pair<State<T>, State<T>>> result= new ArrayList<Pair<State<T>, State<T>>>(h);
    final List<Step<T>> anotherSteps= another.getSteps();
    if (anotherSteps.size() != this.steps.size()) {
      throw new IllegalArgumentException();
    }

    int i= 0;
    boolean equivalent= true;
    while ((equivalent)&&(i < k)) {
      final Step<T> myStep= this.steps.get(i);
      final Step<T> anotherStep= anotherSteps.get(i);
      equivalent= myStep.getAcceptSymbol().equals(anotherStep.getAcceptSymbol());
      if ((equivalent)&&(i >= k - h)) {
        if (!myStep.getSource().equals(anotherStep.getSource())) {
          result.add(
                  new Pair<State<T>, State<T>>(
                    myStep.getSource(), anotherStep.getSource()
                  )
                );
        }
      }
      if ((equivalent)&&(i == k - 1)) {
        if (!myStep.getDestination().equals(anotherStep.getDestination())) {
          result.add(
                  new Pair<State<T>, State<T>>(
                    myStep.getDestination(), anotherStep.getDestination()
                  )
                );
        }
      }
      i++;
    }
    if (equivalent) {
      return result;
    } else {
      return Collections.<Pair<State<T>, State<T>>>emptyList();
    }
  }

  /**
   * @return the steps
   */
  public List<Step<T>> getSteps() {
    return Collections.unmodifiableList(this.steps);
  }

  /**
   * @return the k
   */
  public Integer getK() {
    return this.k;
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
    return this.h;
  }

  /**
   * @param h the h to set
   */
  public void setH(final Integer h) {
    this.h = h;
  }

  @Override
  public String toString() {
    StringBuilder sb= new StringBuilder("k,h-context: k=" + this.k.toString() + " h=" + this.h.toString() + ".\nSteps:");
    for (Step<T> step : this.steps) {
      sb.append(step);
    }
    return sb.toString();
  }
}
