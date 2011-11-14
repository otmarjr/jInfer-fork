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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.regexpbitcode;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.Evaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers.UniversalCodeForIntegers;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class BitCodeInterval implements Evaluator<RegexpInterval> {

  @Override
  public double evaluate(RegexpInterval x) throws InterruptedException {
    if (x.isUnbounded()) {
      return 1 + UniversalCodeForIntegers.getSingleton().evaluate(x.getMin());
    }
    return 1 + UniversalCodeForIntegers.getSingleton().evaluate(x.getMin()) + UniversalCodeForIntegers.getSingleton().evaluate(x.getMax());
  }
}
