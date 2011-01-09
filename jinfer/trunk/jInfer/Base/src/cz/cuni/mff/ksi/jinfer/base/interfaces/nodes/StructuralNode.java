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
package cz.cuni.mff.ksi.jinfer.base.interfaces.nodes;

/**
 * Interface for nodes that form structure of document tree.
 * That are elements and text nodes (classes Element and SimpleData).
 *
 * Contains isElement() and isSimpleData() functions to recognize actual instance.
 *
 * @author anti
 */
public interface StructuralNode extends NamedNode {

  /**
   * @return enum value of type of actual instance.
   */
  StructuralNodeType getType();

  /**
   * @return true if node type is element.
   */
  boolean isElement();

  /**
   * @return true if node type is simple data.
   */
  boolean isSimpleData();
}
