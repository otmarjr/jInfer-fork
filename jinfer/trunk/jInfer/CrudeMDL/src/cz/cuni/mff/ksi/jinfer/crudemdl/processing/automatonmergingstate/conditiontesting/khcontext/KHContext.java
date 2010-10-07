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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.khcontext;

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayList;
import java.util.Collections;


import java.util.List;

/**
 * Class representing KHContext of state. KHcontext is path of states ending in
 * desired state. Path has length k - that is number of steps on path.
 *
 * For example 3,x-context:
 * A -> B -> C -> D
 * ending in D, only steps are held by this class (steps have source and destination
 * either so no information is lost).
 *
 * Two context are equivalent, if all k corresponding pairs of steps have same
 * accept symbol.
 * Equivalent contexts:
 * A -on x-> B -on y-> C
 * D -on x-> D -on y-> G
 *
 * When two contexts are equivalent, ending states (C, G), are said to be merged.
 * Second number, h, is the number of states before the ending states, that are
 * said to be merged if context are equivalent.
 *
 * So, lets have instances of this class with 4,1-context, k=4, h=1.
 * Only contexts with 4 steps equals acceptSymbol are equivalent, and only on them
 * the last two states are said to be merged, and one more pair before then. See:
 * Z -d-> A -x-> B -y-> C -z-> D -f-> E
 * R -o-> F -x-> L -y-> C -z-> R -f-> I
 *
 * States I,E are to be merged, and, as h=1, also one preceeding pair: D,R.
 *
 * @author anti
 */
public class KHContext<T> {
  /**
   * List of steps held by this context
   */
  private final List<Step<T>> steps;
  private Integer k;
  private Integer h;

  /**
   * Constructor does not set steps, they are added separately.
   * @param k
   * @param h
   */
  public KHContext(final Integer k, final Integer h) {
    this.steps= new ArrayList<Step<T>>();
    this.k= k;
    this.h= h;
  }

  /**
   * Use addStepLast to add steps. Add them from left to right. Starting
   * with step farthest on left from inspected state.
   *
   * @param step
   */
  public void addStepLast(final Step<T> step) {
    this.steps.add(step);
  }

  /**
   * Whole magic procedure. Compares this context to another, and if equivalent
   * return list of pairs of states to merge.
   * 
   * @param another
   * @return
   */
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
    final StringBuilder sb= new StringBuilder("k,h-context: k=" + this.k.toString() + " h=" + this.h.toString() + ".\nSteps:");
    for (Step<T> step : this.steps) {
      sb.append(step);
    }
    return sb.toString();
  }
}
