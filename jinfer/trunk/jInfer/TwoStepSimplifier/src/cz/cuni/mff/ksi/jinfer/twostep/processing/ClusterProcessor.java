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
package cz.cuni.mff.ksi.jinfer.twostep.processing;

import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import java.util.List;

/**
 * Process cluster of elements.
 *
 * Interface representing one box in chain. Process one element at a time, given
 * clusterer which can respond to {@link Clusterer.getClusterRepresentant} request and
 * {@link Clusterer.getClusters} request.
 * And whole cluster containing instances of element found in documents.
 * <p>
 * Implementors have to take care about thread interruptions. Check for it, and
 * throw InterruptedException in some inner main loop. Example:
 * <pre>
 * for (...) {
 *   if (Thread.interrupted()) {
 *     throw new InterruptedException();
 *   }
 * }
 * </pre>
 * 
 * @author anti
 */
public interface ClusterProcessor<T> {

  /**
   * Do the job - given clusterer and rules, process rules to obtain one representative
   *
   * @param clusterer used to cluster rules
   * @param rules of one cluster to process
   * @return representative which have common parameters of cluster
   * @throws InterruptedException
   */
  T processCluster(
          final Clusterer<T> clusterer,
          final List<T> rules) throws InterruptedException;
}
