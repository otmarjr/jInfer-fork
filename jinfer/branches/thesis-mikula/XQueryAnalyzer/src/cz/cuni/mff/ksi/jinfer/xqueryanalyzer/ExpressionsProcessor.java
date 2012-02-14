/*
 * Copyright (C) 2012 rio
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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.*;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.types.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO rio
 *
 * @author rio
 */
public class ExpressionsProcessor {
  
  private final ModuleNode root;
  private final FunctionsProcessor fp;

  public ExpressionsProcessor(final ModuleNode root, final FunctionsProcessor fp) {
    this.root = root;
    this.fp = fp;
  }
  
  public Map<ExprNode, Type> process() {
    final Map<ExprNode, Type> expressionTypes = new HashMap<ExprNode, Type>();
    
    final PrologNode prologNode = root.getPrologNode();
    final Map<String, Type> globalVarTypes = new HashMap<String, Type>();
    if (prologNode != null) {
      globalVarTypes.putAll(analysisOfGlobalVarTypes(prologNode, fp, expressionTypes));
    }
    
    analysisOfExpressionTypes(expressionTypes, root, globalVarTypes, fp);
    return expressionTypes;
  }
  
  private static Map<String, Type> analysisOfGlobalVarTypes(final PrologNode prologNode, final FunctionsProcessor fp, final Map<ExprNode, Type> globalVarsExpressionTypes) {
    final Map<String, Type> globalVarTypes = new HashMap<String, Type>();
    
    for (final XQNode node : prologNode.getSubnodes()) {
      if (VarDeclNode.class.isInstance(node)) {
        final VarDeclNode varDeclNode = (VarDeclNode)node;
        final String varName = varDeclNode.getVarName();
        final TypeNode typeNode = varDeclNode.getTypeDeclarationNode();
        
        // If the variable definition declares its type, use it.
        // Otherwise, determine its type from the expression.
        if (typeNode != null) {
          globalVarTypes.put(varName, TypeFactory.createType(typeNode));
        } else {
          // External variables are not processed.
          if (varDeclNode.getVarValueNode().isExternal()) {
            globalVarTypes.put(varName, new UnknownType());
          } else {
            final ExprNode exprNode = varDeclNode.getVarValueNode().getExprNode();
            analysisOfExpressionTypes(globalVarsExpressionTypes, exprNode, globalVarTypes, fp);
            globalVarTypes.put(varName, globalVarsExpressionTypes.get(exprNode));
          }
        }
      }
    }
    
    return globalVarTypes;
  }
  
  private static Map<String, Type> analysisOfExpressionTypes(final Map<ExprNode, Type> expressionTypes, final XQNode root, final Map<String, Type> contextVarTypes, final FunctionsProcessor fp) {
    // Copy context variable types as we do not want to modify it for the previous recursive calls.
    final Map<String, Type> localContextVarTypes = new HashMap<String, Type>(contextVarTypes);
    final Map<String, Type> newContextVarTypes = new HashMap<String, Type>();

    // Analyze types of the subnodes.
    final List<XQNode> subnodes = root.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : root.getSubnodes()) {
        final Map<String, Type> newVars = analysisOfExpressionTypes(expressionTypes, subnode, localContextVarTypes, fp);
        newContextVarTypes.putAll(newVars);
        // Extend context variable types for the next subnodes.
        localContextVarTypes.putAll(newVars);
      }
    }

    // If root is an expression, analyze it and save its type.
    if (ExprNode.class.isInstance(root)) {
      expressionTypes.put((ExprNode) root, determineExpressionType(fp, (ExprNode) root, expressionTypes, localContextVarTypes));
    }

    // If root extends context variables for its siblings in recursion, return those variables.
    Map<String, Type> newVars = new HashMap<String, Type>();
    if (VariableBindingNode.class.isInstance(root)) {
      final VariableBindingNode variableBindingNode = (VariableBindingNode) root;
      if (variableBindingNode.getTypeNode() != null) {
        newVars.put(variableBindingNode.getVarName(), TypeFactory.createType(variableBindingNode.getTypeNode()));
      } else {
        final Type type = determineExpressionType(fp, variableBindingNode.getBindingSequenceNode().getExprNode(), expressionTypes, localContextVarTypes);
        if (ForClauseNode.class.isInstance(variableBindingNode)) {
          // If the binding is for binding, we make a for bound type.
          newVars.put(variableBindingNode.getVarName(), TypeFactory.createForBoundType(type));
        } else {
          newVars.put(variableBindingNode.getVarName(), type);
        }
      }
    } else if (TupleStreamNode.class.isInstance(root)) {
      newVars = newContextVarTypes;
    }

    return newVars;
  }

  private static Type determineExpressionType(final FunctionsProcessor fp, final ExprNode expressionNode, final Map<ExprNode, Type> expressionTypes, final Map<String, Type> contextVarTypes) {
    if (LiteralNode.class.isInstance(expressionNode)) {
      final LiteralNode literalNode = (LiteralNode) expressionNode;
      return new XSDType(literalNode.getType());
    } else if (FunctionCallNode.class.isInstance(expressionNode)) {
      final FunctionCallNode functionCallNode = (FunctionCallNode) expressionNode;
      return fp.getFunctionType(functionCallNode.getFuncName());
    } else if (OperatorNode.class.isInstance(expressionNode)) {
      return determineOperatorType((OperatorNode) expressionNode, expressionTypes);
    } else if (VarRefNode.class.isInstance(expressionNode)) {
      return contextVarTypes.get(((VarRefNode) expressionNode).getVarName());
    } else if (FLWORExprNode.class.isInstance(expressionNode)) {
      return expressionTypes.get(((FLWORExprNode) expressionNode).getReturnClauseNode().getExprNode());
    } else if (PathExprNode.class.isInstance(expressionNode)) {
      return new PathType((PathExprNode) expressionNode);
    }
    assert (false); // TODO rio
    return null;
  }

  private static Type determineOperatorType(final OperatorNode operatorNode, final Map<ExprNode, Type> expressionTypes) {
    if (operatorNode.getTypeNode() != null) {
      return TypeFactory.createType(operatorNode.getTypeNode());
    }

    final Operator operator = operatorNode.getOperator();

    if (isOperatorClassComparison(operator)) {
      return new XSDType(XSDType.XSDAtomicType.BOOLEAN, Cardinality.ONE);
    }

    if (isOperatorClassAddition(operator)) {
      final Type leftType = expressionTypes.get(operatorNode.getOperand());
      final Type rightType = expressionTypes.get(operatorNode.getRightSide());
      if (leftType.isNumeric() && rightType.isNumeric()) {
        return leftType; // TODO rio vybrat obecnejsi z typov
      } else if (leftType.isNumeric()) {
        return leftType;
      } else if (rightType.isNumeric()) {
        return rightType;
      } // TODO rio else?
    }

    if (operator == Operator.TO) {
      return new XSDType(XSDType.XSDAtomicType.INTEGER, Cardinality.ZERO_OR_MORE);
    }

    return null;
  }

  private static boolean isOperatorClassComparison(final Operator op) {
    switch (op) {
      case GEN_EQUALS:
      case GEN_GREATER_THAN:
      case GEN_GREATER_THAN_EQUALS:
      case GEN_LESS_THAN:
      case GEN_LESS_THAN_EQUALS:
      case GEN_NOT_EQUALS:
      case VAL_EQUALS:
      case VAL_GREATER_THAN:
      case VAL_GREATER_THAN_EQUALS:
      case VAL_LESS_THAN:
      case VAL_LESS_THAN_EQUALS:
      case VAL_NOT_EQUALS:
        return true;
      default:
        return false;
    }
  }

  private static boolean isOperatorClassAddition(final Operator op) {
    switch (op) {
      case PLUS:
      case MINUS:
        return true;
      default:
        return false; // TODO rio treba pridat aj unarne plus a minus
    }
  }
}
