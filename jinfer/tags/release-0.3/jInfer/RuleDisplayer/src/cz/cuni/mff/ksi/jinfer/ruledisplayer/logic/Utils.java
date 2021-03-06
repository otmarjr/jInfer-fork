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
package cz.cuni.mff.ksi.jinfer.ruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.ruledisplayer.options.RuleDisplayerPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.UIManager;

/**
 * Some rule painter utils.
 * 
 * @author vektor
 */
public final class Utils {

  private Utils() { }

  // TODO vektor Choose some sensible defaults
  public static final Color COLOR_ELEMENT = Color.gray;
  public static final Color COLOR_ATTRIBUTE = Color.blue;
  public static final Color COLOR_SIMPLE_DATA = Color.black;
  public static final Color COLOR_CONCATENATION = Color.red;
  public static final Color COLOR_ALTERNATION = Color.yellow;

  public static final Image LAMBDA = loadStatic("cz/cuni/mff/ksi/jinfer/ruledisplayer/graphics/lambda.png");
  public static final Image DOTS = loadStatic("cz/cuni/mff/ksi/jinfer/ruledisplayer/graphics/dots.png");
  public static final Image ARROW = loadStatic("cz/cuni/mff/ksi/jinfer/ruledisplayer/graphics/arrow.png");

  public static Color getColorElement() {
    return Color.decode(Preferences.userNodeForPackage(RuleDisplayerPanel.class).get("element.color", String.valueOf(COLOR_ELEMENT.getRGB())));
  }

  public static Color getColorAttribute() {
    return Color.decode(Preferences.userNodeForPackage(RuleDisplayerPanel.class).get("attribute.color", String.valueOf(COLOR_ATTRIBUTE.getRGB())));
  }

  public static Color getColorSimpleData() {
    return Color.decode(Preferences.userNodeForPackage(RuleDisplayerPanel.class).get("simple.data.color", String.valueOf(COLOR_SIMPLE_DATA.getRGB())));
  }

  public static Color getColorConcatenation() {
    return Color.decode(Preferences.userNodeForPackage(RuleDisplayerPanel.class).get("concatenation.color", String.valueOf(COLOR_CONCATENATION.getRGB())));
  }

  public static Color getColorAlternation() {
    return Color.decode(Preferences.userNodeForPackage(RuleDisplayerPanel.class).get("alternation.color", String.valueOf(COLOR_ALTERNATION.getRGB())));
  }

  public static Color getColorBackground() {
    return UIManager.getDefaults().getColor("TabbedPane.background");
  }

  public static Color getColorForeground() {
    return UIManager.getDefaults().getColor("TabbedPane.foreground");
  }

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
   * @color Background color of the image.
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
