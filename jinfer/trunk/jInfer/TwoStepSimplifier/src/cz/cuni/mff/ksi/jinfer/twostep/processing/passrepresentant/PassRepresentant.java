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
package cz.cuni.mff.ksi.jinfer.twostep.processing.passrepresentant;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import java.util.List;

/**
 * Returns representative of cluster as solution.
 *
 * Trivial cluster processor to prove concept of changing submodules of simplifier.
 * For each clusters, it does no processing (grammar generation), just returns
 * representative.
 *
 * @author anti
 */
public class PassRepresentant implements ClusterProcessor<AbstractStructuralNode> {

  @Override
  public AbstractStructuralNode processCluster(final Clusterer<AbstractStructuralNode> clusterer, final List<AbstractStructuralNode> rules) throws InterruptedException {
    return rules.get(0);
  }
}
