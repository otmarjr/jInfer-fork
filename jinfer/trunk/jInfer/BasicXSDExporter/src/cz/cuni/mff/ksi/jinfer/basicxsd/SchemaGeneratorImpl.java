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
package cz.cuni.mff.ksi.jinfer.basicxsd;

import cz.cuni.mff.ksi.jinfer.basicxsd.utils.RegexpTypeUtils;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicxsd.properties.XSDExportPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.basicxsd.utils.TypeCategory;
import cz.cuni.mff.ksi.jinfer.basicxsd.utils.TypeUtils;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * A basic implementation of XSD exporter.
 * 
 * @author riacik
 */
@ServiceProvider(service = SchemaGenerator.class)
public class SchemaGeneratorImpl implements SchemaGenerator {

  private static final Logger LOG = Logger.getLogger(SchemaGenerator.class);
  private Preprocessor preprocessor = null;
  private Indentator indentator = null;
  private String typenamePrefix = null;
  private String typenamePostfix = null;

  private static final int MINOCCURS_DEFAULT = 1;
  private static final int MAXOCCURS_DEFAULT = 1;
  private static final String NAME = "Basic_XSD_exporter";
  private static final String DISPLAY_NAME = "Basic XSD exporter";

  private static final String GENERATED_SUBSTITUTION_STRING = "<!-- %generated% -->\n";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }

  @Override
  public void start(final List<Element> grammar, final SchemaGeneratorCallback callback) throws InterruptedException {
    LOG.info("XSD Exporter: got " + grammar.size()
            + " rules.");

    if (grammar.isEmpty()) {
      LOG.warn("XSD Exporter: nothing to export.");
      callback.finished(GENERATED_SUBSTITUTION_STRING, "xsd");
      return;
    }

    if (!InputGrammarVerifier.verifyUniqueElementNames(grammar)) {
      throw new IllegalStateException("Input grammar contains elements with not unique names.");
    }

    final Properties properties = RunningProject.getActiveProjectProps(XSDExportPropertiesPanel.NAME);

    final int spacesPerIndent = Integer.parseInt(properties.getProperty(XSDExportPropertiesPanel.SPACES_PER_INDENT, String.valueOf(XSDExportPropertiesPanel.SPACES_PER_INDENT_DEFAULT)));
    indentator = new Indentator(spacesPerIndent);

    // Generate head of a new XSD.
    indentator.indent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    indentator.indent("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
    indentator.indent(GENERATED_SUBSTITUTION_STRING);

    final boolean generateGlobal = Boolean.parseBoolean(properties.getProperty(XSDExportPropertiesPanel.GENERATE_GLOBAL, String.valueOf(XSDExportPropertiesPanel.GENERATE_GLOBAL_DEFAULT)));
    final int numberToGlobal = generateGlobal ? Integer.parseInt(properties.getProperty(XSDExportPropertiesPanel.NUMBER_TO_GLOBAL, String.valueOf(XSDExportPropertiesPanel.NUMBER_TO_GLOBAL_DEFAULT))) : 0;
    preprocessor = new Preprocessor(grammar, numberToGlobal);

    typenamePrefix = properties.getProperty(XSDExportPropertiesPanel.TYPENAME_PREFIX, XSDExportPropertiesPanel.TYPENAME_PREFIX_DEFAULT);
    typenamePostfix = properties.getProperty(XSDExportPropertiesPanel.TYPENAME_POSTFIX, XSDExportPropertiesPanel.TYPENAME_POSTFIX_DEFAULT);

    // Handle global elements.
    final List<Element> globalElements = preprocessor.getGlobalElements();
    if (!globalElements.isEmpty()) {
      indentator.append("\n");
      indentator.indent("<!-- global types -->\n");

      for (Element globalElement : globalElements) {
        InterruptChecker.checkInterrupt();
        processGlobalElement(globalElement);
      }
    }

    // Run recursion starting at the top element.
    indentator.indent("<!-- top level element -->\n");
    processElement(preprocessor.getTopElement(), RegexpInterval.getBounded(MINOCCURS_DEFAULT, MAXOCCURS_DEFAULT));

    // Close XSD.
    indentator.indent("</xs:schema>");

    LOG.info("XSD Exporter: schema generated at "
            + indentator.toString().length() + " characters.");

    callback.finished(indentator.toString(), "xsd");
  }

  /**
   * Processes element. If its type is defined globally simply uses it. Otherwise
   * defines it inline.
   *
   * @param element
   * @param interval
   * @throws InterruptedException
   */
  private void processElement(final Element element, final RegexpInterval interval) throws InterruptedException {
    InterruptChecker.checkInterrupt();

    // Begin definition of element and write its name.
    indentator.indent("<xs:element name=\"");
    indentator.append(element.getName());
    indentator.append("\"");

    // If its type is one of built-in types we don't have much work to do
    if (TypeUtils.isOfBuiltinType(element)) {
      final String type = TypeUtils.getBuiltinType(element);

      indentator.append(" type=\"" + type + '"');
      processOccurrences(interval);
      indentator.append("/>\n");
      return;
    }

    // If element's type is global set it and finish.
    if (preprocessor.isElementGlobal(element.getName())) {
      indentator.append(" type=\"");
      indentator.append(typenamePrefix);
      indentator.append(element.getName());
      indentator.append(typenamePostfix);
      indentator.append("\"");
      processOccurrences(interval);
      indentator.append("/>\n");
      return;
    }

    processOccurrences(interval);

    indentator.append(">\n");
    indentator.increaseIndentation();

    final TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("<xs:simpleType>\n");
        break;
      case COMPLEX:
        indentator.indent("<xs:complexType");
        if (TypeUtils.hasMixedContent(element)) {
          indentator.append(" mixed=\"true\"");
        }
        indentator.append(">\n");
        break;
      default:
        throw new IllegalStateException("Unknown or illegal enum member.");
    }

    indentator.increaseIndentation();

    processElementContent(element);

    indentator.decreaseIndentation();

    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("</xs:simpleType>\n");
        break;
      case COMPLEX:
        indentator.indent("</xs:complexType>\n");
        break;
      default:
        throw new IllegalStateException("Unknown or illegal enum member.");
    }

    indentator.decreaseIndentation();

    indentator.indent("</xs:element>\n");
  }

  /**
   * Defines element's type globally.
   *
   * @param element
   * @throws InterruptedException
   */
  private void processGlobalElement(final Element element) throws InterruptedException {
    InterruptChecker.checkInterrupt();

    // If element is of a built-in type don't define it.
    if (TypeUtils.isOfBuiltinType(element)) {
      return;
    }

    final TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("<xs:simpleType name=\"");
        break;
      case COMPLEX:
        indentator.indent("<xs:complexType name=\"");
        break;
      default:
        throw new IllegalStateException("Unknown or illegal enum member.");
    }
    indentator.append(typenamePrefix);
    indentator.append(element.getName());
    indentator.append(typenamePostfix);
    indentator.append("\"");

    if (TypeUtils.hasMixedContent(element)) {
      indentator.append(" mixed=\"true\"");
    }

    indentator.append(">\n");

    indentator.increaseIndentation();

    processElementContent(element);
    
    indentator.decreaseIndentation();

    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("</xs:simpleType>\n");
        break;
      case COMPLEX:
        indentator.indent("</xs:complexType>\n");
        break;
      default:
        throw new IllegalStateException("Unknown of illegal enum member.");
    }

    indentator.append("\n");
  }

  private void processElementContent(final Element element) throws InterruptedException {
    // SPECIAL CASE
    // if element subnodes is token and it is element, wrap it in <xs:sequence></xs:sequence>
    if (element.getSubnodes().isToken() && element.getSubnodes().getContent().isElement()) {
      indentator.indent("<xs:sequence>\n");
      indentator.increaseIndentation();
      processSubElements(element.getSubnodes());
      indentator.decreaseIndentation();
      indentator.indent("</xs:sequence>\n");
    } else {
      processSubElements(element.getSubnodes());
    }

    processElementAttributes(element);
  }

  private void processElementAttributes(final Element element) throws InterruptedException {
    final List<Attribute> attributes = element.getAttributes();

    if (!attributes.isEmpty()) {
      assert TypeUtils.isOfComplexType(element);
      for (Attribute attribute : attributes) {
        InterruptChecker.checkInterrupt();
        indentator.indent("<xs:attribute name=\"");
        indentator.append(attribute.getName());

        final String type = TypeUtils.getBuiltinAttributeType(attribute);
        indentator.append("\" type=\"" + type + '"');

        if (attribute.getMetadata().containsKey(IGGUtils.REQUIRED)) {
          indentator.append(" use=\"required\"");
          // By default attribute is not required.
        }

        indentator.append("/>\n");
      }
    }
  }

  private void processLambdaNodeAlternation(final Regexp<AbstractStructuralNode> simpleAlternation) throws InterruptedException {
    if (simpleAlternation.getChild(1).isToken()) {
      processSubElements(simpleAlternation);
    } else if (simpleAlternation.getChild(1).isConcatenation()) {
      indentator.indent("<xs:sequence minOccurs=\"0\">\n");
      indentator.increaseIndentation();
      for (final Regexp<AbstractStructuralNode> subReg : simpleAlternation.getChild(1).getChildren()) {
        processSubElements(subReg);
      }
      indentator.decreaseIndentation();
      indentator.indent("</xs:sequence>\n");

    } else {
      indentator.indent("<xs:sequence minOccurs=\"0\">\n");
      indentator.increaseIndentation();
      processSubElements(simpleAlternation.getChild(1));
      indentator.decreaseIndentation();
      indentator.indent("</xs:sequence>\n");
    }
  }

  private void processConcatenation(final Regexp<AbstractStructuralNode> concatenation) throws InterruptedException {
    indentator.indent("<xs:sequence");
    processOccurrences(concatenation.getInterval());
    indentator.append(">\n");
    indentator.increaseIndentation();

    for (final Regexp<AbstractStructuralNode> subRegexp : concatenation.getChildren()) {
      if (RegexpTypeUtils.isLambdaNodeAlternation(subRegexp)) {
        processLambdaNodeAlternation(subRegexp);
      } else {
        processSubElements(subRegexp);
      }
    }

    indentator.decreaseIndentation();
    indentator.indent("</xs:sequence>\n");
  }

  private void processAlternation(final Regexp<AbstractStructuralNode> alternation) throws InterruptedException {
    if (RegexpTypeUtils.isLambdaTokenAlternation(alternation)) {
      // SPECIAL CASE
      // Alternation (lambda | Element)
      // The second child has to be an element because of the filtering above.
      processToken(alternation.getChild(1).getContent(), RegexpInterval.getBounded(0, 1));
    } else if (RegexpTypeUtils.isSimpleDataTokenAlternation(alternation)) {
      // SPECIAL CASE
      // Alternation (simpleData | Element)
      // The second child has to br an element because of the filtering above.
      indentator.indent("<xs:sequence>\n");
      indentator.increaseIndentation();
      processToken(alternation.getChild(1).getContent(), RegexpInterval.getBounded(0, 1));
      indentator.decreaseIndentation();
      indentator.indent("</xs:sequence>\n");
    } else {
      // Alternation (lambda | notToken) is handled in CONCATENATION branch.

      // Other alternation (A | B ...)
      indentator.indent("<xs:choice");
      processOccurrences(alternation.getInterval());
      indentator.append(">\n");
      indentator.increaseIndentation();
      for (Regexp<AbstractStructuralNode> subRegexp : alternation.getChildren()) {
        if ((subRegexp != null) && (!subRegexp.isLambda())) {
          processSubElements(subRegexp);
        }
      }
      indentator.decreaseIndentation();
      indentator.indent("</xs:choice>\n");
    }
  }

  /**
   * Given one node from result of getSubnodes method call. We are interested
   * only in elements, so if the node does not contain any elements, function
   * terminates.
   * This filtering also prevents generation of empty xs elements, which may
   * occurs for example if all TOKENs are SIMPLE_DATA.
   *
   * @param regexp Node to process its sub elements.
   * @throws InterruptedException When inference was interrupted.
   */
  private void processSubElements(final Regexp<AbstractStructuralNode> regexp) throws InterruptedException {
    InterruptChecker.checkInterrupt();

    if (regexp.isLambda()) {
      return;
    }

    if (BaseUtils.filter(regexp.getTokens(), new BaseUtils.Predicate<AbstractStructuralNode>() {
      @Override
      public boolean apply(final AbstractStructuralNode argument) {
        return argument.isElement();
      }
    }).isEmpty()) {
      return;
    }

    switch (regexp.getType()) {
      case TOKEN:
        processToken(regexp.getContent(), regexp.getInterval());
        break;
      case CONCATENATION:
        processConcatenation(regexp);
        break;
      case ALTERNATION:
        processAlternation(regexp);
        break;
      default:
        throw new IllegalArgumentException("Unknown enum member.");
    }
  }

  private void processToken(final AbstractStructuralNode node, final RegexpInterval interval) throws InterruptedException {
    assert !node.isSimpleData();
    assert node.isElement();

    final Element element = preprocessor.getElementByName(node.getName());

    if (element == null) {
      LOG.warn("XSD Exporter: Referenced element(" + node.getName() + ") not found in IG element list, probably error in code");
      return;
    }

    processElement(element, interval);
  }

  private void processOccurrences(final RegexpInterval interval) {
    final int minOccurs = interval.getMin();
    if (minOccurs != MINOCCURS_DEFAULT) {
      indentator.append(" minOccurs=\"");
      indentator.append(Integer.toString(minOccurs));
      indentator.append("\"");
    }

    if (interval.isUnbounded()) {
      indentator.append(" maxOccurs=\"unbounded\"");
    } else {
      final int maxOccurs = interval.getMax();
      if (maxOccurs != MAXOCCURS_DEFAULT) {
        indentator.append(" maxOccurs=\"");
        indentator.append(Integer.toString(maxOccurs));
        indentator.append("\"");
      }
    }
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
