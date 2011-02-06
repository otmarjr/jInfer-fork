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

import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import java.util.HashMap;
import java.util.Map;

/**
 *  Wrapper class for tag entity read by SAX parser.
 * @author reseto
 */
public class SAXDocumentElement {

  /**
   * Name of element in the schema, trimmed of namespace.
   * For example <code><xs:element name="Car" /></code>
   * the name <code>trimmedQName</code> will be 'element'.
   */
  private final String name;
  /**
   * All attributes of the element.
   */
  private final Map<String, SAXAttributeData> attrs;

  private boolean associated;

  /**
   * Constructs an instance with given name and empty attribute list.
   * @param nameWithNS Name of the schema element, should be set in lowercase.
   */
  public SAXDocumentElement(final String nameWithNS) {
    final String trimmedName = XSDUtility.trimNS(nameWithNS);
    if (BaseUtils.isEmpty(trimmedName)) {
      throw new IllegalArgumentException("XSD Document Element can't have empty or null name.");
    } else {
      this.name = trimmedName;
    }
    attrs = new HashMap<String, SAXAttributeData>();
    associated = false;
  }

  public Map<String, SAXAttributeData> getAttrs() {
    return attrs;
  }

  public String getName() {
    return name;
  }

  public String attributeNameValue() {
    if (attrs.containsKey("name")) {
      return attrs.get("name").getValue();
    } else if (attrs.containsKey("ref")) {
      return attrs.get("ref").getValue();
    } else {
      return null;
    }
  }

  public boolean isNamedComplexType() {
    final SAXAttributeData nameAttr = attrs.get("name");
    return (nameAttr != null && !nameAttr.getQName().equals("") && !nameAttr.getValue().equals("") && isComplexType()) ? true : false;
  }

  public boolean isComplexType() {
    return XSDTag.COMPLEXTYPE.getName().equals(name);
  }

  /**
   * Check if element is one of xs:choice, xs:sequence, xs:all.
   * @return true if it is, false otherwise.
   */
  public boolean isOrderIndicator() {
    return XSDTag.matchName(name).isOrderIndicator();
  }

  /**
   * Couples the current instance with a "named complex type".
   * This means that current instance is a direct successor of the <i>complexType</i> tag
   * and it should not create its own container for sub-nodes, but pass all sub-nodes to its parent.
   */
  public void associate() {
    this.associated = true;
  }

  public boolean isAssociated() {
    return associated;
  }

  public boolean isSchema() {
    return (name.equalsIgnoreCase("schema")) ? true : false;
  }
}
