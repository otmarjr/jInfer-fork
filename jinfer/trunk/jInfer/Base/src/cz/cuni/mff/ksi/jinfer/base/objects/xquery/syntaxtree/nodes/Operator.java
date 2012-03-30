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
 * The enumeration of operators.
 *
 * @author Jiri Schejbal
 */
public enum Operator {

  // -------------------------------------------------------------------------
  // logical
  // -------------------------------------------------------------------------
  OR(
  AttrValues.ATTR_VALUE_OR,
  AttrValues.ATTR_VALUE_LOGICAL),
  AND(
  AttrValues.ATTR_VALUE_AND,
  AttrValues.ATTR_VALUE_LOGICAL),
  // -------------------------------------------------------------------------
  // comparison
  // -------------------------------------------------------------------------

  GEN_EQUALS(
  AttrValues.ATTR_VALUE_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_GENERAL),
  GEN_NOT_EQUALS(
  AttrValues.ATTR_VALUE_NOT_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_GENERAL),
  GEN_LESS_THAN(
  AttrValues.ATTR_VALUE_LESS_THAN,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_GENERAL),
  GEN_LESS_THAN_EQUALS(
  AttrValues.ATTR_VALUE_LESS_THAN_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_GENERAL),
  GEN_GREATER_THAN(
  AttrValues.ATTR_VALUE_GREATER_THAN,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_GENERAL),
  GEN_GREATER_THAN_EQUALS(
  AttrValues.ATTR_VALUE_GREATER_THAN_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_GENERAL),
  VAL_EQUALS(
  AttrValues.ATTR_VALUE_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_VALUE),
  VAL_NOT_EQUALS(
  AttrValues.ATTR_VALUE_NOT_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_VALUE),
  VAL_LESS_THAN(
  AttrValues.ATTR_VALUE_LESS_THAN,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_VALUE),
  VAL_LESS_THAN_EQUALS(
  AttrValues.ATTR_VALUE_LESS_THAN_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_VALUE),
  VAL_GREATER_THAN(
  AttrValues.ATTR_VALUE_GREATER_THAN,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_VALUE),
  VAL_GREATER_THAN_EQUALS(
  AttrValues.ATTR_VALUE_GREATER_THAN_EQUALS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_VALUE),
  NOD_IS(
  AttrValues.ATTR_VALUE_IS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_NODE),
  NOD_PRECEDES(
  AttrValues.ATTR_VALUE_PRECEDES,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_NODE),
  NOD_FOLLOWS(
  AttrValues.ATTR_VALUE_FOLLOWS,
  AttrValues.ATTR_VALUE_COMPARISON,
  AttrValues.ATTR_VALUE_NODE),
  // -------------------------------------------------------------------------
  // range
  // -------------------------------------------------------------------------

  TO(
  AttrValues.ATTR_VALUE_TO,
  AttrValues.ATTR_VALUE_RANGE),
  // -------------------------------------------------------------------------
  // additive
  // -------------------------------------------------------------------------

  PLUS(
  AttrValues.ATTR_VALUE_PLUS,
  AttrValues.ATTR_VALUE_ADDITIVE),
  MINUS(
  AttrValues.ATTR_VALUE_MINUS,
  AttrValues.ATTR_VALUE_ADDITIVE),
  // -------------------------------------------------------------------------
  // multiplicative
  // -------------------------------------------------------------------------

  MUL(
  AttrValues.ATTR_VALUE_MULTIPLY,
  AttrValues.ATTR_VALUE_MULTIPLICATIVE),
  DIV(
  AttrValues.ATTR_VALUE_DIV,
  AttrValues.ATTR_VALUE_MULTIPLICATIVE),
  IDIV(
  AttrValues.ATTR_VALUE_IDIV,
  AttrValues.ATTR_VALUE_MULTIPLICATIVE),
  MOD(
  AttrValues.ATTR_VALUE_MOD,
  AttrValues.ATTR_VALUE_MULTIPLICATIVE),
  // -------------------------------------------------------------------------
  // set
  // -------------------------------------------------------------------------

  UNION(
  AttrValues.ATTR_VALUE_UNION,
  AttrValues.ATTR_VALUE_SET),
  INTERSECTION(
  AttrValues.ATTR_VALUE_INTERSECTION,
  AttrValues.ATTR_VALUE_SET),
  DIFFERENCE(
  AttrValues.ATTR_VALUE_DIFFERENCE,
  AttrValues.ATTR_VALUE_SET),
  // -------------------------------------------------------------------------
  // type test
  // -------------------------------------------------------------------------

  INSTANCE_OF(
  AttrValues.ATTR_VALUE_INSTANCE_OF,
  AttrValues.ATTR_VALUE_TYPE_TEST),
  CASTABLE_AS(
  AttrValues.ATTR_VALUE_CASTABLE_AS,
  AttrValues.ATTR_VALUE_TYPE_TEST),
  // -------------------------------------------------------------------------
  // type conversion
  // -------------------------------------------------------------------------

  TREAT_AS(
  AttrValues.ATTR_VALUE_TREAT_AS,
  AttrValues.ATTR_VALUE_TYPE_CAST),
  CAST_AS(
  AttrValues.ATTR_VALUE_CAST_AS,
  AttrValues.ATTR_VALUE_TYPE_CAST),
  // -------------------------------------------------------------------------
  // unary
  // -------------------------------------------------------------------------

  UNARY_PLUS(
  AttrValues.ATTR_VALUE_PLUS,
  AttrValues.ATTR_VALUE_UNARY),
  UNARY_MINUS(
  AttrValues.ATTR_VALUE_MINUS,
  AttrValues.ATTR_VALUE_UNARY);
  private String value;
  private String opClass;
  private String opSubClass;

  private Operator(String value, String opClass, String opSubClass) {
    this.value = value;
    this.opClass = opClass;
    this.opSubClass = opSubClass;
  }

  private Operator(String value, String opClass) {
    this(value, opClass, null);
  }

  @Override
  public String toString() {
    return value;
  }

  public String getOpClass() {
    return opClass;
  }

  public String getOpSubClass() {
    return opSubClass;
  }
}
