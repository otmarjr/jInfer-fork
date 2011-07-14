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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.*;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.w3c.dom.NodeList;

/**
 * Class responsible for building rule-trees from DOM tree.
 * <p>
 * Extracts information from existing DOM tree to create rule-trees.
 * Rule-trees are tree data structures, where vertices are instances of {@link Element }
 * and edges are the relations between them stored in {@link Regexp } instances within vertices.
 * Every vertex is a rule by itself, so in order to obtain IG rules,
 * one must extract all <code>Element</code>s from a rule-tree.
 * </p>
 * Please read package info.
 * @author reseto
 */
@SuppressWarnings("PMD.TooManyMethods")
public class DOMHandler {

  private static final Logger LOG = Logger.getLogger(DOMHandler.class);
  private final boolean verbose = XSDImportSettings.isVerbose();

  /**
   * Name for an <code>Element</code>, which is only a container for the contents parsed from a <i>complexType</i> tag.
   * To distinguish containers, it should be different from any real <code>Element</code> name, hence the colons.
   */
  private static final String CONTAINER_CTYPE = "::container::complextype::";
  /**
   * Name for an <code>Element</code>, which is only a container for an order indicator.
   * To distinguish containers, it should be different from any real element name, hence the colons.
   */
  private static final String CONTAINER_ORDER = "::container::order::";
  /**
   * List of all <i>element</i> tags in current schema that are immediate children of the <i>schema</i> tag itself.
   */
  private final List<Element> roots = new ArrayList<Element>();
  /**
   * Mapping between unique names of complexType tags and nodes of DOM tree containing the tag.
   * The key is the value of attribute <code>name</code> of the particular tag.
   */
  private final Map<String, org.w3c.dom.Element> namedCTypes = new HashMap<String, org.w3c.dom.Element>();
  /**
   * Mapping between unique names of <i>element</i> tags and <code>DOM.Element</code> nodes containing the tag.
   * These tags can be later referenced from within the schema using <i>ref</i> attribute.
   * The key is the value of tag attribute <i>name</i> of the corresponding tag.
   */
  private final Map<String, org.w3c.dom.Element> referenced = new HashMap<String, org.w3c.dom.Element>();

  /**
   * Construct this parser, set loglevel as defined in XSD Import properties.
   */
  public DOMHandler() {
    LOG.setLevel(XSDImportSettings.getLogLevel());
  }

  /**
   * Create rule-trees from direct children of the <code>root</code> node of DOM tree.
   * <p>
   * Returns list of <code>Element</code>s containing complete rule-trees of the top level tags.
   * This method recursively builds rule-trees for all its sub-nodes,
   * thus should be invoked only once, on the <i>schema</i> tag node itself.
   * </p>
   * @param root Root of the DOM tree, (<i>schema</i> tag) node.
   * @return Rule-trees of all top level <i>element</i> tags, or empty list if schema was empty.
   */
  public List<Element> createRuleTrees(final org.w3c.dom.Element root) throws XSDException, InterruptedException {
    // All <i>complexType</i> tags with <i>name</i> tag attribute are added to the <code>namedCTypes</code> map.
    // All <i>element</i> tags with <i>name</i> tag attribute are added to the <code>referenced</code> map.
    // Then, the whole rule-tree is built using the DOM tree and the two maps.
    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
      // check if child is of type ELEMENT_NODE as defined by DOM spec.
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
        InterruptChecker.checkInterrupt();
      }
      // if domElem == null, it's not interesting for us
    }
    // only after we know all available complex types can we parse the rule subtrees!
    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
      final org.w3c.dom.Element domElem = DOMHelper.getDOMElement(root.getChildNodes().item(i));
      // every element must be added to roots
      if (domElem != null && XSDTag.ELEMENT.equals(XSDTag.matchName(XSDUtility.trimNS(domElem.getNodeName())))) {
        // XSD Schema specification states that use of minOccurs and maxOccurs tag attributes
        // cannot be used for element tag directly under schema tag
        // that's why we throw away the second value of returned pair
        roots.add(buildRuleSubtree(domElem,
                               new ArrayList<String>(),
                               new ArrayList<org.w3c.dom.Element>()).getFirst());
      }
      // else .. we are not interested in the node
    }
    return roots;
  }

  // #########################################################################       PRIVATE METHODS

  /**
   * Add <code>DOM.Element</code> to the <code>referenced</code> map.
   * This means that the <i>element</i> tag can be pointed at, by another <i>element</i> tag from within the schema,
   * using <i>ref</i> tag attribute.
   * XSD Schema only allows referencing of <i>element</i> tags that are directly under <i>schema</i> tag.
   * @param domElem Element Element tag directly under schema tag.
   */
  private void addToReferenced(final org.w3c.dom.Element domElem) {
    final String name = domElem.getAttribute(XSDAttribute.NAME.toString());
    if (!BaseUtils.isEmpty(name)) {
      if (!referenced.containsKey(name)) {
        if (verbose) {
          LOG.debug(NbBundle.getMessage(DOMHandler.class, "Debug.AddingToRefd", name));
        }
        referenced.put(name, domElem);
      } else {
        LOG.warn(NbBundle.getMessage(DOMHandler.class, "Warn.DuplicateElement", name));
      }
    }
  }

  /**
   * Add <code>DOM.Element</code> to the <code>namedCTypes</code> map.
   * The map key is created from the value of <i>name</i> tag attribute of the tag.
   * @param domElem Node of the DOM tree containing the <i>complexType</i> tag.
   */
  private void addNamedCType(final org.w3c.dom.Element domElem) {
    final String name = domElem.getAttribute(XSDAttribute.NAME.toString());
    if (!BaseUtils.isEmpty(name)) {
      if (!namedCTypes.containsKey(name)) {
        if (verbose) {
          LOG.debug(NbBundle.getMessage(DOMHandler.class, "Debug.AddingCType", name));
        }
        namedCTypes.put(name, domElem);
      } else {
        LOG.warn(NbBundle.getMessage(DOMHandler.class, "Warn.DuplicateCType", name));
      }
    } else {
      LOG.warn(NbBundle.getMessage(DOMHandler.class, "Warn.NoCTypeName", domElem.getParentNode().getNodeName()));
    }
  }

  // ####################################################################           SUBTREE BUILDING

  /**
   * Build a tree of {@link Element}s that represent the rules in Initial Grammar,
   * starting with current node.
   * This method examines the relationship between a given node and its children to create
   * a rule, then recursively builds the rule-tree for each of the children.
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
                                   final List<org.w3c.dom.Element> visited) throws XSDException, InterruptedException {

    InterruptChecker.checkInterrupt();

    final List<org.w3c.dom.Element> newVisited = new ArrayList<org.w3c.dom.Element>(visited);
    final List<String> newContext = new ArrayList<String>(context);

    final RegexpInterval interval = DOMHelper.determineInterval(currentNode);
    final XSDTag tag = XSDTag.matchName(XSDUtility.trimNS(currentNode.getNodeName()));

    // RET is the Element that is returned from this recursive method
    // most methods below use this instance of RET to store data in it,
    final Element ret = Element.getMutable();
    ret.getContext().addAll(context);
    ret.getMetadata().putAll(XSDUtility.prepareMetadata(interval));

    // inspect self
    final Element self = inspectSelf(currentNode, tag, ret, context, visited, newContext, newVisited, interval);
    if (self != null) {
      return new ImmutablePair<Element, RegexpInterval>(self, interval);
    } // else the ret element has all the changes needed

    // inspect children
    final NodeList children = currentNode.getChildNodes();
    inspectChildren(children, tag, ret, newContext, newVisited, interval);

    if (XSDTag.ELEMENT.equals(tag)) {
      DOMHelper.finalizeElement(ret, newContext);
    }
    return new ImmutablePair<Element, RegexpInterval>(ret, interval);
  }

  private Element inspectSelf(final org.w3c.dom.Element currentNode,
                              final XSDTag tag,
                              final Element ret,
                              final List<String> context,
                              final List<org.w3c.dom.Element> visited,
                              final List<String> newContext,
                              final List<org.w3c.dom.Element> newVisited,
                              final RegexpInterval interval) throws XSDException, InterruptedException {
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
        LOG.warn(NbBundle.getMessage(DOMHandler.class, "Warn.UnsupportedTag", currentNode.getTagName()));
    }
    return null;
  }

  private void inspectChildren(final NodeList children,
                               final XSDTag tag,
                               final Element ret,
                               final List<String> newContext,
                               final List<org.w3c.dom.Element> newVisited,
                               final RegexpInterval interval) throws XSDException, InterruptedException {
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
            LOG.warn(NbBundle.getMessage(DOMHandler.class, "Warn.UnsupportedTag", child.getTagName()));
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
                                 final List<org.w3c.dom.Element> newVisited) throws InterruptedException {
    // check if node was visited previously
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
      throw new XSDException(NbBundle.getMessage(DOMHandler.class, "Error.NameNORRef"));
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
          LOG.debug(NbBundle.getMessage(DOMHandler.class, "Debug.VisitedBefore"));
        }
        return DOMHelper.createSentinel(currentNode, context, XSDAttribute.NAME);
      }
    }
    return null;
  }

  private Element checkElementRefAttribute(final org.w3c.dom.Element currentNode,
                                           final List<String> context) {
    if (XSDTag.SCHEMA.toString().equals(XSDUtility.trimNS(currentNode.getParentNode().getNodeName()))) {
      throw new XSDException(NbBundle.getMessage(DOMHandler.class, "Error.RefNotAllowed"));
    } else if (!referenced.containsKey(currentNode.getAttribute(XSDAttribute.REF.toString()))) {
      throw new XSDException(NbBundle.getMessage(DOMHandler.class, "Error.RefNotResolved", currentNode.getAttribute(XSDAttribute.REF.toString())));
    } else {
      return DOMHelper.createSentinel(currentNode, context, XSDAttribute.REF);
    }
  }

  private Element checkElementComplexType(final org.w3c.dom.Element currentNode,
                                          final Element ret,
                                          final List<String> newContext,
                                          final List<org.w3c.dom.Element> newVisited,
                                          final String name) throws InterruptedException {
    if (!BaseUtils.isEmpty(currentNode.getAttribute(XSDAttribute.TYPE.toString()))) {
      // element has defined type
      final String type = XSDUtility.trimNS(currentNode.getAttribute(XSDAttribute.TYPE.toString()));
      if (XSDBuiltInDataTypes.isBuiltInType(type)) {
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
        LOG.error(NbBundle.getMessage(DOMHandler.class, "Error.TypeNotFound", type, name));
      }
    }
    return null;
  }

  // ####################################################################       HANDLING OF CHILDREN

  private void handleChildElement(final Element ret,
                                  final org.w3c.dom.Element child,
                                  final XSDTag tag,
                                  final XSDTag childTag,
                                  final List<String> newContext,
                                  final List<org.w3c.dom.Element> newVisited,
                                  final RegexpInterval interval) throws XSDException, InterruptedException {
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
                                      final List<org.w3c.dom.Element> newVisited) throws XSDException, InterruptedException {
    if (XSDTag.ELEMENT.equals(tag)) {
      DOMHelper.extractSubnodesFromContainer(buildRuleSubtree(child, newContext, newVisited).getFirst(), ret, CONTAINER_CTYPE);
    } else if (XSDTag.REDEFINE.equals(tag)) {
      LOG.warn(DOMHelper.warnUnsupported(childTag, tag));
    } else {
      // parent is also complexType tag, or other unsuitable tag
      throw new XSDException(DOMHelper.errorWrongNested(childTag, tag));
    }
    // complexType tag directly under schema tag is handled in createRuleTrees()
  }

  private void handleChildAll(final Element ret,
                              final org.w3c.dom.Element child,
                              final XSDTag tag,
                              final XSDTag childTag,
                              final List<String> newContext,
                              final List<org.w3c.dom.Element> newVisited) throws XSDException, InterruptedException {
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
                                         final List<org.w3c.dom.Element> newVisited) throws XSDException, InterruptedException {
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
    // firstly, get the tag attribute 'name' or 'ref'
    // we don't resolve the 'ref' at all (out of assignment), just register it's there
    final String attrName = DOMHelper.getAttributeName(child);
    if (BaseUtils.isEmpty(attrName)) {
      // this should never happen
      LOG.error(NbBundle.getMessage(DOMHandler.class, "Error.AttributeNoName", ret.getName()));
      return;
    }
    // if we have the name or ref, add the contents of the attribute to parent element (ret)
    String attrType = XSDUtility.trimNS(child.getAttribute(XSDAttribute.TYPE.toString()));
    if (!XSDBuiltInDataTypes.isBuiltInType(attrType)) {
      attrType = "";
    }
    ret.getAttributes().add(
      new Attribute(newContext, attrName, DOMHelper.getAttributeMetadata(child), attrType, new ArrayList<String>(0)));
  }

}
