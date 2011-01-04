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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.graphviz;

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutF;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author rio
 * TODO rio comment & refactor
 */
@ServiceProvider(service = LayoutF.class)
public class GraphvizLayoutFactory implements LayoutF {
  public static final String NAME= "GraphvizLayout";
  public static final String PROPERTIES_DOTBIN = "dotbin";
  private static Logger LOG = Logger.getLogger(GraphvizLayoutFactory.class);
  
  @Override
  public <T> Layout<State<T>, Step<T>> createLayout(Automaton<T> automaton, Graph<State<T>, Step<T>> graph,  final Transformer<Step<T>, String> edgeLabelTransformer) {
    final Map<State<T>, Point2D> positions= new HashMap<State<T>, Point2D>();

    ProcessBuilder p = new ProcessBuilder(Arrays.asList(
            "/usr/bin/dot",
            "-Tplain"));
   
    try {
      Process k = p.start();
      k.getOutputStream().write(
              (new AutomatonToDot<T>()).convertToDot(automaton, edgeLabelTransformer).getBytes()
              );
      k.getOutputStream().flush();
      BufferedReader b = new BufferedReader(new InputStreamReader( k.getInputStream() ) );
      k.getOutputStream().close();

      Scanner s = new Scanner(b);
      s.next();
      s.next();
      double width= s.nextDouble();
      double height= s.nextDouble();
      double windowW= 700;
      double windowH= 300;

      while (s.hasNext()) {
        if (s.next().equals("node")) {
          int nodeName = s.nextInt();
          double x= s.nextDouble();
          double y= s.nextDouble();
          boolean found = false;
          for (State<T> state : automaton.getDelta().keySet()) {
            if (state.getName() == nodeName) {
              positions.put(state, new Point(
                      (int) (windowW * x / width),
                      (int) (windowH * y / height)
                      ));
              found= true;
              break;
            }
          }
          assert(found);
        }
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    Transformer<State<T>, Point2D> trans= TransformerUtils.mapTransformer(positions);

    return new StaticLayout<State<T>, Step<T>>(graph, trans, new Dimension(700,300));
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }
}
