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
import cz.cuni.mff.ksi.jinfer.autoeditor.monitor.Monitor;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
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
public class AutoEditor<T> {

  private final Monitor<Pair<State<T>, State<T>>> monitor;
  private VisualizationViewer<State<T>, Step<T>> vv;

  public AutoEditor() {
    monitor = new Monitor<Pair<State<T>, State<T>>>();
  }

  public void callback() {
    final Set<State<T>> pickedSet = vv.getPickedVertexState().getPicked();
    final Pair<State<T>, State<T>> result = (pickedSet.size() >= 2) ? new Pair<State<T>, State<T>>((State<T>)pickedSet.toArray()[0], (State<T>)pickedSet.toArray()[1]) : null;
    synchronized(monitor) {
      monitor.setData(result);
      monitor.notify();
    }
  }

  public Pair<State<T>, State<T>> drawAutomaton(final Automaton<T> automaton) {
    final DirectedSparseGraph<State<T>, Step<T>> graph = new DirectedSparseGraph<State<T>, Step<T>>();
    final Map<State<T>, Set<Step<T>>> delta = automaton.getDelta();

    // vrcholy
    for (Entry<State<T>, Set<Step<T>>> entry : delta.entrySet()) {
      graph.addVertex(entry.getKey());
    }

    // hrany
    for (Entry<State<T>, Set<Step<T>>> entry : delta.entrySet()) {
      for (Step<T> step : entry.getValue()) {
        graph.addEdge(step, step.getSource(), step.getDestination());
      }
    }

    final Layout<State<T>, Step<T>> layout = new ISOMLayout<State<T>, Step<T>>(graph);
    //layout.setSize(new Dimension(300,300)); // sets the initial size of the space
    // The BasicVisualizationServer<V,E> is parameterized by the edge types
    vv = new VisualizationViewer<State<T>, Step<T>>(layout);
    //vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<State<T>>());
    vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Step<T>>());

    final DefaultModalGraphMouse<State<T>, Step<T>> gm = new DefaultModalGraphMouse<State<T>, Step<T>>();
    gm.setMode(ModalGraphMouse.Mode.PICKING);
    vv.setGraphMouse(gm);

    synchronized(monitor) {
      WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

        @Override
        public void run() {
          AutoEditorTopComponent.findInstance().drawAutomatonBasicVisualizationServer(new Callback() {

            @Override
            public void call() {
              AutoEditor.this.callback();
            }
          }, vv);
        }
      });

      try {
        monitor.wait();
      } catch (InterruptedException e) {
        return null;
      }
    }

    return monitor.getData();
  }
}
