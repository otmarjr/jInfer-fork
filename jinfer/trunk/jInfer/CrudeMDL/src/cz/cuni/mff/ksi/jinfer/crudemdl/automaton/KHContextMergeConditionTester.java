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

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO anti Comment!
 * @author anti
 */
public class KHContextMergeConditionTester<T> implements MergeCondidionTester<T> {
  private int k;
  private int h;
  public KHContextMergeConditionTester(int k, int h) {
    if (h > k) {
      throw  new IllegalArgumentException("K must be greater than h");
    }
    this.k= k;
    this.h= h;
  }

  @Override
  public List<Pair<State<T>, State<T>>> getMergableStates(State<T> mainState, State<T> mergedState, Automaton<T> automaton) {
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

    Set<Step<T>> mainInSteps= reverseDelta.get(mainState);
    Set<Step<T>> mergedInSteps= reverseDelta.get(mergedState);
    for (Step<T> mainInStep : mainInSteps) {
      for (Step<T> mergedInStep : mergedInSteps) {
        if (mainInStep.getAcceptSymbol().equals(mergedInStep.getAcceptSymbol())) {

        }
      }
    }
  }
}
