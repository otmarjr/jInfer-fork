
package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import cz.cuni.mff.ksi.jinfer.projecttype.NoDataObjectException;
import java.util.Collection;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author sviro
 */
public class FileChildren extends Children.Keys<FileObject>{

  private final Collection<FileObject> files;

  public FileChildren(Collection<FileObject> files) {
    super();
    this.files = files;
  }

  @Override
  public void addNotify() {
    setKeys(files);
  }

  @Override
  protected Node[] createNodes(FileObject fileOb) {
    try {
      return new Node[]{DataObject.find(fileOb).getNodeDelegate()};
    } catch (DataObjectNotFoundException ex) {
      throw new NoDataObjectException(ex.getMessage());
    }
  }

}
