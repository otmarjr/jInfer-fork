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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.interfaces.UserModuleDescription;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

/**
 * Factory for {@link RegularExpressionCleaner}.
 *
 * @author anti
 */
public interface RegularExpressionCleanerFactory extends NamedModule, Capabilities, UserModuleDescription {

  /**
   * Create cleaner of {@link Regexp}<T>.
   *
   * @param <T> type of symbol around which are regexpes build.
   * @return cleaner instance
   */
   <T> RegularExpressionCleaner<T> create();
}
