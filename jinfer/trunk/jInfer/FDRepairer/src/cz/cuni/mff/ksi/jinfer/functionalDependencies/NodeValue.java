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
 *
 * @author sviro
 */
public class NodeValue {
  private final String changedValue;
  private final boolean newValue;

  public NodeValue(String changedValue, boolean newValue) {
    this.changedValue = changedValue;
    this.newValue = newValue;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof NodeValue)) {
      return false;
    }

    NodeValue nodeValue = (NodeValue) obj;
    return this.newValue == nodeValue.isNewValue() || this.getChangedValue().equals(nodeValue.getChangedValue());
  }

  public boolean isNewValue() {
    return newValue;
  }

  public String getChangedValue() {
    return changedValue;
  }
  
  
  
}
