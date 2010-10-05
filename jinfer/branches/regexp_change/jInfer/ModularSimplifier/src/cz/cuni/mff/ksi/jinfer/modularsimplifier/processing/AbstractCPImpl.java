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

import cz.cuni.mff.ksi.jinfer.base.objects.StructuralAbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Cluster;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * Common logic for cluster processing.
 *
 * @author vektor
 */
public abstract class AbstractCPImpl implements ClusterProcessor {

  @Override
  public List<StructuralAbstractNode> processClusters(
          final List<Cluster> clusters)
          throws InterruptedException {
    final List<StructuralAbstractNode> ret = new ArrayList<StructuralAbstractNode>();

    for (final Cluster cluster : clusters) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      ret.add(processCluster(cluster));
    }

    return ret;
  }

  protected abstract Element processCluster(
          final Cluster cluster);

}
