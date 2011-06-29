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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.regexp;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.Evaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers.UniversalCodeForIntegers;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class BasicRegexp<T> implements Evaluator<Regexp<T>> {
  BasicInterval bi = new BasicInterval();
 
  @Override
  public double evaluate(Regexp<T> x) throws InterruptedException {
    UniversalCodeForIntegers uic = UniversalCodeForIntegers.getSingleton();
    double result = 2;
    switch (x.getType()) {
      case LAMBDA:
        return 0;
      case TOKEN: 
        return result + bi.evaluate(x.getInterval());
      case CONCATENATION:
      case ALTERNATION:
      case PERMUTATION:
        result+= uic.evaluate(x.getChildren().size());
        for (Regexp<T> y : x.getChildren()) {
          result+= this.evaluate(y);
        }
        return result;
      default:
        throw new IllegalStateException("Unknown regexp type.");
    }
  }
 
}
