/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.base.objects;

/**
 * Class representing a generic 2-tuple. Immutable class.
 * 
 * @author vektor
 */
public class Pair<S, T> {

  private final S first;
  private final T second;

  /**
   * Full constructor. Both members of this pair must be specified.
   * @param first First member of this pair.
   * @param second Second member of this pair.
   */
  public Pair(final S first, final T second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Returns the first member of this pair.
   * 
   * @return First member of this pair.
   */
  public S getFirst() {
    return first;
  }
  
  /**
   * Returns the second member of this pair.
   *
   * @return Second member of this pair.
   */
  public T getSecond() {
    return second;
  }

  @Override
  public String toString() {
    return "Pair:" + first.toString() + ":" + second.toString() + ";";
  }
}
