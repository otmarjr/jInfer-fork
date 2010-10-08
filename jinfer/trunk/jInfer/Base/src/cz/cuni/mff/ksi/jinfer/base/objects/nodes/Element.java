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
  private final List<Attribute> attributes;

  private void checkConstraits() {
    if (subnodes == null) {
      throw new IllegalArgumentException("Subnodes has to be non-null.");
    }
    if (attributes == null) {
      throw new IllegalArgumentException("Attributes has to be non-null.");
    }
  }

  public Element(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final Regexp<AbstractStructuralNode> subnodes, final List<Attribute> attributes) {
    this(context, name, metadata, subnodes, attributes, false);
  }

  private Element(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final Regexp<AbstractStructuralNode> subnodes, final List<Attribute> attributes, final boolean mutable) {
    super(context, name, metadata, mutable);
    this.subnodes = subnodes;
    this.attributes= attributes;
    checkConstraits();
  }

  public static Element getMutable() {
    return new Element(new ArrayList<String>(), 
            null, 
            new HashMap<String, Object>(),
            Regexp.<AbstractStructuralNode>getMutable(),
            new ArrayList<Attribute>(),
            true
            );
  }

  @Override
  public StructuralNodeType getType() {
    return StructuralNodeType.ELEMENT;
  }

  public Regexp<AbstractStructuralNode> getSubnodes() {
    return subnodes;
  }

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
    if (subnodes != null) {
      ret.append('\n').append(subnodes.toString());
    }
    return ret.toString();
  }
}
