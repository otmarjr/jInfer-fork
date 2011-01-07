/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.advancedruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.advancedruledisplayer.options.AdvancedRuleDisplayerPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import java.awt.Color;
import java.awt.Shape;
import org.openide.util.NbPreferences;

/**
 *
 * @author sviro
 */
public class Utils {

  public enum SHAPE_TYPE {

    ROOT, TOKEN, CONCAT, ALTER, PERMUT
  };
  public static final String BG_COLOR_PROP = "background.color";
  public static final Color BG_COLOR_DEFAULT = Color.white;
  public static final String HORIZONTAL_DISTANCE_PROP = "horizontal.distance";
  public static final int HORIZONTAL_DISTANCE_DEFAULT = 150;
  public static final String VERTICAL_DISTANCE_PROP = "vertical.distance";
  public static final int VERTICAL_DISTANCE_DEFAULT = 150;

  /*
   * Circle - 0
   * Square - 1
   * Rounded Square - 2
   * Polygon - 3
   * Star - 4
   */
  public static final String ROOT_SHAPE_PROP = "root.shape";
  public static final int ROOT_SHAPE_DEFAULT = 4;
  public static final String TOKEN_SHAPE_PROP = "token.shape";
  public static final int TOKEN_SHAPE_DEFAULT = 0;
  public static final String CONCAT_SHAPE_PROP = "concat.shape";
  public static final int CONCAT_SHAPE_DEFAULT = 2;
  public static final String ALTER_SHAPE_PROP = "alter.shape";
  public static final int ALTER_SHAPE_DEFAULT = 2;
  public static final String PERMUT_SHAPE_PROP = "permut.shape";
  public static final int PERMUT_SHAPE_DEFAULT = 2;
  public static final String ROOT_SIZE_PROP = "root.size";
  public static final int ROOT_SIZE_DEFAULT = 40;
  public static final String TOKEN_SIZE_PROP = "token.size";
  public static final int TOKEN_SIZE_DEFAULT = 40;
  public static final String CONCAT_SIZE_PROP = "concat.size";
  public static final int CONCAT_SIZE_DEFAULT = 40;
  public static final String ALTER_SIZE_PROP = "alter.size";
  public static final int ALTER_SIZE_DEFAULT = 40;
  public static final String PERMUT_SIZE_PROP = "permut.size";
  public static final int PERMUT_SIZE_DEFAULT = 40;
  public static final String ROOT_COLOR_PROP = "root.color";
  public static final Color ROOT_COLOR_DEFAULT = Color.cyan;
  public static final String TOKEN_COLOR_PROP = "token.color";
  public static final Color TOKEN_COLOR_DEFAULT = Color.blue;
  public static final String CONCAT_COLOR_PROP = "concat.color";
  public static final Color CONCAT_COLOR_DEFAULT = Color.red;
  public static final String ALTER_COLOR_PROP = "alter.color";
  public static final Color ALTER_COLOR_DEFAULT = Color.yellow;
  public static final String PERMUT_COLOR_PROP = "permut.color";
  public static final Color PERMUT_COLOR_DEFAULT = Color.green;
  private int rootShape;
  private int tokenShape;
  private int concatShape;
  private int alterShape;
  private int permutShape;
  private Color bgColor;
  private int horizontalDistance;
  private int verticalDistance;

  public Utils() {
    this.bgColor = getColorProperty(BG_COLOR_PROP, BG_COLOR_DEFAULT);
    this.horizontalDistance = getProperty(HORIZONTAL_DISTANCE_PROP, HORIZONTAL_DISTANCE_DEFAULT);
    this.verticalDistance = getProperty(VERTICAL_DISTANCE_PROP, VERTICAL_DISTANCE_DEFAULT);
    this.rootShape = getProperty(ROOT_SHAPE_PROP, ROOT_SHAPE_DEFAULT);
    this.tokenShape = getProperty(TOKEN_SHAPE_PROP, TOKEN_SHAPE_DEFAULT);
    this.concatShape = getProperty(CONCAT_SHAPE_PROP, CONCAT_SHAPE_DEFAULT);
    this.alterShape = getProperty(ALTER_SHAPE_PROP, ALTER_SHAPE_DEFAULT);
    this.permutShape = getProperty(PERMUT_SHAPE_PROP, PERMUT_SHAPE_DEFAULT);
  }

  public static int getProperty(String property, int defaultValue) {
    return NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).getInt(property, defaultValue);
  }

  public static Color getColorProperty(String property, Color defaultValue) {
    return Color.decode(NbPreferences.forModule(AdvancedRuleDisplayerPanel.class).get(property, String.valueOf(defaultValue.getRGB())));
  }

  public Shape getVertexShape(SHAPE_TYPE shapeType, VertexShapeFactory<Regexp<AbstractStructuralNode>> shapeFactory, Regexp<AbstractStructuralNode> regexp) {
    switch (shapeType) {
      case ROOT:
        return getShape(rootShape, shapeFactory, regexp);
      case TOKEN:
        return getShape(tokenShape, shapeFactory, regexp);
      case CONCAT:
        return getShape(concatShape, shapeFactory, regexp);
      case ALTER:
        return getShape(alterShape, shapeFactory, regexp);
      case PERMUT:
        return getShape(permutShape, shapeFactory, regexp);
      default:
        return null;
    }
  }

  private Shape getShape(int rootShape, VertexShapeFactory<Regexp<AbstractStructuralNode>> shapeFactory, Regexp<AbstractStructuralNode> regexp) {
    switch (rootShape) {
      case 0:
        return shapeFactory.getEllipse(regexp);
      case 1:
        return shapeFactory.getRectangle(regexp);
      case 2:
        return shapeFactory.getRoundRectangle(regexp);
      case 3:
        return shapeFactory.getRegularPolygon(regexp, 5);
      case 4:
        return shapeFactory.getRegularStar(regexp, 5);
      default:
        return null;
    }
  }

  public Color getBackgroundColor() {
    return bgColor;
  }

  public int getHorizontalDistance() {
    return horizontalDistance;
  }

  public int getVerticalDistance() {
    return verticalDistance;
  }
}
