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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.ClustererWithAttributes;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
 * For SimpleData, check for same name is omitted, all simpledata ends up in one cluster for each element.
 *
 * @author anti
 */
public class Iname implements ClustererWithAttributes<AbstractStructuralNode, Attribute> {

  private final List<Cluster<AbstractStructuralNode>> nodeClusters;
  private final List<AbstractStructuralNode> items;
  private final Map<AbstractStructuralNode, Clusterer<Attribute>> attributeClusterers;
  private final Map<AbstractStructuralNode, Clusterer<SimpleData>> simpleDataClusterers;

  /**
   * Default constructor.
   */
  public Iname() {
    this.nodeClusters = new LinkedList<Cluster<AbstractStructuralNode>>();
    this.items = new LinkedList<AbstractStructuralNode>();
    this.attributeClusterers = new LinkedHashMap<AbstractStructuralNode, Clusterer<Attribute>>();
    this.simpleDataClusterers = new LinkedHashMap<AbstractStructuralNode, Clusterer<SimpleData>>();
  }

  private AbstractStructuralNode addNode(final AbstractStructuralNode item) throws InterruptedException {
    final Iterator<Cluster<AbstractStructuralNode>> iterator = this.nodeClusters.iterator();

    while (iterator.hasNext()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final Cluster<AbstractStructuralNode> cluster = iterator.next();
      final AbstractStructuralNode representant = cluster.getRepresentant();
      if (item.isElement()
              && representant.isElement()
              && representant.getName().equalsIgnoreCase(item.getName())) {
        cluster.add(item);
        return representant;
      }
    }
    this.nodeClusters.add(
            new Cluster<AbstractStructuralNode>(item));
    return item;
  }

  @Override
  public void add(final AbstractStructuralNode item) {
    this.items.add(item);
  }

  @Override
  public void addAll(final Collection<AbstractStructuralNode> items) {
    this.items.addAll(items);
  }

  @Override
  public void cluster() throws InterruptedException {
    for (AbstractStructuralNode node : items) {
      final AbstractStructuralNode representant = this.addNode(node);

      if (node.isElement()) {
        for (AbstractStructuralNode subNode : ((Element) node).getSubnodes().getTokens()) {
          if (Boolean.TRUE.equals(subNode.getMetadata().get(IGGUtils.IS_SENTINEL))) {
            this.addNode(subNode);
          }
          if (subNode.isSimpleData()) {
            if (!this.simpleDataClusterers.containsKey(representant)) {
              this.simpleDataClusterers.put(representant,
                      new SimpleDataClusterer());
            }
            this.simpleDataClusterers.get(representant).add((SimpleData) subNode);
          }
        }
        for (Attribute attribute : ((Element) node).getAttributes()) {
          if (!this.attributeClusterers.containsKey(representant)) {
            this.attributeClusterers.put(representant, new AttributeClusterer());
          }
          this.attributeClusterers.get(representant).add(attribute);
        }
      }
    }
    for (AbstractStructuralNode rep : this.attributeClusterers.keySet()) {
      this.attributeClusterers.get(rep).cluster();
    }
    for (AbstractStructuralNode rep : simpleDataClusterers.keySet()) {
      simpleDataClusterers.get(rep).cluster();
    }
    this.items.clear();
  }

  @Override
  public AbstractStructuralNode getRepresentantForItem(final AbstractStructuralNode item) {
    if (item.isSimpleData()) {
      final Iterator<Clusterer<SimpleData>> it = simpleDataClusterers.values().iterator();
      while (it.hasNext()) {
        try {
          return it.next().getRepresentantForItem((SimpleData) item);
        } catch (IllegalArgumentException e) {
          // the exception is normal to occur here!
        }
      }
    } else {
      for (Cluster<AbstractStructuralNode> cluster : this.nodeClusters) {
        if (cluster.isMember(item)) {
          return cluster.getRepresentant();
        }
      }
    }
    throw new IllegalArgumentException("Node " + item.toString() + " is not in clusters, it wasn't added, i can't find it.");
  }

  @Override
  public List<Cluster<AbstractStructuralNode>> getClusters() {
    return Collections.unmodifiableList(this.nodeClusters);
  }

  @Override
  public List<Cluster<Attribute>> getAttributeClusters(final AbstractStructuralNode representant) {
    if (this.attributeClusterers.containsKey(representant)) {
      return Collections.unmodifiableList(this.attributeClusterers.get(representant).getClusters());
    }
    return Collections.<Cluster<Attribute>>emptyList();
  }
}
