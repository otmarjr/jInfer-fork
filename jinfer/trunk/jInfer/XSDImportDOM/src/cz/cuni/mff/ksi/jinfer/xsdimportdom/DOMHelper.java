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
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDOccurences;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
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
  private static final Logger LOG = Logger.getLogger(DOMHandler.class);


  /**
   * Trims (cuts) namespace prefix from the beginning of element qName and returns it in original case.
   */
  protected static String trimNS(final String qName) {
    if (BaseUtils.isEmpty(qName)) {
      return qName;
    }
    return qName.substring(qName.lastIndexOf(':') + 1);
  }

  /**
   * Determine if given <code>Node</code> is in fact <i>org.w3c.dom.Element</i> and return a type cast if so.
   * @param node Entity to be checked.
   * @return Parameter cast to <code>org.w3c.dom.Element</code>, or <code>null</code>.
   * @see org.w3c.dom.Element
   */
  protected static org.w3c.dom.Element isDOMElement(final Node node) {
    if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
      return (org.w3c.dom.Element) node;
    }
    return null;
  }

  protected static RegexpInterval determineInterval(final org.w3c.dom.Element currentNode, final RegexpInterval outerInterval) {
    final String minOccurence = currentNode.getAttribute(XSDAttribute.MINOCCURS.toString());
    final String maxOccurence = currentNode.getAttribute(XSDAttribute.MAXOCCURS.toString());
    RegexpInterval interval;
    final RegexpInterval occurence = XSDOccurences.createInterval(minOccurence, maxOccurence);
    System.out.println("INTERVAL occurence is: " + occurence.toString());
    if (outerInterval != null) {
      try {
        if (outerInterval.isOnce()) {
          System.out.println("OUTer is once");
        } else {
          System.out.println("OUTer is: " + outerInterval.toString());
        }
        interval = RegexpInterval.intersectIntervals(outerInterval, occurence);
      } catch (IllegalArgumentException e) {
        LOG.warn("Occurence of element " + currentNode.getNodeName() + " has no intersection with constraints defined by its parent, ignoring constraints.");
        interval = occurence;
      }
    } else {
      System.out.println("OUTER WAS NULL");
      interval = occurence;
    }
    System.out.println("INTERVAL returning is: " + interval.toString());
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
    if (ret != null
        && ret.getSubnodes() != null
        && isOrderType(ret.getSubnodes().getType())
        && ret.getSubnodes().getInterval() == null) {
      ret.getSubnodes().setInterval(RegexpInterval.getOnce());
    }
  }

  /**
   * Determine whether the parameter is a {@link RegexpType#CONCATENATION }
   * or {@link RegexpType#ALTERNATION } or {@link RegexpType#PERMUTATION }.
   * In other words, if it indicates a type of order.
   * @param type Type to check.
   * @return True if type indicates an order.
   */
  protected static boolean isOrderType(final RegexpType type) {
    return RegexpType.CONCATENATION.equals(type)
      || RegexpType.ALTERNATION.equals(type)
      || RegexpType.PERMUTATION.equals(type);
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
