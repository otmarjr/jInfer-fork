/*
 *  Copyright (C) 2010 riacik
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
package cz.cuni.mff.ksi.jinfer.trivialxsd;

import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.trivialxsd.utils.TypeCategory;
import cz.cuni.mff.ksi.jinfer.trivialxsd.utils.XSDUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * A simple XSD exporter.
 * 
 * @author riacik
 */
@ServiceProvider(service = SchemaGenerator.class)
public class SchemaGeneratorImpl implements SchemaGenerator {

  private static Logger LOG = Logger.getLogger(SchemaGenerator.class);
  private Preprocessor preprocessor = null;
  private Indentator indentator = null;
  // TODO rio nastavovat uzivatelom?
  private static final String TYPENAME_PREFIX = "T";
  private static final int MINOCCURS_DEFAULT = 1;
  private static final int MAXOCCURS_DEFAULT = 1;

  @Override
  public String getModuleName() {
    return "Trivial XSD exporter";
  }

  @Override
  public void start(final List<AbstractNode> grammar, final SchemaGeneratorCallback callback) throws InterruptedException {
    LOG.info("XSD Exporter: got " + grammar.size()
            + " rules.");
    
    // filter only the elements
    final List<Element> elements = new ArrayList<Element>();
    for (final AbstractNode node : grammar) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        elements.add((Element) node);
      }
    }

    LOG.info("XSD Exporter: that is " + elements.size()
            + " elements.");

    if (elements.isEmpty()) {
      return;
    }

    assert(verifyInput(elements));

    indentator = new Indentator();

    // generate head of a new XSD
    indentator.indent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    indentator.indent("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");

    preprocessor = new Preprocessor(elements);
    preprocessor.run();

    // handle global elements
    final List<Element> globalElements = preprocessor.getGlobalElements();
    if (!globalElements.isEmpty()) {
      indentator.append("\n");
      indentator.indent("<!-- global types -->\n");
    }
    for (Element globalElement : globalElements) {
      checkInterrupt();
      processGlobalElement(globalElement);
    }

    // run recursion starting at the top element
    indentator.indent("<!-- top level element -->\n");
    processElement(preprocessor.getTopElement(), 1, 1);

    // close XSD
    indentator.indent("</xs:schema>");

    LOG.info("XSD Exporter: schema generated at "
            + indentator.toString().length() + " characters.");

    callback.finished(indentator.toString(), "xsd");
  }

  private void processElement(final Element element, final int minOccurs, final int maxOccurs) throws InterruptedException {
    checkInterrupt();

    // begin definition of element and write its name
    indentator.indent("<xs:element name=\"");
    indentator.append(element.getName());
    indentator.append("\"");

    processOccurrences(minOccurs, maxOccurs);

    // if its type is one of built-in types we don't have much work to do
    // TODO rio dalsie built-in typy
    TypeCategory typeCategory = XSDUtils.getTypeCategory(element);
    if (typeCategory.equals(TypeCategory.BUILTIN)) {
      indentator.append(" type=\"xs:string\"/>\n");
      return;
    }

    // if element's type is global set it and finish
    if (preprocessor.isElementGlobal(element.getName())) {
      indentator.append(" type=\"");
      indentator.append(TYPENAME_PREFIX);
      indentator.append(element.getName());
      indentator.append("\"/>\n");
      return;
    }

    indentator.append(">\n");
    indentator.increaseIndentation();

    // TODO rio mixed content
    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("<xs:simpleType>\n");
        break;
      case COMPLEX:
        indentator.indent("<xs:complexType>\n");
        break;
      default:
        throw new IllegalArgumentException("Unknown of illegal enum member.");
    }

    indentator.increaseIndentation();
    processSubElements(element.getSubnodes(), 1, 1);
    processElementAttributes(element);
    indentator.decreaseIndentation();

    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("</xs:simpleType>\n");
        break;
      case COMPLEX:
        indentator.indent("</xs:complexType>\n");
        break;
      default:
        throw new IllegalArgumentException("Unknown of illegal enum member.");
    }

    indentator.decreaseIndentation();

    indentator.indent("</xs:element>\n");
    return;
  }

  private void processGlobalElement(final Element element) throws InterruptedException {
    checkInterrupt();

    // if element is of a built-in type don't define it
    // TODO rio refactor to XSDUtils
    TypeCategory typeCategory = XSDUtils.getTypeCategory(element);
    if (typeCategory.equals(TypeCategory.BUILTIN)) {
      return;
    }

     // TODO rio mixed content
    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("<xs:simpleType name=\"");
        break;
      case COMPLEX:
        indentator.indent("<xs:complexType name=\"");
        break;
      default:
        throw new IllegalArgumentException("Unknown of illegal enum member.");
    }
    indentator.append(TYPENAME_PREFIX);
    indentator.append(element.getName());
    indentator.append("\">\n");

    indentator.increaseIndentation();
    processSubElements(element.getSubnodes(), 1, 1);
    processElementAttributes(element);
    indentator.decreaseIndentation();

    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("</xs:simpleType>\n");
        break;
      case COMPLEX:
        indentator.indent("</xs:complexType>\n");
        break;
      default:
        throw new IllegalArgumentException("Unknown of illegal enum member.");
    }

    indentator.append("\n");

    return;
  }

  private void processElementAttributes(final Element element) throws InterruptedException {
    final List<Attribute> attributes = element.getElementAttributes();

    if (!attributes.isEmpty()) {
      TypeCategory typeCategory = XSDUtils.getTypeCategory(element);
      assert(typeCategory.equals(TypeCategory.COMPLEX));
      for (Attribute attribute : attributes) {
        checkInterrupt();
        indentator.indent("<xs:attribute name=\"");
        indentator.append(attribute.getName());
        // TODO rio types of attributes
        indentator.append("\" type=\"xs:string\"/>\n");
      }
    }
  }

  private void processSubElements(final Regexp<AbstractNode> regexp, final int minOccurs, final int maxOccurs) throws InterruptedException {
    checkInterrupt();

    if (regexp.isEmpty()) {
      return;
    }

    if (BaseUtils.filter(regexp.getTokens(), new BaseUtils.Predicate<AbstractNode>() {
      @Override
      public boolean apply(final AbstractNode argument) {
        return argument.isElement();
      }
    }).isEmpty()) {
      return;
    }

    switch (regexp.getType()) {
      case TOKEN:
        processToken(regexp.getContent(), minOccurs, maxOccurs);
        return;
      case KLEENE:
      {
        indentator.indent("<xs:sequence>\n");
        indentator.increaseIndentation();
        processSubElements(regexp.getChild(0), 0, Integer.MAX_VALUE);
        indentator.decreaseIndentation();
        indentator.indent("</xs:sequence>\n");
        return;
      }
      case CONCATENATION:
      {
        // TODO rio specialny pripad konkatenacie alternacie(A|lambda)
        List<Regexp<AbstractNode>> simpleAlternations = new LinkedList<Regexp<AbstractNode>>();
        for (final Regexp<AbstractNode> child : regexp.getChildren()) {
          if (child.isAlternation()) {
            if (child.getChildren().size() == 2) {
              if (child.getChild(0).isEmpty()) {
                simpleAlternations.add(child);
              }
            }
          }
        }

        indentator.indent("<xs:sequence");
        processOccurrences(minOccurs, maxOccurs);
        indentator.append(">\n");
        indentator.increaseIndentation();

        for (final Regexp<AbstractNode> subRegexp : regexp.getChildren()) {
          if (simpleAlternations.contains(subRegexp)) {
            final Regexp<AbstractNode> alternation = subRegexp;
            if (alternation.getChild(1).isToken()) {
              processSubElements(alternation, 1, 1);
            } else if (alternation.getChild(1).isConcatenation()) {
              indentator.indent("<xs:sequence minOccurs=\"0\">\n");
              indentator.increaseIndentation();
              for (final Regexp<AbstractNode> subReg : alternation.getChild(1).getChildren()) {
                processSubElements(subReg, 1, 1);
              }
              indentator.decreaseIndentation();
              indentator.indent("</xs:sequence>\n");

            } else {
              indentator.indent("<xs:sequence minOccurs=\"0\">\n");
              indentator.increaseIndentation();
              processSubElements(alternation.getChild(1), 1, 1);
              indentator.decreaseIndentation();
              indentator.indent("</xs:sequence>\n");
            }
          } else {
            processSubElements(subRegexp, 1, 1);
          }
        }
        
        indentator.decreaseIndentation();
        indentator.indent("</xs:sequence>\n");
        return;
      }
      case ALTERNATION:
      {
        // simple alternation (Element | lambda)
        if (regexp.getChildren().size() == 2) {
          if (regexp.getChild(0).isEmpty()) {
            if (regexp.getChild(1).isToken()) {
              processToken(regexp.getChild(1).getContent(), 0, 1);
              return;
            }
          }
        }

        // simple alternation (notToken | lambda) is handled in CONCATENATION

        // other alternation (A | B ...)
        indentator.indent("<xs:choice");
        processOccurrences(minOccurs, maxOccurs);
        indentator.append(">\n");
        indentator.increaseIndentation();
        for (Regexp<AbstractNode> subRegexp : regexp.getChildren()) {
          if ((subRegexp != null) && (!subRegexp.isEmpty())) {
            processSubElements(subRegexp, 1, 1);
          }
        }
        indentator.decreaseIndentation();
        indentator.indent("</xs:choice>\n");
        return;
      }
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }

  private void processToken(final AbstractNode node, final int minOccurs, final int maxOccurs) throws InterruptedException {
    assert(node.isSimpleData() == false);
    assert(node.isElement());

    final Element element = preprocessor.getElementByName(node.getName());

    if (element == null) {
      LOG.warn("XSD Exporter: Referenced element(" + node.getName() + ") not found in IG element list, probably error in code");
      return;
    }
      processElement(element, minOccurs, maxOccurs);
  }

  private void processOccurrences(final int minOccurs, final int maxOccurs) {
    if (minOccurs != MINOCCURS_DEFAULT) {
      indentator.append(" minOccurs=\"");
      indentator.append(Integer.toString(minOccurs));
      indentator.append("\"");
    }

    if (maxOccurs != MAXOCCURS_DEFAULT) {
      indentator.append(" maxOccurs=\"");
      if (maxOccurs == Integer.MAX_VALUE) {
        indentator.append("unbounded");
      } else {
        indentator.append(Integer.toString(minOccurs));
      }
      indentator.append("\"");
    }
  }

  private void checkInterrupt() throws InterruptedException {
    if (Thread.interrupted()) {
      throw new InterruptedException();
    }
  }

  private boolean verifyInput(final List<Element> input) {
    final Set<String> set = new HashSet<String>() {};

    for (Element element : input) {
      if (set.contains(element.getName())) {
        return false;
      }
      set.add(element.getName());
    }

    return true;
  }
}