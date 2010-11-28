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

import java.util.List;

/**
 * Interface representing basic work cycle with nodes, that have content.
 * It is used to work with content of text nodes (SimpleData) and attributes
 * (Attribute)
 *
 * @author anti
 */
public interface ContentNode extends NamedNode {

  // TODO anti Comment!
  // TODO anti What is "basic work cycle" from the class comment? Please clarify.
  String getContentType();

  List<String> getContent();
}
