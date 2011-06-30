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
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.deterministic.Deterministic;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.deterministic.DeterministicFactory;
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
    this.evaluator = evaluatorFactory.<T>create();
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
    final List<List<List<State<T>>>> mergableStates = new ArrayList<List<List<State<T>>>>();
    MergeConditionTester<T> dt = (new DeterministicFactory()).<T>create();
    Automaton<T> oldAut = inputAutomaton;
    evaluator.setInputStrings(inputStrings);
    double oldMdl = evaluator.evaluate(oldAut);
    Automaton<T> newAut = null;
    double newMdl = Double.MAX_VALUE;
    boolean stagger = true;
    do {
      stagger = true;
      mergableStates.clear();
      mergableStates.addAll(mergeConditionTester.getMergableStates(oldAut));
      if (!mergableStates.isEmpty()) {
        newAut = new Automaton<T>(oldAut);
        for (List<List<State<T>>> mergAlt : mergableStates) {
          for (List<State<T>> mergSeq : mergAlt) {
            newAut.mergeStates(mergSeq);
          }
          for (List<List<State<T>>> mA : dt.getMergableStates(newAut)) {
            for (List<State<T>> mS : mA) {
              newAut.mergeStates(mS);
            }
          }
          newMdl = evaluator.evaluate(newAut);
          if (newMdl < oldMdl) {
            oldAut = newAut;
            oldMdl = newMdl;
            stagger = false;
          } else {
            newAut = new Automaton<T>(oldAut);
          }
        }
      }
    } while (!stagger);
    return oldAut;
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
