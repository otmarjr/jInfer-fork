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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.base.utils.Indentator;
import cz.cuni.mff.ksi.jinfer.jsx.preprocessing.PreprocessingResult;
import cz.cuni.mff.ksi.jinfer.jsx.preprocessing.Preprocessor;
import cz.cuni.mff.ksi.jinfer.jsx.util.RuleBuilder;
import cz.cuni.mff.ksi.jinfer.jsx.util.Debugs;
import cz.cuni.mff.ksi.jinfer.jsx.util.Filters;
import cz.cuni.mff.ksi.jinfer.jsx.util.StringUtils;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
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
    public void start(List<Element> grammar, SchemaGeneratorCallback callback) throws InterruptedException {
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
        indent.indentln("dostavam pravidla:");
        int z = 1;
        for (Element element : grammar) {
            indent.indentln(z + ")" + element.getName() + " contTyp: " + element.getSubnodes().getType());
            indent.indentln("->" + StringUtils.regexpToString(element.getSubnodes()));
            z++;
        }
        indent.indentln("");
        indent.indentln("ROOT string:");
        indent.indentln(StringUtils.regexpToString(root.getSubnodes()));
        indent.indentln("");

        RuleBuilder.generateSchemaHeader(indent);                               // HEADER
        RuleBuilder.generateRootPattern(indent, root);                          // ROOT
        indent.indentln("");


        List<Element> filterDistictContexts = Filters.filterDistictContexts(root.getAllDescendantElements(), K_ANCESTORS);
        Deque<Element> soloQueue = new ArrayDeque<Element>(filterDistictContexts);
        soloQueue.addFirst(root);

        while (!soloQueue.isEmpty()) {
            Element parent = soloQueue.removeFirst();
            String parentName = parent.getName();
            List<Element> childElements = Filters.filterElements(parent.getSubnodes().getTokens());
            List<Element> distinct = Filters.filterDistictNames(childElements);
            //Debugs.dumpQueue(indent, queue, K_ANCESTORS);

            // iterate on elements of queue
            // pattern allowed children of root
            if (BaseUtils.isEmpty(distinct)) {
                indent.indentln("<pattern name=\"empty_element_" + parentName + "\">");
                indent.increaseIndentation();
                indent.indentln("<rule context=\"" + Filters.getKAncestorContextFromNode(parent, K_ANCESTORS) + '/' + parentName + "\">");
                indent.increaseIndentation();
                indent.indentln("<assert test=\"count(*) = 0\">");
                indent.increaseIndentation();
                indent.indentln("Element '" + parentName + "' should be empty.");
                indent.indentln("It has subelements <value-of select=\"string-join(for $c in * return local-name ( $c ), ', ')\"/>.");
                indent.decreaseIndentation();
                indent.indentln("</assert>");
                // TODO assert for no attributes?

                if (BaseUtils.isEmpty(parent.getAttributes())) {
                    indent.indentln("<assert test=\"count(*) = 0\">");
                    indent.increaseIndentation();
                    indent.indentln("Element '" + parentName + "' should not have any attributes.");
                    indent.indentln("It has attributes <value-of select=\"string-join(for $c in @* return local-name ( $c ), ', ')\"/>.");
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
                    indent.indentln("--------------------------DIAG MESSAGE");

                    indent.decreaseIndentation();
                    indent.indentln("</assert>");
                }

                indent.decreaseIndentation();
                indent.indentln("</rule>");
                indent.decreaseIndentation();
                indent.indentln("</pattern>");
                indent.indentln("");
            } else {
                indent.indentln("<pattern name=\"content_of_element_" + parentName + "\">");
                indent.increaseIndentation();
                indent.indentln("<rule context=\"" + Filters.getKAncestorContextFromNode(parent, K_ANCESTORS) + '/' + parentName + "\">");
                indent.increaseIndentation();
                indent.indentln("<let name=\"children_regexp\" value=\"'" + StringUtils.regexpToString(parent.getSubnodes()) + "'\" />");
                indent.indentln("<let name=\"children_order\" value=\"string-join(for $c in * return local-name ( $c ), ' ')\" />");

                indent.indentln("<assert test=\"matches( $children_order, $children_regex )\">");
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
                indent.indent("Element '" + parentName + "' has subelements not specified by the schema: <value-of select=\"string-join(distinct-values(for $c in *[not(");
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
                indent.indentln("<assert test=\"matches( $children_order, $children_regex )\">");
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
        } // end while

//            Debugs.dumpElements(indent, element.getSubnodes().getTokens());
//            indent.indentln("");
//            Debugs.dumpElements(indent, element.getAllDescendants());
//            Debugs.dumpElements(indent, root.getAllDescendants());
//            Debugs.dumpElements(indent, soloQueue.getAllDescendants());

        // Close SCH.
        indent.indent("</schema>");

        LOG.debug("Schematron schema was generated in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");
        callback.finished(indent.toString(), FILE_EXTENSION);
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