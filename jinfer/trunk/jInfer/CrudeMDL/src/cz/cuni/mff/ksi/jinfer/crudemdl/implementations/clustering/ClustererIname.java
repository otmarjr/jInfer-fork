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

package cz.cuni.mff.ksi.jinfer.crudemdl.implementations.clustering;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 * Cluster nodes by name - ignoring case.
 *
 * Clustering is done by iterating queued items for clustering, for
 * each item, iterate clusters. If any representant is of same type (SimpleData,
 * Element,Attribute) add item to cluster. If cluster is not found, create new
 * with item as representant.
 * For SimpleData, check for same name is omitted, all simpledata ends up in one cluster.
 *
 * @author anti
 */
@ServiceProvider(service = Clusterer.class)
public class ClustererIname implements Clusterer<AbstractNode> {
  private final List<Cluster<AbstractNode>> nodeClusters;
  private final List<AbstractNode> items;
  //private final Map<AbstractNode, List<Cluster<AbstractNode>>> attributeClusters;

  public ClustererIname() {
    this.nodeClusters= new LinkedList<Cluster<AbstractNode>>();
    this.items= new LinkedList<AbstractNode>();
  }

  private void addNode(final AbstractNode item) throws InterruptedException {
    final Iterator<Cluster<AbstractNode>> iterator= this.nodeClusters.iterator();

    boolean found= false;
    while (iterator.hasNext()&&(!found)) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final Cluster<AbstractNode> cluster= iterator.next();
      final AbstractNode representant= cluster.getRepresentant();
      if (item.isSimpleData()&&representant.isSimpleData()) {
        cluster.add(item);
        found= true;
      } else if (item.isElement() &&
          representant.isElement() &&
          representant.getName().equalsIgnoreCase(item.getName())
        ) {
          cluster.add(item);
          found= true;
      } else if (item.isAttribute() &&
          representant.isAttribute() &&
          representant.getName().equalsIgnoreCase(item.getName())
        ) {
          cluster.add(item);
          found= true;
      }
    }
    if (!found) {
      this.nodeClusters.add(
              new Cluster<AbstractNode>(item)
              );
    }
  }

  @Override
  public void add(final AbstractNode item) {
    this.items.add(item);
  }

  @Override
  public void addAll(final Collection<AbstractNode> items){
    this.items.addAll(items);
  }

  @Override
  public void cluster() throws InterruptedException {
    for (AbstractNode node : items) {
      this.addNode(node);
      if (node.isElement()&&(node instanceof Element)) {
        for (AbstractNode subNode: ((Element) node).getSubnodes().getTokens()) {
          this.addNode(subNode);
        }
      }
    }
    this.items.clear();

  }

  @Override
  public AbstractNode getRepresentantForItem(final AbstractNode item) {
    for (Cluster<AbstractNode> cluster : this.nodeClusters) {
      if (cluster.isMember(item)) {
        return cluster.getRepresentant();
      }
    }
    throw new IllegalArgumentException("Node " + item.toString() + " is not in clusters, it wasn't added, i can't find it.");
  }

  @Override
  public List<Cluster<AbstractNode>> getClusters() {
    return Collections.unmodifiableList(this.nodeClusters);
  }
}
