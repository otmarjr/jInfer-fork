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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;

/**
 * A class representing the triplet <code>(element name, attribute name,
 * attribute value)</code> for use in attribute mapping.
 *
 * @author vektor
 */
// TODO vektor Provisional name
public class Triplet implements Comparable<Triplet> {

  private final String element;
  private final String attribute;
  private final String value;

  /**
   * Full constructor.
   *
   * @param element {@link Element} name.
   * @param attribute {@link Attribute} name.
   * @param value {@link Attribute} value.
   */
  public Triplet(final String element, final String attribute, final String value) {
    if (element == null || attribute == null || value == null) {
      throw new IllegalArgumentException("All three arguments must be not null.");
    }
    this.element = element;
    this.attribute = attribute;
    this.value = value;
  }

  public String getElement() {
    return element;
  }

  public String getAttribute() {
    return attribute;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "(" + getElement() + ", " + getAttribute() + ", " + getValue() + ")";
  }

  /**
   * Compares to another {@link Triplet}. First of all, <code>null</code> is
   * supposed to be the last. Second, the comparison is done first on element name,
   * then on attribute name (it is assumed that the pair <code>(element name,
   * attribute name)</code> is a unique key).
   *
   * @param o The other {@link Triplet} to be compared. May be null.
   * @return
   */
  @Override
  public int compareTo(final Triplet o) {
    if (o == null) {
      return 1;
    }
    final int cmpElement = element.compareTo(o.getElement());
    if (cmpElement != 0) {
      return cmpElement;
    }
    return attribute.compareTo(o.getAttribute());
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Triplet)) {
      return false;
    }
    final Triplet other = (Triplet) obj;
    return element.equals(other.getElement())
            && attribute.equals(other.getAttribute())
            && value.equals(other.getValue());
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + (this.element != null ? this.element.hashCode() : 0);
    hash = 71 * hash + (this.attribute != null ? this.attribute.hashCode() : 0);
    hash = 71 * hash + (this.value != null ? this.value.hashCode() : 0);
    return hash;
  }

}
