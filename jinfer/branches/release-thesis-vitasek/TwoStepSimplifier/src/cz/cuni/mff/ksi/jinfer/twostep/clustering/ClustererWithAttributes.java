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

import java.util.List;

/**
 * Extending {@link Clusterer} interface with attributes in mind.
 *
 * Interface for clustering algorithms implementations. Actual implementors will
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
 * As method getRepresentantForItem is used for getting one representative of
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
 * for all simpledata nodes).
 * <p>
 * This Clusterer have to do more work on attributes. For each cluster, it has
 * to collect attributes in cluster members and cluster attributes separately,
 * by some criterion. Then simplifier may ask for all attributes clusters by
 * giving representative to getAttributeClusters method.
 * <p>
 * Clusterer may decide that attribute should be converted into element, and
 * update clusters accordingly (removing attributes, adding elements with same
 * content to appropriate element cluster). There should be log message to user
 * about such a decision, may be consulted with user in some way.
 * <p>
 * It's up to simplifier, to decide what to do with attributes (simplifying).
 * <p>
 * Each item has to be in exactly one cluster.
 *
 * @author anti
 */
public interface ClustererWithAttributes<T, S> extends Clusterer<T> {

  /**
   * Returns all clusters of attributes for a given representative of cluster (for
   * a given element cluster de facto). Attributes have to be collected from
   * elements.
   * @param representant
   * @return
   */
  List<Cluster<S>> getAttributeClusters(T representant);
}
