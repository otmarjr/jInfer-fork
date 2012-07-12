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
package cz.cuni.mff.ksi.jinfer.projecttype.actions;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FileChildren;
import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderNode;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Lookup;

/**
 * Action for {@link FolderNode} which adds specific file into input folder.
 * @author sviro
 * @see FolderNode
 */
public class FileAddAction extends AbstractAction {

  private static final long serialVersionUID = 12121452l;
  private final FolderNode node;
  private final FolderType type;

  /**
   * Default constructor.
   * @param node Node for which is this action registered.
   */
  public FileAddAction(final FolderNode node) {
    super();
    type = node.getFolderType();
    putValue(NAME, "Add " + type.getName() + " files");
    this.node = node;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void actionPerformed(final ActionEvent e) {
    final Collection<? extends Processor> processors = Lookup.getDefault().
            lookupAll(Processor.class);

    final StringBuilder builder = new StringBuilder();
    buildFilterName(builder, processors);

    FileChooserBuilder fileChooserBuilder = new FileChooserBuilder(FileAddAction.class).setDefaultWorkingDirectory(new File(System.getProperty("user.home"))).
            setTitle("Add " + type.getName() + " files").setFilesOnly(true);

    if (FolderType.QUERY.equals(type)) {
      fileChooserBuilder = fileChooserBuilder.addFileFilter(new FileNameExtensionFilter(
              "XPath files (*.xpath)", "xpath")).addFileFilter(new FileNameExtensionFilter("XQuery files (*.xq)", "xq")).
              addFileFilter(new FileNameExtensionFilter(
              "Text files (*.txt)", "txt")).addFileFilter(new FileNameExtensionFilter(
              "Text, XPath, and XQuery files (*.txt, *.xpath, *.xq)",
              "txt", "xpath", "xq"));
    } else {
      final ArrayList<String> extensions = new ArrayList<String>();
      for (Processor processor : processors) {
        if (type.equals(processor.getFolder())) {
          extensions.add(processor.getExtension());
        }
      }
      fileChooserBuilder = fileChooserBuilder.addFileFilter(new FileNameExtensionFilter(
              builder.toString(), extensions.toArray(new String[extensions.size()])));
    }

    final File[] selectedFiles = fileChooserBuilder.showMultiOpenDialog();

    if (selectedFiles != null) {
      ((Collection<File>) node.getLookup().lookup(Collection.class)).addAll(Arrays.asList(selectedFiles));

      ((FileChildren) node.getChildren()).refreshNodes();

      final Project project = node.getLookup().lookup(Project.class);
      project.getLookup().lookup(ProjectState.class).markModified();
    }
  }

  private void buildFilterName(final StringBuilder builder,
          final Collection<? extends Processor> processors) {
    if (FolderType.DOCUMENT.equals(type)) {
      builder.append("XML files (");
    } else if (FolderType.SCHEMA.equals(type)) {
      builder.append("Schema files (");
    } else if (FolderType.QUERY.equals(type)) { //NOPMD - QUERY is handling separately
      //
    } else if (FolderType.FD.equals(type)) {
        builder.append("FD files(");
    }
    boolean first = true;
    for (Processor processor : processors) {
      if (type.equals(processor.getFolder())) {
        if (first) {
          first = false;
          builder.append("*.").append(processor.getExtension());
        } else {
          builder.append(", *.").append(processor.getExtension());
        }
      }
    }
    builder.append(")");
  }
}
