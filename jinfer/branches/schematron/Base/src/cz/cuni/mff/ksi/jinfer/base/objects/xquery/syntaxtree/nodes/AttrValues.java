/*
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

/*
 * This code originates from Jiří Schejbal's master thesis. Jiří Schejbal
 * is also the author of the original version of this code.
 * With his approval, we use his code in jInfer and we slightly modify it to
 * suit our cause.
 */
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

/**
 * This class constains the values of attributes.
 *
 * @author Jiri Schejbal
 */
public class AttrValues {

  public static final String ATTR_VALUE_MAIN = "main";
  public static final String ATTR_VALUE_LIBRARY = "library";
  public static final String ATTR_VALUE_SCHEMA = "schema";
  public static final String ATTR_VALUE_MODULE = "module";
  public static final String ATTR_VALUE_PRESERVE = "preserve";
  public static final String ATTR_VALUE_STRIP = "strip";
  public static final String ATTR_VALUE_ELEMENT = "element";
  public static final String ATTR_VALUE_FUNCTION = "function";
  public static final String ATTR_VALUE_ORDERED = "ordered";
  public static final String ATTR_VALUE_UNORDERED = "unordered";
  public static final String ATTR_VALUE_GREATEST = "greatest";
  public static final String ATTR_VALUE_LEAST = "least";
  public static final String ATTR_VALUE_NO_PRESERVE = "no-preserve";
  public static final String ATTR_VALUE_INHERIT = "inherit";
  public static final String ATTR_VALUE_NO_INHERIT = "no-inherit";
  public static final String ATTR_VALUE_ASCENDING = "ascending";
  public static final String ATTR_VALUE_DESCENDING = "descending";
  public static final String ATTR_VALUE_SOME = "some";
  public static final String ATTR_VALUE_EVERY = "every";
  public static final String ATTR_VALUE_DOCUMENT = "document";
  public static final String ATTR_VALUE_ATTRIBUTE = "attribute";
  public static final String ATTR_VALUE_SCHEMA_ELEMENT = "schema-element";
  public static final String ATTR_VALUE_SCHEMA_ATTRIBUTE = "schema-attribute";
  public static final String ATTR_VALUE_PROCESSING_INSTRUCTION =
          "processing-instruction";
  public static final String ATTR_VALUE_COMMENT = "comment";
  public static final String ATTR_VALUE_TEXT = "text";
  public static final String ATTR_VALUE_ANY_KIND = "any-kind";
  public static final String ATTR_VALUE_ZERO = "zero";
  public static final String ATTR_VALUE_ONE = "one";
  public static final String ATTR_VALUE_ZERO_OR_ONE = "zero-or-one";
  public static final String ATTR_VALUE_ZERO_OR_MORE = "zero-or-more";
  public static final String ATTR_VALUE_ONE_OR_MORE = "one-or-more";
  public static final String ATTR_VALUE_DIRECT = "direct";
  public static final String ATTR_VALUE_COMPUTED = "computed";
  public static final String ATTR_VALUE_STRING = "string";
  public static final String ATTR_VALUE_INTEGER = "integer";
  public static final String ATTR_VALUE_DECIMAL = "decimal";
  public static final String ATTR_VALUE_SINGLE = "single";
  public static final String ATTR_VALUE_DOUBLE = "double";
  public static final String ATTR_VALUE_CHILD = "child";
  public static final String ATTR_VALUE_DESCENDANT = "descendant";
  public static final String ATTR_VALUE_SELF = "self";
  public static final String ATTR_VALUE_DESCENDANT_OR_SELF = "descendant-or-self";
  public static final String ATTR_VALUE_FOLLOWING_SIBLING = "following-siblibg";
  public static final String ATTR_VALUE_FOLLOWING = "following";
  public static final String ATTR_VALUE_PARENT = "parent";
  public static final String ATTR_VALUE_ANCESTOR = "ancestor";
  public static final String ATTR_VALUE_PRECEDING_SIBLING = "preceding-sibling";
  public static final String ATTR_VALUE_PRECEDING = "preceding";
  public static final String ATTR_VALUE_ANCESTOR_OR_SELF = "ancestor-or-self";
  public static final String ATTR_VALUE_FORWARD = "forward";
  public static final String ATTR_VALUE_REVERSE = "reverse";
  public static final String ATTR_VALUE_ROOT = "root";
  public static final String ATTR_VALUE_CONTEXT = "context";
  public static final String ATTR_VALUE_LAX = "lax";
  public static final String ATTR_VALUE_STRICT = "strict";
  // -------------------------------------------------------------------------
  // operators
  // -------------------------------------------------------------------------
  //logical
  public static final String ATTR_VALUE_LOGICAL = "logical";
  public static final String ATTR_VALUE_OR = "or";
  public static final String ATTR_VALUE_AND = "and";
  //comparison
  public static final String ATTR_VALUE_COMPARISON = "comparison";
  public static final String ATTR_VALUE_VALUE = "value";
  public static final String ATTR_VALUE_GENERAL = "general";
  public static final String ATTR_VALUE_NODE = "node";
  public static final String ATTR_VALUE_EQUALS = "equals";
  public static final String ATTR_VALUE_NOT_EQUALS = "not-equals";
  public static final String ATTR_VALUE_LESS_THAN = "less-than";
  public static final String ATTR_VALUE_LESS_THAN_EQUALS = "less-than-equals";
  public static final String ATTR_VALUE_GREATER_THAN = "greater-than";
  public static final String ATTR_VALUE_GREATER_THAN_EQUALS = "greater-than-equals";
  public static final String ATTR_VALUE_IS = "is";
  public static final String ATTR_VALUE_PRECEDES = "precedes";
  public static final String ATTR_VALUE_FOLLOWS = "follows";
  //range
  public static final String ATTR_VALUE_RANGE = "range";
  public static final String ATTR_VALUE_TO = "to";
  //additive
  public static final String ATTR_VALUE_ADDITIVE = "additive";
  public static final String ATTR_VALUE_PLUS = "plus";
  public static final String ATTR_VALUE_MINUS = "minus";
  //multiplicative
  public static final String ATTR_VALUE_MULTIPLICATIVE = "multiplicative";
  public static final String ATTR_VALUE_MULTIPLY = "multiply";
  public static final String ATTR_VALUE_DIV = "div";
  public static final String ATTR_VALUE_IDIV = "idiv";
  public static final String ATTR_VALUE_MOD = "mod";
  //set
  public static final String ATTR_VALUE_SET = "set";
  public static final String ATTR_VALUE_UNION = "union";
  public static final String ATTR_VALUE_INTERSECTION = "intersection";
  public static final String ATTR_VALUE_DIFFERENCE = "difference";
  //type test
  public static final String ATTR_VALUE_TYPE_TEST = "type-test";
  public static final String ATTR_VALUE_INSTANCE_OF = "instance-of";
  public static final String ATTR_VALUE_CASTABLE_AS = "castable-as";
  //type conversion
  public static final String ATTR_VALUE_TYPE_CAST = "type-cast";
  public static final String ATTR_VALUE_TREAT_AS = "treat-as";
  public static final String ATTR_VALUE_CAST_AS = "cast-as";
  //unary
  public static final String ATTR_VALUE_UNARY = "unary";
}
