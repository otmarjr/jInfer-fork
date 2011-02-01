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
 * Class holding known attributes from XSD Schema specification, that are used by import parsers.
 * Special value <code>INVALID</code> is reserved for possible unknown attributes.
 * Every value has its case sensitive name which should be used for comparison with nodes from schema.
 * @author reseto
 */
public enum XSDAttribute {

  /**
   * Most common attribute <code>name</code>.
   * For example, every <i>complexType</i> directly under <i>schema</i> tag must have this attribute defined.
   */
  NAME("name"),
  /**
   * Attribute <code>ref</code>, used to reference another element.
   */
  REF("ref"),
  /**
   * Attribute <code>type</code>, can be used to define that element is of a built-in type, or that it references a <i>complexType</i>.
   */
  TYPE("type"),
  /**
   * Attribute <code>use</code>, specifies if the usage of a schema attribute is compulsory or optional.
   */
  USE("use"),
  /**
   * Attribute <code>minOccurs</code>, sets the minimum number of occurrences for element.
   */
  MINOCCURS("minOccurs"),
  /**
   * Attribute <code>maxOccurs</code>, sets the maximum number of occurrences for element.
   */
  MAXOCCURS("maxOccurs"),
  /**
   * Attribute <code>substitutionGroup</code>, specifies the name of an element that can be substituted with the element that has this attribute.
   */
  SUBSTITUTION("substitutionGroup"),
  /**
   * Special value meaning that the parsed attribute is not known.
   */
  INVALID("InVAlid");

  private final String name;
  private static final String METADATA_PREFIX = "XSDSchema.";

  private XSDAttribute(final String name) {
    this.name = name;
  }

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
   * Determine if the parameter can match any of the enum values.
   * Operation is case sensitive.
   * @param name String to match up with an attribute.
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
