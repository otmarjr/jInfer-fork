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

import cz.cuni.mff.ksi.jinfer.autoeditor.options.ColorUtils;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for automaton vertex color.
 * @author sviro
 */
public class NodeColorTransformer<T> implements Transformer<State<T>, Paint> {

  private final Color color;
  private final Color finalColor;
  private final Color pickedColor;
  private final PickedState<State<T>> pickedState;

  public NodeColorTransformer(final PickedState<State<T>> pickedState) {
    this.color = ColorUtils.getColorProperty(ColorUtils.NODE_COLOR_PROP, ColorUtils.NODE_COLOR_DEFAULT);
    this.finalColor = ColorUtils.getColorProperty(ColorUtils.FINAL_COLOR_PROP, ColorUtils.FINAL_COLOR_DEFAULT);
    this.pickedColor = ColorUtils.getColorProperty(ColorUtils.PICKED_COLOR_PROP, ColorUtils.PICKED_COLOR_DEFAULT);
    this.pickedState = pickedState;
  }

  @Override
  public Paint transform(final State<T> state) {
    if (pickedState != null && pickedState.isPicked(state)) {
      return pickedColor;
    }


    if (state.getFinalCount() > 0) {
      return finalColor;
    }
    
    return color;
  }
}
