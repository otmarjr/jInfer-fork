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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.interfaces.UserModuleDescription;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;

/**
 * Factory interface for {@link Orderer}.
 *
 * @author anti
 */
public interface OrdererFactory extends NamedModule, Capabilities, UserModuleDescription {

  /**
   * Create new orderer, which works on {@link StateRemovalRegexpAutomaton}.
   * @param <T> type of symbol of automaton (alphabet)
   * @return orderer instance
   */
   <T> Orderer<T> create();
}
