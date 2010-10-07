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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.kstring;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Using class KHString, we build 2,x-context condition merger.
 *
 * Should be generalized to k,h parameters, but currently only 2,x works
 * (because of recursive character of finding contexts of state)
 *
 * It first finds all contexts of two states, then compares each pair. Returns
 * list of alternative merge lists of pairs.
 *
 * @author anti
 */
public class MergeConditionTesterKHString<T> implements MergeConditionTester<T> {
  private int k;
  private int h;

  /**
   * Setting k,h. It has to be k >= h, or exception thrown (cannot merge more than
   * k states on path, when only k of them were examined).
   * @param k
   * @param h
   */
  public MergeConditionTesterKHString(int k, int h) {
    if (h > k) {
      throw  new IllegalArgumentException("K must be greater than h");
    }
    this.k= k;
    this.h= h;
  }

  public MergeConditionTesterKHString() {
    this(2, 1);
  }

  public void setKH(int k, int h) {
    if (h > k) {
      throw  new IllegalArgumentException("K must be greater than h");
    }
    this.k= k;
    this.h= h;
  }
  
  private List<KHString<T>> findKHStrings(final State<T> state, final Map<State<T>, Set<Step<T>>> delta, final Map<State<T>, Set<Step<T>>> reverseDelta) {
    final List<KHString<T>> result= new LinkedList<KHString<T>>();

    for (Step<T> outStep : delta.get(state)) {
      for (Step<T> secondOutStep : delta.get(outStep.getDestination())) {
        final KHString<T> string= new KHString<T>(this.k, this.h);
        string.addStepLast(outStep);
        string.addStepLast(secondOutStep);
        result.add(string);
      }
    }
    return result;
  }

  @Override
  public List<List<Pair<State<T>, State<T>>>> getMergableStates(final State<T> mainState, final State<T> mergedState, final Automaton<T> automaton) {
    final Map<State<T>, Set<Step<T>>> delta= automaton.getDelta();
    final Map<State<T>, Set<Step<T>>> reverseDelta= automaton.getReverseDelta();

    if (!delta.containsKey(mainState)) {
      throw new IllegalArgumentException("Delta function does not contain state to check.");
    }
    if (!reverseDelta.containsKey(mainState)) {
      throw new IllegalArgumentException("reverseDelta function does not contain state to check.");
    }
    if (!delta.containsKey(mergedState)) {
      throw new IllegalArgumentException("Delta function does not contain state to check.");
    }
    if (!reverseDelta.containsKey(mergedState)) {
      throw new IllegalArgumentException("reverseDelta function does not contain state to check.");
    }

    final List<KHString<T>> mainKHStrings= this.findKHStrings(mainState, delta, reverseDelta);
    final List<KHString<T>> mergedKHStrings= this.findKHStrings(mergedState, delta, reverseDelta);

    final List<List<Pair<State<T>, State<T>>>> result= new LinkedList<List<Pair<State<T>, State<T>>>>();
    for (KHString<T> mainKHString : mainKHStrings) {
      for (KHString<T> mergedKHString : mergedKHStrings) {
        final List<Pair<State<T>, State<T>>> temp= mainKHString.getMergeableStates(mergedKHString);
        if (!temp.isEmpty()) {
          result.add(temp);
        }
      }
    }
    return result;
  }
}
