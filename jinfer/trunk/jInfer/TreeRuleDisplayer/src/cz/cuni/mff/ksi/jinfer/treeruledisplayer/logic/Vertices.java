/*
 *  Copyright (C) 2011 sviro
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

package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

/**
 * Enum of all types of Vertices occured in Tree rule displayer legend. Each vertex has
 * size of its name.
 * @author sviro
 */
public enum Vertices {
  ROOT(30),
  ELEMENT(50),
  SIMPLE_DATA(80),
  ATTRIBUTE(57),
  LAMBDA(14),
  CONCATENATION(105),
  ALTERNATION(85),
  PERMUTATION(85);
  
  private final int nameSize;

  Vertices(final int nameSize) {
    this.nameSize = nameSize;
  }

  /**
   * get size of the vertex name.
   * @return Size of the vertex name.
   */
  int nameSize() {
    return nameSize;
  }


}
