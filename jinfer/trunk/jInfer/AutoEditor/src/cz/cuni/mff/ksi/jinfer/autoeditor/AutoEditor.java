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

package cz.cuni.mff.ksi.jinfer.autoeditor;

import cz.cuni.mff.ksi.jinfer.autoeditor.gui.AutoEditorTopComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.openide.windows.WindowManager;

/**
 *
 * @author rio
 * TODO rio comment
 */
public class AutoEditor {

  public static void drawAutomatonAsync(final Automaton<AbstractNode> automaton) {
    final DirectedSparseGraph<State<AbstractNode>, Step<AbstractNode>> graph = new DirectedSparseGraph<State<AbstractNode>, Step<AbstractNode>>();
    final Map<State<AbstractNode>, Set<Step<AbstractNode>>> delta = automaton.getDelta();

    // vrcholy
    for (Entry<State<AbstractNode>, Set<Step<AbstractNode>>> entry : delta.entrySet()) {
      graph.addVertex(entry.getKey());
    }

    // hrany
    for (Entry<State<AbstractNode>, Set<Step<AbstractNode>>> entry : delta.entrySet()) {
      for (Step<AbstractNode> step : entry.getValue()) {
        graph.addEdge(step, step.getSource(), step.getDestination());
      }
    }

    final Layout<State<AbstractNode>, Step<AbstractNode>> layout = new CircleLayout<State<AbstractNode>, Step<AbstractNode>>(graph);
    //layout.setSize(new Dimension(300,300)); // sets the initial size of the space
    // The BasicVisualizationServer<V,E> is parameterized by the edge types
    final VisualizationViewer<State<AbstractNode>, Step<AbstractNode>> vv = new VisualizationViewer<State<AbstractNode>, Step<AbstractNode>>(layout);
    //vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<State<AbstractNode>>());
    vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Step<AbstractNode>>());

    final DefaultModalGraphMouse<State<AbstractNode>, Step<AbstractNode>> gm = new DefaultModalGraphMouse<State<AbstractNode>, Step<AbstractNode>>();
    gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
    vv.setGraphMouse(gm);
    
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        AutoEditorTopComponent.findInstance().drawAutomatonBasicVisualizationServer(vv);
      }
    });
  }
}
