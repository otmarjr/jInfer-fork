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
package cz.cuni.mff.ksi.jinfer.iss.experiments.data;

/**
 * Encapsulation of a test data file.
 *
 * @author vektor
 */
public interface TestData {

  /**
   * Returns the input file represented.
   *
   * @return Test data file.
   */
  InputFile getFile();

  /**
   * Returns the known optimum for alpha = beta = 1, if available.
   *
   * @return Known optimum for this file or <code>null</code> if unknown.
   */
  Double getKnownOptimum();

  /**
   * Returns the number of vertices in the graph representation of this file.
   */
  int getVertices();

  /**
   * Returns the number of edges in the graph representation of this file.
   */
  int getEdges();

}
