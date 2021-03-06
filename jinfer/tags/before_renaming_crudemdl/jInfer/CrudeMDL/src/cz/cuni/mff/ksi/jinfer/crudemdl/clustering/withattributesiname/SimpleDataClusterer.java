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

package cz.cuni.mff.ksi.jinfer.crudemdl.clustering.withattributesiname;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class to cluster simpleData inside one element.
 *
 * @author anti
 */
public class SimpleDataClusterer implements Clusterer<SimpleData> {
  private final List<Cluster<SimpleData>> clusters;

  public SimpleDataClusterer() {
    this.clusters= new LinkedList<Cluster<SimpleData>>();
  }
  
  @Override
  public void add(SimpleData item) {
    if (clusters.isEmpty()) {
      clusters.add(
              new Cluster<SimpleData>(item)
              );
    } else {
      clusters.get(0).add(item);
    }
  }

  @Override
  public void addAll(Collection<SimpleData> items) {
    for (SimpleData item : items) {
      this.add(item);
    }
  }

  @Override
  public void cluster() throws InterruptedException {
  }

  @Override
  public SimpleData getRepresentantForItem(SimpleData item) {
    if (clusters.get(0).isMember(item)) {
      return clusters.get(0).getRepresentant();
    }
    throw new IllegalArgumentException("Node " + item.toString() + " is not in clusters, it wasn't added, i can't find it.");
  }

  @Override
  public List<Cluster<SimpleData>> getClusters() {
    return Collections.unmodifiableList(clusters);
  }

}
