/*
 *  Copyright (C) 2010 reseto
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
 * All possible tags (i.e. element names) defined in XSD Schema used by XSD parsers of jInfer.
 * With one additional <code>INVALID</code> tag, to distinguish errors.
 * @author reseto
 */
public enum XSDTag {

  SCHEMA("schema"),
  ELEMENT("element"),
  COMPLEXTYPE("complexType"),
  SEQUENCE("sequence"),
  CHOICE("choice"),
  ALL("all"),
  ATTRIBUTE("attribute"),
  ATTGROUP("attributeGroup"),
  SIMPLECON("simpleContent"),
  COMPLEXCON("complexContent"),
  REDEFINE("redefine"),
  INVALID("INValid");

  private final String name;

  private XSDTag(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * Get the case sensitive name of current tag.
   * @return Case sensitive name of tag.
   */
  public String getName() {
    return name;
  }

  /**
   * Determine if the parameter can match any of the enum values.
   * Operation is case sensitive.
   * @param name String to match up with a tag.
   * @return Corresponding tag or <code>INVALID</code> when such tag is not known.
   */
  public static XSDTag matchName(final String name) {
    for (XSDTag tag : XSDTag.values()) {
      if (name.equals(tag.getName())) {
        return tag;
      }
    }
    return INVALID;
  }

  /**
   * Check if tag is one of <code>SEQUENCE</code>, <code>CHOICE</code> or <code>ALL</code>.
   * @param tag Tag to be checked.
   * @return <code>true</true> if tag indicated and order.
   */
  public static boolean isOrderIndicator(final XSDTag tag) {
    if (tag == SEQUENCE || tag == CHOICE || tag == ALL) {
      return true;
    }
    return false;
  }

  /**
   * Check if tag is one of <code>SEQUENCE</code>, <code>CHOICE</code> or <code>ALL</code>.
   * @return <code>true</code> if tag indicated and order.
   */
  public boolean isOrderIndicator() {
    if (this == SEQUENCE || this == CHOICE || this == ALL) {
      return true;
    }
    return false;
  }
}
