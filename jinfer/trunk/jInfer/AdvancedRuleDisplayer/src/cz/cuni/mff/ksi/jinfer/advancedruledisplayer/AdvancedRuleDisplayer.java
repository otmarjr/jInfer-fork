/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.advancedruledisplayer;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.windows.WindowManager;

/**
 *
 * @author sviro
 */
public final class AdvancedRuleDisplayer {

  private static List<Regexp<AbstractStructuralNode>> getRootVertices(Forest<Regexp<AbstractStructuralNode>, RegexpInterval> graph) {
    List<Regexp<AbstractStructuralNode>> result = new ArrayList<Regexp<AbstractStructuralNode>>();
    Collection<Tree<Regexp<AbstractStructuralNode>, RegexpInterval>> trees = graph.getTrees();
    for (Tree<Regexp<AbstractStructuralNode>, RegexpInterval> tree : trees) {
      result.add(tree.getRoot());
    }

    return result;
  }


  private static Forest<Regexp<AbstractStructuralNode>, RegexpInterval> getForestFromRules(List<Element> rules) {
    DelegateForest<Regexp<AbstractStructuralNode>, RegexpInterval> result = new DelegateForest<Regexp<AbstractStructuralNode>, RegexpInterval>();
    for (Element element : rules) {
      result.addTree(getRuleTree(element));
    }
    return result;
  }

  private static Tree<Regexp<AbstractStructuralNode>, RegexpInterval> getRuleTree(Element element) {
    DelegateTree<Regexp<AbstractStructuralNode>, RegexpInterval> result = new DelegateTree<Regexp<AbstractStructuralNode>, RegexpInterval>();
    final Regexp<AbstractStructuralNode> root = Regexp.getToken((AbstractStructuralNode) element, RegexpInterval.getOnce());
    result.addVertex(root);
    result.addEdge(root.getInterval().getCopy(), root, element.getSubnodes());
    parseRegexp(result, element.getSubnodes());
    return result;
  }

  private static void parseRegexp(DelegateTree<Regexp<AbstractStructuralNode>, RegexpInterval> tree, Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
      case TOKEN: return;
      case ALTERNATION:
      case PERMUTATION:
      case CONCATENATION: addEdges(tree, regexp);
    }
  }

  private static void addEdges(DelegateTree<Regexp<AbstractStructuralNode>, RegexpInterval> tree, Regexp<AbstractStructuralNode> regexp) {
    if (BaseUtils.isEmpty(regexp.getChildren())) {
      return;
    }

    for (Regexp<AbstractStructuralNode> child : regexp.getChildren()) {
      tree.addEdge(regexp.getInterval().getCopy(), regexp, child);
      parseRegexp(tree, child);
    }
  }

  private AdvancedRuleDisplayer() {
  }

  /**
   * Display a list of rules. The list will be rendered as an image and put into
   * the Rule Displayer window, in a named panel.
   *
   * @param panelName Title of the panel where these rules will be displayed.
   * @param rules List of rules to display.
   * @param render Flag whether to actually do anything.
   */
  public static void showRulesAsync(final String panelName,
          final List<Element> rules, final boolean render) {
    if (!render || BaseUtils.isEmpty(rules)) {
      return;
    }
    Forest<Regexp<AbstractStructuralNode>, RegexpInterval> graph = getForestFromRules(rules);
    Layout<Regexp<AbstractStructuralNode>, RegexpInterval> layout = new TreeLayout<Regexp<AbstractStructuralNode>, RegexpInterval>(graph, 150, 150);
    final VisualizationViewer<Regexp<AbstractStructuralNode>, RegexpInterval> vv = new VisualizationViewer<Regexp<AbstractStructuralNode>, RegexpInterval>(layout, new Dimension(400, 300));

    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
    vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<RegexpInterval>());
    vv.getRenderContext().setVertexLabelTransformer(new RegexpTransformer());
    vv.getRenderContext().setVertexShapeTransformer(new VertexShapeTransformer(getRootVertices(graph)));
    vv.getRenderer().getVertexLabelRenderer().setPosition(Position.W);

    DefaultModalGraphMouse<Regexp<AbstractStructuralNode>, RegexpInterval> gm = new DefaultModalGraphMouse<Regexp<AbstractStructuralNode>, RegexpInterval>(1/1.1f, 1.1f);
    gm.setMode(Mode.TRANSFORMING);
    vv.setGraphMouse(gm);

    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        AdvancedRuleDisplayerTopComponent.findInstance().createNewPanel(panelName, new GraphZoomScrollPane(vv));
      }
    });
  }
}
