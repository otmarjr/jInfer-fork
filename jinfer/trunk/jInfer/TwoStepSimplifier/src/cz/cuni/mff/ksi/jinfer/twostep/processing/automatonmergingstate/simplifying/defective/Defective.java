/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class Defective<T> implements AutomatonSimplifier<T> {
  private AutomatonSimplifier<T> automatonSimplifier;
  private DefectiveAutomatonSimplifier<T> defectiveAutomatonSimplifier;
  Defective(AutomatonSimplifierFactory automatonSimplifierFactory, DefectiveAutomatonSimplifierFactory defectiveAutomatonSimplifierFactory) {
    this.automatonSimplifier = automatonSimplifierFactory.<T>create();
    this.defectiveAutomatonSimplifier= defectiveAutomatonSimplifierFactory.<T>create();
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString) throws InterruptedException {
    return defectiveAutomatonSimplifier.simplify(automatonSimplifier.simplify(inputAutomaton, symbolToString), symbolToString);
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, String elementName) throws InterruptedException {
    return defectiveAutomatonSimplifier.simplify(automatonSimplifier.simplify(inputAutomaton, symbolToString, elementName), symbolToString, elementName);
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, List<List<T>> inputStrings) throws InterruptedException {
    return defectiveAutomatonSimplifier.simplify(automatonSimplifier.simplify(inputAutomaton, symbolToString, inputStrings), symbolToString, inputStrings);
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, String elementName, List<List<T>> inputStrings) throws InterruptedException {
    return defectiveAutomatonSimplifier.simplify(automatonSimplifier.simplify(inputAutomaton, symbolToString, elementName, inputStrings), symbolToString, elementName, inputStrings);
  }
 
  
}
