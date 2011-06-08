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
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.basicruledisplayer.options.BasicRuleDisplayerPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbPreferences;

/**
 * Class responsible for rendering a set of rules to a canvas.
 *
 * @author vektor
 */
public class RulePainter {

  /** Maximum rules drawn (in each cluster). */
  private final int maxRules = NbPreferences.forModule(BasicRuleDisplayerPanel.class)
      .getInt(BasicRuleDisplayerPanel.MAX_RULES_PROP, BasicRuleDisplayerPanel.MAX_RULES_DEFAULT);
  private final Component root;
  private Image image;

  /**
   * Full constructor.
   *
   * @param root Root component, in which rules will be rendered.
   */
  public RulePainter(final Component root) {
    this.root = root;
  }

  /**
   * Set the rules this painter will render.
   */
  public void setRules(final List<Element> rules) {
    this.image = drawRules(rules);
    root.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
    root.repaint();
  }

  /**
   * Paint the rendered image to the specified graphics object.
   *
   * @param g Graphics object to paint rendered rules to.
   */
  public void paint(final Graphics2D g) {
    g.setColor(Utils.getColorBackground());
    g.fillRect(0, 0, root.getWidth(), root.getHeight());
    g.drawImage(image, 0, 0, null);
  }

  private Image drawRules(final List<Element> rules) {
    if (rules == null) {
      return null;
    }

    final List<Element> rulesUsed;
    final boolean rulesTrimmed;
    if (rules.size() > maxRules) {
      rulesUsed = rules.subList(0, maxRules);
      rulesTrimmed = true;
    } else {
      rulesUsed = rules;
      rulesTrimmed = false;
    }

    final List<Image> ruleImgs = new ArrayList<Image>(rules.size());
    int width = 0;
    int height = 0;

    final NodePainter np = new NodePainter((Graphics2D) root.getGraphics());

    for (final AbstractStructuralNode a : rulesUsed) {
      final Image i = np.drawNode(Regexp.getToken(a, RegexpInterval.getOnce()), 0);
      ruleImgs.add(i);
      width = Math.max(width, i.getWidth(null));
      height += i.getHeight(null) + 2;
    }

    if (rulesTrimmed) {
      ruleImgs.add(Utils.DOTS);
      height += Utils.DOTS.getHeight(null) + 2;
    }

    final BufferedImage ret = Utils.getImage(width, height);
    int offset = 0;
    final Graphics2D g = ret.createGraphics();
    for (final Image i : ruleImgs) {
      g.drawImage(i, 0, offset, null);
      offset += i.getHeight(null) + 2;
    }

    return ret;
  }
}
