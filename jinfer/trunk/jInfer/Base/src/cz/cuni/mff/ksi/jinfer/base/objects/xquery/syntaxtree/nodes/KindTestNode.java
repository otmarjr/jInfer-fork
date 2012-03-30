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
 * The node representing a kind test.
 *
 * @author Jiri Schejbal
 */
public class KindTestNode extends ItemTypeNode {

  private NodeKind nodeKind;
  private KindTestNode elementNode;

  public KindTestNode(NodeKind nodeKind) {
    assert (nodeKind != null);
    this.nodeKind = nodeKind;
    addAttribute(AttrNames.ATTR_KIND, nodeKind.toString());
  }

  public NodeKind getNodeKind() {
    return nodeKind;
  }

  public KindTestNode(NodeKind nodeKind, String name) {
    this(nodeKind);
    switch (nodeKind) {
      case PROCESSING_INSTRUCTION:
        addAttribute(AttrNames.ATTR_TARGET, name);
      case SCHEMA_ATTRIBUTE:
      case SCHEMA_ELEMENT:
        addAttribute(AttrNames.ATTR_NAME, name);
        break;
      default:
        assert (false);
    }
  }

  public KindTestNode(NodeKind nodeKind, KindTestSuffix suffix) {
    this(nodeKind);
    assert (nodeKind == NodeKind.ELEMENT || nodeKind == NodeKind.ATTRIBUTE);
    if (suffix != null) {
      if (suffix.getName() != null) {
        addAttribute(AttrNames.ATTR_NAME, suffix.getName());
      }
      if (suffix.getType() != null) {
        addAttribute(AttrNames.ATTR_TYPE, suffix.getType());
      }
      if (nodeKind == NodeKind.ELEMENT && suffix.getIsNillable() != null) {
        addAttribute(AttrNames.ATTR_NILLABLE,
                suffix.getIsNillable().toString());
      }
    }
  }

  public KindTestNode(NodeKind nodeKind, KindTestNode elementNode) {
    this(nodeKind);
    assert (nodeKind == NodeKind.DOCUMENT);
    assert (elementNode.getNodeKind() == NodeKind.ELEMENT
            || elementNode.getNodeKind() == NodeKind.SCHEMA_ELEMENT);
    this.elementNode = elementNode;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_KIND_TEST;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    final List<XQNode> subnodes = new ArrayList<XQNode>();
    
    if (elementNode != null) {
      subnodes.add(elementNode);
    }
    
    return subnodes;
  }
}
