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
 * Class representing XML node with textual content.
 * 
 * @author vitasek
 */
public abstract class AbstractContentNode extends AbstractNode {

  /** Unspecific type of textual data. */
  private final String contentType;
  /** List of all data found in this node. If not aggregating, this list
   * contains only one item. */
  private final List<? extends Object> content;

  public AbstractContentNode(final List<String> context,
          final String name,
          final NodeType type,
          final List<Pair<String, Object>> attributes,
          final String contentType,
          final List<? extends Object> content) {
    super(context, name, type, attributes);
    this.contentType = contentType;
    this.content = content;
  }

  public String getContentType() {
    return contentType;
  }

  public List<? extends Object> getContent() {
    return content;
  }

  @Override
  public String toString() {
    final StringBuilder ret = new StringBuilder(super.toString());
    ret.append('\n').append(contentType).append(": ");
    for (final Object o : content) {
      ret.append(o.toString()).append(' ');
    }
    return ret.toString();
  }
}
