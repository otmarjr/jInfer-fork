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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering;

import cz.cuni.mff.ksi.jinfer.autoeditor.SymbolToString;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.RegexpAutomatonStateRemoval;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;

/**
 * RegexpAutomatonSimplifierStateRemovalOrderer is interface for determining
 * which state to remove from RegexpAutomaton to obtain shorter regexp.
 *
 * Given RegexpAutomatonStateRemoval automaton, it has to explore structure and
 * decide, which state would be best to remove first.
 *
 * @author anti
 */
public interface RegexpAutomatonSimplifierStateRemovalOrderer<T> {
  State<Regexp<T>> getStateToRemove(final RegexpAutomatonStateRemoval<T> automaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException;
}
