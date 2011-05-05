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
import java.util.Map;

/**
 * Interface representing general node in grammar structure.
 * Elements, attributes and content nodes are all descendants.
 *
 * @author anti
 */
public interface NamedNode {

  /**
   * Context of a node is it's absolute XPath in document.
   * For example from document
   * <a><b><c></c></b></a>
   *
   * Element c has context a/b/.
   *
   * @return list of strings = list of names of elements in XPath
   */
  List<String> getContext();

  /**
   * Name of the node.
   * 
   * @return name of the node.
   */
  String getName();

  /**
   * General map of objects not coming from document data, but describing
   * more precisely how the data was seen. For example may contain location from
   * where the node comes from (from xml file, from query, from schema).
   * And any extensional data needed by inferring methods.
   *
   * @return map <string name of meta data, object representing meta data>
   */
  Map<String, Object> getMetadata();
}
