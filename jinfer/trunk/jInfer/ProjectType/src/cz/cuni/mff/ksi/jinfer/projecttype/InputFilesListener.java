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

import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FileChildren;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Children;

/**
 * {@link FileChangeListener} for refreshing input node after removing input file from project.
 * @author sviro
 */
public class InputFilesListener implements FileChangeListener {

  private final InputFilesList list;

  public InputFilesListener(final InputFilesList list) {
    this.list = list;
  }

  @Override
  public void fileFolderCreated(final FileEvent fe) {
    //
  }

  @Override
  public void fileDataCreated(final FileEvent fe) {
    //
  }

  @Override
  public void fileChanged(final FileEvent fe) {
    //
  }

  @Override
  public void fileDeleted(final FileEvent fe) {
    if (list != null) {
      list.remove(FileUtil.toFile(fe.getFile()));

      final Children fileChildren = list.getFileChildren();
      if (fileChildren != null) {
        ((FileChildren) fileChildren).refreshNodes();
      }
      DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
              org.openide.util.NbBundle.getMessage(InputFilesListener.class, "Input.deletedInputFiles.message"),
              NotifyDescriptor.INFORMATION_MESSAGE));
    }
  }

  @Override
  public void fileRenamed(final FileRenameEvent fe) {
    //
  }

  @Override
  public void fileAttributeChanged(final FileAttributeEvent fe) {
    //
  }
}
