/*
 *  Copyright (C) 2010 sviro
 * 
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

package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 * Creates children for output folder node. For each {@link File}, {@link OutputFileNode} which
 * represents particular file in project tree is created.
 * @author sviro
 */
public class OutputChildren extends FilterNode.Children{

  /**
   * Default constructor.
   * @param node {@link Node} defining output folder in jInfer project.
   */
  public OutputChildren(final Node node) {
    super(node);
  }

  @Override
  protected Node[] createNodes(final Node key) {
    return new Node[] {new OutputFileNode(super.createNodes(key)[0])};
  }



}
