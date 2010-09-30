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

package cz.cuni.mff.ksi.jinfer.crudemdl.clustering;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class ClustererInameAttributeHelperClusterer implements Clusterer<AbstractNode> {
  private final List<Cluster<AbstractNode>> nodeClusters;
  private final List<AbstractNode> items;

  public ClustererInameAttributeHelperClusterer() {
    this.nodeClusters= new LinkedList<Cluster<AbstractNode>>();
    this.items= new LinkedList<AbstractNode>();
  }

  @Override
  public void add(final AbstractNode item) {
    assert item.isAttribute();
    this.items.add(item);
  }

  @Override
  public void addAll(final Collection<AbstractNode> items) {
    this.items.addAll(items);
  }

  private AbstractNode addNode(final AbstractNode item) throws InterruptedException {
    final Iterator<Cluster<AbstractNode>> iterator= this.nodeClusters.iterator();

    while (iterator.hasNext()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final Cluster<AbstractNode> cluster= iterator.next();
      final AbstractNode representant= cluster.getRepresentant();
      if (
              representant.getName().equalsIgnoreCase(item.getName())
      ) {
        cluster.add(item);
        return representant;
      }
    }
    this.nodeClusters.add(
            new Cluster<AbstractNode>(item)
            );
    return item;
  }

  @Override
  public void cluster() throws InterruptedException {
    for (AbstractNode item : this.items) {
      this.addNode(item);
    }
    this.items.clear();
  }

  @Override
  public AbstractNode getRepresentantForItem(AbstractNode item) {
    for (Cluster<AbstractNode> cluster : this.nodeClusters) {
      if (cluster.isMember(item)) {
        return cluster.getRepresentant();
      }
    }
    throw new IllegalArgumentException("Attribute " + item.toString() + " is not in attribute clusters, it wasn't added, i can't find it.");
  }

  @Override
  public List<Cluster<AbstractNode>> getClusters() {
    return Collections.unmodifiableList(this.nodeClusters);
  }

}
