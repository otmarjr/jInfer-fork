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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.ExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.InitialStep;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.VarRefNode;
import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.PathType;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author rio
 */
public class PathTypeParser {
  
  private final Stack<Integer> stepsIndices = new Stack<Integer>(); // Zasobnik indexov, kde sa prave nachadzame v kazdom liste v zasobniku listov krokov. Hodnota udava ktory je dalsi v poradi (0 -> ideme prave na nulu).
  private final Stack<List<StepExprNode>> stepsStack = new Stack<List<StepExprNode>>();
  private final Stack<PathType> pathTypesStack = new Stack<PathType>();
  private StepExprNode actualStep;
  private InitialStep initialStep;
  private boolean isFirstStep = true;
  
  public PathTypeParser(final PathType pathType) {
    stepsIndices.push(0);
    stepsStack.push(pathType.getPathExprNode().getSteps());
    pathTypesStack.push(pathType);
  }
   
  public boolean goNextStep() {
    int index = stepsIndices.pop();
    final List<StepExprNode> steps = stepsStack.peek();
    final PathType actualPathType = pathTypesStack.peek();
    
    if (steps.size() > index) {
      actualStep = steps.get(index);
      ++index;
      stepsIndices.push(index);
      
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
  
  public StepExprNode getActualStep() {
    return actualStep;
  }
  
  // Platne az po prvom volani goNextStep.
  public InitialStep getInitialStep() {
    return initialStep;
  }
}
