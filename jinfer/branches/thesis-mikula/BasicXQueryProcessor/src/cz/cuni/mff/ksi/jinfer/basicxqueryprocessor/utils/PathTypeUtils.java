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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.AxisKind;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.InitialStep;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NormalizedPathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An utility class providing PathType functions.
 * @author rio
 */
public class PathTypeUtils {
  
  /**
   * Determined if a path is without predicates with an exception of its last step.
   */
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
      if (subSteps.containsKey(stepExprNode) && !isWithoutPredicates(subSteps.get(stepExprNode))) {
        return false;
      }
    }

    return true;
  }
  
  /**
   * Determined if a path is completely without predicates.
   */
  public static boolean isWithoutPredicates(final PathType pathType) {
    final Map<StepExprNode, PathType> subSteps = pathType.getSubpaths();
    for (final StepExprNode stepExprNode : pathType.getSteps()) {
      if (stepExprNode.hasPredicates()) {
        return false;
      }
      if (subSteps.containsKey(stepExprNode) && !isWithoutPredicates(subSteps.get(stepExprNode))) {
        return false;
      }
    }

    return true;
  }
  
  /**
   * Determines if a path uses only axis child, descendant, and descendant-or-self.
   */
  public static boolean usesOnlyChildAndDescendantAxes(final PathType pathType) {
    for (final StepExprNode stepNode : pathType.getSteps()) {
      if (stepNode.isAxisStep()) {
        final AxisKind axisKind = stepNode.getAxisNode().getAxisKind();
        if (axisKind != AxisKind.CHILD && axisKind != AxisKind.DESCENDANT && axisKind != AxisKind.DESCENDANT_OR_SELF) {
          return false;
        }
      }

      final ExprNode detailNode = stepNode.getDetailNode();
      if (detailNode != null && VarRefNode.class.isInstance(detailNode)
                && usesOnlyChildAndDescendantAxes(pathType.getSubpaths().get(stepNode)) == false) {
        return false;
      }
    }

    return true;
  }
  
  /**
   * Determines if a path uses only axis child, descendant, descendant-or-self, and attribute.
   */
  public static boolean usesOnlyChildAndDescendantAndAttributeAxes(final PathType pathType) { // TODO do DP!!
    for (final StepExprNode stepNode : pathType.getSteps()) {
      if (stepNode.isAxisStep()) {
        final AxisKind axisKind = stepNode.getAxisNode().getAxisKind();
        if (axisKind != AxisKind.CHILD && axisKind != AxisKind.DESCENDANT && axisKind != AxisKind.DESCENDANT_OR_SELF && axisKind != AxisKind.ATTRIBUTE) {
          return false;
        }
      }

      final ExprNode detailNode = stepNode.getDetailNode();
      if (detailNode != null && VarRefNode.class.isInstance(detailNode)
                && usesOnlyChildAndDescendantAndAttributeAxes(pathType.getSubpaths().get(stepNode)) == false) {
        return false;
      }
    }

    return true;
  }

  /**
   * If a path has just one predicate and it is in its last step, returns this
   * predicate. Else returns null.
   * @param pathType A path.
   * @return The only predicate from the last step or null, if it does not exist.
   */
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
  
  public static NormalizedPathType join(final NormalizedPathType path1, final NormalizedPathType path2) { // TODO rio do DP!!
    assert(path2.getInitialStep() == InitialStep.CONTEXT);
    
    final List<StepExprNode> steps = new ArrayList<StepExprNode>(path1.getSteps());
    steps.addAll(path2.getSteps());
    
    final PathType pathType = new PathType(steps, path1.getInitialStep(), null, false); // TODO rio is for bound? probably from copy from path1.
    
    return new NormalizedPathType(pathType);
  }
}
