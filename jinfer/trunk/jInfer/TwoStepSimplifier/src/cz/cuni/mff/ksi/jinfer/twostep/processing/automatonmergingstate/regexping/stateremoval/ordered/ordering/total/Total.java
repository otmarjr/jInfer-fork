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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.total;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.regexpbitcode.BitCode;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers.UniversalCodeForIntegers;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
public class Total<T> implements Orderer<T> {

  private List<Pair<State<Regexp<T>>, Double>> totalOrdering;
  private UniversalCodeForIntegers uic;
  private RegexpEvaluator<T> rEval;

  public Total() {
    this.totalOrdering = new LinkedList<Pair<State<Regexp<T>>, Double>>();
    this.uic= UniversalCodeForIntegers.getSingleton();
    this.rEval= new BitCode<T>();
  }
  
  private double getStepWeight(final StateRemovalRegexpAutomaton<T> automaton, final Step<Regexp<T>> step) throws InterruptedException {
    double result = uic.evaluate(step.getUseCount());
    result+= rEval.evaluate(step.getAcceptSymbol());
    return result;
  }

  private double getStateWeight(final StateRemovalRegexpAutomaton<T> automaton, final State<Regexp<T>> state) throws InterruptedException {
    double weight = uic.evaluate(state.getFinalCount());
    for (Step<Regexp<T>> step : automaton.getReverseDelta().get(state)) {
      weight += getStepWeight(automaton, step);
    }
    for (Step<Regexp<T>> step : automaton.getDelta().get(state)) {
      if (!step.getDestination().equals(state)) {
        // To prevent twice counting the loops
        weight += this.getStepWeight(automaton, step);
      }
    }
    return weight;
  }

  @Override
  public State<Regexp<T>> getStateToRemove(final StateRemovalRegexpAutomaton<T> automaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    if (totalOrdering.isEmpty()) {
      initializeTotalOrdering(automaton);
    }
    return totalOrdering.remove(0).getFirst();
  }

  private void initializeTotalOrdering(final StateRemovalRegexpAutomaton<T> automaton) throws InterruptedException {
    for (State<Regexp<T>> state : automaton.getDelta().keySet()) {
      if (
              state.equals(automaton.getSuperFinalState())
              ||
              state.equals(automaton.getSuperInitialState())) {
        continue;
      }
      totalOrdering.add(
              new Pair<State<Regexp<T>>, Double>(state, getStateWeight(automaton, state)));
    }

    
    Collections.<Pair<State<Regexp<T>>, Double>>sort(totalOrdering, new Comparator<Pair<State<Regexp<T>>, Double>>() {

      @Override
      public int compare(Pair<State<Regexp<T>>, Double> o1, Pair<State<Regexp<T>>, Double> o2) {
        return Double.compare(o1.getSecond(), o2.getSecond());
      }
      
    });
  }
}
