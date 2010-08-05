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

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti comment
 *
 * @author anti
 */
public class InameClusterer implements Clusterer<AbstractNode> {
  private final List<Cluster<AbstractNode>> clusters;

  public InameClusterer() {
    this.clusters= new LinkedList<Cluster<AbstractNode>>();
  }

  /*
   * Add action, add item to some cluster. In our method - elements are clustered by
   * name (ignore case). DeFacto clustering happens at this method - when adding, all
   * clusters are scanned, if there is one with representant of same name, item is added
   * to it. If not, new cluster with item as representant is added.
   */
  @Override
  public void add(final AbstractNode item) {
    final Iterator<Cluster<AbstractNode>> iterator= this.clusters.iterator();
    boolean found= false;
    while (iterator.hasNext()&&(!found)) {
      final Cluster<AbstractNode> cluster= iterator.next();
      final AbstractNode representant= cluster.getRepresentant();
      if (representant.isSimpleData()) {
        cluster.add(item);
        found= true;
      } else if (
              representant.isElement() &&
              representant.getName().equalsIgnoreCase(item.getName())
              ) {
          cluster.add(item);
          found= true;
      }
    }
    if (!found) {
      this.clusters.add(
              new Cluster<AbstractNode>(item)
              );
    }
  }

  @Override
  public void addAll(final List<AbstractNode> items) {
    for (AbstractNode node : items) {
      this.add(node);
    }
  }

  /*
   * In this method no magic is found, clustering happens already when items are added.
   */
  @Override
  public List<Cluster<AbstractNode>> cluster() {
    return this.clusters;
  }

  @Override
  public AbstractNode getRepresentantForItem(final AbstractNode item) {
    for (Cluster<AbstractNode> cluster : this.clusters) {
      if (cluster.isMember(item)) {
        return cluster.getRepresentant();
      }
    }
    throw new IllegalArgumentException("Node " + item.toString() + " is not in clusters, it wasn't added, i can't find it.");
  }
}
