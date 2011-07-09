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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.heuristic;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.Orderer;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 * Worker class of state removal algorithm.
 * 
 * Removing states and combining regexps on transitions properly leads to
 * automaton with only 2 states and one transition. Final regexp is on that transition.
 * <p>
 * Uses {@link RegexpAutomatonStateRemoval} automaton implementation of automaton, to
 * which it supplies states to remove in "correct" order. The order matters -
 * regexp can be shorter or longer (and complicated) according to order of states selected.
 * <p>
 * Order is determined by implementation of submodule interface
 * {@link Orderer}.
 *
 * @author anti
 */
public class StateRemovalHeuristic<T> implements RegexpAutomatonSimplifier<T> {

  private RegexpEvaluator<T> rEval;
  private static final Logger LOG = Logger.getLogger(StateRemovalHeuristic.class);

  public StateRemovalHeuristic(RegexpEvaluatorFactory rEvalFactory) {
    this.rEval = rEvalFactory.<T>create();
  }

  private double getAutomatonLength(final StateRemovalRegexpAutomaton<T> automaton) throws InterruptedException {
    double result = 0.0;
    Set<Step<Regexp<T>>> brum = new HashSet<Step<Regexp<T>>>();
    for (State<Regexp<T>> state : automaton.getDelta().keySet()) {
      for (Step<Regexp<T>> step : automaton.getDelta().get(state)) {
        brum.add(step);
      }
    }
    for (Step<Regexp<T>> step : brum) {
      result += rEval.evaluate(step.getAcceptSymbol());
    }
    return result;
  }

  @Override
  public Regexp<T> simplify(final RegexpAutomaton<T> inputAutomaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    final StateRemovalRegexpAutomaton<T> stateRemovalAutomaton = new StateRemovalRegexpAutomaton<T>(inputAutomaton);

    SortedMap<Double, StateRemovalRegexpAutomaton<T>> solutions = new TreeMap<Double, StateRemovalRegexpAutomaton<T>>();
    solutions.put(getAutomatonLength(stateRemovalAutomaton), stateRemovalAutomaton);


    StateRemovalRegexpAutomaton<T> newAut;
    SortedMap<Double, StateRemovalRegexpAutomaton<T>> toAdd = new TreeMap<Double, StateRemovalRegexpAutomaton<T>>();
    while (solutions.get(solutions.firstKey()).getDelta().size() > 2) {
      for (Double key : solutions.keySet()) {
        StateRemovalRegexpAutomaton<T> actual = solutions.get(key);
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        for (State<Regexp<T>> state : actual.getDelta().keySet()) {
          if (state.equals(actual.getSuperFinalState()) || state.equals(actual.getSuperInitialState())) {
            continue;
          }
          newAut = new StateRemovalRegexpAutomaton<T>(actual);
          newAut.removeState(state);
          double thisLength = getAutomatonLength(newAut);
          toAdd.put(thisLength, newAut);
          /*          if (toAdd.keySet().size() > 10) {
          toAdd.remove(toAdd.lastKey());
          }*/
        }
      }
      solutions.clear();
      SortedMap<Double, StateRemovalRegexpAutomaton<T>> tmp = solutions;
      solutions = toAdd;
      toAdd = tmp;
    }
    SortedMap<Double, Regexp<T>> regexpMap = new TreeMap<Double, Regexp<T>>();
    for (StateRemovalRegexpAutomaton<T> aut : solutions.values()) {
      aut.finalStep();
      Set<Step<Regexp<T>>> regexpSteps = aut.getDelta().get(
              aut.getSuperInitialState());
      if (!(regexpSteps.size() == 1)) {
        throw new IllegalStateException("There should be only one step in whole automaton.");
      }
      Regexp<T> reg = regexpSteps.iterator().next().getAcceptSymbol();
      double regD = rEval.evaluate(reg);
      regexpMap.put(regD, reg);
      LOG.error("#:" + regD + " " + reg);
    }
    return regexpMap.get(regexpMap.firstKey());
  }
}
