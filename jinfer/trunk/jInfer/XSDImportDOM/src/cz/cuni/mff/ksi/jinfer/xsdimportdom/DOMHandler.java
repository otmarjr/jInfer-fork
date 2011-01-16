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
package cz.cuni.mff.ksi.jinfer.xsdimportdom;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDBuiltInDataTypes;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TODO reseto Comment!
 * 
 * @author reseto
 */
public class DOMHandler {

  private final XSDImportSettings settings = new XSDImportSettings();
  private static final Logger LOG = Logger.getLogger(DOMHandler.class);
  private final boolean verbose = settings.isVerbose();

  private static final String STAR = "*";
  private static final String CONTAINER_CTYPE = "::container::complextype::";
  private static final String CONTAINER_ORDER = "::container::order::";
  private static final String UNBOUNDED = "unbounded";

  /**
   * List of all element tags in current schema that are immediate children of the schema tag itself.
   * This list includes all element tags that can be referenced by the <i>ref</i> attribute from
   * within this schema (according to XML Schema specification).
   */
  private final List<Element> roots = new ArrayList<Element>();
  /**
   * Mapping between unique names of complexType tags and nodes of DOM tree containing the tag.
   * The key is the value of attribute <code>name</code> of the particular tag.
   */
  private final Map<String, org.w3c.dom.Element> namedCTypes = new HashMap<String, org.w3c.dom.Element>();

  public DOMHandler() {
    LOG.setLevel(settings.logLevel());
  }

  /**
   * Get the list of all rules extracted from the schema.
   * @return List of rules.
   */
  public List<Element> getRules() {
    final List<Element> rules = new ArrayList<Element>();
    for (Element root : roots) {
      final List<Element> elementRules = new ArrayList<Element>();
      rules.add(root);
      getRulesFromElement(root, elementRules);
      rules.addAll(elementRules);
    }
    if (verbose) {
      LOG.info("Schema imported with following rules:");
      for (Element elem : rules) {
        LOG.info(elem.toString());
      }
    }
    return rules;
  }

  /**
   * Parse the schema from stream.
   * Must be called before {@link #getRules() },
   * because it creates the tree of <code>Elements</code> from which the rules are extracted.
   * @param stream Schema to be parsed.
   */
  public void parse(final InputStream stream) {
    try {
      final DOMParser parser = new DOMParser();
      parser.parse(new InputSource(stream));
      final Document doc = parser.getDocument();
      final org.w3c.dom.Element root = doc.getDocumentElement();

      examineRootChildren(root); // this recursively builds rule trees for subelements

    } catch (SAXException ex) {
      Exceptions.printStackTrace(ex);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  /**
   * Extract all rules from a subtree of <code>Element</code>, not including the root itself.
   * @param root Root of the subtree from which we extract the rules.
   * @param elementRules List where all the rules are stored.
   */
  private void getRulesFromElement(final Element root, final List<Element> elementRules) {
    for (AbstractStructuralNode node : root.getSubnodes().getTokens()) {
      if (node.isElement()) {
        elementRules.add((Element) node);
        getRulesFromElement((Element) node, elementRules);
      }
    }
  }

  private void examineRootChildren(final org.w3c.dom.Element root) {
    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
      // check if child is of type ELEMENT_NODE as defined by DOM spec
      final org.w3c.dom.Element domElem = isDOMElement(root.getChildNodes().item(i));
      if (domElem != null) {
        // we have a valid child
        // complexType with name must be added to the map, unnamed complexType is not allowed
        // every element must be added to roots, but element with ref attribute has no subtree (sentinel)
        final XSDTag tag = XSDTag.matchName(trimNS(domElem.getNodeName())); // safety check by trimNS
        if (XSDTag.ELEMENT.equals(tag)) {
          addElementToRoots(domElem);
        } else if (XSDTag.COMPLEXTYPE.equals(tag)) {
          addNamedCType(domElem);
        }
      }
      // if domElem == null, it's not interesting for us
    }
  }

  /**
   * Transform domElem to <code>Element</code>, which will be either a sentinel with no subtree
   * (if it has defined <i>ref</i> attribute), or a regular element with full subtree that is
   * recursively parsed from the schema.
   * @param domElem Node in the DOM tree to process.
   */
  private void addElementToRoots(final org.w3c.dom.Element domElem) {
    final String name = domElem.getAttribute(XSDAttribute.NAME.toString());
    if (BaseUtils.isEmpty(name) && domElem.hasAttribute(XSDAttribute.REF.toString())) {
      roots.add(createSentinel(domElem, new ArrayList<String>(), XSDAttribute.REF));
    } else {
      roots.add(buildRuleSubtree(domElem, new ArrayList<String>(), new ArrayList<org.w3c.dom.Element>()));
    }
  }

  /**
   * Create a mapping of <i>name</i> to the instance of <code>org.w3c.dom.Element</code>
   * and store it in {@link DOMHandler#namedCTypes } map. The map key is created from the
   * value of <i>name</i> attribute of the instance.
   * @param domElem Node of the DOM tree containing the complexType tag.
   */
  private void addNamedCType(final org.w3c.dom.Element domElem) {
    final String name = domElem.getAttribute(XSDAttribute.NAME.toString());
    if (!BaseUtils.isEmpty(name)) {
      if (!namedCTypes.containsKey(name)) {
        if (verbose) {
          LOG.debug("Adding complextype: " + name);
        }
        namedCTypes.put(name, domElem);
      } else {
        LOG.warn("Trying to add duplicate complexType named: " + name);
      }
    } else {
      LOG.warn("Trying to add a complexType with no name under: " + domElem.getParentNode().getNodeName());
    }
  }

  private Element createSentinel(final org.w3c.dom.Element domElem, final List<String> context, final XSDAttribute useAsName) {
    final Element sentinel = Element.getMutable();
    if (XSDAttribute.NAME.equals(useAsName) || XSDAttribute.REF.equals(useAsName)) {
      sentinel.setName(domElem.getAttribute(useAsName.toString()));
    } else {
      throw new XSDException("Cannot use attribute " + useAsName.toString() + " as a name for " + domElem.getTagName());
    }

    if (BaseUtils.isEmpty(sentinel.getName())) {
      throw new XSDException("Trying to create sentinel with no name.");
    }
    sentinel.getContext().addAll(context);
    sentinel.getMetadata().putAll(IGGUtils.METADATA_SENTINEL);
    sentinel.getMetadata().putAll(IGGUtils.ATTR_FROM_SCHEMA);
    sentinel.getSubnodes().setType(RegexpType.LAMBDA);
    sentinel.setImmutable();
    return sentinel;
  }

  private void debugDumpOfElements(final org.w3c.dom.Element root) {
    final NodeList elements = root.getElementsByTagNameNS(STAR, XSDTag.ELEMENT.getName());
    for (int i = 0; i < elements.getLength(); i++) {
      final org.w3c.dom.Element domEl = isDOMElement(elements.item(i)); //always in this case
      if (domEl != null) {
        if (!BaseUtils.isEmpty(domEl.getAttribute(XSDAttribute.NAME.toString()))) {
          LOG.debug("Found element with name:" + domEl.getAttribute(XSDAttribute.NAME.toString()));
        } else if (!BaseUtils.isEmpty(domEl.getAttribute(XSDAttribute.REF.toString()))) {
          LOG.debug("Found element with ref:" + domEl.getAttribute(XSDAttribute.NAME.toString()));
        }
      }
    }
  }

  /**
   * Determine if ret has no children and thus can be retyped to <code>LAMBDA</code>.
   * On return from this method, the parameter is set to {@link RegexpType#LAMBDA }
   * with all the valid constraints, and then it is made immutable.
   * @param ret Element to check for children.
   */
  private void checkForLambda(final Element ret) {
    if (ret.getSubnodes().getChildren().isEmpty()
        && (ret.getSubnodes().getType() == null
            || !ret.getSubnodes().getType().equals(RegexpType.LAMBDA))) {
      ret.getSubnodes().setInterval(null);
      ret.getSubnodes().setContent(null);
      ret.getSubnodes().setType(RegexpType.LAMBDA);
      ret.setImmutable();
    }
  }

  private Element buildRuleSubtree(final org.w3c.dom.Element currentNode, final List<String> context, final List<org.w3c.dom.Element> visited) throws XSDException {
    final Element ret = Element.getMutable();
    final List<org.w3c.dom.Element> newVisited = new ArrayList<org.w3c.dom.Element>(visited);
    final List<String> newContext = new ArrayList<String>(context);
    final Map<String, Object> metadata = ret.getMetadata();

    ret.getContext().addAll(context);
    metadata.putAll(IGGUtils.ATTR_FROM_SCHEMA);

    final RegexpInterval interval = determineOccurence(currentNode);

    metadata.put(XSDAttribute.MINOCCURS.getMetadataName(), interval.getMin());
    if (interval.isUnbounded()) {
      metadata.put(XSDAttribute.MAXOCCURS.getMetadataName(), UNBOUNDED);
    } else {
      metadata.put(XSDAttribute.MAXOCCURS.getMetadataName(), interval.getMax());
    }
    
    // inspect self
    final XSDTag tag = XSDTag.matchName(trimNS(currentNode.getNodeName()));
    switch (tag) {
      case ELEMENT:
        for (org.w3c.dom.Element el : visited) {
          // if this node was visited previously in the recursion
          // (for example when dealing with a recursive complextype)
          // we just create a sentinel to be used as the leaf node of rule tree
          if(el == currentNode) { // has to be the same instance
            if (verbose) {
              LOG.debug("creating sentinel from " + currentNode.getNodeName());
            }
            return createSentinel(currentNode, context, XSDAttribute.NAME);
          }
        }
        
        final String name = currentNode.getAttribute(XSDAttribute.NAME.toString());
        if (!BaseUtils.isEmpty(name)) {
          ret.setName(name);
          newContext.add(name);
        } else if (!BaseUtils.isEmpty(currentNode.getAttribute(XSDAttribute.REF.toString()))) {
          return createSentinel(currentNode, context, XSDAttribute.REF);
        } else {
          throw new XSDException("Element must have name or ref attribute defined.");
        }
        newVisited.add(currentNode);
        ret.getSubnodes().setInterval(interval);

        // element has defined type
        if (!BaseUtils.isEmpty(currentNode.getAttribute(XSDAttribute.TYPE.toString()))) {
          final String type = currentNode.getAttribute(XSDAttribute.TYPE.toString());
          if (XSDBuiltInDataTypes.isSimpleDataType(type)) {
            metadata.put(XSDAttribute.TYPE.getMetadataName(), type);
          } else if (namedCTypes.containsKey(type)) {
            final Element subtree = buildRuleSubtree(namedCTypes.get(type), newContext, newVisited);
            extractSubnodesFromContainer(subtree, ret, CONTAINER_CTYPE);
            checkForLambda(ret);
            return ret; // RETURN STATEMENT !
          } else {
            LOG.error("Specified type '" + type + "' of element '" + name + "' was not found!");
          }
        }

        break;
      case ALL:
        ret.getSubnodes().setType(RegexpType.PERMUTATION);
        ret.setName(CONTAINER_ORDER);
        break;
      case CHOICE:
        ret.getSubnodes().setType(RegexpType.ALTERNATION);
        ret.setName(CONTAINER_ORDER);
        break;
      case SEQUENCE:
        //TODO reseto: create child interval to mark the number of occurences
        ret.getSubnodes().setType(RegexpType.CONCATENATION);
        ret.setName(CONTAINER_ORDER);
        break;
      case COMPLEXTYPE:
        ret.setName(CONTAINER_CTYPE);
        break;
      default:
        // do nothing;
        LOG.warn("Behavior undefined for tag " + currentNode.getTagName());
    }
    
    // inspect children
    final NodeList children = currentNode.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      final org.w3c.dom.Element child = isDOMElement(children.item(i));
      if (child != null) {
        final XSDTag childTag = XSDTag.matchName(trimNS(child.getTagName()));
        switch (childTag) {
          case ELEMENT:
            if (XSDTag.ELEMENT.equals(tag)) { // parent is also element
              throw new XSDException("Invalid schema, two directly nested element tags.");
            }
            ret.getSubnodes().addChild(
              Regexp.<AbstractStructuralNode>getToken(
              buildRuleSubtree(child, newContext, newVisited)));
            break;
          case COMPLEXTYPE:
            if (XSDTag.COMPLEXTYPE.equals(tag)) { // parent is also complexType
              throw new XSDException("Invalid schema, two directly nested complexType tags.");
            }
            extractSubnodesFromContainer(buildRuleSubtree(child, newContext, newVisited), ret, CONTAINER_CTYPE);
            //TODO reseto: extract children and attributes from subcontainer
            break;
          case ATTRIBUTE:
            // first get the attribute name or ref
            // we don't resolve the ref at all (out of assignment), just register it's there
            String attrName = child.getAttribute(XSDAttribute.NAME.toString());
            if (BaseUtils.isEmpty(attrName)) {
              attrName = child.getAttribute(XSDAttribute.REF.toString());
              if (BaseUtils.isEmpty(attrName)) {
                LOG.error("Attribute under " + ret.getName() + " has no name.");
                continue;
              }
            }
            String attrType = child.getAttribute(XSDAttribute.TYPE.toString());
            if (BaseUtils.isEmpty(attrType)) {
              attrType = null;
            }
            final HashMap<String, Object> attrMeta = new HashMap<String, Object>();
            if (child.hasAttribute(XSDAttribute.USE.toString())
              && child.getAttribute(XSDAttribute.USE.toString()).equalsIgnoreCase("required")) {
              attrMeta.put("required", Boolean.TRUE);
            }
            //TODO reseto: possibly add more metadata
            ret.getAttributes().add(
              new Attribute(newContext, attrName, attrMeta, attrType, new ArrayList<String>(0)));
            break;
          case ALL:
            if (XSDTag.COMPLEXTYPE.equals(tag)) {
              
            } else {
              //TODO reseto: group and complexcontent should be supported?
              LOG.warn("Unsupported tree structure: element 'all' under " + tag.toString());
            }
            break;
          case CHOICE:
          case SEQUENCE:
            if (XSDTag.isOrderIndicator(tag)) {
              ret.getSubnodes().addChild(buildRuleSubtree(child, newContext, newVisited).getSubnodes());
            } else if (XSDTag.COMPLEXTYPE.equals(tag)) {
              extractSubnodesFromContainer(buildRuleSubtree(child, newContext, newVisited), ret, CONTAINER_ORDER);
            } else if (XSDTag.ELEMENT.equals(tag)) {
              throw new XSDException("Invalid schema, element '" + childTag.toString() + "' directly under '" + tag.toString() + "'.");
            } else {
              LOG.warn("Unsupported tree structure: element '" + childTag.toString() + "' under '" + tag.toString() + "'.");
            }
            break;
          default:
            LOG.warn("Behavior undefined for child tag " + child.getTagName());
        }

      }

    } // end children loop

    checkForLambda(ret);
    
    return ret;
  }

  /**
   * Trims (cuts) namespace prefix from the beginning of element qName and returns it in original case.
   */
  private String trimNS(final String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1);
  }

  /**
   * Determine if given <code>Node</code> is in fact <i>org.w3c.dom.Element</i> and return a type cast if so.
   * @param node Entity to be checked.
   * @return Parameter cast to <code>org.w3c.dom.Element</code>, or <code>null</code>.
   * @see org.w3c.dom.Element
   */
  private org.w3c.dom.Element isDOMElement(final Node node) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      return (org.w3c.dom.Element) node;
    }
    return null;
  }

  /**
   * Create interval for current node by extracting the occurrence from DOM node attributes.
   * Default values of min and max occurrences are 1, as defined by XSD Schema specification.
   * This method should not throw any exceptions.
   * @param currentNode Node to be examined.
   * @return Interval with the proper values.
   */
  private RegexpInterval determineOccurence(final org.w3c.dom.Element currentNode) {
    RegexpInterval interval;
    int min = -2, max = -2; // set to undefined value
    final String minOccurence = currentNode.getAttribute(XSDAttribute.MINOCCURS.toString());
    final String maxOccurence = currentNode.getAttribute(XSDAttribute.MAXOCCURS.toString());

    if (!BaseUtils.isEmpty(minOccurence)) {
      try {
        min = Integer.parseInt(minOccurence);
      } catch (NumberFormatException e) {
      } // if parsing fails, we set a default value according to specification
    }
    // according to XSD Schema specification, the default values for min and max are 1
    if (min < 0) {
      min = 1;
    }

    if (!BaseUtils.isEmpty(maxOccurence)) {
      try {
        if (UNBOUNDED.equals(maxOccurence)) {
          max = -1;
        } else {
          max = Integer.parseInt(maxOccurence);
        }
      } catch (NumberFormatException e) {
      }
    }
    if (max < -1) {
      max = 1;
    }
    if (max != -1 && min > max) {
      min = max;
    }
    if (max == -1) {
      interval = RegexpInterval.getUnbounded(min);
    } else {
      interval = RegexpInterval.getBounded(min, max);
    }
    return interval;
  }

  /**
   * Extract subnodes and attributes from subtree stored in container and put them directly under
   * the destination element. This is a helper method for omitting containers.
   * @param subtree Container element, from which the subnodes will be extracted.
   * @param destination Destination element, where the subnodes are copied to.
   * @param containerType Expected name of the subtree element, this is just a consistency check.
   * @throws XSDException
   */
  private void extractSubnodesFromContainer(final Element subtree, final Element destination, final String containerType) throws XSDException {
    if (!subtree.getName().equals(containerType)) {
      throw new XSDException("Extracting subnodes failed, unexpected contaner.");
    }
    if (destination.getSubnodes().getType() == null && subtree.getSubnodes().getType() != null) {
      destination.getSubnodes().setType(subtree.getSubnodes().getType());
    } else {
      LOG.error("subtree name: " + subtree.getName() + " and destination name: " + destination.getName());
      throw new XSDException("Extracting subnodes failed, incompatible regexp types "
        + destination.getSubnodes().getType()
        + " and " + subtree.getSubnodes().getType());
    }
    for (Regexp<AbstractStructuralNode> regexp : subtree.getSubnodes().getChildren()) {
      destination.getSubnodes().addChild(regexp);
    }
    destination.getAttributes().addAll(subtree.getAttributes());
  }
}
