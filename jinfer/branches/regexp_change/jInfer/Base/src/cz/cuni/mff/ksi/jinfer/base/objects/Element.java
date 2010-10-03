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
public class Element extends AbstractNode {

  /** List of all subnodes of this element, in the same order as in the
   * document. */
  private final Regexp<AbstractNode> subnodes;
  private final Regexp<AbstractNode> attributes;

  public Element(final List<String> context,
          final String name,
          final Map<String, Object> metadata,
          final Regexp<AbstractNode> subnodes, final Regexp<AbstractNode> attributes) {
    super(context, name, metadata);
    this.subnodes = subnodes;
    this.attributes= attributes;
  }

  @Override
  public NodeType getType() {
    return NodeType.ELEMENT;
  }

  public Regexp<AbstractNode> getSubnodes() {
    return subnodes;
  }

  public Regexp<AbstractNode> getAttributes() {
    return attributes;
  }

  // TODO exception when unknown regexptype

  public List<Attribute> getElementAttributes() {
    if (subnodes.isLambda()) {
      return Collections.emptyList();
    }
    if (subnodes.isToken()) {
      if (subnodes.getContent().isAttribute()) {
        return Arrays.asList((Attribute) subnodes.getContent());
      }
      return Collections.emptyList();
    }
    final List<Attribute> ret= new ArrayList<Attribute>();
    for (final Regexp<AbstractNode> r : getSubnodes().getChildren()) {
      if (r.isToken() && NodeType.ATTRIBUTE.equals(r.getContent().getType())) {
        ret.add((Attribute) r.getContent());
      }
    }
    return ret;
  }

  /**
   * Returns all attributes of this element.
   * Note: does NOT return attributes of any subelements.
   * Returned list is writable in the sense that method add() will work.
   *
   * @return All attributes of current element.
   */
  public List<Attribute> getElementAttributesMutable() {
    final List<Attribute> ret = new ArrayList<Attribute>() {

      private static final long serialVersionUID = 87451521l;

      @Override
      public boolean add(final Attribute att) {
        boolean found = false;
        for (final AbstractNode node : getSubnodes().getTokens()) {
          if (NodeType.ATTRIBUTE.equals(node.getType())
                  && att.getName().equals(node.getName())) {
            found = true;
          }
        }
        if (!found) {
          getSubnodes().addChild(Regexp.<AbstractNode>getToken(att));
        }
        return super.add(att);
      }
    };
    for (final AbstractNode node : getSubnodes().getTokens()) {
      if (NodeType.ATTRIBUTE.equals(node.getType())) {
        ret.add((Attribute) node);
      }
    }
    return ret;
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
