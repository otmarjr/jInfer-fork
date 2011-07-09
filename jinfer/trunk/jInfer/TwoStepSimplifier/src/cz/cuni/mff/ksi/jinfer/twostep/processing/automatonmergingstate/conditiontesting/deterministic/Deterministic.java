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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.deterministic;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class Deterministic<T> implements MergeConditionTester<T> {

  public Deterministic() {
  }

  @Override
  public List<List<List<State<T>>>> getMergableStates(final Automaton<T> automaton) throws InterruptedException {
    final List<List<List<State<T>>>> alternatives = new ArrayList<List<List<State<T>>>>();

    List<List<State<T>>> ret = new ArrayList<List<State<T>>>();
    for (State<T> state : automaton.getDelta().keySet()) {
      Map<T, State<T>> mm = new HashMap<T, State<T>>();
      for (Step<T> step : automaton.getDelta().get(state)) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        if (!mm.containsKey(step.getAcceptSymbol())) {
          mm.put(step.getAcceptSymbol(), step.getDestination());
        } else if (!mm.get(step.getAcceptSymbol()).equals(step.getDestination())) {
          List<State<T>> mergePair = new ArrayList<State<T>>();
          mergePair.add(mm.get(step.getAcceptSymbol()));
          mergePair.add(step.getDestination());
          ret.add(mergePair);
        }
      }
    }
    if (!ret.isEmpty()) {
      alternatives.add(ret);
    }
    return alternatives;
  }
}
