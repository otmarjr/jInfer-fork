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

import java.util.ArrayList;
import java.util.List;

/**
 * This is a representation of an attribute mapping, as described in the
 * article "Finding ID Attributes in XML Documents". It contains the mapping
 * itself (element and attribute name), as well as its image (all values the
 * attribute under the element ever has). Note that the image is stored as a
 * list (not a set), thus duplicate values are all stored.
 *
 * @author vektor
 */
public class AttributeMapping {

  private final AttributeMappingId id;

  private final List<String> image = new ArrayList<String>();

  /**
   * Minimal constructor. Image of this mapping will be created as empty.
   *
   * @param element Element name.
   * @param attribute Attribute name.
   */
  public AttributeMapping(final String element, final String attribute) {
    this.id = new AttributeMappingId(element, attribute);
  }

  /**
   * Minimal constructor. Image of this mapping will be created as empty.
   *
   * @param id Identifier of this mapping.
   */
  public AttributeMapping(final AttributeMappingId id) {
    this.id = id;
  }

  /**
   * Full constructor.
   *
   * @param element Element name.
   * @param attribute Attribute name.
   * @param image Attribute mapping image.
   */
  public AttributeMapping(final String element, final String attribute, final List<String> image) {
    this.id = new AttributeMappingId(element, attribute);
    this.image.addAll(image);
  }

  /**
   * Full constructor.
   *
   * @param id Identifier of this mapping.
   * @param image Attribute mapping image.
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

  @Override
  public String toString() {
    return "(" + id.getElement() + ", " + id.getAttribute() + ", " + image.size() + " values)";
  }

}
