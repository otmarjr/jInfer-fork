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
 * Enumeration of known tag attributes for the purpose of parsing XSD Schemas.
 * <p>
 * XSD Schema specifies several tag attributes that can occur within a schema document.
 * Every value has its case sensitive name which should be used for comparison with tag attributes from schema.
 * Special value <code>INVALID</code> is reserved for possible unknown tag attributes.
 * @author reseto
 */
public enum XSDAttribute {

  /**
   * Attribute <i>name</i> of a tag.
   * For example, every <i>complexType</i> tag directly under <i>schema</i> tag must have this attribute defined.
   */
  NAME("name"),
  /**
   * Attribute <i>ref</i> of a tag.
   * Used to reference another <i>element</i> tag.
   */
  REF("ref"),
  /**
   * Attribute <i>type</i> of a tag.
   * Used to define that element is of a built-in type, or that it references a <i>complexType</i> tag.
   */
  TYPE("type"),
  /**
   * Attribute <i>use</i> of a tag.
   * Specifies if the usage of the corresponding <i>attribute</i> tag is compulsory or optional.
   */
  USE("use"),
  /**
   * Attribute <i>minOccurs</i> of a tag.
   * Sets the minimum number of occurrences for the corresponding <i>element</i> tag.
   */
  MINOCCURS("minOccurs"),
  /**
   * Attribute <i>maxOccurs</i> of a tag.
   * Sets the maximum number of occurrences for the corresponding <i>element</i> tag.
   */
  MAXOCCURS("maxOccurs"),
  /**
   * Attribute <i>substitutionGroup</i> of a tag.
   * Specifies the name of an <i>element</i> tag that can be substituted with the corresponding <i>element</i> tag.
   */
  //SUBSTITUTION("substitutionGroup"),
  /**
   * Special value meaning that the parsed attribute is not known.
   */
  INVALID("InVAlid");

  private final String name;
  private static final String METADATA_PREFIX = "XSDSchema.";

  private XSDAttribute(final String name) {
    this.name = name;
  }

  /**
   * Get the case sensitive name of current attribute.
   * @return Case sensitive name of attribute.
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Get the case sensitive name of current attribute.
   * @return Case sensitive name of attribute.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the prefixed name of current attribute to be used as a key in metadata.
   * Prefix distinguished that this metadata key is specifically from XSD Schema.
   * @return Prefixed attribute name.
   */
  public String getMetadataName() {
    return METADATA_PREFIX + name;
  }

  /**
   * Determine if the parameter can match any of the attributes.
   * Operation is case sensitive.
   * @param name A name to match up with an attribute.
   * @return Corresponding attribute or <code>INVALID</code> when such attribute is not known.
   */
  public static XSDAttribute matchName(final String name) {
    for (XSDAttribute att : XSDAttribute.values()) {
      if (att.getName().equals(name)) {
        return att;
      }
    }
    return INVALID;
  }

}
