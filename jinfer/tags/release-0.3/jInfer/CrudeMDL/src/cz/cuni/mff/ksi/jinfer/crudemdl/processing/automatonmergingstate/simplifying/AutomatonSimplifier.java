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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying;

import cz.cuni.mff.ksi.jinfer.autoeditor.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;

/**
 * Interface for simplifying automaton - given PTA should return something
 * reasonable sized, accepting more general language.
 *
 * @author anti
 */
public interface AutomatonSimplifier<T> {
  Automaton<T> simplify(final Automaton<T> inputAutomaton, final SymbolToString<T> symbolToString) throws InterruptedException;
}
