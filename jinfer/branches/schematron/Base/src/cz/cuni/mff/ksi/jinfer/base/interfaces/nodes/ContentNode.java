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
 * Interface representing nodes, that have content.
 * Both text nodes and attributes have content, so they implement this interface.
 *
 * @author anti
 */
public interface ContentNode extends NamedNode {

  /**
   * General way o describing content of node.
   * Reserved for further extensibility by content analyzing module developers.
   * 
   * @return future developer defined name of content type.
   */
  String getContentType();

  /**
   * General way o describing content of node.
   * Reserved for further extensibility by content analyzing module developers.
   * 
   * At initial crawling, each content node may have only one element in this list.
   * 
   * Inferrence methods can take advantage of list by groups similar contents
   * to one node.
   *  
   * @return list of strings - content assigned to this node.
   */
  List<String> getContent();
}
