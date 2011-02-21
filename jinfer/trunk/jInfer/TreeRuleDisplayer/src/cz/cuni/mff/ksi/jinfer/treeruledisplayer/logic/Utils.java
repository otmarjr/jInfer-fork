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
package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.VertexShape;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import cz.cuni.mff.ksi.jinfer.treeruledisplayer.options.TreeRuleDisplayerPanel;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import java.awt.Color;
import java.awt.Shape;
import java.util.List;
import org.openide.util.NbPreferences;

/**
 * Some Rule Displayer utils.
 * @author sviro
 */
public class Utils {

  /**
   * Background color property name.
   */
  public static final String BG_COLOR_PROP = "background.color";
  /**
   * Background color default value.
   */
  public static final Color BG_COLOR_DEFAULT = Color.decode("-1");
  /**
   * Horizontal distance of vertices property name.
   */
  public static final String HORIZONTAL_DISTANCE_PROP = "horizontal.distance";
  /**
   * Horizontal distance of vertices default value.
   */
  public static final int HORIZONTAL_DISTANCE_DEFAULT = 150;
  /**
   * Vertical distance of vertices property name.
   */
  public static final String VERTICAL_DISTANCE_PROP = "vertical.distance";
  /**
   * Vertical distance of vertices default value.
   */
  public static final int VERTICAL_DISTANCE_DEFAULT = 100;
  /**
   * Shape of root vertex property name.
   */
  public static final String ROOT_SHAPE_PROP = "root.shape";
  /**
   * Shape of root vertex default value.
   */
  public static final VertexShape ROOT_SHAPE_DEFAULT = VertexShape.CIRCLE;
  /**
   * Shape of element vertex property name.
   */
  public static final String ELEMENT_SHAPE_PROP = "token.shape";
  /**
   * Shape of element vertex default value.
   */
  public static final VertexShape ELEMENT_SHAPE_DEFAULT = VertexShape.ROUNDED_SQUARE;
  /**
   * Shape of concatenation vertex property name.
   */
  public static final String CONCAT_SHAPE_PROP = "concat.shape";
  /**
   * Shape of concatenation vertex default value.
   */
  public static final VertexShape CONCAT_SHAPE_DEFAULT = VertexShape.POLYGON;
  /**
   * Shape of alternation vertex property name.
   */
  public static final String ALTER_SHAPE_PROP = "alter.shape";
  /**
   * Shape of alternation vertex default value.
   */
  public static final VertexShape ALTER_SHAPE_DEFAULT = VertexShape.SQUARE;
  /**
   * Shape of permutation vertex property name.
   */
  public static final String PERMUT_SHAPE_PROP = "permut.shape";
  /**
   * Shape of permutation vertex default value.
   */
  public static final VertexShape PERMUT_SHAPE_DEFAULT = VertexShape.STAR;
  /**
   * Shape of lambda vertex property name.
   */
  public static final String LAMBDA_SHAPE_PROP = "lambda.shape";
  /**
   * Shape of lambda vertex default value.
   */
  public static final VertexShape LAMBDA_SHAPE_DEFAULT = VertexShape.CIRCLE;
  /**
   * Shape of simple data vertex property name.
   */
  public static final String SIMPLE_DATA_SHAPE_PROP = "simpleData.shape";
  /**
   * Shape of simple data vertex default value.
   */
  public static final VertexShape SIMPLE_DATA_SHAPE_DEFAULT = VertexShape.SQUARE;
  /**
   * Shape of attribute vertex property name.
   */
  public static final String ATTRIBUTE_SHAPE_PROP = "attribute.shape";
  /**
   * Shape of attribute vertex default value.
   */
  public static final VertexShape ATTRIBUTE_SHAPE_DEFAULT = VertexShape.STAR;
  /**
   * Size of root vertex property name.
   */
  public static final String ROOT_SIZE_PROP = "root.size";
  /**
   * Size of root vertex default value.
   */
  public static final int ROOT_SIZE_DEFAULT = 30;
  /**
   * Size of element vertex property name.
   */
  public static final String ELEMENT_SIZE_PROP = "token.size";
  /**
   * Size of element vertex default value.
   */
  public static final int ELEMENT_SIZE_DEFAULT = 20;
  /**
   * Size of concatenation vertex property name.
   */
  public static final String CONCAT_SIZE_PROP = "concat.size";
  /**
   * Size of concatenation vertex default value.
   */
  public static final int CONCAT_SIZE_DEFAULT = 25;
  /**
   * Size of alternation vertex property name.
   */
  public static final String ALTER_SIZE_PROP = "alter.size";
  /**
   * Size of alternation vertex default value.
   */
  public static final int ALTER_SIZE_DEFAULT = 25;
  /**
   * Size of permutation vertex property name.
   */
  public static final String PERMUT_SIZE_PROP = "permut.size";
  /**
   * Size of permutation vertex default value.
   */
  public static final int PERMUT_SIZE_DEFAULT = 25;
  /**
   * Size of lambda vertex property name.
   */
  public static final String LAMBDA_SIZE_PROP = "lambda.size";
  /**
   * Size of lambda vertex default value.
   */
  public static final int LAMBDA_SIZE_DEFAULT = 20;
  /**
   * Size of simple data vertex property name.
   */
  public static final String SIMPLE_DATA_SIZE_PROP = "simpleData.size";
  /**
   * Size of simple data vertex default value.
   */
  public static final int SIMPLE_DATA_SIZE_DEFAULT = 20;
  /**
   * Size of attribute vertex property name.
   */
  public static final String ATTRIBUTE_SIZE_PROP = "attribute.size";
  /**
   * Size of attribute vertex default value.
   */
  public static final int ATTRIBUTE_SIZE_DEFAULT = 15;
  /**
   * Color of root vertex property name.
   */
  public static final String ROOT_COLOR_PROP = "root.color";
  /**
   * Color of root vertex default value.
   */
  public static final Color ROOT_COLOR_DEFAULT = Color.decode("-8362846");
  /**
   * Color of element vertex property name.
   */
  public static final String ELEMENT_COLOR_PROP = "token.color";
  /**
   * Color of element vertex default value.
   */
  public static final Color ELEMENT_COLOR_DEFAULT = Color.decode("-10837573");
  /**
   * Color of concatenation vertex property name.
   */
  public static final String CONCAT_COLOR_PROP = "concat.color";
  /**
   * Color of concatenation vertex default value.
   */
  public static final Color CONCAT_COLOR_DEFAULT = Color.decode("-1936099");
  /**
   * Color of alternation vertex property name.
   */
  public static final String ALTER_COLOR_PROP = "alter.color";
  /**
   * Color of alternation vertex default value.
   */
  public static final Color ALTER_COLOR_DEFAULT = Color.decode("-4173747");
  /**
   * Color of permutation vertex property name.
   */
  public static final String PERMUT_COLOR_PROP = "permut.color";
  /**
   * Color of permutation vertex default value.
   */
  public static final Color PERMUT_COLOR_DEFAULT = Color.decode("-8473082");
  /**
   * Color of lambda vertex property name.
   */
  public static final String LAMBDA_COLOR_PROP = "lambda.color";
  /**
   * Color of lambda vertex default value.
   */
  public static final Color LAMBDA_COLOR_DEFAULT = Color.decode("-8355712");
  /**
   * Color of simple data vertex property name.
   */
  public static final String SIMPLE_DATA_COLOR_PROP = "simpleData.color";
  /**
   * Color of simple data vertex default value.
   */
  public static final Color SIMPLE_DATA_COLOR_DEFAULT = Color.decode("-14726787");
  /**
   * Color of attribute vertex property name.
   */
  public static final String ATTRIBUTE_COLOR_PROP = "attribute.color";
  /**
   * Color of attribute vertex default value.
   */
  public static final Color ATTRIBUTE_COLOR_DEFAULT = Color.decode("-16744448");
  public static final int VERTEX_LABEL_MAX_LENGHT = 15;
  private final VertexShape rootShape;
  private final VertexShape elementShape;
  private final VertexShape concatShape;
  private final VertexShape alterShape;
  private final VertexShape permutShape;
  private final VertexShape lambdaShape;
  private final VertexShape simpleDataShape;
  private final VertexShape attributeShape;
  private final Color bgColor;
  private final int horizontalDistance;
  private final int verticalDistance;
  private final List<Regexp<AbstractStructuralNode>> roots;

  /**
   * Default contructor.
   * @param roots List of root Regexp of each rule tree.
   */
  public Utils(final List<Regexp<AbstractStructuralNode>> roots) {
    this.roots = roots;
    this.bgColor = getColorProperty(BG_COLOR_PROP, BG_COLOR_DEFAULT);
    this.horizontalDistance = getProperty(HORIZONTAL_DISTANCE_PROP, HORIZONTAL_DISTANCE_DEFAULT);
    this.verticalDistance = getProperty(VERTICAL_DISTANCE_PROP, VERTICAL_DISTANCE_DEFAULT);
    this.rootShape = getShapeProperty(ROOT_SHAPE_PROP, ROOT_SHAPE_DEFAULT);
    this.elementShape = getShapeProperty(ELEMENT_SHAPE_PROP, ELEMENT_SHAPE_DEFAULT);
    this.concatShape = getShapeProperty(CONCAT_SHAPE_PROP, CONCAT_SHAPE_DEFAULT);
    this.alterShape = getShapeProperty(ALTER_SHAPE_PROP, ALTER_SHAPE_DEFAULT);
    this.permutShape = getShapeProperty(PERMUT_SHAPE_PROP, PERMUT_SHAPE_DEFAULT);
    this.lambdaShape = getShapeProperty(LAMBDA_SHAPE_PROP, LAMBDA_SHAPE_DEFAULT);
    this.simpleDataShape = getShapeProperty(SIMPLE_DATA_SHAPE_PROP, SIMPLE_DATA_SHAPE_DEFAULT);
    this.attributeShape = getShapeProperty(ATTRIBUTE_SHAPE_PROP, ATTRIBUTE_SHAPE_DEFAULT);
  }

  /**
   * Get property value for particular property.
   * @param property Name of property for which to get value.
   * @param defaultValue Default value to be returned, if no value is saved for particular property.
   * @return Property value if exists for particular property, otherwise is returned defaultValue.
   */
  public static int getProperty(final String property, final int defaultValue) {
    return NbPreferences.forModule(TreeRuleDisplayerPanel.class).getInt(property, defaultValue);
  }

  /**
   * Get {@link VertexShape} property value for particular property.
   * @param property Name of property for which to get value.
   * @param defaultValue Default value to be returned, if no value is saved for particular property.
   * @return Property value if exists for particular property, otherwise is returned defaultValue.
   */
  public static VertexShape getShapeProperty(final String property, final VertexShape defaultValue) {
    return VertexShape.valueOf(NbPreferences.forModule(TreeRuleDisplayerPanel.class).get(property, defaultValue.name()));
  }

  /**
   * Get {@link Color} property value for particular property.
   * @param property Name of property for which to get value.
   * @param defaultValue Default value to be returned, if no value is saved for particular property.
   * @return Property value if exists for particular property, otherwise is returned defaultValue.
   */
  public static Color getColorProperty(final String property, final Color defaultValue) {
    return Color.decode(NbPreferences.forModule(TreeRuleDisplayerPanel.class).get(property, String.valueOf(defaultValue.getRGB())));
  }

  /**
   * Get {@link Shape} of Vertex for particular {@link Regexp}.
   * @param shapeFactory Shape facotry which creates shape for particular regexp.
   * @param regexp Regexp for which is shape returned.
   * @return Shape of Vertex for particular regexp.
   */
  public Shape getVertexShape(final VertexShapeFactory<Regexp<? extends AbstractNamedNode>> shapeFactory, final Regexp<? extends AbstractNamedNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        return getShape(lambdaShape, shapeFactory, regexp);
      case TOKEN:
        if (roots.contains(regexp)) {
          return getShape(rootShape, shapeFactory, regexp);
        } else if (regexp.getContent() instanceof AbstractStructuralNode) {
          if (((AbstractStructuralNode) regexp.getContent()).isSimpleData()) {
            return getShape(simpleDataShape, shapeFactory, regexp);
          } else {
            return getShape(elementShape, shapeFactory, regexp);
          }
        } else {
          return getShape(attributeShape, shapeFactory, regexp);
        }
      case ALTERNATION:
        return getShape(alterShape, shapeFactory, regexp);
      case CONCATENATION:
        return getShape(concatShape, shapeFactory, regexp);
      case PERMUTATION:
        return getShape(permutShape, shapeFactory, regexp);
      default:
        return null;
    }
  }

  private Shape getShape(final VertexShape shape, final VertexShapeFactory<Regexp<? extends AbstractNamedNode>> shapeFactory, final Regexp<? extends AbstractNamedNode> regexp) {
    switch (shape) {
      case CIRCLE:
        return shapeFactory.getEllipse(regexp);
      case SQUARE:
        return shapeFactory.getRectangle(regexp);
      case ROUNDED_SQUARE:
        return shapeFactory.getRoundRectangle(regexp);
      case POLYGON:
        return shapeFactory.getRegularPolygon(regexp, 5);
      case STAR:
        return shapeFactory.getRegularStar(regexp, 5);
      default:
        return null;
    }
  }

  /**
   * Get background color of canvas where rules are displayed.
   * @return Background color of canvas.
   */
  public Color getBackgroundColor() {
    return bgColor;
  }

  /**
   * Get horizontal distance between vertices in rule trees.
   * @return Horizontal distance between vertices in rule trees.
   */
  public int getHorizontalDistance() {
    return horizontalDistance;
  }

  /**
   * Get vertical distance between vertices in rule trees.
   * @return Vertical distance between vertices in rule trees.
   */
  public int getVerticalDistance() {
    return verticalDistance;
  }

  /**
   * Get list of regexp which are represented as root vertex in each rule tree.
   * @return list of regexp which are represented as root vertex in each rule tree.
   */
  public List<Regexp<AbstractStructuralNode>> getRoots() {
    return roots;
  }

  public static String getVertexLabel(final Regexp<? extends AbstractNamedNode> regexp, final boolean trim) {
    if (regexp == null) {
      return null;
    }
    String result;
    switch (regexp.getType()) {
      case LAMBDA:
        result = "\u03BB";
        break;
      case TOKEN:
        if (regexp.getContent() instanceof AbstractStructuralNode && ((AbstractStructuralNode) regexp.getContent()).isSimpleData()) {
          result = regexp.getContent().getName();
          return getTrimmedSimpleData(result, trim);
        }
        result = regexp.getContent().getName();
        break;
      case ALTERNATION:
      case CONCATENATION:
      case PERMUTATION:
        result = regexp.getType().toString();
        break;
      default:
        return null;
    }

    if (result != null) {
      result = result.trim();

      if (trim && result.trim().length() > VERTEX_LABEL_MAX_LENGHT) {
        result = result.trim().substring(0, VERTEX_LABEL_MAX_LENGHT) + "...";
      }
    }

    return result;
  }

  private static String getTrimmedSimpleData(final String text, final boolean trim) {
    String result;
    if (text != null) {
      result = text.trim();
      if (trim && result.length() > VERTEX_LABEL_MAX_LENGHT) {
        result = result.substring(0, VERTEX_LABEL_MAX_LENGHT) + "...";
      }
      return "\"" + result + "\"";
    } else {
      return null;
    }
  }
}
