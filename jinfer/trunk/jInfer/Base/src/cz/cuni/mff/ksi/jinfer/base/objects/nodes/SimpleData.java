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
package cz.cuni.mff.ksi.jinfer.base.objects.nodes;

import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.ContentNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a simple text XML node.
 * 
 * @author vektor
 */
public class SimpleData extends AbstractStructuralNode implements ContentNode {

  /** Unspecific type of textual data. */
  private final String contentType;
  /** List of all data found in this node. If not aggregating, this list
   * contains only one item. */
  private final List<String> content;

  /**
   * Create text node given all members (immutable)
   * 
   * @param context context of the in the document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with the node
   * @param contentType type of the content
   * @param content content of the node
   */
  public SimpleData(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final String contentType,
          final List<String> content) {
    this(context, name, metadata, contentType, content, false);
  }

  /**
   * Create text node given all members.
   * 
   * @param context context of the in the document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with the node
   * @param contentType type of the content
   * @param content content of the node
   * @param mutable if it has to be mutable
   */
  private SimpleData(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final String contentType,
          final List<String> content, final boolean mutable) {
    super(context, name, metadata, mutable);

    this.contentType = contentType;
    this.content = content;
    checkConstraits();
  }

  /**
   * Standard way of obtaining empty mutable text node.
   * Be sure to set all fields properly after creating.
   * 
   * @return empty mutable text node
   */
  public static SimpleData getMutable() {
    return new SimpleData(new ArrayList<String>(),
            null,
            new HashMap<String, Object>(),
            null,
            new ArrayList<String>(),
            true);
  }

  @Override
  public StructuralNodeType getType() {
    return StructuralNodeType.SIMPLE_DATA;
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
  public void setImmutable() {
    super.setImmutable();
    checkConstraits();
  }

  @Override
  public String toString() {
    final StringBuilder ret = new StringBuilder(super.toString());
    ret.append('#').append(contentType).append(": ");
    boolean first = true;
    for (final String str : content) {
      if (!first) {
        ret.append(' ');
      }
      first = false;
      ret.append(str.toString());
    }
    return ret.toString();
  }

  private void checkConstraits() {
    if (content == null) {
      throw new IllegalArgumentException("Content must not be null");
    }
  }


}
