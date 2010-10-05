/*
 *  Copyright (C) 2010 anti
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
import java.util.Map;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class NamedAbstractNode implements NamedNode {
  /** Names of all elements along the path from root to this element (excluded). */
  private final List<String> context;
  /** Name of this node. */
  private final String name;
  /** List of unspecific attributes - metadata assigned to this node. */
  private final Map<String, Object> metadata;

  public NamedAbstractNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata) {
    this.context = context;
    this.name = name;
    this.metadata = metadata;
  }

  // TODO vektor + anti immutable?
  @Override
  public List<String> getContext() {
    return context;
  }

  @Override
  public String getName() {
    return name;
  }

  // TODO vektor + anti immutable?
  @Override
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  @Override
  public String toString() {
    final StringBuilder ret = new StringBuilder();
    // complete context
    if (context != null) {
      for (final String element : context) {
        ret.append(element).append('/');
      }
    }
    // + name
    ret.append(name);
    return ret.toString();
  }
}
