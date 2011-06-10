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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * k,h-context equivalence criterion implementation.
 *
 * It first finds all contexts of two states, then compares each pair. Returns
 * list of alternative merge lists of pairs.
 *
 * Not using k-grams algorithm.
 * 
 * @author anti
 */
public class SKStrings<T> implements MergeConditionTester<T> {

  private int k;
  private int s;
  private String strategy;

  /**
   * Setting k,h. It has to be k >= h, or exception thrown (cannot merge more than
   * k states on path, when only k of them were examined).
   * @param k
   * @param h
   */
  public SKStrings(final int k, final int s, final String strategy) {
    this.k = k;
    this.s = s;
    this.strategy= strategy;
  }

  private SKBucket<T> findSKStrings(final int _k, final State<T> state, final Map<State<T>, Set<Step<T>>> delta) {
    final SKBucket<T> result = new SKBucket<T>();
    int sum = 0;
    for (Step<T> step : delta.get(state)) {
      sum+= step.getUseCount();
    }

    if (k > 1) {
      for (Step<T> step : delta.get(state)) {
        SKBucket<T> fromHim = findSKStrings(_k - 1, step.getDestination(), delta);
        fromHim.preceede(step, (double) step.getUseCount() / (double) sum);
        result.addAll(fromHim);
      }
      return result;
    } else if (k == 1) {
      for (Step<T> step : delta.get(state)) {
        result.add(step, (double) step.getUseCount() / (double) sum);
      }
      return result;
    }
    throw new IllegalStateException("This is impossible.");
  }

  @Override
  public List<List<List<State<T>>>> getMergableStates(final State<T> state1, final State<T> state2, final Automaton<T> automaton) {
    final Map<State<T>, Set<Step<T>>> delta = automaton.getDelta();
    final List<List<List<State<T>>>> alternatives = new ArrayList<List<List<State<T>>>>();

    final SKBucket<T> state1strings = this.findSKStrings(k, state1, delta);
    final SKBucket<T> state2strings = this.findSKStrings(k, state2, delta);
    if ("AND".equals(this.strategy)) {
      if (state1strings.getMostProbable(this.s).areSubset(state2strings) && state2strings.getMostProbable(this.s).areSubset(state2strings)) {
        List<State<T>> mergePair = new ArrayList<State<T>>();
        mergePair.add(state1);
        mergePair.add(state2);
        List<List<State<T>>> ret= new ArrayList<List<State<T>>>();
        ret.add(mergePair);
        alternatives.add(ret);
      }
    } else if ("OR".equals(this.strategy)) {
      if (state1strings.getMostProbable(this.s).areSubset(state2strings) || state2strings.getMostProbable(this.s).areSubset(state2strings)) {
        List<State<T>> mergePair = new ArrayList<State<T>>();
        mergePair.add(state1);
        mergePair.add(state2);
        List<List<State<T>>> ret= new ArrayList<List<State<T>>>();
        ret.add(mergePair);
        alternatives.add(ret);
      }
    }
    return alternatives;
  }
}

