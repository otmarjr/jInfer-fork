
package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import cz.cuni.mff.ksi.jinfer.projecttype.JInferProject;
import cz.cuni.mff.ksi.jinfer.projecttype.actions.FileAddAction;
import java.awt.Image;
import java.util.Collection;
import javax.swing.Action;
import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author sviro
 */
public class FolderNode extends AbstractNode {


  private final String folderName;
  private final Collection<FileObject> files;
  private final JInferProject project;

  public FolderNode(final JInferProject project, final String folderName, final Collection<FileObject> files) {
    this(project, folderName, files, new InstanceContent());
  }

  private FolderNode(final JInferProject project, final String folderName, final Collection<FileObject> files, InstanceContent content) {
    super(new FileChildren(files), new AbstractLookup(content));
    content.add(project);
    content.add(files);
    this.project = project;
    this.folderName = folderName;
    this.files = files;
  }

  @Override
  public String getDisplayName() {
    return folderName;
  }
  

  @Override
  public Image getIcon(int type) {
    return ImageUtilities.loadImage("cz/cuni/mff/ksi/jinfer/projecttype/graphics/folder.png");
  }

  @Override
  public Image getOpenedIcon(int type) {
    return getIcon(type);
  }

  @Override
  public Action[] getActions(boolean context) {
    return new Action[] {new FileAddAction(project, this, files)};
  }

}
