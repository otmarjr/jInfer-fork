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
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.FLWORExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ForClauseNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.LetClauseNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Operator;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.OperatorNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VariableBindingNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.WhereClauseNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.XQNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.utils.PathTypeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class searches a syntax tree for join patterns occurrences.
 * 
 * @see JoinPattern
 * 
 * @author rio
 */
public class JoinPatternsFinder {
  
  private final ModuleNode root;
  private final List<JoinPattern> joinPatterns;
  
  /**
   * @param root A syntax tree.
   */
  public JoinPatternsFinder(final ModuleNode root) {
    this.root = root;
    joinPatterns = new ArrayList<JoinPattern>();
  }
  
  /**
   * Performs the search for join patterns occurrences.
   */
  public void process() {
    Map<String, ForClauseNode> forVariables = new HashMap<String, ForClauseNode>();
    processRecursive(root, forVariables, joinPatterns);
  }
  
  /**
   * Retrieves the found join patterns occurrences by {@link #process()} method.
   */
  public List<JoinPattern> getJoinPatterns() {
    return joinPatterns;
  }
  
  private static void processRecursive(final XQNode node, final Map<String, ForClauseNode> forVariables, final List<JoinPattern> foundJoinPatterns) {
    // Copy for variables to not affect the for variables in higher levels of recursion.
    Map<String, ForClauseNode> newForVariables = new HashMap<String, ForClauseNode>(forVariables);

    if (FLWORExprNode.class.isInstance(node)) {
      newForVariables = processFLWORExpr((FLWORExprNode) node, newForVariables, foundJoinPatterns);
    }

    final List<XQNode> subnodes = node.getSubnodes();
    if (subnodes != null) {
      for (final XQNode subnode : node.getSubnodes()) {
        processRecursive(subnode, newForVariables, foundJoinPatterns);
      }
    }
  }
  
  private static Map<String, ForClauseNode> processFLWORExpr(final FLWORExprNode flworExpr, final Map<String, ForClauseNode> forVariables, final List<JoinPattern> foundJoinPatterns) {
    final List<VariableBindingNode> bindingNodes = flworExpr.getTupleStreamNode().getBindingClauses();
    final WhereClauseNode whereClauseNode = flworExpr.getWhereClauseNode();

    boolean checkJoinPattern3 = false;
    ExprNode whereExpr = null;

    if (whereClauseNode != null) {
      whereExpr = whereClauseNode.getExprNode();
      if (OperatorNode.class.isInstance(whereExpr)
              && ((OperatorNode) whereExpr).getOperator() == Operator.GEN_EQUALS) {
        checkJoinPattern3 = true;
      }
    }

    for (final VariableBindingNode bindingNode : bindingNodes) {
      final ExprNode bindingExpr = bindingNode.getBindingSequenceNode().getExprNode();
      final Type bindingExprType = bindingExpr.getType();
      if (bindingExprType.getCategory() == Type.Category.PATH
              && PathTypeUtils.usesOnlyChildAndDescendantAxes((PathType) bindingExprType)) {
        final ExprNode predicate = PathTypeUtils.endsWithExactlyOnePredicate((PathType) bindingExprType);
        if (predicate != null && PathTypeUtils.isWithoutPredicatesExceptLastStep((PathType) bindingExprType)) {
          for (final Entry<String, ForClauseNode> entry : forVariables.entrySet()) {
            determineJoinPattern(bindingNode, predicate, entry.getValue(), entry.getKey(), checkJoinPattern3, whereExpr, foundJoinPatterns);
          }
        } else if (checkJoinPattern3 && PathTypeUtils.isWithoutPredicates((PathType) bindingExprType)) {
          for (final Entry<String, ForClauseNode> entry : forVariables.entrySet()) {
            determineJoinPattern(bindingNode, predicate, entry.getValue(), entry.getKey(), checkJoinPattern3, whereExpr, foundJoinPatterns);
          }
        }

        if (PathTypeUtils.isWithoutPredicates((PathType) bindingExprType)
                && ForClauseNode.class.isInstance(bindingNode)) {
          forVariables.put(bindingNode.getVarName(), (ForClauseNode) bindingNode);
        }
      }
    }

    return forVariables;
  }

  private static void determineJoinPattern(final VariableBindingNode bindingNode, final ExprNode predicate, final ForClauseNode forClauseNode, final String forVar, final boolean checkJoinPattern3, final ExprNode whereExpr, final List<JoinPattern> foundJoinPatterns) {
    // At first, check join pattern 3.
    if (checkJoinPattern3) {
      if (ForClauseNode.class.isInstance((bindingNode))) {
        //if (isWhereClauseForm(whereExpr, bindingNode.getVarName(), forVar, L1, L2)) {
        final JoinWhereFormExprParser exprParser = new JoinWhereFormExprParser(whereExpr, forVar, bindingNode.getVarName());
        if (exprParser.isExprWhereForm()) {
          final PathType P1 = (PathType) forClauseNode.getBindingSequenceNode().getExprNode().getType(); // TODO rio I'm not sure is this is correct - if these expressions are always path types.
          final PathType P2 = (PathType) bindingNode.getBindingSequenceNode().getExprNode().getType();
          final PathType L1 = exprParser.getL1();
          final PathType L2 = exprParser.getL2();
          if (PathTypeUtils.usesOnlyChildAndDescendantAndAttributeAxes(L1)
                  && PathTypeUtils.usesOnlyChildAndDescendantAndAttributeAxes(L2)) {
            foundJoinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.JP3, forClauseNode, bindingNode, P1, P2, L1, L2));
          }
        }
      }
    }

    // Check for and let join patterns.
    final JoinPredicateFormExprParser exprParser = new JoinPredicateFormExprParser(predicate, forVar);
    if (exprParser.isExprPredicateForm()) {
      final PathType P1 = (PathType) forClauseNode.getBindingSequenceNode().getExprNode().getType(); // TODO rio I'm not sure is this is correct - if these expressions are always path types.
      final PathType P2WithPredicate = (PathType) bindingNode.getBindingSequenceNode().getExprNode().getType();
      final StepExprNode lastStep = P2WithPredicate.getSteps().get(P2WithPredicate.getSteps().size() - 1);
      StepExprNode newLastStep;

      if (lastStep.isAxisStep()) {
        newLastStep = new StepExprNode(lastStep.getAxisNode(), null);
      } else {
        newLastStep = new StepExprNode(lastStep.getDetailNode(), null);
      }

      final List<StepExprNode> newSteps = new ArrayList<StepExprNode>(P2WithPredicate.getSteps());
      newSteps.set(P2WithPredicate.getSteps().size() - 1, newLastStep);

      final PathType P2 = new PathType(new PathExprNode(newSteps, P2WithPredicate.getInitialStep()));
      final PathType L1 = exprParser.getL1();
      final PathType L2 = exprParser.getL2();
          
      if (PathTypeUtils.usesOnlyChildAndDescendantAndAttributeAxes(L1)
                  && PathTypeUtils.usesOnlyChildAndDescendantAndAttributeAxes(L2)) {
        if (ForClauseNode.class.isInstance(bindingNode)) {
          foundJoinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.FOR, forClauseNode, bindingNode, P1, P2, exprParser.getL1(), exprParser.getL2()));
        } else if (LetClauseNode.class.isInstance(bindingNode)) {
          foundJoinPatterns.add(new JoinPattern(JoinPattern.JoinPatternType.LET, forClauseNode, bindingNode, P1, P2, exprParser.getL1(), exprParser.getL2()));
        } else {
          assert (false);
        }
      }
    }
  }
  
}
