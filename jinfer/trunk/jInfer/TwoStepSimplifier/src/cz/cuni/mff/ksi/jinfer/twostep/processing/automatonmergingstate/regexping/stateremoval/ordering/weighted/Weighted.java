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

package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.weighted;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;

/**
 * Most simple ordering of states to remove from RegexpAutomaton is implemented
 * here.
 *
 * Each state get a number assigned to it - called weight. Weight of a state
 * is calculated as sum of length (=count of tokens) of regular expression
 * on each {in-step, loop, out-step}
 *
 * The state with minimum weight is returned as a state to be removed first.
 * 
 * @author anti
 */
public class Weighted<T> implements Orderer<T> {
  private int getRegexpWeight(final Regexp<T> regexp) {
    return regexp.getTokens().size();
  }

  private int getStateWeight(final StateRemovalRegexpAutomaton<T> automaton, final State<Regexp<T>> state) {
    int weight = 0;
    for (Step<Regexp<T>> step : automaton.getReverseDelta().get(state)) {
      weight += this.getRegexpWeight(step.getAcceptSymbol());
    }
    for (Step<Regexp<T>> step : automaton.getDelta().get(state)) {
      if (!step.getDestination().equals(state)) {
        // To prevent twice counting the loops
        weight += this.getRegexpWeight(step.getAcceptSymbol());
      }
    }
    return weight;
  }

  @Override
  public State<Regexp<T>> getStateToRemove(final StateRemovalRegexpAutomaton<T> automaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    int minWeight= Integer.MAX_VALUE;
    State<Regexp<T>> minState= null;
    for (State<Regexp<T>> state : automaton.getDelta().keySet()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (state.equals(automaton.getSuperInitialState())||state.equals(automaton.getSuperFinalState())) {
        continue;//remove except superinitial state and superfinal state
      }
      if (minState == null) {
        minState= state;
        minWeight= this.getStateWeight(automaton, state);
      } else {
        final int stateWeight= this.getStateWeight(automaton, state);
        if ((stateWeight <= minWeight)&&
          (state.getName() < minState.getName())) {
          minWeight= stateWeight;
          minState= state;
        }
      }
    }
    return minState;
  }
}
