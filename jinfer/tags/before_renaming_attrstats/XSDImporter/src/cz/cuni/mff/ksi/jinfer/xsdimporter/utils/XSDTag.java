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
 * Enumeration of all known tags for the purpose of parsing XSD Schemas.
 * <p>
 * XSD Schema specifies several names for entities that can occur within a schema document.
 * We refer to these entities as "tags" to avoid confusion with different types of elements used in jInfer.
 * Tags have case sensitive names and do NOT contain any namespace prefix.
 * Additional <code>INVALID</code> tag is provided to distinguish an unknown tag name.
 * </p>
 * For basic information on XSD Schema tags 
 * visit {@link http://www.w3schools.com/schema/schema_elements_ref.asp }.
 * @author reseto
 */
public enum XSDTag {

  /**
   * Tag <i>schema</i>, root tag of every XSD Schema document.
   */
  SCHEMA("schema"),
  /**
   * Tag <i>element</i>, most common entity in XSD Schema document.
   */
  ELEMENT("element"),
  /**
   * Tag <i>complexType</i>, wrapping contents of <i>element</i> tag.
   */
  COMPLEXTYPE("complexType"),
  /**
   * Tag <i>sequence</i>, indicates that contents must appear in precise order.
   */
  SEQUENCE("sequence"),
  /**
   * Tag <i>choice</i>, allows only one of the elements contained in the declaration to be present.
   */
  CHOICE("choice"),
  /**
   * Tag <i>all</i>, indicates that contents may appear in any order.
   */
  ALL("all"),
  /**
   * Tag <i>attribute</i>, defines an attribute.
   */
  ATTRIBUTE("attribute"),
  //ATTGROUP("attributeGroup"),
  //SIMPLECON("simpleContent"),
  //COMPLEXCON("complexContent"),
  /**
   * The redefine element redefines simple and complex types, groups, and attribute groups from an external schema.
   */
  REDEFINE("redefine"),
  /**
   * Special value meaning that the parsed tag is not known.
   */
  INVALID("INValid");

  private final String name;

  private XSDTag(final String name) {
    this.name = name;
  }

  /**
   * Get the case sensitive name of current tag.
   * @return Case sensitive name of tag.
   */
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
   * Determine if the parameter can match any of the tags.
   * Operation is case sensitive.
   * @param name A name to match up with a tag.
   * @return Corresponding tag or <code>INVALID</code> when such tag is not known.
   */
  public static XSDTag matchName(final String name) {
    for (XSDTag tag : XSDTag.values()) {
      if (tag.getName().equals(name)) {
        return tag;
      }
    }
    return INVALID;
  }

  /**
   * Convenience method, trims the namespace prefix beforehand.
   * Determine if the parameter can match any of the tags.
   * @param name A name to match up with a tag.
   * @return Corresponding tag or <code>INVALID</code> when such tag is not known.
   * @see XSDUtility#trimNS(java.lang.String)
   */
  public static XSDTag matchNameTrimNS(final String name) {
    return matchName(XSDUtility.trimNS(name));
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
