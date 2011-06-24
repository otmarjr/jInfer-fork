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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class NaiveRules<T> implements Evaluator<Automaton<T>> {

  private List<List<T>> inputStrings;
  
  public NaiveRules() {
  }

  public NaiveRules(List<List<T>> inputStrings) {
    this.inputStrings= inputStrings;
  }

  public void setInputStrings(List<List<T>> inputStrings) {
    this.inputStrings = inputStrings;
  }
  
  @Override
  public double evaluate(Automaton<T> aut) throws InterruptedException {
    Map<State<T>, Double> units = new HashMap<State<T>, Double>();

    for (State<T> state : aut.getDelta().keySet()) {
      double unity = 0;
      for (Step<T> step : aut.getDelta().get(state)) {
        unity += step.getUseCount();
      }
      unity+= state.getFinalCount();
      units.put(state, unity);
    }
    double result = 0;
    for (List<T> inputString : inputStrings) {
      State<T> actual= aut.getInitialState();
      for (T rs : inputString) {
        Step<T> outStep = aut.getOutStepOnSymbol(actual, rs);
      //  assert outStep != null;
        if (outStep == null) {
          break;
        }
        result+= -UniversalCodeForIntegers.log2(outStep.getUseCount() / units.get(actual));
        actual= outStep.getDestination();
      }
      //assert actual.getFinalCount() > 0;
      result+= -UniversalCodeForIntegers.log2(actual.getFinalCount() / units.get(actual));
    }
    return result;
  }
}
