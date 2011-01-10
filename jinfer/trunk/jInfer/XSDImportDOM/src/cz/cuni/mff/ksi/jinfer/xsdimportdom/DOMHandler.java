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
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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

  private static final String NAME = "name";
  private static final String REF = "ref";

  private final List<Element> roots = new ArrayList<Element>();
  private final Map<String, org.w3c.dom.Element> namedCTypes = new HashMap<String, org.w3c.dom.Element>();


  public DOMHandler() {
    LOG.setLevel(settings.logLevel());
  }

  public List<Element> getRules() {
    final List<Element> rules = new ArrayList<Element>();
    for (Element root : roots) {
      //final List<Element> elementRules = new ArrayList<Element>();
      rules.add(root);
      //getRulesFromElement(root, elementRules);
      //rules.addAll(elementRules);
    }
    return rules;
  }

  private void getRulesFromElement(final Element root, final List<Element> elementRules) {
    for (AbstractStructuralNode node : root.getSubnodes().getTokens()) {
      if (node.isElement()) {
        elementRules.add((Element) node);
        getRulesFromElement((Element) node, elementRules);
      }
    }
  }

  public void parse(final InputStream stream) {
    try {
      final DOMParser parser = new DOMParser();
      parser.parse(new InputSource(stream));
      final Document doc = parser.getDocument();
      final org.w3c.dom.Element root = doc.getDocumentElement();
      
      registerNamedCTypes(root);
      registerRootElements(root);

    } catch (SAXException ex) {
      Exceptions.printStackTrace(ex);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

  }

  private void registerNamedCTypes(final org.w3c.dom.Element root) {
    final NodeList namedCTypeNodes = root.getElementsByTagName(XSDTag.COMPLEXTYPE.getName());
    for (int i = 0; i < namedCTypeNodes.getLength(); i++) {
      final org.w3c.dom.Element el = (org.w3c.dom.Element) namedCTypeNodes.item(i);
      if (el.hasAttribute(NAME)) {
        addNamedCType(el);
      }
    }
  }

  private void registerRootElements(final org.w3c.dom.Element root) throws XSDException {
    final NodeList rootChildren = root.getChildNodes();
    for (int i = 0; i < rootChildren.getLength(); i++) {
      final org.w3c.dom.Element domEl = isDOMElement(rootChildren.item(i));
      if (domEl != null) {
        final XSDTag nameTag = XSDTag.matchName(trimNS(domEl.getNodeName()));
        switch (nameTag) {
          case ELEMENT:
            roots.add(getElementFromDOMElement(domEl, Collections.<String>emptyList()));
            break;
          case COMPLEXTYPE:
            if (domEl.hasAttribute(NAME)) {
              addNamedCType(domEl);
            } else {
              throw new XSDException("ComplexType declaration directly under schema tag must have a name attribute specified.");
            }
            break;
          default:
        //TODO reseto use this, it is strong ;)
        }
      }
    }
  }

  /**
   * Trims (cuts) namespace prefix from the beginning of element qName and returns it in original case.
   */
  private String trimNS(final String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1);
  }

  /**
   * Determine if given node is in fact <i>org.w3c.dom.Element</i> and return a type cast if so.
   * @param node
   * @return Parameter cast to <i>org.w3c.dom.Element</i>, or <code>null</code>.
   * @see org.w3c.dom.Element
   */
  private org.w3c.dom.Element isDOMElement(final Node node) {
    if (node.getNodeType() == Node.ELEMENT_NODE) { // means the node is a DOM ELEMENT
      return (org.w3c.dom.Element) node;
    }
    return null;
  }

  private void addNamedCType(final org.w3c.dom.Element el) {
    LOG.info("Adding complextype: " + el.toString());
    final String name = el.getAttribute(NAME);
    if (!namedCTypes.containsKey(name)) {
      namedCTypes.put(name, el);
    }
  }

  private Element getElementFromDOMElement(final org.w3c.dom.Element el, List<String> context) {
    final Element ret = Element.getMutable();
    ret.setName(getNameOrRef(el));
    ret.getContext().addAll(context);
    //TODO reseto what if element has ref ...
    //TODO reseto implement logic! :)

    // firstly inspect self

    // inspect each child by the tag name (type)
    final NodeList children = el.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      final org.w3c.dom.Element domEl = isDOMElement(children.item(i));
      if (domEl != null) {
        final XSDTag tagName = XSDTag.matchName(trimNS(domEl.getNodeName()));
        switch(tagName) {
          case ELEMENT:
            // recursion to add all relevant children to the ret
            ret.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(getElementFromDOMElement(el, null)));
            break;
          case COMPLEXTYPE:
            if (XSDTag.matchName(el.getNodeName()).equals(XSDTag.COMPLEXTYPE))
            if (!domEl.hasAttribute(NAME)) {
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

  private String getNameAttr(final org.w3c.dom.Element el) {
    if (el.hasAttribute(NAME) && !el.getAttribute(NAME).equals("")) {
      return el.getAttribute(NAME);
    } else {
      return null;
    }
  }

  private String getNameOrRef(final org.w3c.dom.Element el) {
    String ret = getNameAttr(el);
    if (ret == null && el.hasAttribute(REF) && !el.getAttribute(REF).equals("")) {
      ret = el.getAttribute(REF);
    }
    if (ret != null) {
      return ret;
    } else {
      throw new XSDException("Unable to create Element with no name.");
    }
  }
}
