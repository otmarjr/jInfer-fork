/*
 *  Copyright (C) 2010 vektor
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

import cz.cuni.mff.ksi.jinfer.base.objects.Input;

/**
 * Interface of an IGGenerator module.
 * 
 * @author vektor
 */
public interface IGGenerator {

  /**
   * Returns a user friendly yet unique name of the module.
   *
   * @return Unique module name.
   */
  String getModuleName();

  /**
   * Start the IG generation process.
   *
   * @param input Input data.
   * @param callback Method to be run when IG generation is complete.
   */
  void start(final Input input, final IGGeneratorCallback callback);
  
}
