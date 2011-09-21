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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.automatonNaiveDefective;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.Evaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers.UniversalCodeForIntegers;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.DefectiveAutomaton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class NaiveDefectiveAutomaton<T> implements Evaluator<DefectiveAutomaton<T>> {

  @Override
  public double evaluate(DefectiveAutomaton<T> x) throws InterruptedException {
    UniversalCodeForIntegers uic = UniversalCodeForIntegers.getSingleton();
    int states_count = x.getDelta().keySet().size();
    Map<T, Integer> alphabet = new HashMap<T, Integer>();
    double result = uic.evaluate(states_count);
    int steps_count = 0;
    for (State<T> state : x.getDelta().keySet()) {
      result += uic.evaluate(state.getFinalCount());
      result += uic.evaluate(x.getDelta().get(state).size());
      for (Step<T> step : x.getDelta().get(state)) {
        if (alphabet.containsKey(step.getAcceptSymbol())) {
          alphabet.put(step.getAcceptSymbol(), 1 + alphabet.get(step.getAcceptSymbol()));
        } else {
          alphabet.put(step.getAcceptSymbol(), 1);
        }
        steps_count += 1;
        result += uic.evaluate(step.getUseCount());
      }
    }
    result += uic.evaluate(states_count) * steps_count;

    for (List<T> string : x.getRemovedInputStrings()) {
      for (T symbol : string) {
        if (alphabet.containsKey(symbol)) {
          alphabet.put(symbol, 1 + alphabet.get(symbol));
        } else {
          alphabet.put(symbol, 1);
        }
      }
    }
    double unity = 0;
    for (T symbol : alphabet.keySet()) {
      result += UniversalCodeForIntegers.log2(1 + alphabet.size()) + uic.evaluate(alphabet.get(symbol));
      unity += alphabet.get(symbol);
    }
    for (T symbol : alphabet.keySet()) {
      result += alphabet.get(symbol) * -UniversalCodeForIntegers.log2(alphabet.get(symbol) / unity);
    }
    for (List<T> string : x.getRemovedInputStrings()) {
      result += uic.evaluate(string.size());
      for (T symbol : string) {
        result += -UniversalCodeForIntegers.log2(alphabet.get(symbol) / unity);
      }
    }
    return result;
  }
}
