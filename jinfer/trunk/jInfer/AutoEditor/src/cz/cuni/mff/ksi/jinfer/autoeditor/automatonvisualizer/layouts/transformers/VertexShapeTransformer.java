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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.transformers;

import cz.cuni.mff.ksi.jinfer.autoeditor.options.ShapeUtils;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.objects.VertexShape;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import java.awt.Shape;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for vertex node shape. Transformation is performed according to
 * values set in AutoEditor options.
 * @author sviro, rio
 */
public class VertexShapeTransformer<T> implements Transformer<State<T>, Shape> {

  private static final int POLYGON_SIDES_NUMBER = 5;
  private static final int STAR_POINTS_NUMBER = 5;

  private final State<T> finalState;
  private final State<T> initialState;
  private final VertexShapeFactory<State<T>> vertexShapeFactory;

  /** Default size of a node. */
  public static final int NODE_SIZE = 20;

  /**
   * Constructor for automaton without any superinitial and superfinal states.
   */
  public VertexShapeTransformer() {
    this(null, null);
  }

  /**
   * Constructor for automaton with superinitial and superfinal states.
   */
  public VertexShapeTransformer(final State<T> initialState, final State<T> finalState) {
    this.finalState = finalState;
    this.initialState = initialState;
    this.vertexShapeFactory = new VertexShapeFactory<State<T>>(new Transformer<State<T>, Integer>() {

      @Override
      public Integer transform(final State<T> state) {
        return NODE_SIZE;
      }
    }, new Transformer<State<T>, Float>() {

      @Override
      public Float transform(final State<T> state) {
        return 1f;
      }
    });
  }

  @Override
  public Shape transform(final State<T> state) {
    VertexShape shape;

    if (state.equals(initialState)) {
      shape = ShapeUtils.getShapeProperty(ShapeUtils.SUPERINITIAL_NODE_SHAPE_PROP, ShapeUtils.SUPERINITIAL_NODE_SHAPE_DEFAULT);
    } else if (state.equals(finalState)) {
      shape = ShapeUtils.getShapeProperty(ShapeUtils.SUPERFINAL_NODE_SHAPE_PROP, ShapeUtils.SUPERFINAL_NODE_SHAPE_DEFAULT);
    } else {
      shape = ShapeUtils.getShapeProperty(ShapeUtils.REGULAR_NODE_SHAPE_PROP, ShapeUtils.REGULAR_NODE_SHAPE_DEFAULT);
    }

    switch (shape) {
      case CIRCLE:
        return vertexShapeFactory.getEllipse(state);
      case POLYGON:
        return vertexShapeFactory.getRegularPolygon(state, POLYGON_SIDES_NUMBER);
      case ROUNDED_SQUARE:
        return vertexShapeFactory.getRoundRectangle(state);
      case SQUARE:
        return vertexShapeFactory.getRectangle(state);
      case STAR:
        return vertexShapeFactory.getRegularStar(state, STAR_POINTS_NUMBER);
      default:
        throw new IllegalStateException("Unknown enum member.");
    }
  }
}
