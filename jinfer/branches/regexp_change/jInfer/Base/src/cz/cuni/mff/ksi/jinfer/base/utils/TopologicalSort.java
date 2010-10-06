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
package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.StructuralNodeType;
import java.util.ArrayList;
import java.util.List;

/**
 * Topological sorting of elements. In the DAG where sorting takes place,
 * elements are vertices and edges go from depending to dependant elements.
 * 
 * @author vektor
 */
public final class TopologicalSort {

  private final List<Element> elements;
  private final List<Element> ret = new ArrayList<Element>();
  private final List<Element> visited = new ArrayList<Element>();

  public TopologicalSort(final List<Element> elements) {
    this.elements = elements;
  }

  public List<Element> sort() {
    for (final Element e : elements) {
      visit(e);
    }
    return ret;
  }

  private void visit(final Element e) {
    for (final Element vis : visited) {
      if (vis == e) {
        return;
      }
    }

    visited.add(e);

    // visit all my subnodes
    for (final AbstractStructuralNode node : e.getSubnodes().getTokens()) {
      if (node.getType().equals(StructuralNodeType.ELEMENT)) {
        visit((Element) node);
      }
      // links between input elements might be torn, try to visit others too
      for (final Element el : elements) {
        if (el.getName().equalsIgnoreCase(node.getName())) {
          visit(el);
        }
      }
    }

    // output only original elements
    for (final Element original : elements) {
      if (e == original) {
        ret.add(e);
        break;
      }
    }
  }
}
