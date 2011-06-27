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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.heuristicmdl;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemoval;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.weighted.WeightedFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * HeuristicMDL simplifier, given {@link MergeContitionTester}, will merge all states
 * that tester returns as equivalent.
 *
 * It will ask the tester for capability "parameters".
 * When the tester is one from known, it will set parameters to it according
 * to user preference. If it is not known, it is created with factory,
 * so tester have to have some reasonable default parameter setting or own
 * properties panel for user to enter defaults.
 *
 * @author anti
 */
public class HeuristicMDL<T> implements AutomatonSimplifier<T> {

  private static final Logger LOG = Logger.getLogger(HeuristicMDL.class);
  private final MergeConditionTester<T> mergeConditionTester;
  private final AutomatonEvaluator<T> evaluator;

  /**
   * Create with factory of {@link MergeConditionTester} selected.
   * Without parameters capability!
   *
   * @param mergeConditionTesterFactory factory for {link MergeConditionTester} to use in simplifying.
   */
  public HeuristicMDL(final MergeConditionTesterFactory mergeConditionTesterFactory, final AutomatonEvaluatorFactory evaluatorFactory) {
    this.mergeConditionTester = mergeConditionTesterFactory.<T>create();
    this.evaluator = evaluatorFactory.<T>create();
  }

  /**
   * Create with factory of {@link MergeConditionTester} selected.
   *
   * @param mergeConditionTesterFactory factory for {link MergeConditionTester} to use in simplifying.
   * @param properties project properties (from which it takes parameter values).
   */
  public HeuristicMDL(final MergeConditionTesterFactory mergeConditionTesterFactory, final AutomatonEvaluatorFactory evaluatorFactory,
          final Properties properties) {
/*    if (mergeConditionTesterFactory.getCapabilities().contains("parameters")) {
      final ModuleParameters factoryParam = ((ModuleParameters) mergeConditionTesterFactory);
      final List<String> parameterNames = factoryParam.getParameterNames();

      for (String parameterName : parameterNames) {
        final String value = properties.getProperty(mergeConditionTesterFactory.getName() + parameterName);
        factoryParam.setParameter(parameterName, value);
      }
    }*/
    this.mergeConditionTester = mergeConditionTesterFactory.<T>create();
    this.evaluator = evaluatorFactory.<T>create();
  }

  /**
   * Simplify by merging states greedily.
   *
   * Loops until there are no more states to merge by tester.
   *
   * @param inputAutomaton automaton to modify
   * @param symbolToString edge label to string converter
   * @throws InterruptedException
   */
  @Override
  public Automaton<T> simplify(final Automaton<T> inputAutomaton,
          final SymbolToString<T> symbolToString, List<List<T>> inputStrings) throws InterruptedException {
    SortedMap<Double, Automaton<T>> solutions = new TreeMap<Double, Automaton<T>>();
    this.evaluator.setInputStrings(inputStrings);
    solutions.put(this.evaluator.evaluate(inputAutomaton), inputAutomaton);
    final List<List<List<State<T>>>> mergableStates = new ArrayList<List<List<State<T>>>>();

    boolean staggering = true;
    do {
      Automaton<T> actual= solutions.get(solutions.firstKey());
      mergableStates.clear();
      for (State<T> state1 : actual.getDelta().keySet()) {
        for (State<T> state2 : actual.getDelta().keySet()) {
          if (Thread.interrupted()) {
            throw new InterruptedException();
          }
          mergableStates.addAll(mergeConditionTester.getMergableStates(state1, state2, actual));
        }
      }

      for (List<List<State<T>>> mergeAlternative : mergableStates) {
        Automaton<T> newAutomaton = new Automaton<T>(actual);
        for (List<State<T>> mergeChain : mergeAlternative) {
          if (Thread.interrupted()) {
            throw new InterruptedException();
          }
          newAutomaton.mergeStates(mergeChain);
        }
        double thisD = this.evaluator.evaluate(newAutomaton);
        if (thisD < solutions.lastKey()) {
          solutions.put(thisD, newAutomaton);
        }
        if (solutions.size() > 10) {
          solutions.remove(solutions.lastKey());
        }
      }
    } while (!staggering);
    return solutions.get(solutions.firstKey());
  }
  
  private Double myRandom(Set<Double> srt) {
    int size = srt.size();
    int index= (int) Math.floor(Math.random()*size);
    Iterator<Double> it = srt.iterator();
    int i = 0;
    while (it.hasNext()&&i < index) {
      it.next();
      i++;
    }
    return it.next();
  }

  /**
   * Same as above.
   *
   * @param inputAutomaton automaton to modify
   * @param symbolToString edge label to string converter
   * @param elementName name of element (cluster) we process right now, to be presented to user
   * @return
   * @throws InterruptedException
   */
  @Override
  public Automaton<T> simplify(
          final Automaton<T> inputAutomaton,
          final SymbolToString<T> symbolToString,
          final String elementName, List<List<T>> inputStrings) throws InterruptedException {
    return simplify(inputAutomaton, symbolToString, inputStrings);
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString) throws InterruptedException {
    throw new UnsupportedOperationException("Needs input strings to do evaluation.");
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, String elementName) throws InterruptedException {
    throw new UnsupportedOperationException("Needs input strings to do evaluation.");
  }
}
