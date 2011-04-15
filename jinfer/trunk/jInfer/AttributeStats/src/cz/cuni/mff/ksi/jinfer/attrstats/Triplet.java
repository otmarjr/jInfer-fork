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
package cz.cuni.mff.ksi.jinfer.attrstats;

/**
 * TODO vektor Provisional name
 * TODO vektor Comment!
 * 
 * @author vektor
 */
public class Triplet implements Comparable<Triplet> {
  
  private final String element;
  private final String attribute;
  private final String value;
  
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
  public int compareTo(Triplet o) {
    if (o == null) {
      return 1;
    }
    final int cmpElement = element.compareTo(o.getElement());
    if (cmpElement != 0) {
      return cmpElement;
    }
    return attribute.compareTo(o.getAttribute());
  }
  
}
