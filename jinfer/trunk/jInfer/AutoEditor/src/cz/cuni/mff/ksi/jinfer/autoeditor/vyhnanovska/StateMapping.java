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
import java.awt.geom.Ellipse2D;

/**
 * Maps states, ellipses and points for the plotting purposes.
 *
 * @author Julie Vyhnanovska
 *
 */
public class StateMapping<T> {

	private final State<T>[]	states;

	private final Ellipse2D[]	ellipses;

	private final int[][]		points;

	private int					actual	= 0;

	public StateMapping(final int size) {
		states = new State[size];
		ellipses = new Ellipse2D[size];
		points = new int[size][2];
	}

	public void add(final State<T> state, final Ellipse2D ellipse, final int[] point) {
		if (actual < states.length) {
			states[actual] = state;
			ellipses[actual] = ellipse;
			points[actual] = point;
			actual++;
		}
	}

	public State<T> getState(final int[] point) {
		for (int i = 0; i < points.length; i++) {
			if (points[i][0] == point[0] && points[i][1] == point[1])
				return states[i];
		}
		return null;
	}

	public State<T> getState(final Ellipse2D ellipse) {
		for (int i = 0; i < ellipses.length; i++) {
			if (ellipses[i] == ellipse)
				return states[i];
		}
		return null;
	}

	public Ellipse2D getEllipse(final int[] point) {
		for (int i = 0; i < points.length; i++) {
			if (points[i][0] == point[0] && points[i][1] == point[1])
				return ellipses[i];
		}
		return null;
	}

	public Ellipse2D getEllipse(final State<T> state) {
		for (int i = 0; i < states.length; i++) {
			if (states[i] == state)
				return ellipses[i];
		}
		return null;
	}

	public int[] getPoint(final Ellipse2D ellipse) {
		for (int i = 0; i < ellipses.length; i++) {
			if (ellipses[i] == ellipse)
				return points[i];
		}
		return null;
	}

	public int[] getPoint(final State<T> state) {
		for (int i = 0; i < states.length; i++) {
			if (states[i] == state)
				return points[i];
		}
		return null;
	}
}