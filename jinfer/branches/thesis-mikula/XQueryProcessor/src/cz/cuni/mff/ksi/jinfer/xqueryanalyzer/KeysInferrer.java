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

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.xqueryprocessor.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.TypeFactory;
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

  private class JoinExprFormParser {

    private PathExprNode L1;
    private PathExprNode L2;

    public boolean isExprJoinFormPredicate(final ExprNode exprNode, final String var1) {
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

      final ExprNode leftDetailNode = leftPathType.getPathExprNode().getSteps().get(0).getDetailNode();
      final ExprNode rightDetailNode = rightPathType.getPathExprNode().getSteps().get(0).getDetailNode();

      if (leftDetailNode == null && rightDetailNode == null) {
        return false;
      }

      if (VarRefNode.class.isInstance(leftDetailNode)) {
        final VarRefNode leftVarRefNode = (VarRefNode) leftDetailNode;

        if (leftVarRefNode.getVarName().equals(var1)) {
          List<StepExprNode> L1Steps = leftPathType.getPathExprNode().getSteps().subList(1, leftPathType.getPathExprNode().getSteps().size());
          L1 = new PathExprNode(L1Steps, InitialStep.CONTEXT);
          L2 = (PathExprNode) operatorNode.getRightSide();
          return true;
        }
      }

      if (VarRefNode.class.isInstance(rightDetailNode)) {
        final VarRefNode rightVarRefNode = (VarRefNode) rightDetailNode;

        if (rightVarRefNode.getVarName().equals(var1)) {
          List<StepExprNode> L1Steps = rightPathType.getPathExprNode().getSteps().subList(1, rightPathType.getPathExprNode().getSteps().size());
          L1 = new PathExprNode(L1Steps, InitialStep.CONTEXT);
          L2 = (PathExprNode) operatorNode.getOperand();
          return true;
        }
      }

      return false;
    }

    public boolean isExprJoinFormWhereClause(final ExprNode exprNode, final String var1, final String var2) {
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

      final ExprNode leftDetailNode = leftPathType.getPathExprNode().getSteps().get(0).getDetailNode();
      final ExprNode rightDetailNode = rightPathType.getPathExprNode().getSteps().get(0).getDetailNode();

      if (leftDetailNode == null || rightDetailNode == null) {
        return false;
      }

      if (!VarRefNode.class.isInstance(leftDetailNode) || !VarRefNode.class.isInstance(rightDetailNode)) {
        return false;
      }

      final VarRefNode leftVarRefNode = (VarRefNode) leftDetailNode;
      final VarRefNode rightVarRefNode = (VarRefNode) rightDetailNode;

      if (leftVarRefNode.getVarName().equals(var1) && rightVarRefNode.getVarName().equals(var2)) {
        List<StepExprNode> L1Steps = leftPathType.getPathExprNode().getSteps().subList(1, leftPathType.getPathExprNode().getSteps().size());
        List<StepExprNode> L2Steps = rightPathType.getPathExprNode().getSteps().subList(1, rightPathType.getPathExprNode().getSteps().size());
        L1 = new PathExprNode(L1Steps, InitialStep.CONTEXT);
        L2 = new PathExprNode(L2Steps, InitialStep.CONTEXT);
        return true;
      }

      if (rightVarRefNode.getVarName().equals(var1) && leftVarRefNode.getVarName().equals(var2)) {
        List<StepExprNode> L2Steps = leftPathType.getPathExprNode().getSteps().subList(1, leftPathType.getPathExprNode().getSteps().size());
        List<StepExprNode> L1Steps = rightPathType.getPathExprNode().getSteps().subList(1, rightPathType.getPathExprNode().getSteps().size());
        L1 = new PathExprNode(L1Steps, InitialStep.CONTEXT);
        L2 = new PathExprNode(L2Steps, InitialStep.CONTEXT);
        return true;
      }

      return false;
    }

    public PathExprNode getL1() {
      return L1;
    }

    public PathExprNode getL2() {
      return L2;
    }
  }

  private final List<JoinPattern> joinPatterns = new ArrayList<JoinPattern>();
  private final List<ClassifiedJoinPattern> classifiedJoinPatterns = new ArrayList<ClassifiedJoinPattern>();
  private final List<WeightedKey> keys = new ArrayList<WeightedKey>();
  private final List<WeightedForeignKey> foreignKeys = new ArrayList<WeightedForeignKey>();
  private final List<NegativeUniquenessStatement> negativeUniquenessStatements = new ArrayList<NegativeUniquenessStatement>();
  
  private final JoinExprFormParser jefp = new JoinExprFormParser();
  
  private Map<Key, KeySummarizer.SummarizedInfo> summarizedKeys;
  private Map<Key, List<ForeignKey>> keysToForeignKeys;

  public void process(final XQNode root) {
    Map<String, ForClauseNode> forVariables = new HashMap<String, ForClauseNode>();
    processRecursive(root, forVariables);
  }
  
  public void summarize() {
    classifyJoinPatterns();
    classifiedJoinPatternsToKeys();
    summarizedKeys = summarizeKeys(keys, negativeUniquenessStatements);
    
    keysToForeignKeys = new HashMap<Key, List<ForeignKey>>();
    for (final WeightedForeignKey wfk : foreignKeys) {
      final ForeignKey fk = wfk.getKey();
      final Key k = fk.getKey();
      
      assert(summarizedKeys.containsKey(k));
      
      if (keysToForeignKeys.containsKey(k)) {
        keysToForeignKeys.get(k).add(fk);
      } else {
        final List<ForeignKey> fkList = new ArrayList<ForeignKey>();
        fkList.add(fk);
        keysToForeignKeys.put(k, fkList);
      }
    }
  }

  public Map<Key, KeySummarizer.SummarizedInfo> getKeys() {
    return summarizedKeys;
  }
  
  public Map<Key, List<ForeignKey>> getForeignKeys() {
    return keysToForeignKeys;
  }

  private void processRecursive(final XQNode node, Map<String, ForClauseNode> forVariables) {
    // Copy for variables to not affect the for variables in higher levels of recursion.
    Map<String, ForClauseNode> newForVariables = new HashMap<String, ForClauseNode>(forVariables);

    if (FLWORExprNode.class.isInstance(node)) {
      newForVariables = processFLWORExpr((FLWORExprNode) node, newForVariables);
    }

    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : node.getSubnodes()) {
        processRecursive(subnode, newForVariables);
      }
    }

    rejectionOfUniqueness_aggregationFunctions(node);
    rejectionOfUniqueness_comparisonWithAConstant(node);
  }

  public Map<String, ForClauseNode> processFLWORExpr(final FLWORExprNode flworExpr, Map<String, ForClauseNode> forVariables) {
    final List<VariableBindingNode> bindingNodes = flworExpr.getTupleStreamNode().getBindingClauses();
    final WhereClauseNode whereClauseNode = flworExpr.getWhereClauseNode();

    boolean checkJoinPattern3 = false;
    ExprNode whereExpr = null;

    if (whereClauseNode != null) {
      whereExpr = whereClauseNode.getExprNode();
      if (OperatorNode.class.isInstance(whereExpr)) {
        if (((OperatorNode) whereExpr).getOperator() == Operator.GEN_EQUALS) {
          checkJoinPattern3 = true;
        }
      }
    }

    for (final VariableBindingNode bindingNode : bindingNodes) {
      final ExprNode bindingExpr = bindingNode.getBindingSequenceNode().getExprNode();
      final Type bindingExprType = bindingExpr.getType();
      if (bindingExprType.getCategory() == Type.Category.PATH) {
        if (usesOnlyChildAndDescendantAxes((PathType) bindingExprType)) {
          final ExprNode predicate = endsWithExactlyOnePredicate((PathType) bindingExprType);
          if (predicate != null && isWithoutPredicatesExceptLastStep((PathType) bindingExprType)) {
            for (final Entry<String, ForClauseNode> entry : forVariables.entrySet()) {
              determineJoinPattern(bindingNode, predicate, entry.getValue(), entry.getKey(), forVariables, checkJoinPattern3, whereExpr);
            }
          } else if (isWithoutPredicates((PathType) bindingExprType)) {
            if (ForClauseNode.class.isInstance(bindingNode)) {
              forVariables.put(bindingNode.getVarName(), (ForClauseNode) bindingNode);
            }
          }
        }
      }
    }

    return forVariables;
  }

  private static boolean usesOnlyChildAndDescendantAxes(final PathType pathType) {
    for (final StepExprNode stepNode : pathType.getPathExprNode().getSteps()) {
      if (stepNode.isAxisStep()) {
        final AxisKind axisKind = stepNode.getAxisNode().getAxisKind();
        if (axisKind != AxisKind.CHILD && axisKind != AxisKind.DESCENDANT && axisKind != AxisKind.DESCENDANT_OR_SELF) {
          return false;
        }
      }

      final ExprNode detailNode = stepNode.getDetailNode();
      if (detailNode != null) {
        if (VarRefNode.class.isInstance(detailNode)) {
          if (usesOnlyChildAndDescendantAxes(pathType.getSubsteps().get(stepNode)) == false) {
            return false;
          }
        }
      }
    }

    return true;
  }

  // Vracia ten jediny predikat na konci.
  private static ExprNode endsWithExactlyOnePredicate(final PathType pathType) {
    final StepExprNode lastStepNode = pathType.getPathExprNode().getSteps().get(pathType.getPathExprNode().getSteps().size() - 1);
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
        //if (isExprJoinFormWhereClause(whereExpr, bindingNode.getVarName(), forVar, L1, L2)) {
        if (jefp.isExprJoinFormWhereClause(whereExpr, bindingNode.getVarName(), forVar)) {
          final PathExprNode P1 = (PathExprNode) forClauseNode.getBindingSequenceNode().getExprNode(); // TODO rio Su tieto dva riadky OK? Je exprNode vzdy naozaj PathExprNode?
          final PathExprNode P2 = (PathExprNode) bindingNode.getBindingSequenceNode().getExprNode();
          joinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.JP3, forClauseNode, bindingNode, P1, P2, jefp.getL1(), jefp.getL2()));
        }
      }
      return;
    }

    // Check for and let join patterns.
    //if (isExprJoinFormPredicate(predicate, forVar, L1, L2)) {
    if (jefp.isExprJoinFormPredicate(predicate, forVar)) {
      final PathExprNode P1 = (PathExprNode) forClauseNode.getBindingSequenceNode().getExprNode(); // TODO rio Su tieto dva riadky OK? Je exprNode vzdy naozaj PathExprNode?
      final PathExprNode P2WithPredicate = (PathExprNode) bindingNode.getBindingSequenceNode().getExprNode();
      final StepExprNode lastStep = P2WithPredicate.getSteps().get(P2WithPredicate.getSteps().size() - 1);
      StepExprNode newLastStep;

      if (lastStep.isAxisStep()) {
        newLastStep = new StepExprNode(lastStep.getAxisNode(), null);
      } else {
        newLastStep = new StepExprNode(lastStep.getDetailNode(), null);
      }

      final List<StepExprNode> newSteps = new ArrayList<StepExprNode>(P2WithPredicate.getSteps());
      newSteps.set(P2WithPredicate.getSteps().size() - 1, newLastStep);

      final PathExprNode P2 = new PathExprNode(newSteps, InitialStep.CONTEXT);
      if (ForClauseNode.class.isInstance(bindingNode)) {
        joinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.FOR, forClauseNode, bindingNode, P1, P2, jefp.getL1(), jefp.getL2()));
      } else if (LetClauseNode.class.isInstance(bindingNode)) {
        joinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.LET, forClauseNode, bindingNode, P1, P2, jefp.getL1(), jefp.getL2()));
      } else {
        assert (false);
      }
    }
  }

  private void classifyJoinPatterns() {
    for (final JoinPattern joinPattern : joinPatterns) {
      if (joinPattern.getType() == JoinPattern.JoinPatternType.FOR) {
        classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 100));
      } else if (joinPattern.getType() == JoinPattern.JoinPatternType.JP3) {
        classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 50));
      } else {
        // R2
        final FLWORExprNode flworNode = (FLWORExprNode) joinPattern.getSecondVariableBindingNode().getParentNode().getParentNode();
        List<PathType> returnPathTypes = getTargetReturnPathTypes(flworNode, joinPattern.getSecondVariableBindingNode().getVarName());
        boolean cont = true;
        for (final PathType pathType : returnPathTypes) {
          for (final String functionName : pathType.getSpecialFunctionCalls()) {
            if (functionName.equals("min")
                    || functionName.equals("max")
                    || functionName.equals("avg")
                    || functionName.equals("sum")) {
              classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 100));
              cont = false;
              break;
            }
          }
          if (!cont) {
            break;
          }
        }

        if (!cont) {
          continue;
        }

        // R3
        final List<PathExprNode> returnPaths = getTargetReturnPaths(flworNode, joinPattern.getSecondVariableBindingNode().getVarName());
        for (final PathExprNode path : returnPaths) {
          final XQNode parent = path.getParentNode();
          if (FunctionCallNode.class.isInstance(parent)) {
            if (((FunctionCallNode)parent).getFuncName().equals("count")) {
              classifiedJoinPatterns.add(new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 75));
              cont = false;
              break;
            }
          }
        }

        if (!cont) {
          continue;
        }

        // R4 R5
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
    final StepExprNode firstStep = pathType.getPathExprNode().getSteps().get(0);
    final ExprNode detailNode = firstStep.getDetailNode();

    if (detailNode == null) {
      return false;
    }

    if (!VarRefNode.class.isInstance(detailNode)) {
      return false;
    }

    if (((VarRefNode) detailNode).getVarName().equals(varName)) {
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
      final Type type = ((ExprNode) node).getType();
      if (type.getCategory() == Type.Category.PATH) {
        if (isTargetPath((PathType) type, varName)) {
          paths.add((PathType) type);
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
    getTargetReturnPathsRecursive(flworNode.getReturnClauseNode(), varName, paths);
    return paths;
  }

  private static void getTargetReturnPathsRecursive(final XQNode node, final String varName, final List<PathExprNode> paths) {
    if (PathExprNode.class.isInstance(node)) {
      final PathType type = TypeFactory.createPathType((PathExprNode) node);
      if (isTargetPath(type, varName)) {
        paths.add((PathExprNode) node);
      }
    } else if (VarRefNode.class.isInstance(node)) {
      final VarRefNode varRefNode = (VarRefNode)node;
      if (varRefNode.getVarName().equals(varName)) {
        // Toto je to kvoli tomu, aby vo FLWORoch bola premenna obsahujuca cestu sama cesta zacinajuce touto premennou, aby bolo mozne ju povazovat za target return path.
        assert(varRefNode.getType().getCategory() == Type.Category.PATH);
        final PathType pathType = (PathType)varRefNode.getType();
        if (isTargetPath(pathType, varName)) {
          final PathExprNode path = pathType.getPathExprNode();
          path.setParentNode(varRefNode.getParentNode());
          paths.add(path);
        }
      }
    }

    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : subnodes) {
        getTargetReturnPathsRecursive(subnode, varName, paths);
      }
    }
  }

  private static boolean isWithoutPredicates(final PathType pathType) {
    final Map<StepExprNode, PathType> subSteps = pathType.getSubsteps();
    for (final StepExprNode stepExprNode : pathType.getPathExprNode().getSteps()) {
      if (stepExprNode.hasPredicates()) {
        return false;
      }
      if (subSteps.containsKey(stepExprNode)) {
        if (!isWithoutPredicates(subSteps.get(stepExprNode))) {
          return false;
        }
      }
    }

    return true;
  }

  private static boolean isWithoutPredicatesExceptLastStep(final PathType pathType) {
    final Map<StepExprNode, PathType> subSteps = pathType.getSubsteps();
    final List<StepExprNode> stepNodes = pathType.getPathExprNode().getSteps();
    for (final StepExprNode stepExprNode : stepNodes) {
      if (stepExprNode == stepNodes.get(stepNodes.size() - 1)) {
        break;
      }

      if (stepExprNode.hasPredicates()) {
        return false;
      }
      if (subSteps.containsKey(stepExprNode)) {
        if (!isWithoutPredicates(subSteps.get(stepExprNode))) {
          return false;
        }
      }
    }

    return true;
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
        if (usesOnlyChildAndDescendantAxes(pathType) && isWithoutPredicatesExceptLastStep(pathType)) {
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
          if (usesOnlyChildAndDescendantAxes((PathType) exprNode.getType())) {
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
        if (usesOnlyChildAndDescendantAxes(pathType) && isWithoutPredicatesExceptLastStep(pathType)) {
          for (final String var : forVars) {
            final ExprNode detailNode = pathType.getPathExprNode().getSteps().get(0).getDetailNode();
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
        if (usesOnlyChildAndDescendantAxes(pathType) && isWithoutPredicatesExceptLastStep(pathType)) {
          for (final String var : forVars) {
            final ExprNode detailNode = pathType.getPathExprNode().getSteps().get(0).getDetailNode();
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

  private void classifiedJoinPatternsToKeys() {
    for (final ClassifiedJoinPattern cjp : classifiedJoinPatterns) {
      final JoinPattern jp = cjp.getJoinPattern();
      final ClassifiedJoinPattern.Type type = cjp.getType();
      final int weight = cjp.getWeight();
      
      PathType P1 = TypeFactory.createPathType(jp.getP1());
      PathType P2 = TypeFactory.createPathType(jp.getP2());
      PathType C = null;
      final ContextPathFinder cpf = new ContextPathFinder(P1, P2);
      if (cpf.haveCommonContext()) {
        C = cpf.getContextPath();
        P1 = cpf.getNewPath1();
        P2 = cpf.getNewPath2();
      }

      if (type == ClassifiedJoinPattern.Type.O1) {
        final Key key = new Key(C, P1, TypeFactory.createPathType(jp.getL1()));
        final Key notKey = new NegativeKey(C, P2, TypeFactory.createPathType(jp.getL2()));
        final ForeignKey foreignKey = new ForeignKey(key, P2, TypeFactory.createPathType(jp.getL2()));

        final WeightedKey wKey = new WeightedKey(key, weight);
        final WeightedKey wNotKey = new WeightedKey(notKey, weight);
        final WeightedForeignKey wForeignKey = new WeightedForeignKey(foreignKey, weight);

        keys.add(wKey);
        keys.add(wNotKey);
        foreignKeys.add(wForeignKey);
      } else {
        final Key key = new Key(C, P2, TypeFactory.createPathType(jp.getL2()));
        final ForeignKey foreignKey = new ForeignKey(key, P1, TypeFactory.createPathType(jp.getL1()));

        final WeightedKey wKey = new WeightedKey(key, weight);
        final WeightedForeignKey wForeignKey = new WeightedForeignKey(foreignKey, weight);

        keys.add(wKey);
        foreignKeys.add(wForeignKey);
      }
    }
  }
  
  private static Map<Key, KeySummarizer.SummarizedInfo> summarizeKeys(List<WeightedKey> wKeys, List<NegativeUniquenessStatement> nuss) {
    final KeySummarizer ks = new KeySummarizer();
    for (final WeightedKey wk : wKeys) {
      ks.summarize(wk, nuss);
    }
    return ks.getSummarizedKeys();
  }
}
