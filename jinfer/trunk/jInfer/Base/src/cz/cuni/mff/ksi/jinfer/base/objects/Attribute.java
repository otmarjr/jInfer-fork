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
package cz.cuni.mff.ksi.jinfer.base.objects;

import java.util.List;

/**
 * Class representing a XML attribute.
 * 
 * @author vitasek
 */
public class Attribute extends AbstractContentNode {

  public Attribute(final List<String> context,
          final String name,
          final List<Pair<String, Object>> attributes,
          final String contentType,
          final List<? extends Object> content) {
    super(context, name, NodeType.ATTRIBUTE, attributes, contentType, content);
  }
}
