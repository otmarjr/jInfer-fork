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
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.DefectiveAutomaton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
    SortedMap<Double, Automaton<T>> solutions2 = new TreeMap<Double, Automaton<T>>();
    this.evaluator.setInputStrings(inputStrings);
    solutions.put(this.evaluator.evaluate(inputAutomaton), inputAutomaton);
    final List<List<List<State<T>>>> mergableStates = new ArrayList<List<List<State<T>>>>();

    int stagger = 0;
    do {
      Double aKey = solutions.lastKey();// myRandom(solutions.keySet());
      Automaton<T> actual = solutions.get(aKey);
      solutions.remove(aKey);
      solutions2.put(aKey, actual);
      mergableStates.clear();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      mergableStates.addAll(mergeConditionTester.getMergableStates(actual));
      if (!mergableStates.isEmpty()) {
        for (List<List<State<T>>> mergeAlternative : mergableStates) {
          Automaton<T> newAutomaton = new Automaton<T>(actual);
          for (List<State<T>> mergeChain : mergeAlternative) {
            if (Thread.interrupted()) {
              throw new InterruptedException();
            }
            newAutomaton.mergeStates(mergeChain);
          }

          double thisD = this.evaluator.evaluate(newAutomaton);
          solutions.put(thisD, newAutomaton);
          if (solutions.size() > 10) {
            solutions.remove(solutions.lastKey());
          }
        }
      }
      if (solutions.isEmpty()||(!(solutions.firstKey() < solutions2.firstKey()))) {
        stagger++;
      }
    } while ((stagger < 10)&&(!solutions.isEmpty()));
    solutions2.putAll(solutions);
    DefectiveAutomaton<T> a = (new DefectiveAutomaton<T>(solutions2.get(solutions2.firstKey())));
    a.minimize();
    return a;
  }

  private Double myRandom(Set<Double> srt) {
    List<Double> prd = new LinkedList<Double>();
    double sum = 0.0;
    for (Double x : srt) {
      sum += 1 / x;
      prd.add(sum);
    }

    double rnd = Math.random() * sum;
    Iterator<Double> prdit = prd.iterator();
    Iterator<Double> srtit = srt.iterator();
    while (prdit.hasNext()) {
      Double x = prdit.next();
      Double orig = srtit.next();
      if (x >= rnd) {
        return orig;
      }
    }
    throw new IllegalStateException("Impossible");
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
