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
import java.util.List;
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
  private Preprocessor preprocessing = null;
  private Indentator indentator = null;

  @Override
  public String getModuleName() {
    return "Trivial XSD exporter";
  }

  @Override
  public void start(final List<AbstractNode> grammar, final SchemaGeneratorCallback callback) {
    LOG.info("XSD Exporter: got " + grammar.size()
            + " rules.");

    // load settings
    //maxEnumSize = Preferences.userNodeForPackage(ConfigPanel.class).getInt("max.enum.size", 3);
    //minDefaultRatio = Preferences.userNodeForPackage(ConfigPanel.class).getFloat("min.default.ratio", 0.67f);
    
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

    /* TODO rio overit ci plati poziadavka:
     * - nazov elementu je jeho unikatny identifikator vo vstupnom liste
     */

    // generate XSD
    indentator = new Indentator();
    indentator.indent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    indentator.indent("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");

    preprocessing = new Preprocessor(elements);
    preprocessing.run();

    // handle global elements
    final List<Element> globalElements = preprocessing.getGlobalElements();
    for (Element globalElement : globalElements) {
      globalElementToString(globalElement);
    }
    
    // TODO rio interrupt
    elementToString(preprocessing.getTopElement());

    indentator.indent("</xs:schema>");

    LOG.info("XSD Exporter: schema generated at "
            + indentator.toString().length() + " characters.");

    callback.finished(indentator.toString(), "xsd");
  }

  private void elementToString(final Element element) {
    indentator.indent("<xs:element name=\"" + element.getName() + "\"");

    TypeCategory typeCategory = XSDUtils.getTypeCategory(element);
    if (typeCategory.equals(TypeCategory.BUILTIN)) {
      indentator.append(" type=\"xs:string\">\n");
      return;
    }

    if (preprocessing.isElementGlobal(element.getName())) {
      indentator.append(" type=\"T" + element.getName() + "\"/>\n");
      return;
    }

    indentator.append(">\n");
     // TODO rio mixed content
    indentator.increaseIndentation();

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

    final List<Attribute> attributes = element.getElementAttributes();
    if (!attributes.isEmpty()) {
      assert(typeCategory.equals(TypeCategory.COMPLEX));
      for (Attribute attribute : attributes) {
        indentator.indent("<xs:attribute name=\"");
        indentator.append(attribute.getName());
        indentator.append("\" type=\"xs:string\">\n");
      }
    }

    subElementsToString(element.getSubnodes());

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

  private void globalElementToString(final Element element) {
    TypeCategory typeCategory = XSDUtils.getTypeCategory(element);
    if (typeCategory.equals(TypeCategory.BUILTIN)) {
      return;
    }

     // TODO rio mixed content
    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("<xs:simpleType name=");
        break;
      case COMPLEX:
        indentator.indent("<xs:complexType name=");
        break;
      default:
        throw new IllegalArgumentException("Unknown of illegal enum member.");
    }
    indentator.append("\"T" + element.getName() + "\">\n");
    indentator.increaseIndentation();

    final List<Attribute> attributes = element.getElementAttributes();
    if (!attributes.isEmpty()) {
      assert(typeCategory.equals(TypeCategory.COMPLEX));
      for (Attribute attribute : attributes) {
        indentator.indent("<xs:attribute name=\"");
        indentator.append(attribute.getName());
        indentator.append("\" type=\"xs:string\">\n");
      }
    }

    subElementsToString(element.getSubnodes());
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

  private void subElementsToString(final Regexp<AbstractNode> regexp) {
    assert(regexp.isEmpty() == false);

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
        tokenToString(regexp.getContent());
        return;
      case KLEENE:
      {
        final StringBuilder ret = new StringBuilder();
        indentator.indent("<xs:sequence>\n");
        indentator.increaseIndentation();
        subElementsToString(regexp.getChild(0));
        indentator.decreaseIndentation();
        indentator.indent("</xs:sequence>\n");
        return;
      }
      case CONCATENATION:
      {
        final StringBuilder ret = new StringBuilder();
        indentator.indent("<xs:sequence>\n");
        indentator.increaseIndentation();
        for (Regexp<AbstractNode> subRegexp : regexp.getChildren()) {
          subElementsToString(subRegexp);
        }
        indentator.decreaseIndentation();
        indentator.indent("</xs:sequence>\n");
        return;
      }
      case ALTERNATION:
        {
        final StringBuilder ret = new StringBuilder();
        indentator.indent("<xs:choice>\n");
        indentator.increaseIndentation();
        for (Regexp<AbstractNode> subRegexp : regexp.getChildren()) {
          if ((subRegexp != null) && (!subRegexp.isEmpty())) {
            subElementsToString(subRegexp);
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

  private void tokenToString(final AbstractNode node) {
    assert(node.isSimpleData() == false);
    assert(node.isElement());

    final Element element = preprocessing.getElementByName(node.getName());

    if (element == null) {
      LOG.warn("XSD Exporter: Referenced element(" + node.getName() + ") not found in IG element list, probably error in code");
      return;
    }

    elementToString(element);
  }
/*
  private void indent(final StringBuilder sb, final String txt) {
    char[] spaces = new char[indentationLevel];
    for (int i = 0; i < indentationLevel; ++i) {
      spaces[i] = ' ';
    }
    final String indentation = new String(spaces);
    sb.append(indentation)
            .append(txt);
  }

  private void indentationIncrease() {
    indentationLevel += indentationStep;
  }

  private void indentationDecrease() {
    assert(indentationLevel >= indentationStep);
    indentationLevel -= indentationStep;
  } */
}