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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * The node representing a step in path expressions.
 *
 * @author Jiri Schejbal
 */
public class StepExprNode extends XQNode {

  private boolean hasPredicates;
  private boolean isAxisStep;
  private ExprNode detailNode = null;
  private PredicateListNode predicateListNode;
  private AxisNode axisNode;

  private void addPredicates(PredicateListNode predicateListNode) {
    this.predicateListNode = predicateListNode;
    hasPredicates = predicateListNode != null;
  }

  public StepExprNode(ExprNode exprNode,
          PredicateListNode predicateListNode) {
    assert (exprNode != null);
    detailNode = exprNode;
    isAxisStep = false;
    addPredicates(predicateListNode);
  }

  public StepExprNode(AxisNode axisNode,
          PredicateListNode predicateListNode) {
    assert (axisNode != null);
    this.axisNode = axisNode;
    isAxisStep = true;
    addPredicates(predicateListNode);
  }

  public boolean hasPredicates() {
    return hasPredicates;
  }

  public boolean isAxisStep() {
    return isAxisStep;
  }

  public ExprNode getDetailNode() {
    return detailNode;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_STEP_EXPR;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    final List<XQNode> subnodes = new ArrayList<XQNode>();
    
    if (axisNode != null) {
      subnodes.add(axisNode);
    }
    if (detailNode != null) {
      subnodes.add(detailNode);
    }
    if (predicateListNode != null) {
      subnodes.add(predicateListNode);
    }
    
    return subnodes;
  }
  
  public AxisNode getAxisNode() {
    return axisNode;
  }
  
  public PredicateListNode getPredicateListNode() {
    return predicateListNode;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StepExprNode other = (StepExprNode) obj;
    if (this.hasPredicates != other.hasPredicates) {
      return false;
    }
    if (this.isAxisStep != other.isAxisStep) {
      return false;
    }
    if (this.detailNode != other.detailNode && (this.detailNode == null || !this.detailNode.equals(other.detailNode))) {
      return false;
    }
    if (this.predicateListNode != other.predicateListNode && (this.predicateListNode == null || !this.predicateListNode.equals(other.predicateListNode))) {
      return false;
    }
    if (this.axisNode != other.axisNode && (this.axisNode == null || !this.axisNode.equals(other.axisNode))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + (this.hasPredicates ? 1 : 0);
    hash = 59 * hash + (this.isAxisStep ? 1 : 0);
    hash = 59 * hash + (this.detailNode != null ? this.detailNode.hashCode() : 0);
    hash = 59 * hash + (this.predicateListNode != null ? this.predicateListNode.hashCode() : 0);
    hash = 59 * hash + (this.axisNode != null ? this.axisNode.hashCode() : 0);
    return hash;
  }
  
  
}
