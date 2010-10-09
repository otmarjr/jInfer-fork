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
package cz.cuni.mff.ksi.jinfer.crudemdl;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.ClustererWithAttributes;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import cz.cuni.mff.ksi.jinfer.ruledisplayer.RuleDisplayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment! STILL UNDER HEAVY DEVELOPMENT, after more is done, comment
 * will be written (at least i hope so)
 * @author anti
 */
@ServiceProvider(service = Simplifier.class)
public class TwoStepSimplifierImpl implements Simplifier {
  /**
   * Name of the module in constant, for use in classes in this module.
   */
  public static final String NAME = "TwoStepSimplifier";
  /**
   * Property name of clusterer submodule.
   */
  public static final String PROPERTIES_CLUSTERER = "clusterer";
  /**
   * Property name of cluster processor submodule.
   */
  public static final String PROPERTIES_CLUSTER_PROCESSOR = "cluster-processor";

  private static final Logger LOG = Logger.getLogger(Simplifier.class);

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getName() + "(" + this.getClustererFactory().getModuleDescription() + ", " + this.getClusterProcessorFactory().getModuleDescription() + ")";
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }

  private ClustererFactory getClustererFactory() {
    final Properties p = RunningProject.getActiveProjectProps(this.getName());

    return ModuleSelectionHelper.lookupImpl(ClustererFactory.class, p.getProperty(PROPERTIES_CLUSTERER));
  }

  private ClusterProcessorFactory getClusterProcessorFactory() {
    final Properties p = RunningProject.getActiveProjectProps(this.getName());

    return ModuleSelectionHelper.lookupImpl(ClusterProcessorFactory.class, p.getProperty(PROPERTIES_CLUSTER_PROCESSOR));
  }

  private void verifyInput(final List<Element> initialGrammar) throws InterruptedException {
    for (AbstractStructuralNode node : initialGrammar) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (node == null) {
        throw new IllegalArgumentException("Got null as left side in grammar.");
      }
    }
  }

  // TODO anti Display rules!
  @Override
  public void start(final List<Element> initialGrammar, final SimplifierCallback callback) throws InterruptedException {
    this.verifyInput(initialGrammar);

    /*
     * Testing, testing .. can you hear me?
     * Gordon doesn't need to hear all this, he is a highly trained professional!
     * I'm sure nothing will go wrong.
     */
    LOG.debug("# Begin of rules dump");
    for (AbstractStructuralNode node : initialGrammar) {
      LOG.debug(node.toString());
    }
    LOG.debug("# End of rules dump");

    RuleDisplayer.showRulesAsync("Original", new CloneHelper().cloneRules(initialGrammar), true);
    final List<AbstractStructuralNode> abstracts = new ArrayList<AbstractStructuralNode>(initialGrammar.size());
    for (final Element e : initialGrammar) {
      abstracts.add(e);
    }

    // 1. cluster elements according to name
    final ClustererFactory clustererFactory= this.getClustererFactory();
    final Clusterer<AbstractStructuralNode> clusterer= clustererFactory.create();
    clusterer.addAll(abstracts);
    clusterer.cluster();

    // 2. prepare emtpy final grammar
    final List<Element> finalGrammar= new LinkedList<Element>();

    // 3. process rules
    final ClusterProcessor<AbstractStructuralNode> processor= this.getClusterProcessorFactory().create();
    for (Cluster<AbstractStructuralNode> cluster : clusterer.getClusters()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }

      List<AbstractStructuralNode> rules= BaseUtils.<AbstractStructuralNode>filter(
              new ArrayList<AbstractStructuralNode>(cluster.getMembers()),
              new BaseUtils.Predicate<AbstractStructuralNode>() {
                @Override
                public boolean apply(AbstractStructuralNode argument) {
                  return !Boolean.TRUE.equals(
                          argument.getMetadata().get("is_sentinel"));
                }
              });
      final AbstractStructuralNode node =  processor.processCluster(clusterer, rules);

      // 3.1 process attributes if supported
      final List<Attribute> attList= new ArrayList<Attribute>();
      if (clustererFactory.getCapabilities().contains("attributeClusters")) {
        @SuppressWarnings("unchecked")
        final List<Cluster<Attribute>> attributeClusters=
                ((ClustererWithAttributes<AbstractStructuralNode, Attribute>) clusterer).
                getAttributeClusters(cluster.getRepresentant());

        for (Cluster<Attribute> attCluster : attributeClusters) {
          final Attribute representant= attCluster.getRepresentant();
          Attribute output= new Attribute(representant.getContext(), representant.getName(), representant.getMetadata(), representant.getContentType(), representant.getContent());
          if (attCluster.size() < cluster.size()) {
            final Map<String, Object> m= new HashMap<String, Object>(representant.getMetadata());
            m.remove("required");
            output= new Attribute(representant.getContext(), representant.getName(), m, representant.getContentType(), representant.getContent());
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
      final StringBuilder sb= new StringBuilder(">>> Attributes are:");
      for (Attribute att : attList) {
        sb.append(att);
      }
      LOG.debug(sb);
      finalGrammar.add(
              new Element(node.getContext(), node.getName(), node.getMetadata(), ((Element) node).getSubnodes(), attList)
              );
    }

    RuleDisplayer.showRulesAsync("Processed", new CloneHelper().cloneRules(finalGrammar), true);
    callback.finished(finalGrammar);
  }
}
