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

import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutFactory;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.graphviz.options.GraphvizUtils;
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
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.lookup.ServiceProvider;

/**
 * Can create instance of {@link Layout} using external Graphviz dot executable.
 *
 * @author anti, rio
 */
@ServiceProvider(service = LayoutFactory.class)
public class GraphvizLayoutFactory implements LayoutFactory {

  public static final String NAME = "GraphvizLayout";
  public static final String DISPLAY_NAME = "Graphviz Layout";
  public static final String PROPERTIES_DOTBIN = "dotbin";
  private static final Logger LOG = Logger.getLogger(GraphvizLayoutFactory.class);
  private static int windowWidth = 100;
  private static int windowHeight = 100;

  @Override
  public <T> Layout<State<T>, Step<T>> createLayout(final Automaton<T> automaton, final Graph<State<T>, Step<T>> graph, final Transformer<Step<T>, String> edgeLabelTransformer) throws InterruptedException {
    if (!GraphvizUtils.isBinaryValid()) {
      DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(org.openide.util.NbBundle.getMessage(GraphvizLayoutFactory.class, "binary.invalid.message"), NotifyDescriptor.ERROR_MESSAGE));
      throw new InterruptedException();
    }

    try {
      final byte[] graphInDotFormat = AutomatonToDot.<T>convertToDot(automaton, edgeLabelTransformer).getBytes();
      final Map<State<T>, Point2D> positions = getGraphvizPositions(graphInDotFormat, automaton.getDelta().keySet());
       final Transformer<State<T>, Point2D> trans = TransformerUtils.mapTransformer(positions);
       return new StaticLayout<State<T>, Step<T>>(graph, trans, new Dimension(windowWidth, windowHeight));
    } catch (Exception ex) {
      LOG.error("Error occured in Graphviz", ex);
      DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(org.openide.util.NbBundle.getMessage(GraphvizLayoutFactory.class, "error.dialog.message"), NotifyDescriptor.ERROR_MESSAGE));
      throw new InterruptedException(); //NOPMD
    }
  }

  @SuppressWarnings("PMD")
  private <T> Map<State<T>, Point2D> getGraphvizPositions(final byte[] graphInDotFormat, final Set<State<T>> automatonStates) throws GraphvizException, IOException {
    final Map<State<T>, Point2D> result = new HashMap<State<T>, Point2D>();

    // creates new dot process.
    final ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(GraphvizUtils.getPath(), "-Tplain"));
    final Process process = processBuilder.start();

    // write our graph into dot binary standart input
    process.getOutputStream().write(graphInDotFormat);
    process.getOutputStream().flush();

    // read from dot binary standard output
    final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    process.getOutputStream().close();

    final Scanner scanner = new Scanner(reader);
    // jumps some unnecessary information from output
    scanner.next();
    scanner.next();

    // get width and height of graph
    windowWidth = 100 + (int) Double.parseDouble(scanner.next());
    windowHeight = 100 + (int) Double.parseDouble(scanner.next());

    // parse output for graph vertex positions
    while (scanner.hasNext()) {
      final String n = scanner.next();
      if (n.equals("node")) {
        final int nodeName = Integer.parseInt(scanner.next());
        final double x = Double.parseDouble(scanner.next());
        final double y = Double.parseDouble(scanner.next());
        boolean found = false;
        for (State<T> state : automatonStates) {
          if (state.getName() == nodeName) {
            result.put(state, new Point(50 + (int) (x), 50 + (int) (y)));
            found = true;
            break;
          }
        }
        if (!found) {
          throw new GraphvizException("Node with name " + nodeName + " was not found in automaton.");
        }
      }
    }

    return result;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" lays graph nodes by using graphviz dot layouter.");
    sb.append(" For each graph, dot binary is executed with dot language translated");
    sb.append(" automaton. Output is parsed for position of nodes, which are used in JUNG.");
    return sb.toString();
  }
}
