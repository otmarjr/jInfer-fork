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
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import java.util.List;

/**
 *
 * @author reseto
 */
public class StringUtils {
    
    /**
     * Returns string representation of regexp for use by XPath.
     *
     * @param subnodes
     * @param topLevel
     * @return
     */
    public static String regexpToString(final Regexp<AbstractStructuralNode> regexp) {
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

    public static String comboToString(final Regexp<AbstractStructuralNode> regexp,
            final String delimiter) {
        RegexpInterval interval = regexp.getInterval();
        String intervalString = (interval.isKleeneCross() || interval.isKleeneStar() || interval.isOptional()) ? interval.toString() : "";
        return listToString(regexp.getChildren(), delimiter)
                + intervalString;
    }

    public static String listToString(final List<Regexp<AbstractStructuralNode>> list,
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
}
