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

/** TODO rio refactor
 * Transforms the state to a distance on which it should be plotted. - A visualisation tool
 *
 * @author Julie Vyhnanovska
 *
 */
public class AutomatonLayoutTransformer<T> implements Transformer<State<T>, Point2D> {

  private static Logger LOG = Logger.getLogger(AutomatonLayoutTransformer.class);

  // minimalne rozmery mriezky a podla ich pomeru sa vypocitava gridSize
  private final int minXsize;
  private final int minYsize;

  // velkost jedneho stvorca v mriezke
  private final int squareSize;

  // dimenzia mriezky = pocet stvorcov na x-ovej osy a pocet stvorcov na y-ovej osy
  private final Dimension gridDimension;

  private final StateMapping stateGridMapping;
  private static final double FILL_FACTOR = 3;
  // TODO rio vyhodit automaton
  private Automaton<T> automaton;

  // TODO rio vyhodit automaton
  public AutomatonLayoutTransformer(final int minXsize, final int minYsize, final int square_size, final Graph<State<T>, Step<T>> graph, final Automaton<T> automaton) {
    this.automaton = automaton;
    this.minXsize = minXsize;
    this.minYsize = minYsize;
    this.squareSize = square_size;
    final int vertexCount = graph.getVertexCount();
    gridDimension = computeGridDimension((int) Math.round(vertexCount * FILL_FACTOR));
    stateGridMapping = new StateMapping(vertexCount);
  }

  public Dimension getDimension() {
    return new Dimension(gridDimension.width * squareSize, gridDimension.height * squareSize);
  }

  @Override
  public Point2D transform(final State<T> state) {
    LOG.info("transform location of " + state);
    // TODO rio getId() == 1 znamena, ze je pociatocny??
    //if (state.getId() == 1) {
    // Pociatocny stav umiestni na [1,1].
    if (state.equals(automaton.getInitialState())) {
      final Coordinate statePosition = new Coordinate(1, 1);
      stateGridMapping.addStateCoordinate(state, statePosition);
      return getPoint(statePosition);
    }

    // Najdi nejaky (? skutocne nejaky?) stav, z ktoreho vedie hrana do tohoto
    // stavu a je uz nakresleny.
    // Ak taky neexistuje, vezmi, ze je to [1,1].
    Coordinate prev = null;
    for (final Step<T> backEdge : automaton.getReverseDelta().get(state)) {
      prev = stateGridMapping.getStateCoordinate(backEdge.getSource());
      if (prev != null) {
        break;
      }
    }
    if (prev == null) {
      prev = new Coordinate(1, 1);
    }

    Coordinate nextI = prev;
    LOG.info("nextI: " + nextI.getX() + " " + nextI.getY());
    // TODO rio Kym je na pozicii nextI obsadene alebo kym je nextI [1,1] (je nutna druha podmienka??) TODO
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
   * Vrati dalsi pole pro umisteni vrcholu, se stredem hledani ve startingCoordinate a aktualni pozici actualCoordinate. Pohybujeme se v
   * mantinelech gridSize. Postupujeme od nejblizsich ctvercu po vzdalenejsi. Zaciname v pravem hornim rohu. X je
   * stred hledani. Zaciname na 1 a postupujeme dolu, doleva, nahoru a dopava. Pote prejdeme do ctverce o jedno vyssi
   * a opakujeme Kontrolujeme mantinely gridSize.
   *
   * | - - ->2 | | ->1 | | | X | | | - - | | - - - - |
   */
  private Coordinate getNextCoordinate(final Coordinate startingCoordinate, final Coordinate actualCoordinate, final Dimension gridDimension) {

    // vlastne vzdalenost actualCoordinate vuci vychozimu bodu startingCoordinate
    final Coordinate distance = new Coordinate(actualCoordinate.getX() - startingCoordinate.getX(), actualCoordinate.getY() - startingCoordinate.getY());
    LOG.info("distance: " + distance.getX() + ":" + distance.getY());

    // index ctverce okolo vychoziho bodu na kterem se zrovna pohybuju
    final int index = Math.max(Math.abs(distance.getX()), Math.abs(distance.getY()));
    LOG.info("index: " + index);

    // pokud jsem dokoncila ctverec, posunu se na dalsi
    if (distance.equals(new Coordinate(index, -index))) {
      return goNewIndex(startingCoordinate, gridDimension, index + 1);
    } // jinak spocitam dalsi pozici
    else {
      return goNextI(startingCoordinate, actualCoordinate, gridDimension);
    }
  }

  private Coordinate goNextI(final Coordinate startingCoordinate, final Coordinate actualCoordinate, final Dimension gridDimension) {
    LOG.info("goNextI");
    // vlastne vzdalenost actualCoordinate vuci vychozimu bodu statePosition
    final Coordinate point = new Coordinate(actualCoordinate.getX() - startingCoordinate.getX(), actualCoordinate.getY() - startingCoordinate.getY());
    LOG.info("point: " + point.getX() + ":" + point.getY());

    // index ctverce okolo vychoziho bodu na kterem se zrovna pohybuju
    final int index = Math.max(Math.abs(point.getX()), Math.abs(point.getY()));
    LOG.info("index: " + index);

    // prava hrana, postupujeme dolu
    if (point.getX() == index && point.getY() < index) {
      LOG.info("prava hrana, postupujeme dolu");
      if (actualCoordinate.getY() == gridDimension.height) { // nemuzu dolu, zkusime doleva
        LOG.info("nemuzu dolu, zkusim doleva");
        // TODO rio povodne tu bolo ...getX() - 2 * index
        if (actualCoordinate.getX() <= startingCoordinate.getX() - index || actualCoordinate.getX() <= 1) { // nemuzu ani doleva, zkusim nahoru
          LOG.info("nemuzu ani doleva, zkusim nahoru");
          return goUp(startingCoordinate, gridDimension, index);
        }
        // TODO rio povodne tu bolo ...getX() - 2 * index
        final Coordinate ret = new Coordinate(actualCoordinate.getX() - 1, actualCoordinate.getY());
        LOG.info("ret: " + ret);
        return ret;
      }
      final Coordinate ret = new Coordinate(actualCoordinate.getX(), actualCoordinate.getY() + 1);
      LOG.info("ret: " + ret);
      return ret;
    }

    // spodni hrana, postupujeme doleva
    if (point.getY() == index && point.getX() > -index) {
      LOG.info("spodni hrana, postupuju doleva");
      if (actualCoordinate.getX() == 1) { // nemuzu jit doleva, skocim nahoru
        LOG.info("nemuzu jit doleva, skocim nahoru");
        return goUp(startingCoordinate, gridDimension, index);
      }
      final Coordinate ret = new Coordinate(actualCoordinate.getX() - 1, actualCoordinate.getY());
      LOG.info("ret: " + ret);
      return ret;
    }

    // leva hrana, postupujeme nahoru
    if (point.getX() == -index && point.getY() > -index) {
      LOG.info("leva hrana, postupujeme nahoru");
      if (actualCoordinate.getY() == 1) { // nemuzu jit nahoru, zaciname s novym indexem
        LOG.info("nemuzu jit nahoru, zacinam s novym indexem");
        return goNewIndex(startingCoordinate, gridDimension, index + 1);
      }
      final Coordinate ret = new Coordinate(actualCoordinate.getX(), actualCoordinate.getY() - 1);
      LOG.info("ret: " + ret);
      return ret;

    }

    // horni hrana, jdeme doprava
    if (point.getY() == -index && point.getX() < index) {
      LOG.info("horni hrana, jdeme doprava");
      if (actualCoordinate.getX() == gridDimension.width) { // nemuzu jit doleva, zacinam s novym indexem
        LOG.info("nemuzu jit doleva, zacinam s novym indexem");
        return goNewIndex(startingCoordinate, gridDimension, index + 1);
      }
      if (point.getX() + 1 == index) { // jsem na konci, zacinam novy index
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
    // TODO rio povodne: if (i.getY() - index <= 0)
    if (i.getY() - index <= 0) {// nemuzu jit ani nahoru, zaciname s novym indexem.
      LOG.info("nemuzu jit ani nahoru, zacinam s novym indexem");
      return goNewIndex(i, gridDimension, index + 1);
    }
    final Coordinate ret = new Coordinate(1, i.getY() - index);
    LOG.info("ret: " + ret);
    return ret;
  }

  private Coordinate goNewIndex(final Coordinate i, final Dimension gridDimension, final int index) {
    LOG.info("goNewIndex: " + index);
    // uz neni kam dal jit
    if (index >= Math.max(gridDimension.width, gridDimension.height)) {
      LOG.info("Go new index - Uz neni kam dal jit");
      return null;
    }

    int actualX = i.getX() + index;
    int actualY = i.getY() - index;
    if (actualX > gridDimension.width) {
      // pravou hranu nemuzu, prejdu rovnou dolu
      actualY = i.getY() + index;
      actualX = gridDimension.width + 1;
      if (actualY > gridDimension.height) {
        // spodni hranu taky nemuzu, takze doleva
        actualY = gridDimension.height + 1;
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
    final Coordinate actual = new Coordinate(actualX, actualY);
    return goNextI(i, actual, gridDimension);
  }
}
