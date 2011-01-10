/*
 *  Copyright (C) 2011 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

/**
 *
 * @author reseto
 */
public enum XSDAttribute {

  NAME("name"),
  REF("ref"),
  MINOCCURS("minOccurs"),
  MAXOCCURS("maxOccurs"),
  INVALID("InVAlid");

  private final String name;

  private XSDAttribute(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  public String getName() {
    return name;
  }

  public static XSDAttribute matchName(final String name) {
    for (XSDAttribute att : XSDAttribute.values()) {
      if (name.equals(att.getName())) {
        return att;
      }
    }
    return INVALID;
  }

}
