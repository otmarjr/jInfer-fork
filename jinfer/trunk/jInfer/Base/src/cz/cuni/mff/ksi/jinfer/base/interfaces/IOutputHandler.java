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

/**
 * TODO sviro Comment!
 * 
 * @author vektor
 */
public interface IOutputHandler {

  // TODO sviro Why the prefix "I"? Rename

  /**
   * TODO sviro Comment!
   * 
   * @param fileName
   * @param data
   * @param extension
   */
  void addOutput(final String fileName, final String data, final String extension);

}
