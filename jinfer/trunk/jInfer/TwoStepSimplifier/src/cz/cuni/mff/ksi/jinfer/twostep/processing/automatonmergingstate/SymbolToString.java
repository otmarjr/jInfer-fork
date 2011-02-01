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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;

/**
 * Convert generic type T symbol on automaton transitions to String.
 * 
 * When automaton is being renderer in {@link AutoEditor}, this interface is used to
 * convert symbols on transitions to strings drawn about lines.
 * Along with passing automaton to drawing procedure in {@link AutoEditor} one has to
 * pass class implementing this interface, which will convert type T on
 * Automaton<T> to string. (type of symbol on automaton transitions).
 * <p>
 * Automaton and all simplifiers don't know the type T in run-time, this is the
 * only way to present automaton to user with meaningful labels on transitions
 * rendered.
 *
 * @param <T> generic type of symbol (same as automaton it has to work with)
 * @author anti
 */
public interface SymbolToString<T> {

  /**
   * Produce user-comprehensive string representation of symbol.
   *
   * @param symbol of alphabet in automaton
   * @return
   */
  String toString(T symbol);
}
