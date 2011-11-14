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
package cz.cuni.mff.ksi.jinfer.twostep;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RuleDisplayerHelper;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleanerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.ClustererWithAttributes;
import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * TwoStepSimplifier works in two step for simplification.
 * First it searches for a suitable clusterer submodule, to which it passes whole initialGrammar.
 * Clusterer is responsible to cluster elements properly. Cluster of elements
 * is then considered to be one, and the same element, with various instances
 * in input files.
 * <p>
 * For every cluster of elements, the clusterProcessor submodule is called.
 * Given the clusterer and list of observed positive examples (grammar = list
 * of elements), cluster processor is expected to produce one Element instance,
 * on which proper definition of regular expression representing content model
 * of the element children will be held.
 * <p>
 * The attributes of elements in the cluster are processed separately afterwards.
 * Currently, only simple processing is done - required/optional. This will be
 * extended in future to separated attribute processor submodule.
 * <p>
 * Produced regular expressions are further refined in {@link RegularExpressionCleaner}
 * submodule.
 *
 * @author anti
 */
public class TwoStepSimplifier {

  private static final Logger LOG = Logger.getLogger(TwoStepSimplifier.class);
  private final ClustererFactory clustererFactory;
  private final ClusterProcessorFactory clusterProcessorFactory;
  private final RegularExpressionCleanerFactory regularExpressionCleanerFactory;
  private final ContentInferrerFactory contentInfererFactory;

  /**
   * Create new simplifier and give all submodule factories to it.
   *
   * @param clustererFactory factory of clusterer submodule
   * @param clusterProcessorFactory factory of ClusterProcessor submodule
   * @param regularExpressionCleanerFactory factory of cleaner submodule
   */
  public TwoStepSimplifier(final ClustererFactory clustererFactory,
          final ClusterProcessorFactory clusterProcessorFactory,
          final RegularExpressionCleanerFactory regularExpressionCleanerFactory,
          final ContentInferrerFactory contentInfererFactory) {
    this.clustererFactory = clustererFactory;
    this.clusterProcessorFactory = clusterProcessorFactory;
    this.regularExpressionCleanerFactory = regularExpressionCleanerFactory;
    this.contentInfererFactory = contentInfererFactory;
  }

  private void verifyInput(final List<Element> initialGrammar) throws InterruptedException {
    for (AbstractStructuralNode node : initialGrammar) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (node == null) {
        throw new IllegalArgumentException("Got null as left side in grammar.");
      } else if (Boolean.TRUE.equals(node.getMetadata().get(IGGUtils.IS_SENTINEL))) {
        throw new IllegalArgumentException("Left side of grammar rule cannot be sentinel node!. Read documentation on architecture.");
      }
    }
  }

  /**
   * Do the main job of simplifier - simplify given grammar.
   *
   * @param initialGrammar grammar obtained from source files. In simple form - only concatenations.
   * @return Simplified Grammar
   * @throws InterruptedException
   */
  public List<Element> simplify(final List<Element> initialGrammar) throws InterruptedException {
    this.verifyInput(initialGrammar);

    final List<AbstractStructuralNode> abstracts = new ArrayList<AbstractStructuralNode>(initialGrammar.size());
    for (final Element e : initialGrammar) {
      abstracts.add(e);
    }

    // 1. cluster elements according to name
    final Clusterer<AbstractStructuralNode> clusterer = clustererFactory.create();
    clusterer.addAll(abstracts);
    clusterer.cluster();

    // 2. prepare emtpy final grammar
    final List<Element> finalGrammar = new LinkedList<Element>();

    // 3. process rules
    final ClusterProcessor<AbstractStructuralNode> processor = clusterProcessorFactory.create();
    for (Cluster<AbstractStructuralNode> cluster : clusterer.getClusters()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }

      final AbstractStructuralNode node = processor.processCluster(
              clusterer, new ArrayList<AbstractStructuralNode>(cluster.getMembers()));

      // 3.1 process attributes if supported
      final List<Attribute> attList = new ArrayList<Attribute>();
      if (clustererFactory.getCapabilities().contains("attributeClusters")) {
        @SuppressWarnings("unchecked")
        final List<Cluster<Attribute>> attributeClusters =
                ((ClustererWithAttributes<AbstractStructuralNode, Attribute>) clusterer).getAttributeClusters(cluster.getRepresentant());

        for (Cluster<Attribute> attCluster : attributeClusters) {
          final Attribute representant = attCluster.getRepresentant();
          //final ContentInferrer contentInferer = contentInfererFactory.create();
/*          Attribute output = new Attribute(representant.getContext(), representant.getName(), representant.getMetadata(),
          contentInferer.inferContentType(new ArrayList<ContentNode>(attCluster.getMembers())),
          representant.getContent());
           */
          Attribute output = new Attribute(representant.getContext(), representant.getName(), representant.getMetadata(), representant.getContentType(), representant.getContent());

          if (attCluster.size() < cluster.size()) {
            final Map<String, Object> m = new HashMap<String, Object>(representant.getMetadata());
            m.remove(IGGUtils.REQUIRED);
            output = new Attribute(representant.getContext(), representant.getName(), m, representant.getContentType(), representant.getContent());
          }

          for (Attribute instance : attCluster.getMembers()) {
            if (!instance.equals(attCluster.getRepresentant())) {
              output.getContent().addAll(
                      instance.getContent());
            }
          }

          attList.add(output);
        }
      }
      // 4. add to rules
      final StringBuilder sb = new StringBuilder(">>> Attributes are:");
      for (Attribute att : attList) {
        sb.append(att);
      }
      LOG.debug(sb);
      final RegularExpressionCleaner<AbstractStructuralNode> cleaner = regularExpressionCleanerFactory.<AbstractStructuralNode>create();
      finalGrammar.add(
              new Element(node.getContext(),
              node.getName(),
              node.getMetadata(),
              cleaner.cleanRegularExpression(((Element) node).getSubnodes()),
              attList));
    }

    RuleDisplayerHelper.showRulesAsync("Simplified", new CloneHelper().cloneGrammar(finalGrammar), true);

    return finalGrammar;
  }
}
