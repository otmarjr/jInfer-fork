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

/**
 * This class represents the node that can occur under the Content node of the
 * constructor.
 *
 * @author Jiri Schejbal
 */
public class NodeContentItem extends ContentItem {

  private XQNode node;

  public NodeContentItem(XQNode node) {
    this.node = node;
  }

  @Override
  public void handleContent(
          XQNodeList<XQNode> nodes,
          StringContent stringContent) {
    flushStringContent(nodes, stringContent);
    nodes.add(node);
  }
}
