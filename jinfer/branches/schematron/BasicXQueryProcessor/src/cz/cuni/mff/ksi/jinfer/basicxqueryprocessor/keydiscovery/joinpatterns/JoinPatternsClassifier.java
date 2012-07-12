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
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.FunctionCallNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.PathExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.XQNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides classification of join patterns occurrences using the
 * rules R1-R5. For details, see PDF documentation.
 * 
 * @author rio
 */
public class JoinPatternsClassifier {
  
  /**
   * Classifies the specified list of join pattern occurrences.
   */
  public static List<ClassifiedJoinPattern> classify(final List<JoinPattern> joinPatterns) {
    final List<ClassifiedJoinPattern> classifiedJoinPatterns = new ArrayList<ClassifiedJoinPattern>();
    for (final JoinPattern joinPattern : joinPatterns) {
      classifiedJoinPatterns.add(classifyJoinPattern(joinPattern));
    }
    return classifiedJoinPatterns;
  }

  private static ClassifiedJoinPattern classifyJoinPattern(final JoinPattern joinPattern) {
    if (joinPattern.getType() == JoinPattern.JoinPatternType.FOR) {
      return new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 100);
    } else if (joinPattern.getType() == JoinPattern.JoinPatternType.JP3) {
      return new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 50);
    } else {
      // R2
      final FLWORExprNode flworNode = (FLWORExprNode) joinPattern.getSecondVariableBindingNode().getParentNode().getParentNode();
      List<PathType> returnPathTypes = getTargetReturnPathTypes(flworNode, joinPattern.getSecondVariableBindingNode().getVarName());
      for (final PathType pathType : returnPathTypes) {
        for (final String functionName : pathType.getSpecialFunctionCalls()) {
          if (functionName.equals("min")
                  || functionName.equals("max")
                  || functionName.equals("avg")
                  || functionName.equals("sum")) {
            return new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 100);
          }
        }
      }

      // R3
      final List<PathExprNode> returnPaths = getTargetReturnPaths(flworNode, joinPattern.getSecondVariableBindingNode().getVarName());
      for (final PathExprNode path : returnPaths) {
        final XQNode parent = path.getParentNode();
        if (FunctionCallNode.class.isInstance(parent)
                && ((FunctionCallNode) parent).getFuncName().equals("count")) {
          return new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O1, 75);
        }
      }

      // R4 R5
      final int returnPathsNumber = returnPaths.size();
      if (returnPathsNumber > 1) {
        return new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O2, 100);
      } else {
        return new ClassifiedJoinPattern(joinPattern, ClassifiedJoinPattern.Type.O2, 50);
      }
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
      if (type.getCategory() == Type.Category.PATH && isTargetPath((PathType) type, varName)) {
        paths.add((PathType) type);
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
      final PathType type = new PathType((PathExprNode) node);
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
          final PathExprNode path = new PathExprNode(pathType.getSteps(), pathType.getInitialStep());
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
  
  private static boolean isTargetPath(final PathType pathType, final String varName) {
    final StepExprNode firstStep = pathType.getSteps().get(0);
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
}
