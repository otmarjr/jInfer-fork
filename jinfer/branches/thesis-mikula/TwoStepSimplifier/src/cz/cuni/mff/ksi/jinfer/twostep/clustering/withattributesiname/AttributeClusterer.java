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
package cz.cuni.mff.ksi.jinfer.twostep.clustering.withattributesiname;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class for {@link Iname}. Clusters attributes for one
 * element, by name (case insensitive).
 *
 * @author anti
 */
public class AttributeClusterer implements Clusterer<Attribute> {

  private final List<Cluster<Attribute>> nodeClusters;
  private final List<Attribute> items;

  /**
   * Default constructor.
   */
  public AttributeClusterer() {
    this.nodeClusters = new LinkedList<Cluster<Attribute>>();
    this.items = new LinkedList<Attribute>();
  }

  @Override
  public void add(final Attribute item) {
    this.items.add(item);
  }

  @Override
  public void addAll(final Collection<Attribute> items) {
    this.items.addAll(items);
  }

  private Attribute addNode(final Attribute item) throws InterruptedException {
    final Iterator<Cluster<Attribute>> iterator = this.nodeClusters.iterator();

    while (iterator.hasNext()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final Cluster<Attribute> cluster = iterator.next();
      final Attribute representant = cluster.getRepresentant();
      if (representant.getName().equalsIgnoreCase(item.getName())) {
        cluster.add(item);
        return representant;
      }
    }
    this.nodeClusters.add(
            new Cluster<Attribute>(item));
    return item;
  }

  @Override
  public void cluster() throws InterruptedException {
    for (Attribute item : this.items) {
      this.addNode(item);
    }
    this.items.clear();
  }

  @Override
  public Attribute getRepresentantForItem(final Attribute item) {
    for (Cluster<Attribute> cluster : this.nodeClusters) {
      if (cluster.isMember(item)) {
        return cluster.getRepresentant();
      }
    }
    throw new IllegalArgumentException("Attribute " + item.toString() + " is not in attribute clusters, it wasn't added, i can't find it.");
  }

  @Override
  public List<Cluster<Attribute>> getClusters() {
    return Collections.unmodifiableList(this.nodeClusters);
  }
}
