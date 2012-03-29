/*
 * Copyright (C) 2012 rio
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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns;

/**
 * A representation of classified join patterns occurrences.
 * 
 * The classification involves assigning a join pattern occurrence with one of
 * O1 and O2 types and with weight. For details see PDF documentation.
 * 
 * @author rio
 */
public class ClassifiedJoinPattern {
  
  public enum Type {
    O1,
    O2
  }
  
  private final JoinPattern joinPattern;
  private final int weight; // Multiplied by 100.
  private final Type type;
  
  public ClassifiedJoinPattern(final JoinPattern joinPattern, final Type type, final int weight) {
    this.joinPattern = joinPattern;
    this.type = type;
    this.weight = weight;
  }
  
  public Type getType() {
    return type;
  }
  
  public int getWeight() {
    return weight;
  }
  
  public JoinPattern getJoinPattern() {
    return joinPattern;
  }
}
