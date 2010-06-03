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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import org.openide.util.Exceptions;

/**
 * Some rule painter utils.
 * 
 * @author vektor
 */
public final class Utils {

  private Utils() { }
  
  private static final Color COLOR_ELEMENT = Color.gray;
  private static final Color COLOR_ATTRIBUTE = Color.blue;
  private static final Color COLOR_SIMPLE_DATA = Color.red;

  public static final Image LAMBDA = loadStatic("cz/cuni/mff/ksi/jinfer/trivialsimplifier/graphics/lambda.png");
  public static final Image DOTS = loadStatic("cz/cuni/mff/ksi/jinfer/trivialsimplifier/graphics/dots.png");

  public static Color getNodeColor(final AbstractNode n) {
    switch (n.getType()) {
      case ELEMENT:
        return COLOR_ELEMENT;
      case ATTRIBUTE:
        return COLOR_ATTRIBUTE;
      case SIMPLE_DATA:
        return COLOR_SIMPLE_DATA;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static BufferedImage getImage(final int width, final int height) {
    final BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g = ret.createGraphics();
    g.setColor(UIManager.getDefaults().getColor("TabbedPane.background"));
    g.fillRect(0, 0, width, height);
    return ret;
  }

  public static void drawArrow(final Graphics2D g, final int x, final int height) {
    g.drawLine(x, height / 2, x + 10, height / 2);
    g.drawLine(x + 5, height / 4, x + 10, height / 2);
    g.drawLine(x + 5, (3 * height) / 4, x + 10, height / 2);
  }

  private static Image loadStatic(final String url) {
    try {
      return ImageIO.read(Utils.class.getClassLoader().getResource(url));
    } catch (IOException ex) {
      return null;
    }
  }
}
