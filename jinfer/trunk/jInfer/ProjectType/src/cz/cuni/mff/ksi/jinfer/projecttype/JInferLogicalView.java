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

import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderNode;
import java.awt.Image;
import javax.swing.Action;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.loaders.DataFolder;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author sviro
 */
public class JInferLogicalView implements LogicalViewProvider {

  private static final class InputNode extends FilterNode {

    final JInferProject project;

    public InputNode(Node node, JInferProject project) {
      super(node, new FilterNode.Children(node), new ProxyLookup(new Lookup[]{Lookups.singleton(project), node.getLookup()}));
      this.project = project;
    }

    @Override
    public Image getIcon(int type) {
      return ImageUtilities.loadImage("cz/cuni/mff/ksi/jinfer/projecttype/graphics/folder.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
      return getIcon(type);
    }


  }

  private static final class RootNode extends AbstractNode {

    private final JInferProject project;

    public RootNode(final JInferProject project) {
      super(new Children.Keys<Node>() {

        @Override
        protected void addNotify() {
          setKeys(new Node[]{new FolderNode("xml"), new FolderNode("schema"), new FolderNode("query"), new InputNode(DataFolder.findFolder(project.getOutputFolder(true)).getNodeDelegate(), project)});
        }

        @Override
        protected Node[] createNodes(Node node) {

          return new Node[]{node};
        }
      }, Lookups.singleton(project));
      this.project = project;
    }

    @Override
    public Image getIcon(int type) {
      return ImageUtilities.loadImage("cz/cuni/mff/ksi/jinfer/projecttype/graphics/icon16.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
      return getIcon(type);
    }

    @Override
    public String getDisplayName() {
      return project.getProjectDirectory().getName();
    }

    @Override
    public Action[] getActions(boolean context) {
      Action[] nodeActions = new Action[4];
      nodeActions[0] = CommonProjectActions.copyProjectAction();
      nodeActions[1] = CommonProjectActions.deleteProjectAction();
      nodeActions[2] = CommonProjectActions.setAsMainProjectAction();
      nodeActions[3] = CommonProjectActions.closeProjectAction();
      return nodeActions;
    }

  }
  private final JInferProject project;

  public JInferLogicalView(JInferProject project) {
    this.project = project;
  }

  @Override
  public Node createLogicalView() {
    return new RootNode(project);
  }

  @Override
  public Node findPath(Node node, Object o) {
    return null;
  }
}
