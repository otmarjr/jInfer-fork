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

import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.InameClusterer;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Simplifier intended to user some crude two-part MDL to evaluate solutions.
 * In general, simplification should proceed as follows:
 * 1. cluster elements
 * 2. for each element
 *      create prefix tree automaton
 *      simplify automaton by merging condition - currently supports k,h-contexts - configured to use 2,1-contexts
 *      create automaton with regexps on steps
 *      by removing states and combining regexp simplify regexp automaton and retrieve regexp
 *
 * @author anti
 */
@ServiceProvider(service = Simplifier.class)
public class SimplifierImpl implements Simplifier {
  private static final Logger LOG = Logger.getLogger(Simplifier.class);

  @Override
  public String getModuleName() {
    return "CrudeMDL";
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

  private Clusterer<AbstractNode> getClusterer() {
    return new InameClusterer();
  }

  private Processor<AbstractNode> getProcessor() {
    return new AutomatonMergingStateProcessor();
  }


  @Override
  public void start(final List<AbstractNode> initialGrammar, final SimplifierCallback callback) throws InterruptedException {
    this.verifyInput(initialGrammar);

    // 1. cluster elements according to name
    final Clusterer<AbstractNode> clusterer= this.getClusterer();
    clusterer.addAll(initialGrammar);
    clusterer.cluster();
    
    final Processor<AbstractNode> processor= this.getProcessor();
    final List<AbstractNode> finalGrammar= processor.processClusters(clusterer);

    // TODO Shortener
    
    callback.finished( finalGrammar );
  }
}