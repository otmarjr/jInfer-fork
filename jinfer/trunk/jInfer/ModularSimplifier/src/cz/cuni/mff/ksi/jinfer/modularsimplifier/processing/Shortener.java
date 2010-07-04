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

package cz.cuni.mff.ksi.jinfer.modularsimplifier.processing;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Removes concatenations and alternations of length 1 and pulls its children up.
 * 
 * @author vektor
 */
public class Shortener {

  private final List<Element> visited = new ArrayList<Element>();

  public Element simplify(final Element treeBase) {
    for (final Element v : visited) {
      if (v == treeBase) {
        return treeBase;
      }
    }
    visited.add(treeBase);
    return new Element(
            treeBase.getContext(),
            treeBase.getName(),
            treeBase.getAttributes(),
            simplify(treeBase.getSubnodes()));
  }

  @SuppressWarnings("PMD.MissingBreakInSwitch")
  private Regexp<AbstractNode> simplify(final Regexp<AbstractNode> regexp) {
    switch (regexp.getType()) {
      case TOKEN:
        if (NodeType.ELEMENT.equals(regexp.getContent().getType())) {
          return Regexp.<AbstractNode>getToken(simplify((Element)regexp.getContent()));
        }
        return regexp;
      case ALTERNATION:
      case CONCATENATION:
        if (regexp.getChildren().size() == 1) {
          return simplify(regexp.getChild(0));
        }
        final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>(regexp.getChildren().size());
        for (final Regexp<AbstractNode> child : regexp.getChildren()) {
          children.add(simplify(child));
        }
        return new Regexp<AbstractNode>(null, children, regexp.getType());
      default: return regexp;
    }
  }

}
