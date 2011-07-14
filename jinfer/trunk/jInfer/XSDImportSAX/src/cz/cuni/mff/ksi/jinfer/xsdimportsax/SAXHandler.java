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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
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
 * SAX handler for importing XSD schemas, overrides methods from DefaultHandler.
 * @author reseto
 */
@SuppressWarnings("PMD.TooManyMethods")
public class SAXHandler extends DefaultHandler {
  private static final Logger LOG = Logger.getLogger(SAXHandler.class);
  private final boolean verbose = XSDImportSettings.isVerbose();

  /** Stack of XSDDocumentElements to be used only internally for storing data and correct nesting */
  private final Stack<SAXDocumentElement> docElementStack = new Stack<SAXDocumentElement>();
  /** Stack of elements of type Element, to be used for creating rules */
  private final Stack<Element> contentStack = new Stack<Element>();
  /**
   * Map of named complexTypes, saved as templates for creating rules
   * The structure is indexed by the name of the type - the String in the Map.
   * The value of an entry is the container Element holding the rule subtree template.
   * Contents of the container are copied to the tag with corresponding <i>type</i> attribute.
   */
  private final Map<String, Element> namedTypes = new HashMap<String, Element>();
  /**
   * List of elements with unknown type when they were processed.
   */
  private final List<Pair<String, Element>> unresolved = new ArrayList<Pair<String, Element>>();
  /**
   * Tags that are direct children of the <i>schema</i> tag. They hold the rule sub-trees of the entire document.
   */
  private final List<Element> roots = new ArrayList<Element>();
  /**
   * Name of the currently parsed complexType tag - to avoid recursion
   */
  private String currentNamedType = "";

  /**
   * Default constructor, sets preferred log level.
   */
  public SAXHandler() {
    super();
    LOG.setLevel(XSDImportSettings.getLogLevel());
  }

  /**
   * Returns rules extracted from the schema.
   * @return Rules extracted from the schema.
   */
  public List<Element> getRules() throws InterruptedException {
    final List<Element> rules = new ArrayList<Element>();
    for (Element root : roots) {
      final List<Element> elementRules = new ArrayList<Element>();
      rules.add(root);
      XSDUtility.getRulesFromElement(root, elementRules);
      rules.addAll(elementRules);
    }
    return rules;
  }

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException, XSDException {

    SAXInterruptChecker.checkInterrupt();

    // first, create xsd-element that can be pushed into documentElementStack
    // this contains all attributes of the schema element
    final SAXDocumentElement docElement = new SAXDocumentElement(qName);
    for (int i = 0; i < attributes.getLength(); i++) {
      final SAXAttributeData data = new SAXAttributeData(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i), attributes.getValue(i));
      docElement.getAttrs().put(XSDUtility.trimNS(attributes.getQName(i)), data);
    }

    if (!docElementStack.isEmpty() && docElementStack.peek().isComplexType()
        && docElement.isOrderIndicator()) {
      // there is a special case when order indicator is an immediate child of any complexType tag
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
      case SCHEMA:
      case ATTRIBUTE:
        // these are supported and processed on element end
        break;
      default:
        LOG.warn("Unsupported tag "+ docElement.getName() +", behavior is undefined.");
    }

    docElementStack.push(docElement);
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) throws SAXException, XSDException {
    final SAXDocumentElement docElement = docElementStack.pop();

    SAXInterruptChecker.checkInterrupt();

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
        if (!contentStack.isEmpty()) {
          contentStack.peek().getAttributes().add(
              new Attribute(getContext(),
                            docElement.getNameOrRefValue(),
                            SAXHelper.prepareAttributeMetadata(docElement),
                            attrType,
                            new ArrayList<String>(0)));
        }
        break;
      default:
    }
  }

  @Override
  public void endDocument() throws SAXException {
    SAXInterruptChecker.checkInterrupt();

    super.endDocument();

    if (verbose) {
      LOG.debug("Schema registered following " + namedTypes.size() + " named types");
      for (final Iterator<String> it = namedTypes.keySet().iterator(); it.hasNext();) {
        final String name = it.next();

        if (!BaseUtils.isEmpty(namedTypes.get(name).getSubnodes().getChildren())) {
          final int numChildren = namedTypes.get(name).getSubnodes().getChildren().size();
          LOG.debug("\t'" + name + "': " + namedTypes.get(name).getSubnodes().getType().toString().toLowerCase() + " has " + numChildren + " children:");
          for (int i = 0; i < numChildren; i++) {
            final Regexp<AbstractStructuralNode> child = namedTypes.get(name).getSubnodes().getChild(i);
            if (child.getContent() == null) {
              LOG.debug("\t\t<unnamed> " + child.getType().toString().toLowerCase());
            } else {
              LOG.debug("\t\t" + child.getContent().getName());
            }
          } //end for children
        } else {
          LOG.debug("\t" + name + ": " + namedTypes.get(name).getSubnodes().getType() + " has 0 children.");
        }
      }
    }

    // RESOLVING
    for (int i = unresolved.size() - 1; i >= 0; --i) {
      final Pair<String, Element> pair = unresolved.get(i);
      final boolean resolved = tryResolveElementType(pair.getFirst(), pair.getSecond());
      if (resolved) {
        SAXHelper.finalizeElement(pair.getSecond());
      } else {
        LOG.warn("Can't resolve the type of element " + pair.getSecond().getName() + "!");
      }
    }
  }

  /**
   * There are 2 kinds of "complexType" tags:
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
   * @param docElement
   */
  private void processStartOfComplexType(final SAXDocumentElement docElement) {
    if (docElement.hasAttribute(XSDAttribute.NAME)) {
      currentNamedType = docElement.getAttributeValue(XSDAttribute.NAME);
      //namedTypes.put(currentNamedType, new XSDNamedType());
      createAndPushContent(docElement, true);
    }
  }

  private void processEndOfComplexType(final SAXDocumentElement docElement) {
    if (docElement.hasAttribute(XSDAttribute.NAME)) {
      final Element container = contentStack.pop();
      if (!SAXHelper.isContainer(container)) {
        throw new XSDException("Unexpected element on stack " + container.getName());
      }
      SAXHelper.finalizeElement(container);
      // add the container to the correct named type
      namedTypes.put(currentNamedType, container);
      currentNamedType = "";
    }
  }

  /**
   * Handles the start of an order indicating tag (<i>all, choice, sequence</i>).
   * The outermost OrderIndicator is said to be associated with a complexType,
   * it determines the type of the associated CType and therefore we don't
   * create a container.
   * If the O.I. is not associated - it is nested, and a container is needed.
   * @param docElement Wrapper for the tag.
   */
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
   * Determines the current context by looking on top of the contentStack.
   * @return Valid context for {@link Element }.
   */
  private List<String> getContext() {
    List<String> ret;

    if (contentStack.isEmpty()) {
      ret = new ArrayList<String>(0);
    } else {
      final Element parent = contentStack.peek();
      ret = new ArrayList<String>(parent.getContext());

      if (!SAXHelper.isContainer(parent)) {
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

    final Element rule = contentStack.pop();
    final String name = docElement.getNameOrRefValue();
    if (name == null) {
      throw new XSDException(NbBundle.getMessage(SAXHandler.class, "Error.ElementNameNull"));
    }

    if (!rule.getName().equals(name)) {
      throw new XSDException(NbBundle.getMessage(SAXHandler.class, "Error.UnexpectedElement", name));
    }

    boolean resolved = true;
    final String TYPE = XSDUtility.trimNS(docElement.getAttributeValue(XSDAttribute.TYPE));

    if (!BaseUtils.isEmpty(TYPE)) {
      // deal with element that has specified type

      if (XSDBuiltInDataTypes.isBuiltInType(TYPE)) {
        if (rule.getSubnodes().getChildren().isEmpty()) {
          // [SIMPLE DATA SECTION]
          // element has empty children, but its specified type is one of the built-in types
          // create SimpleData with the defined type of the element
          rule.getSubnodes().setType(RegexpType.TOKEN);
          final List<String> newContext = new ArrayList<String>(rule.getContext());
          newContext.add(rule.getName());
          rule.getSubnodes().setContent(
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
        resolved = tryResolveElementType(TYPE, rule);
        if (!resolved) {
          unresolved.add(new ImmutablePair<String, Element>(TYPE, rule));
          LOG.debug("Can't resolve the type of element '" + rule.getName() + "' on first try.");
        }
      }
    }

    addAsChild(rule, docElement.determineInterval(), resolved);
  }

  /**
   * Add parameter as a child (token) to its parent (if it exists)
   * and add it to adequate rules list.
   * @param elem
   */
  private void addAsChild(final Element elem, final RegexpInterval interval, final boolean resolved) {
    if (resolved) {
      SAXHelper.finalizeElement(elem);
    }
    if (!docElementStack.isEmpty() && docElementStack.peek().isSchema()) {
      roots.add(elem);
      if (verbose) {
        LOG.debug("adding to roots: " + elem.getName());
      }
    }
    if (!contentStack.isEmpty()) {
      // always add self as token to parent with proper interval
      contentStack.peek().getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(elem, interval));
    }
  }

  private boolean tryResolveElementType(final String TYPE, final Element old) throws XSDException {

    if (namedTypes.containsKey(TYPE) && namedTypes.get(TYPE) != null) {

      final Element entry = namedTypes.get(TYPE);


      if (old.getSubnodes().getType() != null) {
        // if the type is already set, check if it's the same
        if (!old.getSubnodes().getType().equals(entry.getSubnodes().getType())) {
          // it's not the same type, serious problem, conflict
          final String msg = "Element '" + old.getName() + "' should have type '" + TYPE
                  + "' but its content already set its type to: " + old.getSubnodes().getType();
          throw new XSDException(msg);
        }
      } else {
        // set correct type
        old.getSubnodes().setType(entry.getSubnodes().getType());
        if (old.getType() == null && verbose) {
          LOG.warn("element " + old.getName() + " is still null type");
        }
        old.getSubnodes().setInterval(entry.getSubnodes().getInterval());
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
      for (Regexp<AbstractStructuralNode> child : entry.getSubnodes().getChildren()) {
        try {
        old.getSubnodes().addChild(cloner.cloneRegexp(child, prefix));
        } catch (IllegalArgumentException e) {
          if (verbose) {
            LOG.warn("Unresolved subtree of " + entry.getName() + ". Skipping unresolved child. This is an open issue.");
          }
        }
      }

      // add all attributes
      for (Attribute at : entry.getAttributes()) {
        old.getAttributes().add(
          new Attribute(CloneHelper.getPrefixedContext(at, prefix),
                        at.getName(),
                        at.getMetadata(),
                        at.getContentType(),
                        at.getContent()));
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
   * to remember the context, but we don't know the type of it's Regexp (by default it's null),
   * but when the element ends it's changed according to its content.
   * That is why an element is added as a child to its parent only after it ends.
   * @param docElement Currently processed internal element.
   * @param isContainer True if docElement is an order indicator (choice,sequence,all). False if docElement is a proper xs:element.
   */
  private void createAndPushContent(final SAXDocumentElement docElement, final boolean isContainer) {
    final Element elem = Element.getMutable();
    elem.getContext().addAll(getContext());
    elem.setName(SAXHelper.prepareElementName(docElement, isContainer));
    elem.getMetadata().putAll(SAXHelper.prepareElementMetadata(docElement, isContainer));
    if (docElement.isOrderIndicator()) {
      elem.getSubnodes().setType(docElement.determineRegexpType());
      // we have to set the interval even if it is a container,
      // because it may be a nested order indicator
      elem.getSubnodes().setInterval(docElement.determineInterval());
    }
    contentStack.push(elem);
  }
}
