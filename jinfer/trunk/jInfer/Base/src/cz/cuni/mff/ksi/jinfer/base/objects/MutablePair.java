/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.base.objects;

/**
 * Class representing a generic 2-tuple. Mutable class (contrast {@link Pair}).
 *
 * @param <S> Type argument of the first object in the pair.
 * @param <T> Type argument of the second object in the pair.
 *
 * @author vektor
 */
public class MutablePair<S, T> {

  private S first;
  private T second;

  /**
   * Full constructor. Both members of this pair must be specified.
   *
   * @param first First member of this pair.
   * @param second Second member of this pair.
   */
  public MutablePair(final S first, final T second) {
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
   * Sets the first member of this pair.
   *
   * @param first First member of this pair.
   */
  public void setFirst(final S first) {
    this.first = first;
  }

  /**
   * Returns the second member of this pair.
   *
   * @return Second member of this pair.
   */
  public T getSecond() {
    return second;
  }

  /**
   * Sets the second member of this pair.
   *
   * @param second Second member of this pair.
   */
  public void setSecond(final T second) {
    this.second = second;
  }

  @Override
  public String toString() {
    return "Pair:" + first.toString() + ":" + second.toString() + ";";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof MutablePair)) {
      return false;
    }
    @SuppressWarnings("unchecked")
    final MutablePair<S, T> other = (MutablePair) obj;
    final boolean firstEqual = equal(first, other.getFirst());
    final boolean secondEqual = equal(second, other.getSecond());
    return firstEqual && secondEqual;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.first != null ? this.first.hashCode() : 0);
    hash = 97 * hash + (this.second != null ? this.second.hashCode() : 0);
    return hash;
  }

  private static <X> boolean equal(final X arg1, final X arg2) {
    if (arg1 == null && arg1 == null) {
      return true;
    }
    if (arg2 == null) {
      return false;
    }
    return arg1.equals(arg2);
  }

}
