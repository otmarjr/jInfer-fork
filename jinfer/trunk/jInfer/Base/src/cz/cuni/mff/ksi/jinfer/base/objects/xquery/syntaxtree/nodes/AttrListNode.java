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
 * The node representing the attribute list.
 *
 * @author Jiri Schejbal
 */
public class AttrListNode extends XQNode {

  /**
   * Flags showing whether the list is empty.
   */
  private boolean isEmpty;
  private List<XQNode> children;

  /**
   * Creates a new node representing the attribute list.
   *
   * @param xqDocument Reference to associated XQuery document.
   * @param attributes List of attribute nodes.
   */
  public AttrListNode(XQNodeList<AttributeNode> attributes) {
    isEmpty = attributes == null || attributes.size() == 0;
    if (attributes == null) {
      children = new ArrayList<XQNode>();
    } else {
      children = new ArrayList<XQNode>(attributes);
    }
  }

  /**
   * Gets information whether the attribute list is empty.
   *
   * @return <code>true</code> if the attribute list is empty,
   *      <code>false</code> otherwise.
   */
  public boolean isEmpty() {
    return isEmpty;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_ATTR_LIST;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    return children;
  }
}
