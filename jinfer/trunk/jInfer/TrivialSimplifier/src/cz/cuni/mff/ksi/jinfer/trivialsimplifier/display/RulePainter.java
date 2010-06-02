/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.trivialsimplifier.display;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;

/**
 * Class responsible for rendering a set of rules to a canvas.
 *
 * @author vektor
 */
public class RulePainter {  

  private final Component root;
  private Image image;

  public RulePainter(final Component root) {
    this.root = root;
  }

  /**
   * Set the rules this painter will render.
   */
  public void setRules(final List<AbstractNode> rules) {
    final BufferedImage img = getImage(1000, 1000);
    if (rules != null) {
      int offset = 0;
      final Graphics2D g = img.createGraphics();
      for (final AbstractNode a : rules) {
        final Image i = drawNode(a);
        g.drawImage(i, 0, offset, null);
        offset += i.getHeight(null) + 2;
      }
    }
    this.image = img;
    root.repaint();
  }
  
  public void paint(final Graphics2D g) {
    g.drawImage(image, 0, 0, null);
  }

  private Image drawNode(final AbstractNode n) {
    final FontMetrics fm = root.getGraphics().getFontMetrics();
    final int nameWidth = (int)(fm.stringWidth(n.getName()) * 1.2);
    final int nameHeight = fm.getHeight() - fm.getDescent();

    final Image children = drawSubnodes(n);
    
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

    final BufferedImage ret = getImage(width, height);

    final Graphics2D g = ret.createGraphics();

    g.setColor(Color.black);
    g.drawRect(0, 0, width - 1, height - 1);
    g.setColor(Utils.getNodeColor(n));
    g.fillRect(0, 0, nameWidth + 10, fm.getHeight());
    g.setColor(Color.white);
    g.drawString(n.getName(), 0, nameHeight);
    
    if (children != null) {
      g.drawLine(nameWidth, fm.getHeight() / 2, nameWidth + 10, fm.getHeight() / 2);
      g.drawLine(nameWidth + 5, fm.getHeight() / 4, nameWidth + 10, fm.getHeight() / 2);
      g.drawLine(nameWidth + 5, (3 * fm.getHeight()) / 4, nameWidth + 10, fm.getHeight() / 2);
      g.drawImage(children, nameWidth + 10, 5, null);
    }
    return ret;
  }

  private Image drawSubnodes(final AbstractNode n) {
    if (!NodeType.ELEMENT.equals(n.getType())) {
      return null;
    }
    final Regexp<AbstractNode> subnodes = ((Element)n).getSubnodes();
    if (subnodes == null) {
      return null;
    }
    return drawRegexp(subnodes);
  }

  private Image drawRegexp(final Regexp<AbstractNode> subnodes) {
    switch (subnodes.getType()) {
      case TOKEN: return drawNode(subnodes.getContent());
      case ALTERNATION: return drawAlternation(subnodes);
      case CONCATENATION: return drawConcatenation(subnodes);
      case KLEENE: 
        final Image kleene = drawNode(subnodes.getChildren().get(0).getContent());
        final BufferedImage kleeneRet = getImage(kleene.getWidth(null) + 10, kleene.getHeight(null));
        final Graphics2D g = kleeneRet.createGraphics();
        g.drawImage(kleene, 0, 0, null);
        g.setColor(Color.black);
        g.drawString("*", kleene.getWidth(null), 10);
        return kleeneRet;
      default: throw new IllegalArgumentException();
    }
  }

  private Image drawAlternation(final Regexp<AbstractNode> subnodes) {
    if (subnodes.getChildren().size() == 0) {
      return null;
    }
    
    final List<Image> altImgs = new ArrayList<Image>(subnodes.getChildren().size());
    int width = 0;
    int height = 0;
    for (final Regexp<AbstractNode> child : subnodes.getChildren()) {
      final Image childImg = drawRegexp(child);
      if (childImg != null) {
        altImgs.add(childImg);
        width = Math.max(width, childImg.getWidth(null));
        height += childImg.getHeight(null) + 1;
      }
    }
    final BufferedImage altRet = getImage(width, height);
    final Graphics2D g = altRet.createGraphics();
    int offset = 0;
    for (final Image img : altImgs) {
      g.drawImage(img, 0, offset, null);
      offset += img.getHeight(null) + 1;
    }
    return altRet;
  }

  private Image drawConcatenation(final Regexp<AbstractNode> subnodes) {
    if (subnodes.getChildren().size() == 0) {
      return null;
    }
    
    final List<Image> concatImgs = new ArrayList<Image>(subnodes.getChildren().size());
    int width = 0;
    int height = 0;
    for (final Regexp<AbstractNode> child : subnodes.getChildren()) {
      final Image childImg = drawRegexp(child);
      if (childImg != null) {
        concatImgs.add(childImg);
        width += childImg.getWidth(null) + 1;
        height = Math.max(height, childImg.getHeight(null));
      }
    }
    final BufferedImage concatRet = getImage(width, height);
    final Graphics2D g = concatRet.createGraphics();
    int offset = 0;
    for (final Image img : concatImgs) {
      g.drawImage(img, offset, 0, null);
      offset += img.getWidth(null) + 1;
    }
    return concatRet;
  }

  private BufferedImage getImage(final int width, final int height) {
    final BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g = ret.createGraphics();
    g.setColor(UIManager.getDefaults().getColor("Panel.background"));
    g.fillRect(0, 0, width - 1, height - 1);
    return ret;
  }

}
