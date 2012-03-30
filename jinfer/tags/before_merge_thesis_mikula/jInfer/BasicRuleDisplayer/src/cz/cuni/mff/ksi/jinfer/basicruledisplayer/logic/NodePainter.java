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

package cz.cuni.mff.ksi.jinfer.basicruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.basicruledisplayer.options.BasicRuleDisplayerPanel;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Class for painting XML nodes.
 *
 * @author vektor
 */
public class NodePainter {

  /** Maximum nesting level. */
  private final int maxLevel;

  private final List<AbstractStructuralNode> visited = new ArrayList<AbstractStructuralNode>();

  private final Graphics2D graphics;

  private final int margin;

  /**
   * Full constructor.
   *
   * @param graphics Graphics object used to get font metrics.
   */
  public NodePainter(final Graphics2D graphics) {
    final Preferences p = NbPreferences.forModule(BasicRuleDisplayerPanel.class);
    this.maxLevel = p.getInt(BasicRuleDisplayerPanel.NESTING_LEVEL_PROP, BasicRuleDisplayerPanel.NESTING_LEVEL_DEFAULT);
    this.margin = p.getInt(BasicRuleDisplayerPanel.MARGIN_PROP, BasicRuleDisplayerPanel.MARGIN_DEFAULT);
    this.graphics = graphics;
  }

  /**
   * Renders an abstract node and returns its image representation.
   *
   * @param r Node to be rendered. Must not be null.
   * @param level Level of recursion - how deep in the tree are we.
   * @return Image representation of this node. If the level is higher that the
   * threshold from configuration, an image of three dots is returned.
   */
  public Image drawNode(final Regexp<AbstractStructuralNode> r, final int level) {
    if (!r.isToken()) {
      throw new IllegalArgumentException("Can only draw TOKENs.");
    }

    if (level > maxLevel) {
      return Utils.DOTS;
    }

    final FontMetrics fm = graphics.getFontMetrics();

    final String label = r.getContent().getName() + r.getInterval().toString();

    final int labelWidth = (int)(fm.stringWidth(label) * 1.5);
    final int labelHeight = fm.getHeight() - fm.getDescent();

    final Image children = isVisited(r.getContent()) ? Utils.ARROW : drawSubnodes(r.getContent(), level + 1);
    if (r.getContent().isElement()) {
      visited.add(r.getContent());
    }

    final int width;
    final int height;

    if (children != null) {
      width =  Math.max(labelWidth, Utils.getWidth(children)) + 2 * margin;
      height = Utils.getHeight(children) + fm.getHeight() + 3 * margin;
    }
    else {
      width = labelWidth + 2 * margin;
      height = fm.getHeight() + 2 * margin;
    }

    final BufferedImage ret = Utils.getImage(width, height, Utils.getNodeColor(r.getContent()));
    final Graphics2D g = ret.createGraphics();

    g.setColor(Utils.getColorBackground());
    g.drawString(label, margin, labelHeight + margin);
    g.setColor(Utils.getColorForeground());
    g.drawRect(0, 0, width - 1, height - 1);

    if (children != null) {
      g.drawImage(children, margin, fm.getHeight() + 2 * margin, null);
    }
    return ret;
  }

  private Image drawSubnodes(final AbstractStructuralNode n, final int level) {
    if (!n.isElement()) {
      return null;
    }
    final Element e = (Element) n;
    final Image imgSubnodes;
    final Image imgAttrs;

    if (e.getSubnodes() != null) {
      imgSubnodes = drawRegexp(e.getSubnodes(), level + 1);
    } else {
      imgSubnodes = null;
    }
    if (!BaseUtils.isEmpty(e.getAttributes())) {
      imgAttrs = drawAttributes(e.getAttributes());
    }
    else {
      imgAttrs = null;
    }
    if (imgSubnodes == null && imgAttrs == null) {
      return null;
    }

    final BufferedImage ret = Utils.getImage(
            Math.max(Utils.getWidth(imgSubnodes), Utils.getWidth(imgAttrs)),
            Utils.getHeight(imgSubnodes) + Utils.getHeight(imgAttrs),
            Utils.getColorElement());
    final Graphics2D g = ret.createGraphics();
    if (imgAttrs != null) {
      g.drawImage(imgAttrs, 0, 0, null);
    }
    if (imgSubnodes != null) {
      g.drawImage(imgSubnodes, 0, Utils.getHeight(imgAttrs), null);
    }

    return ret;
  }

  private Image drawAttributes(final List<Attribute> attributes) {
    final List<Image> attrImages = getAttrImages(attributes);
    int width = 0;
    int height = 0;
    for (final Image img : attrImages) {
      width += Utils.getWidth(img) + margin;
      height = Math.max(height, Utils.getHeight(img));
    }
    final BufferedImage concatRet = Utils.getImage(width + margin,
            height + 2 * margin,
            Utils.getColorElement());
    final Graphics2D g = concatRet.createGraphics();

    int offset = margin;
    for (final Image img : attrImages) {
      g.drawImage(img, offset, margin, null);
      offset += Utils.getWidth(img) + margin;
    }
    return concatRet;
  }

  private List<Image> getAttrImages(final List<Attribute> attributes) {
    final List<Image> ret = new ArrayList<Image>(attributes.size());
    int count = 0;
    for (final Attribute a : attributes) {
      ret.add(drawAttribute(a));
      if (count >= maxLevel) {
        ret.add(Utils.DOTS);
        return ret;
      }
      count++;
    }
    return ret;
  }

  private Image drawAttribute(final Attribute a) {
    final FontMetrics fm = graphics.getFontMetrics();

    final boolean required = a.getMetadata().containsKey(IGGUtils.REQUIRED);

    final String label = a.getName() + (required ? " (REQUIRED)" : "");

    final int labelWidth = (int)(fm.stringWidth(label) * 1.5);
    final int labelHeight = fm.getHeight() - fm.getDescent();

    final int width = labelWidth + 2 * margin;
    final int height = fm.getHeight() + 2 * margin;

    final BufferedImage ret = Utils.getImage(width, height);
    final Graphics2D g = ret.createGraphics();

    g.setColor(Utils.getColorAttribute());
    g.fillRect(0, 0, width, height);
    g.setColor(Utils.getColorBackground());
    g.drawString(label, margin, labelHeight + margin);
    g.setColor(Utils.getColorForeground());
    g.drawRect(0, 0, width - 1, height - 1);

    return ret;
  }

  private Image drawRegexp(final Regexp<AbstractStructuralNode> subnodes, final int level) {
    if (level > maxLevel) {
      return Utils.DOTS;
    }
    switch (subnodes.getType()) {
      case LAMBDA: return Utils.LAMBDA;
      case TOKEN: return drawNode(subnodes, level + 1);
      case ALTERNATION: return drawAlternation(subnodes, level + 1);
      case CONCATENATION: return drawConcatenation(subnodes, level + 1);
      default: throw new IllegalArgumentException();
    }
  }

  private List<Image> getChildrenImages(final List<Regexp<AbstractStructuralNode>> children, final int level) {
    final List<Image> ret = new ArrayList<Image>(children.size());
    int count = 0;
    for (final Regexp<AbstractStructuralNode> child : children) {
      if (child.isLambda()) {
        ret.add(Utils.LAMBDA);
      }
      else {
        ret.add(drawRegexp(child, level + 1));
      }
      if (count >= maxLevel) {
        ret.add(Utils.DOTS);
        return ret;
      }
      count++;
    }
    return ret;
  }

  private Image drawAlternation(final Regexp<AbstractStructuralNode> subnodes, final int level) {
    if (BaseUtils.isEmpty(subnodes.getChildren())) {
      return null;
    }

    final List<Image> altImgs = getChildrenImages(subnodes.getChildren(), level + 1);
    int width = 0;
    int height = 0;
    for (final Image img : altImgs) {
      width = Math.max(width, Utils.getWidth(img));
      height += Utils.getHeight(img) + margin;
    }
    final BufferedImage altRet = Utils.getImage(width + 2 * margin,
            height + margin,
            Utils.getColorAlternation());
    final Graphics2D g = altRet.createGraphics();

    int offset = margin;
    for (final Image img : altImgs) {
      g.drawImage(img, margin, offset, null);
      offset += Utils.getHeight(img) + margin;
    }
    return altRet;
  }

  private Image drawConcatenation(final Regexp<AbstractStructuralNode> subnodes, final int level) {
    if (BaseUtils.isEmpty(subnodes.getChildren())) {
      return null;
    }

    final List<Image> concatImgs = getChildrenImages(subnodes.getChildren(), level + 1);
    int width = 0;
    int height = 0;
    for (final Image img : concatImgs) {
      width += Utils.getWidth(img) + margin;
      height = Math.max(height, Utils.getHeight(img));
    }
    final BufferedImage concatRet = Utils.getImage(width + margin,
            height + 2 * margin,
            Utils.getColorConcatenation());
    final Graphics2D g = concatRet.createGraphics();

    int offset = margin;
    for (final Image img : concatImgs) {
      g.drawImage(img, offset, margin, null);
      offset += Utils.getWidth(img) + margin;
    }
    return concatRet;
  }

  private boolean isVisited(final AbstractStructuralNode n) {
    for (final AbstractStructuralNode v : visited) {
      if (v == n) {
        return true;
      }
    }
    return false;
  }

}
