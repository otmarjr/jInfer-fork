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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.Indentator;
import cz.cuni.mff.ksi.jinfer.jsx.preprocessing.PreprocessingResult;
import cz.cuni.mff.ksi.jinfer.jsx.preprocessing.Preprocessor;
import cz.cuni.mff.ksi.jinfer.jsx.util.RuleBuilder;
import cz.cuni.mff.ksi.jinfer.jsx.util.Debugs;
import cz.cuni.mff.ksi.jinfer.jsx.util.Filters;
import cz.cuni.mff.ksi.jinfer.jsx.util.StringUtils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Main class managing the export process of schemas in Schematron.
 * @author reseto
 */
@ServiceProvider(service = SchemaGenerator.class)
public class JsxSchemaGeneratorImpl implements SchemaGenerator {

    private static final Logger LOG = Logger.getLogger(JsxSchemaGeneratorImpl.class);
    private static final String NAME = "SchematronExporter";
    private static final String DISPLAY_NAME = "jInfer Schematron Exporter";
    private static final String FILE_EXTENSION = "sch";
    private static final int K_ANCESTORS = 1;

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
    public void start(final List<Element> grammar, SchemaGeneratorCallback callback) throws InterruptedException {
        LOG.info("JSX: got " + grammar.size() + " rules.");
        long startTime = System.currentTimeMillis();

        if (grammar.isEmpty()) {
            LOG.warn("JSX: nothing to export.");
            callback.finished(RuleBuilder.GENERATED_SUBSTITUTION_STRING, FILE_EXTENSION);
            return;
        }

        final Preprocessor preprocessor = new Preprocessor(grammar);
        final PreprocessingResult preprocessingResult = preprocessor.getResult(); // this is always non-null
        final Indentator indent = new Indentator(2); // 2 spaces

        //extract all ELEMENTs from under root element, so that they have proper context
        Element root = preprocessingResult.getRootElement();

        if (null == root || null == root.getSubnodes()) {
            LOG.warn("JSX: nothing to export.");
            callback.finished(RuleBuilder.GENERATED_SUBSTITUTION_STRING, FILE_EXTENSION);
            return;
        }

//DEBUG      
        Debugs.dumpGrammarToConsole(LOG, grammar, root);

        RuleBuilder.generateSchemaHeader(indent);                               // HEADER
        RuleBuilder.generateRootPattern(indent, root);                          // ROOT
        indent.indentln("");

        List<String> generatedPatternIds = new ArrayList<String>();
        generatedPatternIds.add(RuleBuilder.ROOT_PATTERN_ID);

        List<Element> filterDistictContexts = Filters.filterDistictContexts(root.getAllDescendantElements(), K_ANCESTORS);
        Deque<Element> soloQueue = new ArrayDeque<Element>(filterDistictContexts);
        soloQueue.addFirst(root);
        //Debugs.dumpQueue(indent, queue, K_ANCESTORS);

        int uniqueCounter = 0;
        // iterate on elements of queue
        while (!soloQueue.isEmpty()) {
            uniqueCounter++;

            Element parent = soloQueue.removeFirst();
            List<Element> childElements = Filters.filterElements(parent.getSubnodes().getTokens());
            dumpElementChildren(indent, childElements, "current");

            // dont take children of the element directly, first match the element to original grammar rule,
            // because the element MAY be empty, but the grammar rule is properly simplified!
            // the only thing we cannot get from the original grammar rule is the context, which is different for each element in structure

            Element testMatch = Filters.matchTargetToGrammarWithContext(parent, grammar, K_ANCESTORS);
            if (null == testMatch) {
                testMatch = parent;
            }

            childElements = Filters.filterElements(testMatch.getSubnodes().getTokens());
            dumpElementChildren(indent, childElements, "grammar");

            List<Element> distinct = Filters.filterDistictNames(childElements);

            String patternId;

            if (BaseUtils.isEmpty(distinct)) {
                patternId = parent.getName() + "_empty_element_" + uniqueCounter;
                indent.indentln("<pattern id=\"" + patternId + "\">");
                indent.increaseIndentation();
                indent.indentln("<rule context=\"" + Filters.getKAncestorContextFromNode(parent, K_ANCESTORS) + '/' + parent.getName() + "\">");
                indent.increaseIndentation();
                indent.indentln("<assert test=\"count(*) = 0\">");
                indent.increaseIndentation();
                indent.indentln("Element '" + parent.getName() + "' should be empty.");
                indent.indentln("It has subelements <value-of select=\"string-join(for $c in * return local-name( $c ), ', ')\"/>.");
                indent.decreaseIndentation();
                indent.indentln("</assert>");
                // TODO assert for no attributes?

                if (BaseUtils.isEmpty(parent.getAttributes())) {
                    indent.indentln("<assert test=\"count(*) = 0\">");
                    indent.increaseIndentation();
                    indent.indentln("Element '" + parent.getName() + "' should not have any attributes.");
                    indent.indentln("It has attributes <value-of select=\"string-join(for $c in @* return local-name( $c ), ', ')\"/>.");
                    indent.decreaseIndentation();
                    indent.indentln("</assert>");
                } else {
                    indent.indent("<assert test=\"count(@*) = count(");
                    boolean first = true;
                    for (Attribute a : parent.getAttributes()) {
                        if (first) {
                            indent.append("@");
                        } else {
                            indent.append("|@");
                        }
                        indent.append(a.getName());
                        first = false;
                    }
                    indent.append(")\">\n");
                    indent.increaseIndentation();
                    indent.indent("Element '" + parent.getName() + "' may have attributes ");
                    first = true;
                    for (Attribute a : parent.getAttributes()) {
                        if (first) {
                            indent.append("@");
                        } else {
                            indent.append(" @");
                        }
                        indent.append(a.getName());
                        first = false;
                    }
                    indent.append(".\n");
                    indent.indentln("It has attributes <value-of select=\"string-join(for $c in @* return local-name( $c ), ', ')\"/>.");
                    indent.decreaseIndentation();
                    indent.indentln("</assert>");
                }

                indent.decreaseIndentation();
                indent.indentln("</rule>");
                indent.decreaseIndentation();
                indent.indentln("</pattern>");
                indent.indentln("");
            } else {
                patternId = parent.getName() + "_element_content_" + uniqueCounter;
                indent.indentln("<pattern id=\"" + patternId + "\">");
                indent.increaseIndentation();
                indent.indentln("<rule context=\"" + Filters.getKAncestorContextFromNode(parent, K_ANCESTORS) + '/' + parent.getName() + "\">");
                indent.increaseIndentation();
                indent.indentln("<let name=\"children_regexp\" value=\"'" + StringUtils.regexpToString(testMatch.getSubnodes()) + "'\" />");
                indent.indentln("<let name=\"children_order\" value=\"string-join(for $c in * return local-name ( $c ), ' ')\" />");

                // ASSERT no unspecified elements are present
                indent.indent("<assert test=\"count(*) = count(");
                boolean first = true;
                for (Element el : distinct) {
                    if (!first) {
                        indent.append("|");
                    }
                    indent.append(el.getName());
                    first = false;
                }
                indent.append(")\">\n");
                indent.increaseIndentation();
                indent.indent("Element '" + parent.getName() + "' has subelements not specified by the schema: <value-of select=\"string-join(distinct-values(for $c in *[not(");
                first = true;
                for (Element el : distinct) {
                    if (!first) {
                        indent.append(" or ");
                    }
                    indent.append("self::" + el.getName());
                    first = false;
                }
                indent.append(")] return local-name ( $c )), ', ')\"/>.\n");
                indent.decreaseIndentation();
                indent.indentln("</assert>");
                // ASSERT for structure
                indent.indentln("<assert test=\"matches( $children_order, $children_regexp )\">");
                indent.increaseIndentation();
                //diagnostic message
                indent.indentln("--------------------------DIAG MESSAGE");

                indent.decreaseIndentation();
                indent.indentln("</assert>");

                // ASSERT for attributes
                // ASSERT for keys
                indent.decreaseIndentation();
                indent.indentln("</rule>");
                indent.decreaseIndentation();
                indent.indentln("</pattern>");
                indent.indentln("");
            }
            generatedPatternIds.add(patternId);
        }

        indent.indentln("<phase id=\"everything\">");
        indent.increaseIndentation();
        for (String id : generatedPatternIds) {
            indent.indentln("<active pattern=\"" + id + "\"/>");
        }
        indent.decreaseIndentation();
        indent.indentln("</phase>");

//            Debugs.dumpElements(indent, element.getSubnodes().getTokens());
//            indent.indentln("");
//            Debugs.dumpElements(indent, element.getAllDescendants());
//            Debugs.dumpElements(indent, root.getAllDescendants());
//            Debugs.dumpElements(indent, soloQueue.getAllDescendants());

        // Close SCH.
        indent.decreaseIndentation();
        indent.indent("</schema>");

        LOG.info("Schematron schema generated in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");
        callback.finished(indent.toString(), FILE_EXTENSION);
    }

    private void dumpElementChildren(final Indentator indent, List<Element> childElements, final String commentName) {
        indent.append("\n<!--\n");
        indent.append(commentName + " element:\n");
        for (Element element : childElements) {
            indent.indentln("FULL NAME: " + element.getName());
            indent.indentln("FULL CONTEXT: " + element.getContext().toString());
        }
        indent.append("\n-->\n");
    }

    private void oldcode(Indentator indent, List<Element> distinct) {
        for (Element element : distinct) {
            // make pattern
            indent.indentln("<pattern name=\"pattern-\">");
            indent.increaseIndentation();

            // now we need to make context based on the number of ancestors selected ...
            indent.indent("<rule context=\"");
            indent.append(element.getName());
            indent.append("\">\n");

            indent.increaseIndentation();
//          <let name="grammar" value=" 'leg(tail|head)*'" />
            //debug
            indent.indent(element.toString() + "\n\n\n");
//          <let name="contents" value="string-join(for $e in * return local-name ( $e ), ' ') " />
            indent.indentln("<let name=\"children_regexp\" value=\" '" + StringUtils.regexpToString(element.getSubnodes()) + "'\" />");
            indent.indentln("<let name=\"children_order\" value=\"string-join(for $c in * return local-name ( $c ), ' ') \" />");
            indent.indentln("<assert test=\"matches( $children_order, $children_regex )\">");
            indent.increaseIndentation();
            //diagnostic message
            // for better diagnostic make lists of following siblings
            indent.indentln("DIAG MESSAGE");
            indent.decreaseIndentation();
            indent.indentln("</assert>");
            indent.decreaseIndentation();
            indent.indentln("</rule>");
            indent.decreaseIndentation();
            indent.indentln("</pattern>");
        }
    }
}