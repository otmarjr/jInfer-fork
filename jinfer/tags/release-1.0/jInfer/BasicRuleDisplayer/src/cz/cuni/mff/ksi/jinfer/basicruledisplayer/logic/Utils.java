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
import org.openide.util.NbPreferences;

/**
 * Some rule painter utils.
 * 
 * @author vektor
 */
public final class Utils {

  private Utils() { }

  public static final Color COLOR_ELEMENT = Color.gray;
  public static final Color COLOR_ATTRIBUTE = Color.blue;
  public static final Color COLOR_SIMPLE_DATA = Color.black;
  public static final Color COLOR_CONCATENATION = Color.red;
  public static final Color COLOR_ALTERNATION = Color.yellow;

  public static final Image LAMBDA = loadStatic("cz/cuni/mff/ksi/jinfer/basicruledisplayer/graphics/lambda.png");
  public static final Image DOTS = loadStatic("cz/cuni/mff/ksi/jinfer/basicruledisplayer/graphics/dots.png");
  public static final Image ARROW = loadStatic("cz/cuni/mff/ksi/jinfer/basicruledisplayer/graphics/arrow.png");

  /**
   * Returns the background color for an element from the options.
   * 
   * @return Background color for an element.
   */
  public static Color getColorElement() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get("element.color", String.valueOf(COLOR_ELEMENT.getRGB())));
  }

  /**
   * Returns the background color for an attribute from the options.
   *
   * @return Background color for an attribute.
   */
  public static Color getColorAttribute() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get("attribute.color", String.valueOf(COLOR_ATTRIBUTE.getRGB())));
  }

  /**
   * Returns the background color for simple data from the options.
   *
   * @return Background color for simple data.
   */
  public static Color getColorSimpleData() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get("simple.data.color", String.valueOf(COLOR_SIMPLE_DATA.getRGB())));
  }

  /**
   * Returns the background color for a concatenation from the options.
   *
   * @return Background color for a concatenation.
   */
  public static Color getColorConcatenation() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get("concatenation.color", String.valueOf(COLOR_CONCATENATION.getRGB())));
  }

  /**
   * Returns the background color for an alternation from the options.
   *
   * @return Background color for an alternation.
   */
  public static Color getColorAlternation() {
    return Color.decode(NbPreferences.forModule(BasicRuleDisplayerPanel.class).get("alternation.color", String.valueOf(COLOR_ALTERNATION.getRGB())));
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
      return ImageIO.read(Utils.class.getClassLoader().getResource(url));
    } catch (IOException ex) {
      return null;
    }
  }
}
