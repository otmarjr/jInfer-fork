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

import java.util.List;

/**
 * Interface for clustering algorithms implementations. Actual implementors will
 * probably use AbstractNode as generic class for Clusterer and differentiate
 * Nodes coming for clustering in runtime.
 *
 * Purpose of clustering is to cluster elements based on some criterion into clusters -
 * generally same name. Sometimes elements with same name appear in documents with
 * different semantics, sometimes misspelled element names in documents causes
 * semantically same elements to have different names.
 *
 * Clusterer have to deal with these issues.
 *
 * As method getRepresentantForItem is used for getting one representant of
 * element/simpledata/attribute when adding steps into automaton (which have to
 * be A.equals(B) when node A and B are in same cluster), clusterer have to
 * parse elements right sides. Maybe by just doing:
 * for (Node x : queue) {
 *   if (x.isElement) {
 *     this.addAll(((Element) x).getSubnodes().getTokens());
 *   }
 * }
 * When automaton is created, getRepresentantForItem() is called for everything on
 * elements right side of rule. So Clusterer have to deal with SimpleData (one cluster
 * for all simpledata nodes), attributes
 *
 * TODO anti Comment! what about attributes when done with attributes
 *
 * Each item has to be in exactly one cluster, that's what clustering is all about.
 *
 * @author anti
 */
public interface ClustererWithAttributes<T> extends Clusterer<T> {
   /**
    * TODO anti Comment
    * @param representant
    * @return
    */
   List<Cluster<T>> getAttributeClusters(T representant);
}