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
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
public class ClustererWithAttributesIname implements ClustererWithAttributes<AbstractNode> {
  private final List<Cluster<AbstractNode>> nodeClusters;
  private final List<AbstractNode> items;
  private final Map<AbstractNode, Clusterer<AbstractNode>> attributeClusterers;

  public ClustererWithAttributesIname() {
    this.nodeClusters= new LinkedList<Cluster<AbstractNode>>();
    this.items= new LinkedList<AbstractNode>();
    this.attributeClusterers= new HashMap<AbstractNode, Clusterer<AbstractNode>>();
  }

  private AbstractNode addNode(final AbstractNode item) throws InterruptedException {
    assert !item.isAttribute();
    final Iterator<Cluster<AbstractNode>> iterator= this.nodeClusters.iterator();

    while (iterator.hasNext()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final Cluster<AbstractNode> cluster= iterator.next();
      final AbstractNode representant= cluster.getRepresentant();
      if (item.isSimpleData()&&representant.isSimpleData()) {
        cluster.add(item);
        return representant;
      } else if (
              item.isElement() &&
              representant.isElement() &&
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
      final AbstractNode representant= this.addNode(node);
      if (node.isElement()&&(node instanceof Element)) {
        for (AbstractNode subNode: ((Element) node).getSubnodes().getTokens()) {
          if (subNode.isAttribute()) {
            if (!this.attributeClusterers.containsKey(representant)) {
              this.attributeClusterers.put(representant, new ClustererWithAttributesInameHelperClusterer());
            }
            this.attributeClusterers.get(representant).add((Attribute) subNode);
          } else {
            this.addNode(subNode);
          }
        }
      }
    }
    for (AbstractNode rep : this.attributeClusterers.keySet()) {
      this.attributeClusterers.get(rep).cluster();
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

  @Override
  public List<Cluster<AbstractNode>> getAttributeClusters(final AbstractNode representant) {
    if (this.attributeClusterers.containsKey(representant)) {
      return Collections.unmodifiableList(this.attributeClusterers.get(representant).getClusters());
    } else {
      return Collections.emptyList();
    }
  }
}
