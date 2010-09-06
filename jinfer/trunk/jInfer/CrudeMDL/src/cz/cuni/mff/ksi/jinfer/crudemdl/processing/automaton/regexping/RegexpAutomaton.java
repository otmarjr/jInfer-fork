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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.AutomatonCloner;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.AutomatonClonerImpl;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.AutomatonSymbolConverter;
import org.apache.log4j.Logger;

/**
 * Automaton with Regexp<AbstractNode> on steps. Can be simplified by removing states
 * to a single regexp. 
 *
 * @author anti
 */
public class RegexpAutomaton<T> extends Automaton<Regexp<T>> {
  private static final Logger LOG = Logger.getLogger(RegexpAutomaton.class);
//  private State<Regexp<T>> superFinalState;

  /**
   * Constructor to create empty automaton.
   */
  public RegexpAutomaton() {
    super(false);
  }

  /**
   * As in automaton, whether to create initial state
   * 
   * @param createInitialState
   */
  public RegexpAutomaton(boolean createInitialState) {
    super(createInitialState);
  }

  /**
   * Given another automaton, creates this automaton with same structure (states, delta function).
   * But with Regexp<T> on steps.
   * Using AutomatonCloner of course.
   *
   * @param anotherAutomaton
   */
  public RegexpAutomaton(final Automaton<T> anotherAutomaton) {
    super(false);

    AutomatonCloner<T, Regexp<T>> cloner= new AutomatonClonerImpl<T, Regexp<T>>();

    cloner.convertAutomaton(anotherAutomaton, this,
      new AutomatonSymbolConverter<T, Regexp<T>>() {
        @Override
        public Regexp<T> convertSymbol(T symbol) {
          return Regexp.<T>getToken(symbol);
        }
      }
    );
  }

  /**
   * Clear cloning constructor.
   * 
   * @param anotherAutomaton
   */
  public RegexpAutomaton(final RegexpAutomaton<T> anotherAutomaton) {
    super(anotherAutomaton);
  }
}
