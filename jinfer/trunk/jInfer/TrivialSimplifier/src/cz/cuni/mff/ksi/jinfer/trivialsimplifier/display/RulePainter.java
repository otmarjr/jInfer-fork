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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import org.openide.util.Exceptions;

/**
 * Class responsible for rendering a set of rules to a canvas.
 *
 * @author vektor
 */
public class RulePainter {  

  private final Component root;
  private Image image;
  private Image lambda = null;

  public RulePainter(final Component root) {
    this.root = root;
    try {
      lambda = ImageIO.read(RulePainter.class.getClassLoader().getResource("cz/cuni/mff/ksi/jinfer/trivialsimplifier/graphics/lambda.png"));
    } catch (final IOException ex) {
    }
  }

  /**
   * Set the rules this painter will render.
   */
  public void setRules(final List<AbstractNode> rules) {
    if (rules == null) {
      return;
    }

    final List<Image> ruleImgs = new ArrayList<Image>(rules.size());
    int width = 0;
    int height = 0;
    for (final AbstractNode a : rules) {
      final Image i = drawNode(a);
      ruleImgs.add(i);
      width = Math.max(width, i.getWidth(null));
      height += i.getHeight(null) + 2;
    }


    final BufferedImage img = Utils.getImage(width, height);    
    int offset = 0;
    final Graphics2D g = img.createGraphics();
    for (final Image i : ruleImgs) {
      g.drawImage(i, 0, offset, null);
      offset += i.getHeight(null) + 2;
    }
    
    this.image = img;
    root.repaint();
  }
  
  public void paint(final Graphics2D g) {
    g.drawImage(image, 0, 0, null);
  }

  private Image drawNode(final AbstractNode n) {
    final FontMetrics fm = root.getGraphics().getFontMetrics();    

    final int nameWidth = (int)(fm.stringWidth(n.getName()) * 1.5);
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
      g.drawLine(nameWidth, fm.getHeight() / 2, nameWidth + 10, fm.getHeight() / 2);
      g.drawLine(nameWidth + 5, fm.getHeight() / 4, nameWidth + 10, fm.getHeight() / 2);
      g.drawLine(nameWidth + 5, (3 * fm.getHeight()) / 4, nameWidth + 10, fm.getHeight() / 2);
      g.drawImage(children, nameWidth + 10, 4, null);
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
      case KLEENE: return drawKleene(subnodes);
      default: throw new IllegalArgumentException();
    }
  }

  private Image drawKleene(final Regexp<AbstractNode> subnodes) {
    final Image kleene = drawNode(subnodes.getChildren().get(0).getContent());
    final BufferedImage kleeneRet = Utils.getImage(kleene.getWidth(null) + 10, kleene.getHeight(null));
    final Graphics2D g = kleeneRet.createGraphics();
    g.drawImage(kleene, 0, 0, null);
    g.setColor(Color.black);
    g.drawString("*", kleene.getWidth(null), 10);
    return kleeneRet;
  }

  private List<Image> getChildrenImages(final List<Regexp<AbstractNode>> children) {
    final List<Image> ret = new ArrayList<Image>(children.size());
    for (final Regexp<AbstractNode> child : children) {
      final Image childImg = drawRegexp(child);
      if (childImg != null) {
        ret.add(childImg);
      }
      else {
        ret.add(lambda);
      }
    }
    return ret;
  }

  private Image drawAlternation(final Regexp<AbstractNode> subnodes) {
    if (subnodes.getChildren().size() == 0) {
      return null;
    }
    
    final List<Image> altImgs = getChildrenImages(subnodes.getChildren());
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

  private Image drawConcatenation(final Regexp<AbstractNode> subnodes) {
    if (subnodes.getChildren().size() == 0) {
      return null;
    }
    
    final List<Image> concatImgs = getChildrenImages(subnodes.getChildren());
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

  

}
