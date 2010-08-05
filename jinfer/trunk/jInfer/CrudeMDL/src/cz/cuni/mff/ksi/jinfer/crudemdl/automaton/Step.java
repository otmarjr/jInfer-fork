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
 * TODO anti Comment!
 *
 * @author anti
 */
public class Step<T> {
  // TODO anti Comment the fields
  private T acceptSymbol;
  private Integer useCount;
  private State<T> source;
  private State<T> destination;

  public Step(final T acceptSymbol, final State<T> source, final State<T> destination, final Integer useCount) {
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
  public Integer getUseCount() {
    return useCount;
  }

  /**
   * @param useCount the useCount to set
   */
  public void setUseCount(final Integer useCount) {
    this.useCount = useCount;
  }

  public void incUseCount() {
    this.incUseCount(1);
  }

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
    sb.append(this.acceptSymbol.toString().replaceAll("\n", ""));
    sb.append("|");
    sb.append(this.useCount);
    sb.append("} to ");
    sb.append(this.destination.getName());
    sb.append("\n");
    return sb.toString();
  }
}
