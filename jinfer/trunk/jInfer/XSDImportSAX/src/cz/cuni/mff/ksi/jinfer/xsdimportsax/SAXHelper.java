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

package cz.cuni.mff.ksi.jinfer.xsdimportsax;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import cz.cuni.mff.ksi.jinfer.xsdimportsax.utils.SAXDocumentElement;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Helper class for {@link DOMHandler }, provides convenience methods used during parsing.
 * It is best NOT to use these methods outside this package.
 * Please read package info.
 * @author reseto
 */
public final class SAXHelper {

  private SAXHelper() {}

  private static final Logger LOG = Logger.getLogger(SAXHelper.class);
  static {
    LOG.setLevel(XSDImportSettings.getLogLevel());
  }

  /**
   * Pseudo-unique name for the container elements that are pushed in contentStack
   * the name should be distict from every element name in the actual schema
   */
  private static final String CONTAINER_NAME = "__conTAIner__";

  /**
   * Prepare metadata of <i>attribute</i> tag stored in the parameter.
   * If the parameter has a defined attribute <i>use</i>, add this information to metadata.
   * @param el Instance of the wrapper class containing the <i>attribute</i> tag,
   * from which the metadata is extracted.
   * @return New metadata.
   */
  public static Map<String, Object> prepareAttributeMetadata(final SAXDocumentElement el) {
    final Map<String, Object> attrMeta = new HashMap<String, Object>();
    if (XSDUtility.REQUIRED.equals(el.getAttributeValue(XSDAttribute.USE))) {
      attrMeta.put(IGGUtils.REQUIRED, Boolean.TRUE);
    }
    //else if (XSDUtility.OPTIONAL.equals(el.getAttributeValue(XSDAttribute.USE))) {
    //  attrMeta.put(IGGUtils.REQUIRED, Boolean.FALSE);
    //}
    return attrMeta;
  }

  /**
   * Finds out if parameter is only a temporary container with the name set by CONTAINER_NAME constant.
   * @param elem Instance to be examined.
   * @return True if {@code elem } is a container, false otherwise.
   */
  public static boolean isContainer(final Element elem) {
    return (elem.getName().equals(CONTAINER_NAME)
            && elem.getMetadata().containsKey(CONTAINER_NAME)
            && elem.getMetadata().get(CONTAINER_NAME).equals(Boolean.TRUE));
  }

  /**
   * Prepares the name for a rule by extracting data from {@code el }.
   * Resulting name is taken from attributes of the tag.
   * Attribute <i>name</i> has priority before attribute <i>ref</i>.
   * If it is a container, special internal value for the name is used.
   * @param el Instance wrapping the tag.
   * @param isContainer Indicates if the name is prepared only for a container.
   * @return New name for the rule.
   */
  public static String prepareElementName(final SAXDocumentElement el, final boolean isContainer) {
    if (isContainer) {
      return CONTAINER_NAME;
    } else if (el.hasAttribute(XSDAttribute.NAME)) {
      return el.getAttributeValue(XSDAttribute.NAME);
    } else if (el.hasAttribute(XSDAttribute.REF)) {
      return el.getAttributeValue(XSDAttribute.REF);
    } else {
      throw new XSDException("Invalid schema. Tag " + el.getName() + " has no name attribute specified.");
    }
  }

  /**
   * Prepares the metadata for a rule by extracting information from {@code el}.
   * If it is not a container, {@link IGGUtils#ATTR_FROM_SCHEMA } is added to metadata.
   * If {@code el } has <i>ref</i> attribute specified, {@link IGGUtils#METADATA_SENTINEL } is added to metadata.
   * @param el Instance wrapping the tag.
   * @param isContainer Indicates if the metadata is prepared only for a container.
   * @return New metadata.
   */
  public static Map<String, Object> prepareElementMetadata(final SAXDocumentElement el, final boolean isContainer) {
    final Map<String, Object> metadata = new HashMap<String, Object>();
    if (isContainer) {
      metadata.put(CONTAINER_NAME, Boolean.TRUE);
    } else {
      // this is a normal element
      if (el.hasAttribute(XSDAttribute.REF)) {
        // tags with ref attribute don't have any contents, so it's a sentinel
        metadata.putAll(IGGUtils.METADATA_SENTINEL);
      }
      metadata.putAll(IGGUtils.ATTR_FROM_SCHEMA);
    }
    return metadata;
  }

  /**
   * Finalizes the element, makes it immutable.
   * Should be called only when all type dependencies have been resolved.
   * @param elem Element to finalize.
   */
  public static void finalizeElement(Element elem) {
    if (RegexpType.LAMBDA.equals(elem.getSubnodes().getType())) {
      XSDUtility.setLambda(elem);
      return;
    }
    if (elem.getSubnodes().getInterval() == null) {
      elem.getSubnodes().setInterval(RegexpInterval.getOnce());
    }
    if (elem.getSubnodes().getChildren().isEmpty()
        && elem.getSubnodes().getType() == null) {
      // make it a lambda
      XSDUtility.setLambda(elem);
      return;
    } else if (elem.getSubnodes().getType() == null) {
      LOG.warn("Element " + elem.getName() + " had no defined regexp type, but contained sub-nodes. Setting type to concatenation.");
      elem.getSubnodes().setType(RegexpType.CONCATENATION);
    }
    if (elem.isMutable()) {
      elem.setImmutable();
    }
  }
}
