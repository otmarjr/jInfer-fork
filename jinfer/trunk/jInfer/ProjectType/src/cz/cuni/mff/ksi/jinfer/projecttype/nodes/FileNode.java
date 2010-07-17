
package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import java.io.IOException;
import java.util.Collection;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author sviro
 */
public class FileNode extends FilterNode{


  public FileNode(final Node node) {
    super(node, new FilterNode.Children(node), node.getLookup());
    disableDelegation(DELEGATE_DESTROY);
  }

  @Override
  public void destroy() throws IOException {
    final Collection<FileObject> files = (Collection<FileObject>)((FolderNode)this.getParentNode()).getLookup().lookup(Collection.class);
    final DataNode orig = (DataNode) this.getOriginal();
    final FileObject fileOb = orig.getDataObject().getPrimaryFile();
    files.remove(fileOb);

    ((FileChildren)((FolderNode)this.getParentNode()).getChildren()).addNotify();
    
    super.destroy();
  }

  

}
