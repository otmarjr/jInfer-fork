/*
 *  Copyright (C) 2010 rio
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

package cz.cuni.mff.ksi.jinfer.trivialxsd.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

/**
 *
 * @author rio
 */
public final class XSDUtils {
  private XSDUtils() {
  }

  public static TypeCategory getTypeCategory(final Element e) {
    if (!e.getElementAttributes().isEmpty()) {
      return TypeCategory.COMPLEX;
    }
    
    Regexp<AbstractNode> subnodes = e.getSubnodes();

    if (subnodes.isEmpty()) {
      // TODO rio skutocne built-in??
      return TypeCategory.BUILTIN;
    }

    if (subnodes.isToken()) {
      AbstractNode node = subnodes.getContent();
      switch (node.getType()) {
        case ELEMENT:
          return TypeCategory.COMPLEX;
        case ATTRIBUTE:
          return TypeCategory.COMPLEX;
        case SIMPLE_DATA:
          return TypeCategory.BUILTIN;
        default:
          throw new IllegalArgumentException("Unknown enum member.");
      }
    }

    return TypeCategory.COMPLEX;
  }
}
