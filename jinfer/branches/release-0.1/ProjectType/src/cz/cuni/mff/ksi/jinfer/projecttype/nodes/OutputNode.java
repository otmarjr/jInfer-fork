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

import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import java.awt.Image;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *Provides Node for output folder.
 *
 * @author sviro
 */
final class OutputNode extends FilterNode {

  public OutputNode(final Node node, final JInferProject project) {
    super(node, new FilterNode.Children(node),
            new ProxyLookup(new Lookup[]{Lookups.singleton(project), node.getLookup()}));
  }

  @Override
  public Image getIcon(final int type) {
    return ImageUtilities.loadImage("cz/cuni/mff/ksi/jinfer/projecttype/graphics/folder.png");
  }

  @Override
  public Image getOpenedIcon(final int type) {
    return getIcon(type);
  }

  @Override
  public boolean canDestroy() {
    return false;
  }


}
