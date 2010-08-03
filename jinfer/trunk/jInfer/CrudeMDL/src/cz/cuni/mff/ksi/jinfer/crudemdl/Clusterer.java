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

import java.util.List;

/**
 *
 * @author anti
 */
public interface Clusterer<T> {
  /*
   * Add x to some cluster, find the right one or create new. Depends on clustering algorithm.
   * One can do classify on demand, or can create a method that builds just one cluster on classify
   * and then call some own method to 'reclassify' - do the real clustering. Opposite is also in mind
   * do cluster for each x, then apply own merging clustering algorithm. The algorithms should be then
   * added in own methods.
   */
  public void add(T item);
  public void addAll(List<T> items);
  public List<Cluster<T>> cluster();
  public T getRepresentantForItem(T item);
}
