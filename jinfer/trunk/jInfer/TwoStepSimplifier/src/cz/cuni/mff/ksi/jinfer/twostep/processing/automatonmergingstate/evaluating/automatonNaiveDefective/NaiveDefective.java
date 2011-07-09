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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.automatonNaiveDefective;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.DefectiveAutomatonEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.DefectiveAutomaton;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class NaiveDefective<T> implements DefectiveAutomatonEvaluator<T> {

  private NaiveDefectiveAutomaton<T> naiveAutomaton;
  private NaiveDefectiveRules<T> naiveRules;

  public NaiveDefective() {
    this.naiveAutomaton = new NaiveDefectiveAutomaton<T>();
    this.naiveRules = new NaiveDefectiveRules<T>();
  }

  @Override
  public double evaluate(DefectiveAutomaton<T> aut) throws InterruptedException {
    return naiveAutomaton.evaluate(aut) + naiveRules.evaluate(aut);
  }
}
