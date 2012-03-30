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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns;

import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.InitialStep;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Operator;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.OperatorNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import java.util.List;

/**
 * Parser of an expression node of form var1/L1 = var2/L2 for specified variables var1, var2.
 * @see ExpressionNode
 * @see PathExprNode
 * @author rio
 */
public final class JoinWhereFormExprParser {

  private final boolean isExprWhereForm;
  private PathType L1;
  private PathType L2;

  public JoinWhereFormExprParser(final ExprNode exprNode, final String varName1, final String varName2) {
    isExprWhereForm = isWhereClauseForm(exprNode, varName1, varName2);
  }

  /**
   * Is the expression passed to the constructor of a desired form?
   */
  public boolean isExprWhereForm() {
    return isExprWhereForm;
  }

  /**
   * If {@link #isExprPredicateForm()} method returned true, this method returns
   * the path L1.
   * @return Path L1.
   */
  public PathType getL1() {
    return L1;
  }

  /**
   * If {@link #isExprPredicateForm()} method returned true, this method returns
   * the path L2.
   * @return Path L2.
   */
  public PathType getL2() {
    return L2;
  }

  private boolean isWhereClauseForm(final ExprNode exprNode, final String var1, final String var2) {
    if (!OperatorNode.class.isInstance(exprNode)) {
      return false;
    }

    final OperatorNode operatorNode = (OperatorNode) exprNode;
    if (operatorNode.getOperator() != Operator.GEN_EQUALS) {
      return false;
    }

    final Type leftOperandType = operatorNode.getOperand().getType();
    final Type rightOperandType = operatorNode.getRightSide().getType();

    if (leftOperandType.getCategory() != Type.Category.PATH || rightOperandType.getCategory() != Type.Category.PATH) {
      return false;
    }

    final PathType leftPathType = (PathType) leftOperandType;
    final PathType rightPathType = (PathType) rightOperandType;

    final ExprNode leftDetailNode = leftPathType.getSteps().get(0).getDetailNode();
    final ExprNode rightDetailNode = rightPathType.getSteps().get(0).getDetailNode();

    if (leftDetailNode == null || rightDetailNode == null) {
      return false;
    }

    if (!VarRefNode.class.isInstance(leftDetailNode) || !VarRefNode.class.isInstance(rightDetailNode)) {
      return false;
    }

    final VarRefNode leftVarRefNode = (VarRefNode) leftDetailNode;
    final VarRefNode rightVarRefNode = (VarRefNode) rightDetailNode;

    if (leftVarRefNode.getVarName().equals(var1) && rightVarRefNode.getVarName().equals(var2)) {
      List<StepExprNode> L1Steps = leftPathType.getSteps().subList(1, leftPathType.getSteps().size());
      List<StepExprNode> L2Steps = rightPathType.getSteps().subList(1, rightPathType.getSteps().size());
      L1 = new PathType(new PathExprNode(L1Steps, InitialStep.CONTEXT));
      L2 = new PathType(new PathExprNode(L2Steps, InitialStep.CONTEXT));
      return true;
    }

    if (rightVarRefNode.getVarName().equals(var1) && leftVarRefNode.getVarName().equals(var2)) {
      List<StepExprNode> L2Steps = leftPathType.getSteps().subList(1, leftPathType.getSteps().size());
      List<StepExprNode> L1Steps = rightPathType.getSteps().subList(1, rightPathType.getSteps().size());
      L1 = new PathType(new PathExprNode(L1Steps, InitialStep.CONTEXT));
      L2 = new PathType(new PathExprNode(L2Steps, InitialStep.CONTEXT));
      return true;
    }

    return false;
  }
}
