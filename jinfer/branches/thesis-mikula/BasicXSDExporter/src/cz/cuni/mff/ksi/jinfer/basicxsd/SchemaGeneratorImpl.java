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

import cz.cuni.mff.ksi.jinfer.basicxsd.elementsexporters.RootElementExporter;
import cz.cuni.mff.ksi.jinfer.basicxsd.elementsexporters.GlobalElementsExporter;
import cz.cuni.mff.ksi.jinfer.basicxsd.preprocessing.Preprocessor;
import cz.cuni.mff.ksi.jinfer.basicxsd.preprocessing.PreprocessingResult;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicxsd.properties.XSDExportPropertiesPanel;
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
  public String getDisplayName() {
    return DISPLAY_NAME;
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

    final int spacesPerIndent = Integer.parseInt(properties.getProperty(XSDExportPropertiesPanel.SPACES_PER_INDENT_PROP, String.valueOf(XSDExportPropertiesPanel.SPACES_PER_INDENT_DEFAULT)));
    final Indentator indentator = new Indentator(spacesPerIndent);

    // Generate head of a new XSD.
    indentator.indent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    indentator.indent("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
    indentator.indent(GENERATED_SUBSTITUTION_STRING);

    final boolean generateGlobal = Boolean.parseBoolean(properties.getProperty(XSDExportPropertiesPanel.GENERATE_GLOBAL_PROP, String.valueOf(XSDExportPropertiesPanel.GENERATE_GLOBAL_DEFAULT)));
    final int numberToGlobal = generateGlobal ? Integer.parseInt(properties.getProperty(XSDExportPropertiesPanel.NUMBER_TO_GLOBAL_PROP, String.valueOf(XSDExportPropertiesPanel.NUMBER_TO_GLOBAL_DEFAULT))) : 0;
    final Preprocessor preprocessor = new Preprocessor(grammar, numberToGlobal);
    final PreprocessingResult preprocessingResult = preprocessor.getResult();

    // Handle global elements.
    final List<Element> globalElements = preprocessingResult.getGlobalElements();
    if (!globalElements.isEmpty()) {
      indentator.append("\n");
      indentator.indent("<!-- global types -->\n");

      final GlobalElementsExporter globalElementsExporter = new GlobalElementsExporter(preprocessingResult, indentator);
      globalElementsExporter.run();
    }

    // Run recursion starting at the top element.
    indentator.indent("<!-- top level element -->\n");
    final RootElementExporter rootElementExporter = new RootElementExporter(preprocessingResult, indentator);
    rootElementExporter.run();

    // Close XSD.
    indentator.indent("</xs:schema>");

    LOG.info("XSD Exporter: schema generated at "
            + indentator.toString().length() + " characters.");

    callback.finished(indentator.toString(), "xsd");
  }
}
