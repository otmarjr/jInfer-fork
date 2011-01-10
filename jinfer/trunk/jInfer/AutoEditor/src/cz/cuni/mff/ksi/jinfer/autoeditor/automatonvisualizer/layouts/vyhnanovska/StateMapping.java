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
package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.vyhnanovska;

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps {@link State}s and {@link Coordinate}s for the plotting purposes.
 *
 * @author Julie Vyhnanovska, rio
 */
public class StateMapping<T> {

  private final List<State<T>> states;
  private final List<Coordinate> stateCoordinates;

  /**
   * Number of states must be known before creation and has to be specified
   * in this constructor.
   *
   * @param numberOfStates Number of states we would like to map.
   */
  public StateMapping(final int numberOfStates) {
    states = new ArrayList<State<T>>(numberOfStates);
    stateCoordinates = new ArrayList<Coordinate>(numberOfStates);
  }

  /**
   * Adds {@link State} &lt;--&gt; {@link Coordinate} mapping.
   *
   * @param state
   * @param coordinate
   */
  public void addStateCoordinate(final State<T> state, final Coordinate coordinate) {
    states.add(state);
    stateCoordinates.add(coordinate);
  }

  /**
   * Get {@link State} mapped with a specified {@link Coordinate}.
   *
   * @param coordinate {@link Coordinate} mapped with desired {@link State}.
   * @return {@link State} mapped with specified {@link Coordinate}.
   */
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

  /**
   * Get {@link Coordinate} mapped with a specified {@link State}.
   *
   * @param state {@link State} mapped with desired {@link Coordinate}.
   * @return {@link Coordinate} mapped with specified {@link State}.
   */
  public Coordinate getStateCoordinate(final State<T> state) {
    for (int i = 0; i < states.size(); i++) {
      if (states.get(i) == state) {
        return stateCoordinates.get(i);
      }
    }
    return null;
  }
}
