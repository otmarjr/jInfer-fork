/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.base.interfaces;

/**
 * Interface of a module with a name and description.
 * 
 * @author sviro
 */
public interface NamedModule {

  /**
   * Returns a user friendly yet unique name of the module.
   *
   * @return Unique module name.
   */
  String getName();

  /**
   * Returns a user friendly name of the module. This name is not neccessary unique.
   * @return Displayable module name.
   */
  String getDisplayName();

  /**
   * Returns the information about this module's inner workings.
   * Most of the time will be equal to a call to 
   * {@link NamedModule#getDisplayName()}, but if the
   * module for example consists of more sub-modules, their names should be
   * listed here.
   *
   * @return String describing inner structure of the module.
   */
  String getModuleDescription();
}
