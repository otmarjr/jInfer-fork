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
import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author sviro
 */
public class NodeColorTransformer<T> implements Transformer<State<T>, Paint> {

  private final Color color;

  public NodeColorTransformer() {
    this.color = ColorUtils.getColorProperty(ColorUtils.NODE_COLOR_PROP, ColorUtils.NODE_COLOR_DEFAULT);
  }

  @Override
  public Paint transform(State<T> state) {
    return color;
  }
}
