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

package cz.cuni.mff.ksi.jinfer.xsdimportdom;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDOccurences;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;

/**
 *
 * @author reseto
 */
public final class DOMHelper {

  private DOMHelper() {}

 /**
   * String for attribute value of <i>maxOccurs</i> attribute, indicating unlimited occurrence.
   */
  private static final String UNBOUNDED = "unbounded";

  /**
   * Trims (cuts) namespace prefix from the beginning of element qName and returns it in original case.
   */
  protected static String trimNS(final String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1);
  }

  /**
   * Determine if given <code>Node</code> is in fact <i>org.w3c.dom.Element</i> and return a type cast if so.
   * @param node Entity to be checked.
   * @return Parameter cast to <code>org.w3c.dom.Element</code>, or <code>null</code>.
   * @see org.w3c.dom.Element
   */
  protected static org.w3c.dom.Element isDOMElement(final Node node) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      return (org.w3c.dom.Element) node;
    }
    return null;
  }

  protected static RegexpInterval determineInterval(final org.w3c.dom.Element currentNode, final RegexpInterval outerInterval) {
    final String minOccurence = currentNode.getAttribute(XSDAttribute.MINOCCURS.toString());
    final String maxOccurence = currentNode.getAttribute(XSDAttribute.MAXOCCURS.toString());
    RegexpInterval interval;
    if (outerInterval != null) {
      final RegexpInterval occurence = XSDOccurences.createInterval(minOccurence, maxOccurence);
      interval = RegexpInterval.intersectIntervals(outerInterval, occurence);
    } else {
      interval = XSDOccurences.createInterval(minOccurence, maxOccurence);
    }
    return interval;
  }

  /**
   * Create metadata for an element. This should contain information about element interval
   * and information that the element was parsed from schema.
   * @param interval Valid interval specifying element occurrence limits.
   * @return New metadata.
   */
  protected static Map<String, Object> prepareMetadata(final  RegexpInterval interval) {
    final Map<String, Object> metadata = new HashMap<String, Object>();
    metadata.putAll(IGGUtils.ATTR_FROM_SCHEMA);
    metadata.put(XSDAttribute.MINOCCURS.getMetadataName(), interval.getMin());
    if (interval.isUnbounded()) {
      metadata.put(XSDAttribute.MAXOCCURS.getMetadataName(), UNBOUNDED);
    } else {
      metadata.put(XSDAttribute.MAXOCCURS.getMetadataName(), interval.getMax());
    }
    return metadata;
  }

  /**
   * Set the interval of given element to the default (1,1) if it was not set.
   * @param ret Element to check for unset interval.
   */
  protected static void repairConcatInterval(final Element ret) {
    if (RegexpType.CONCATENATION.equals(ret.getSubnodes().getType()) && ret.getSubnodes().getInterval() == null) {
      ret.getSubnodes().setInterval(RegexpInterval.getOnce());
    }
  }

  /**
   * Set the parameter to {@link RegexpType#LAMBDA } element,
   * with all the valid constraints and make it immutable.
   * @param ret Element to set as LAMBDA.
   */
  protected static void setLambda(final Element ret) {
      ret.getSubnodes().setInterval(null);
      ret.getSubnodes().setContent(null);
      ret.getSubnodes().setType(RegexpType.LAMBDA);
      ret.setImmutable();
  }
}
