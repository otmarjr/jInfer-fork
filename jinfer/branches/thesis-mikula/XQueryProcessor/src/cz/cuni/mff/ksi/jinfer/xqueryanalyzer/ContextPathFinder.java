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

import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.*;
import cz.cuni.mff.ksi.jinfer.base.xqueryprocessor.types.PathType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
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

  public PathType getContextPath() {
    return contextPath;
  }

  public PathType getNewPath1() {
    return newPath1;
  }

  public PathType getNewPath2() {
    return newPath2;
  }
  
  private void process() {
    final StepExprNode firstStep1 = origPath1.getPathExprNode().getSteps().get(0);
    final StepExprNode firstStep2 = origPath2.getPathExprNode().getSteps().get(0);
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
      contextPath = origPath1.getSubsteps().get(firstStep1);
      
      final PathExprNode pathExprNode1 = new PathExprNode(origPath1.getPathExprNode().getSteps().subList(1, origPath1.getPathExprNode().getSteps().size()), InitialStep.CONTEXT);
      final Map<StepExprNode, PathType> substeps1 = new HashMap(origPath1.getSubsteps());
      substeps1.remove(firstStep1);
      newPath1 = new PathType(pathExprNode1, substeps1, false);
      
      final PathExprNode pathExprNode2 = new PathExprNode(origPath2.getPathExprNode().getSteps().subList(1, origPath2.getPathExprNode().getSteps().size()), InitialStep.CONTEXT);
      final Map<StepExprNode, PathType> substeps2 = new HashMap(origPath2.getSubsteps());
      substeps2.remove(firstStep2);
      newPath2 = new PathType(pathExprNode2, substeps2, false);
      
      return;
    }
    
    final ContextPathFinder cpf = new ContextPathFinder(origPath1.getSubsteps().get(firstStep1), origPath2.getSubsteps().get(firstStep2));
    if (cpf.haveCommonContext()) {
      contextPath = cpf.getContextPath();
      
      final List<StepExprNode> steps1 = cpf.getNewPath1().getPathExprNode().getSteps();
      steps1.addAll(origPath1.getPathExprNode().getSteps().subList(1 , origPath1.getPathExprNode().getSteps().size()));
      final Map<StepExprNode, PathType> substeps1 = cpf.getNewPath1().getSubsteps();
      substeps1.putAll(origPath1.getSubsteps());
      substeps1.remove(firstStep1);
      final PathExprNode pathExprNode1 = new PathExprNode(steps1, InitialStep.CONTEXT);
      newPath1 = new PathType(pathExprNode1, substeps1, false);
      
      final List<StepExprNode> steps2 = cpf.getNewPath2().getPathExprNode().getSteps();
      steps2.addAll(origPath2.getPathExprNode().getSteps().subList(1 , origPath2.getPathExprNode().getSteps().size()));
      final Map<StepExprNode, PathType> substeps2 = cpf.getNewPath2().getSubsteps();
      substeps2.putAll(origPath2.getSubsteps());
      substeps2.remove(firstStep2);
      final PathExprNode pathExprNode2 = new PathExprNode(steps2, InitialStep.CONTEXT);
      newPath1 = new PathType(pathExprNode2, substeps2, false);
    }
  }
}
