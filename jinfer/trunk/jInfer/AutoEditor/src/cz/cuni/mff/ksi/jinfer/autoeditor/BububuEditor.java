/*
 *  Copyright (C) 2010 anti
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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.AutomatonToDot;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.PickingUnlimitedGraphMousePlugin;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.SymbolToString;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.examples.OneButtonComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 * TODO anti Comment!
 *
 * @param <T>
 * @author anti
 */
public class BububuEditor<T> extends AutoEditor<T> {
  private static final Logger LOG = Logger.getLogger(BububuEditor.class);
  private SymbolToString<T> symbolToString;

  public BububuEditor(SymbolToString<T> symbolToString) {
    this.symbolToString= symbolToString;
  }

  public BububuEditor() {
  }

  /**
   * Variable used to pass data to the GUI and gets the result of user
   * interaction.
   */
  private VisualizationViewer<State<T>, Step<T>> visualizationViewer;

  /**
   * Draws automaton and waits until user picks two states and clicks
   * 'continue' button.
   *
   * @param automaton automaton to be drawn
   * @return if user picks exactly two states returns Pair of them otherwise null
   */
  //@Override
  public List<State<T>> drawAutomatonToPickStates(final OneButtonComponent panel, final Automaton<T> automaton) {


    final DirectedSparseMultigraph<State<T>, Step<T>> graph = new DirectedSparseMultigraph<State<T>, Step<T>>();
    final Map<State<T>, Set<Step<T>>> automatonDelta = automaton.getDelta();

    // Get vertices = states of automaton
    for (Entry<State<T>, Set<Step<T>>> entry : automatonDelta.entrySet()) {
      graph.addVertex(entry.getKey());
    }

    // Get edges of automaton
    for (Entry<State<T>, Set<Step<T>>> entry : automatonDelta.entrySet()) {
      for (Step<T> step : entry.getValue()) {
        graph.addEdge(step, step.getSource(), step.getDestination());
      }
    }

    Map<State<T>, Point2D> positions= new HashMap<State<T>, Point2D>();

    ProcessBuilder p = new ProcessBuilder(Arrays.asList(
            "/usr/bin/dot",
            "-Tplain"));
    try {
      Process k = p.start();
      k.getOutputStream().write(
              (new AutomatonToDot<T>()).convertToDot(automaton, symbolToString).getBytes()
              );
      k.getOutputStream().flush();
      BufferedReader b = new BufferedReader(new InputStreamReader( k.getInputStream() ) );
      k.getOutputStream().close();

      Scanner s = new Scanner(b);
      s.next();
      s.next();
      double width= s.nextDouble();
      double height= s.nextDouble();
      double windowW= 500;
      double windowH= 300;

      while (s.hasNext()) {
        if (s.next().equals("node")) {
          int nodeName = s.nextInt();
          double x= s.nextDouble();
          double y= s.nextDouble();
          for (State<T> state : automatonDelta.keySet()) {
            if (state.getName() == nodeName) {
              positions.put(state, new Point(
                      (int) (windowW * x / width),
                      (int) (windowH * y / height)
                      ));
              break;
            }
          }
        }
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    Transformer<State<T>, Point2D> trans= TransformerUtils.mapTransformer(positions);

    // TODO rio find suitable layout
    final Layout<State<T>, Step<T>> layout = new StaticLayout<State<T>, Step<T>>(graph, trans);

    //layout.setSize(new Dimension(300,300)); // sets the initial size of the space

    visualizationViewer = new VisualizationViewer<State<T>, Step<T>>(layout);
    //visualizationViewer.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

    visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<State<T>>());
    visualizationViewer.getRenderContext().setEdgeLabelTransformer(
            new Transformer<Step<T>, String>() {
              @Override
              public String transform(Step<T> i) {
                return BububuEditor.this.symbolToString.toString(i.getAcceptSymbol());
              }
    });

    final PluggableGraphMouse gm = new PluggableGraphMouse();
    gm.add(new PickingUnlimitedGraphMousePlugin<State<T>, Step<T>>());
    visualizationViewer.setGraphMouse(gm);

    // Call GUI in a special thread. Required by NB.
    synchronized(this) {
      WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

        @Override
        public void run() {
          // Pass this as argument so the thread will be able to wake us up.
          //AutoEditorTopComponent.findInstance().drawAutomatonBasicVisualizationServer(BububuEditor.this, visualizationViewer, "Please select two states to be merged together.");
        }
      });

      try {
        // Sleep on this.
        this.wait();
      } catch (InterruptedException e) {
        return null;
      }
    }

    /* AutoEditorTopComponent wakes us up. Get the result and return it.
     * VisualizationViewer should give us the information about picked vertices.
     */
    final Set<State<T>> pickedSet = visualizationViewer.getPickedVertexState().getPicked();
    List<State<T>> lst = new ArrayList<State<T>>(pickedSet);
    return lst;
  }
}
