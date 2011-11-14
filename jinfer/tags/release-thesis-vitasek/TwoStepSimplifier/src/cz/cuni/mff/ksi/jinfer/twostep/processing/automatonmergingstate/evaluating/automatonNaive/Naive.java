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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.automatonNaive;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluator;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class Naive<T> implements AutomatonEvaluator<T> {

  private NaiveAutomaton<T> naiveAutomaton;
  private NaiveRules<T> naiveRules;

  @Override
  public void setInputStrings(List<List<T>> inputStrings) {
    this.naiveRules.setInputStrings(inputStrings);
  }

  public Naive() {
    this.naiveAutomaton = new NaiveAutomaton<T>();
    this.naiveRules = new NaiveRules<T>();
  }

  @Override
  public double evaluate(Automaton<T> aut) throws InterruptedException {
    return naiveAutomaton.evaluate(aut) + naiveRules.evaluate(aut);
  }
}
