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

package cz.cuni.mff.ksi.jinfer.crudemdl.automaton;

/**
 * Class representing state in deterministic finite automaton.
 * has fields- finalCount - number the times its final state, how
 * many input strings ended in this state
 * 
 * name - simple integer to name states correctly in log output and
 * visualization
 * 
 * parentAutomaton
 *
 * @author anti
 */
public class State<T> implements Comparable<State<T>> {
  private Integer finalCount;
  private Integer name;
  private Automaton<T> parentAutomaton;

  State(final Integer finalCount, final Integer name, final Automaton<T> parentAutomaton) {
    this.finalCount= finalCount;
    this.name= name;
    this.parentAutomaton= parentAutomaton;
  }

  /**
   * @return the finalCount
   */
  public Integer getFinalCount() {
    return finalCount;
  }

  /**
   * @param finalCount the finalCount to set
   */
  public void setFinalCount(final Integer finalCount) {
    this.finalCount = finalCount;
  }

  public void incFinalCount() {
    this.incFinalCount(1);
  }

  public void incFinalCount(final Integer i) {
    this.setFinalCount(this.getFinalCount() + i);
  }

  /**
   * @return the name
   */
  public Integer getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final Integer name) {
    this.name = name;
  }

  /**
   * @return the myAutomaton
   */
  public Automaton<T> getParentAutomaton() {
    return parentAutomaton;
  }

  /**
   * @param parentAutomaton the myAutomaton to set
   */
  public void setParentAutomaton(final Automaton<T> parentAutomaton) {
    this.parentAutomaton = parentAutomaton;
  }

  @Override
  public String toString() {
  //  return super.toString();
    final StringBuilder sb = new StringBuilder("[");
    sb.append(this.getName());
    sb.append("|");
    sb.append(this.finalCount);
    sb.append("]");
    return sb.toString();
  }

  @Override
  public int compareTo(final State<T> o) {
    if (o.getName() > this.name) {
      return -1;
    } else if (o.getName() < this.name) {
      return 1;
    } else {
      return 0;
    }
  }
}
