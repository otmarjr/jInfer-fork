
package cz.cuni.mff.ksi.jinfer.projecttype.nodes;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;

/**
 *
 * @author sviro
 */
public class FolderNode extends AbstractNode {


  private final String folderName;

  public FolderNode(String folderName) {
    super(Children.LEAF);
    this.folderName = folderName;
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





}
