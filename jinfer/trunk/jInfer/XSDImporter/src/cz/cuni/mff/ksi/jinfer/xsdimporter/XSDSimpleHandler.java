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
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import java.util.ArrayList;
import java.util.HashMap;
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

    LOG.warn("XSD: start of element '" + qName + "' with " + attributes.getLength() + "attributes.");

    // first, create xsd-element that can be pushed into documentElementStack
    XSDDocumentElement docElement = new XSDDocumentElement(trimNS(qName));
    for (int i = 0; i < attributes.getLength(); i++) {
      XSDAttributeData data = new XSDAttributeData(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i).toLowerCase(), attributes.getType(i), attributes.getValue(i));
      docElement.getAttrs().put(attributes.getQName(i).toLowerCase(), data);
    }

    
    if (docElement.getName().equalsIgnoreCase("element")) {
      // we are dealing with <xs:element name="somename" type="sometype">
      createAndPushRuleElement(docElement);
    }

    //TODO reseto take care of complextype
    /*
    if (!documentElementStack.isEmpty()) {
      && documentElementStack.peek().isNamedComplexType()
    }


    ) {
      if (!docElement.getName().equalsIgnoreCase("element") && !docElement.getName().equalsIgnoreCase("schema"))
      docElement.setAssociatedCTypeName(documentElementStack.peek().getName());
    }
    */
    documentElementStack.push(docElement);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);

    LOG.warn("XSD: end of element '" + qName + "'");

    XSDDocumentElement docElement = documentElementStack.pop();
    if ( !docElement.getName().equalsIgnoreCase(trimNS(qName)) ) {
      throw new IllegalArgumentException("unpaired element");
    }
    
    if (trimNS(qName).equalsIgnoreCase("element")) {
      // we are dealing with <xs:element name="somename" type="sometype">
      AbstractNode last = ruleElementStack.pop();
      rules.add(last);
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

      final Element e = new Element(context, docElement.getAttrs().get("name").getValue(), null,
            Regexp.<AbstractNode>getConcatenation());

      // if there is parent element, it sits at the top of the stack
      // we add the current element to its parent's rule
      if (!ruleElementStack.isEmpty() && (ruleElementStack.peek().getType().equals(NodeType.ELEMENT))) {
        ((Element) ruleElementStack.peek()).getSubnodes().addChild(Regexp.<AbstractNode>getToken(e));
      }

      // push the current element to the stack with an empty rule
      ruleElementStack.push(e);
  }
}
