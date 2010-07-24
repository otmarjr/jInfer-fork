package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.projecttype.NoDataObjectException;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author sviro
 */
public class FileChildren extends Children.Keys<File> {

  private final Collection<File> files;

  public FileChildren(final Collection<File> files) {
    super();
    this.files = files;
  }

  @Override
  public void addNotify() {
    final boolean fileRemoved = Input.removeNonExistFiles(files);
    if (fileRemoved) {
      DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message("Some of the input files were deleted from disk. They were removed from input files.", NotifyDescriptor.INFORMATION_MESSAGE));
    }
    setKeys(files);
  }

  @Override
  protected Node[] createNodes(final File file) {
    try {
      return new Node[]{new FileNode(DataObject.find(FileUtil.toFileObject(file)).getNodeDelegate())};
    } catch (DataObjectNotFoundException ex) {
      throw new NoDataObjectException(ex.getMessage());
    }
  }
}
