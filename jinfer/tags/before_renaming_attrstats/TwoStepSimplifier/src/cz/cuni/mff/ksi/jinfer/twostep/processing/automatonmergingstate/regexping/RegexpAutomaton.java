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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.AutomatonCloner;
import cz.cuni.mff.ksi.jinfer.base.automaton.AutomatonClonerSymbolConverter;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;

/**
 * Automaton with Regexp<T> on transitions
 *
 * Type T is to be the same as in ordinary automaton.
 *
 * @author anti
 */
public class RegexpAutomaton<T> extends Automaton<Regexp<T>> {

  /**
   * Constructor to create empty automaton.
   */
  public RegexpAutomaton() {
    super(false);
  }

  /**
   * As in automaton, whether to create initial state
   * 
   * @param createInitialState true/false = create/not create initial state
   */
  public RegexpAutomaton(final boolean createInitialState) {
    super(createInitialState);
  }

  /**
   * Given another automaton, creates this automaton with same structure (states, delta function).
   * But with Regexp<T> on steps.
   * Using {@link AutomatonCloner} of course.
   *
   * @param anotherAutomaton ordinary automaton which structure we will copy
   */
  public RegexpAutomaton(final Automaton<T> anotherAutomaton) {
    super(false);

    final AutomatonCloner<T, Regexp<T>> cloner = new AutomatonCloner<T, Regexp<T>>();

    cloner.convertAutomaton(anotherAutomaton, this,
            new AutomatonClonerSymbolConverter<T, Regexp<T>>() {

              @Override
              public Regexp<T> convertSymbol(final T symbol) {
                return Regexp.<T>getToken(symbol);
              }
            });
    for (State<Regexp<T>> state : this.delta.keySet()) {
      for (Step<Regexp<T>> step : this.delta.get(state)) {
        step.setMinUseCount(step.getUseCount());
      }
    }
  }

  /**
   * Cloning constructor.
   * 
   * @param anotherAutomaton another regexp automaton we will be clone of
   */
  public RegexpAutomaton(final RegexpAutomaton<T> anotherAutomaton) {
    super(anotherAutomaton);
  }
}
