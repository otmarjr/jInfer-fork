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
package cz.cuni.mff.ksi.jinfer.iss.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * Weighted random number generator. Abridged from
 * http://www.codedanger.com/caglar/archives/374
 *
 * @author vektor
 */
public class WeightedRandomGenerator {

  private static final Random RND = new Random();

  private final double[] totals;

  public WeightedRandomGenerator(final double[] weights) {
    totals = new double[weights.length];
    double runningTotal = 0;
    for (int i = 0; i < weights.length; i++) {
      if (weights[i] < 0) {
        throw new IllegalArgumentException("Negative weight: " + weights[i]);
      }
      runningTotal += weights[i];
      totals[i] = runningTotal;
    }
  }

  /*
   * @return the weighted random number. Actually this sends the weighted randomly
   * selected index of weights vector.
   */
  public int next() {
    final double rndNum = RND.nextDouble() * totals[totals.length - 1];
    final int sNum = Arrays.binarySearch(totals, rndNum);
    return (sNum < 0) ? (Math.abs(sNum) - 1) : sNum;
  }
}
