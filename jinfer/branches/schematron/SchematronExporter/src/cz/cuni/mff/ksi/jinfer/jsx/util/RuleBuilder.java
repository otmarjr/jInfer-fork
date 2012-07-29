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
package cz.cuni.mff.ksi.jinfer.jsx.util;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.Indentator;

/**
 *
 * @author reseto
 */
public class RuleBuilder {

    public static final String GENERATED_SUBSTITUTION_STRING = "<!-- %generated% -->\n";
    public static final String ROOT_PATTERN_ID = "root_elements";

    public static void generateSchemaHeader(final Indentator indentator) {
        // Generate head of a new SCH.
        indentator.indent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        indentator.indent(GENERATED_SUBSTITUTION_STRING);
        indentator.indent("<schema xmlns=\"http://purl.oclc.org/dsdl/schematron\" schemaVersion=\"1.0beta\">\n"); //queryBinding=\"xslt2\" 
        indentator.increaseIndentation();
    }

    public static void generateRootPattern(final Indentator indentator, Element root) {
        // create pattern for root element(s), since we always get 1 root element, this rule is made statically
        indentator.indentln("<pattern id=\"" + ROOT_PATTERN_ID + "\">");
        indentator.increaseIndentation();
        // context for root is always "/%name%"
        indentator.indent("<rule context=\"/");
        indentator.append(root.getName());
        indentator.append("\">\n");
        indentator.increaseIndentation();
        indentator.indentln("<assert test=\"true()\"/>"); // not-pair tag, true never fails
        indentator.decreaseIndentation();
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
}
