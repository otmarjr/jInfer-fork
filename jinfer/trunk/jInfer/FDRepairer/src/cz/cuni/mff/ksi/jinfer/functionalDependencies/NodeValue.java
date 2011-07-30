/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

/**
 * Class representing value modification of the {@link RXMLTree} node.
 * @author sviro
 */
public class NodeValue {
  private final String changedValue;
  private final boolean newValue;

  /**
   * Default constructor. As a parameter gets new value of the node and a flag describing if
   * this value is newly generated.
   * @param changedValue New value of the node.
   * @param newValue flag representing newly generated value.
   */
  public NodeValue(final String changedValue, final boolean newValue) {
    this.changedValue = changedValue;
    this.newValue = newValue;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof NodeValue)) {
      return false;
    }

    final NodeValue nodeValue = (NodeValue) obj;
    return this.newValue == nodeValue.isNewValue() || this.getChangedValue().equals(nodeValue.getChangedValue());
  }

  @Override
  public int hashCode() {
    int hash = 11;
    hash = 41 * hash + (this.changedValue != null ? this.changedValue.hashCode() : 0);
    hash = 41 * hash + (this.newValue ? 1 : 0);
    return hash;
  }

  /**
   * Check if the changed node value is newly generated.
   * @return true if the value is newly generated, otherwise return false.
   */
  public boolean isNewValue() {
    return newValue;
  }

  /**
   * Get changed value of the node.
   * @return Changed value of the node.
   */
  public String getChangedValue() {
    return changedValue;
  }
}