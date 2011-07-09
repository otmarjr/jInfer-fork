/*
 * Copyright (C) 2011 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.Evaluator;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class UniversalCodeForIntegers implements Evaluator<Integer> {

  private static double log2constant = Math.log(2.0);
  ;
  private static double log2c0 = log2(2.865);
  private static UniversalCodeForIntegers instance = new UniversalCodeForIntegers();

  public static double log2(double x) {
    return Math.log(x) / log2constant;
  }

  private UniversalCodeForIntegers() {
  }

  public static UniversalCodeForIntegers getSingleton() {
    return instance;
  }

  @Override
  public double evaluate(Integer x) throws InterruptedException {
    double result = 0;
    double intermediate = x;
    while ((intermediate = log2(intermediate)) > 0) {
      result += intermediate;
    }
    result += log2c0;
    return result;
  }
}
