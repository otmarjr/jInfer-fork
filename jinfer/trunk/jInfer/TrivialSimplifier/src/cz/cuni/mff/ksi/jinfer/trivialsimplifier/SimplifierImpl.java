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
package cz.cuni.mff.ksi.jinfer.trivialsimplifier;

import cz.cuni.mff.ksi.jinfer.trivialsimplifier.processing.CPTrie;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.clustering.NameClusterer;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.clustering.ContextClusterer;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.display.RuleDisplayerTopComponent;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.kleening.KleeneProcessor;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.kleening.SimpleKP;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.options.ConfigPanel;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.processing.CPAlternations;
import java.util.List;
import java.util.prefs.Preferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Basic implementation of Simplifier interface.
 * 
 * @author vektor
 */
public class SimplifierImpl implements Simplifier {

  @Override
  public String getModuleName() {
    return "Trivial Simplifier";
  }

  private ClusterProcessor getClusterProcessor() {
    final String cp = Preferences.userNodeForPackage(ConfigPanel.class).get("cluster.processor", "Trie");
    final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
    io.getOut().println("Simplifier: using " + cp + " cluster processor.");
    if (cp.equals("Trie")) {
      return new CPTrie();
    }
    if (cp.equals("Alternations")) {
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
    RuleDisplayerTopComponent.findInstance().createNewPanel("Original").setRules(initialGrammar);
    final List<Pair<AbstractNode, List<AbstractNode>>> clustered = getClusterer().cluster(initialGrammar);
    //RuleDisplayerTopComponent.findInstance().createNewPanel("Clustered").setRules(clustered);
    final List<AbstractNode> processed = getClusterProcessor().processClusters(clustered);
    RuleDisplayerTopComponent.findInstance().createNewPanel("Processed").setRules(processed);
    final List<AbstractNode> kleened = getKleeneProcessor().kleeneProcess(processed);
    RuleDisplayerTopComponent.findInstance().createNewPanel("Kleened").setRules(kleened);
    callback.finished(kleened);
  }

}
