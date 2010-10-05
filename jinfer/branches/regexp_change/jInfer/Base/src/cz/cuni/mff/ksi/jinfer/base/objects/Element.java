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

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class representing a XML element.
 * 
 * @author vektor
 */
public class Element extends StructuralAbstractNode {

  /** List of all subnodes of this element, in the same order as in the
   * document. */
  private final Regexp<StructuralAbstractNode> subnodes;
  private final List<Attribute> attributes;

  public Element(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final Regexp<StructuralAbstractNode> subnodes, final List<Attribute> attributes) {
    super(context, name, metadata);
    this.subnodes = subnodes;
    this.attributes= attributes;
  }

  @Override
  public StructuralNodeType getType() {
    return StructuralNodeType.ELEMENT;
  }

  public Regexp<StructuralAbstractNode> getSubnodes() {
    return subnodes;
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  @Override
  public String toString() {
    return getName();
    /*
    final StringBuilder ret = new StringBuilder(super.toString());
    if (subnodes != null) {
      ret.append('\n').append(subnodes.toString());
    }
    return ret.toString();
     *
     */
  }
}
