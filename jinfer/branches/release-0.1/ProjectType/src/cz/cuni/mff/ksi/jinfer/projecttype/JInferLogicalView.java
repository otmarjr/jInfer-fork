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
package cz.cuni.mff.ksi.jinfer.projecttype;

import cz.cuni.mff.ksi.jinfer.projecttype.nodes.RootNode;
import java.awt.Image;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Creates logical view of the jInfer Project, which means that it return root node.
 * This root node contains three input folders: XML, schema, query and one outup folder.
 * @author sviro
 */
public class JInferLogicalView implements LogicalViewProvider {

  private final JInferProject project;
  private RootNode rootNode = null;

  public JInferLogicalView(final JInferProject project) {
    this.project = project;
  }

  @Override
  public Node createLogicalView() {
    if (rootNode == null) {
      rootNode = new RootNode(project);
    }
    return rootNode;
  }

  @Override
  public Node findPath(final Node node, final Object o) {
    return null;
  }
}
