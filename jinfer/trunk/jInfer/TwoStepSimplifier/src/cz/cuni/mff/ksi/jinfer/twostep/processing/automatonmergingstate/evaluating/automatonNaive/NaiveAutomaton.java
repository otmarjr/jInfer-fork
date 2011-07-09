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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.automatonNaive;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.Evaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers.UniversalCodeForIntegers;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class NaiveAutomaton<T> implements Evaluator<Automaton<T>> {

  @Override
  public double evaluate(Automaton<T> x) throws InterruptedException {
    UniversalCodeForIntegers uic = UniversalCodeForIntegers.getSingleton();
    int states_count = x.getDelta().keySet().size();
    double result = uic.evaluate(states_count);
    int steps_count = 0;
    for (State<T> state : x.getDelta().keySet()) {
      result += uic.evaluate(state.getFinalCount());
      result += uic.evaluate(x.getDelta().get(state).size());
      for (Step<T> step : x.getDelta().get(state)) {
        steps_count += 1;
        result += uic.evaluate(step.getUseCount());
      }
    }
    result += uic.evaluate(states_count) * steps_count;
    return result;
  }
}
