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
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.*;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class responsible for creating Initial Grammar rules from XSD Schema.
 * It uses a proprietary Xerces DOM parser (from SUN) to build the DOM tree from a stream, which is then
 * recursively traversed and a parallel tree with nodes of type {@link Element } is created.
 * This new rule tree contains the whole IG rules that are returned by {@link #getRules() } method.
 * These rules may contain complex regexps (depending on parsed schema) and should be expanded
 * before simplifying.
 * @author reseto
 */
public class DOMHandler {

  private static final Logger LOG = Logger.getLogger(DOMHandler.class);
  private final boolean verbose = XSDImportSettings.isVerbose();

  /**
   * Name of an element, which is only a container for a complex type. 
   * To distinguish containers, it should be different from any real element name, hence the colons.
   */
  private static final String CONTAINER_CTYPE = "::container::complextype::";
  /**
   * Name of an element, which is only a container for an order indicator.
   * To distinguish containers, it should be different from any real element name, hence the colons.
   */
  private static final String CONTAINER_ORDER = "::container::order::";
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
  /**
   * Mapping between unique names of element tags and nodes of DOM tree containing the tag.
   * These elements can be later referenced from within the schema using <i>ref</i> attribute.
   * The key is the value of attribute <code>name</code> of the particular tag.
   */
  private final Map<String, org.w3c.dom.Element> referenced = new HashMap<String, org.w3c.dom.Element>();

  /**
   * Default constructor, set loglevel as defined in XSD Import properties.
   */
  public DOMHandler() {
    LOG.setLevel(XSDImportSettings.logLevel());
  }

  /**
   * Parse the schema from stream.
   * Must be called before {@link #getRules() },
   * because it creates the tree of {@link Element }s from which the rules are extracted.
   * @param stream Schema to be parsed.
   */
  public void parse(final InputStream stream) {
    if (stream == null) {
      return;
    }
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
   * Get the list of all rules extracted from the schema.
   * @return List of rules.
   */
  public List<Element> getRules() {
    final List<Element> rules = new ArrayList<Element>();
    for (Element root : roots) {
      final List<Element> elementRules = new ArrayList<Element>();
      rules.add(root);
      XSDUtility.getRulesFromElement(root, elementRules);
      rules.addAll(elementRules);
    }
    return rules;
  }

  // #########################################################################       PRIVATE METHODS
  
  /**
   * Examine direct children of the root node of DOM element tree.
   * This should be invoked only once, on the schema node itself.
   * All complexTypes with name are added to the <code>namedCTypes</code> map.
   * All elements with name are added to the <code>referenced</code> map.
   * Then the whole rule tree is built using the DOM tree and the two maps.
   * @param root Root (schema) node.
   */
  private void examineRootChildren(final org.w3c.dom.Element root) throws XSDException {
    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
      // check if child is of type ELEMENT_NODE as defined by DOM spec
      final org.w3c.dom.Element domElem = DOMHelper.getDOMElement(root.getChildNodes().item(i));
      if (domElem != null) {
        // we have a valid child
        final XSDTag tag = XSDTag.matchName(XSDUtility.trimNS(domElem.getNodeName()));
        if (XSDTag.ELEMENT.equals(tag)) {
          // elements with name can be referenced from any subtree, so we create a map
          addToReferenced(domElem);
        } else if (XSDTag.COMPLEXTYPE.equals(tag)) {
          // complexType with name must be added to the map, unnamed complexType is not allowed
          addNamedCType(domElem);
        }
      }
      // if domElem == null, it's not interesting for us
    }
    // only after we know all available complex types can we parse the rule subtrees!
    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
      final org.w3c.dom.Element domElem = DOMHelper.getDOMElement(root.getChildNodes().item(i));
      // every element must be added to roots
      if (domElem != null && XSDTag.ELEMENT.equals(XSDTag.matchName(XSDUtility.trimNS(domElem.getNodeName())))) {
        // XSD Schema specification states that use of minOccurs and maxOccurs attributes
        // cannot be used for element directly under schema tag
        // that's why we throw away the second value of returned pair
        roots.add(buildRuleSubtree(domElem,
                               new ArrayList<String>(),
                               new ArrayList<org.w3c.dom.Element>()).getFirst());
      }
      // else .. we are not interested in the node
    }
  }

  /**
   * Add element to the <code>referenced</code> map.
   * This means that the element can be pointed at, by another element from within the schema,
   * using <i>ref</i> attribute.
   * XSD Schema allows this only for elements directly under schema tag.
   * @param domElem Element Element tag directly under schema tag.
   */
  private void addToReferenced(final org.w3c.dom.Element domElem) {
    final String name = domElem.getAttribute(XSDAttribute.NAME.toString());
    if (!BaseUtils.isEmpty(name)) {
      if (!referenced.containsKey(name)) {
        if (verbose) {
          LOG.debug("Adding element to referenced: " + name);
        }
        referenced.put(name, domElem);
      } else {
        LOG.warn("Trying to add duplicate element to referenced named: " + name);
      }
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

  // ####################################################################           SUBTREE BUILDING

  /**
   * Build a tree of {@link Element}s that represent the rules in Initial Grammar,
   * starting with current node.
   * This method examines the relationship between a given node and its children to create
   * a rule, then recursively builds the rule tree for each of the children.
   * It depends on an existing DOM tree for examining the relationships.
   * @param currentNode Root of the subtree for which we want to build the rules.
   * @param context Context of the current node in the whole rule tree. Every direct child of
   * <code>currentNode</code> has this context appended with the name of the <code>currentNode</code>
   * (name is the value of <i>name</i> attribute).
   * @param visited Similar to <code>context</code>, this is a list of all visited nodes
   * in the particular recursion branch, to avoid cycles when creating rules from complex types.
   * @param outerInterval Constraints of parent element on this element's number of occurrences.
   * Some structures in XSD Schema impose limits on the attributes <i>minOccurs</i> and <i>maxOccurs</i>.
   * @return Element Returns element with complete subtree of rules.
   * @throws XSDException When schema is invalid.
   */
  private Pair<Element, RegexpInterval> buildRuleSubtree(final org.w3c.dom.Element currentNode,
                                   final List<String> context,
                                   final List<org.w3c.dom.Element> visited) throws XSDException {

    final List<org.w3c.dom.Element> newVisited = new ArrayList<org.w3c.dom.Element>(visited);
    final List<String> newContext = new ArrayList<String>(context);

    final RegexpInterval interval = DOMHelper.determineInterval(currentNode);
    final XSDTag tag = XSDTag.matchName(XSDUtility.trimNS(currentNode.getNodeName()));

    // RET is the element that is returned from this recursive method
    // most methods below use this instance of RET to store data in it, 
    final Element ret = Element.getMutable();
    ret.getContext().addAll(context);
    ret.getMetadata().putAll(XSDUtility.prepareMetadata(interval));
    
    // inspect self
    final Element self = inspectSelf(currentNode, tag, ret, context, visited, newContext, newVisited, interval);
    if (self != null) {
      return new Pair<Element, RegexpInterval>(self, interval);
    } // else the ret element has all the changes needed

    // inspect children
    final NodeList children = currentNode.getChildNodes();
    inspectChildren(children, tag, ret, newContext, newVisited, interval);

    if (XSDTag.ELEMENT.equals(tag)) {
      DOMHelper.finalizeElement(ret, newContext);
    }
    return new Pair<Element, RegexpInterval>(ret, interval);
  }

  private Element inspectSelf(final org.w3c.dom.Element currentNode,
                              final XSDTag tag,
                              final Element ret,
                              final List<String> context,
                              final List<org.w3c.dom.Element> visited,
                              final List<String> newContext,
                              final List<org.w3c.dom.Element> newVisited,
                              final RegexpInterval interval) throws XSDException {
    switch (tag) {
      case ELEMENT:
        final Element selfElement = inspectElement(currentNode, ret, context, visited, newContext, newVisited);
        if (selfElement != null) {
          return selfElement;
        }
        break;
      case ALL:
        ret.getSubnodes().setType(RegexpType.PERMUTATION);
        ret.getSubnodes().setInterval(interval);
        ret.setName(CONTAINER_ORDER);
        break;
      case CHOICE:
        ret.getSubnodes().setType(RegexpType.ALTERNATION);
        ret.getSubnodes().setInterval(interval);
        ret.setName(CONTAINER_ORDER);
        break;
      case SEQUENCE:
        ret.getSubnodes().setType(RegexpType.CONCATENATION);
        ret.getSubnodes().setInterval(interval);
        ret.setName(CONTAINER_ORDER);
        break;
      case COMPLEXTYPE:
        ret.setName(CONTAINER_CTYPE);
        break;
      default:
        LOG.warn("Behavior undefined for tag " + currentNode.getTagName());
    }
    return null;
  }

  private void inspectChildren(final NodeList children,
                               final XSDTag tag,
                               final Element ret,
                               final List<String> newContext,
                               final List<org.w3c.dom.Element> newVisited,
                               final RegexpInterval interval) throws XSDException {

    for (int i = 0; i < children.getLength(); i++) {
      final org.w3c.dom.Element child = DOMHelper.getDOMElement(children.item(i));
      if (child != null) {
        final XSDTag childTag = XSDTag.matchName(XSDUtility.trimNS(child.getTagName()));
        switch (childTag) {
          case ELEMENT:
            handleChildElement(ret, child, tag, childTag, newContext, newVisited, interval);
            break;
          case COMPLEXTYPE:
            handleChildComplexType(ret, child, tag, childTag, newContext, newVisited);
            break;
          case ALL:
            handleChildAll(ret, child, tag, childTag, newContext, newVisited);
            break;
          case CHOICE:
          case SEQUENCE:
            handleChildChoiceSequence(ret, child, tag, childTag, newContext, newVisited);
            break;
          case ATTRIBUTE:
            handleChildAttribute(ret, child, newContext);
            break;
          default:
            LOG.warn("Behavior undefined for child tag " + child.getTagName());
        }
      } // end child != null
      // if child is null (it's a different DOM node), we are not interested
    }
  }

  private Element inspectElement(final org.w3c.dom.Element currentNode,
                                 final Element ret,
                                 final List<String> context,
                                 final List<org.w3c.dom.Element> visited,
                                 final List<String> newContext,
                                 final List<org.w3c.dom.Element> newVisited) {

    final Element sentinelOfVisited = checkElementVisited(currentNode, context, visited);
    if (sentinelOfVisited != null) {
      return sentinelOfVisited;
    }

    final String name = currentNode.getAttribute(XSDAttribute.NAME.toString());
    if (!BaseUtils.isEmpty(name)) {
      ret.setName(name);
    } else if (!BaseUtils.isEmpty(currentNode.getAttribute(XSDAttribute.REF.toString()))) {
      return checkElementRefAttribute(currentNode, context);
    } else {
      throw new XSDException("Element must have name or ref attribute defined.");
    }
    newContext.add(name);
    newVisited.add(currentNode);

    final Element elementWithNamedCType = checkElementComplexType(currentNode, ret, newContext, newVisited, name);
    if (elementWithNamedCType != null) {
      return elementWithNamedCType;
    }
    return null;
  }

  private Element checkElementVisited(final org.w3c.dom.Element currentNode,
                               final List<String> context,
                               final List<org.w3c.dom.Element> visited) {

    for (org.w3c.dom.Element el : visited) {
      // if this node was visited previously in the recursion, it is the same instance,
      // (for example when dealing with a recursive complextype)
      // we just create a sentinel to be used as the leaf node of rule tree
      if (el == currentNode) {
        if (verbose) {
          LOG.debug("Element visited previously, creating sentinel.");
        }
        return DOMHelper.createSentinel(currentNode, context, XSDAttribute.NAME);
      }
    }
    return null;
  }

  private Element checkElementRefAttribute(final org.w3c.dom.Element currentNode,
                           final List<String> context) {

    if (XSDTag.SCHEMA.toString().equals(XSDUtility.trimNS(currentNode.getParentNode().getNodeName()))) {
      throw new XSDException("Invalid schema. Element attribute 'ref' is not allowed for elements directly under the schema tag.");
    } else if (!referenced.containsKey(currentNode.getAttribute(XSDAttribute.REF.toString()))) {
      throw new XSDException("Invalid schema. Attribute 'ref' is referring to an element that is not in schema. Note: only top level elements can be referred to.");
    } else {
      return DOMHelper.createSentinel(currentNode, context, XSDAttribute.REF);
    }
  }

  private Element checkElementComplexType(final org.w3c.dom.Element currentNode,
                                          final Element ret,
                                          final List<String> newContext,
                                          final List<org.w3c.dom.Element> newVisited,
                                          final String name) {

    if (!BaseUtils.isEmpty(currentNode.getAttribute(XSDAttribute.TYPE.toString()))) {
      // element has defined type
      final String type = XSDUtility.trimNS(currentNode.getAttribute(XSDAttribute.TYPE.toString()));
      if (XSDBuiltInDataTypes.isSimpleDataType(type)) {
        // if element is of some built-in data type, we must first check its children
        // only then can we add the simple type as a token
        // see [SIMPLE DATA SECTION] in finalizeElement()
        ret.getMetadata().put(XSDAttribute.TYPE.getMetadataName(), type);
      } else if (namedCTypes.containsKey(type)) {
        final Element container = buildRuleSubtree(namedCTypes.get(type), newContext, newVisited).getFirst();
        DOMHelper.extractSubnodesFromContainer(container, ret, CONTAINER_CTYPE);
        DOMHelper.finalizeElement(ret, newContext);
        return ret; // RETURN STATEMENT
      } else {
        LOG.error("Specified type '" + type + "' of element '" + name + "' was not found!");
      }
    }
    return null;
  }

  private void handleChildElement(final Element ret,
                                  final org.w3c.dom.Element child,
                                  final XSDTag tag,
                                  final XSDTag childTag,
                                  final List<String> newContext,
                                  final List<org.w3c.dom.Element> newVisited,
                                  final RegexpInterval interval) throws XSDException {
    if (XSDTag.ELEMENT.equals(tag)) {
      // parent is also element
      throw new XSDException(DOMHelper.errorWrongNested(childTag, tag));
    }
    final Pair<Element, RegexpInterval> subtree = buildRuleSubtree(child, newContext, newVisited);
    RegexpInterval tokenOccurence = subtree.getSecond();
    if (XSDTag.ALL.equals(tag) && !tokenOccurence.isOnce() && !tokenOccurence.isOptional()) {
      // parent is ALL tag - special treatment for intervals
      // token occurence can be only {0,1} or {1,1} because element ALL restricts this property
      tokenOccurence = interval; // set it to the default interval of parent (ALL)
    }
    final Regexp<AbstractStructuralNode> token = Regexp.<AbstractStructuralNode>getToken(subtree.getFirst(), tokenOccurence);
    ret.getSubnodes().addChild(token);
  }

  private void handleChildComplexType(final Element ret,
                                      final org.w3c.dom.Element child,
                                      final XSDTag tag,
                                      final XSDTag childTag,
                                      final List<String> newContext,
                                      final List<org.w3c.dom.Element> newVisited) throws XSDException {
    if (XSDTag.ELEMENT.equals(tag)) {
      DOMHelper.extractSubnodesFromContainer(buildRuleSubtree(child, newContext, newVisited).getFirst(), ret, CONTAINER_CTYPE);
    } else if (XSDTag.REDEFINE.equals(tag)) {
      LOG.warn(DOMHelper.warnUnsupported(childTag, tag));
    } else {
      // parent is also complexType or other unsuitable tag
      throw new XSDException(DOMHelper.errorWrongNested(childTag, tag));
    }
    // complexType directly under schema tag is handled in examineRootChildren()
  }

  private void handleChildAll(final Element ret,
                              final org.w3c.dom.Element child,
                              final XSDTag tag,
                              final XSDTag childTag,
                              final List<String> newContext,
                              final List<org.w3c.dom.Element> newVisited) throws XSDException {
    if (XSDTag.COMPLEXTYPE.equals(tag)) {
      DOMHelper.extractSubnodesFromContainer(buildRuleSubtree(child, newContext, newVisited).getFirst(), ret, CONTAINER_ORDER);
    } else if (XSDTag.CHOICE.equals(tag) || XSDTag.SEQUENCE.equals(tag)) {
      throw new XSDException(DOMHelper.errorWrongNested(childTag, tag));
    } else {
      LOG.warn(DOMHelper.warnUnsupported(childTag, tag));
    }
  }

  private void handleChildChoiceSequence(final Element ret, 
                                         final org.w3c.dom.Element child,
                                         final XSDTag tag,
                                         final XSDTag childTag,
                                         final List<String> newContext, 
                                         final List<org.w3c.dom.Element> newVisited) throws XSDException {
    if (XSDTag.CHOICE.equals(tag) || XSDTag.SEQUENCE.equals(tag)) {
      ret.getSubnodes().addChild(buildRuleSubtree(child, newContext, newVisited).getFirst().getSubnodes());
    } else if (XSDTag.COMPLEXTYPE.equals(tag)) {
      DOMHelper.extractSubnodesFromContainer(buildRuleSubtree(child, newContext, newVisited).getFirst(), ret, CONTAINER_ORDER);
    } else if (XSDTag.ELEMENT.equals(tag) || XSDTag.ALL.equals(tag)) {
      throw new XSDException(DOMHelper.errorWrongNested(childTag, tag));
    } else {
      LOG.warn(DOMHelper.warnUnsupported(childTag, tag));
    }
  }

  private void handleChildAttribute(final Element ret,
                                    final org.w3c.dom.Element child,
                                    final List<String> newContext) {

    // first get the attribute name or ref
    // we don't resolve the ref at all (out of assignment), just register it's there
    final String attrName = DOMHelper.getAttributeName(child);
    if (BaseUtils.isEmpty(attrName)) {
      LOG.error("Attribute under " + ret.getName() + " has no name.");
      return;
    }
    // if we have the name or ref, add the contents of the attribute to parent element (ret)
    String attrType = child.getAttribute(XSDAttribute.TYPE.toString());
    if (!BaseUtils.isEmpty(attrType)) {
      attrType = XSDUtility.trimNS(attrType);
      if (!XSDBuiltInDataTypes.isSimpleDataType(attrType)) {
        attrType = "";
      }
    }
    ret.getAttributes().add(new Attribute(newContext, attrName, DOMHelper.getAttributeMeta(child), attrType, new ArrayList<String>(0)));
  }

}
