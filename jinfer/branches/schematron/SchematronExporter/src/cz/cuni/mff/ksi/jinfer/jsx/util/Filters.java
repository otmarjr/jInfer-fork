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
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.*;

/**
 * Various filters, to remove clutter from business logic in other classes.
 *
 * @author reseto
 */
public class Filters {

    /**
     *
     * @param <T>
     * @param list
     * @param k k-ancestors to compare in context
     * @return
     */
    public static <T extends AbstractStructuralNode> List<T> filterDistictContexts(List<T> list, int k) {

        if (BaseUtils.isEmpty(list)) {
            return Collections.<T>emptyList();
        }

        ArrayList<T> distinct = new ArrayList<T>(list.size());
        Map<String, List<String>> encountered = new HashMap<String, List<String>>();


        for (T node : list) {
            String name = node.getName();
            String k_context = getKAncestorContextFromNode(node, k);
            if (encountered.containsKey(name)) {
                List<String> known = encountered.get(name);
                boolean found = false;
                for (String c : known) {
                    if (c.equals(k_context)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    known.add(k_context);
                    distinct.add(node);
                }
            } else {
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(k_context);
                encountered.put(name, arrayList);
                distinct.add(node);
            }
        }
        distinct.trimToSize();
        return distinct;
    }

    public static <T extends AbstractStructuralNode> List<T> filterDistictNames(List<T> list) {

        if (BaseUtils.isEmpty(list)) {
            return Collections.<T>emptyList();
        }

        ArrayList<T> distinct = new ArrayList<T>(list.size());
        Set<String> encountered = new HashSet<String>();

        for (T node : list) {
            if (!encountered.contains(node.getName())) {
                encountered.add(node.getName());
                distinct.add(node);
            }
        }
        distinct.trimToSize();
        return distinct;
    }

    /**
     * Filters items that are StructuralNodeType.ELEMENT and returns them in a
     * new list.
     *
     * @return filtered list
     */
    public static List<Element> filterElements(List<? extends AbstractStructuralNode> list) {
        if (BaseUtils.isEmpty(list)) {
            return Collections.<Element>emptyList();
        }
        ArrayList<Element> elements = new ArrayList<Element>(list.size());
        for (AbstractStructuralNode node : list) {
            if (node.isElement()) {
                elements.add((Element) node);
            }
        }
        elements.trimToSize();
        return elements;
    }

    /**
     * Builds context for given node that contains at most k ancestors. If node
     * has no context, empty string is returned. If node has less ancestors then
     * k, the context begins with "/", which means root.
     *
     * @param node
     * @param k
     * @return
     */
    public static String getKAncestorContextFromNode(AbstractStructuralNode node, int k) {
        if (k < -1 || 0 == k || null == node || null == node.getContext() || node.getContext().isEmpty()) {
            return "";
        }

        List<String> context = node.getContext();
        StringBuilder res = new StringBuilder();

        if (k == -1) {
            res.append('/');
            for (final String ancestor : context) {
                res.append(ancestor).append('/');
            }
            return res.toString();
        }

        int remaining = k;
        for (int i = context.size() - 1; i > 0; i--) {
            res.insert(0, context.get(i)); // prepend
            remaining--;
            if (remaining > 0) {
                res.insert(0, "/");
            } else {
                return res.toString();
            }
        }
        return res.toString();
    }

    public static String prependContextWithBackslashes(String contextString) {
        if (BaseUtils.isEmpty(contextString)) {
            return "";
        }
        if (contextString.startsWith("/")) {
            return contextString; // already a root, no need to change
        }
        return "//" + contextString;
    }

    public Element matchTargetToGrammarWithContext(Element target, List<Element> grammar) {
        if (BaseUtils.isEmpty(grammar)) {
            return null;
        }

        String k_context = getKAncestorContextFromNode(target, -1);
        Element match = null;
        Element fallback = null;

        for (Element element : grammar) {
            if (!element.getName().equals(target.getName())) {
                continue;
            }
            String kc2 = getKAncestorContextFromNode(element, -1);

            if (kc2.isEmpty() && null == fallback) {
                fallback = element;
                continue;
            }
            if (kc2.equals(k_context)) {
                match = element;
                break;
            }
        }
        
        if (null == match && null != fallback) {
            match = fallback;
        }
        return match;
    }
}
