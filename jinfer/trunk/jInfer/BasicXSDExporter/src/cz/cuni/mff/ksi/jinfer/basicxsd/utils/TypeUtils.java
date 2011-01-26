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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;

/**
 * Utility functions for element type handling.
 *
 * @author rio
 */
public final class TypeUtils {
  private TypeUtils() {
  }

  /**
   * Determine XSD type category for specified element.
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
    
    final Regexp<AbstractStructuralNode> subnodes = element.getSubnodes();

    if (subnodes.isLambda()) {
      // Type of element without sub nodes is considered a built-in type because
      // we define en empty element type as a complexType without any content.
      return TypeCategory.COMPLEX;
    }

    if (subnodes.isToken()) {
      final AbstractStructuralNode node = subnodes.getContent();
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
   * Determine whether specified element is one of the XSD built-in types.
   *
   * @param element Element instance.
   */
  public static boolean isOfBuiltinType(final Element element) {
    final TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
    return typeCategory.equals(TypeCategory.BUILTIN) ? true : false;
  }

  /**
   * {@link #getTypeCategory(cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element)} wrapper.
   * Determinate whether specified element is an user defined XSD complex type.
   *
   * @param element Element instance.
   */
  public static boolean isOfComplexType(final Element element) {
    final TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
    return typeCategory.equals(TypeCategory.COMPLEX) ? true : false;
  }

  /**
   * Determine whether specified element has mixed content. Element has mixed
   * content if contains both sub elements and some constant data.
   *
   * @param element Element instance.
   */
  public static boolean hasMixedContent(final Element element) {
    final Regexp<AbstractStructuralNode> regexp = element.getSubnodes();

    boolean hasSimpleData = false;
    boolean hasElements = false;
    final boolean hasAttributes = !element.getAttributes().isEmpty();

    for (final AbstractStructuralNode token : regexp.getTokens()) {
      if (token.isSimpleData()) {
        hasSimpleData = true;
      } else if (token.isElement()) {
        hasElements = true;
      }
    }

    return hasSimpleData && (hasElements || hasAttributes);
  }

  /**
   * Gets a type of a built-in element. Element passed to this method must
   * be of a built-in type otherwise an exception is raised.
   *
   * @param element Element of a built-in type.
   * @return Particular type of the element.
   */
  public static String getBuiltinType(final Element element) {
    if (!isOfBuiltinType(element)) {
      throw new IllegalArgumentException("Passed element has to be of a built-in type.");
    }

    // Element may have set its type. If type is not set,
    // element will be defined as "xs:string".
    String type = ((SimpleData)element.getSubnodes().getContent()).getContentType();
    if (!BaseUtils.isEmpty(type)) {
      type = "xs:" + type;
    } else {
      type = "xs:string";
    }

    return type;
  }

  /**
   * Gets a type of an attribute.
   *
   * @param attribute Attribute to get a type.
   * @return Particular type of the attribute.
   */
  public static String getBuiltinAttributeType(final Attribute attribute) {
    // attribute may have set its type. If type is not set,
    // attribute will be defined as "xs:string".
    String type = attribute.getContentType();
    if (!BaseUtils.isEmpty(type)) {
      type = "xs:" + type;
    } else {
      type = "xs:string";
    }

    return type;
  }
}
