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
import java.util.Set;

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
    return result;
  }
}
