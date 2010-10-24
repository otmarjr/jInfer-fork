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
package cz.cuni.mff.ksi.jinfer.xsdimportsax.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author reseto
 */
public class SAXDocumentElement {

  /**
   * Name of element in the schema, trimmed of namespace.
   * For example <code><xs:element name="Car" /></code>
   * the name <code>trimmedQName</code> will be 'element'.
   */
  private final String trimmedQName;
  /**
   * All attributes of the element.
   */
  private final Map<String, SAXAttributeData> attrs;

  /* determines if the current XSDDocElem is a direct successor
  of named complex type (so it's a sequence/all/choice or complexContent/ext)
   */
  private boolean associated;

  /**
   * Constructs an instance with given name and empty attribute list.
   * @param trimmedQName Name of the schema element, should be set in lowercase.
   */
  public SAXDocumentElement(final String trimmedQName) {
    if (trimmedQName == null || trimmedQName.equals("")) {
      throw new IllegalArgumentException("XDS Document Element: can't have empty or null element name!");
    } else {
      this.trimmedQName = trimmedQName;
    }
    attrs = new HashMap<String, SAXAttributeData>();
    associated = false;
  }

  public Map<String, SAXAttributeData> getAttrs() {
    return attrs;
  }

  public String getName() {
    return trimmedQName;
  }

  public String attributeNameValue() {
    return attrs.get("name").getValue();
  }

  public boolean isNamedComplexType() {
    final SAXAttributeData nameAttr = attrs.get("name");
    return (nameAttr != null && !nameAttr.getQName().equals("") && !nameAttr.getValue().equals("") && isComplexType()) ? true : false;
  }

  public boolean isComplexType() {
    return (trimmedQName.equalsIgnoreCase("complextype")) ? true : false;
  }

  /**
   * Check if element is one of xs:choice, xs:sequence, xs:all.
   * @return true if it is, false otherwise.
   */
  public boolean isOrderIndicator() {
    return (trimmedQName.equalsIgnoreCase("choice")
            || trimmedQName.equalsIgnoreCase("all")
            || trimmedQName.equalsIgnoreCase("sequence")) ? true : false;
  }

  public void associate() {
    this.associated = true;
  }

  public boolean isAssociated() {
    return associated;
  }
}
