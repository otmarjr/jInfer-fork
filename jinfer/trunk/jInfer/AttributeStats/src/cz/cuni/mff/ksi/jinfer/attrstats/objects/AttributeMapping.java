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
package cz.cuni.mff.ksi.jinfer.attrstats.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * Note that the image is stored non-uniquely.
 *
 * @author vektor
 */
public class AttributeMapping {

  private final AttributeMappingId id;

  private final List<String> image = new ArrayList<String>();

  /**
   * TODO vektor Comment!
   *
   * @param element
   * @param attribute
   */
  public AttributeMapping(final String element, final String attribute) {
    this.id = new AttributeMappingId(element, attribute);
  }

  /**
   * TODO vektor Comment!
   *
   * @param id
   */
  public AttributeMapping(final AttributeMappingId id) {
    this.id = id;
  }

  /**
   * TODO vektor Comment!
   *
   * @param element
   * @param attribute
   * @param image
   */
  public AttributeMapping(final String element, final String attribute, final List<String> image) {
    this.id = new AttributeMappingId(element, attribute);
    this.image.addAll(image);
  }

  /**
   * TODO vektor Comment!
   *
   * @param id
   * @param image
   */
  public AttributeMapping(final AttributeMappingId id, final List<String> image) {
    this.id = id;
    this.image.addAll(image);
  }

  public AttributeMappingId getId() {
    return id;
  }

  public String getElement() {
    return id.getElement();
  }

  public String getAttribute() {
    return id.getAttribute();
  }

  public List<String> getImage() {
    return image;
  }

  /**
   * Returns the size of the image of this mapping.
   *
   * @return Size of the image of this mapping, which is the number of all values
   * this mapping has.
   */
  public int size() {
    return image.size();
  }

}
