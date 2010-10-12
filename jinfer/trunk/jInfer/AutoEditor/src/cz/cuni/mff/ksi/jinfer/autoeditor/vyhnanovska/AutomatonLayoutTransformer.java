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

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import org.apache.log4j.Logger;

/**
 * Transforms the state to a point on which it should be plotted. - A visualisation tool
 *
 * @author Julie Vyhnanovska
 *
 */
public class AutomatonLayoutTransformer<T> implements Transformer<State<T>, Point2D> {

  // TODO rio rename to LOG
	private static Logger log = Logger.getLogger(AutomatonLayoutTransformer.class);

	private final int			minXsize;

	private final int			minYsize;

	private final int			squareSize;

	private final int[]			gridAxes;

	private final StateMapping	stateGridMapping;

	private static final double	FILL_FACTOR	= 1.3;

    // TODO rio vyhodit automaton
    private Automaton<T> automaton;

    // TODO rio vyhodit automaton
	public AutomatonLayoutTransformer(final int minXsize, final int minYsize, final int square_size, final Graph<State<T>, Step<T>> graph, final Automaton<T> automaton) {
        this.automaton = automaton;
		this.minXsize = minXsize;
		this.minYsize = minYsize;
		this.squareSize = square_size;
		final int vertexCount = graph.getVertexCount();
		gridAxes = computeGridSize((int) Math.round(vertexCount * FILL_FACTOR));
		stateGridMapping = new StateMapping(vertexCount);
	}

	public Dimension getDimension() {
		return new Dimension(gridAxes[0] * squareSize, gridAxes[1] * squareSize);
	}

	@Override
	public Point2D transform(final State<T> state) {
		log.info("paint " + state);
        // TODO rio getId() == 1 znamena, ze je pociatocny??
		//if (state.getId() == 1) {
        if (state.equals(automaton.getInitialState())) {
			final Point statePosition = new Point(1, 1);
			stateGridMapping.add(state, statePosition);
			return getPoint(statePosition);
		}
		Point prev = null;
		for (final Step<T> backEdge : automaton.getReverseDelta().get(state)) {
			prev = stateGridMapping.getPoint(backEdge.getSource());
			if (prev != null) {
				break;
			}
		}
		if (prev == null) {
			prev = new Point(1, 1);
		}

		Point nextI = prev;
		log.info("nextI: " + nextI.getX() + " " + nextI.getY());
		while (stateGridMapping.getState(nextI) != null || (nextI.equals(new Point(1, 1)))) {
			log.info("obsazeno!");
			nextI = getNextI(prev, nextI, gridAxes);
			if (nextI != null) {
				log.info("nextI: " + nextI.getX() + " " + nextI.getY());
				if (nextI.getX() < 1 || nextI.getY() < 1) {
					log.error("invalid grid index: " + nextI.getX() + " " + nextI.getY());
					return null;
				}
			}
		}

		if (nextI != null) {
			stateGridMapping.add(state, nextI);
			return getPoint(nextI);
		}

		return null;
	}

	private Point2D getPoint(final Point i) {
		final double x = i.getX() * squareSize - squareSize / 2;
		final double y = i.getY() * squareSize - squareSize / 2;
		return new Point2D.Double(x, y);
	}

	private int[] computeGridSize(final int minGridSize) {
		final int[] axesSize = new int[2];

		if (minGridSize < (minXsize * minYsize)) {
			axesSize[0] = minXsize;
			axesSize[1] = minYsize;
		}
		else {
			axesSize[1] = (int) Math.round(Math.sqrt(minGridSize * minYsize / minXsize));
			axesSize[0] = (int) Math.floor(minGridSize / axesSize[1]) + 1;
		}

		return axesSize;
	}

	/*
	 * Vrati dalsi pole pro umisteni vrcholu, se stredem hledani ve start a aktualni pozici actual. Pohybujeme se v
	 * mantinelech gridAxes. Postupujeme od nejblizsich ctvercu po vzdalenejsi. Zaciname v pravem hornim rohu. X je
	 * stred hledani. Zaciname na 1 a postupujeme dolu, doleva, nahoru a dopava. Pote prejdeme do ctverce o jedno vyssi
	 * a opakujeme Kontrolujeme mantinely gridAxes.
	 *
	 * | - - ->2 | | ->1 | | | X | | | - - | | - - - - |
	 */
	private Point getNextI(final Point start, final Point actual, final int[] gridAxes) {

		// vlastne vzdalenost actual vuci vychozimu bodu statePosition
		final Point point = new Point(actual.getX() - start.getX(), actual.getY() - start.getY());
		log.info("point: " + point.getX() + ":" + point.getY());

		// index ctverce okolo vychoziho bodu na kterem se zrovna pohybuju
		final int index = Math.max(Math.abs(point.getX()), Math.abs(point.getY()));
		log.info("index: " + index);

		// pokud jsem dokoncila ctverec, posunu se na dalsi
		if (point.equals(new Point(index, -index))) {
			return goNewIndex(start, gridAxes, index + 1);
		}
		// jinak spocitam dalsi pozici
		else {
			return goNextI(start, actual, gridAxes);
		}
	}

	private Point goNextI(final Point i, final Point actual, final int[] gridAxes) {
		log.info("goNextI");
		// vlastne vzdalenost actual vuci vychozimu bodu statePosition
		final Point point = new Point(actual.getX() - i.getX(), actual.getY() - i.getY());
		log.info("point: " + point.getX() + ":" + point.getY());

		// index ctverce okolo vychoziho bodu na kterem se zrovna pohybuju
		final int index = Math.max(Math.abs(point.getX()), Math.abs(point.getY()));
		log.info("index: " + index);

		// prava hrana, postupujeme dolu
		if (point.getX() == index && point.getY() < index) {
			log.info("prava hrana, postupujeme dolu");
			if (actual.getY() == gridAxes[1]) { // nemuzu dolu, zkusime doleva
				log.info("nemuzu dolu, zkusim doleva");
				if ((actual.getX() - 2 * index) <= 0) { // nemuzu ani doleva, zkusim nahoru
					log.info("nemuzu ani doleva, zkusim nahoru");
					return goUp(i, gridAxes, index);
				}
				final Point ret = new Point(actual.getX() - 2 * index, actual.getY());
				log.info("ret: " + ret);
				return ret;
			}
			final Point ret = new Point(actual.getX(), actual.getY() + 1);
			log.info("ret: " + ret);
			return ret;
		}

		// spodni hrana, postupujeme doleva
		if (point.getY() == index && point.getX() > -index) {
			log.info("spodni hrana, postupuju doleva");
			if (actual.getX() == 1) { // nemuzu jit doleva, skocim nahoru
				log.info("nemuzu jit doleva, skocim nahoru");
				return goUp(i, gridAxes, index);
			}
			final Point ret = new Point(actual.getX() - 1, actual.getY());
			log.info("ret: " + ret);
			return ret;
		}

		// leva hrana, postupujeme nahoru
		if (point.getX() == -index && point.getY() > -index) {
			log.info("leva hrana, postupujeme nahoru");
			if (actual.getY() == 1) { // nemuzu jit nahoru, zaciname s novym indexem
				log.info("nemuzu jit nahoru, zacinam s novym indexem");
				return goNewIndex(i, gridAxes, index + 1);
			}
			final Point ret = new Point (actual.getX(), actual.getY() - 1);
			log.info("ret: " + ret);
			return ret;

		}

		// horni hrana, jdeme doprava
		if (point.getY() == -index && point.getX() < index) {
			log.info("horni hrana, jdeme doprava");
			if (actual.getX() == gridAxes[0]) { // nemuzu jit doleva, zacinam s novym indexem
				log.info("nemuzu jit doleva, zacinam s novym indexem");
				return goNewIndex(i, gridAxes, index + 1);
			}
			if (point.getX() + 1 == index) { // jsem na konci, zacinam novy index
				log.info("jsem na konci, zacinam novy index");
				return goNewIndex(i, gridAxes, index + 1);
			}
			final Point ret = new Point(actual.getX() + 1, actual.getY());
			log.info("ret: " + ret);
			return ret;
		}
		return null;
	}

	private Point goUp(final Point i, final int[] gridAxes, final int index) {
		log.info("goUp");
		if (i.getY() - index <= 0) {// nemuzu jit ani nahoru, zaciname s novym indexem.
			log.info("nemuzu jit ani nahoru, zacinam s novym indexem");
			return goNewIndex(i, gridAxes, index + 1);
		}
		final Point ret = new Point(1, i.getY() - index);
		log.info("ret: " + ret);
		return ret;
	}

	private Point goNewIndex(final Point i, final int[] gridAxes, final int index) {
		log.info("goNewIndex: " + index);
		// uz neni kam dal jit
		if (index >= Math.max(gridAxes[0], gridAxes[1])) {
			log.info("Go new index - Uz neni kam dal jit");
			return null;
		}

		int actualX = i.getX() + index;
		int actualY = i.getY() - index;
		if (actualX > gridAxes[0]) {
			// pravou hranu nemuzu, prejdu rovnou dolu
			actualY = i.getY() + index;
			actualX = gridAxes[0] + 1;
			if (actualY > gridAxes[1]) {
				// spodni hranu taky nemuzu, takze doleva
				actualY = gridAxes[1] + 1;
				actualX = i.getX() - index;
				if (actualX < 1) {
					// levou hranu taky ne... jdu nahoru
					actualX = 0;
					actualY = i.getY() - index;
					if (actualY < 1) {
						// nahoru taky nemuzu...
						return null;
					}

				}
			}
		}
		if (actualY < 0) {
			actualY = 0;
		}
		final Point actual = new Point(actualX, actualY);
		return goNextI(i, actual, gridAxes);
	}
}