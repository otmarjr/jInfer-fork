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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.greedy;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.twostep.ModuleParameters;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Greedy simplifier, given {@link MergeContitionTester}, will merge all states
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
public class Greedy<T> implements AutomatonSimplifier<T> {

  private static final Logger LOG = Logger.getLogger(Greedy.class);
  private final MergeConditionTester<T> mergeConditionTester;

  /**
   * Create with factory of {@link MergeConditionTester} selected.
   * Without parameters capability!
   *
   * @param mergeConditionTesterFactory factory for {link MergeConditionTester} to use in simplifying.
   */
  public Greedy(final MergeConditionTesterFactory mergeConditionTesterFactory) {
    this.mergeConditionTester = mergeConditionTesterFactory.<T>create();
  }
  
  /**
   * Create with factory of {@link MergeConditionTester} selected.
   *
   * @param mergeConditionTesterFactory factory for {link MergeConditionTester} to use in simplifying.
   * @param properties project properties (from which it takes parameter values).
   */
  public Greedy(final MergeConditionTesterFactory mergeConditionTesterFactory,
          final Properties properties) {
    if (mergeConditionTesterFactory.getCapabilities().contains("parameters")) {
      final ModuleParameters factoryParam = ((ModuleParameters) mergeConditionTesterFactory);
      final List<String> parameterNames = factoryParam.getParameterNames();

      for (String parameterName : parameterNames) {
        final String value = properties.getProperty(mergeConditionTesterFactory.getName() + parameterName);
        int intValue;
        try {
          intValue = Integer.parseInt(value);
          factoryParam.setParameter(parameterName, intValue);
        } catch (NumberFormatException e) {
          LOG.error("Parameter named "
                  + parameterName
                  + " which should be set for using submodule "
                  + mergeConditionTesterFactory.getName()
                  + " is not a number. "
                  + "Submodule will use some default value. "
                  + "This error was triggered by exception: " + e.toString());
        }
      }
    }
    this.mergeConditionTester = mergeConditionTesterFactory.<T>create();
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
          final SymbolToString<T> symbolToString) throws InterruptedException {
    final Map<State<T>, Set<Step<T>>> delta = inputAutomaton.getDelta();

    final List<List<List<State<T>>>> mergableStates = new ArrayList<List<List<State<T>>>>();
    for (State<T> mainState : delta.keySet()) {
      for (State<T> mergedState : delta.keySet()) {
        mergableStates.addAll(mergeConditionTester.getMergableStates(mainState, mergedState, inputAutomaton));
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
      }
    }
    LOG.debug("Found states to merge\n");
    for (List<List<State<T>>> mergableAlternative : mergableStates) {
      for (List<State<T>> mergeSeq : mergableAlternative) {
        final String st = CollectionToString.<State<T>>colToString(mergeSeq, " + ",
                new CollectionToString.ToString<State<T>>() {

                  @Override
                  public String toString(final State<T> t) {
                    return t.toString();
                  }
                });
        LOG.debug("Merging states: " + st + "\n");
        inputAutomaton.mergeStates(mergeSeq);
      }
    }
    mergableStates.clear();
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
          final String elementName) throws InterruptedException {
    return simplify(inputAutomaton, symbolToString);
  }
}
