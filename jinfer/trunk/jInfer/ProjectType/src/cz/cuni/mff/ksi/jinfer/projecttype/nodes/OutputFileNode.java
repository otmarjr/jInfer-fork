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

import cz.cuni.mff.ksi.jinfer.projecttype.actions.ValidateAction;
import javax.swing.Action;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 * Node which encapsulates output Files in output node.
 * @author sviro
 * @see OutputNode
 */
public class OutputFileNode extends FilterNode{

  public OutputFileNode(final Node node) {
    super(node);
  }

  @Override
  public Action[] getActions(final boolean context) {
    final Action[] actions = super.getActions(context);
    Action[] result = new Action[actions.length + 1];

    System.arraycopy(actions, 0, result, 0, actions.length);
    result[result.length - 1] = ValidateAction.getInstance();
    return result;
  }




}
