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

import java.util.HashSet;
import java.util.Set;

/**
 * TODO anti comment
 *
 * @author anti
 */
public class Cluster<T> {
  private T representant;
  private Set<T> members;

  public Cluster(final T representant) {
    this.representant= representant;
    this.members= new HashSet<T>();
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
    this.representant = representant;
  }

  /**
   * @return the members
   */
  public Set<T> getMembers() {
    return this.members;
  }

  /**
   * @param members the members to set
   */
  public void setMembers(final Set<T> members) {
    this.members = members;
  }

  public Boolean isMember(final T item) {
    return this.members.contains(item);
  }

  public void add(final T item) {
    this.members.add(item);
  }

  @Override
  public String toString() {
//    return super.toString();
    final StringBuilder sb = new StringBuilder("Cluster\n");
    sb.append("representant: ");
    sb.append(this.representant);
    sb.append("\nmembers: ");
    for (T member: this.members) {
      sb.append(member);
    }
    return sb.toString();
  }
}
