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
package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.treeruledisplayer.RulePanel;
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
import javax.swing.JPanel;

/**
 * Class responsible for rendering a set of rules as a rule trees.
 *
 * @author sviro
 */
public final class GraphBuilder {


  private GraphBuilder() {
  }

  @SuppressWarnings("unchecked")
  private static List<Regexp<AbstractStructuralNode>> getRootVertices(final Forest<Regexp<? extends AbstractNamedNode>, RegexpInterval> graph) {
    final List<Regexp<AbstractStructuralNode>> result = new ArrayList<Regexp<AbstractStructuralNode>>();
    final Collection<Tree<Regexp<? extends AbstractNamedNode>, RegexpInterval>> trees = graph.getTrees();
    for (Tree<Regexp<? extends AbstractNamedNode>, RegexpInterval> tree : trees) {
      final Regexp<? extends AbstractNamedNode> root = tree.getRoot();
      if (root.getContent() instanceof AbstractStructuralNode) {
        result.add((Regexp<AbstractStructuralNode>) root);
      }
    }

    return result;
  }

  private static Forest<Regexp<? extends AbstractNamedNode>, RegexpInterval> getForestFromRules(final List<Element> rules) {
    final DelegateForest<Regexp<? extends AbstractNamedNode>, RegexpInterval> result = new DelegateForest<Regexp<? extends AbstractNamedNode>, RegexpInterval>();
    for (Element element : rules) {
      result.addTree(getRuleTree(element));
    }
    return result;
  }

  private static Tree<Regexp<? extends AbstractNamedNode>, RegexpInterval> getRuleTree(final Element element) {
    final DelegateTree<Regexp<? extends AbstractNamedNode>, RegexpInterval> result = new DelegateTree<Regexp<? extends AbstractNamedNode>, RegexpInterval>();
    if (element == null) {
      return result;
    }

    final Regexp<AbstractStructuralNode> root = Regexp.getToken((AbstractStructuralNode) element, RegexpInterval.getOnce());
    result.addVertex(root);
    final RegexpInterval regexpInterval = (element.getSubnodes().getType() == RegexpType.LAMBDA) ? RegexpInterval.getOnce() : element.getSubnodes().getInterval().getCopy();
    result.addEdge(regexpInterval, root, element.getSubnodes());
    parseAttributes(result, root);
    parseRegexp(result, element.getSubnodes());
    return result;
  }

  private static void parseRegexp(final DelegateTree<Regexp<? extends AbstractNamedNode>, RegexpInterval> tree, final Regexp<? extends AbstractNamedNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
      case TOKEN:
        break;
      case ALTERNATION:
      case PERMUTATION:
      case CONCATENATION:
        addEdges(tree, regexp);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static void parseAttributes(final DelegateTree<Regexp<? extends AbstractNamedNode>, RegexpInterval> tree, final Regexp<AbstractStructuralNode> regexp) {
    for (Attribute attribute : ((Element) regexp.getContent()).getAttributes()) {
      tree.addEdge(RegexpInterval.getOnce(), regexp, Regexp.getToken(attribute));
    }
  }

  private static void addEdges(final DelegateTree<Regexp<? extends AbstractNamedNode>, RegexpInterval> tree, final Regexp<? extends AbstractNamedNode> regexp) {
    if (BaseUtils.isEmpty(regexp.getChildren())) {
      return;
    }

    for (Regexp<? extends AbstractNamedNode> child : regexp.getChildren()) {
      tree.addEdge(child.getInterval().getCopy(), regexp, child);
      parseRegexp(tree, child);
    }
  }

  /**
   * Get panel with rendered rule trees.
   * @param rules Rules to be created rule trees from.
   * @return Panel with rendered rule trees.
   */
  public static JPanel buildGraphPanel(final List<Element> rules) {
    if (rules == null) {
      return new JPanel();
    }
    
    final Forest<Regexp<? extends AbstractNamedNode>, RegexpInterval> graph = getForestFromRules(rules);
    final List<Regexp<AbstractStructuralNode>> roots = getRootVertices(graph);
    final Utils utils = new Utils(roots);

    final Layout<Regexp<? extends AbstractNamedNode>, RegexpInterval> layout = new TreeLayout<Regexp<? extends AbstractNamedNode>, RegexpInterval>(graph, utils.getHorizontalDistance(), utils.getVerticalDistance());
    final VisualizationViewer<Regexp<? extends AbstractNamedNode>, RegexpInterval> vv = new VisualizationViewer<Regexp<? extends AbstractNamedNode>, RegexpInterval>(layout, new Dimension(400, 300));

    vv.setBackground(utils.getBackgroundColor());
    setTransformers(vv, utils);

    final DefaultModalGraphMouse<Regexp<? extends AbstractNamedNode>, RegexpInterval> gm = new DefaultModalGraphMouse<Regexp<? extends AbstractNamedNode>, RegexpInterval>(1 / 1.1f, 1.1f);
    gm.setMode(Mode.TRANSFORMING);
    vv.setGraphMouse(gm);
    
    return new RulePanel(new GraphZoomScrollPane(vv));
  }

  private static void setTransformers(final VisualizationViewer<Regexp<? extends AbstractNamedNode>, RegexpInterval> vv, final Utils utils) {
    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Regexp<? extends AbstractNamedNode>, RegexpInterval>());
    vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<RegexpInterval>());
    vv.getRenderContext().setEdgeFontTransformer(new EdgeFontTransformer());
    vv.getRenderContext().setVertexLabelTransformer(new RegexpTransformer());
    vv.setVertexToolTipTransformer(new VertexTooltipTransformer());
    vv.getRenderContext().setVertexShapeTransformer(new VertexShapeTransformer(utils));
    vv.getRenderContext().setVertexFillPaintTransformer(new VertexColorTransformer(utils.getRoots()));
    vv.getRenderContext().setVertexFontTransformer(new VertexFontTransformer());
    vv.getRenderer().getVertexLabelRenderer().setPosition(Position.W);
  }
}
