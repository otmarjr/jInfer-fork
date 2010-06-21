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
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering.ContextClusterer;
import cz.cuni.mff.ksi.jinfer.ruledisplayer.RuleDisplayerTopComponent;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.KleeneProcessor;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.SimpleKP;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.options.ConfigPanel;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.CPAlternations;
import java.util.List;
import java.util.prefs.Preferences;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Basic implementation of Simplifier interface.
 * 
 * @author vektor
 */
@ServiceProvider(service = Simplifier.class)
public class SimplifierImpl implements Simplifier {

  @Override
  public String getModuleName() {
    return "Modular Simplifier";
  }

  private ClusterProcessor getClusterProcessor() {
    final String cp = Preferences.userNodeForPackage(ConfigPanel.class).get("cluster.processor", "Trie");
    final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
    io.getOut().println("Simplifier: using " + cp + " cluster processor.");
    if ("Trie".equals(cp)) {
      return new CPTrie();
    }
    if ("Alternations".equals(cp)) {
      return new CPAlternations();
    }
    throw new IllegalArgumentException("Unknown cluster processor: " + cp);
  }

  private Clusterer getClusterer() {
    final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
    if (Preferences.userNodeForPackage(ConfigPanel.class).getBoolean("use.context", false)) {
      io.getOut().println("Simplifier: using context.");
      return new ContextClusterer();
    }
    io.getOut().println("Simplifier: not using context.");
    return new NameClusterer();
  }

  private KleeneProcessor getKleeneProcessor() {
    return new SimpleKP();
  }

  @Override
  public void start(final List<AbstractNode> initialGrammar, final SimplifierCallback callback) {
    if (!Preferences.userNodeForPackage(ConfigPanel.class).getBoolean("enabled", true)) {
      callback.finished(initialGrammar);
      return;
    }

    final boolean render = Preferences.userNodeForPackage(ConfigPanel.class).getBoolean("render", true);

    if (render) {
      RuleDisplayerTopComponent.findInstance().createNewPanel("Original").setRules(initialGrammar);
    }
    final List<Pair<AbstractNode, List<AbstractNode>>> clustered = getClusterer().cluster(initialGrammar);
    if (render) {
      RuleDisplayerTopComponent.findInstance().createNewPanel("Clustered").setClusters(clustered);
    }
    final List<AbstractNode> processed = getClusterProcessor().processClusters(clustered);
    if (render) {
      RuleDisplayerTopComponent.findInstance().createNewPanel("Processed").setRules(processed);
    }
    final List<AbstractNode> kleened = getKleeneProcessor().kleeneProcess(processed);
    if (render) {
      RuleDisplayerTopComponent.findInstance().createNewPanel("Kleened").setRules(kleened);
    }
    callback.finished(kleened);
  }
}
