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
package cz.cuni.mff.ksi.jinfer.iss.experiments.quality;

/**
 * Class representing quality of a solution. This means some kind of "linear"
 * quality, possibly weight, number of attribute mappings and a flag whether
 * this solution is optimal.
 *
 * @author vektor
 */
public class Quality {

  private final double scalar;

  private final int amCount;

  private final boolean optimal;

  public static final Quality ZERO = new Quality(0, 0, false);

  public Quality(final double scalar, final int amCount, final boolean optimal) {
    this.scalar = scalar;
    this.amCount = amCount;
    this.optimal = optimal;
  }

  public String getText() {
    return getScalar() + " (" + getAmCount() + " AMs)" + (isOptimal() ? ", optimal" : "");
  }

  public double getScalar() {
    return scalar;
  }

  public int getAmCount() {
    return amCount;
  }

  public boolean isOptimal() {
    return optimal;
  }

}
