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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.Indentator;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author reseto
 */
public class Debugs {
    
    public static void dumpQueue(final Indentator indentator, Deque<Element> queue, int k) {
        indentator.indentln("vo fronte mam:");
        for (Element element : queue) {
            indentator.indentln(element.getName() + " contTyp: " + element.getSubnodes().getType());
            indentator.indentln("context >" + Filters.getKAncestorContextFromNode(element, k));
            indentator.indentln("subnodes>" + StringUtils.regexpToString(element.getSubnodes()));
        }
        indentator.indentln("");
    }
    
    
    public static void dumpElements(final Indentator indentator, List<? extends AbstractStructuralNode> list) {
        indentator.append("========= debug dump ===========\n");

        for (AbstractStructuralNode node : list) {
            if (!node.isElement()) {
                indentator.append("(following node is of type " + node.getType().toString() + ")\n");
            }
            final List<String> context = node.getContext();
            StringBuilder path = new StringBuilder();
            path.append('/');
            if (context != null) {
                for (final String ancestor : context) {
                    path.append(ancestor).append('/');
                }
            }


            indentator.append("  NAME:" + node.getName() + "\t\t CONTEXT: " + path.toString() + "\n");
        }
        indentator.append("========= debug dump end =======\n");
    }
}
