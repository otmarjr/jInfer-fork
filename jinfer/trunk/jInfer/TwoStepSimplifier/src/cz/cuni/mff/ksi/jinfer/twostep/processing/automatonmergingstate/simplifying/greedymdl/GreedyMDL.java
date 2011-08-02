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
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.DefectiveAutomaton;
import java.util.ArrayList;
import java.util.List;

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
    DefectiveAutomaton<T> a = (new DefectiveAutomaton<T>(oldAut));
    a.minimize();
    return a;
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
