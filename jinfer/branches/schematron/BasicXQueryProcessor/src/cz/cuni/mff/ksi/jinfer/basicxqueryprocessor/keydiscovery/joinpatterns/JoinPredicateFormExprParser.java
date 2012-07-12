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

import com.sun.org.apache.xpath.internal.ExpressionNode;
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
 * Parser of an expression node of form L2 = var/L1 for a specified variable var.
 * @see ExpressionNode
 * @see PathExprNode
 * @author rio
 */
public final class JoinPredicateFormExprParser {

  private boolean isExprPredicateForm;
  private PathType L1;
  private PathType L2;

  public JoinPredicateFormExprParser(final ExprNode exprNode, final String varName) {
    isExprPredicateForm = isPredicateForm(exprNode, varName);
  }
  
  /**
   * Is the expression passed to the constructor of a desired form?
   */
  public boolean isExprPredicateForm() {
    return isExprPredicateForm;
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
  
  private boolean isPredicateForm(final ExprNode exprNode, final String varName) {
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

    if (leftDetailNode == null && rightDetailNode == null) {
      return false;
    }

    if (VarRefNode.class.isInstance(leftDetailNode)) {
      final VarRefNode leftVarRefNode = (VarRefNode) leftDetailNode;

      if (leftVarRefNode.getVarName().equals(varName)) {
        List<StepExprNode> L1Steps = leftPathType.getSteps().subList(1, leftPathType.getSteps().size());
        L1 = new PathType(new PathExprNode(L1Steps, InitialStep.CONTEXT));
        L2 = new PathType((PathExprNode) operatorNode.getRightSide());
        return true;
      }
    }

    if (VarRefNode.class.isInstance(rightDetailNode)) {
      final VarRefNode rightVarRefNode = (VarRefNode) rightDetailNode;

      if (rightVarRefNode.getVarName().equals(varName)) {
        List<StepExprNode> L1Steps = rightPathType.getSteps().subList(1, rightPathType.getSteps().size());
        L1 = new PathType(new PathExprNode(L1Steps, InitialStep.CONTEXT));
        L2 = new PathType((PathExprNode) operatorNode.getOperand());
        return true;
      }
    }

    return false;
  }
}
