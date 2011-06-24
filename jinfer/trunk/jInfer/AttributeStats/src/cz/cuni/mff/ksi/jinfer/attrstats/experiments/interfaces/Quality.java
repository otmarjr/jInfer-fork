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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces;

/**
 * Interface representing general quality of a solution. It needs to be able to
 * state some kind of a number describing a "linear" quality for sorting and
 * similar purposes, as well as report whether the solution is optimal.
 *
 * @author vektor
 */
public interface Quality {

  /**
   * Returns a scalar representation of the quality of the solution.
   *
   * @return Scalar representation of the quality of the solution.
   */
  double getScalar();

  /**
   * Returns a flag whether this solution is know to be optimal.
   *
   * @return <code>True</code> if this solution is known to be optimal,
   * <code>false</code> otherwise.
   */
  boolean isOptimal();

  /**
   * Get the quality information in textual form.
   *
   * @return String describing the quality.
   */
  String getText();

  Quality ZERO = new Quality() {

    @Override
    public double getScalar() {
      return 0;
    }

    @Override
    public boolean isOptimal() {
      return false;
    }

    @Override
    public String getText() {
      return "0";
    }
  };

}
