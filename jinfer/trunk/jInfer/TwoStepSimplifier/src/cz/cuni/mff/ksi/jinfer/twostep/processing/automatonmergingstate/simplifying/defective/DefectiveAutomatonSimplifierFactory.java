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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.interfaces.UserModuleDescription;

/**
 * Factory for {@link DefectiveAutomatonSimplifier}.
 *
 * @author anti
 */
public interface DefectiveAutomatonSimplifierFactory extends NamedModule, Capabilities, UserModuleDescription {

  /**
   * Create generic {@link DefectiveAutomatonSimplifier} instance. Simplifying automaton
   * does not depend on symbol type T.
   * 
   * @param <T> type of symbol in automaton (alphabet domain)
   * @return instance of {@link DefectiveAutomatonSimplifier} implementation
   */
  <T> DefectiveAutomatonSimplifier<T> create();
}
