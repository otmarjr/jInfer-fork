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
 * This class contains the names of nodes.
 * 
 * @author Jiri Schejbal
 */
public class NodeNames {

  public static final String NODE_MODULE = "Module";
  public static final String NODE_PROLOG = "Prolog";
  public static final String NODE_QUERY_BODY = "QueryBody";
  public static final String NODE_MODULE_DECL = "ModuleDecl";
  public static final String NODE_SCHEMA_IMPORT = "SchemaImport";
  public static final String NODE_MODULE_IMPORT = "ModuleImport";
  public static final String NODE_LOCATION_HINT = "Hint";
  public static final String NODE_LOCATION_HINTS = "LocationHints";
  public static final String NODE_NAMESPACE_DECL = "NamespaceDecl";
  public static final String NODE_BOUNDARY_SPACE_DECL = "BoundarySpaceDecl";
  public static final String NODE_DEFAULT_NAMESPACE_DECL = "DefaultNamespaceDecl";
  public static final String NODE_OPTION_DECL = "OptionDecl";
  public static final String NODE_ORDERING_MODE_DECL = "OrderingModeDecl";
  public static final String NODE_EMPTY_ORDER_DECL = "EmptyOrderDecl";
  public static final String NODE_COPY_NAMESPACE_DECL = "CopyNamespacesDecl";
  public static final String NODE_DEFAULT_COLLATION_DECL = "DefaultCollationDecl";
  public static final String NODE_BASE_URI_DECL = "BaseURIDecl";
  public static final String NODE_VAR_DECL = "VarDecl";
  public static final String NODE_VAR_VALUE = "VarValue";
  public static final String NODE_CONSTRUCTION_DECL = "ConstructionDecl";
  public static final String NODE_FUNCTION_DECL = "FunctionDecl";
  public static final String NODE_RETURN_TYPE = "ReturnType";
  public static final String NODE_FUNCTION_BODY = "FunctionBody";
  public static final String NODE_PARAM_LIST = "ParamList";
  public static final String NODE_PARAM = "Param";
  public static final String NODE_COMMA_OPERATOR = "CommaOperator";
  public static final String NODE_FLWOR_EXPR = "FLWOR";
  public static final String NODE_TUPLE_STREAM = "TupleStream";
  public static final String NODE_BINDING_SEQUENCE = "BindingSequence";
  public static final String NODE_LET_CLAUSE = "LetClause";
  public static final String NODE_FOR_CLAUSE = "ForClause";
  public static final String NODE_WHERE_CLAUSE = "WhereClause";
  public static final String NODE_ORDER_BY_CLAUSE = "OrderByClause";
  public static final String NODE_ORDER_SPEC = "OrderSpec";
  public static final String NODE_QUANTIFIED_EXPR = "QuantifiedExpr";
  public static final String NODE_IN_CLAUSES = "InClauses";
  public static final String NODE_IN_CLAUSE = "InClause";
  public static final String NODE_TEST_EXPRESSION = "TestExpression";
  public static final String NODE_TYPESWITCH_EXPR = "Typeswitch";
  public static final String NODE_OPERAND_EXPRESSION = "OperandExpression";
  public static final String NODE_CASE_CLAUSES = "CaseClauses";
  public static final String NODE_CASE_CLAUSE = "CaseClause";
  public static final String NODE_RETURN_CLAUSE = "ReturnClause";
  public static final String NODE_DEFAULT_CASE = "DefaultCase";
  public static final String NODE_IF_EXPR = "IfExpr";
  public static final String NODE_THEN_EXPRESSION = "ThenExpression";
  public static final String NODE_ELSE_EXPRESSION = "ElseExpression";
  public static final String NODE_OPERATOR = "Operator";
  public static final String NODE_ATOMIC_TYPE = "AtomicType";
  public static final String NODE_EMPTY_SEQUENCE = "EmptySequence";
  public static final String NODE_SEQUENCE_TYPE = "Type";
  public static final String NODE_ANY_ITEM = "AnyItem";
  public static final String NODE_KIND_TEST = "KindTest";
  public static final String NODE_PI_TARGET = "PITarget";
  public static final String NODE_CONSTRUCTOR = "Constructor";
  public static final String NODE_NAME = "Name";
  public static final String NODE_ATTR_LIST = "AttrList";
  public static final String NODE_ATTRIBUTE = "Attribute";
  public static final String NODE_CONTENT = "Content";
  public static final String NODE_CDATA = "CData";
  public static final String NODE_EXPR = "Expr";
  public static final String NODE_CONTEXT_ITEM = "ContextItem";
  public static final String NODE_FUNCTION_CALL = "FunctionCall";
  public static final String NODE_UNORDERED_EXPR = "UnorderedExpr";
  public static final String NODE_ORDERED_EXPR = "OrderedExpr";
  public static final String NODE_VAR_REF = "VarRef";
  public static final String NODE_LITERAL = "Literal";
  public static final String NODE_PREDICATE_LIST = "Predicates";
  public static final String NODE_PARENTHESIZED_EXPR = "ParenthesizedExpr";
  public static final String NODE_NAME_TEST = "NameTest";
  public static final String NODE_ITEM_TYPE = "ItemType";
  public static final String NODE_PATH_EXPR = "Path";
  public static final String NODE_STEP_EXPR = "Step";
  public static final String NODE_PRAGMA_LIST = "PragmaList";
  public static final String NODE_PRAGMA = "Pragma";
  public static final String NODE_EXTENSION_EXPR = "Extension";
  public static final String NODE_DEFAULT_VALUE = "DefaultValue";
  public static final String NODE_VALIDATE_EXPR = "ValidateExpr";
  public static final String NODE_AXIS = "Axis";
  public static final String NODE_CHAR_REF = "CharRef";
  public static final String NODE_ENTITY_REF = "EntityRef";
  public static final String NODE_STRING = "String";
}
