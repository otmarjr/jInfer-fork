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

import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDDocumentElement;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.SAXAttributeData;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.SimpleDataTypes;
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
 * SAX handler for importing XSD schemas, requires named ComplexType elements
 * to be declared before their usage in the schema!
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

  private static final Logger LOG = Logger.getLogger(XSDProcessorSAX.class);
/**
 * Pseudo-unique name for the container elements that are pushed in contentStack
 * the name should be distict from every element name in the actual schema
 */
  private final String CONTAINER_NAME = "__conTAIner__";

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    //TODO reseto debug? LOG.warn("XSD handler: start of element '" + qName + "' with " + attributes.getLength() + " attributes.");

    // first, create xsd-element that can be pushed into documentElementStack
    // this contains all attributes of the schema element
    XSDDocumentElement docElement = new XSDDocumentElement(trimNS(qName));
    for (int i = 0; i < attributes.getLength(); i++) {
      SAXAttributeData data = new SAXAttributeData(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i), attributes.getValue(i));
      docElement.getAttrs().put(attributes.getQName(i).toLowerCase(), data);
    }

    if (!docElementStack.isEmpty()) {
      // there is a special case when element follows
      if (docElementStack.peek().isNamedComplexType()) {
        // remember: name of the complex type is the value of its attribute 'name'
        String name = docElementStack.peek().getAttrs().get("name").getValue();
        docElement.setAssociatedCTypeName(name);
      } else if (docElementStack.peek().isComplexType()) {
        docElement.associateWithUnnamedCType();
      }
    }

    if (docElement.getName().equalsIgnoreCase("element")) {
      createAndPushContent(docElement, false);
    } else if (isOrderIndicator(docElement)) {
      createAndPushContent(docElement, true);
    } else if (docElement.getName().equalsIgnoreCase("complexcontent")) {
      LOG.warn("XSD handler: unsupported element complexContent, behavior is undefined");
    } else if (docElement.getName().equalsIgnoreCase("simplecontent")) {
      LOG.warn("XSD handler: unsupported element simpleContent, behavior is undefined");
    }

    docElementStack.push(docElement);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);

    XSDDocumentElement docElement = docElementStack.pop();
    
    if ( !docElement.getName().equalsIgnoreCase(trimNS(qName)) ) {
      throw new IllegalArgumentException("XSD handler: unpaired element");
    }

    // BIG IF -> we check what type of element ended [ element|complextype|schema| (choice|sequence|all) ]
    if (trimNS(qName).equalsIgnoreCase("element")) {

      Element old = contentStack.pop();

      String name = docElement.getAttrs().get("name").getValue();
      if (!old.getName().equals(name)) {
        throw new IllegalArgumentException("XSD handler: unexpected element on stack " + name);
      }
      
      if (docElement.getAttrs().containsKey("type")) {
        // deal with element that has specified type
        processTypedElement(old, docElement);
      } else {
        // element doesn't have a declared type => it must be empty or locally defined
        processUntypedElement(old);
      }
    } else if (isOrderIndicator(docElement)) {
      // this branch is for choice|sequence|all
      processEndOfOrderIndicator(docElement);
    } else if (trimNS(qName).equalsIgnoreCase("schema")) {
      // schema element closes, do additional checking
      LOG.warn("XSD handler: schema registered following " + namedTypes.size() + " named types");
      for (Iterator<String> it = namedTypes.keySet().iterator(); it.hasNext();) {
        String name = it.next();
        int numChildren = namedTypes.get(name).getChildren().size();
        LOG.warn("\t" + name + "-" + namedTypes.get(name).getType().toString().toLowerCase() + " has " + numChildren + " children:");
        for (int i = 0; i < numChildren; i++) {
          Regexp<AbstractNode> child = namedTypes.get(name).getChild(i);
          if (child.getContent()==null) {
            LOG.warn("\t\t<unnamed> " + child.getType().toString().toLowerCase());
          } else {
            LOG.warn("\t\t"+ child.getContent().getName());
          }
        }
      }
    }

    //TODO reseto take care of arguments
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

  /**
   * Add parameter as a child (token) to its parent (if it exists)
   * and add it to rules.
   * @param elem
   */
  private void addChildAndRule(Element elem) {
    if (!elem.getMetadata().containsKey("from.schema")) {
      elem.getMetadata().put("from.schema", Boolean.TRUE);
    }
    if (!contentStack.isEmpty()) {
      contentStack.peek().getSubnodes().addChild(Regexp.<AbstractNode>getToken(elem));
    }
    //TODO reseto reconsider this:
    //if (!docElementStack.isEmpty() && docElementStack.peek().getName().equalsIgnoreCase("schema")) {
    rules.add(elem);
  }

  private List<String> getContext() {

    final List<String> ret;

    if (contentStack.isEmpty()) {
      ret = new ArrayList<String>(0);
    } else {
      Element parent = contentStack.peek();
      ret = new ArrayList<String>(parent.getContext());
      
      if (contentStack.peek().getName().equals(CONTAINER_NAME)) {
        // double check if we have a container (in case container name was really used in schema)
        if (!parent.getMetadata().containsKey(CONTAINER_NAME)) {
          // when we construct the container, it has this metadata, so now we know it's not a container
          ret.add(contentStack.peek().getName());
        }
      } else {
        // parent is not a container, add its name to the context
        ret.add(contentStack.peek().getName());
      }
    }
    return ret;
  }

  /**
   * Check if element is one of xs:choice, xs:sequence, xs:all.
   * @param docElem
   * @return
   */
  private boolean isOrderIndicator(XSDDocumentElement docElem) {
    return (docElem.getName().equalsIgnoreCase("choice")
            || docElem.getName().equalsIgnoreCase("all")
            || docElem.getName().equalsIgnoreCase("sequence")) ? true : false;
  }

  // *************************************************** private complex methods

  /**
   * Processes an element with declared 'type' attribute.
   * @param old Element from contentStack, expected to be regular element.
   * @param docElement Node from documentElementStack.
   */
  private void processTypedElement(Element old, XSDDocumentElement docElement) {
    final String TYPE = docElement.getAttrs().get("type").getValue();

    if (SimpleDataTypes.isSimpleDataType(TYPE)) {
      // this element is simple => has no children
      addChildAndRule(old);
    } else {
      // we have to pair this element with its defined type
      // the problem is, that the complexType it may be associated with
      // can be declared later in the schema than this element
      if (namedTypes.containsKey(TYPE)) {
        // we are lucky and can be done with this element
        Element ruleElement = new Element(old.getContext(), old.getName(), old.getMetadata(), namedTypes.get(TYPE));
        addChildAndRule(ruleElement);
      } else {
        //TODO reseto add this to parent element as UNRESOLVED node
        LOG.warn("XSD handler: can't resolve the type of element " + old.getName());
        addChildAndRule(old);
      }
    }
  }
  /**
   * Processes an element with NO 'type' attribute.
   * @param old Element from contentStack, expected to be regular element.
   */
  private void processUntypedElement(Element old) {
    int size = old.getSubnodes().getChildren().size();

    if (size == 0) {
      // this is an empty element, we can add this directly as a child to the parent
      addChildAndRule(old);
    } else if (size == 1) {
      // this should be an element containing a complextype with nested container
      // we need to remove this element entirely, because it may have wrong type of Subnodes
      if (old.getSubnodes().getChild(0).isToken() && old.getSubnodes().getChild(0).getContent().getName().equals(CONTAINER_NAME) ) {
        Element newContent = (Element) old.getSubnodes().getChild(0).getContent();
        Element ruleElement = new Element(old.getContext(), old.getName(), old.getMetadata(), newContent.getSubnodes());
        addChildAndRule(ruleElement);
      } else {
        //TODO reseto probably handle a single argument?
        addChildAndRule(old);
      }
    } else {
      int hit = -1;
      for (int i=0; i<size; ++i) {
        if (old.getSubnodes().getChild(i).isToken() && old.getSubnodes().getChild(i).getContent().getName().equals(CONTAINER_NAME)) {
          hit = i;
          break;
        }
      }
      if (hit < 0) {
        throw new IllegalArgumentException("XSD Handler: element has no contents");
      }

      Element newContent = (Element) old.getSubnodes().getChild(hit).getContent();
      Element ruleElement = new Element(old.getContext(), old.getName(), old.getMetadata(), newContent.getSubnodes());

      // and now add all other children from old element to the new one
      for (int i=0; i<size; ++i) {
        if (i != hit) {
          ruleElement.getSubnodes().addChild(old.getSubnodes().getChild(i));
        }
      }
      addChildAndRule(ruleElement);
    }
  }

  private void processEndOfOrderIndicator(XSDDocumentElement docElement) {
    // ^^ here we have a container on the contentStack,
    // we can forget its entire contents except for Subnodes!
    Element container = contentStack.pop();

    if (docElement.getAssociatedCTypeName().equals("")) {
      // container is not associated to any named complexType
      // it is a local declaration of complexType, or nested container
      // now we have to add its contents as its parent's child

      if (docElement.isAssociated()) {
        contentStack.peek().getSubnodes().addChild(Regexp.<AbstractNode>getToken(container));
      } else {
        contentStack.peek().getSubnodes().addChild(container.getSubnodes());
      }
    } else {
      // this container is associated with a named complexType,
      // we don't have to add it as a child to parent element (there is none)
      // just create a mapping with this name
      namedTypes.put(docElement.getAssociatedCTypeName(), container.getSubnodes());
    }
  }

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
   * @param docElement Currently processed internal element.
   * @param isContainer True if docElement is an order indicator (choice,sequence,all). False if docElement is a proper xs:element.
   */
  private void createAndPushContent(XSDDocumentElement docElement, boolean isContainer) {
    
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
    final List<String> context = getContext();

    if (docElement.getName().equalsIgnoreCase("sequence")) {
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getConcatenation(new ArrayList<Regexp<AbstractNode>>()) );
    } else if (docElement.getName().equalsIgnoreCase("choice")) {
      //TODO reseto allow alternation
      //elem = new Element(context, name, metadata, Regexp.<AbstractNode>getAlternation(new ArrayList<Regexp<AbstractNode>>()) );
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getConcatenation(new ArrayList<Regexp<AbstractNode>>()) );
    } else if (docElement.getName().equalsIgnoreCase("all")) {
      //TODO reseto allow kleene
      //elem = new Element(context, name, metadata, Regexp.<AbstractNode>getKleene(new ArrayList<Regexp<AbstractNode>>()) );
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getConcatenation(new ArrayList<Regexp<AbstractNode>>()) );
    } else {
      // this in not a container, create element with regexp of any type,
      // it will be changed later to the correct one
      elem = new Element(context, name, metadata, Regexp.<AbstractNode>getConcatenationMutable());
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
      SAXAttributeData data = docElement.getAttrs().get(key);
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
