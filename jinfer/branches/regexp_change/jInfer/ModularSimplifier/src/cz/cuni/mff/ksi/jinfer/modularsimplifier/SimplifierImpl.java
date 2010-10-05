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

import cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.StructuralAbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Cluster;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.KleeneProcessor;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.KleeneProcessorFactory;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.ClusterProcessorFactory;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.properties.PropertiesPanel;
import cz.cuni.mff.ksi.jinfer.ruledisplayer.RuleDisplayer;
import java.util.ArrayList;
import java.util.Collections;
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

  private static final Logger LOG = Logger.getLogger(Simplifier.class);

  @Override
  public String getName() {
    return "Modular Simplifier";
  }

  @Override
  public String getModuleDescription() {
    return getName(); // TODO vektor Elaborate based on selected sub-modules
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }

  private Clusterer getClusterer() {
    final String cs = RunningProject.getActiveProjectProps(PropertiesPanel.NAME).getProperty(
            PropertiesPanel.CLUSTERER,
            PropertiesPanel.CLUSTERER_DEFAULT);
    LOG.info("Simplifier: using " + cs + ".");
    return ModuleSelectionHelper.lookupImpl(ClustererFactory.class, cs).create();
  }

  private ClusterProcessor getClusterProcessor() {
    final String cp = RunningProject.getActiveProjectProps(PropertiesPanel.NAME).getProperty(
            PropertiesPanel.CLUSTER_PROCESSOR,
            PropertiesPanel.CLUSTER_PROCESSOR_DEFAULT);
    LOG.info("Simplifier: using " + cp + " cluster processor.");
    return ModuleSelectionHelper.lookupImpl(ClusterProcessorFactory.class,
            cp).create();
  }

  private KleeneProcessor getKleeneProcessor() {
    final String kp = RunningProject.getActiveProjectProps(PropertiesPanel.NAME).getProperty(
            PropertiesPanel.KLEENE_PROCESSOR,
            PropertiesPanel.KLEENE_PROCESSOR_DEFAULT);
    LOG.info("Simplifier: using " + kp + ".");
    return ModuleSelectionHelper.lookupImpl(KleeneProcessorFactory.class,
            kp).create();
  }

  @Override
  public void start(final List<StructuralAbstractNode> initialGrammar,
          final SimplifierCallback callback) throws InterruptedException {
    final Properties properties = RunningProject.getActiveProjectProps(PropertiesPanel.NAME);

    if (!Boolean.parseBoolean(properties.getProperty(PropertiesPanel.ENABLED,
            Boolean.toString(PropertiesPanel.ENABLED_DEFAULT)))) {
      callback.finished(initialGrammar);
      return;
    }

    final boolean render = Boolean.parseBoolean(
            properties.getProperty(PropertiesPanel.RENDER,
            Boolean.toString(PropertiesPanel.RENDER_DEFAULT)));

    final List<StructuralAbstractNode> mutableGrammar = getMutableGrammar(initialGrammar);

    RuleDisplayer.showRulesAsync("Original", new CloneHelper().cloneRules(mutableGrammar), render);
    final List<Cluster> clustered = getClusterer().cluster(mutableGrammar);
    RuleDisplayer.showClustersAsync("Clustered", new CloneHelper().cloneClusters(clustered), render);
    final List<StructuralAbstractNode> processed = getClusterProcessor().processClusters(clustered);
    RuleDisplayer.showRulesAsync("Processed", new CloneHelper().cloneRules(processed), render);
    final List<StructuralAbstractNode> kleened = getKleeneProcessor().kleeneProcess(processed);
    RuleDisplayer.showRulesAsync("Kleened", new CloneHelper().cloneRules(kleened), render);
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        callback.finished(kleened);
      }
    });
  }

  // TODO vektor Share this logic
  private List<StructuralAbstractNode> getMutableGrammar(final List<StructuralAbstractNode> initialGrammar) {
    final List<StructuralAbstractNode> ret = new ArrayList<StructuralAbstractNode>(initialGrammar.size());

    for (final StructuralAbstractNode n : initialGrammar) {
      if (!(n instanceof Element)) {
        throw new IllegalArgumentException();
      }
      final Element e = (Element) n;
      final Regexp<StructuralAbstractNode> r = Regexp.getMutable();
      if (e.getSubnodes().isToken()) {
        r.setContent(e.getSubnodes().getContent());
      } else {
        r.getChildren().addAll(e.getSubnodes().getChildren());
      }
      r.setType(e.getSubnodes().getType());
      r.setInterval(e.getSubnodes().getInterval());
      final Element en = new Element(e.getContext(), e.getName(), e.getMetadata(), r);
      ret.add(en);
    }

    return ret;
  }
}
