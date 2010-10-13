/*
 *  Copyright (C) 2010 rio
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
package cz.cuni.mff.ksi.jinfer.autoeditor.vyhnanovska;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;

/** TODO rio refactor
 * Maps states, ellipses and stateCoordinates for the plotting purposes.
 *
 * @author Julie Vyhnanovska
 *
 */
public class StateMapping<T> {

  private final State<T>[] states;
  private final Coordinate[] stateCoordinates;
  private int actual = 0;

  public StateMapping(final int numberOfStates) {
    states = new State[numberOfStates];
    stateCoordinates = new Coordinate[numberOfStates];
  }

  public void addStateCoordinate(final State<T> state, final Coordinate coordinate) {
    assert(actual < states.length);

    states[actual] = state;
    stateCoordinates[actual] = coordinate;
    ++actual;
  }

  public State<T> getStateAtCoordinate(final Coordinate coordinate) {
    for (int i = 0; i < stateCoordinates.length; ++i) {
      if (stateCoordinates[i] == null) {
        continue;
      }
      if (stateCoordinates[i].equals(coordinate)) {
        return states[i];
      }
    }
    return null;
  }

  public Coordinate getStateCoordinate(final State<T> state) {
    for (int i = 0; i < states.length; i++) {
      if (states[i] == state) {
        return stateCoordinates[i];
      }
    }
    return null;
  }
}
