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
 * The node representing a module.
 *
 * @author Jiri Schejbal
 */
public class ModuleNode extends XQNode {

  private final List<ModuleChildNode> moduleChildren;
  private final PrologNode prologNode;
  private final QueryBodyNode queryBodyNode;

  public ModuleNode(VersionDecl versionDecl, Module module) {
    assert (module != null);
    if (versionDecl != null) {
      addAttribute(AttrNames.ATTR_VERSION, versionDecl.getVersion());
      addAttribute(AttrNames.ATTR_ENCODING, versionDecl.getEncoding());
    }
    addAttribute(AttrNames.ATTR_MODULE_TYPE, module.getModuleTypeString());
    moduleChildren = module.getNodes();
    
    XQNode pNode = null;
    XQNode qbNode = null;
    for (final ModuleChildNode child : moduleChildren) {
      if (PrologNode.class.isInstance(child)) {
        assert(pNode == null);
        pNode = child;
      } else if (QueryBodyNode.class.isInstance(child)) {
        assert(qbNode == null);
        qbNode = child;
      }
    }
    
    prologNode = (PrologNode)pNode;
    queryBodyNode = (QueryBodyNode)qbNode;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_MODULE;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    if (moduleChildren != null) {
      return new ArrayList<XQNode>(moduleChildren);
    }
    return null;
  }

  public PrologNode getPrologNode() {
    return prologNode;
  }

  public QueryBodyNode getQueryBodyNode() {
    return queryBodyNode;
  }
  
}
