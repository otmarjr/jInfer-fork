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

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDDocumentElement;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.SAXAttributeData;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.SimpleDataTypes;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDMetadata;
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
class SAXHandler extends DefaultHandler {

  /** Stack of XSDDocumentElements to be used only internally for storing data and correct nesting */
  private final Stack<XSDDocumentElement> docElementStack = new Stack<XSDDocumentElement>();
  /** Stack of elements of type Element, to be used for creating rules */
  private final Stack<Element> contentStack = new Stack<Element>();
  /** Rules that have been inferred so far. */
  private final List<Element> rules = new ArrayList<Element>();
  /** HashMap of named complextypes, saved as templates for creating rules */
  private final Map<String, Pair<Element, List<Element>>> namedTypes = new HashMap<String, Pair<Element, List<Element>>>();
  /**
   * List of elements with unknown type when they were processed.
   */
  private final List<Pair<String, Element>> unresolved = new ArrayList<Pair<String, Element>>();

  private static final Logger LOG = Logger.getLogger(SAXHandler.class);
/**
 * Pseudo-unique name for the container elements that are pushed in contentStack
 * the name should be distict from every element name in the actual schema
 */
  private static final String CONTAINER_NAME = "__conTAIner__";

  private String currentRuleListName = "";

  /** Process start of XSDelement.
   * When XSDelement starts, we have to check for several things.
   * Firstly we maintain the stack of all XSDelements in the schema
   * to check if it's well formed (documentElementStack).
   *
   * Depending on what XSDelement started we create a new mutable Element
   * and push it to contentStack. This is the case if qName is 'element'
   * or 'complexType' and the complexType has a defined attribute name.
   * The type of this mutable Element is unknown at start, and is determined
   * by the outermost OrderIndicator (choice,sequence,all) in its content.
   * If there is no OrderIndicator, we pronounce the mutable Element a LAMBDA.
   *
   * The outermost OrderIndicator is said to be associated with a complexType,
   * it determines the type of the associated CType and therefore we don't
   * create a container.
   * If the O.I. is not associated - it is nested, and a container is needed.
   *
   * @param uri
   * @param localName
   * @param qName
   * @param attributes
   * @throws SAXException
   */
  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    //TODO reseto debug? LOG.warn("XSD handler: start of element '" + qName + "' with " + attributes.getLength() + " attributes.");

    // first, create xsd-element that can be pushed into documentElementStack
    // this contains all attributes of the schema element
    final XSDDocumentElement docElement = new XSDDocumentElement(trimNS(qName));
    for (int i = 0; i < attributes.getLength(); i++) {
      final SAXAttributeData data = new SAXAttributeData(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i), attributes.getValue(i));
      docElement.getAttrs().put(attributes.getQName(i).toLowerCase(), data);
    }

    if (!docElementStack.isEmpty() 
            && docElementStack.peek().isComplexType()
            && docElement.isOrderIndicator()) {
      // there is a special case when OrderI. follows any complex type
      docElement.associate();
    }

    if (docElement.getName().equalsIgnoreCase("element")) {
      createAndPushContent(docElement, false);

    } else if (docElement.isNamedComplexType()) {
      // named CType has no element to contain it, we must create a container
      createAndPushContent(docElement, true);
      // every element that is a descendant under a namedCType 
      // is not added to the global rules, but to the rules of a current CType
      namedTypes.put(docElement.attributeNameValue(), new Pair<Element, List<Element>>(null, new ArrayList<Element>(0)) );
      currentRuleListName = docElement.attributeNameValue();
      
    } else if (docElement.isOrderIndicator()) {
      if (docElement.isAssociated()) {
        contentStack.peek().getSubnodes().setType(determineRegexpType(docElement));
      } else {
        //OrderIndicator is nested, we create and push a container
        createAndPushContent(docElement, true);
      }
    } else if (docElement.getName().equalsIgnoreCase("complexcontent")) {
      LOG.warn("Unsupported element complexContent, behavior is undefined");
    } else if (docElement.getName().equalsIgnoreCase("simplecontent")) {
      LOG.warn("Unsupported element simpleContent, behavior is undefined");
    }

    docElementStack.push(docElement);
  }

  @Override
  public void endDocument() throws SAXException {
    super.endDocument();

    LOG.info("Schema registered following " + namedTypes.size() + " named types");
    for (final Iterator<String> it = namedTypes.keySet().iterator(); it.hasNext();) {
      final String name = it.next();

      if (!BaseUtils.isEmpty(namedTypes.get(name).getFirst().getSubnodes().getChildren())) {
        final int numChildren = namedTypes.get(name).getFirst().getSubnodes().getChildren().size();
        LOG.info("\t" + name + ": " + namedTypes.get(name).getFirst().getSubnodes().getType().toString().toLowerCase() + " has " + numChildren + " children:");
        for (int i = 0; i < numChildren; i++) {
          final Regexp<AbstractStructuralNode> child = namedTypes.get(name).getFirst().getSubnodes().getChild(i);
          if (child.getContent() == null) {
            LOG.info("\t\t<unnamed> " + child.getType().toString().toLowerCase());
          } else {
            LOG.info("\t\t"+ child.getContent().getName());
          }
        } //end for children
      } else {
        LOG.info("\t" + name + ": " + namedTypes.get(name).getFirst().getSubnodes().getType() + " has 0 children.");
      }
    }

    for (Pair<String, Element> pair : unresolved) {
      final boolean resolved = tryResolveElementType(pair.getFirst(), pair.getSecond());
      if (resolved) {
        pair.getSecond().setImmutable();
      } else {
        LOG.warn("Can't resolve the type of element " + pair.getSecond().getName() + "!");
      }
    }
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) throws SAXException {
    super.endElement(uri, localName, qName);

    final XSDDocumentElement docElement = docElementStack.pop();
    
    if ( !docElement.getName().equalsIgnoreCase(trimNS(qName)) ) {
      LOG.error("Unpaired element " + docElement.getName() + " and " + qName);
      throw new IllegalArgumentException("Unpaired element" + docElement.getName() + " and " + qName);
    }

    // BIG IF -> we check what type of element ended
    if (trimNS(qName).equalsIgnoreCase("element")) {
      processEndOfElement(docElement);

    } else if (docElement.isNamedComplexType()) {
      final Element container = contentStack.pop();
      if (!isContainer(container)) {
        LOG.error("Popped wrong element from stack " + container.getName());
        throw new IllegalArgumentException("Popped wrong element from stack " + container.getName());
      }
      
      final List<Element> oldRules = namedTypes.get(docElement.attributeNameValue()).getSecond();
      namedTypes.remove(docElement.attributeNameValue());
      namedTypes.put(docElement.attributeNameValue(), new Pair(container, oldRules));
      // stop adding rules to that CTypes rules
      currentRuleListName = "";

    } else if (docElement.isOrderIndicator()) {
      // this branch is for choice|sequence|all
      processEndOfOrderIndicator(docElement);
      
    } else if (docElement.getName().equalsIgnoreCase("attribute")) {
      //TODO reseto take care of arguments
      final HashMap<String, Object> metadata = new HashMap<String, Object>();
      if (docElement.getAttrs().containsKey("use") && docElement.getAttrs().get("use").getValue().equalsIgnoreCase("required")) {
        metadata.put("required", Boolean.TRUE);
      }
      contentStack.peek().getAttributes().add(
              new Attribute(getContext(), docElement.attributeNameValue(), metadata, null, new ArrayList<String>(0)));
    }
    
  }

  public List<Element> getRules() {
    return rules;
  }

  // **************************************************** private simple methods

  /**
   * Trims (cuts) namespace prefix from the beginning of element qName
   */
  private String trimNS(final String qName) {
    return qName.substring(qName.lastIndexOf(':') + 1).toLowerCase();
  }

  private boolean isContainer(final Element elem) {
    return (elem.getName().equals(CONTAINER_NAME)
            && elem.getMetadata().containsKey(CONTAINER_NAME)
            && elem.getMetadata().get(CONTAINER_NAME).equals(Boolean.TRUE));
  }

  /**
   * Add parameter as a child (token) to its parent (if it exists)
   * and add it to rules.
   * @param elem
   */
  private void addChildAndRule(final Element elem, boolean resolved) {
    if (elem.getSubnodes().getType() == null) {
      elem.getSubnodes().setType(RegexpType.CONCATENATION);
    }
    if (resolved) {
      elem.setImmutable();
    }
    //add self as token to parent
    if (!contentStack.isEmpty()) {
      contentStack.peek().getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(elem));
    }

    if (currentRuleListName.equals("")) {
      // add element to global rules
      //LOG.debug("ADDING RULE:" + elem.toString());
      rules.add(elem);
    } else {
      // add element to rules for the active CType
      // these are copied to global when the CType is used
      namedTypes.get(currentRuleListName).getSecond().add(elem);
    }
  }

  private List<String> getContext() {
    List<String> ret;
    
    if (contentStack.isEmpty()) {
      ret = new ArrayList<String>(0);
    } else {
      final Element parent = contentStack.peek();
      ret = new ArrayList<String>(parent.getContext());
      
      if (!isContainer(parent)) {
        // parent is not a container, add its name to the context
        ret.add(parent.getName());
      }
    }
    return ret;
  }
  
  private RegexpType determineRegexpType(final XSDDocumentElement docElement) {
    if (docElement.getName().equalsIgnoreCase("sequence")) {
      return RegexpType.CONCATENATION;
    } else if (docElement.getName().equalsIgnoreCase("choice")) {
      //TODO reseto allow alternation
      return RegexpType.CONCATENATION;
    } else if (docElement.getName().equalsIgnoreCase("all")) {
      //TODO reseto allow permutation
      return RegexpType.CONCATENATION;
    } else {
      // else it's a regular element, we don't know the type yet
      // this has to stay null,
      // because the element can have a type defined in some named CType
      return null;
    }
  }

  // *************************************************** private complex methods

  /**
   * Processes an element with declared 'type' attribute.
   * @param old Element from contentStack, expected to be regular element.
   * @param docElement Node from documentElementStack.
   */
  private void processEndOfElement(final XSDDocumentElement docElement) throws IllegalArgumentException {

    final Element old = contentStack.pop();
    final String name = docElement.attributeNameValue();
    boolean resolved = true;

    if (!old.getName().equals(name)) {
      LOG.error("Unexpected element on stack " + name);
      throw new IllegalArgumentException("Unexpected element on stack " + name);
    }

    if (docElement.getAttrs().containsKey("type")) {
      // deal with element that has specified type
      final String TYPE = docElement.getAttrs().get("type").getValue();

      if (!SimpleDataTypes.isSimpleDataType(TYPE)) {
        // try to pair this element with its defined type
        // the problem is, that the complexType it may be associated with
        // can be declared later in the schema than this element
        resolved = tryResolveElementType(TYPE, old);
        if (!resolved) {
          //TODO reseto add this to parent element as UNRESOLVED node
          unresolved.add(new Pair<String, Element>(TYPE, old));
          LOG.debug("Can't resolve the type of element " + old.getName() + " on first try.");
        }
      }
    }


    addChildAndRule(old, resolved);
  }

  private boolean tryResolveElementType(final String TYPE, final Element old) throws IllegalArgumentException {

    if (namedTypes.containsKey(TYPE) && namedTypes.get(TYPE).getFirst() != null) {

      final Pair<Element, List<Element>> pair = namedTypes.get(TYPE);
      if (old.getSubnodes().getType() != null) {
        //check if the type is not the same
        if (!old.getSubnodes().getType().equals(pair.getFirst().getSubnodes().getType())) {
          // serious problem, type conflict
          LOG.error("Element " + old.getName() + " should have type " + TYPE
                  + " but its content already set its type to " + old.getSubnodes().getType());
          throw new IllegalArgumentException("Element " + old.getName() + " should have type " + TYPE
                  + " but its content already set its type to " + old.getSubnodes().getType());
        }
      } else {
        // set correct type
        old.getSubnodes().setType(pair.getFirst().getSubnodes().getType());
      }
      // add all children
      for (Regexp<AbstractStructuralNode> child : pair.getFirst().getSubnodes().getChildren()) {
        old.getSubnodes().addChild(child);
      }
      // copy rules to global
      rules.addAll(pair.getSecond());
      // add all attributes
      for (Attribute at : pair.getFirst().getAttributes()) {
        //arrrrgh stupid context, i kill u 2:30 in the morning
        final List<String> context = old.getContext();
        context.add(old.getName());
        context.addAll(at.getContext());
        old.getAttributes().add(new Attribute(context, at.getName(), at.getMetadata(), at.getContentType(), at.getContent()));
      }
      return true;
    } else {
      return false;
    }
  }

  private void processEndOfOrderIndicator(final XSDDocumentElement docElement) {
    if (!docElement.isAssociated()) {
      // we are dealing with a nested container
      final Element container = contentStack.pop();
      contentStack.peek().getSubnodes().addChild(container.getSubnodes());
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
  private void createAndPushContent(final XSDDocumentElement docElement, final boolean isContainer) {
    
    final Map<String, Object> metadata = new HashMap<String, Object>();

    String name = "";
    RegexpInterval interval = RegexpInterval.getOnce();

    if (isContainer) {
      metadata.put(CONTAINER_NAME, Boolean.TRUE);
      name = CONTAINER_NAME;
    } else {
      // this is a normal element, create its metadata in the designated object
      final XSDMetadata xsdmd = new XSDMetadata();

      determineOccurence(docElement, xsdmd);
      
      if (docElement.getAttrs().containsKey("name")) {
        name = docElement.attributeNameValue();
      } else if (docElement.getAttrs().containsKey("ref")) {
        name = docElement.getAttrs().get("ref").getValue();
        xsdmd.setRef(name);
      }

      metadata.put("from.schema", Boolean.TRUE);
      metadata.put("schema.data", xsdmd);
      interval = xsdmd.getInterval();
    }

    final Element elem = Element.getMutable();
    elem.getContext().addAll(getContext());
    elem.setName(name);
    elem.getMetadata().putAll(metadata);
    elem.getSubnodes().setType(determineRegexpType(docElement));
    elem.getSubnodes().setInterval(interval);

    contentStack.push(elem);
  }

  
  private void determineOccurence(final XSDDocumentElement docElement, final XSDMetadata xsdmd) throws IllegalArgumentException {
    // occurences
    int min = 1, max = 1; //default values from XSD specification

    if (docElement.getAttrs().containsKey("minoccurs")) {
      min = Integer.parseInt(docElement.getAttrs().get("minoccurs").getValue());
    }

    if (docElement.getAttrs().containsKey("maxoccurs")) {
      final String s = docElement.getAttrs().get("maxoccurs").getValue();
      if ("unbounded".equals(s)) {
        xsdmd.setInterval(RegexpInterval.getUnbounded(min));
        return;
      } else {
        max = Integer.parseInt(s);
      }
    }

    if (min > max) {
      throw new IllegalArgumentException("minOccurs > maxOccurs!");
    }

    if (xsdmd.getInterval() == null) {
      xsdmd.setInterval(RegexpInterval.getBounded(min, max));
    }
  }
  
}
