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
 * The node representing a operator.
 *
 * @author Jiri Schejbal
 */
public class OperatorNode extends ExprNode {

  private ExprNode operand;
  private TypeNode typeNode;
  private ExprNode rightSide;
  private final Operator operator;

  public OperatorNode(Operator operator, ExprNode operand) {
    assert (operator != null);
    assert (operand != null);
    this.operator = operator;
    if (operator.getOpSubClass() != null) {
      addAttribute(AttrNames.ATTR_SUB_CLASS, operator.getOpSubClass());
    }
    this.operand = operand;
  }

  public OperatorNode(Operator operator,
          ExprNode exprNode, TypeNode typeNode) {
    this(operator, exprNode);
    assert (operator == Operator.INSTANCE_OF
            || operator == Operator.CASTABLE_AS
            || operator == Operator.TREAT_AS
            || operator == Operator.CAST_AS);
    assert (typeNode != null);
    this.typeNode = typeNode;
  }

  public OperatorNode(Operator operator,
          ExprNode leftSide, ExprNode rightSide) {
    this(operator, leftSide);
    assert (rightSide != null);
    this.rightSide = rightSide;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_OPERATOR;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    final List<XQNode> subnodes = new ArrayList<XQNode>();
    
    subnodes.add(operand);
    if (typeNode != null) {
      subnodes.add(typeNode);
    }
    if (rightSide != null) {
      subnodes.add(rightSide);
    }
    
    return subnodes;
  }
  
  public Operator getOperator() {
    return operator;
  }
  
  public TypeNode getTypeNode() {
    return typeNode;
  }
  
  public ExprNode getOperand() {
    return operand;
  }
  
  public ExprNode getRightSide() {
    return rightSide;
  }
}
