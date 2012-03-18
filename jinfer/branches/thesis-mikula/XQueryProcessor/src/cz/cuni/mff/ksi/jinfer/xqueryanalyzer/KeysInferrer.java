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

import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.BuiltinFunctions;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.joinpatterns.ClassifiedJoinPattern;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils.ContextPathFinder;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.joinpatterns.JoinPattern;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.KeySummarizer;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.NegativeUniquenessStatement;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.WeightedForeignKey;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.WeightedKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rio
 */
public class KeysInferrer {

  private final List<JoinPattern> joinPatterns = new ArrayList<JoinPattern>();
  private final List<ClassifiedJoinPattern> classifiedJoinPatterns = new ArrayList<ClassifiedJoinPattern>();
  private final List<WeightedKey> keys = new ArrayList<WeightedKey>();
  private final List<WeightedForeignKey> foreignKeys = new ArrayList<WeightedForeignKey>();
  private final List<NegativeUniquenessStatement> negativeUniquenessStatements = new ArrayList<NegativeUniquenessStatement>();
  
  private Map<Key, KeySummarizer.SummarizedInfo> summarizedKeys;
  private Map<Key, Set<ForeignKey>> keysToForeignKeys;
  
  

  public void process(final ModuleNode root) {
    processRecursive(root);
    final JoinPatternsFinder joinPatternsFinder = new JoinPatternsFinder(root);
    joinPatternsFinder.process();
    joinPatterns.addAll(joinPatternsFinder.getJoinPatterns());
  }
  
  public void summarize() {
    classifiedJoinPatterns.addAll(JoinPatternsClassifier.classify(joinPatterns));
    classifiedJoinPatternsToKeys();
    summarizedKeys = summarizeKeys(keys, negativeUniquenessStatements);
    
    keysToForeignKeys = new HashMap<Key, Set<ForeignKey>>();
    for (final WeightedForeignKey wfk : foreignKeys) {
      final ForeignKey fk = wfk.getKey();
      final Key k = fk.getKey();
      
      assert(summarizedKeys.containsKey(k));
      
      if (keysToForeignKeys.containsKey(k)) {
        keysToForeignKeys.get(k).add(fk);
      } else {
        final Set<ForeignKey> fkList = new LinkedHashSet<ForeignKey>();
        fkList.add(fk);
        keysToForeignKeys.put(k, fkList);
      }
    }
  }

  public Map<Key, KeySummarizer.SummarizedInfo> getKeys() {
    return summarizedKeys;
  }
  
  public Map<Key, Set<ForeignKey>> getForeignKeys() {
    return keysToForeignKeys;
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

  private void classifiedJoinPatternsToKeys() {
    for (final ClassifiedJoinPattern cjp : classifiedJoinPatterns) {
      final JoinPattern jp = cjp.getJoinPattern();
      final ClassifiedJoinPattern.Type type = cjp.getType();
      final int weight = cjp.getWeight();
      
      PathType P1 = jp.getP1();
      PathType P2 = jp.getP2();
      PathType C = null;
      final ContextPathFinder cpf = new ContextPathFinder(P1, P2);
      if (cpf.haveCommonContext()) {
        C = cpf.getContextPath();
        P1 = cpf.getNewPath1();
        P2 = cpf.getNewPath2();
      }

      if (type == ClassifiedJoinPattern.Type.O1) {
        final Key key = new Key(C, P1, new PathType(jp.getL1()));
        final Key notKey = new Key(C, P2, new PathType(jp.getL2()));
        final ForeignKey foreignKey = new ForeignKey(key, P2, new PathType(jp.getL2()));

        final WeightedKey wKey = new WeightedKey(key, weight);
        final WeightedKey wNotKey = new WeightedKey(notKey, weight * (-1));
        final WeightedForeignKey wForeignKey = new WeightedForeignKey(foreignKey, weight);

        keys.add(wKey);
        keys.add(wNotKey);
        foreignKeys.add(wForeignKey);
      } else {
        final Key key = new Key(C, P2, new PathType(jp.getL2()));
        final ForeignKey foreignKey = new ForeignKey(key, P1, new PathType(jp.getL1()));

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
