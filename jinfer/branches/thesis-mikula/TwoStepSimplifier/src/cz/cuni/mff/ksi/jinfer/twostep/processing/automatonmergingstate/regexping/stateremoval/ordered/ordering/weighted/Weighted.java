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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.weighted;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluatorFactory;

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
public class Weighted<T> implements Orderer<T> {

  private RegexpEvaluator<T> rEval;

  public Weighted(RegexpEvaluatorFactory factory) {
    this.rEval = factory.<T>create();
  }

  private double getStateWeight(final StateRemovalRegexpAutomaton<T> automaton, final State<Regexp<T>> state) throws InterruptedException {
    double weight = 0;
    int loopSize = automaton.getLoopSteps(state).size();
    int outSize = automaton.getDelta().get(state).size() - loopSize;
    int inSize = automaton.getReverseDelta().get(state).size() - loopSize;
    for (Step<Regexp<T>> step : automaton.getReverseDelta().get(state)) {
      if (!step.getSource().equals(state)) {
        weight += rEval.evaluate(step.getAcceptSymbol()) * (outSize - 1);
      } else {
        weight += rEval.evaluate(step.getAcceptSymbol()) * ((outSize * inSize) - 1);
      }
    }
    for (Step<Regexp<T>> step : automaton.getDelta().get(state)) {
      if (!step.getDestination().equals(state)) {
        // To prevent twice counting the loops
        weight += rEval.evaluate(step.getAcceptSymbol()) * (inSize - 1);
      }
    }
    return weight;
  }

  @Override
  public State<Regexp<T>> getStateToRemove(final StateRemovalRegexpAutomaton<T> automaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    double minWeight = Double.MAX_VALUE;
    State<Regexp<T>> minState = null;
    for (State<Regexp<T>> state : automaton.getDelta().keySet()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (state.equals(automaton.getSuperInitialState()) || state.equals(automaton.getSuperFinalState())) {
        continue;//remove except superinitial state and superfinal state
      }
      automaton.collapseStateParallelSteps(state);
      if (minState == null) {
        minState = state;
        minWeight = this.getStateWeight(automaton, state);
      } else {
        final double stateWeight = this.getStateWeight(automaton, state);
        if ((stateWeight < minWeight) || ((stateWeight == minWeight) && (state.getName() < minState.getName()))) {
          minWeight = stateWeight;
          minState = state;
        }
      }
    }
    return minState;
  }
}
