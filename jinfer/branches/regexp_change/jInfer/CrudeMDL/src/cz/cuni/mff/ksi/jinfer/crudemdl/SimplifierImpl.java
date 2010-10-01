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
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.ClustererWithAttributes;
import cz.cuni.mff.ksi.jinfer.crudemdl.properties.CrudeMDLPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment! STILL UNDER HEAVY DEVELOPMENT, after more is done, comment
 * will be written (at least i hope so)
 * @author anti
 */
@ServiceProvider(service = Simplifier.class)
public class SimplifierImpl implements Simplifier {
  private static final Logger LOG = Logger.getLogger(Simplifier.class);

  @Override
  public String getName() {
    return CrudeMDLPropertiesPanel.NAME;
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

    return ModuleSelectionHelper.lookupImpl(ClustererFactory.class, p.getProperty(CrudeMDLPropertiesPanel.PROPERTIES_CLUSTERER));
  }

  private ClusterProcessorFactory getClusterProcessorFactory() {
    final Properties p = RunningProject.getActiveProjectProps(this.getName());

    return ModuleSelectionHelper.lookupImpl(ClusterProcessorFactory.class, p.getProperty(CrudeMDLPropertiesPanel.PROPERTIES_CLUSTER_PROCESSOR));
  }

  private void verifyInput(final List<AbstractNode> initialGrammar) throws InterruptedException {
    for (AbstractNode node : initialGrammar) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (!NodeType.ELEMENT.equals(node.getType())) {
        final StringBuilder sb = new StringBuilder("Initial grammar contains rule with ");
        sb.append(node.getType().toString());
        sb.append(" as left side.");
        throw new IllegalArgumentException(sb.toString());
      }
      if (node == null) {
        throw new IllegalArgumentException("Got null as left side in grammar.");
      }
    }
  }

  @Override
  public void start(final List<AbstractNode> initialGrammar, final SimplifierCallback callback) throws InterruptedException {
    this.verifyInput(initialGrammar);

 //   RuleDisplayer.showRulesAsync("Original", new CloneHelper().cloneRules(initialGrammar), true);
    // 1. cluster elements according to name
    final ClustererFactory clustererFactory= this.getClustererFactory();
    final Clusterer<AbstractNode> clusterer= clustererFactory.create();
    clusterer.addAll(initialGrammar);
    clusterer.cluster();

    // 2. prepare emtpy final grammar
    final List<AbstractNode> finalGrammar= new LinkedList<AbstractNode>();

    // 3. process rules
    final ClusterProcessor<AbstractNode> processor= this.getClusterProcessorFactory().create();
    for (Cluster<AbstractNode> cluster : clusterer.getClusters()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (!cluster.getRepresentant().isElement()) {
        continue;
      }

      final AbstractNode node =  processor.processCluster(clusterer, cluster);

      // 3.1 process attributes if supported
      final List<Regexp<AbstractNode>> attTokens= new ArrayList<Regexp<AbstractNode>>();
      if (clustererFactory.getCapabilities().contains("attributeClusters")) {
        final List<Cluster<AbstractNode>> attributeClusters=
                ((ClustererWithAttributes<AbstractNode>) clusterer).
                getAttributeClusters(cluster.getRepresentant());

        for (Cluster<AbstractNode> attCluster : attributeClusters) {
          if (attCluster.size() < cluster.size()) {
            attCluster.getRepresentant().getMetadata().remove("required");
          }

          for (AbstractNode instance : attCluster.getMembers()) {
            assert instance.isAttribute();
            if (instance.isAttribute()&&(!instance.equals(attCluster.getRepresentant()))) {
              ((Attribute) attCluster.getRepresentant()).getContent().addAll(
                      ((Attribute) instance).getContent());
            }
          }

          attTokens.add(
                  Regexp.<AbstractNode>getToken(attCluster.getRepresentant())
                  );

        }
        // 4. add to rules
        attTokens.add(((Element) node).getSubnodes());
        final Regexp<AbstractNode> regexpAttributes=
                Regexp.<AbstractNode>getConcatenation( attTokens );
        LOG.debug(">>> Attributes regexp is:");
        LOG.debug(regexpAttributes);
        finalGrammar.add(
                new Element(node.getContext(), node.getName(), node.getMetadata(), regexpAttributes)
                );
      } else {
        finalGrammar.add( node );
      }
    }

  //  RuleDisplayer.showRulesAsync("Processed", new CloneHelper().cloneRules(  finalGrammar), true);
    callback.finished( finalGrammar );
  }
}