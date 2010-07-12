/*
 *  Copyright (C) 2010 vitasek
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

package cz.cuni.mff.ksi.jinfer.ruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.ruledisplayer.options.RuleDisplayerPanel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 *
 * @author vitasek
 */
public class NodePainter {

  /** Maximum nesting level. */
  private final int maxLevel;
  
  private final List<AbstractNode> visited = new ArrayList<AbstractNode>();

  private final Graphics2D graphics;

  public NodePainter(final Graphics2D graphics) {
    this.maxLevel = Preferences.userNodeForPackage(RuleDisplayerPanel.class).getInt("nesting.level", 25);
    this.graphics = graphics;
  }

  public Image drawNode(final AbstractNode n, final int level) {
    if (level > maxLevel) {
      return Utils.DOTS;
    }

    final FontMetrics fm = graphics.getFontMetrics();

    final int nameWidth = (int)(fm.stringWidth(n.getName()) * 1.5);
    final int nameHeight = fm.getHeight() - fm.getDescent();

    final Image children = isVisited(n) ? Utils.ARROW : drawSubnodes(n, level + 1);
    if (n.isElement()) {
      visited.add(n);
    }

    final int width;
    final int height;

    if (children != null) {
      width =  nameWidth + 10 + children.getWidth(null) + 1;
      height = Math.max(children.getHeight(null), fm.getHeight()) + 5;
    }
    else {
      width = nameWidth + 1;
      height = fm.getHeight() + 1;
    }

    final BufferedImage ret = Utils.getImage(width, height);

    final Graphics2D g = ret.createGraphics();

    g.setColor(Utils.getNodeColor(n));
    g.fillRect(0, 0, nameWidth + 10, fm.getHeight());
    g.setColor(Color.white);
    g.drawString(n.getName(), 1, nameHeight);
    g.setColor(Color.black);
    g.drawRect(0, 0, width - 1, height - 1);

    if (children != null) {
      g.setColor(Color.white);
      Utils.drawArrow(g, nameWidth, fm.getHeight());
      g.drawImage(children, nameWidth + 10, 4, null);
    }
    return ret;
  }

  private Image drawSubnodes(final AbstractNode n, final int level) {
    if (!n.isElement()) {
      return null;
    }
    final Regexp<AbstractNode> subnodes = ((Element)n).getSubnodes();
    if (subnodes == null) {
      return null;
    }
    return drawRegexp(subnodes, level + 1);
  }

  private Image drawRegexp(final Regexp<AbstractNode> subnodes, final int level) {
    if (level > maxLevel) {
      return Utils.DOTS;
    }
    switch (subnodes.getType()) {
      case TOKEN: return drawNode(subnodes.getContent(), level + 1);
      case ALTERNATION: return drawAlternation(subnodes, level + 1);
      case CONCATENATION: return drawConcatenation(subnodes, level + 1);
      case KLEENE: return drawKleene(subnodes, level + 1);
      default: throw new IllegalArgumentException();
    }
  }

  private Image drawKleene(final Regexp<AbstractNode> subnodes, final int level) {
    final Image kleene = drawNode(subnodes.getChild(0).getContent(), level + 1);
    final BufferedImage kleeneRet = Utils.getImage(kleene.getWidth(null) + 10, kleene.getHeight(null));
    final Graphics2D g = kleeneRet.createGraphics();
    g.drawImage(kleene, 0, 0, null);
    g.setColor(Color.black);
    g.drawString("*", kleene.getWidth(null), 10);
    return kleeneRet;
  }

  private List<Image> getChildrenImages(final List<Regexp<AbstractNode>> children, final int level) {
    final List<Image> ret = new ArrayList<Image>(children.size());
    for (final Regexp<AbstractNode> child : children) {
      final Image childImg = drawRegexp(child, level + 1);
      if (childImg != null) {
        ret.add(childImg);
      }
      else {
        ret.add(Utils.LAMBDA);
      }
    }
    return ret;
  }

  private Image drawAlternation(final Regexp<AbstractNode> subnodes, final int level) {
    if (BaseUtils.isEmpty(subnodes.getChildren())) {
      return null;
    }

    final List<Image> altImgs = getChildrenImages(subnodes.getChildren(), level + 1);
    int width = 0;
    int height = 0;
    for (final Image img : altImgs) {
      width = Math.max(width, img.getWidth(null));
      height += img.getHeight(null) + 1;
    }
    final BufferedImage altRet = Utils.getImage(width + 3, height);
    final Graphics2D g = altRet.createGraphics();
    int offset = 0;
    for (final Image img : altImgs) {
      g.drawImage(img, 3, offset, null);
      offset += img.getHeight(null) + 1;
    }
    g.setColor(Color.blue);
    g.drawLine(1, 0, 1, height - 1);
    return altRet;
  }

  private Image drawConcatenation(final Regexp<AbstractNode> subnodes, final int level) {
    if (BaseUtils.isEmpty(subnodes.getChildren())) {
      return null;
    }

    final List<Image> concatImgs = getChildrenImages(subnodes.getChildren(), level + 1);
    int width = 0;
    int height = 0;
    for (final Image img : concatImgs) {
      width += img.getWidth(null) + 1;
      height = Math.max(height, img.getHeight(null));
    }
    final BufferedImage concatRet = Utils.getImage(width, height + 3);
    final Graphics2D g = concatRet.createGraphics();
    int offset = 0;
    for (final Image img : concatImgs) {
      g.drawImage(img, offset, 3, null);
      offset += img.getWidth(null) + 1;
    }
    g.setColor(Color.red);
    g.drawLine(0, 1, width - 1, 1);
    return concatRet;
  }

  private boolean isVisited(final AbstractNode n) {
    for (final AbstractNode v : visited) {
      if (v == n) {
        return true;
      }
    }
    return false;
  }

}
