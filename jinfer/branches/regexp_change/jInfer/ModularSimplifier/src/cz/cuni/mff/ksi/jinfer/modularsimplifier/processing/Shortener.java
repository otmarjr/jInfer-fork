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

import cz.cuni.mff.ksi.jinfer.base.objects.StructuralAbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.StructuralNodeType;
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

  public Element simplify(final Element root) {
    for (final Element v : visited) {
      if (v == root) {
        return root;
      }
    }
    visited.add(root);
    return new Element(
            root.getContext(),
            root.getName(),
            root.getMetadata(),
            simplify(root.getSubnodes()));
  }

  @SuppressWarnings("PMD.MissingBreakInSwitch")
  private Regexp<StructuralAbstractNode> simplify(final Regexp<StructuralAbstractNode> regexp) {
    switch (regexp.getType()) {
      case TOKEN:
        if (StructuralNodeType.ELEMENT.equals(regexp.getContent().getType())) {
          return Regexp.<StructuralAbstractNode>getToken(simplify((Element)regexp.getContent()), regexp.getInterval());
        }
        return regexp;
      case ALTERNATION:
      case CONCATENATION:
        if (regexp.getChildren().size() == 1) {
          return simplify(regexp.getChild(0));
        }
        final List<Regexp<StructuralAbstractNode>> children = new ArrayList<Regexp<StructuralAbstractNode>>(regexp.getChildren().size());
        for (final Regexp<StructuralAbstractNode> child : regexp.getChildren()) {
          children.add(simplify(child));
        }
        return new Regexp<StructuralAbstractNode>(null, children, regexp.getType(), regexp.getInterval());
      default: return regexp;
    }
  }

}
