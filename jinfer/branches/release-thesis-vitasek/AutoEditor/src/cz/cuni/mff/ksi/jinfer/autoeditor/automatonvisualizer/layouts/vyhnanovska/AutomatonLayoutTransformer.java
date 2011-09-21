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

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import org.apache.log4j.Logger;

/** 
 * Transforms {@link State} to a {@link Point2D} on which it should be plotted.
 *
 * Most of the code is the original code by Julie Vyhnanovska. May need some
 * refactoring to fit our conventions.
 *
 * @author Julie Vyhnanovska, rio
 *
 */
public class AutomatonLayoutTransformer<T> implements Transformer<State<T>, Point2D> {

  private static final Logger LOG = Logger.getLogger(AutomatonLayoutTransformer.class);

  // minimal dimensions of a grid (in squares)
  private final int minXsize;
  private final int minYsize;

  // size of one square (in pixels)
  private final int squareSize;

  // grid dimension = (number of squares on axis x) * (number of squares on axis y)
  private final Dimension gridDimension;

  private final StateMapping<T> stateGridMapping;
  private static final double FILL_FACTOR = 3;
  private final Automaton<T> automaton;

  /**
   * Constructs transformer according to specified values.
   *
   * @param minXsize Minimal vertical size of a grid in squares.
   * @param minYsize Minimal horizontal size of a grid in squares.
   * @param square_size Size of one square in the grid in pixels.
   * @param graph Graph created from Automaton.
   * @param automaton
   */
  public AutomatonLayoutTransformer(final int minXsize, final int minYsize, final int square_size, final Graph<State<T>, Step<T>> graph, final Automaton<T> automaton) {
    this.automaton = automaton;
    this.minXsize = minXsize;
    this.minYsize = minYsize;
    this.squareSize = square_size;
    final int vertexCount = graph.getVertexCount();
    gridDimension = computeGridDimension((int) Math.round(vertexCount * FILL_FACTOR));
    stateGridMapping = new StateMapping<T>(vertexCount);
  }

  /**
   * Getter for a dimension of the grid
   *
   * @return Dimension of the grid in pixels.
   */
  public Dimension getDimension() {
    return new Dimension(gridDimension.width * squareSize, gridDimension.height * squareSize);
  }

  @Override
  public Point2D transform(final State<T> state) {
    LOG.info("transform location of " + state);
    // Initial state position is [1,1].
    if (state.equals(automaton.getInitialState())) {
      final Coordinate statePosition = new Coordinate(1, 1);
      stateGridMapping.addStateCoordinate(state, statePosition);
      return getPoint(statePosition);
    }

    /* Find a state which has been drawn already and there is an edge from that
     * state to a current state.
     * If there is not such state, take [1,1].
     */
    Coordinate prev = new Coordinate(1, 1);
    for (final Step<T> backEdge : automaton.getReverseDelta().get(state)) {
      final Coordinate drawnStateCoordinate = stateGridMapping.getStateCoordinate(backEdge.getSource());
      if (drawnStateCoordinate != null) {
        prev = drawnStateCoordinate;
        break;
      }
    }

    Coordinate nextI = prev;
    LOG.info("nextI: " + nextI.getX() + " " + nextI.getY());
    // While posistion of nextI is not empty or nextI equals [1,1] (is the second condition necessary?)
    while (stateGridMapping.getStateAtCoordinate(nextI) != null || (nextI.equals(new Coordinate(1, 1)))) {
      LOG.info("obsazeno!");
      nextI = getNextCoordinate(prev, nextI, gridDimension);
      if (nextI != null) {
        LOG.info("nextI: " + nextI.getX() + " " + nextI.getY());
        if (nextI.getX() < 1 || nextI.getY() < 1) {
          LOG.error("invalid grid index: " + nextI.getX() + " " + nextI.getY());
          return null;
        }
      }
    }

    if (nextI != null) {
      stateGridMapping.addStateCoordinate(state, nextI);
      return getPoint(nextI);
    }

    return null;
  }

  private Point2D getPoint(final Coordinate i) {
    final double x = i.getX() * squareSize - squareSize / 2;
    final double y = i.getY() * squareSize - squareSize / 2;
    return new Point2D.Double(x, y);
  }

  private Dimension computeGridDimension(final int minGridSize) {
    final Dimension dimension = new Dimension();

    if (minGridSize < (minXsize * minYsize)) {
      dimension.setSize(minXsize, minXsize);
    } else {
      dimension.width = (int) Math.round(Math.sqrt(minGridSize * minYsize / minXsize));
      dimension.height = (int) Math.floor(minGridSize / dimension.width) + 1;
    }

    return dimension;
  }

  /*
   * Gets coordinate of next state with a centre of search in startingCoordinate, actual coordinate is actualCoordinate.
   * Searching area is bounded by gridDimension and starts from the nearest coordinates, continues through a futher ones to the furthest.
   *
   * First from the nearest coordinates is the upper right one. Centre of search is X. Starting with 1, continues down, then to the left, up and to the right.
   * This repeats in a larger square.
   *
   * Original text by Julie Vyhnanovska:
   * ------------------------------------------------------------
   * Vrati dalsi pole pro umisteni vrcholu, se stredem hledani ve startingCoordinate a aktualni pozici actualCoordinate. Pohybujeme se v
   * mantinelech gridSize. Postupujeme od nejblizsich ctvercu po vzdalenejsi. Zaciname v pravem hornim rohu. X je
   * stred hledani. Zaciname na 1 a postupujeme dolu, doleva, nahoru a dopava. Pote prejdeme do ctverce o jedno vyssi
   * a opakujeme Kontrolujeme mantinely gridSize.
   *
   * | - - ->2 | | ->1 | | | X | | | - - | | - - - - |
   * ------------------------------------------------------------
   */
  private Coordinate getNextCoordinate(final Coordinate startingCoordinate, final Coordinate actualCoordinate, final Dimension gridDimension) {

    // distance of actualCoordinate from the startingCoordinate
    final Coordinate distance = new Coordinate(actualCoordinate.getX() - startingCoordinate.getX(), actualCoordinate.getY() - startingCoordinate.getY());
    LOG.info("distance: " + distance.getX() + ":" + distance.getY());

    // index of a square around the starting coordinate on which the search is actually performed
    final int index = Math.max(Math.abs(distance.getX()), Math.abs(distance.getY()));
    LOG.info("index: " + index);

    // if done on the square, moves to the next one
    if (distance.equals(new Coordinate(index, -index))) {
      return goNewIndex(startingCoordinate, gridDimension, index + 1);
    } // else computes the next coordinate
    else {
      return goNextI(startingCoordinate, actualCoordinate, gridDimension);
    }
  }

  private Coordinate goNextI(final Coordinate startingCoordinate, final Coordinate actualCoordinate, final Dimension gridDimension) {
    LOG.info("goNextI");
    // distance of actualCoordinate from the starting startingCoordinate
    final Coordinate point = new Coordinate(actualCoordinate.getX() - startingCoordinate.getX(), actualCoordinate.getY() - startingCoordinate.getY());
    LOG.info("point: " + point.getX() + ":" + point.getY());

    // index of a square around the starting coordinate on which the search is actually performed
    final int index = Math.max(Math.abs(point.getX()), Math.abs(point.getY()));
    LOG.info("index: " + index);

    // right side, moves down
    if (point.getX() == index && point.getY() < index) {
      LOG.info("prava hrana, postupujeme dolu");
      if (actualCoordinate.getY() == gridDimension.height) { // cannot move down, try to the left
        LOG.info("nemuzu dolu, zkusim doleva");
        // Original code before 'fixing', this is not tested properly: ...getX() - 2 * index
        if (actualCoordinate.getX() <= startingCoordinate.getX() - index || actualCoordinate.getX() <= 1) { // cannot to the left, try up
          LOG.info("nemuzu ani doleva, zkusim nahoru");
          return goUp(startingCoordinate, gridDimension, index);
        }
        // Original code before 'fixing', this is not tested properly: ...getX() - 2 * index
        final Coordinate ret = new Coordinate(actualCoordinate.getX() - 1, actualCoordinate.getY());
        LOG.info("ret: " + ret);
        return ret;
      }
      final Coordinate ret = new Coordinate(actualCoordinate.getX(), actualCoordinate.getY() + 1);
      LOG.info("ret: " + ret);
      return ret;
    }

    // down side, moves to the left
    if (point.getY() == index && point.getX() > -index) {
      LOG.info("spodni hrana, postupuju doleva");
      if (actualCoordinate.getX() == 1) { // cannot to the left, go up
        LOG.info("nemuzu jit doleva, skocim nahoru");
        return goUp(startingCoordinate, gridDimension, index);
      }
      final Coordinate ret = new Coordinate(actualCoordinate.getX() - 1, actualCoordinate.getY());
      LOG.info("ret: " + ret);
      return ret;
    }

    // left side, moves up
    if (point.getX() == -index && point.getY() > -index) {
      LOG.info("leva hrana, postupujeme nahoru");
      if (actualCoordinate.getY() == 1) { // cannot move up, start with a new index
        LOG.info("nemuzu jit nahoru, zacinam s novym indexem");
        return goNewIndex(startingCoordinate, gridDimension, index + 1);
      }
      final Coordinate ret = new Coordinate(actualCoordinate.getX(), actualCoordinate.getY() - 1);
      LOG.info("ret: " + ret);
      return ret;

    }

    // up side, moves to the right
    if (point.getY() == -index && point.getX() < index) {
      LOG.info("horni hrana, jdeme doprava");
      if (actualCoordinate.getX() == gridDimension.width) { // cannot move to the left, start with a new index
        LOG.info("nemuzu jit doleva, zacinam s novym indexem");
        return goNewIndex(startingCoordinate, gridDimension, index + 1);
      }
      if (point.getX() + 1 == index) { // at the end, start a new index
        LOG.info("jsem na konci, zacinam novy index");
        return goNewIndex(startingCoordinate, gridDimension, index + 1);
      }
      final Coordinate ret = new Coordinate(actualCoordinate.getX() + 1, actualCoordinate.getY());
      LOG.info("ret: " + ret);
      return ret;
    }
    return null;
  }

  private Coordinate goUp(final Coordinate i, final Dimension gridDimension, final int index) {
    LOG.info("goUp");
    // Original code before 'fixing', this is not tested properly: if (i.getY() - index <= 0)
    if (i.getY() - index <= 0) {// cannot move up, start with a new index
      LOG.info("nemuzu jit ani nahoru, zacinam s novym indexem");
      return goNewIndex(i, gridDimension, index + 1);
    }
    final Coordinate ret = new Coordinate(1, i.getY() - index);
    LOG.info("ret: " + ret);
    return ret;
  }

  private Coordinate goNewIndex(final Coordinate i, final Dimension gridDimension, final int index) {
    LOG.info("goNewIndex: " + index);
    // there are not any free coordinates anymore
    if (index >= Math.max(gridDimension.width, gridDimension.height)) {
      LOG.info("Go new index - Uz neni kam dal jit");
      return null;
    }

    int actualX;
    int actualY;
    if (i.getX() + index > gridDimension.width) {
      // connot move at the right side, move to the down side
      if (i.getY() + index > gridDimension.height) {
        // cannot move at the down side too, move to the left side
        if (i.getX() - index < 1) {
          // left one is not possible as well, move to the up side
          actualY = i.getY() - index;
          if (actualY < 1) {
            // and the up one is not possible too
            return null;
          }
          actualX = 0;
        } else {
          actualX = i.getX() - index;
          actualY = gridDimension.height + 1;
        }
      } else {
        actualX = gridDimension.width + 1;
        actualY = i.getY() + index;
      }
    } else {
      actualX = i.getX() + index;
      actualY = i.getY() - index;
    }

    final Coordinate actual = new Coordinate(actualX, (actualY < 0) ? 0 : actualY);
    return goNextI(i, actual, gridDimension);
  }
}
