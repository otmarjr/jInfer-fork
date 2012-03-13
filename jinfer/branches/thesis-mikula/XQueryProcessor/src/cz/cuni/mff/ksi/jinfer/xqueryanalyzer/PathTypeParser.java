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

import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.InitialStep;
import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.xqanalyser.nodes.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.xqueryprocessor.types.PathType;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author rio
 */
public final class PathTypeParser {
  
  private final Stack<Integer> stepsIndices = new Stack<Integer>(); // Zasobnik indexov, kde sa prave nachadzame v kazdom liste v zasobniku listov krokov. Hodnota udava ktory je dalsi v poradi (0 -> ideme prave na nulu).
  private final Stack<List<StepExprNode>> stepsStack = new Stack<List<StepExprNode>>();
  private final Stack<PathType> pathTypesStack = new Stack<PathType>();
  private StepExprNode actualStep;
  private InitialStep initialStep;
  private boolean isFirstStep = true;
  private boolean hasPredicates = false;
  private List<StepExprNode> steps = new ArrayList<StepExprNode>();
  
  public PathTypeParser(final PathType pathType) {
    stepsIndices.push(0);
    stepsStack.push(pathType.getPathExprNode().getSteps());
    pathTypesStack.push(pathType);
    
    while(goNextStep()) {
      steps.add(actualStep);
    }
  }
   
  private boolean goNextStep() {
    int index = stepsIndices.pop();
    final List<StepExprNode> actualSteps = stepsStack.peek();
    final PathType actualPathType = pathTypesStack.peek();
    
    if (actualSteps.size() > index) {
      actualStep = actualSteps.get(index);
      ++index;
      stepsIndices.push(index);
      
      if (actualStep.hasPredicates()) {
        hasPredicates = true;
      }
      
      final ExprNode detailNode = actualStep.getDetailNode();
      if (VarRefNode.class.isInstance(detailNode)) {
        final PathType referredPathType = actualPathType.getSubsteps().get(actualStep);
        stepsIndices.push(0);
        stepsStack.push(referredPathType.getPathExprNode().getSteps());
        pathTypesStack.push(referredPathType);
        return goNextStep();
      }
      
      if (isFirstStep) {
        initialStep = actualPathType.getPathExprNode().getInitialStep();
        isFirstStep = false;
      }
      
      return true;
    } else {
      // Ak je index vacsi ako aktualny list krokov...
      // Ak toto je najvyssia uroven stacku (stack indexov je prazdny), znamena to, ze koncime.
      if (stepsIndices.empty()) {
        return false;
      }
      
      // Inak vyhodime aktualny list krokov, ktory sme uz presli a zavolame rekurzivne.
      stepsStack.pop();
      pathTypesStack.pop();
      return goNextStep();
    }
  }
  
  // Platne az po prvom volani goNextStep.
  public InitialStep getInitialStep() {
    return initialStep;
  }

  public boolean isHasPredicates() {
    return hasPredicates;
  }

  public List<StepExprNode> getSteps() {
    return steps;
  }
  
}
