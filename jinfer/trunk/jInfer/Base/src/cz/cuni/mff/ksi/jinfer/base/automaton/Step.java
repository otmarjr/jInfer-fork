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
 * Class representing step sin finite automaton.
 * It's just a container for .equals() runs in automaton.
 *
 * But also has useful information, source, destination - this is the only place
 * where such information in automaton is held. Although automaton has
 * map<state, steps>, it has no direct information about where the steps have
 * the other ends.
 *
 * Step also holds acceptSymbol, which is whatever object on which the automaton
 * is build on.
 *
 * useCount is just integer representing times the step was used when automaton
 * was builded as PTA.
 *
 * @author anti
 */
public class Step<T> {
  /**
   * Whatever object the automaton is build on. When doing comparisons, automaton
   * uses .equals on symbols too. So take care that same symbols really yields
   * .equals.
   */
  private T acceptSymbol;
  /**
   * Number the times transition was used when builded as PTA. Its useful as
   * probability in some simplification methods.
   */
  private int useCount;
  /**
   * State source of transition
   */
  private State<T> source;
  /**
   * And destination, simple.
   */
  private State<T> destination;

  /**
   * All settings at one constructor. In time of creating step, states from and to
   * which it should point, have to be already instantiated.
   *
   * @param acceptSymbol
   * @param source
   * @param destination
   * @param useCount
   */
  public Step(final T acceptSymbol, final State<T> source, final State<T> destination, final int useCount) {
    this.acceptSymbol= acceptSymbol;
    this.useCount= useCount;
    this.source= source;
    this.destination= destination;
  }

  /**
   * @return the acceptSymbol
   */
  public T getAcceptSymbol() {
    return acceptSymbol;
  }

  /**
   * @param acceptSymbol the acceptSymbol to set
   */
  public void setAcceptSymbol(final T acceptSymbol) {
    this.acceptSymbol = acceptSymbol;
  }

  /**
   * @return the useCount
   */
  public int getUseCount() {
    return useCount;
  }

  /**
   * @param useCount the useCount to set
   */
  public void setUseCount(final int useCount) {
    this.useCount = useCount;
  }

  /**
   * Increment useCount by one, useful in PTA build procedure
   */
  public void incUseCount() {
    this.incUseCount(1);
  }

  /**
   * Increment by arbitrary number. Useful when collapsing steps such that
   * A -2-> B
   * A -5-> B
   * to one step
   * A -7-> B
   * 
   * @param i
   */
  public void incUseCount(final Integer i) {
    this.setUseCount(this.getUseCount() + i);
  }

  /**
   * @return the source
   */
  public State<T> getSource() {
    return source;
  }

  /**
   * @param source the source to set
   */
  public void setSource(final State<T> source) {
    this.source = source;
  }

  /**
   * @return the destination
   */
  public State<T> getDestination() {
    return destination;
  }

  /**
   * @param destination the destination to set
   */
  public void setDestination(final State<T> destination) {
    this.destination = destination;
  }

  @Override
  public String toString() {
    //return super.toString();
    final StringBuilder sb = new StringBuilder("from ");
    sb.append(this.source.getName());
    sb.append(" on {");
    sb.append(this.acceptSymbol.toString());
    sb.append("|");
    sb.append(this.useCount);
    sb.append("} to ");
    sb.append(this.destination.getName());
    return sb.toString();
  }

  public String toTestString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.source.getName());
    sb.append("--");
    sb.append(this.acceptSymbol.toString());
    sb.append("|");
    sb.append(this.useCount);
    sb.append("--");
    sb.append(this.destination.getName());
    return sb.toString();
  }
}
