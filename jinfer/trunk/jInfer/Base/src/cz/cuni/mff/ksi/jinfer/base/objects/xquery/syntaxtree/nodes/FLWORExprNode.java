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
import java.util.Arrays;
import java.util.List;

/**
 * The node representing a FLWOR.
 *
 * @author Jiri Schejbal
 */
public class FLWORExprNode extends ExprNode {

  private final TupleStreamNode tupleStreamNode;
  private final WhereClauseNode whereClauseNode;
  private final OrderByClauseNode orderByClauseNode;
  private final ReturnClauseNode returnClauseNode;

  public FLWORExprNode(TupleStreamNode tupleStreamNode,
          WhereClauseNode whereClauseNode, OrderByClauseNode orderByClauseNode,
          ReturnClauseNode returnClauseNode) {
    assert (tupleStreamNode != null);
    assert (returnClauseNode != null);
    this.tupleStreamNode = tupleStreamNode;
    this.whereClauseNode = whereClauseNode;
    this.orderByClauseNode = orderByClauseNode;
    this.returnClauseNode = returnClauseNode;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_FLWOR_EXPR;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    final List<XQNode> subnodes = new ArrayList<XQNode>();
    
    subnodes.add(tupleStreamNode);
    if (whereClauseNode != null) {
      subnodes.add(whereClauseNode);
    }
    if (orderByClauseNode != null) {
      subnodes.add(orderByClauseNode);
    }
    subnodes.add(returnClauseNode);
    
    return subnodes;
  }
  
  public ReturnClauseNode getReturnClauseNode() {
    return returnClauseNode;
  }
  
  public TupleStreamNode getTupleStreamNode() {
    return tupleStreamNode;
  }
  
  public WhereClauseNode getWhereClauseNode() {
    return whereClauseNode;
  }
}
