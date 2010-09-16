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
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
  private final Stack<Element> contentStack = new Stack<Element>();
  /** Stack of XSDDocumentElements to be used only internally for storing data and correct nesting */
  private final Stack<XSDDocumentElement> docElementStack = new Stack<XSDDocumentElement>();
  /** Rules that have been inferred so far. */
  private final List<AbstractNode> rules = new ArrayList<AbstractNode>();
  /** HashMap of named complextypes, saved as templates for creating rules */
  private final Map<String, Regexp<AbstractNode>> namedTypes = new HashMap<String, Regexp<AbstractNode>>();

  private static final Logger LOG = Logger.getLogger(XSDProcessor.class);

  private final String CONTAINER_NAME = "__conTAIner__";

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    //TODO reseto nejaky debug?
    LOG.warn("XSD: start of element '" + qName + "' with " + attributes.getLength() + " attributes.");

    // first, create xsd-element that can be pushed into documentElementStack
    XSDDocumentElement docElement = new XSDDocumentElement(trimNS(qName));
    for (int i = 0; i < attributes.getLength(); i++) {
      XSDAttributeData data = new XSDAttributeData(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i), attributes.getValue(i));
      docElement.getAttrs().put(attributes.getQName(i).toLowerCase(), data);
    }

    if (docElement.getName().equalsIgnoreCase("element")) {
      // we are dealing with <xs:element name="somename" type="sometype">
      createAndPushContent(docElement, false);
    }
    
    if (isOrderIndicator(docElement)) {
      createAndPushContent(docElement, true);
    }

    if (!docElementStack.isEmpty()) {
      if (docElementStack.peek().isNamedComplexType()) {
        // remember: name of the complex type is the value of its attribute 'name'
        String name = docElementStack.peek().getAttrs().get("name").getValue();
        docElement.setAssociatedCTypeName(name);
      } else if (docElementStack.peek().isComplexType()) {
        docElement.associateWithUnnamedCType();
      }
    }

    docElementStack.push(docElement);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);

    XSDDocumentElement docElement = docElementStack.pop();
    
    if ( !docElement.getName().equalsIgnoreCase(trimNS(qName)) ) {
      throw new IllegalArgumentException("unpaired element");
    }

    if (isOrderIndicator(docElement)) {
      // here we have a container on the contentStack, 
      // we can forget its entire contents except for Subnodes!
      if (docElement.getAssociatedCTypeName().equals("")) {
        // container is not associated to any named complexType
        // it is a local declaration of complexType, or nested container
        // now we have to add its contents as its parent's child
        Element container = contentStack.pop();
        if (docElement.isAssociated()) {
          contentStack.peek().getSubnodes().addChild(Regexp.<AbstractNode>getToken(container));
        } else {
          contentStack.peek().getSubnodes().addChild(container.getSubnodes());
        }
      } else {
        // this container is associated with a named complexType,
        // we don't have to add it as a child to parent element (there is none)
        // just create a mapping with this name
        namedTypes.put(docElement.getAssociatedCTypeName(), contentStack.pop().getSubnodes());
      }
    }
    
    if (trimNS(qName).equalsIgnoreCase("element")) {
      // we are dealing with <xs:element name="somename" type="sometype">

      boolean unresolved = false;
      if (docElement.getAttrs().containsKey("type")) {
        if (!SimpleDataTypes.isSimpleDataType(docElement.getAttrs().get("type").getValue())) {
          //TODO reseto add this to parent element as UNRESOLVED node
          if (namedTypes.containsKey(docElement.getAttrs().get("type").getValue())) {
            Element old = contentStack.pop();
            Element ruleElement = new Element(old.getContext(), old.getName(), old.getMetadata(), namedTypes.get(docElement.getAttrs().get("type").getValue()));
            rules.add(ruleElement);
            unresolved = true;
          } else {
            unresolved = true;
          }
        }
      }

      if (!unresolved) {
        Element old = contentStack.pop();
        int size = old.getSubnodes().getChildren().size();
        if (size == 0) {
          // this is an empty element, we can add this directly as a child to the parent
          if (!contentStack.isEmpty()) {
            contentStack.peek().getSubnodes().addChild(Regexp.<AbstractNode>getToken(old));
          }
          rules.add(old);
        } else if (size == 1) {
          // this should be an element containing a complextype with nested container
          // we need to remove this element entirely, because it may have wrong type of Subnodes
          if (old.getSubnodes().getChild(0).isToken() && old.getSubnodes().getChild(0).getContent().getName().equals(CONTAINER_NAME) ) {
            Element newContent = (Element) old.getSubnodes().getChild(0).getContent();
            Element ruleElement = new Element(old.getContext(), old.getName(), old.getMetadata(), newContent.getSubnodes());
            rules.add(ruleElement);
          }
        } else {
          //TODO reseto size more than 1
          // size is greater, means there are some attributes and whatnot
          // one of the children should be a container, so find it and work it
          
        }
      }

      
    } else if (trimNS(qName).equalsIgnoreCase("schema")) {
      // schema element closes, do additional checking
      LOG.warn("XSD: schema registered following " + namedTypes.size() + " named types");
      for (Iterator<String> it = namedTypes.keySet().iterator(); it.hasNext();) {
        String name = it.next();
        int numChildren = namedTypes.get(name).getChildren().size();
        LOG.warn("\t" + name + "-" + namedTypes.get(name).getType().toString().toLowerCase() + " has " + numChildren + " children:");
        for (int i = 0; i < numChildren; i++) {
          Regexp<AbstractNode> child = namedTypes.get(name).getChild(i);
          LOG.warn("\t\t"+ child.getContent().getName());
        }
      }
    }

    /* DO THIS WHEN ELEMENT ENDS!!!
    // if there is parent element, it sits at the top of the stack
    // we add the current element to its parent's rule
    if (!isContainer && !ruleElementStack.isEmpty()
        && (ruleElementStack.peek().getType().equals(NodeType.ELEMENT)))
    {
      ((Element) ruleElementStack.peek()).getSubnodes().addChild(Regexp.<AbstractNode>getToken(elem));
    }


    // if we have an element that is descendant to a named ComplexType,
    // add it to the CTypes children
    if (!isContainer &&  !documentElementStack.isEmpty()
        && !documentElementStack.peek().getAssociatedCTypeName().equals("") )
    {
      namedTypes.get(documentElementStack.peek().getAssociatedCTypeName()).addChild(Regexp.<AbstractNode>getToken(elem));
    }
    */
    //TODO reseto take care of complextype
  }

  List<AbstractNode> getRules() {
    return rules;
  }

  // **************************************************** private simple methods

  /**
   * Trims (cuts) namespace prefix from the beginning of element qName
   */
  private String trimNS(String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1).toLowerCase();
  }

  private List<String> getContext(boolean isContainer) {
    if (contentStack.isEmpty()) {
      return new ArrayList<String>(0);
    }
    final List<String> ret = new ArrayList<String>(contentStack.peek().getContext());
    if (!isContainer) {
      ret.add(contentStack.peek().getName());
    }
    return ret;
  }

  private boolean isOrderIndicator(XSDDocumentElement docElem) {
    return (docElem.getName().equalsIgnoreCase("choice")
            || docElem.getName().equalsIgnoreCase("all")
            || docElem.getName().equalsIgnoreCase("sequence")) ? true : false;
  }

  // *************************************************** private complex methods

  private void createAndPushContent(XSDDocumentElement docElement, boolean isContainer) {
    
    /** If current docElement is an OrderIndicator (sequence/all/choice)
     * we add it to the ruleElementStack, but it's not a regular Element, it's only a container
     * it's there only to support recursive definitions like this:
     *
     * <xs:complexType name="personinfo">
     *   <xs:sequence>
     *     <xs:element name="firstname" type="xs:string"/>
     *     <xs:element name="lastname" type="xs:string"/>
     *     <xs:choice>
     *        <xs:element name="homePhone" type="xs:string"/>
     *        <xs:element name="mobilePhone" type="xs:string"/>
     *        <xs:element name="workPhone" type="xs:string"/>
     *     </xs:choice>
     *   </xs:sequence>
     * </xs:complexType>
     *
     * On the other hand, when an element starts, we have to put something on the stack,
     * to remember the context, but we don't know the type of it's Regexp
     * -> by default it's CONCATENATION,
     * but when the element ends it's changed according to its content
     * => that is why an element is added as a child only when it ends!!
     */
    
    final Map<String, Object> metadata = new HashMap<String, Object>();

    String name = "";
    if (isContainer) {
      metadata.put(CONTAINER_NAME, Boolean.TRUE);
      name = CONTAINER_NAME;
    } else {
      String key = "minoccurs";
      determineOccurence(docElement, metadata, key);
      key = "maxoccurs";
      determineOccurence(docElement, metadata, key);
      name = docElement.getAttrs().get("name").getValue();
    }

    Element elem;
    final List<String> context = getContext(isContainer);

    if (docElement.getName().equalsIgnoreCase("sequence")) {
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getConcatenation(new ArrayList<Regexp<AbstractNode>>()) );
    } else if (docElement.getName().equalsIgnoreCase("choice")) {
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getAlternation(new ArrayList<Regexp<AbstractNode>>()) );
    } else if (docElement.getName().equalsIgnoreCase("all")) {
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getKleene(new ArrayList<Regexp<AbstractNode>>()) );
    } else {
      // this in not a container, create element with regexp of any type,
      // it will be changed later to the correct one
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getConcatenation());
    }
        
    // push the current element to the stack with an empty rule
    contentStack.push(elem);
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
  
}
