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
import cz.cuni.mff.ksi.jinfer.projecttype.InputFilesList;
import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.DeleteAllAction;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.FileAddAction;
import java.awt.Image;
import java.io.File;
import java.util.Collection;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Provide node for input folder. Each folder node has associated {@link FileAddAction} and
 * {@link DeleteAllAction}.
 * @author sviro
 */
public class FolderNode extends AbstractNode {

  private final FolderType folderType;
  private final Collection<File> files;
  private final JInferProject project;

  /**
   * Default FolderNode constructor. For each FolderNode is determined for which
   * type of input files is used for.
   * @param project Project to be associated with.
   * @param folderType Type of input files for which is FolderNode created.
   * @param files Collection to store files added to this FolderNode.
   */
  public FolderNode(final JInferProject project, final FolderType folderType,
          final Collection<File> files) {
    this(project, folderType, files, new InstanceContent());
  }

  private FolderNode(final JInferProject project, final FolderType folderType,
          final Collection<File> files, final InstanceContent content) {
    super(new FileChildren(files), new AbstractLookup(content));
    content.add(project);
    content.add(files);
    this.project = project;
    this.files = files;
    this.folderType = folderType;

    if (files instanceof InputFilesList) {
      ((InputFilesList) files).setFileChildren(this.getChildren());
    }
  }

  @Override
  public String getDisplayName() {
    return folderType.getName();
  }

  @Override
  public Image getIcon(final int type) {
    return ImageUtilities.loadImage("cz/cuni/mff/ksi/jinfer/projecttype/graphics/folder.png");
  }

  @Override
  public Image getOpenedIcon(final int type) {
    return getIcon(type);
  }

  public FolderType getFolderType() {
    return folderType;
  }

  @Override
  public Action[] getActions(final boolean context) {
    return new Action[]{
              new FileAddAction(this),
              new DeleteAllAction(this)};
  }
}
