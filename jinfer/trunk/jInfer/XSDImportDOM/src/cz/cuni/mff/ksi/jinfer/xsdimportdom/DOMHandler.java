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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
      roots.add(buildRuleSubtree(domElem, new ArrayList<String>(), domElem));
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

  private Element buildRuleSubtree(org.w3c.dom.Element currentNode, List<String> context, org.w3c.dom.Element subRoot) {
    return Element.getMutable();
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
  
  private Element getElementFromDOMElement(final org.w3c.dom.Element domElem, List<String> context) {
    final Element ret = Element.getMutable();
    ret.getContext().addAll(context);


    if (BaseUtils.isEmpty(domElem.getAttribute(XSDAttribute.NAME.toString()))
        && domElem.hasAttribute(XSDAttribute.REF.toString())) {
      
    }
    //ret.setName(getNameOrRef(domElem));
    //TODO reseto what if element has ref ...
    //TODO reseto implement logic! :)

    // firstly inspect self

    // inspect each child by the tag name (type)
    final NodeList children = domElem.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      final org.w3c.dom.Element domEl = isDOMElement(children.item(i));
      if (domEl != null) {
        final XSDTag tagName = XSDTag.matchName(trimNS(domEl.getNodeName()));
        switch(tagName) {
          case ELEMENT:
            // recursion to add all relevant children to the ret
            ret.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(getElementFromDOMElement(domElem, null)));
            break;
          case COMPLEXTYPE:
            if (XSDTag.matchName(domElem.getNodeName()).equals(XSDTag.COMPLEXTYPE))
            if (!domEl.hasAttribute(XSDAttribute.NAME.toString())) {
              final Element container = getElementFromDOMElement(domEl, null);
              ret.getAttributes().addAll(container.getAttributes());

              // add all children
              for (Regexp<AbstractStructuralNode> child : container.getSubnodes().getChildren()) {
                ret.getSubnodes().addChild(child);
              }
            } else {
              throw new XSDException("ComplexType declaration under element tag must NOT have a name attribute specified.");
            }
            break;
          case ALL:
            break;
          case CHOICE:
            break;
          case SEQUENCE:
            break;
          default:
            //TODO reseto use this, it is strong ;)
        }
        ret.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(getElementFromDOMElement(domEl, null)));
      }
    }
    //TODO reseto test if ret has 0 subnodes and set type to lambda if so
    
    return ret;
  }
}
