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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.base.utils.Indentator;
import cz.cuni.mff.ksi.jinfer.jsx.preprocessing.PreprocessingResult;
import cz.cuni.mff.ksi.jinfer.jsx.preprocessing.Preprocessor;
import cz.cuni.mff.ksi.jinfer.jsx.util.Filters;
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
    private static final int K_ANCESTORS = 2;

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

        final Preprocessor preprocessor = new Preprocessor(grammar);
        final PreprocessingResult preprocessingResult = preprocessor.getResult(); // this is always non-null
        final Indentator indent = new Indentator(2); // 2 spaces

        //extract all ELEMENTs from under root element, so that they have proper context
        Element root = preprocessingResult.getRootElement();

        if (null == root || null == root.getSubnodes()) {
            LOG.warn("JSX: nothing to export.");
            callback.finished(GENERATED_SUBSTITUTION_STRING, FILE_EXTENSION);
            return;
        }

//DEBUG      
        indent.indentln("dostavam pravidla:");
        int z = 1;
        for (Element element : grammar) {
            indent.indentln(z + ")" + element.getName() + " contTyp: " + element.getSubnodes().getType());
            indent.indentln("->" + regexpToString(element.getSubnodes()));
            z++;
        }

//        indentator.indent("=== THE ROOT ELEMENT ===\n");
//        indentator.indent(preprocessingResult.getRootElement().toString() + "\n");
//        indentator.indent("=== ================ ===\n");
//DEBUG

        generateSchemaHeader(indent); // HEADER
        generateRootAbsorbingPattern(indent, root); // ROOT


        Element parent = root;
        String parentName = parent.getName();
        List<Element> childElements = Filters.filterElements(parent.getSubnodes().getTokens());
        List<Element> distinct = Filters.filterDistictNames(childElements);

        indent.indentln("");
        // pattern allowed children of root
        if (BaseUtils.isEmpty(distinct)) {
            indent.indentln("<pattern name=\"empty_element_" + parentName + "\">");
            indent.increaseIndentation();
            indent.indentln("<rule context=\"" + Filters.getKAncestorContextFromNode(root, K_ANCESTORS) + '/' + parentName + "\">");
            indent.increaseIndentation();
            indent.indentln("<assert test=\"count(*) = 0\">");
            indent.increaseIndentation();
            indent.indentln("Element '" + parentName + "' should be empty, but isn't.");
            indent.indentln("It has subelements <value-of select=\"string-join(for $c in * return local-name ( $c ), ' ')\"/>.");
            indent.decreaseIndentation();
            indent.indentln("</assert>");
            indent.decreaseIndentation();
            indent.indentln("</rule>");
            indent.decreaseIndentation();
            indent.indentln("</pattern>");
        } else {
            indent.indentln("<pattern name=\"children_of_" + parentName + "\">");
            indent.increaseIndentation();
            indent.indentln("<rule context=\"" + Filters.getKAncestorContextFromNode(root, K_ANCESTORS) + '/' + parentName + "\">");
            indent.increaseIndentation();
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
            indent.decreaseIndentation();
            indent.indentln("</rule>");
            indent.decreaseIndentation();
            indent.indentln("</pattern>");

        }
        indent.indentln("");

        for (Element element : distinct) {
            // make pattern
            indent.indentln("<pattern name=\"pattern-\">");
            indent.increaseIndentation();

            // now we need to make context based on the number of ancestors selected ...
            indent.indent("<rule context=\"");
            indent.append(element.getName());
            indent.append("\">\n");

            indent.increaseIndentation();
//            <let name="grammar" value=" 'leg(tail|head)*'" />
            //debug
            indent.indent(element.toString() + "\n\n\n");
            indent.indentln("<let name=\"children_regexp\" value=\" '" + regexpToString(element.getSubnodes()) + "'\" />");
//         <let name="contents" value="string-join(for $e in * return local-name ( $e ), ' ') " />
            indent.indentln("<let name=\"children_order\" value=\"string-join(for $c in * return local-name ( $c ), ' ') \" />");
            indent.indentln("<assert test=\"matches( $children_order, $children_regex )\">");
            indent.increaseIndentation();
            //diagnostic message
            // for better diagnostic make lists of following siblings
            indent.indentln("DIAG MESSAGE");

            indent.indent(debugDumpElements(element.getSubnodes().getTokens()).toString() + "\n");
            indent.indent(debugDumpElements(element.getAllDescendants()).toString() + "\n");

            indent.decreaseIndentation();
            indent.indentln("</assert>");
            indent.decreaseIndentation();
            indent.indentln("</rule>");
            indent.decreaseIndentation();
            indent.indentln("</pattern>");

        }


        //List<Element> distinctDescendants = Filters.filterElements(root.getAllDescendants());





//DEBUG
//        StringBuilder debugDumpElements = debugDumpElements(allElements);
//        StringBuilder debugDumpElements2 = debugDumpElements(root.getAllDescendants());
//        indentator.indent(debugDumpElements.toString() + "\n");
//        indentator.indent(debugDumpElements2.toString() + "\n");
//DEBUG
        // Close SCH.
        indent.indent("</schema>");

        callback.finished(indent.toString(), FILE_EXTENSION);
    }

    private void generateSchemaHeader(final Indentator indentator) {
        // Generate head of a new SCH.
        indentator.indent(GENERATED_SUBSTITUTION_STRING);
        indentator.indent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        indentator.indent("<schema xmlns=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\" schemaVersion=\"1.0beta\">\n");
        indentator.increaseIndentation();
    }

    private void generateRootAbsorbingPattern(final Indentator indentator, Element root) {
        // create pattern for root element(s), since we always get 1 root element, this rule is made statically
        indentator.indentln("<pattern id=\"check_root_elements\">");
        indentator.increaseIndentation();
        // context for root is always "/%name%"
        indentator.indent("<rule context=\"/");
        indentator.append(root.getName());
        indentator.append("\">");
        indentator.append("<assert test=\"true()\"/>\n"); // not-pair tag, true never fails
        indentator.indentln("</rule>");
        indentator.indentln("<rule context=\"/*\">");
        indentator.increaseIndentation();
        indentator.indentln("<assert test=\"false()\">"); // pair tag
        indentator.increaseIndentation();
        indentator.indentln("Element '<name/>' is not allowed as root element. Only element '" + root.getName() + "' may be declared as root.");
        indentator.decreaseIndentation();
        indentator.indentln("</assert>");
        indentator.decreaseIndentation();
        indentator.indentln("</rule>");
        indentator.decreaseIndentation();
        indentator.indentln("</pattern>");
    }

    private StringBuilder debugDumpElements(List<? extends AbstractStructuralNode> list) {
        StringBuilder sb = new StringBuilder();

        sb.append("========= debug dump ===========\n");

        for (AbstractStructuralNode node : list) {
            if (!node.isElement()) {
                sb.append("(following node is of type ").append(node.getType().toString()).append(")\n");
            }
            final List<String> context = node.getContext();
            StringBuilder path = new StringBuilder();
            path.append('/');
            if (context != null) {
                for (final String ancestor : context) {
                    path.append(ancestor).append('/');
                }
            }


            sb.append("  NAME:").append(node.getName()).append("\t\t CONTEXT: ").append(path.toString()).append("\n");
        }
        sb.append("========= debug dump end =======\n");
        return sb;
    }

    /**
     * Returns string representation of regexp for use by XPath.
     *
     * @param subnodes
     * @param topLevel
     * @return
     */
    private String regexpToString(final Regexp<AbstractStructuralNode> regexp) {
        switch (regexp.getType()) {
            case LAMBDA:
                return "";
            case TOKEN:
                if (regexp.getContent().isElement()) {
                    RegexpInterval interval = regexp.getInterval();
                    String intervalString = (interval.isKleeneCross() || interval.isKleeneStar() || interval.isOptional()) ? interval.toString() : "";
                    return regexp.getContent().getName() + intervalString;
                } else {
                    return "";
                }
            case CONCATENATION:
                return comboToString(regexp, ",");
            case ALTERNATION:
                return comboToString(regexp, "|");
            case PERMUTATION:
                return comboToString(regexp, "|");
            default:
                throw new IllegalArgumentException("Unknown enum member: " + regexp.getType());
        }
    }

    private String comboToString(final Regexp<AbstractStructuralNode> regexp,
            final String delimiter) {
        RegexpInterval interval = regexp.getInterval();
        String intervalString = (interval.isKleeneCross() || interval.isKleeneStar() || interval.isOptional()) ? interval.toString() : "";
        return listToString(regexp.getChildren(), delimiter)
                + intervalString;
    }

    private String listToString(final List<Regexp<AbstractStructuralNode>> list,
            final String separator) {
        return CollectionToString.colToString(
                list,
                separator,
                new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {

                    @Override
                    public String toString(final Regexp<AbstractStructuralNode> t) {
                        return regexpToString(t);
                    }
                });
    }
//
//for (Element element : Collections.<Element>emptyList()) { //grammar -> commented out
//            final StringBuilder path = new StringBuilder();
//            Regexp<AbstractStructuralNode> subnodes = element.getSubnodes();
//            if (RegexpType.TOKEN.equals(subnodes.getType()) && subnodes.getContent() != null) {
//                final List<String> context = subnodes.getContent().getContext();
//                if (context != null) {
//                    for (final String ancestor : context) {
//                        path.append(ancestor).append('/');
//                    }
//                }
//            }
//
//            indentator.indent("\n");
//            indentator.indent("\t" + element.getName() + " ConText: " + path.toString() + "\n");
//            indentator.indent("\n");
//            indentator.indent(element.toString());
//        }
}
