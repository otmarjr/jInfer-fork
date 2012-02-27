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
import java.util.Arrays;
import java.util.List;

/**
 * The node representing an axis.
 * 
 * @author Jiri Schejbal
 */
public class AxisNode extends XQNode {

  private final ItemTypeNode nodeTestNode;
  private final AxisKind axisKind;

  /**
   * Creates a new node representing an axis.
   * 
   * @param xqDocument Reference to associated XQuery document.
   * @param axisKind Axis kind.
   * @param nodeTestNode The node with node test.
   * @param isAbbreviated Flags showing weather the abbreviated syntax is used.
   */
  public AxisNode(AxisKind axisKind,
          ItemTypeNode nodeTestNode, boolean isAbbreviated) {
    assert (axisKind != null);
    addAttribute(AttrNames.ATTR_DIRECTION, axisKind.getDirectionValue());
    this.axisKind = axisKind;
    addAttribute(AttrNames.ATTR_ABBREVIATED,
            Boolean.toString(isAbbreviated));
    this.nodeTestNode = nodeTestNode;
  }

  @Override
  protected String getElementName() {
    return NodeNames.NODE_AXIS;
  }
  
  @Override
  public List<XQNode> getSubnodes() {
    if (nodeTestNode != null) {
      return new ArrayList<XQNode>(Arrays.asList(nodeTestNode));
    }
    return null;
  }

  public AxisKind getAxisKind() {
    return axisKind;
  }
  
}