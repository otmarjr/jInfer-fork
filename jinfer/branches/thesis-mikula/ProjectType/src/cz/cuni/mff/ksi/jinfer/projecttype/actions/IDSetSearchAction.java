/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.projecttype.actions;

import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IDSetSearchHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.runner.properties.ModuleSelectionPropertiesPanel;
import java.io.File;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 * Action on a XML file node to open ISS panel for this one file.
 *
 * @author vektor
 */
public final class IDSetSearchAction extends NodeAction {

  private static final long serialVersionUID = 1562121212L;

  private static final Logger LOG = Logger.getLogger(IDSetSearchAction.class);

  private IDSetSearchAction() {

  }

  private static IDSetSearchAction action = null;

  public static synchronized IDSetSearchAction getInstance() {
    if (action == null) {
      action = new IDSetSearchAction();
    }
    return action;
  }

  @Override
  protected void performAction(final Node[] nodes) {
    if (nodes.length != 1
            || !FolderType.DOCUMENT.getName().equals(nodes[0].getParentNode().getDisplayName())) {
      throw new IllegalStateException("This action is available only to one XML node");
    }

    final Node node = nodes[0];
    final File f = getFileForNode(node);
    final Properties p = getPropsForNode(node);

    final IGGenerator igg = ModuleSelectionHelper.lookupImpl(IGGenerator.class,
            p.getProperty(ModuleSelectionPropertiesPanel.IGG_PROP, ModuleSelectionPropertiesPanel.IGG_DEFAULT));

    final Input input = new Input();
    input.getDocuments().add(f);
    start(input, igg);
  }

  private static File getFileForNode(final Node node) {
    return FileUtil.toFile(node.getLookup().lookup(DataObject.class).getPrimaryFile());
  }

  private static Properties getPropsForNode(final Node node) {
    return node.getParentNode().getLookup().lookup(Project.class).getLookup().lookup(Properties.class);
  }

  private static void start(final Input input, final IGGenerator igg) {
    AsynchronousUtils.runAsync(new Runnable() {

      @Override
      public void run() {
        try {
          igg.start(input,
                  new IGGeneratorCallback() {

                    @Override
                    public void finished(final List<Element> grammar) {
                      IDSetSearchHelper.showISSPanelAsync("ID Set Search", grammar);
                    }
                  });
        } catch (final InterruptedException e) {
          LOG.error("User interrupted the inference.", e);
        }
      }
    }, "Retrieving attribute statistics");
  }

  @Override
  protected boolean enable(final Node[] nodes) {
    if (nodes.length != 1) {
      return false;
    }
    return FolderType.DOCUMENT.getName().equals(nodes[0].getParentNode().getDisplayName());
  }

  @Override
  public String getName() {
    return "ID Set Search...";
  }

  @Override
  public HelpCtx getHelpCtx() {
    return null;
  }

  @Override
  protected boolean asynchronous() {
    return false;
  }

}
