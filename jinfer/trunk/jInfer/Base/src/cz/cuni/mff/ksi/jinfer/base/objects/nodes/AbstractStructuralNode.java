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
package cz.cuni.mff.ksi.jinfer.base.objects.nodes;

import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.StructuralNode;
import java.util.List;
import java.util.Map;

/**
 * Class representing a XML node (rule in grammar): element and text node.
 * 
 * @author vektor
 */
public abstract class AbstractStructuralNode extends AbstractNamedNode implements StructuralNode {

  /** Create new structural node given all members.
   *
   * @param context context of the node in document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with node
   * @param mutable whether node is to be created as mutable
   */
  protected AbstractStructuralNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata, final boolean mutable) {
    super(context, name, metadata, mutable);
  }

  /**
   * Create new immutable structural node.
   * 
   * @param context context of the node in document (if any)
   * @param name name of the node
   * @param metadata any metadata associated with node
   */
  protected AbstractStructuralNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata) {
    super(context, name, metadata);
  }

  @Override
  public abstract StructuralNodeType getType();

  @Override
  public boolean isElement() {
    return StructuralNodeType.ELEMENT.equals(getType());
  }

  @Override
  public boolean isSimpleData() {
    return StructuralNodeType.SIMPLE_DATA.equals(getType());
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(super.toString());
    sb.append(": ");
    sb.append(getType());
    return sb.toString();
  }
}
