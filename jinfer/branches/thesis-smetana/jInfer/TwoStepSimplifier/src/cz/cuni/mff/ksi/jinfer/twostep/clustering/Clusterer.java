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
package cz.cuni.mff.ksi.jinfer.twostep.clustering;

import java.util.Collection;
import java.util.List;

/**
 * Interface for clustering algorithms implementations.
 *
 * Actual implementors will
 * probably use AbstractNode as generic class for Clusterer and differentiate
 * Nodes coming for clustering in runtime.
 * <p>
 * Purpose of clustering is to cluster elements based on some criterion into clusters -
 * generally same name. Sometimes elements with same name appear in documents with
 * different semantics, sometimes misspelled element names in documents causes
 * semantically same elements to have different names.
 * <p>
 * Clusterer have to deal with these issues.
 * <p>
 * As method getRepresentantForItem is used for getting one representant of
 * element/simpledata/attribute when adding steps into automaton (which have to
 * be A.equals(B) when node A and B are in same cluster), clusterer have to
 * parse elements right sides. Maybe by just doing:
 * <pre>
 * for (Node x : queue) {
 *   if (x.isElement) {
 *     this.addAll(((Element) x).getSubnodes().getTokens());
 *   }
 * }
 * </pre>
 * When automaton is created, getRepresentantForItem() is called for everything on
 * elements right side of rule. So Clusterer have to deal with SimpleData (one cluster
 * for all simpledata nodes), attributes are omitted in automaton creation,
 * can be omitted in clusterer. For those who wish to write simplifier with
 * attributes handling, take a look at ClustererWithAttributes interface.
 * <p>
 * Each item has to be in exactly one cluster (that's what clustering is all
 * about).
 *
 * @author anti
 */
public interface Clusterer<T> {

  /**
   * Add x to some clusterer, enqueue for processing. Don't implement clustering here,
   * has to be in cluster() method to enable thread interruption.
   * @param item to add
   */
  void add(final T item);

  /**
   * Add the whole collection to queue for clustering
   * @param items elements to add
   */
  void addAll(final Collection<T> items);

  /**
   * Do the main job, cluster enqueued items into clusters. But don't throw away old items.
   * If there are already some items in clusters, user suppose they didn't disappeared.
   * <p>
   * Example: add(x), add(y), add(xx), add(yx), enqueued items: x, y, xx, yx. Calling
   * cluster() creates clusters for example based on starting letter. Creates two clusters:
   * (x, xx) | (y, yx)
   * <p>
   * Now let user use add(xd), add(zz). Calling cluster() again have to result in
   * (x, xx, xd) | (y, yx) | (zz)
   * <p>
   * Of course, if cluster criterion is not so stable as first letter, items x, xx, y, yx
   * can change their clusters and so. Point is, that they don't disappear. Once an item is added
   * clusterer has to hold it for future cluster() calls.
   *
   * <code>
   * cluster method has to check for interruption of thread by using:
   *  if (Thread.interrupted()) {
   *    throw new InterruptedException();
   *  }
   * </code>
   * in some main loop.
   *
   */
  void cluster() throws InterruptedException;

  /**
   * Return representative of the item's cluster
   * @param item
   * @return
   */
  T getRepresentantForItem(final T item);

  /**
   * Without doing clustering again, return result of last cluster() call.
   * @return all clusters obtained by last call of {@link cluster}
   */
  List<Cluster<T>> getClusters();
}
