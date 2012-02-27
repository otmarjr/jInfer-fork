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

import java.util.ArrayList;
import java.util.List;

/**
 * The node representing a tuple stream.
 *
 * @author Jiri Schejbal
 */
public class TupleStreamNode extends XQNode {

  private final List<VariableBindingNode> bindingClauses;

  public TupleStreamNode(XQNodeList<VariableBindingNode> bindingClauses) {
    assert (bindingClauses != null);
    this.bindingClauses = bindingClauses;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_TUPLE_STREAM;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    if (bindingClauses != null) {
      return new ArrayList<XQNode>(bindingClauses);
    }
    return null;
  }
  
  public List<VariableBindingNode> getBindingClauses() {
    return bindingClauses;
  }
}