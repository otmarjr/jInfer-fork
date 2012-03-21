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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.DefectiveAutomatonEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.DefectiveAutomatonEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.DefectiveAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl.suspection.Suspection;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl.suspection.SuspectionFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.determinist.Determinist;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * DefectiveMDL simplifier, given {@link MergeContitionTester}, will merge all states
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
public class DefectiveMDL<T> implements AutomatonSimplifier<T> {

  private static final Logger LOG = Logger.getLogger(DefectiveMDL.class);
  private final DefectiveAutomatonEvaluator<T> evaluator;
  private final Suspection<T> suspection;

  /**
   * Create with factory of {@link MergeConditionTester} selected.
   * Without parameters capability!
   *
   * @param mergeConditionTesterFactory factory for {link MergeConditionTester} to use in simplifying.
   */
  public DefectiveMDL(final DefectiveAutomatonEvaluatorFactory evaluatorFactory, SuspectionFactory suspectionFactory) {
    this.evaluator = evaluatorFactory.<T>create();
    this.suspection = suspectionFactory.<T>create();
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
    Determinist<T> dt = new Determinist<T>();
    DefectiveAutomaton<T> stepAutomaton = new DefectiveAutomaton<T>(dt.simplify(inputAutomaton, symbolToString));
    this.suspection.setInputStrings(inputStrings);
    this.suspection.setSymbolToString(symbolToString);
    this.suspection.setInputAutomaton(stepAutomaton);

    List<List<T>> suspected;
    while (null != (suspected = this.suspection.getNextSuspectedInputStrings())) {
      double mdl = evaluator.evaluate(stepAutomaton);
      List<List<T>> sspected = new ArrayList<List<T>>(suspected.size());
      sspected.addAll(suspected);
      for (List<T> inputString : sspected) {
        stepAutomaton.tryRemoveInputString(inputString);
      }
      double mdl2 = evaluator.evaluate(stepAutomaton);
      for (List<T> inputString : sspected) {
        LOG.error("string: " + inputString.toString());
      }
      LOG.error(" mdl:" + String.valueOf(mdl) + " mdl2: " + String.valueOf(mdl2));
      if (mdl2 >= mdl) {
        for (List<T> inputString : sspected) {
          stepAutomaton.undoRemoveInputString(inputString);
        }
      } else {
        LOG.fatal("Removed strings");
      }
    }
    stepAutomaton.minimize();
    return stepAutomaton;
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
