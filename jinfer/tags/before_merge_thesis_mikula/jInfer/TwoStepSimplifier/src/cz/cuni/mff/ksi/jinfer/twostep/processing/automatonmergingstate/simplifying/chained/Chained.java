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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.chained;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Chained simplifier, given {@link MergeContitionTester}, will merge all states
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
public class Chained<T> implements AutomatonSimplifier<T> {

  private List<AutomatonSimplifier<T>> simplifiers;
  private static final Logger LOG = Logger.getLogger(Chained.class);

  public Chained(List<AutomatonSimplifierFactory> factories) {
    this.simplifiers = new LinkedList<AutomatonSimplifier<T>>();
    for (AutomatonSimplifierFactory factory : factories) {
      this.simplifiers.add(factory.<T>create());
    }
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
    Automaton<T> aut = inputAutomaton;
    for (AutomatonSimplifier<T> simplifier : simplifiers) {
      aut = simplifier.simplify(aut, symbolToString);
    }
    return aut;
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
    Automaton<T> aut = inputAutomaton;
    for (AutomatonSimplifier<T> simplifier : simplifiers) {
      aut = simplifier.simplify(aut, symbolToString, elementName);
    }
    return aut;
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, List<List<T>> inputStrings) throws InterruptedException {
    Automaton<T> aut = inputAutomaton;
    for (AutomatonSimplifier<T> simplifier : simplifiers) {
      aut = simplifier.simplify(aut, symbolToString, inputStrings);

    }
    return aut;
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, String elementName, List<List<T>> inputStrings) throws InterruptedException {
    Automaton<T> aut = inputAutomaton;
    for (AutomatonSimplifier<T> simplifier : simplifiers) {
      aut = simplifier.simplify(aut, symbolToString, elementName, inputStrings);
    }
    return aut;
  }
}
