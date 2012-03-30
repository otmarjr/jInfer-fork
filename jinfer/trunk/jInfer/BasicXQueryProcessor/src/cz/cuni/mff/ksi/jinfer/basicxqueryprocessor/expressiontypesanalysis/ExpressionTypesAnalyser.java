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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.expressiontypesanalysis;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.XSDType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.UnknownType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.AbstractType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides analysis of expression types for a syntax tree.
 * 
 * After a call to {@link #process()} method, the types of expression are written
 * the expression nodes in the syntax tree.
 * 
 * @see Type
 * @see ExpressionNode
 * 
 * @author rio
 */
public class ExpressionTypesAnalyser {
  
  private final ModuleNode root;
  private final FunctionsAnalyser functionsAnalyser;

  /**
   * 
   * @param root A syntax tree.
   * @param functionsAnalyser A functions analyser for the syntax tree.
   */
  public ExpressionTypesAnalyser(final ModuleNode root, final FunctionsAnalyser functionsAnalyser) {
    this.root = root;
    this.functionsAnalyser = functionsAnalyser;
  }
  
  /**
   * Runs the analysis of the types. The determined types are written directly
   * to the expression nodes in the syntax tree.
   */
  public void process() {
    // Step 1: Determine types of global variables.
    final PrologNode prologNode = root.getPrologNode();
    final Map<String, Type> globalVarTypes = new HashMap<String, Type>();
    if (prologNode != null) {
      globalVarTypes.putAll(analysisOfGlobalVarTypes(prologNode, functionsAnalyser));
    }
    
    // Step 2: Determine types of the query body.
    final QueryBodyNode queryBodyNode = root.getQueryBodyNode();
    if (queryBodyNode != null) {
      final Map<String, Type> vars = analysisOfExpressionTypes(queryBodyNode, globalVarTypes, functionsAnalyser);
      assert(vars.isEmpty());
    }
  }
  
  private static Map<String, Type> analysisOfGlobalVarTypes(final PrologNode prologNode, final FunctionsAnalyser fp) {
    final Map<String, Type> globalVarTypes = new HashMap<String, Type>();
    
    for (final XQNode node : prologNode.getSubnodes()) {
      if (VarDeclNode.class.isInstance(node)) {
        final VarDeclNode varDeclNode = (VarDeclNode)node;
        final String varName = varDeclNode.getVarName();
        final TypeNode typeNode = varDeclNode.getTypeDeclarationNode();
        
        // If the variable definition declares its type, use it.
        // Otherwise, determine its type from the expression.
        if (typeNode != null) {
          globalVarTypes.put(varName, AbstractType.createType(typeNode));
        } else {
          // External variables are not processed.
          if (varDeclNode.getVarValueNode().isExternal()) {
            globalVarTypes.put(varName, new UnknownType());
          } else {
            final ExprNode exprNode = varDeclNode.getVarValueNode().getExprNode();
            analysisOfExpressionTypes(exprNode, globalVarTypes, fp);
            globalVarTypes.put(varName, exprNode.getType());
          }
        }
      }
    }
    
    return globalVarTypes;
  }
  
  private static Map<String, Type> analysisOfExpressionTypes(final XQNode root, final Map<String, Type> contextVarTypes, final FunctionsAnalyser fp) {
    // Copy context variable types as we do not want to modify it for the previous recursive calls.
    final Map<String, Type> localContextVarTypes = new HashMap<String, Type>(contextVarTypes);
    final Map<String, Type> newContextVarTypes = new HashMap<String, Type>();

    // Analyse types of the subnodes.
    final List<XQNode> subnodes = root.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : root.getSubnodes()) {
        final Map<String, Type> newVars = analysisOfExpressionTypes(subnode, localContextVarTypes, fp);
        newContextVarTypes.putAll(newVars);
        // Extend context variable types for the next subnodes.
        localContextVarTypes.putAll(newVars);
      }
    }

    // If root is an expression, analyze it and save its type.
    if (ExprNode.class.isInstance(root)) {
      ((ExprNode)root).setType(determineExpressionType(fp, (ExprNode) root, localContextVarTypes));
    }

    // If root extends context variables for its siblings in recursion, return those variables.
    Map<String, Type> newVars = new HashMap<String, Type>();
    if (VariableBindingNode.class.isInstance(root)) {
      final VariableBindingNode variableBindingNode = (VariableBindingNode) root;
      if (variableBindingNode.getTypeNode() != null) {
        newVars.put(variableBindingNode.getVarName(), AbstractType.createType(variableBindingNode.getTypeNode()));
      } else {
        final Type type = determineExpressionType(fp, variableBindingNode.getBindingSequenceNode().getExprNode(), localContextVarTypes);
        if (ForClauseNode.class.isInstance(variableBindingNode)) {
          // If the binding is for binding, we make a for bound type.
          newVars.put(variableBindingNode.getVarName(), AbstractType.createForBoundType(type));
        } else {
          newVars.put(variableBindingNode.getVarName(), type);
        }
      }
    } else if (TupleStreamNode.class.isInstance(root)) {
      newVars = newContextVarTypes;
    } else if (InClausesNode.class.isInstance(root)) {
      newVars = newContextVarTypes;
    }

    return newVars;
  }

  private static Type determineExpressionType(final FunctionsAnalyser fp, final ExprNode expressionNode, final Map<String, Type> contextVarTypes) {
    if (LiteralNode.class.isInstance(expressionNode)) {
      final LiteralNode literalNode = (LiteralNode) expressionNode;
      return new XSDType(literalNode.getLiteralType());
    } else if (FunctionCallNode.class.isInstance(expressionNode)) {
      final FunctionCallNode functionCallNode = (FunctionCallNode) expressionNode;
      return fp.getFunctionType(functionCallNode);
    } else if (OperatorNode.class.isInstance(expressionNode)) {
      return determineOperatorType((OperatorNode) expressionNode);
    } else if (VarRefNode.class.isInstance(expressionNode)) {
      // We need to do this to achieve a correct type for this case. If the type is PathType, it needs to be corrected to contain one step with the variable reference to the original path.
      final Type varType = contextVarTypes.get(((VarRefNode) expressionNode).getVarName());
      if (varType.isPathType()) {
        final List<StepExprNode> steps = new ArrayList<StepExprNode>();
        final StepExprNode step = new StepExprNode(expressionNode, null);
        steps.add(step);
        final Map<StepExprNode, PathType> substeps = new HashMap<StepExprNode, PathType>();
        substeps.put(step, (PathType)varType);
        return new PathType(steps, ((PathType)varType).getInitialStep(), substeps, false);
      } else {
        return varType;
      }
    } else if (FLWORExprNode.class.isInstance(expressionNode)) {
      return AbstractType.createForUnboundType(((FLWORExprNode) expressionNode).getReturnClauseNode().getExprNode().getType());
    } else if (PathExprNode.class.isInstance(expressionNode)) {
      return new PathType((PathExprNode)expressionNode);
    } else if (ConstructorNode.class.isInstance(expressionNode)) {
      //final ConstructorNode constructorNode = (ConstructorNode)expressionNode;
      // TODO rio How to determine a type of a constructor?
      return new UnknownType();
    } else if (CommaOperatorNode.class.isInstance(expressionNode)) {
      final CommaOperatorNode commaOperatorNode = (CommaOperatorNode)expressionNode;
      final Type type = commaOperatorNode.getExpressionNodes().get(0).getType();
      for (final ExprNode exprNode : commaOperatorNode.getExpressionNodes()) {
        if (!exprNode.getType().equals(type)) {
          return new UnknownType();
        }
        // TODO rio How to determine a type fo a comma operator?
        return new UnknownType();
      }
    } else if (IfExprNode.class.isInstance(expressionNode)) {
      final IfExprNode ifExprNode = (IfExprNode)expressionNode;
      return ifExprNode.getThenExpressionNode().getExprNode().getType();
    } else if (QuantifiedExprNode.class.isInstance(expressionNode)) {
      return new XSDType(XSDBuiltinAtomicType.BOOLEAN, Cardinality.ONE);
    }
    assert (false); // TODO rio Implement other expressions.
    return null;
  }

  private static Type determineOperatorType(final OperatorNode operatorNode) {
    if (operatorNode.getTypeNode() != null) {
      return AbstractType.createType(operatorNode.getTypeNode());
    }

    final Operator operator = operatorNode.getOperator();

    if (isOperatorClassComparison(operator)) {
      return new XSDType(XSDBuiltinAtomicType.BOOLEAN, Cardinality.ONE);
    }

    if (isOperatorClassAddition(operator)) {
      final Type leftType = operatorNode.getOperand().getType();
      
      // Handle an unary operator.
      if (operatorNode.getRightSide() == null) {
        return leftType;
      }
      
      final Type rightType = operatorNode.getRightSide().getType();
      
      // TODO rio What about cardinality and sequences?
      if (leftType.isNumeric() && rightType.isNumeric()) {
        return selectCommonType(leftType, rightType); 
      } else if (leftType.isNumeric()) {
        return leftType;
      } else if (rightType.isNumeric()) {
        return rightType;
      }
    }

    if (operator == Operator.TO) {
      return new XSDType(XSDBuiltinAtomicType.INTEGER, Cardinality.ZERO_OR_MORE);
    }

    return new UnknownType();
  }
  
  private static Type selectCommonType(final Type numericType1, final Type numericType2) {
    assert(numericType1.isNumeric());
    assert(numericType2.isNumeric());
    
    final XSDType type1 = (XSDType)numericType1;
    final XSDType type2 = (XSDType)numericType2;
    
    final XSDBuiltinAtomicType atomic1 = type1.getAtomicType();
    final XSDBuiltinAtomicType atomic2 = type2.getAtomicType();
    
    if (atomic1 == atomic2) {
      return type1;
    }
    
    if (atomic1 == XSDBuiltinAtomicType.DOUBLE) {
      return type1;
    }
    if (atomic2 == XSDBuiltinAtomicType.DOUBLE) {
      return type2;
    }
    
    if (atomic1 == XSDBuiltinAtomicType.FLOAT) {
      return type1;
    }
    if (atomic2 == XSDBuiltinAtomicType.FLOAT) {
      return type2;
    }
    
    if (atomic1 == XSDBuiltinAtomicType.DECIMAL) {
      return type1;
    }
    if (atomic2 == XSDBuiltinAtomicType.DECIMAL) {
      return type2;
    }
    
    if (atomic1 == XSDBuiltinAtomicType.INTEGER) {
      return type1;
    }
    if (atomic2 == XSDBuiltinAtomicType.INTEGER) {
      return type2;
    }
    
    return new XSDType(XSDBuiltinAtomicType.INTEGER, Cardinality.ONE);
    
    // TODO rio Not finished, but probably sufficient for our purpose.
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
      case UNARY_PLUS:
      case UNARY_MINUS:
        return true;
      default:
        return false;
    }
  }
}
