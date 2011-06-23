/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sviro
 */
public class NodeAttribute {
  
  private Set<Tuple> tuples;
  private boolean reliability;
  
  public NodeAttribute(final boolean reliability) {
    this.reliability = reliability;
    this.tuples = new HashSet<Tuple>();
  }
  
  public NodeAttribute() {
    this(true);
  }

  public Set<Tuple> getTuples() {
    return tuples;
  }

  public boolean isReliable() {
    return reliability;
  }

  boolean isInTuple(Tuple tuple) {
    return tuples.contains(tuple);
  }

  boolean removeFromTuple(Tuple tuple) {
    return tuples.remove(tuple);
  }

  void addToTuple(Tuple tuple) {
    tuples.add(tuple);
  }
  
}
