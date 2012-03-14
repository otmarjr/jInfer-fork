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
 * The node representing a constructor.
 *
 * @author Jiri Schejbal
 */
public class ConstructorNode extends ExprNode {

  private ContentNode contentNode;
  private NameNode nameNode;
  private AttrListNode attrListNode;
  private PITargetNode piTargetNode;

  private ConstructorNode(ConstructorKind kind, ConstructorType type) {
    assert (kind != null);
    assert (type != null);
    addAttribute(AttrNames.ATTR_KIND, kind.toString());
    addAttribute(AttrNames.ATTR_TYPE, type.toString());
  }

  /**
   * For direct comment, computed document, computed text and computed
   * comment.
   *
   * @param kind
   * @param type
   * @param contentNode
   */
  public ConstructorNode(
          ConstructorKind kind,
          ConstructorType type,
          ContentNode contentNode) {
    this(kind, type);
    /*assert(kind == ConstructorKind.DIRECT);
    assert(type == ConstructorType.COMMENT);
    assert(kind == ConstructorKind.COMPUTED);
    assert(
    type == ConstructorType.DOCUMENT ||
    type == ConstructorType.TEXT ||
    type == ConstructorType.COMMENT
    );*/
    //TODO: pridat spravne asserty

    this.contentNode = contentNode;
  }

  /**
   * For direct element.
   *
   * @param kind
   * @param type
   * @param nameNode
   * @param attrListNode
   * @param contentNode
   */
  public ConstructorNode(
          ConstructorKind kind,
          ConstructorType type,
          NameNode nameNode,
          AttrListNode attrListNode,
          ContentNode contentNode) {
    this(kind, type);
    assert (kind == ConstructorKind.DIRECT);
    assert (type == ConstructorType.ELEMENT);
    assert (nameNode != null);
    this.nameNode = nameNode;
    if (attrListNode != null && !attrListNode.isEmpty()) {
      this.attrListNode = attrListNode;
    }
    this.contentNode = contentNode;
  }

  /**
   * For direct processing instruction and computed processing instruction.
   * 
   * @param kind
   * @param type
   * @param piTargetNode
   * @param contentNode
   */
  public ConstructorNode(
          ConstructorKind kind,
          ConstructorType type,
          PITargetNode piTargetNode,
          ContentNode contentNode) {
    this(kind, type);
    assert (type == ConstructorType.PROCESSING_INSTRUCTION);
    assert (piTargetNode != null);
    this.piTargetNode = piTargetNode;
    this.contentNode = contentNode;
  }

  /**
   * For computed element and computed attribute.
   *
   * @param kind
   * @param type
   * @param nameNode
   * @param contentNode
   */
  public ConstructorNode(
          ConstructorKind kind,
          ConstructorType type,
          NameNode nameNode,
          ContentNode contentNode) {
    this( kind, type);
    assert (kind == ConstructorKind.COMPUTED);
    assert (type == ConstructorType.ELEMENT
            || type == ConstructorType.ATTRIBUTE);
    this.nameNode = nameNode;
    this.contentNode = contentNode;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_CONSTRUCTOR;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    final List<XQNode> subnodes = new ArrayList<XQNode>();
    
    if (nameNode != null) {
      subnodes.add(nameNode);
    }
    if (piTargetNode != null) {
      subnodes.add(piTargetNode);
    }
    if (attrListNode != null) {
      subnodes.add(attrListNode);
    }
    if (contentNode != null) {
      subnodes.add(contentNode);
    }
    
    return subnodes;
  }
}
