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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedymdl;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.twostep.ModuleParameters;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluatorFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * GreedyMDL simplifier, given {@link MergeContitionTester}, will merge all states
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
public class GreedyMDL<T> implements AutomatonSimplifier<T> {

  private static final Logger LOG = Logger.getLogger(GreedyMDL.class);
  private final MergeConditionTester<T> mergeConditionTester;
  private final AutomatonEvaluator<T> evaluator;

  /**
   * Create with factory of {@link MergeConditionTester} selected.
   * Without parameters capability!
   *
   * @param mergeConditionTesterFactory factory for {link MergeConditionTester} to use in simplifying.
   */
  public GreedyMDL(final MergeConditionTesterFactory mergeConditionTesterFactory, final AutomatonEvaluatorFactory evaluatorFactory) {
    this.mergeConditionTester = mergeConditionTesterFactory.<T>create();
    this.evaluator= evaluatorFactory.<T>create();
  }
  
  /**
   * Create with factory of {@link MergeConditionTester} selected.
   *
   * @param mergeConditionTesterFactory factory for {link MergeConditionTester} to use in simplifying.
   * @param properties project properties (from which it takes parameter values).
   */
  public GreedyMDL(final MergeConditionTesterFactory mergeConditionTesterFactory, final AutomatonEvaluatorFactory evaluatorFactory,
          final Properties properties) {
    if (mergeConditionTesterFactory.getCapabilities().contains("parameters")) {
      final ModuleParameters factoryParam = ((ModuleParameters) mergeConditionTesterFactory);
      final List<String> parameterNames = factoryParam.getParameterNames();

      for (String parameterName : parameterNames) {
        final String value = properties.getProperty(mergeConditionTesterFactory.getName() + parameterName);
        factoryParam.setParameter(parameterName, value);
      }
    }
    this.mergeConditionTester = mergeConditionTesterFactory.<T>create();
    this.evaluator= evaluatorFactory.<T>create();
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
    final List<List<List<State<T>>>> mergableStates = new ArrayList<List<List<State<T>>>>();
    for (State<T> state1 : inputAutomaton.getDelta().keySet()) {
      for (State<T> state2 : inputAutomaton.getDelta().keySet()) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        mergableStates.addAll(mergeConditionTester.getMergableStates(state1, state2, inputAutomaton));      
      }
    }
    List<Automaton<T>> newAutomatons = new LinkedList<Automaton<T>>();
    for (List<List<State<T>>> mergeAlternative : mergableStates) {
      Automaton<T> newAutomaton = new Automaton<T>(inputAutomaton);
      for (List<State<T>> mergeChain : mergeAlternative) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        newAutomaton.mergeStates(mergeChain);
      }
      newAutomatons.add(newAutomaton);
    }
    double minD = Double.MAX_VALUE;
    Automaton<T> minAut = null;
    this.evaluator.setInputStrings(inputStrings);
    for (Automaton<T> aut : newAutomatons) {
      double thisD = this.evaluator.evaluate(aut);
      if (thisD < minD) {
        minD = thisD;
        minAut = aut;
      }
    }
    if (minAut == null) {
      return inputAutomaton;
    }
    if (minD < this.evaluator.evaluate(inputAutomaton)) {
      return this.simplify(minAut, symbolToString, inputStrings);
    }
    return inputAutomaton;
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
