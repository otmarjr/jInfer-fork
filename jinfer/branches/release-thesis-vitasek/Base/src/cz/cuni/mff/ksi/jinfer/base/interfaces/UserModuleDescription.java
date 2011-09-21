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

package cz.cuni.mff.ksi.jinfer.base.interfaces;

/**
 * Interface which must be implemented by factory classes of submodules.
 * The description should be renderer in properties panel of module, which
 * provides user a selection of submodules. Should be 10-20 lines in length.
 *
 * @author anti
 */
public interface UserModuleDescription {
  /**
   * Text to be displayed to user in properties panel as a moderate description
   * of module. 10-20 lines.
   * @return
   */
  String getUserModuleDescription();
}
