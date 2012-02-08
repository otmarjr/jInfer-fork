/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This code originates from Jiří Schejbal's master thesis. Jiří Schejbal
 * is also the author of the original version of this code.
 * With his approval, we use his code in jInfer and we slightly modify it to
 * suit our cause.
 */
package cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser;

/**
 * This class represents the path expression.
 *
 * @author Jiri Schejbal
 */
public class PathExpr {

  private InitialStep pathStartNode;
  private XQNodeList<StepExprNode> steps;
  //private XQDocument xqDocument;
  private InitialStep initialStep;

  public PathExpr(Object xqDocument) { // TODO rio XQDocument nahradeny Objectom...
    //this.xqDocument = xqDocument;
    steps = new XQNodeList<StepExprNode>();
  }

  public PathExpr(Object xqDocument, StepExprNode firstStep) {
    this(xqDocument);
    add(firstStep);
  }

  public void insertRootDescendantStep() {
    steps.add(0, new SelfOrDescendantStepNode(null));
  }

  public ExprNode getNode() {
    if (steps.size() == 1 && initialStep == pathStartNode.CONTEXT) {
      StepExprNode step = steps.get(0);
      if (!step.hasPredicates() && !step.isAxisStep()) {
        return step.getDetailNode();
      } else {
        return new PathExprNode(null, steps, initialStep);
      }
    } else {
      return new PathExprNode(null, steps, initialStep);
    }
  }

  public void setInitialStep(InitialStep pathStartNode) {
    initialStep = pathStartNode;
  }

  public void add(StepExprNode stepExprNode) {
    if (stepExprNode != null) {
      steps.add(stepExprNode);
    }
  }
}
