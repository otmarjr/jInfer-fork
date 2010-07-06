
package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import cz.cuni.mff.ksi.jinfer.projecttype.actions.FileAddAction;
import java.awt.Image;
import java.util.Collection;
import javax.swing.Action;
import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;
import org.openide.util.ImageUtilities;

/**
 *
 * @author sviro
 */
public class FolderNode extends AbstractNode {


  private final String folderName;
  private final Collection<FileObject> files;

  public FolderNode(String folderName, Collection<FileObject> files) {
    super(new FileChildren(files));
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
    return new Action[] {new FileAddAction(this, files)};
  }

}
