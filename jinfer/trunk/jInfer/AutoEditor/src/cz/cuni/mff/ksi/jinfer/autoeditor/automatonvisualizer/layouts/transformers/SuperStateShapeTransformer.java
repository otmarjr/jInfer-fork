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

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import java.awt.Shape;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author sviro
 */
public class SuperStateShapeTransformer<T> implements Transformer<State<T>, Shape> {

  private final State<T> finalState;
  private final Transformer<State<T>, Shape> defaultTransformer;
  private final State<T> initialState;
  private final VertexShapeFactory<State<T>> vertexShapeFactory;
  public static final int NODE_SIZE = 20;

  public SuperStateShapeTransformer(final State<T> initialState, final State<T> finalState, final Transformer<State<T>, Shape> defaultTransformer) {
    this.finalState = finalState;
    this.defaultTransformer = defaultTransformer;
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
    if (state.equals(initialState) || state.equals(finalState)) {
      return vertexShapeFactory.getRoundRectangle(state);
    }
    return defaultTransformer.transform(state);
  }
}
