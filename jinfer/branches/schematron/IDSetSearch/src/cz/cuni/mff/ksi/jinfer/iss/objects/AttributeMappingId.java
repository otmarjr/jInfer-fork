/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss.objects;

/**
 * This is a representation of an attribute mapping identifier, as described in
 * the article "Finding ID Attributes in XML Documents". It contains the
 * element and attribute names. This representation follows the immutable object
 * design pattern.
 *
 * @author vektor
 */
public class AttributeMappingId implements Comparable<AttributeMappingId> {

  private final String element;

  private final String attribute;

  /**
   * Full constructor.
   *
   * @param element Element name.
   * @param attribute Attribute name.
   */
  public AttributeMappingId(final String element, final String attribute) {
    this.element = element;
    this.attribute = attribute;
  }

  public String getElement() {
    return element;
  }

  /**
   * Checks whether this object represents an attribute mapping of the specified
   * type (whether the element has the speficied name). This check is
   * case-sensitive.
   *
   * @param type Type to check.
   * @return <code>true</code> if this mapping is of the specified type,
   * <code>false</code> otherwise.
   */
  public boolean isType(final String type) {
    return getElement().equals(type);
  }

  public String getAttribute() {
    return attribute;
  }

  /**
   * Two attribute mapping IDs are equal iff their element and attribute names
   * are equal (case sensitive).
   *
   * @param obj Other object to check equality.
   * @return <code>true</code> if the other object is {@link AttributeMappingId}
   * and its element and attribute names are equal to ones in this mapping,
   * <code>false<code> otherwise.
   */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof AttributeMappingId)) {
      return false;
    }
    final AttributeMappingId other = (AttributeMappingId) obj;
    return element.equals(other.getElement()) && attribute.equals(other.getAttribute());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 43 * hash + (this.element != null ? this.element.hashCode() : 0);
    hash = 43 * hash + (this.attribute != null ? this.attribute.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "(" + element + "," + attribute + ")";
  }

  @Override
  public int compareTo(final AttributeMappingId o) {
    final int cmpElement = element.compareTo(o.getElement());
    if (cmpElement != 0) {
      return cmpElement;
    }
    return attribute.compareTo(o.getAttribute());
  }

}
