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
  private final XSDTag tag;
  /**
   * All attributes of the element.
   */
  private final Map<String, SAXAttributeData> attrs;

  private boolean associated;

  /**
   * Constructs a new instance with empty attribute list.
   * {@code QName} is trimmed of its namespace prefix.
   * @param QName Name of the schema element, should be set in lowercase.
   */
  public SAXDocumentElement(final String QName) {
    final String trimmedName = XSDUtility.trimNS(QName);
    if (BaseUtils.isEmpty(trimmedName)) {
      throw new IllegalArgumentException("XSD Document Element can't have empty or null name.");
    } else {
      this.name = trimmedName;
      this.tag = XSDTag.matchName(name);
    }
    attrs = new HashMap<String, SAXAttributeData>();
    associated = false;
  }

  /**
   * Returns the whole attribute list for this tag.
   * @return Attribute list of this tag.
   */
  public Map<String, SAXAttributeData> getAttrs() {
    return attrs;
  }

  /**
   * Returns case sensitive name without namespace prefix.
   * @return Name of this instance.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns tag matching the name.
   * @return Valid tag or {@code INVALID }
   */
  public XSDTag getTag() {
    return tag;
  }

  /**
   * Returns the value of attribute <i>name</i> if such an attribute is in the list.
   * @return Value of attribute <i>name</i>.
   */
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

  /**
   * Check if element is one of xs:choice, xs:sequence, xs:all.
   * @return true if it is, false otherwise.
   */
  public boolean isOrderIndicator() {
    return tag.isOrderIndicator();
  }

  /**
   * Couples the current instance with a "named complex type".
   * This means that current instance is a direct successor of the <i>complexType</i> tag
   * and it should not create its own container for sub-nodes, but pass all sub-nodes to its parent.
   */
  public void associate() {
    this.associated = true;
  }

  /**
   * Checks if current instance was paired with its parent's container.
   * @return {@code true } if {@code associate } method was called previously, {@code false } otherwise.
   */
  public boolean isAssociated() {
    return associated;
  }

  /**
   * Checks if instance is wrapping a <i>complexType</i> tag.
   * @return {@code true } if current tag is <i>complexType</i>.
   */
  public boolean isComplexType() {
    return XSDTag.COMPLEXTYPE.equals(tag);
  }

  /**
   * Checks if instance is wrapping a <i>schema</i> tag.
   * @return {@code true } if current tag is <i>schema</i>.
   */
  public boolean isSchema() {
    return XSDTag.SCHEMA.equals(tag);
  }
}
