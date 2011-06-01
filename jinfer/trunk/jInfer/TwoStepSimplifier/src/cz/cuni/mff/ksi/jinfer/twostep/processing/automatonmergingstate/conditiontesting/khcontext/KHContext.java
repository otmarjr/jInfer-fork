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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.khcontext;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Collections;
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
public class KHContext<T> implements MergeConditionTester<T> {

  private int k;
  private int h;

  private void checkConstraits() {
    if (!((h >= 0) && (k >= h))) {
      throw new IllegalArgumentException("Parameter k must be greater than h, be grater than zero. k >= h => 0.");
    }
  }

  /**
   * Setting k,h. It has to be k >= h, or exception thrown (cannot merge more than
   * k states on path, when only k of them were examined).
   * @param k
   * @param h
   */
  public KHContext(final int k, final int h) {
    this.k = k;
    this.h = h;
    checkConstraits();
  }

  /**
   * Set parameters.
   *
   * @param k
   * @param h
   */
  public void setKH(final int k, final int h) {
    this.k = k;
    this.h = h;
    checkConstraits();
  }

  private List<List<Step<T>>> findKHContexts(final int _k,
          final Step<T> step, final Map<State<T>, Set<Step<T>>> reverseDelta) {
    final List<List<Step<T>>> result = new LinkedList<List<Step<T>>>();
    if (_k == 1) {
      final List<Step<T>> d = new LinkedList<Step<T>>();
      d.add(step);
      result.add(d);
      return result;
    }
    for (Step<T> inStep : reverseDelta.get(step.getSource())) {
      final List<List<Step<T>>> indu = findKHContexts(_k - 1, inStep, reverseDelta);
      for (List<Step<T>> context : indu) {
        context.add(step);
      }
      result.addAll(indu);
    }
    return result;
  }

  private List<List<Step<T>>> findKHContexts(final int _k, final State<T> state, final Map<State<T>, Set<Step<T>>> reverseDelta) {
    final List<List<Step<T>>> result = new ArrayList<List<Step<T>>>();
    for (Step<T> step : reverseDelta.get(state)) {
      final List<List<Step<T>>> indu = findKHContexts(_k, step, reverseDelta);
      result.addAll(indu);
    }
    return BaseUtils.<List<Step<T>>>filter(result, new BaseUtils.Predicate<List<Step<T>>>() {

      @Override
      public boolean apply(final List<Step<T>> argument) {
        return argument.size() == _k;
      }
    });
  }

  @Override
  public List<List<List<State<T>>>> getMergableStates(final State<T> state1, final State<T> state2, final Automaton<T> automaton) {
    checkConstraits();
    final Map<State<T>, Set<Step<T>>> reverseDelta = automaton.getReverseDelta();
    final List<List<List<State<T>>>> alternatives = new ArrayList<List<List<State<T>>>>();

    final List<List<Step<T>>> state1KHContexts = this.findKHContexts(k, state1, reverseDelta);
    final List<List<Step<T>>> state2KHContexts = this.findKHContexts(k, state2, reverseDelta);
    for (List<Step<T>> context1 : state1KHContexts) {
      for (List<Step<T>> context2 : state2KHContexts) {
        alternatives.addAll(getAlternatives(context1, context2));
      }
    }
    return alternatives;
  }

  private List<List<List<State<T>>>> getAlternatives(final List<Step<T>> context1, final List<Step<T>> context2) {
    if ((context1.size() != k) || (context2.size() != k)) {
      return Collections.<List<List<State<T>>>>emptyList();
    }
    final List<List<State<T>>> result = new ArrayList<List<State<T>>>();

    boolean totalSame = true;
    for (int i = 0; i < k; i++) {
      if (!context1.get(i).getAcceptSymbol().equals(context2.get(i).getAcceptSymbol())) {
        return Collections.<List<List<State<T>>>>emptyList();
      }
      if ((i >= h) && (!context1.get(i).getSource().equals(context2.get(i).getSource()))) {
        totalSame = false;
      }
      if (i >= h) {
        final List<State<T>> mergePair = new ArrayList<State<T>>();
        mergePair.add(context1.get(i).getSource());
        mergePair.add(context2.get(i).getSource());
        result.add(mergePair);
      }
    }
    final List<State<T>> mergePair = new ArrayList<State<T>>();
    if (!context1.get(k - 1).getDestination().equals(context2.get(k - 1).getDestination())) {
      totalSame = false;
    }
    mergePair.add(context1.get(k - 1).getDestination());
    mergePair.add(context2.get(k - 1).getDestination());
    result.add(mergePair);
    if (totalSame) {
      return Collections.<List<List<State<T>>>>emptyList();
    }
    final List<List<List<State<T>>>> r = new ArrayList<List<List<State<T>>>>();
    r.add(result);
    return r;
  }
}
