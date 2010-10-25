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
import java.util.ArrayList;
import java.util.List;

/** TODO rio refactor
 * Maps states, ellipses and stateCoordinates for the plotting purposes.
 *
 * @author Julie Vyhnanovska
 *
 */
public class StateMapping<T> {

  private final List<State<T>> states;
  private final List<Coordinate> stateCoordinates;

  public StateMapping(final int numberOfStates) {
    states = new ArrayList<State<T>>(numberOfStates);
    stateCoordinates = new ArrayList<Coordinate>(numberOfStates);
  }

  public void addStateCoordinate(final State<T> state, final Coordinate coordinate) {
    states.add(state);
    stateCoordinates.add(coordinate);
  }

  public State<T> getStateAtCoordinate(final Coordinate coordinate) {
    for (int i = 0; i < stateCoordinates.size(); ++i) {
      if (stateCoordinates.get(i) == null) {
        continue;
      }
      if (stateCoordinates.get(i).equals(coordinate)) {
        return states.get(i);
      }
    }
    return null;
  }

  public Coordinate getStateCoordinate(final State<T> state) {
    for (int i = 0; i < states.size(); i++) {
      if (states.get(i) == state) {
        return stateCoordinates.get(i);
      }
    }
    return null;
  }
}
