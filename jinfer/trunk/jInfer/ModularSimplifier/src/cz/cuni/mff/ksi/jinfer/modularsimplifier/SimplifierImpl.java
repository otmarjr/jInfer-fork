/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.modularsimplifier;

import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.CPTrie;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering.NameClusterer;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Cluster;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering.ContextClusterer;
import cz.cuni.mff.ksi.jinfer.ruledisplayer.RuleDisplayerTopComponent;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.KleeneProcessor;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.SimpleKP;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.CPAlternations;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 * Basic implementation of Simplifier interface.
 * 
 * @author vektor
 */
@ServiceProvider(service = Simplifier.class)
public class SimplifierImpl implements Simplifier {
  public static final String MODULAR_SIMPLIFIER_CLUSTER_PROCESSOR = "modularsimplifier.cluster.processor";
  public static final String MODULAR_SIMPLIFIER_ENABLED = "modularsimplifier.enabled";
  public static final String MODULAR_SIMPLIFIER_KLEENE_REPETITIONS = "modularsimplifier.kleene.repetitions";
  public static final String MODULAR_SIMPLIFIER_RENDER = "modularsimplifier.render";
  public static final String MODULAR_SIMPLIFIER_USE_CONTEXT = "modularsimplifier.use.context";

  private static final Logger LOG = Logger.getLogger(Simplifier.class);

  @Override
  public String getModuleName() {
    return "Modular Simplifier";
  }

  private ClusterProcessor getClusterProcessor() {
    final Properties properties = RunningProject.getActiveProjectProps();
    final String cp = properties.getProperty(MODULAR_SIMPLIFIER_CLUSTER_PROCESSOR,"Trie");
    LOG.info("Simplifier: using " + cp + " cluster processor.");
    if ("Trie".equals(cp)) {
      return new CPTrie();
    }
    if ("Alternations".equals(cp)) {
      return new CPAlternations();
    }
    throw new IllegalArgumentException("Unknown cluster processor: " + cp);
  }

  private Clusterer getClusterer() {
    final Properties properties = RunningProject.getActiveProjectProps();
    if (Boolean.parseBoolean(properties.getProperty(MODULAR_SIMPLIFIER_USE_CONTEXT,"false"))) {
      LOG.info("Simplifier: using context.");
      return new ContextClusterer();
    }
    LOG.info("Simplifier: not using context.");
    return new NameClusterer();
  }

  private KleeneProcessor getKleeneProcessor() {
    return new SimpleKP(Integer.parseInt(RunningProject.getActiveProjectProps()
          .getProperty(MODULAR_SIMPLIFIER_KLEENE_REPETITIONS,"3")));
  }

  @Override
  public void start(final List<AbstractNode> initialGrammar, 
          final SimplifierCallback callback) throws InterruptedException {
    final Properties properties = RunningProject.getActiveProjectProps();

    if (!Boolean.parseBoolean(properties.getProperty(MODULAR_SIMPLIFIER_ENABLED,"true"))) {
      callback.finished(initialGrammar);
      return;
    }

    final boolean render = Boolean.parseBoolean(properties.getProperty(MODULAR_SIMPLIFIER_RENDER,"true"));

    showRulesAsync("Original", new CloneHelper().cloneRules(initialGrammar), render);
    final List<Cluster> clustered = getClusterer().cluster(initialGrammar);
    showClustersAsync("Clustered", new CloneHelper().cloneClusters(clustered), render);
    final List<AbstractNode> processed = getClusterProcessor().processClusters(clustered);
    showRulesAsync("Processed", new CloneHelper().cloneRules(processed), render);
    final List<AbstractNode> kleened = getKleeneProcessor().kleeneProcess(processed);
    showRulesAsync("Kleened", new CloneHelper().cloneRules(kleened), render);
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        callback.finished(kleened);
      }
    });
  }

  private static void showRulesAsync(final String panelName, final List<AbstractNode> rules, final boolean render) {
    if (!render || BaseUtils.isEmpty(rules)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        RuleDisplayerTopComponent.findInstance().createNewPanel(panelName).setRules(rules);
      }
    });
  }

  private static void showClustersAsync(final String panelName, final List<Cluster> clusters, final boolean render) {
    if (!render || BaseUtils.isEmpty(clusters)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        RuleDisplayerTopComponent.findInstance().createNewPanel(panelName).setClusters(clusters);
      }
    });
  }
}
