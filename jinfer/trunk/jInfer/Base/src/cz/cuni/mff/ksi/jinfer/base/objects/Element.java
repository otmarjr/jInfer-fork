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
import java.util.List;

/**
 * Class representing a XML element.
 * 
 * @author vitasek
 */
public class Element extends AbstractNode {

  /** List of all subnodes of this element, in the same order as in the
   * document. */
  private final Regexp<AbstractNode> subnodes;

  public Element(final List<String> context,
          final String name,
          final List<Pair<String, Object>> attributes,
          final Regexp<AbstractNode> subnodes) {
    super(context, name, NodeType.ELEMENT, attributes);
    this.subnodes = subnodes;
  }

  public Regexp<AbstractNode> getSubnodes() {
    return subnodes;
  }

  @Override
  public String toString() {
    final StringBuilder ret = new StringBuilder(super.toString());
    ret.append('\n').append(subnodes.toString());
    return ret.toString();
  }
}
