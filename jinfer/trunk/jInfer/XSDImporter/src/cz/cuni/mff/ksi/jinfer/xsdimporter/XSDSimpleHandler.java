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

package cz.cuni.mff.ksi.jinfer.xsdimporter;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author reseto
 */
class XSDSimpleHandler extends DefaultHandler {

  /** Stack of elements of type Element, to be used for creating rules */
  private final Stack<AbstractNode> ruleElementStack = new Stack<AbstractNode>();
  /** Stack of XSDDocumentElements to be used only internally for storing data and correct nesting */
  private final Stack<XSDDocumentElement> documentElementStack = new Stack<XSDDocumentElement>();
  /** Rules that have been inferred so far. */
  private final List<AbstractNode> rules = new ArrayList<AbstractNode>();
  /** HashMap of named complextypes, saved as templates for creating rules */
  private final Map<String, Regexp<AbstractNode>> namedTypes = new HashMap<String, Regexp<AbstractNode>>();

  private final Properties properties = RunningProject.getActiveProjectProps();

  private static final Logger LOG = Logger.getLogger(XSDProcessor.class);

  private String nameSpace = null;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    //TODO reseto nejaky debug? LOG.warn("XSD: start of element '" + qName + "' with " + attributes.getLength() + " attributes.");

    // first, create xsd-element that can be pushed into documentElementStack
    XSDDocumentElement docElement = new XSDDocumentElement(trimNS(qName));
    for (int i = 0; i < attributes.getLength(); i++) {
      XSDAttributeData data = new XSDAttributeData(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i), attributes.getValue(i));
      docElement.getAttrs().put(attributes.getQName(i).toLowerCase(), data);
    }

    
    if (docElement.getName().equalsIgnoreCase("element")) {
      // we are dealing with <xs:element name="somename" type="sometype">
      createAndPushRuleElement(docElement);
    }

    //TODO reseto take care of complextype
    
    if (!documentElementStack.isEmpty() && documentElementStack.peek().isNamedComplexType()) {
      // remember: name of the complex type is the value of its attribute 'name'
      String name = documentElementStack.peek().getAttrs().get("name").getValue();
      createNamedComplexType(docElement, name);
    }

    documentElementStack.push(docElement);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);

    XSDDocumentElement docElement = documentElementStack.pop();
    if ( !docElement.getName().equalsIgnoreCase(trimNS(qName)) ) {
      throw new IllegalArgumentException("unpaired element");
    }
    
    if (trimNS(qName).equalsIgnoreCase("element")) {
      // we are dealing with <xs:element name="somename" type="sometype">
      AbstractNode last = ruleElementStack.pop();
      /*TODO reseto: konci element, pozri sa ci bol simple data typu
       * ak bol, tak nic, ak bol nejakeho pomenovaneho typu->zarad ho na resolvnutie
       */
      rules.add(last);
    } else if (trimNS(qName).equalsIgnoreCase("schema")) {
      // schema element closes, do additional checking
      LOG.warn("XSD: schema registered following " + namedTypes.size() + " named types");
      for (Iterator<String> it = namedTypes.keySet().iterator(); it.hasNext();) {
        String name = it.next();
        LOG.warn("\t" + name + ": " + namedTypes.get(name).getType().toString() + " has " + namedTypes.get(name).getChildren().size() + " children");
      }

    }
  }

  List<AbstractNode> getRules() {
    return rules;
  }

  /**
   * Trims (cuts) namespace prefix from the beginning of element qName
   */
  private String trimNS(String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1).toLowerCase();
  }

  private List<String> getContext() {
    if (ruleElementStack.isEmpty()) {
      return new ArrayList<String>(0);
    }
    final List<String> ret = new ArrayList<String>(ruleElementStack.peek().getContext());
    ret.add(ruleElementStack.peek().getName());
    return ret;
  }

  private void createAndPushRuleElement(XSDDocumentElement docElement) {
    final List<String> context = getContext();

    final Map<String, Object> metadata = new HashMap<String, Object>();

    String key = "minoccurs";
    determineOccurence(docElement, metadata, key);
    key = "maxoccurs";
    determineOccurence(docElement, metadata, key);

    final Element elem = new Element(context, docElement.getAttrs().get("name").getValue(), metadata,
          Regexp.<AbstractNode>getConcatenation());

    // if there is parent element, it sits at the top of the stack
    // we add the current element to its parent's rule
    if (!ruleElementStack.isEmpty() && (ruleElementStack.peek().getType().equals(NodeType.ELEMENT))) {
      ((Element) ruleElementStack.peek()).getSubnodes().addChild(Regexp.<AbstractNode>getToken(elem));
    }

    // if we have an element that is descendant to a named ComplexType,
    // add it to the CTypes children
    if ( !documentElementStack.isEmpty() && !documentElementStack.peek().getAssociatedCTypeName().equals("") ) {
      namedTypes.get(documentElementStack.peek().getAssociatedCTypeName()).addChild(Regexp.<AbstractNode>getToken(elem));
    }

    // push the current element to the stack with an empty rule
    ruleElementStack.push(elem);
  }

  /** Add information about element occurence to its metadata.
   * To be used with keys "minoccurs" and "maxoccurs".
   * @param docElement
   * @param metadata
   * @param key
   */
  private void determineOccurence(XSDDocumentElement docElement, Map<String, Object> metadata, String key) {
    if (docElement.getAttrs().containsKey(key)) {
      XSDAttributeData data = docElement.getAttrs().get(key);
      if (data.getValue().equalsIgnoreCase("unbounded")) {
        if (key.equalsIgnoreCase("minoccurs")) {
          throw new IllegalArgumentException("Attribute minOccurs cannot be 'unbounded'.");
        }
        metadata.put(key, new Integer(-1));
      } else {
        // if parseInt crashes, it's not our problem - valid schema uses only non negative integers
        metadata.put(key, Integer.parseInt(data.getValue()));
      }
    }
  }
  
  /**
   * Create pair String-Regexp to be put in namedTypes Map.
   * Regexp<AbstractNode> will contain children,
   * which are later used as a type template to create rules.
   * 
   * @param docElement
   * @param name
   */
  private void createNamedComplexType(XSDDocumentElement docElement, String name) {
    if (docElement.getName().equalsIgnoreCase("sequence")) {
      Regexp<AbstractNode> data = new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.CONCATENATION);
      namedTypes.put(name, data);
      docElement.setAssociatedCTypeName(name);
    } else if (docElement.getName().equalsIgnoreCase("all")) {
      Regexp<AbstractNode> data = new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.KLEENE);
      namedTypes.put(name, data);
      docElement.setAssociatedCTypeName(name);
    } else if (docElement.getName().equalsIgnoreCase("choice")) {
      Regexp<AbstractNode> data = new Regexp<AbstractNode>(null, new ArrayList<Regexp<AbstractNode>>(), RegexpType.ALTERNATION);
      namedTypes.put(name, data);
      docElement.setAssociatedCTypeName(name);
    } else {
      // we have complexContent with possibly some extension, for now just concatenate
      Regexp<AbstractNode> data = Regexp.<AbstractNode>getConcatenation();
      namedTypes.put(name, data);
      docElement.setAssociatedCTypeName(name);
    }
  }
}
