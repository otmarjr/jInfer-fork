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

import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.FilesAddAction;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.RunAction;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.RunRepairAction;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.loaders.DataFolder;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 * Provides node for jInfer project root. This node has four children: three input nodes and one output
 * node. This node also provides some basic project actions like close, delete, Set as Main Project etc.
 * 
 * @author sviro
 */
public class RootNode extends AbstractNode {

  private final JInferProject project;

  /**
   * Default constructor.
   * @param project jInfer project for which is this node registered.
   */
  public RootNode(final JInferProject project) {
    super(new Children.Keys<Node>() {

      @Override
      protected void addNotify() {
        final Input input = project.getLookup().lookup(Input.class);
        setKeys(new Node[]{new FolderNode(project, FolderType.DOCUMENT,
                  input.getDocuments()), new FolderNode(project, FolderType.SCHEMA,
                  input.getSchemas()), new FolderNode(project, FolderType.QUERY,
                  input.getQueries()), new FolderNode(project, FolderType.FD, input.getFunctionalDependencies()),
                  new OutputNode(DataFolder.findFolder(project.getOutputFolder(true)).
                  getNodeDelegate(),
                  project)});
      }

      @Override
      protected Node[] createNodes(final Node node) {

        return new Node[]{node};
      }
    }, Lookups.singleton(project));
    this.project = project;
  }

  @Override
  public Image getIcon(final int type) {
    return ImageUtilities.loadImage("cz/cuni/mff/ksi/jinfer/projecttype/graphics/icon16.png");
  }

  @Override
  public Image getOpenedIcon(final int type) {
    return getIcon(type);
  }

  @Override
  public String getDisplayName() {
    return ProjectUtils.getInformation(project).getDisplayName();
  }

  @Override
  public Action[] getActions(final boolean context) {
    final List<Action> nodeActions = new ArrayList<Action>();
    nodeActions.add(new FilesAddAction(project));
    nodeActions.add(new RunAction(project));
    nodeActions.add(new RunRepairAction(project));
    nodeActions.add(null); //separator
    nodeActions.add(CommonProjectActions.renameProjectAction());
    nodeActions.add(CommonProjectActions.moveProjectAction());
    nodeActions.add(CommonProjectActions.copyProjectAction());
    nodeActions.add(CommonProjectActions.deleteProjectAction());
    nodeActions.add(null); //separator
    nodeActions.add(CommonProjectActions.setAsMainProjectAction());
    nodeActions.add(CommonProjectActions.closeProjectAction());
    nodeActions.add(null); //separator
    nodeActions.add(CommonProjectActions.customizeProjectAction());
    return nodeActions.toArray(new Action[nodeActions.size()]);
  }
}
