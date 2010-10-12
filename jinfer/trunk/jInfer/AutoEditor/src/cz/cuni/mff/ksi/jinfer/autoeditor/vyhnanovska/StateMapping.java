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

/**
 * Maps states, ellipses and points for the plotting purposes.
 *
 * @author Julie Vyhnanovska
 *
 */
public class StateMapping<T> {

	private final State<T>[]	states;

	private final Point[]		points;

	private int					actual	= 0;

	public StateMapping(final int size) {
		states = new State[size];
		points = new Point[size];
	}

	public void add(final State<T> state, final Point point) {
		if (actual < states.length) {
			states[actual] = state;
			points[actual] = point;
			actual++;
		}
	}

	public State<T> getState(final Point point) {
		for (int i = 0; i < points.length; i++) {
          if (points[i] == null) {
            continue;
          }
			if (points[i].equals(point))
				return states[i];
		}
		return null;
	}

	public Point getPoint(final State<T> state) {
		for (int i = 0; i < states.length; i++) {
			if (states[i] == state)
				return points[i];
		}
		return null;
	}
}