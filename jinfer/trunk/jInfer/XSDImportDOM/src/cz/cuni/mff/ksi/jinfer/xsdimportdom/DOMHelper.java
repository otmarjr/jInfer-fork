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
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDOccurences;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.w3c.dom.Node;

/**
 *
 * @author reseto
 */
public final class DOMHelper {
  private DOMHelper() {}

  private static final Logger LOG = Logger.getLogger(DOMHandler.class);
  static {
    LOG.setLevel(XSDImportSettings.logLevel());
  }

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
  protected static String errorWrongNested(final XSDTag child, final XSDTag parent) {
    return NbBundle.getMessage(DOMHelper.class, "Error.WrongTagsNested", child.toString(), parent.toString());
  }

  /**
   * Helper method for retrieving warning message when unsupported structure occurs in schema.
   * @param child Tag of the child node.
   * @param parent Tag of the parent node.
   * @return Warning message.
   */
  protected static String warnUnsupported(final XSDTag child, final XSDTag parent) {
    return NbBundle.getMessage(DOMHelper.class, "Warn.UnsupportedStructure", child.toString(), parent.toString());
  }

  /**
   * Create an {@link Element } with type {@link RegexpType#LAMBDA } with proper constraints
   * and metadata containing sentinel info.
   * This kind of element is used when it was defined previously and it is unnecessary to
   * build a rule subtree for it again (it is either a reference, or a leaf in recursion).
   * @param domElem Node in the DOM tree from which the sentinel is made.
   * @param context Context of the node in the rule tree.
   * @param useAsName Name of the attribute to be used as a name of the new <code>Element</code>.
   * @return Sentinel element.
   * @see IGGUtils#METADATA_SENTINEL
   */
  protected static Element createSentinel(final org.w3c.dom.Element domElem, final List<String> context, final XSDAttribute useAsName) {
    final Element sentinel = Element.getMutable();
    if (XSDAttribute.NAME.equals(useAsName) || XSDAttribute.REF.equals(useAsName)) {
      sentinel.setName(domElem.getAttribute(useAsName.toString()));
    } else {
      throw new XSDException("Cannot use attribute " + useAsName.toString() + " as a name for " + domElem.getTagName());
    }

    if (BaseUtils.isEmpty(sentinel.getName())) {
      throw new XSDException("Trying to create sentinel with no name.");
    }
    if (XSDImportSettings.isVerbose()) {
      LOG.debug("Creating sentinel using " + useAsName.toString() + " with name " + sentinel.getName());
    }
    sentinel.getContext().addAll(context);
    sentinel.getMetadata().putAll(IGGUtils.METADATA_SENTINEL);
    sentinel.getMetadata().putAll(IGGUtils.ATTR_FROM_SCHEMA);
    sentinel.getSubnodes().setType(RegexpType.LAMBDA);
    sentinel.setImmutable();
    return sentinel;
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
      throw new XSDException(NbBundle.getMessage(DOMHelper.class, "Error.WrongContainer", containerType, subtree.getName()));
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
      final String msg = NbBundle.getMessage(DOMHelper.class, "Error.MismatchedTypes",
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

  /**
   * Extract value of name or ref from an attribute tag.
   * @param child
   * @return Value of attribute <i>name</i> or <i>ref</i> or empty string.
   */
  protected static String getAttributeName(final org.w3c.dom.Element child) {
    final String name = child.getAttribute(XSDAttribute.NAME.toString());
    final String ref = child.getAttribute(XSDAttribute.REF.toString());
    if (!BaseUtils.isEmpty(name)) {
      if (!BaseUtils.isEmpty(ref)) {
        LOG.error("Attribute tag with name '" + name + "' has also defined ref '" + ref + "' which is not allowed.");
      }
      return name;
    } else if (!BaseUtils.isEmpty(ref)) {
      return ref;
    } else {
      return "";
    }
  }

  /**
   * Prepare metadata of attribute tag from the child node.
   * If the child has defined attribute <i>use</i>, add this information to metadata.
   * @param child DOM node of the attribute tag.
   * @return New metadata.
   */
  protected static Map<String, Object> getAttributeMeta(final org.w3c.dom.Element child) {
    final Map<String, Object> attrMeta = new HashMap<String, Object>();
    if (child.hasAttribute(XSDAttribute.USE.toString())) {
      if (XSDUtility.REQUIRED.equals(child.getAttribute(XSDAttribute.USE.toString()))) {
        attrMeta.put(IGGUtils.REQUIRED, Boolean.TRUE);
      } else if (XSDUtility.OPTIONAL.equals(child.getAttribute(XSDAttribute.USE.toString()))) {
        attrMeta.put(IGGUtils.REQUIRED, Boolean.FALSE);
      }
    }
    return attrMeta;
  }
}
