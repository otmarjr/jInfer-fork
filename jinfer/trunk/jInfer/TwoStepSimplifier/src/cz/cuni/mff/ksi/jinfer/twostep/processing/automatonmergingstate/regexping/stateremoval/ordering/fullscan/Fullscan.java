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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.fullscan;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.regexp.RegexpMDL;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers.UniversalCodeForIntegers;
import org.apache.log4j.Logger;

/**
 * Simple heuristic for ordering states in automaton. Weight = sum of {in | out | loop}-transitions
 * regular expressions lengths. Minimal weight = best to remove.
 * <p>
 * Each state get a number assigned to it - called weight. Weight of a state
 * is calculated as sum of length (=count of tokens) of regular expression
 * on each {in-step, loop, out-step}.
 * <p>
 * The state with minimum weight is returned as a state to be removed first.
 * 
 * @author anti
 */
public class Fullscan<T> implements Orderer<T> {

  private static final Logger LOG = Logger.getLogger(Fullscan.class);
  private RegexpEvaluator<T> rEval;

  public Fullscan() {
    this.rEval= new RegexpMDL<T>();
  }

  private double getAutomatonLength(final StateRemovalRegexpAutomaton<T> automaton) throws InterruptedException {
    double result = 0.0;
    for (State<Regexp<T>> state : automaton.getDelta().keySet()) {
      for (Step<Regexp<T>> step : automaton.getDelta().get(state)) {
        result+= rEval.evaluate(step.getAcceptSymbol());
      }
    }
    return result;
  }
  
  @Override
  public State<Regexp<T>> getStateToRemove(final StateRemovalRegexpAutomaton<T> automaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    StateRemovalRegexpAutomaton<T> newAut;
    double oldLength = getAutomatonLength(automaton);
    double minLenght = Double.MAX_VALUE;
    State<Regexp<T>> minState = null;
    for (State<Regexp<T>> state : automaton.getDelta().keySet()) {
      if (state.equals(automaton.getSuperFinalState())||state.equals(automaton.getSuperInitialState())) {
        continue;
      }
      newAut= new StateRemovalRegexpAutomaton<T>(automaton);
      newAut.removeState(state);
      double thisLength= getAutomatonLength(newAut);
      if (thisLength < minLenght) {
        minLenght= thisLength;
        minState = state;
      }
    }
    assert minState != null;
    LOG.debug("Old automaton length: " + String.valueOf(oldLength) + ", new automaton length: " + String.valueOf(minLenght));
    return minState;
  }

}
