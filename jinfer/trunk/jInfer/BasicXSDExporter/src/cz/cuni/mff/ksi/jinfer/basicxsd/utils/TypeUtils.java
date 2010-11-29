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
 * Utility functions for element type handling.
 *
 * @author rio
 */
public final class TypeUtils {
  private TypeUtils() {
  }

  /**
   * Determinate XSD type category for specified element.
   *
   * @param element Element instance.
   */
  public static TypeCategory getTypeCategory(final Element element) {
    if (!element.getAttributes().isEmpty()) {
      return TypeCategory.COMPLEX;
    }

    if (hasMixedContent(element)) {
      return TypeCategory.COMPLEX;
    }
    
    Regexp<AbstractStructuralNode> subnodes = element.getSubnodes();

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

  /**
   * {@link #getTypeCategory(cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element)} wrapper.
   * Determinate whether specified element is one of the XSD built-in types.
   *
   * @param element Element instance.
   */
  public static boolean isOfBuiltinType(final Element element) {
    TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
    return typeCategory.equals(TypeCategory.BUILTIN) ? true : false;
  }

  /**
   * {@link #getTypeCategory(cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element)} wrapper.
   * Determinate whether specified element is an user defined XSD complex type.
   *
   * @param element Element instance.
   */
  public static boolean isOfComplexType(final Element element) {
    TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
    return typeCategory.equals(TypeCategory.COMPLEX) ? true : false;
  }

  /**
   * Determinate whether specified element has mixed content. Element has mixed
   * content if contains both sub elements and some constant data.
   *
   * @param element Element instance.
   */
  public static boolean hasMixedContent(final Element element) {
    final Regexp<AbstractStructuralNode> regexp = element.getSubnodes();

    boolean hasSimpleData = false;
    boolean hasElements = false;
    boolean hasAttributes = !element.getAttributes().isEmpty();

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
