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
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.Type;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.TypeFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author rio
 */
public class KeysInferrer {
  
  private final XQNode root;  
  private final List<JoinPattern> joinPatterns = new ArrayList<JoinPattern>();
  private final List<ClassifiedJoinPattern> classifiedJoinPatterns = new ArrayList<ClassifiedJoinPattern>();
  
  public KeysInferrer(final XQNode root) {
    this.root = root;
  }
  
  public void process() {
    Map<String, ForClauseNode> forVariables = new HashMap<String, ForClauseNode>();
    processRecursive(root, forVariables);
    classifyJoinPatterns();
  }
  
  private void processRecursive(final XQNode node, Map<String, ForClauseNode> forVariables) {
    if (FLWORExprNode.class.isInstance(node)) {
      forVariables = processFLWORExpr((FLWORExprNode)node, forVariables);
    }
    
    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : node.getSubnodes()) {
        processRecursive(subnode, forVariables);
      }
    }
  }
  
  // TODO rio Vyriesit planost premennych v mape forVariables. Aj do DP.
  public Map<String, ForClauseNode> processFLWORExpr(final FLWORExprNode flworExpr, Map<String, ForClauseNode> forVariables) {
    final List<VariableBindingNode> bindingNodes = flworExpr.getTupleStreamNode().getBindingClauses();
    final WhereClauseNode whereClauseNode = flworExpr.getWhereClauseNode();
    
    boolean checkJoinPattern3 = false; // Doplnit do diplomky do volania funckie determineJoinPatterns.
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
      if (bindingExprType.getCategory() == Type.Category.PATH) {
        if (usesOnlyChildAndDescendantAxes((PathType)bindingExprType)) {
          final ExprNode predicate = endsWithExactlyOnePredicate((PathType)bindingExprType);
          if (predicate != null) {
            for (final Entry<String, ForClauseNode> entry : forVariables.entrySet()) {
              determineJoinPattern(bindingNode, predicate, entry.getValue(), entry.getKey(), forVariables, checkJoinPattern3, whereExpr);
            }
          } else {
            if (ForClauseNode.class.isInstance(bindingNode)) {
              forVariables.put(bindingNode.getVarName(), (ForClauseNode)bindingNode);
            }
          }
        }
      }
    }
    
    return forVariables;
  }
  
  private static boolean usesOnlyChildAndDescendantAxes(final PathType pathType) {
    for (final StepExprNode stepNode : pathType.getStepNodes()) {
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
          if (usesOnlyChildAndDescendantAxes(pathType.getForBoundSubsteps().get(stepNode)) == false) {
            return false;
          }
        }
      }
    }
    
    return true;
  }
  
  // Vracia ten jediny predikat na konci.
  private static ExprNode endsWithExactlyOnePredicate(final PathType pathType) {
    final StepExprNode lastStepNode = pathType.getStepNodes().get(pathType.getStepNodes().size() - 1);
    if (!lastStepNode.hasPredicates()) {
      return null;
    }

    if (lastStepNode.getPredicateListNode().getPredicates().size() != 1) {
      return null;
    }
    
    return lastStepNode.getPredicateListNode().getPredicates().get(0);
  }
  
  private void determineJoinPattern(final VariableBindingNode bindingNode, final ExprNode predicate, final ForClauseNode forClauseNode, final String forVar, final Map<String, ForClauseNode> forVariables, final boolean checkJoinPattern3, final ExprNode whereExpr) {
    // At first, check join pattern 3.
    if (checkJoinPattern3) {
      if (ForClauseNode.class.isInstance((bindingNode))) {
        if (isExprJoinForm(whereExpr, bindingNode.getVarName(), forVar)) {
          joinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.JP3, forClauseNode, bindingNode));
        }
      }
      return;
    }
    
    // Check for and let join patterns.
    if (isExprJoinForm(predicate, bindingNode.getVarName(), forVar)) {
      if (ForClauseNode.class.isInstance(bindingNode)) {
        joinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.FOR, forClauseNode, bindingNode));
        forVariables.remove(forVar);
      } else if (LetClauseNode.class.isInstance(bindingNode)) {
        joinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.LET, forClauseNode, bindingNode));
        forVariables.remove(forVar);
      } else {
        assert(false);
      }
    }
  }
  
  private static boolean isExprJoinForm(final ExprNode exprNode, final String var1, final String var2) {
    if (!OperatorNode.class.isInstance(exprNode)) {
      return false;
    }
    
    final OperatorNode operatorNode = (OperatorNode)exprNode;
    if (operatorNode.getOperator() != Operator.GEN_EQUALS) {
      return false;
    }
    
    final Type leftOperandType = operatorNode.getOperand().getType();
    final Type rightOperandType = operatorNode.getRightSide().getType();
    
    if (leftOperandType.getCategory() != Type.Category.PATH || rightOperandType.getCategory() != Type.Category.PATH) {
      return false;
    }
    
    final PathType leftPathType = (PathType)leftOperandType;
    final PathType rightPathType = (PathType)rightOperandType;
    
    final ExprNode leftDetailNode = leftPathType.getStepNodes().get(0).getDetailNode();
    final ExprNode rightDetailNode = rightPathType.getStepNodes().get(0).getDetailNode();
    
    if (leftDetailNode == null || rightDetailNode == null) {
      return false;
    }
    
    if (!VarRefNode.class.isInstance(leftDetailNode) || !VarRefNode.class.isInstance(rightDetailNode)) {
      return false;
    }
    
    final VarRefNode leftVarRefNode = (VarRefNode)leftDetailNode;
    final VarRefNode rightVarRefNode = (VarRefNode)rightDetailNode;
    
    if ((leftVarRefNode.getVarName().equals(var1) && rightVarRefNode.getVarName().equals(var2))
            ||
            (rightVarRefNode.getVarName().equals(var1) && leftVarRefNode.getVarName().equals(var2)))
    {
      return true;
    }
    
    return false;
  }
  
  private void classifyJoinPatterns() {
    for (final JoinPattern joinPattern : joinPatterns) {
      if (joinPattern.getType() == JoinPattern.JoinPatternType.FOR) {
        classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 100));
      } else if (joinPattern.getType() == JoinPattern.JoinPatternType.JP3) {
        classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 50));
      } else {
        // TODO rio pravidla R2-R5
        
        // R2
        final FLWORExprNode flworNode = (FLWORExprNode)joinPattern.getSecondVariableBindingNode().getParentNode().getParentNode();
        List<PathType> returnPathTypes = getTargetReturnPathTypes(flworNode, joinPattern.getSecondVariableBindingNode().getVarName());
        boolean cont = true;
        for (final PathType pathType : returnPathTypes) {
          if (!cont) {
            break;
          }
          for (final String functionName : pathType.getSpecialFunctionCalls()) {
            if (functionName.equals("min")
                    || functionName.equals("max")
                    || functionName.equals("avg")
                    || functionName.equals("sum")) { // TODO rio pozor na nazvy funkcii, mozu byt aj prefixovane.
              classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 100));
              cont = false;
              break;
            }
          }
        }
        
        // R3
        cont = true;
        for (final PathType pathType : returnPathTypes) {
          if (!cont) {
            break;
          }
          for (final String functionName : pathType.getSpecialFunctionCalls()) {
            if (functionName.equals("count")) { // TODO rio pozor na nazvy funkcii, mozu byt aj prefixovane.
              classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 75));
              cont = false;
              break;
            }
          }
        }
        
        // R4 R5
        final List<PathExprNode> returnPaths = getTargetReturnPaths(flworNode, joinPattern.getSecondVariableBindingNode().getVarName());
        final int returnPathsNumber = returnPaths.size();
        if (returnPathsNumber > 1) {
          classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O2, 100));
        } else {
          classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O2, 50));
        }
      }
    }
  }
  
  private static boolean isTargetPath(final PathType pathType, final String varName) {
    final StepExprNode firstStep = pathType.getStepNodes().get(0);
    final ExprNode detailNode = firstStep.getDetailNode();
    
    if (detailNode == null) {
      return false;
    }
    
    if (!VarRefNode.class.isInstance(detailNode)) {
      return false;
    }
    
    if (((VarRefNode)detailNode).getVarName().equals(varName)) {
      return true;
    } else {
      return false;
    }
  }
  
  private static List<PathType> getTargetReturnPathTypes(final FLWORExprNode flworNode, final String varName) {
    final List<PathType> paths = new ArrayList<PathType>();
    getTargetReturnPathTypesRecursive(flworNode.getReturnClauseNode(), varName, paths);
    return paths;
  }
  
  private static void getTargetReturnPathTypesRecursive(final XQNode node, final String varName, final List<PathType> paths) {
    if (ExprNode.class.isInstance(node)) {
      final Type type = ((ExprNode)node).getType();
      if (type.getCategory() == Type.Category.PATH) {
        if (isTargetPath((PathType)type, varName)) {
          paths.add((PathType)type);
        }
      }
    }
    
    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : subnodes) {
        getTargetReturnPathTypesRecursive(subnode, varName, paths);
      }
    }
  }
  
  private static List<PathExprNode> getTargetReturnPaths(final FLWORExprNode flworNode, final String varName) {
    final List<PathExprNode> paths = new ArrayList<PathExprNode>();
    getTargetReturnPathsRecursive(flworNode, varName, paths);
    return paths;
  }
  
  private static void getTargetReturnPathsRecursive(final XQNode node, final String varName, final List<PathExprNode> paths) {
    if (PathExpr.class.isInstance(node)) {
      final PathType type = TypeFactory.createPathType((PathExprNode)node);
      if (isTargetPath(type, varName)) {
        paths.add((PathExprNode)node);
      }
    }
    
    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : subnodes) {
        getTargetReturnPathsRecursive(subnode, varName, paths);
      }
    }
  }
}
