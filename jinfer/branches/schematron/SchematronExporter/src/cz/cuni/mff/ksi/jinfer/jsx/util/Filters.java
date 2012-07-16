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
     * Filters by predicate, in this case, selects entries in list with distinct
     * names. If predicate applies, add node to result list. Result contains
     * nodes with unique names.
     *
     * @return filtered list
     */
    public static <T extends AbstractStructuralNode> List<T> filterDistictNames(List<T> list) {

        if (BaseUtils.isEmpty(list)) {
            return Collections.<T>emptyList();
        }

        ArrayList<T> unique = new ArrayList<T>(list.size());
        Set<String> encountered = new HashSet<String>();

        for (T node : list) {
            if (!encountered.contains(node.getName())) {
                encountered.add(node.getName());
                unique.add(node);
            }
        }
        return unique;

//        // filter by predicate, in this case the node name
//        // if predicate applies, add node to result list 
//        return BaseUtils.filter(list, new BaseUtils.Predicate<T>() {
//
//            private Set<String> encountered = new HashSet<String>();
//
//            @Override
//            public boolean apply(final T node) {
//                if (encountered.contains(node.getName())) {
//                    return false;
//                }
//                encountered.add(node.getName());
//                return true;
//            }
//        });
    }

    /**
     * Filters items that are StructuralNodeType.ELEMENT and returns them in a new list.
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
}
