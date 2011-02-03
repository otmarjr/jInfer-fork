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
package cz.cuni.mff.ksi.jinfer.xsdimportsax;

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDAttribute;
import cz.cuni.mff.ksi.jinfer.xsdimportsax.utils.XSDNamedType;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDBuiltInDataTypes;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDOccurences;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import cz.cuni.mff.ksi.jinfer.xsdimportsax.utils.SAXAttributeData;
import cz.cuni.mff.ksi.jinfer.xsdimportsax.utils.SAXDocumentElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for importing XSD schemas, requires named ComplexType elements
 * to be declared before their usage in the schema!
 * @author reseto
 */
class SAXHandler extends DefaultHandler {
  //TODO reseto MASSIVE REFACTOR exception throwing by nbBundle!!

  private static final Logger LOG = Logger.getLogger(SAXHandler.class);
  private final boolean verbose = XSDImportSettings.isVerbose();

  /** Stack of XSDDocumentElements to be used only internally for storing data and correct nesting */
  private final Stack<SAXDocumentElement> docElementStack = new Stack<SAXDocumentElement>();
  /** Stack of elements of type Element, to be used for creating rules */
  private final Stack<Element> contentStack = new Stack<Element>();
  /** HashMap of named complexTypes, saved as templates for creating rules
   * The structure is indexed by the name of the type - the String in the Map.
   * The value of an entry is a Pair of Element, and List<Element>
   * - the first is a container with all the children of the complexType 
   *   (this container is added to the pair when the complexType closing tag is parsed)
   * - the second is a list of all the rules within the complexType,
   *   these rules are not added to the global rules immediately.
   *   They are copied to the global rules, every time the complexType was used.
   */
  private final Map<String, XSDNamedType> namedTypes = new HashMap<String, XSDNamedType>();
  /**
   * List of elements with unknown type when they were processed.
   */
  private final List<Pair<String, Element>> unresolved = new ArrayList<Pair<String, Element>>();
  /**
   * Pseudo-unique name for the container elements that are pushed in contentStack
   * the name should be distict from every element name in the actual schema
   */
  private static final String CONTAINER_NAME = "__conTAIner__";
  private String currentRuleListName = "";
  private final List<Element> roots = new ArrayList<Element>();

  public SAXHandler() {
    super();
    LOG.setLevel(XSDImportSettings.logLevel());
  }

  /**
   * Return global rules for the schema.
   * @return List<Element> rules.
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
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException, XSDException {

    // first, create xsd-element that can be pushed into documentElementStack
    // this contains all attributes of the schema element
    final SAXDocumentElement docElement = new SAXDocumentElement(qName);
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

    if (XSDTag.ELEMENT.toString().equals(docElement.getName())) {
      createAndPushContent(docElement, false);

    } else if (docElement.isNamedComplexType()) {
      // named CType has no element to contain it, we must create a container
      createAndPushContent(docElement, true);
      // every element that is a descendant under a namedType 
      // is not added to the global rules, but to the rules of a current CType
      namedTypes.put(docElement.attributeNameValue(), new XSDNamedType());
      currentRuleListName = docElement.attributeNameValue();

    } else if (docElement.isOrderIndicator()) {
      if (XSDTag.ELEMENT.toString().equals(docElementStack.peek().getName())) {
        final String msg = "Schema not valid! " + docElement.getName() + " can't follow an element.";
        LOG.error(msg);
        throw new XSDException(msg);
      }
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
  public void endElement(final String uri, final String localName, final String qName) throws SAXException, XSDException {
    final SAXDocumentElement docElement = docElementStack.pop();

    if (!docElement.getName().equals(XSDUtility.trimNS(qName))) {
      final String msg = "Unpaired element " + docElement.getName() + " and " + qName;
      LOG.error(msg);
      throw new XSDException(msg);
    }
    final XSDTag tag = XSDTag.matchNameTrimNS(qName);

    // BIG IF -> we check what type of element ended
    if (XSDTag.ELEMENT.equals(tag)) {
      processEndOfElement(docElement);

    } else if (docElement.isNamedComplexType()) {
      final Element container = contentStack.pop();
      if (!isContainer(container)) {
        final String msg = "Unexpected element on stack " + container.getName();
        LOG.error(msg);
        throw new XSDException(msg);
      }

      // add the container to the correct named type
      namedTypes.get(docElement.attributeNameValue()).setContainer(container);
      // stop adding rules to that CTypes rules
      currentRuleListName = "";

    } else if (docElement.isOrderIndicator()) {
      // this branch is for choice|sequence|all
      processEndOfOrderIndicator(docElement);

    } else if (XSDTag.ATTRIBUTE.equals(tag)) {
      final HashMap<String, Object> metadata = new HashMap<String, Object>();
      if (docElement.getAttrs().containsKey(XSDAttribute.USE.toString())) {
        final String usage = docElement.getAttrs().get(XSDAttribute.USE.toString()).getValue();
        if (XSDUtility.REQUIRED.equals(usage)) {
          metadata.put(IGGUtils.REQUIRED, Boolean.TRUE);
        } else if (XSDUtility.OPTIONAL.equals(usage)) {
          metadata.put(IGGUtils.REQUIRED, Boolean.FALSE);
        }
      }
      String attrType = null;
      if (docElement.getAttrs().containsKey(XSDAttribute.TYPE.toString())) {
        attrType = XSDUtility.trimNS(docElement.getAttrs().get(XSDAttribute.TYPE.toString()).getValue());
        if (!XSDBuiltInDataTypes.isBuiltInType(attrType)) {
          attrType = "";
        }
      }
      contentStack.peek().getAttributes().add(
              new Attribute(getContext(), docElement.attributeNameValue(), metadata, attrType, new ArrayList<String>(0)));
    }

  }

  private void processEndOfOrderIndicator(final SAXDocumentElement docElement) {
    if (!docElement.isAssociated()) {
      // we are dealing with a nested container
      final Element container = contentStack.pop();
      contentStack.peek().getSubnodes().addChild(container.getSubnodes());
    }
  }

  /**
   * Finds out if Element elem is only a temporary container with the name set by CONTAINER_NAME constant.
   * @param elem Element to be decided.
   * @return True if elem is a container, false if not.
   */
  private boolean isContainer(final Element elem) {
    return (elem.getName().equals(CONTAINER_NAME)
            && elem.getMetadata().containsKey(CONTAINER_NAME)
            && elem.getMetadata().get(CONTAINER_NAME).equals(Boolean.TRUE));
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

  private RegexpType determineRegexpType(final SAXDocumentElement docElement) {
    if (docElement.getName().equalsIgnoreCase("sequence")) {
      return RegexpType.CONCATENATION;
    } else if (docElement.getName().equalsIgnoreCase("choice")) {
      return RegexpType.ALTERNATION;
    } else if (docElement.getName().equalsIgnoreCase("all")) {
      return RegexpType.PERMUTATION;
    } else {
      // else it's a regular element, we don't know the type yet
      // this has to stay null,
      // because the element can have a type defined in some named CType
      return null;
    }
  }

  /**
   * Processes an element with declared 'type' attribute.
   * @param old Element from contentStack, expected to be regular element.
   * @param docElement Node from documentElementStack.
   */
  private void processEndOfElement(final SAXDocumentElement docElement) throws XSDException {

    final Element old = contentStack.pop();
    final String name = docElement.attributeNameValue();
    if (name == null) {
      LOG.error(NbBundle.getMessage(SAXHandler.class, "Error.ElementNameNull"));
      throw new XSDException(NbBundle.getMessage(SAXHandler.class, "Error.ElementNameNull"));
    }

    if (!old.getName().equals(name)) {
      throw new XSDException(NbBundle.getMessage(SAXHandler.class, "Error.UnexpectedElement", name));
    }

    boolean resolved = true;
    if (docElement.getAttrs().containsKey(XSDAttribute.TYPE.toString())) {
      // deal with element that has specified type
      final String TYPE = XSDUtility.trimNS(docElement.getAttrs().get(XSDAttribute.TYPE.toString()).getValue());

      if (XSDBuiltInDataTypes.isBuiltInType(TYPE)) {
        if (old.getSubnodes().getChildren().isEmpty()) {
          // [SIMPLE DATA SECTION]
          // element has empty children, but its specified type is one of the built-in types
          // create SimpleData with the defined type of the element
          old.getSubnodes().setType(RegexpType.TOKEN);
          final List<String> newContext = new ArrayList<String>(old.getContext());
          newContext.add(old.getName());
          old.getSubnodes().setContent(
            new SimpleData(newContext,
                           XSDUtility.SIMPLE_DATA_NAME,
                           Collections.<String,Object>emptyMap(),
                           TYPE,
                           Collections.<String>emptyList()));
        }
      } else {
        // try to pair this element with its defined type
        // the problem is, that the complexType it may be associated with
        // can be declared later in the schema than this element
        resolved = tryResolveElementType(TYPE, old);
        if (!resolved) {
          //TODO reseto add this to parent element as UNRESOLVED node
          unresolved.add(new Pair<String, Element>(TYPE, old));
          LOG.debug("Can't resolve the type of element '" + old.getName() + "' on first try.");
        }
      }
    }

    addAsChild(old, resolved);
  }

  /**
   * Add parameter as a child (token) to its parent (if it exists)
   * and add it to adequate rules list.
   * @param elem
   */
  private void addAsChild(final Element elem, final boolean resolved) {
    if (elem.getSubnodes().getType() == null) {
      elem.getSubnodes().setType(RegexpType.CONCATENATION);
    }
    if (resolved) {
      elem.setImmutable();
    }

    if (!docElementStack.isEmpty() && docElementStack.peek().isSchema()) {
      roots.add(elem);
      if (verbose) {
        LOG.debug("adding to roots: " + elem.getName());
      }
    }
if (false) {
    if ("".equals(currentRuleListName)) {     
      // add element to global rules
      //rules.add(elem);
      LOG.debug("Adding to rules element: " + elem.toString());
    } else {
      // add element to rules for the active CType
      // these are copied to global when the CType is used
      namedTypes.get(currentRuleListName).getRules().add(elem);
    }
}
    // always add self as token to parent
    if (!contentStack.isEmpty()) {
      contentStack.peek().getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(elem));
    }
  }

  private boolean tryResolveElementType(final String TYPE, final Element old) throws XSDException {

    if (namedTypes.containsKey(TYPE) && namedTypes.get(TYPE).getContainer() != null) {

      final XSDNamedType entry = namedTypes.get(TYPE);


      if (old.getSubnodes().getType() != null) {
        // if the type is already set, check if it's the same
        if (!old.getSubnodes().getType().equals(entry.getContainer().getSubnodes().getType())) {
          // it's not the same type, serious problem, conflict
          final String msg = "Element '" + old.getName() + "' should have type '" + TYPE
                  + "' but its content already set its type to: " + old.getSubnodes().getType();
          LOG.error(msg);
          throw new XSDException(msg);
        }
      } else {
        // set correct type
        old.getSubnodes().setType(entry.getContainer().getSubnodes().getType());
      }
      // create correct prefix
      final List<String> context = old.getContext();
      final List<String> prefix = new ArrayList<String>();
      if (!BaseUtils.isEmpty(context)) {
        prefix.addAll(context);
      }
      prefix.add(old.getName());

      CloneHelper cloner = new CloneHelper();

      // add all children
      for (Regexp<AbstractStructuralNode> child : entry.getContainer().getSubnodes().getChildren()) {
        old.getSubnodes().addChild(cloner.cloneRegexp(child, prefix));
      }
      //TODO reseto REFACTOR URGENT!!
if (false) {
      // copy rules
      if (!BaseUtils.isEmpty(entry.getRules())) {
        if ("".equals(currentRuleListName)) {
          for (Element rule : entry.getRules()) {
            //rules.add(cloner.cloneElement(rule, prefix));
          }
          LOG.debug("Copying rules from complexType '" + TYPE + "' to global rules.");
        } else {
          final List<Element> namedRules = namedTypes.get(currentRuleListName).getRules();
          for (Element rule : entry.getRules()) {
            namedRules.add(cloner.cloneElement(rule, prefix));
          }
          LOG.debug("Copying rules from complexType '" + TYPE + "' to rules of complexType '" + currentRuleListName + "'");
        }
      }
}
      // add all attributes
      for (Attribute at : entry.getContainer().getAttributes()) {
        //arrrrgh stupid context, i kill u 2:30 in the morning
        old.getAttributes().add(new Attribute(CloneHelper.getPrefixedContext(at, prefix),
                                              at.getName(), at.getMetadata(), at.getContentType(), at.getContent()));
      }

      // return resolved status
      return true;
    } else {
      return false;
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
  private void createAndPushContent(final SAXDocumentElement docElement, final boolean isContainer) {

    final Map<String, Object> metadata = new HashMap<String, Object>();

    String name = "";

    if (isContainer) {
      metadata.put(CONTAINER_NAME, Boolean.TRUE);
      name = CONTAINER_NAME;
    } else {
      // this is a normal element

      if (docElement.getAttrs().containsKey("name")) {
        name = docElement.attributeNameValue();
      } else if (docElement.getAttrs().containsKey("ref")) {
        name = docElement.getAttrs().get("ref").getValue();
        metadata.putAll(IGGUtils.METADATA_SENTINEL); // ref don't have any contents, so it's a sentinel
      }

      metadata.putAll(IGGUtils.ATTR_FROM_SCHEMA);

      // when element is the first level child inside a complexType
      // it should be set a sentinel
      // when under a complexType, the rule list name is set to the complexType's name
      // and the context is empty, because complexTypes are defined immediately inside the xs:schema element
      if (!"".equals(currentRuleListName) && getContext().isEmpty()) {
        //metadata.put(IGGUtils.IS_SENTINEL, Boolean.TRUE);
        //metadata.put("nejde.to", Boolean.TRUE);
      }
    }

    String minOccurrence = null;
    String maxOccurrence = null;
    if (docElement.getAttrs().containsKey("minoccurs")) {
      minOccurrence = docElement.getAttrs().get("minoccurs").getValue();
    }
    if (docElement.getAttrs().containsKey("maxoccurs")) {
      maxOccurrence = docElement.getAttrs().get("maxoccurs").getValue();
    }

    final Element elem = Element.getMutable();
    elem.getContext().addAll(getContext());
    elem.setName(name);
    elem.getMetadata().putAll(metadata);
    elem.getSubnodes().setType(determineRegexpType(docElement));
    elem.getSubnodes().setInterval(XSDOccurences.createInterval(minOccurrence, maxOccurrence));

    contentStack.push(elem);
  }

  @Override
  public void endDocument() throws SAXException {
    super.endDocument();

    if (verbose) {
      LOG.info("Schema registered following " + namedTypes.size() + " named types");
      for (final Iterator<String> it = namedTypes.keySet().iterator(); it.hasNext();) {
        final String name = it.next();

        if (!BaseUtils.isEmpty(namedTypes.get(name).getContainer().getSubnodes().getChildren())) {
          final int numChildren = namedTypes.get(name).getContainer().getSubnodes().getChildren().size();
          LOG.info("\t'" + name + "': " + namedTypes.get(name).getContainer().getSubnodes().getType().toString().toLowerCase() + " has " + numChildren + " children:");
          for (int i = 0; i < numChildren; i++) {
            final Regexp<AbstractStructuralNode> child = namedTypes.get(name).getContainer().getSubnodes().getChild(i);
            if (child.getContent() == null) {
              LOG.info("\t\t<unnamed> " + child.getType().toString().toLowerCase());
            } else {
              LOG.info("\t\t" + child.getContent().getName());
            }
          } //end for children
        } else {
          LOG.info("\t" + name + ": " + namedTypes.get(name).getContainer().getSubnodes().getType() + " has 0 children.");
        }
      }
    }

    for (int i = unresolved.size() - 1; i >= 0; --i) {
      final Pair<String, Element> pair = unresolved.get(i);
      final boolean resolved = tryResolveElementType(pair.getFirst(), pair.getSecond());
      if (resolved) {
        pair.getSecond().setImmutable();
      } else {
        LOG.warn("Can't resolve the type of element " + pair.getSecond().getName() + "!");
      }
    }
  }
}
