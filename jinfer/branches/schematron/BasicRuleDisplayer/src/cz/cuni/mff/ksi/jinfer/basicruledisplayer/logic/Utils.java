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
import cz.cuni.mff.ksi.jinfer.basicruledisplayer.options.BasicRuleDisplayerPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.openide.util.NbPreferences;

/**
 * Some rule painter utils.
 *
 * @author vektor
 */
public final class Utils {

  private Utils() { }

  private static final Logger LOG = Logger.getLogger(Utils.class);

  public static final Image LAMBDA = loadStatic("lambda.png");
  public static final Image DOTS = loadStatic("dots.png");
  public static final Image ARROW = loadStatic("arrow.png");

  /**
   * Returns the background color for an element from the options.
   *
   * @return Background color for an element.
   */
  public static Color getColorElement() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get(BasicRuleDisplayerPanel.COLOR_ELEMENT_PROP,
            String.valueOf(BasicRuleDisplayerPanel.COLOR_ELEMENT_DEFAULT.getRGB())));
  }

  /**
   * Returns the background color for an attribute from the options.
   *
   * @return Background color for an attribute.
   */
  public static Color getColorAttribute() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get(BasicRuleDisplayerPanel.COLOR_ATTRIBUTE_PROP,
            String.valueOf(BasicRuleDisplayerPanel.COLOR_ATTRIBUTE_DEFAULT.getRGB())));
  }

  /**
   * Returns the background color for simple data from the options.
   *
   * @return Background color for simple data.
   */
  public static Color getColorSimpleData() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get(BasicRuleDisplayerPanel.COLOR_SIMPLE_DATA_PROP,
            String.valueOf(BasicRuleDisplayerPanel.COLOR_SIMPLE_DATA_DEFAULT.getRGB())));
  }

  /**
   * Returns the background color for a concatenation from the options.
   *
   * @return Background color for a concatenation.
   */
  public static Color getColorConcatenation() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get(BasicRuleDisplayerPanel.COLOR_CONCATENATION_PROP,
            String.valueOf(BasicRuleDisplayerPanel.COLOR_CONCATENATION_DEFAULT.getRGB())));
  }

  /**
   * Returns the background color for an alternation from the options.
   *
   * @return Background color for an alternation.
   */
  public static Color getColorAlternation() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get(BasicRuleDisplayerPanel.COLOR_ALTERNATION_PROP,
            String.valueOf(BasicRuleDisplayerPanel.COLOR_ALTERNATION_DEFAULT.getRGB())));
  }

  /**
   * Returns the background color for the rule displayer.
   *
   * @return Background color for the rule displayer - taken from the background
   * color of a tabbed pane.
   */
  public static Color getColorBackground() {
    return UIManager.getDefaults().getColor("TabbedPane.background");
  }

  /**
   * Returns the foreground color for the rule displayer.
   *
   * @return Foreground color for the rule displayer - taken from the foreground
   * color of a tabbed pane.
   */
  public static Color getColorForeground() {
    return UIManager.getDefaults().getColor("TabbedPane.foreground");
  }

  /**
   * Return the background color for the specified node.
   *
   * @param n Node for which the background color should be found.
   * @return Background color based on the type of the node specified.
   */
  public static Color getNodeColor(final AbstractStructuralNode n) {
    switch (n.getType()) {
      case ELEMENT:
        return getColorElement();
      case SIMPLE_DATA:
        return getColorSimpleData();
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * Creates an image of specified dimension, filled with the background color.
   *
   * @param width Positive integer representing the width of the image.
   * @param height Positive integer representing the height of the image.
   * @return BufferedImage of specified dimension.
   */
  public static BufferedImage getImage(final int width, final int height) {
    return getImage(width, height, getColorBackground());
  }

  /**
   * Creates an image of specified dimension, filled with the specified color.
   *
   * @param width Positive integer representing the width of the image.
   * @param height Positive integer representing the height of the image.
   * @param color Background color of the image.
   * @return BufferedImage of specified dimension.
   */
  public static BufferedImage getImage(final int width, final int height,
          final Color color) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Image dimension must be positive integers.");
    }
    final BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g = ret.createGraphics();
    g.setColor(color);
    g.fillRect(0, 0, width, height);
    return ret;
  }

  /**
   * Returns the width of the specified image.
   *
   * @return Width of the image, 0 if it is <code>null</code>.
   */
  public static int getWidth(final Image image) {
    return image == null ? 0 : image.getWidth(null);
  }

  /**
   * Returns the height of the specified image.
   *
   * @return Height of the image, 0 if it is <code>null</code>.
   */
  public static int getHeight(final Image image) {
    return image == null ? 0 : image.getHeight(null);
  }

  private static Image loadStatic(final String url) {
    try {
      return ImageIO.read(Utils.class.getClassLoader()
              .getResource("cz/cuni/mff/ksi/jinfer/basicruledisplayer/graphics/" + url));
    } catch (IOException ex) {
      LOG.error("Problem while loading static image", ex);
      return null;
    }
  }
}
