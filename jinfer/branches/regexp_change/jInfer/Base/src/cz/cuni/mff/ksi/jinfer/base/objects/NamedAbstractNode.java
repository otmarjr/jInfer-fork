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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public abstract class NamedAbstractNode implements NamedNode {
  /** Names of all elements along the path from root to this element (excluded). */
  private final List<String> context;
  /** Name of this node. */
  private String name;
  /** List of unspecific attributes - metadata assigned to this node. */
  private final Map<String, Object> metadata;
  protected boolean mutable;

  protected NamedAbstractNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata, boolean mutable) {
    this.context = context;
    this.name = name;
    this.metadata = metadata;
    this.mutable= mutable;
  }

  public NamedAbstractNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata) {
    this(context, name, metadata, false);
  }

  // TODO vektor + anti immutable?
  @Override
  public List<String> getContext() {
    if (context == null) {
      return null;
    }
    if (mutable) {
      return context;
    }
    return Collections.unmodifiableList(context);
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (mutable) {
      this.name= name;
    } else {
      throw new IllegalStateException("Trying to change content of immutable NamedAbstractNode.");
    }
  }

  // TODO vektor + anti immutable?
  @Override
  public Map<String, Object> getMetadata() {
    if (metadata == null) {
      return null;
    }
    if (mutable) {
      return metadata;
    }
    return Collections.unmodifiableMap(metadata);
  }

  public void setImmutable() {
    if (this.mutable) {
      this.mutable = false;
    } else {
      throw new IllegalStateException("Trying to set inmutable regexp, that is once inmutable.");
    }
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
