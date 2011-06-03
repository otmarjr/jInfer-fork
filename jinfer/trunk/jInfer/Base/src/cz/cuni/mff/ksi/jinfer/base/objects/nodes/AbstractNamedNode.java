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
package cz.cuni.mff.ksi.jinfer.base.objects.nodes;

import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.NamedNode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class implementing general features of all nodes in grammar structure.
 * Holds information about name, context, and metadata.
 *
 * By default is immutable, children can implement getMutable() function, to enable
 * creation of temporarily mutable instances, which have to be locked by
 * calling setImmutable() after all fields are set properly.
 *
 * @author anti
 */
public abstract class AbstractNamedNode implements NamedNode {

  /** Names of all elements along the path from root to this element (excluded). */
  private final List<String> context;
  /** Name of this node. */
  private String name;
  /** List of unspecific attributes - metadata assigned to this node. */
  private final Map<String, Object> metadata;
  /** Holds true when node is mutable, false when not. */
  protected boolean mutable;

  /**
   * Create new node given all members.
   *
   * @param context context of node in document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with node
   * @param mutable true iff node is to be created as mutable
   */
  protected AbstractNamedNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata, final boolean mutable) {
    this.context = context;
    this.name = name;
    this.metadata = metadata;
    this.mutable = mutable;
    checkConstraits();
  }

  /**
   * Public constructor, creates immutable node.
   *
   * @param context context of node in document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with node
   */
  public AbstractNamedNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata) {
    this(context, name, metadata, false);
  }

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

  /** Sets the name of node (if mutable).
   * Throws {@link IllegalStateException} when immutable.
   *
   * @param name new name of node
   */
  public void setName(final String name) {
    if (mutable) {
      this.name = name;
    } else {
      throw new IllegalStateException("Trying to change content of immutable NamedAbstractNode.");
    }
  }

  @Override
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  /** Check whether node is mutable
   *
   * @return true iff is mutable
   */
  public boolean isMutable() {
    return this.mutable;
  }

  /**
   * Set object to immutable mode (cannot be changed back to mutable).
   * Lock the node for changing.
   */
  public void setImmutable() {
    if (this.mutable) {
      mutable = false;
    } else {
      throw new IllegalStateException("Trying to set inmutable regexp, that is once inmutable.");
    }
    checkConstraits();
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

  private void checkConstraits() {
    if (context == null) {
      throw new IllegalArgumentException("Context has to be non-null.");
    }
    if (metadata == null) {
      throw new IllegalArgumentException("Metadata has to be non-null.");
    }
  }
}
