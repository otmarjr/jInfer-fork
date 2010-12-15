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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class representing one cluster. Holds all members and representant.
 *
 *
 * @author anti
 */
public class Cluster<T> {
  private T representant;
  private final Set<T> members;

  public Cluster() {
    this.members= new LinkedHashSet<T>();
    this.representant= null;
  }

  public Cluster(final T representant) {
    this();
    this.representant= representant;
    this.members.add(representant);
  }

  /**
   * @return the representant
   */
  public T getRepresentant() {
    return representant;
  }

  /**
   * @param representant the representant to set
   */
  public void setRepresentant(final T representant) {
    if (!this.members.contains(representant)) {
      throw new IllegalArgumentException("Trying to set representant to item, which is not in this cluster.");
    }
    this.representant = representant;
  }

  /**
   * @return the members
   */
  public Set<T> getMembers() {
    return Collections.unmodifiableSet(this.members);
  }

  public Boolean isMember(final T item) {
    return this.members.contains(item);
  }

  public void add(final T item) {
    this.members.add(item);
  }

  public int size() {
    return this.members.size();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Cluster ");
    sb.append("representant: ");
    sb.append(this.representant);
    sb.append("\nmembers: ");
    for (T member: this.members) {
      sb.append(member);
    }
    return sb.toString();
  }
}
