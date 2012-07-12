/*
 * Copyright (C) 2012 reseto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.jsx;

import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.Indentator;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author reseto
 */
@ServiceProvider(service = SchemaGenerator.class)
public class JsxSchemaGeneratorImpl implements SchemaGenerator {

    private static final Logger LOG = Logger.getLogger(JsxSchemaGeneratorImpl.class);
    private static final String NAME = "SchematronExporter";
    private static final String DISPLAY_NAME = "jInfer Schematron Exporter";
    private static final String GENERATED_SUBSTITUTION_STRING = "<!-- %generated% -->\n";
    private static final String FILE_EXTENSION = "sch";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getModuleDescription() {
        return DISPLAY_NAME;
    }

    @Override
    public List<String> getCapabilities() {
        return Collections.emptyList();
    }

    @Override
    public void start(List<Element> grammar, SchemaGeneratorCallback callback) throws InterruptedException {
        LOG.info("JSX: got " + grammar.size() + " rules.");

        if (grammar.isEmpty()) {
            LOG.warn("JSX: nothing to export.");
            callback.finished(GENERATED_SUBSTITUTION_STRING, FILE_EXTENSION);
            return;
        }

        final Indentator indentator = new Indentator(2);

        // Generate head of a new SCH.
        indentator.indent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        indentator.indent("<schema xmlns=\"http://purl.oclc.org/dsdl/schematron\" schemaVersion=\"1.0beta\">\n");
        indentator.indent(GENERATED_SUBSTITUTION_STRING);


        // dump rules
        for (Element element : grammar) {
            indentator.indent(element.toString() + "\n");
        }
        
        
        // Close SCH.
        indentator.indent("</schema>");
        
        callback.finished(indentator.toString(), FILE_EXTENSION);
    }
}
