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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDOccurences;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.w3c.dom.Node;

/**
 *
 * @author reseto
 */
public final class DOMHelper {

  private static final Logger LOG = Logger.getLogger(DOMHandler.class);

  private DOMHelper() {}

  /**
   * Determine if given <code>Node</code> is in fact <i>org.w3c.dom.Element</i> and return a type cast if so.
   * @param node Entity to be checked.
   * @return Parameter cast to <code>org.w3c.dom.Element</code>, or <code>null</code>.
   * @see org.w3c.dom.Element
   */
  protected static org.w3c.dom.Element getDOMElement(final Node node) {
    if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
      return (org.w3c.dom.Element) node;
    }
    return null;
  }

  /**
   * Create valid interval from current node.
   * If node has attributes <i>minOccurs</i> or <i>maxOccurs</i>, their values are used.
   * Otherwise a default interval is returned (once).
   * @param currentNode Node from which the information is extracted.
   * @return Valid interval.
   * @see XSDOccurences#createInterval(java.lang.String, java.lang.String)
   */
  protected static RegexpInterval determineInterval(final org.w3c.dom.Element currentNode) {
    final String minOccurence = currentNode.getAttribute(XSDAttribute.MINOCCURS.toString());
    final String maxOccurence = currentNode.getAttribute(XSDAttribute.MAXOCCURS.toString());
    return XSDOccurences.createInterval(minOccurence, maxOccurence);
  }

  /**
   * Helper method for retrieving error message when tags are not nested correctly.
   * @param child Tag of the child node.
   * @param parent Tag of the parent node.
   * @return Error message.
   */
  protected static String errorWrongNested(final XSDTag child, final XSDTag parent) throws MissingResourceException {
    return NbBundle.getMessage(DOMHelper.class, "Error.WrongTagsNested", child.toString(), parent.toString());
  }

  /**
   * Helper method for retrieving warning message when unsupported structure occurs in schema.
   * @param child Tag of the child node.
   * @param parent Tag of the parent node.
   * @return Warning message.
   */
  protected static String warnUnsupported(final XSDTag child, final XSDTag parent) throws MissingResourceException {
    return NbBundle.getMessage(DOMHelper.class, "Warn.UnsupportedStructure", child.toString(), parent.toString());
  }

  /**
   * Extract subnodes and attributes from subtree stored in container and put them directly under
   * the destination element. This is a helper method for omitting containers.
   * @param subtree Container element, from which the subnodes will be extracted.
   * @param destination Destination element, where the subnodes are copied to.
   * @param containerType Expected name of the subtree element, this is just a consistency check.
   * @throws XSDException
   */
  protected static void extractSubnodesFromContainer(final Element subtree, final Element destination, final String containerType) throws XSDException {
    if (!subtree.getName().equals(containerType)) {
      throw new XSDException(NbBundle.getMessage(DOMHandler.class, "Error.WrongContainer", containerType, subtree.getName()));
    }
    // compare types of subnodes, set correct type if destination is null
    // throw exception if types don't match, or do nothing if they match
    if (destination.getSubnodes().getType() == null && subtree.getSubnodes().getType() != null) {
      destination.getSubnodes().setType(subtree.getSubnodes().getType());
    } else if (destination.getSubnodes().getType() != null
               && subtree.getSubnodes().getType() != null
               && destination.getSubnodes().getType() != subtree.getSubnodes().getType()) {
      //                                              ^^ comparing enums
      // if the two types are not null and not equal, the schema is invalid (mixing wrong types)
      final String msg = NbBundle.getMessage(DOMHandler.class, "Error.MismatchedTypes",
        subtree.getSubnodes().getType(), subtree.getName(),
        destination.getSubnodes().getType(), destination.getName());
      throw new XSDException(msg);
    }
    for (Regexp<AbstractStructuralNode> regexp : subtree.getSubnodes().getChildren()) {
      destination.getSubnodes().addChild(regexp);
    }
    destination.getSubnodes().setInterval(subtree.getSubnodes().getInterval());
    destination.getAttributes().addAll(subtree.getAttributes());
  }

  /**
   * Check if element is properly defined; redefine it to lambda when it was empty,
   * or redefine it to token if it only contained a simple data type.
   * This method should be used only when the tag of a node was ELEMENT!
   * @param ret Element to be finalized.
   */
  protected static void finalizeElement(final Element ret, final List<String> newContext) {

    if (ret.getSubnodes().getInterval() == null) {
      ret.getSubnodes().setInterval(RegexpInterval.getOnce());
    }
    if (ret.getSubnodes().getChildren().isEmpty()
        && ret.getMetadata().containsKey(XSDAttribute.TYPE.getMetadataName())) {
      // [SIMPLE DATA SECTION]
      // element has empty children, but its specified type is one of the built-in types
      // create SimpleData with the defined type of the element
      ret.getSubnodes().setType(RegexpType.TOKEN);
      ret.getSubnodes().setContent(
        new SimpleData(newContext,
                       XSDUtility.SIMPLE_DATA_NAME,
                       Collections.<String,Object>emptyMap(),
                       (String) ret.getMetadata().get(XSDAttribute.TYPE.getMetadataName()),
                       Collections.<String>emptyList()));
    } else if (ret.getSubnodes().getChildren().isEmpty()
        && ret.getSubnodes().getType() == null) {
      // element with empty children and no specified type has currently only one option
      // since we don't support restrictions, extensions, complexcontent -> it has to be LAMBDA
      XSDUtility.setLambda(ret);
    } else if (ret.getSubnodes().getType() == null) {
      // type of element must be non-null, but apparently it has some children
      // this should not happen
      LOG.warn(NbBundle.getMessage(DOMHelper.class, "Warn.SetDefaultType", ret.getName()));
      ret.getSubnodes().setType(RegexpType.CONCATENATION);
      ret.getSubnodes().setContent(null);
    }
    if (ret.isMutable()) {
      ret.setImmutable();
    }
  }
}
