/*
 *  Copyright (C) 2010 rio
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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.statespickingvisualizer.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author rio
 * TODO rio comment
 */
public class GridLayout<T> extends LayoutHolder<T> {

  private Automaton<T> automaton;
  private SymbolToString<T> symbolToString;

  public GridLayout(final Automaton<T> automaton, SymbolToString<T> symbolToString) {
    this.automaton = automaton;
    this.symbolToString = symbolToString;
  }

   @Override
  public Layout<State<T>, Step<T>> getLayout() {
    return cz.cuni.mff.ksi.jinfer.autoeditor.vyhnanovska.LayoutFactory.createLayout(automaton, createGraph(automaton));
  }

  // TODO rio remove this and use Transformer instead of SymbolToString
  @Override
  public Transformer<Step<T>, String> getEdgeLabelTransformer() {
    return new Transformer<Step<T>, String>() {

      @Override
      public String transform(Step<T> step) {
        return symbolToString.toString(step.getAcceptSymbol());
      }
    };
  }
}
