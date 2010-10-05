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
import java.util.Map;

/**
 * Class representing a XML node (rule in grammar).
 * 
 * @author vektor
 */
public abstract class StructuralAbstractNode extends NamedAbstractNode implements StructuralNode {
  public StructuralAbstractNode(final List<String> context,
          final String name,
          final Map<String, Object> metadata, boolean mutable) {
    super(context, name, metadata, mutable);
  }

  public StructuralAbstractNode(final List<String> context,
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

  // TODO super.toString()
  @Override
  public String toString() {
    return super.toString() + ": " + getType().toString();
  }
}
