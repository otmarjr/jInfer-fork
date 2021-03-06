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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class RegexpAutomatonSimplifierStateRemovalOrdererWeighted<T> implements RegexpAutomatonSimplifierStateRemovalOrderer<T> {
  private int getRegexpWeight(final Regexp<T> regexp) {
    return regexp.getTokens().size();
  }

  private int getStateWeight(final RegexpAutomatonStateRemoval<T> automaton, final State<Regexp<T>> state) {
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
  public State<Regexp<T>> getStateToRemove(final RegexpAutomatonStateRemoval<T> automaton) throws InterruptedException {
    int minWeight= Integer.MAX_VALUE;
    State<Regexp<T>> minState= null;
    for (State<Regexp<T>> state : automaton.getDelta().keySet()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (state.equals(automaton.getSuperInitialState())||state.equals(automaton.getSuperFinalState())) {
        continue;//remove except superinitial state and superfinal state
      }
      final int stateWeight= this.getStateWeight(automaton, state);
      if (stateWeight < minWeight) {
        minWeight= stateWeight;
        minState= state;
      }
    }
    return minState;
  }
}
