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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class NaiveRules<T> implements Evaluator<Automaton<T>> {
  private static final Logger LOG = Logger.getLogger(NaiveRules.class);
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
    double result2= 0.0;
    for (State<T> state : aut.getDelta().keySet()) {
      double unity = state.getFinalCount();
      for (Step<T> step : aut.getDelta().get(state)) {
        unity+= step.getUseCount();
      }
      if (state.getFinalCount() > 0) {
        result2+= ((double) state.getFinalCount()) * (-UniversalCodeForIntegers.log2(state.getFinalCount() / unity));
      }
      for (Step<T> step : aut.getDelta().get(state)) {
        result2+= ((double) step.getUseCount()) * (-UniversalCodeForIntegers.log2(step.getUseCount() / unity));
      }
    }
    double result = 0.0;
    for (List<T> inputString : inputStrings) {
      StepPath<T> path = new StepPath<T>(aut);
      for (T rs : inputString) {
        path.suffix(rs);
      }
      path.finalState();
      
      double inter = path.getMDL();
      assert inter >= 0;
      result+= inter;
    }
    LOG.error("Reeeeee" + String.valueOf(result) + "    " + String.valueOf(result2));
    return result2;
  }
}
