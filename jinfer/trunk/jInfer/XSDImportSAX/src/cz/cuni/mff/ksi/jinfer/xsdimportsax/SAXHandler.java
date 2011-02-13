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
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.*;
import cz.cuni.mff.ksi.jinfer.xsdimportsax.utils.*;
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
public class SAXHandler extends DefaultHandler {
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
  private String currentNamedType = "";
  private final List<Element> roots = new ArrayList<Element>();

  /**
   * Default constructor, sets preferred log level.
   */
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
      docElement.getAttrs().put(XSDUtility.trimNS(attributes.getQName(i)), data);
    }

    if (!docElementStack.isEmpty() && docElementStack.peek().isComplexType()
        && docElement.isOrderIndicator()) {
      // there is a special case when order indicator follows any complex type
      docElement.associate();
    }

    switch (docElement.getTag()) {
      case ELEMENT:
        createAndPushContent(docElement, false);
        break;
      case COMPLEXTYPE:
        processStartOfComplexType(docElement);
        break;
      case ALL:
      case CHOICE:
      case SEQUENCE:
        processStartOfOrderIndicator(docElement);
        break;
      default:
        LOG.warn("Unsupported tag "+ docElement.getName() +", behavior is undefined.");
    }
   
    docElementStack.push(docElement);
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) throws SAXException, XSDException {
    final SAXDocumentElement docElement = docElementStack.pop();

    if (!docElement.getName().equals(XSDUtility.trimNS(qName))) {
      throw new XSDException("Invalid schema. Unpaired tags " + docElement.getName() + " and " + qName);
    }

    switch (docElement.getTag()) {
      case ELEMENT:
        processEndOfElement(docElement);
        break;
      case COMPLEXTYPE:
        processEndOfComplexType(docElement);
        break;
      case ALL:
      case CHOICE:
      case SEQUENCE:
        processEndOfOrderIndicator(docElement);
        break;
      case ATTRIBUTE:
        String attrType = XSDUtility.trimNS(docElement.getAttributeValue(XSDAttribute.TYPE));
        if (!XSDBuiltInDataTypes.isBuiltInType(attrType)) {
          attrType = "";
        }
        contentStack.peek().getAttributes().add(
              new Attribute(getContext(),
                            docElement.getNameOrRefValue(),
                            SAXHelper.getAttributeMetadata(docElement),
                            attrType,
                            new ArrayList<String>(0)));
        break;
      default:
    }
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

    // RESOLVING
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

  private void processStartOfComplexType(final SAXDocumentElement docElement) {
    /* There are 2 kinds of "complexType" tags:
     * 1) When there is NO "name" attribute specified - it is inside an "element" tag,
     *    there is no need to create a container, because all inner tags will be added to the
     *    current Element on top of the contentStack ("attribute" tags too).
     *    Nothing is added to the contentStack.
     * 2) When there is "name" attribute specified (named complexType)
     *    - we must create a special container for the children,
     *    because it has no wrapping "element" tag.
     *    Container is pushed to the contentStack.
     *    Also, to resolve dependencies between several "complexType" tags,
     *    we must maintain an information about current namedType being parsed.
     */
    if (docElement.hasAttribute(XSDAttribute.NAME)) {
      currentNamedType = docElement.getAttributeValue(XSDAttribute.NAME);
      namedTypes.put(currentNamedType, new XSDNamedType());
      createAndPushContent(docElement, true);
    }
  }

  private void processEndOfComplexType(final SAXDocumentElement docElement) {
    if (docElement.hasAttribute(XSDAttribute.NAME)) {
          final Element container = contentStack.pop();
          if (!isContainer(container)) {
            throw new XSDException("Unexpected element on stack " + container.getName());
          }
          currentNamedType = "";
          // add the container to the correct named type
          namedTypes.get(docElement.getAttributeValue(XSDAttribute.NAME)).setContainer(container);
        }
  }

  private void processStartOfOrderIndicator(final SAXDocumentElement docElement) {
    if (XSDTag.ELEMENT.equals(docElementStack.peek().getTag())) {
      throw new XSDException("Invalid schema. Tag " + docElement.getTag().toString() + " cannot occur directly under " + docElementStack.peek().getTag().toString() + ".");
    } else if (docElement.isAssociated()) {
      contentStack.peek().getSubnodes().setType(docElement.determineRegexpType());
      contentStack.peek().getSubnodes().setInterval(docElement.determineInterval());
    } else {
      // order indicator is nested, we create and push a container to contentStack
      createAndPushContent(docElement, true);
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

  /**
   * Processes an element with declared 'type' attribute.
   * @param old Element from contentStack, expected to be regular element.
   * @param docElement Node from documentElementStack.
   */
  private void processEndOfElement(final SAXDocumentElement docElement) throws XSDException {

    final Element old = contentStack.pop();
    final String name = docElement.getAttributeValue(XSDAttribute.NAME);
    if (name == null) {
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

      final CloneHelper cloner = new CloneHelper();

      // add all children
      for (Regexp<AbstractStructuralNode> child : entry.getContainer().getSubnodes().getChildren()) {
        old.getSubnodes().addChild(cloner.cloneRegexp(child, prefix));
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

      if (docElement.hasAttribute(XSDAttribute.NAME)) {
        name = docElement.getAttributeValue(XSDAttribute.NAME);
      } else if (docElement.hasAttribute(XSDAttribute.REF)) {
        name = docElement.getAttributeValue(XSDAttribute.REF);
        metadata.putAll(IGGUtils.METADATA_SENTINEL); // ref don't have any contents, so it's a sentinel
      }

      metadata.putAll(IGGUtils.ATTR_FROM_SCHEMA);
    }

    final Element elem = Element.getMutable();
    elem.getContext().addAll(getContext());
    elem.setName(name);
    elem.getMetadata().putAll(metadata);
    elem.getSubnodes().setType(docElement.determineRegexpType());
    if (docElement.isOrderIndicator()) {
      // we have to set the interval even if it is a container, because it may be a nested order indicator
      elem.getSubnodes().setInterval(docElement.determineInterval());
    }

    contentStack.push(elem);
  }

}
