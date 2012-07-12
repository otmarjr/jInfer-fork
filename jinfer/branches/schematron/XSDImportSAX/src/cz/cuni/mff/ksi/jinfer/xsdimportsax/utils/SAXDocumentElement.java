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

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDOccurences;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class for tags read by SAX parser.
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
   * Checks if current instance has a non empty attribute specified by the parameter.
   * @return {@code true} if there is an attribute with the specified name defined and it is not empty, {@code false} otherwise.
   */
  public boolean hasAttribute(final XSDAttribute attribute) {
    final SAXAttributeData nameAttr = attrs.get(attribute.toString());
    return (nameAttr != null && !nameAttr.getQName().equals("") && !nameAttr.getValue().equals(""));
  }

  /**
   * Gets the value of specified attribute.
   * @return Value of specified attribute or empty string if attribute value is absent.
   */
  public String getAttributeValue(final XSDAttribute attribute) {
    if (!hasAttribute(attribute)) {
      return "";
    } else {
      return attrs.get(attribute.toString()).getValue();
    }
  }

  /**
   * Returns value of <i>name</i> attribute or <i>ref</i> attribute if any of them can be used.
   * @return Value of either attribute or empty string if neither value is present.
   */
  public String getNameOrRefValue() {
    if (hasAttribute(XSDAttribute.NAME)) {
      return getAttributeValue(XSDAttribute.NAME);
    } else if (hasAttribute(XSDAttribute.REF)) {
      return getAttributeValue(XSDAttribute.REF);
    } else {
      return "";
    }
  }

  /**
   * Couples the current instance with a "named complex type".
   * This means that current instance is a direct successor of the <i>complexType</i> tag
   * and it should not create its own container for sub-nodes, but pass all sub-nodes to its parent.
   * @throws XSDException When current instance is not an order indicator.
   */
  public void associate() throws XSDException {
    if (isOrderIndicator()) {
      this.associated = true;
    } else {
      throw new XSDException("Unsupported operation. Trying to couple an unsupported element with its parent.");
    }
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

  /**
   * Check if tag of current instance is one of <i>choice</i>, <i>sequence</i>, <i>all</i>.
   * @return true if it is, false otherwise.
   */
  public boolean isOrderIndicator() {
    return tag.isOrderIndicator();
  }

  /**
   * Match tag of current instance to a {@link RegexpType }.
   * @return {@link RegexpType#ALTERNATION },
   * {@link RegexpType#CONCATENATION },
   * {@link RegexpType#PERMUTATION }
   * or {@code null } if instance is not an order indicator.
   */
  public RegexpType determineRegexpType() {
    switch (tag) {
      case ALL:
        return RegexpType.PERMUTATION;
      case CHOICE:
        return RegexpType.ALTERNATION;
      case SEQUENCE:
        return RegexpType.CONCATENATION;
      default:
        // if it's a regular element, we don't know the type yet
        // this has to stay null, because the element can have a type defined in some named CType
        return null;
    }
  }
  
  /**
   * Create an interval using attributes <i>minOccurs</i> and <i>maxOccurs</i> of current instance.
   * @return Valid or default interval.
   * @see XSDOccurences#createInterval(java.lang.String, java.lang.String)
   */
  public RegexpInterval determineInterval() {
    final String minOccurrence = getAttributeValue(XSDAttribute.MINOCCURS);
    final String maxOccurrence = getAttributeValue(XSDAttribute.MAXOCCURS);
    return XSDOccurences.createInterval(minOccurrence, maxOccurrence);
  }

}
