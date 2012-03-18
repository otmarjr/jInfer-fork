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

import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.FLWORExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ForClauseNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.FunctionCallNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.LiteralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Operator;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.OperatorNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VariableBindingNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.WhereClauseNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.XQNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.NegativeUniquenessStatement;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.BuiltinFunctions;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rio
 */
public class NegativeUniquenessFinder {
 
  private final ModuleNode root;
  private final List<NegativeUniquenessStatement> negativeUniquenessStatements = new ArrayList<NegativeUniquenessStatement>();
  
  public NegativeUniquenessFinder(final ModuleNode root) {
    this.root = root;
  }
  
  public void process() {
    processRecursive(root);
  }
  
  public List<NegativeUniquenessStatement> getNegativeUniquenessStatements() {
    return negativeUniquenessStatements;
  }
  
  private void processRecursive(final XQNode node) {
    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : node.getSubnodes()) {
        processRecursive(subnode);
      }
    }

    rejectionOfUniqueness_aggregationFunctions(node);
    rejectionOfUniqueness_comparisonWithAConstant(node);
  }

  private void rejectionOfUniqueness_aggregationFunctions(final XQNode node) {
    if (FunctionCallNode.class.isInstance(node)) {
      final FunctionCallNode funcCallNode = (FunctionCallNode) node;
      final String funcName = funcCallNode.getFuncName();
      final String builtinFuncName = BuiltinFunctions.isBuiltinFunction(funcName);
      if (builtinFuncName != null
              && builtinFuncName.equals("distinct-values") // Bereme len distinct-values, kedze min, max a sum sa uz zapocitavaju v ramci hladania join patterns.
              //|| builtinFuncName.equals("min")
              //|| builtinFuncName.equals("max")
              //|| builtinFuncName.equals("sum")
              ) {
        final ExprNode argument = funcCallNode.getParams().get(0);
        assert (argument != null);
        final Type type = argument.getType();
        if (type.getCategory() != Type.Category.PATH) {
          return;
        }

        final PathType pathType = (PathType) type;
        if (PathTypeUtils.usesOnlyChildAndDescendantAxes(pathType) && PathTypeUtils.isWithoutPredicatesExceptLastStep(pathType)) {
          memorizeNegativeWeight(pathType, 100);
        }
      }
    }
  }

  private void rejectionOfUniqueness_comparisonWithAConstant(final XQNode node) {
    if (!FLWORExprNode.class.isInstance(node)) {
      return;
    }

    final FLWORExprNode flworNode = (FLWORExprNode) node;
    final List<String> forVars = new ArrayList<String>();

    for (final VariableBindingNode bindingNode : flworNode.getTupleStreamNode().getBindingClauses()) {
      if (ForClauseNode.class.isInstance(bindingNode)) {
        final ExprNode exprNode = bindingNode.getBindingSequenceNode().getExprNode();
        if (exprNode.getType().getCategory() == Type.Category.PATH) {
          if (PathTypeUtils.usesOnlyChildAndDescendantAxes((PathType) exprNode.getType())) {
            forVars.add(bindingNode.getVarName());
          }
        }
      }
    }

    final WhereClauseNode whereClauseNode = flworNode.getWhereClauseNode();
    if (whereClauseNode != null) {
      rejectionOfUniqueness_processWhere(flworNode, forVars);
    }
  }

  private void rejectionOfUniqueness_processWhere(final ExprNode whereExprNode, final List<String> forVars) {
    if (!OperatorNode.class.isInstance(whereExprNode)) {
      return;
    }

    final Operator op = ((OperatorNode) whereExprNode).getOperator();
    if (op == Operator.GEN_EQUALS) {
      final ExprNode leftOperand = ((OperatorNode) whereExprNode).getOperand();
      final ExprNode rightOperand = ((OperatorNode) whereExprNode).getRightSide();

      if (leftOperand.getType().getCategory() == Type.Category.PATH
              && LiteralNode.class.isInstance(rightOperand)) {
        final PathType pathType = (PathType) leftOperand.getType();
        if (PathTypeUtils.usesOnlyChildAndDescendantAxes(pathType) && PathTypeUtils.isWithoutPredicatesExceptLastStep(pathType)) {
          for (final String var : forVars) {
            final ExprNode detailNode = pathType.getSteps().get(0).getDetailNode();
            if (detailNode != null) {
              if (VarRefNode.class.isInstance(detailNode)) {
                if (((VarRefNode) detailNode).getVarName().equals(var)) {
                  memorizeNegativeWeight(pathType, 90);
                }
              }
            }
          }
        }
      } else if (rightOperand.getType().getCategory() == Type.Category.PATH
              && LiteralNode.class.isInstance(leftOperand)) {
        final PathType pathType = (PathType) rightOperand.getType();
        if (PathTypeUtils.usesOnlyChildAndDescendantAxes(pathType) && PathTypeUtils.isWithoutPredicatesExceptLastStep(pathType)) {
          for (final String var : forVars) {
            final ExprNode detailNode = pathType.getSteps().get(0).getDetailNode();
            if (detailNode != null) {
              if (VarRefNode.class.isInstance(detailNode)) {
                if (((VarRefNode) detailNode).getVarName().equals(var)) {
                  memorizeNegativeWeight(pathType, 90);
                }
              }
            }
          }
        }
      }
    }
  }

  private void memorizeNegativeWeight(final PathType pathType, final int weight) {
    /*Integer oldWeight = negativeWeights.get(pathType);
    int newWeight = weight;
    if (oldWeight != null) {
      newWeight += oldWeight.intValue();
    }
    negativeWeights.put(pathType, newWeight);*/
    negativeUniquenessStatements.add(new NegativeUniquenessStatement(pathType, weight));
  }
}
