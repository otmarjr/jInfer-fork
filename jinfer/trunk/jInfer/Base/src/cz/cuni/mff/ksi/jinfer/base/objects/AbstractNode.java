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

/**
 * Class representing a XML node (rule in grammar).
 * 
 * @author vektor
 */
public abstract class AbstractNode {

  /** Names of all elements along the path from root to this element (excluded). */
  private final List<String> context;
  /** Name of this node. */
  private final String name;
  /** Type of this node. */
  private final NodeType type;
  /** List of unspecific attributes assigned to this node. */
  private final List<Pair<String, Object>> attributes;

  public AbstractNode(final List<String> context,
          final String name,
          final NodeType type,
          final List<Pair<String, Object>> attributes) {
    this.context = context;
    this.name = name;
    this.type = type;
    this.attributes = attributes;
  }

  public List<String> getContext() {
    return context;
  }

  public String getName() {
    return name;
  }

  public NodeType getType() {
    return type;
  }

  public List<Pair<String, Object>> getAttributes() {
    return attributes;
  }

  @Override
  public String toString() {
    final StringBuilder ret = new StringBuilder();
    // complete context
    for (final String element : context) {
      ret.append(element).append('/');
    }
    // + name
    ret.append(name);
    // type
    ret.append(": ").append(type);
    return ret.toString();
  }

}
