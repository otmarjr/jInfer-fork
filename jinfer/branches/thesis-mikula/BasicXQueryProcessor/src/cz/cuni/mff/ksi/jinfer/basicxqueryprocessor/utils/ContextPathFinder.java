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

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An utility class to determine a common context of two specified paths.
 * The common context of paths P1 and P2 is found as follows:
 *  - If at least one of P1 and P2 is an absolute path, they do not have a common
 *    context.
 *  - Else, if P1 and P2 starts with the same variable, a path referenced by variable
 *    is their common context.
 *  - Else, found P1' and P2' paths referenced by the starting variables and process
 *    recursively.
 * 
 * @see PathType
 * @author rio
 */
public final class ContextPathFinder {
  
  private final PathType origPath1;
  private final PathType origPath2;
  private PathType contextPath;
  private PathType newPath1;
  private PathType newPath2;
 
  public ContextPathFinder(final PathType path1, final PathType path2) {
    origPath1 = path1;
    origPath2 = path2;
    process();
  }
  
  public boolean haveCommonContext() {
    return contextPath != null;
  }

  /**
   * @return The common context path, if it was found.
   */
  public PathType getContextPath() {
    return contextPath;
  }

  /**
   * @return path1 stripped of the common context.
   */
  public PathType getNewPath1() {
    return newPath1;
  }

  /**
   * @return path2 stripped of the common context.
   */
  public PathType getNewPath2() {
    return newPath2;
  }
  
  private void process() {
    final StepExprNode firstStep1 = origPath1.getSteps().get(0);
    final StepExprNode firstStep2 = origPath2.getSteps().get(0);
    final ExprNode detailNode1 = firstStep1.getDetailNode();
    final ExprNode detailNode2 = firstStep2.getDetailNode();
    
    if (detailNode1 == null || detailNode2 == null) {
      contextPath = null;
      return;
    }
    
    if (!VarRefNode.class.isInstance(detailNode1) || !VarRefNode.class.isInstance(detailNode2)) {
      contextPath = null;
      return;
    }
    
    final String varName1 = ((VarRefNode)detailNode1).getVarName();
    final String varName2 = ((VarRefNode)detailNode2).getVarName();
    
    if (varName1.equals(varName2)) {
      contextPath = origPath1.getSubpaths().get(firstStep1);
      
      final Map<StepExprNode, PathType> substeps1 = new HashMap(origPath1.getSubpaths());
      substeps1.remove(firstStep1);
      newPath1 = new PathType(origPath1.getSteps().subList(1, origPath1.getSteps().size()), InitialStep.CONTEXT, substeps1, false);
      
      final Map<StepExprNode, PathType> substeps2 = new HashMap(origPath2.getSubpaths());
      substeps2.remove(firstStep2);
      newPath2 = new PathType(origPath2.getSteps().subList(1, origPath2.getSteps().size()), InitialStep.CONTEXT, substeps2, false);
      
      return;
    }
    
    final ContextPathFinder cpf = new ContextPathFinder(origPath1.getSubpaths().get(firstStep1), origPath2.getSubpaths().get(firstStep2));
    if (cpf.haveCommonContext()) {
      contextPath = cpf.getContextPath();
      
      final List<StepExprNode> steps1 = cpf.getNewPath1().getSteps();
      steps1.addAll(origPath1.getSteps().subList(1 , origPath1.getSteps().size()));
      final Map<StepExprNode, PathType> substeps1 = cpf.getNewPath1().getSubpaths();
      substeps1.putAll(origPath1.getSubpaths());
      substeps1.remove(firstStep1);
      newPath1 = new PathType(steps1, InitialStep.CONTEXT, substeps1, false);
      
      final List<StepExprNode> steps2 = cpf.getNewPath2().getSteps();
      steps2.addAll(origPath2.getSteps().subList(1 , origPath2.getSteps().size()));
      final Map<StepExprNode, PathType> substeps2 = cpf.getNewPath2().getSubpaths();
      substeps2.putAll(origPath2.getSubpaths());
      substeps2.remove(firstStep2);
      newPath1 = new PathType(steps2, InitialStep.CONTEXT, substeps2, false);
    }
  }
}
