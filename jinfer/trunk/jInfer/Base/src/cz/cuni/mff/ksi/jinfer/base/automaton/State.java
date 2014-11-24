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

package cz.cuni.mff.ksi.jinfer.base.automaton;

/**
 * Class representing state in deterministic finite automaton.
 * has fields- finalCount - number the times its final state, how
 * many input strings ended in this state
 *
 * State is just a nearly
 * empty box used as reference for .equals() when traversing automaton and so on.
 * But object with some properties is more useful then representation by numbers
 * for example.
 * 
 * name - simple integer to name states correctly in log output and
 * visualization
 * 
 * parentAutomaton
 *
 * @author anti
 */
public class State<T> {
  /**
   * finalCount - number the times the state was final for some input string.
   */
  private int finalCount;
  /**
   * Just unique number as name, to visualize and log automaton correctly.
   */
  private int name;

  /**
   * Only one constructor, setting all states parameters.
   * 
   * @param finalCount
   * @param name
   * @param parentAutomaton
   */
  public State(final int finalCount, final int name) {
    this.finalCount= finalCount;
    this.name= name;
  }

  /**
   * @return the finalCount
   */
  public int getFinalCount() {
    return finalCount;
  }
  
  public boolean isFinal(){
      return this.finalCount > 0;
  }

  /**
   * @param finalCount the finalCount to set
   */
  public void setFinalCount(final int finalCount) {
    this.finalCount = finalCount;
  }

  /**
   * increment finalCount by 1
   */
  public void incFinalCount() {
    this.incFinalCount(1);
  }

  /**
   * increment finalCount
   *
   * @param i
   */
  public void incFinalCount(final int i) {
    this.setFinalCount(this.getFinalCount() + i);
  }

  /**
   * @return the name
   */
  public int getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final int name) {
    this.name = name;
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

  public String toTestString() {
    final StringBuilder sb = new StringBuilder("[");
    sb.append(this.getName());
    sb.append("|");
    sb.append(this.finalCount);
    sb.append("]");
    return sb.toString();
  }
}
