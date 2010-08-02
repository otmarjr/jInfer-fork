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

package cz.cuni.mff.ksi.jinfer.modularsimplifier.processing;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Cluster processor implementation that exports a cluster as an alternation of
 * its rules.
 * 
 * @author vektor
 */
public class CPAlternations implements ClusterProcessor {

  @Override
  public List<AbstractNode> processClusters(
          final List<Pair<AbstractNode, List<AbstractNode>>> clusters)
          throws InterruptedException {
    
    final List<AbstractNode> ret = new ArrayList<AbstractNode>();

    for (final Pair<AbstractNode, List<AbstractNode>> cluster : clusters) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      ret.add(processCluster(cluster));
    }

    return ret;
  }

  private static Element processCluster(
          final Pair<AbstractNode, List<AbstractNode>> cluster) {
    
    final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>();
    for (final AbstractNode n : cluster.getSecond()) {
      children.add(((Element) n).getSubnodes());
    }
    return new Element(null,
            cluster.getFirst().getName(),
            null,
            Regexp.getAlternation(children));
  }

}
