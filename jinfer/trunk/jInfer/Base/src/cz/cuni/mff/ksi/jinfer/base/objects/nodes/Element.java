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
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a XML element.
 * 
 * @author vektor
 */
public class Element extends AbstractStructuralNode {

  /** List of all subnodes of this element, in the same order as in the
   * document. */
  private final Regexp<AbstractStructuralNode> subnodes;
  /**
   * List of all attributes of this element. Order doesn't matter.
   */
  private final List<Attribute> attributes;

  /**
   * Create immutable element given all members.
   *
   * @param context context of the element in document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with the node
   * @param subnodes regular expression representing subnodes of the element (content model)
   * @param attributes attributes of the element
   */
  public Element(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final Regexp<AbstractStructuralNode> subnodes,
          final List<Attribute> attributes) {
    this(context, name, metadata, subnodes, attributes, false);
  }

  /**
   * Create element given all members.
   *
   * @param context context of the element in document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with the node
   * @param subnodes regular expression representing subnodes of the element (content model)
   * @param attributes attributes of the element
   * @param mutable if it has to be mutable
   */
  private Element(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final Regexp<AbstractStructuralNode> subnodes,
          final List<Attribute> attributes,
          final boolean mutable) {

    super(context, name, metadata, mutable);
    this.subnodes = subnodes;
    this.attributes = attributes;
    checkConstraits();
  }

  /**
   * Standard method of obtaining empty mutable element.
   * Be sure to set all members properly before using the element or calling {@link setImmutable()}.
   *
   * @return new Element with empty fields, which is mutable
   */
  public static Element getMutable() {
    return new Element(new ArrayList<String>(),
            null,
            new HashMap<String, Object>(),
            Regexp.<AbstractStructuralNode>getMutable(),
            new ArrayList<Attribute>(),
            true);
  }

  @Override
  public StructuralNodeType getType() {
    return StructuralNodeType.ELEMENT;
  }

  /**
   * Get the subnodes regexp.
   * @return subnodes
   */
  public Regexp<AbstractStructuralNode> getSubnodes() {
    return subnodes;
  }

  /**
   * Get all attributes of element
   * @return list of attributes
   */
  public List<Attribute> getAttributes() {
    if (attributes == null) {
      return null;
    }
    if (mutable) {
      return attributes;
    }
    return Collections.unmodifiableList(attributes);
  }

  @Override
  public void setImmutable() {
    super.setImmutable();
    checkConstraits();
    subnodes.setImmutable();
  }

  @Override
  public String toString() {
    final StringBuilder ret = new StringBuilder(super.toString());
    // output attributes in form <att1, att2, att3>
    if (!BaseUtils.isEmpty(attributes)) {
      ret.append(' ').append('<');
      for (int i = 0; i < attributes.size(); ++i) {
        ret.append(attributes.get(i));
        if (i != (attributes.size() - 1)) {
          ret.append("; ");
        }
      }
      ret.append('>');
    }
    if (subnodes != null) {
      // we really want to print this even if the collection is empty
      ret.append('\n').append(subnodes.toString());
    }
    return ret.toString();
  }

  private void checkConstraits() {
    if (subnodes == null) {
      throw new IllegalArgumentException("Subnodes has to be non-null.");
    }
    if (attributes == null) {
      throw new IllegalArgumentException("Attributes has to be non-null.");
    }
  }

}
