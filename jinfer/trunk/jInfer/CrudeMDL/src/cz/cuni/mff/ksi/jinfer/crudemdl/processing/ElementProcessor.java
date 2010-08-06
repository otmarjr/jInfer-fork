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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import java.util.List;

/**
 * Interface representing one box in chain. Process one element at a time, given
 * clusterer which can respond to getClusterRepresentant request and getClusters
 * request. And whole cluster containing instances of element found in documents.
 *
 * Implementors have to take care about thread interruptions. Check for it, and
 * throw InterruptedException in some inner main loop. Example:
 * for (...) {
 *   if (Thread.interrupted()) {
 *     throw new InterruptedException();
 *   }
 * }
 * 
 * @author anti
 */
public interface ElementProcessor<T> {
  AbstractNode processElement(
          final Clusterer<T> clusterer,
          final Cluster<T> cluster
          ) throws InterruptedException;
}
