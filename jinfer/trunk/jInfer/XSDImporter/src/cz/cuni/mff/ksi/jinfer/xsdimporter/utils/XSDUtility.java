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

package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class providing convenience methods for importing XSD Schema documents.
 * @author reseto
 */
public final class XSDUtility {

  private XSDUtility() {}

  /**
   * String for attribute value of <i>maxOccurs</i> attribute, indicating unlimited occurrence.
   */
  public static final String UNBOUNDED = "unbounded";
  /**
   * String for attribute value of <i>use</i> attribute, indicating compulsory usage.
   */
  public static final String REQUIRED = "required";
  /**
   * String for attribute value of <i>use</i> attribute, indicating optional usage.
   */
  public static final String OPTIONAL = "optional";
  /**
   * Name of {@link SimpleData } children of TOKEN Elements, holding the simple data type information for XSD exporter.
   */
  public static final String SIMPLE_DATA_NAME = "__SD";

  /**
   * Extract all rules from a subtree of <code>Element</code>, not including the root itself.
   * @param root Root of the subtree from which we extract the rules.
   * @param elementRules List where all the rules are stored.
   * @throws InterruptedException When user interrupts operation.
   */
  public static void getRulesFromElement(final Element root, final List<Element> elementRules) throws InterruptedException {
    for (AbstractStructuralNode node : root.getSubnodes().getTokens()) {
      InterruptChecker.checkInterrupt();
      if (node.isElement()) {
        elementRules.add((Element) node);
        getRulesFromElement((Element) node, elementRules);
      }
    }
  }

  /**
   * Trims (cuts) namespace prefix from the beginning of element tag and returns it in original case.
   * @param qName String to be trimmed of its prefix.
   * @return QName without namespace prefix.
   */
  public static String trimNS(final String qName) {
    if (BaseUtils.isEmpty(qName)) {
      return qName;
    }
    return qName.substring(qName.lastIndexOf(':') + 1);
  }

  /**
   * Check if {@code name } is unqualified (does not contain a colon).
   * @param name Name to be checked.
   * @return {@code true } if {@code name } has no colon (':').
   */
  public static boolean isNCName(final String name) {
    if (BaseUtils.isEmpty(name)) {
      return false;
    }
    return (name.lastIndexOf(':') == -1);
  }

  /**
   * Create metadata for an element. This should contain information about element interval
   * and information that the element was parsed from schema.
   * @param interval Valid interval specifying element occurrence limits.
   * @return New metadata.
   */
  public static Map<String, Object> prepareMetadata(final  RegexpInterval interval) {
    final Map<String, Object> metadata = new HashMap<String, Object>();
    metadata.putAll(IGGUtils.ATTR_FROM_SCHEMA);
    metadata.put(XSDAttribute.MINOCCURS.getMetadataName(), String.valueOf(interval.getMin()));
    if (interval.isUnbounded()) {
      metadata.put(XSDAttribute.MAXOCCURS.getMetadataName(), UNBOUNDED);
    } else {
      metadata.put(XSDAttribute.MAXOCCURS.getMetadataName(), String.valueOf(interval.getMax()));
    }
    return metadata;
  }

  /**
   * Set the parameter to {@link RegexpType#LAMBDA } element,
   * with all the valid constraints and make it immutable.
   * @param ret Element to set as LAMBDA.
   */
  public static void setLambda(final Element ret) {
      ret.getSubnodes().setInterval(null);
      ret.getSubnodes().setContent(null);
      ret.getSubnodes().setType(RegexpType.LAMBDA);
      ret.setImmutable();
  }
}
