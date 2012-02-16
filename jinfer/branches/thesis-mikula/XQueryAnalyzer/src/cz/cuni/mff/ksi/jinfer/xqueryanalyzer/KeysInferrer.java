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
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.ForBoundPathType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.Type;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rio
 */
public class KeysInferrer {
  
  public void processFLWORExpr(final FLWORExprNode flworExpr, Map<String, FLWORExprNode> forVariables) {
    final List<VariableBindingNode> bindingNodes = flworExpr.getTupleStreamNode().getBindingClauses();
    final WhereClauseNode whereClauseNode = flworExpr.getWhereClauseNode();
    
    boolean checkJoinPattern3 = false;
    ExprNode whereExpr = null;
    
    if (whereClauseNode != null) {
      whereExpr = whereClauseNode.getExprNode();
      if (OperatorNode.class.isInstance(whereExpr)) {
        if (((OperatorNode)whereExpr).getOperator() == Operator.GEN_EQUALS) {
          checkJoinPattern3 = true;
        }
      }
    }
    
    for (final VariableBindingNode bindingNode : bindingNodes) {
      final ExprNode bindingExpr = bindingNode.getBindingSequenceNode().getExprNode();
      final Type bindingExprType = bindingExpr.getType();
      if (bindingExprType.getCategory() == Type.Category.PATH
              || bindingExprType.getCategory() == Type.Category.FOR_BOUND_PATH) {
        
      }
    }
  }
  
  private static boolean isConformingPathType(final Type type) { // TODO rio zjednotit path types pod spolocny typ
    if (type.getCategory() == Type.Category.PATH) {
      final PathType pathType = (PathType)type;
      final StepExprNode lastStepNode = pathType.getStepNodes().get(pathType.getStepNodes().size() - 1);
      for (final StepExprNode stepNode : pathType.getStepNodes()) {
        if (stepNode.hasPredicates() && stepNode != lastStepNode) {
          return false;
        }
        
        if (stepNode.isAxisStep()) {
          final AxisNode axisNode = stepNode.getAxisNode();
          if (axisNode.getAxisKind() != AxisKind.CHILD
                  && axisNode.getAxisKind() != AxisKind.DESCENDANT_OR_SELF) {
            return false;
          }
        }
        
        final ExprNode detailNode = stepNode.getDetailNode();
        if (detailNode != null) {
          if (VarRefNode.class.isInstance(detailNode)) {
            if (isConformingPathType(pathType.getForBoundSubsteps().get(stepNode)) == false) {
              return false;
            }
          }
        }
      }
      
      if (!lastStepNode.hasPredicates()) {
        return false;
      }
      
      if (lastStepNode.getPredicateListNode().getPredicates().size() != 1) {
        return false;
      }
    } else if (type.getCategory() == Type.Category.FOR_BOUND_PATH) {
      final ForBoundPathType fbpt = (ForBoundPathType)type;
      return isConformingPathType(fbpt.getPathType());
    } else {
      assert(false);
    }
    
    return true;
  }
}
