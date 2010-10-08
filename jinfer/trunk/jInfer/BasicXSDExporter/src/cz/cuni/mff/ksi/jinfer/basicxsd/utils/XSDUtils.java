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

package cz.cuni.mff.ksi.jinfer.basicxsd.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

/**
 *
 * @author rio
 */
public final class XSDUtils {
  private XSDUtils() {
  }

  public static TypeCategory getTypeCategory(final Element e) {
    if (!e.getAttributes().isEmpty()) {
      return TypeCategory.COMPLEX;
    }

    if (hasMixedContent(e)) {
      return TypeCategory.COMPLEX;
    }
    
    Regexp<AbstractStructuralNode> subnodes = e.getSubnodes();

    if (subnodes.isLambda()) {
      // TODO rio skutocne built-in??
      return TypeCategory.BUILTIN;
    }

    if (subnodes.isToken()) {
      AbstractStructuralNode node = subnodes.getContent();
      switch (node.getType()) {
        case ELEMENT:
          return TypeCategory.COMPLEX;
        case SIMPLE_DATA:
          return TypeCategory.BUILTIN;
        default:
          throw new IllegalArgumentException("Unknown enum member.");
      }
    } else {
      /* Case: <A>aa</A><A>bb</A>
       * A is CONCATENATION of 2 TOKENS which are SIMPLE_DATA
       */
      boolean allNodesSimpleData = true;
      for (final Regexp<AbstractStructuralNode> node : subnodes.getChildren()) {
        if (!node.isToken() || !node.getContent().isSimpleData()) {
          allNodesSimpleData = false;
          break;
        }
      }
      if (allNodesSimpleData) {
        return TypeCategory.BUILTIN;
      }
    }

    return TypeCategory.COMPLEX;
  }

  public static boolean hasBuiltinType(final Element e) {
    TypeCategory typeCategory = XSDUtils.getTypeCategory(e);
    return typeCategory.equals(TypeCategory.BUILTIN) ? true : false;
  }

  public static boolean hasComplexType(final Element e) {
    TypeCategory typeCategory = XSDUtils.getTypeCategory(e);
    return typeCategory.equals(TypeCategory.COMPLEX) ? true : false;
  }

  public static boolean hasMixedContent(final Element e) {
    final Regexp<AbstractStructuralNode> regexp = e.getSubnodes();

    boolean hasSimpleData = false;
    boolean hasElements = false;
    boolean hasAttributes = !e.getAttributes().isEmpty();

    for (final AbstractStructuralNode token : regexp.getTokens()) {
      if (token.isSimpleData()) {
        hasSimpleData = true;
      } else if (token.isElement()) {
        hasElements = true;
      }
    }

    if (hasSimpleData && (hasElements || hasAttributes)) {
      return true;
    } else {
      return false;
    }
  }
}
