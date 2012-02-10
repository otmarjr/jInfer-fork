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

import java.util.List;

/**
 * The node representing a boundary space declaration.
 *
 * @author Jiri Schejbal
 */
public class BoundarySpaceDeclNode extends PrologChildNode {

  public BoundarySpaceDeclNode(
          final XQNode parentNode, BoundarySpaceMode mode) {
    super(parentNode);
    assert (mode != null);
    addAttribute(AttrNames.ATTR_MODE, mode.toString());
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_BOUNDARY_SPACE_DECL;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    return null;
  }
}
