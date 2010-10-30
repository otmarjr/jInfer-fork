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

package cz.cuni.mff.ksi.jinfer.autoeditor.vyhnanovska;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Dimension;

/**
 *
 * @author rio
 * TODO rio comment & refactor
 */
public class LayoutFactory {
  
  public static <T> Layout<State<T>, Step<T>> createLayout(Automaton<T> automaton, Graph<State<T>, Step<T>> graph) {
    final int MIN_X_SIZE = 7;
    final int MIN_Y_SIZE = 3;
    final int SQUARE_SIZE = 100;
    final AutomatonLayoutTransformer<T> automatonLayoutTransformer = new AutomatonLayoutTransformer<T>(
            MIN_X_SIZE, MIN_Y_SIZE, SQUARE_SIZE, graph, automaton);
    final Dimension dimension = automatonLayoutTransformer.getDimension();
    final Layout<State<T>, Step<T>> layout = new StaticLayout<State<T>, Step<T>>(graph,
            automatonLayoutTransformer, dimension);
    return layout;
  }
}
