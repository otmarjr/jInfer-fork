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
 * Class representing attributes of {@link RXMLTree} nodes. This attributes
 * contains weight of node, reliability and set of {@link Tuple tuples} the node 
 * belongs to.
 * @author sviro
 */
public class NodeAttribute {
  
  private Set<Tuple> tuples;
  private boolean reliability;
  private double weight;
  
  /**
   * Constructor setting reliability of the node.
   * @param reliability Reliability to be set.
   */
  public NodeAttribute(final boolean reliability) {
    this.reliability = reliability;
    this.tuples = new HashSet<Tuple>();
  }
  
  /**
   * Default constructor.
   */
  public NodeAttribute() {
    this(true);
  }

  /**
   * Get set of tuples the node belongs to.
   * @return Set of tuples.
   */
  public Set<Tuple> getTuples() {
    return tuples;
  }

  /**
   * Get reliability of the node.
   * @return true if node is reliable, otherwise return false.
   */
  public boolean isReliable() {
    return reliability;
  }

  /**
   * Check if node is part of the provided tuple.
   * @param tuple Tuple to be checked that the node is part of.
   * @return true if node is part of tuple, otherwise return false.
   */
  public boolean isInTuple(final Tuple tuple) {
    return tuples.contains(tuple);
  }

  /**
   * Remove this node from provided tuple.
   * @param tuple Tuple to be node removed from. 
   * @return true if the node was part fo the provided tuple.
   */
  public boolean removeFromTuple(final Tuple tuple) {
    return tuples.remove(tuple);
  }

  /**
   * Add this node to the provided tuple
   * @param tuple Tuple to be node added to.
   */
  public void addToTuple(final Tuple tuple) {
    tuples.add(tuple);
  }

  /**
   * Set the weight of the node.
   * @param weight Weight to be set to the node.
   */
  public void setWeight(final double weight) {
    this.weight = weight;
  }

  /**
   * Get weight of the node.
   * @return weight of the node.
   */
  public double getWeight() {
    return weight;
  }

  /**
   * Set the reliability of the node.
   * @param reliability Reliability to be set to node.
   */
  public void setReliability(final boolean reliability) {
    this.reliability = reliability;
  }

  /**
   * Remove node from all tuples it belongs to.
   */
  public void removeFromAllTuples() {
    tuples.clear();
  }
}