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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;

/**
 * Class representing a generic 2-tuple. Immutable class.
 *
 * @param <S> Type argument of the first object in the pair.
 * @param <T> Type argument of the second object in the pair.
 *
 * @author vektor
 */
public class ImmutablePair<S, T> implements Pair<S, T> {

  private final S first;
  private final T second;

  /**
   * Full constructor. Both members of this pair must be specified.
   * @param first First member of this pair.
   * @param second Second member of this pair.
   */
  public ImmutablePair(final S first, final T second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public S getFirst() {
    return first;
  }

  @Override
  public T getSecond() {
    return second;
  }

  @Override
  public String toString() {
    return "Pair:" + first.toString() + ":" + second.toString() + ";";
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Pair)) {
      return false;
    }
    @SuppressWarnings("unchecked")
    final Pair<S, T> other = (Pair) obj;
    final boolean firstEqual = BaseUtils.equal(first, other.getFirst());
    final boolean secondEqual = BaseUtils.equal(second, other.getSecond());
    return firstEqual && secondEqual;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.first != null ? this.first.hashCode() : 0);
    hash = 97 * hash + (this.second != null ? this.second.hashCode() : 0);
    return hash;
  }
}
