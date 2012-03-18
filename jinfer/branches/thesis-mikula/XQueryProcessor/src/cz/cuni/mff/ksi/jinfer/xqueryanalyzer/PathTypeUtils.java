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

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.AxisKind;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rio
 */
public class PathTypeUtils {
  
  public static boolean isWithoutPredicatesExceptLastStep(final PathType pathType) {
    final Map<StepExprNode, PathType> subSteps = pathType.getSubpaths();
    final List<StepExprNode> stepNodes = pathType.getSteps();
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
  
  public static boolean isWithoutPredicates(final PathType pathType) {
    final Map<StepExprNode, PathType> subSteps = pathType.getSubpaths();
    for (final StepExprNode stepExprNode : pathType.getSteps()) {
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
  
  public static boolean usesOnlyChildAndDescendantAxes(final PathType pathType) {
    for (final StepExprNode stepNode : pathType.getSteps()) {
      if (stepNode.isAxisStep()) {
        final AxisKind axisKind = stepNode.getAxisNode().getAxisKind();
        if (axisKind != AxisKind.CHILD && axisKind != AxisKind.DESCENDANT && axisKind != AxisKind.DESCENDANT_OR_SELF) {
          return false;
        }
      }

      final ExprNode detailNode = stepNode.getDetailNode();
      if (detailNode != null) {
        if (VarRefNode.class.isInstance(detailNode)) {
          if (usesOnlyChildAndDescendantAxes(pathType.getSubpaths().get(stepNode)) == false) {
            return false;
          }
        }
      }
    }

    return true;
  }

  // Vracia ten jediny predikat na konci.
  public static ExprNode endsWithExactlyOnePredicate(final PathType pathType) {
    final StepExprNode lastStepNode = pathType.getSteps().get(pathType.getSteps().size() - 1);
    if (!lastStepNode.hasPredicates()) {
      return null;
    }

    if (lastStepNode.getPredicateListNode().getPredicates().size() != 1) {
      return null;
    }

    return lastStepNode.getPredicateListNode().getPredicates().get(0);
  }
}
