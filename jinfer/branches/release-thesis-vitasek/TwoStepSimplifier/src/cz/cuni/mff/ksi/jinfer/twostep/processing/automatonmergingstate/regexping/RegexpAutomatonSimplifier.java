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

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

/**
 * Interface for {@link RegexpAutomaton} simplifiers.
 *
 * Given input automaton with
 * Regexp<T> on transition, method simplify has to return Regexp<T>, which
 * corresponds to language accepted by automaton. On input, all regexps on
 * transitions are by definition tokens (it is not enforced anywhere however).
 * @author anti
 */
public interface RegexpAutomatonSimplifier<T> {

  /**
   * Convert input {@link RegexpAutomaton} to {@link Regexp} which represents same
   * language.
   *
   * @param inputAutomaton automaton to convert on regexp.
   * @param symbolToString converter of type T to string representation (for presenting automaton to user)
   * @return regular expression that represents same language as input automaton accepted
   * @throws InterruptedException
   */
  Regexp<T> simplify(final RegexpAutomaton<T> inputAutomaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException;
}
