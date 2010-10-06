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
import java.util.Map;

/**
 * Class representing a XML attribute.
 * 
 * @author vektor
 */
public class Attribute extends AbstractNamedNode implements ContentNode {
  /** Unspecific type of textual data. */
  private final String contentType;
  /** List of all data found in this node. If not aggregating, this list
   * contains only one item. */
  private final List<String> content;

  public Attribute(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final String contentType,
          final List<String> content) {
    super(context, name, metadata);

    if (content == null) {
      throw new IllegalArgumentException("Content must not be null");
    }

    this.contentType = contentType;
    this.content = content;

  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public List<String> getContent() {
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
