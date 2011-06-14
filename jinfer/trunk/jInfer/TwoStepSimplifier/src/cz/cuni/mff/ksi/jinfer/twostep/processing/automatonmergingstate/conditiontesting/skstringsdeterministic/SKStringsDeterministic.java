/*
 *  Copyright (C) 2010 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstringsdeterministic;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.twostep.ModuleParameters;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.deterministic.DeterministicFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings.SKStringsFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * k,h-context equivalence criterion implementation.
 *
 * It first finds all contexts of two states, then compares each pair. Returns
 * list of alternative merge lists of pairs.
 *
 * Not using k-grams algorithm.
 * 
 * @author anti
 */
public class SKStringsDeterministic<T> implements MergeConditionTester<T> {

  private MergeConditionTester<T> deterministic;
  private MergeConditionTester<T> skstrings;

  /**
   * Setting k,h. It has to be k >= h, or exception thrown (cannot merge more than
   * k states on path, when only k of them were examined).
   * @param k
   * @param h
   */
  public SKStringsDeterministic(final int k, final double s, final String strategy) {
    if (!((k > 0)&&(s >= 0)&&(s <= 1))) {
      throw new IllegalArgumentException("Parameter k must be greater than 0. Parameter s must satisfy: 0 <= s <= 1.");
    }
    this.deterministic= (new DeterministicFactory()).create();
    final SKStringsFactory mergeConditionTesterFactory = new SKStringsFactory();
    final ModuleParameters factoryParam = ((ModuleParameters) mergeConditionTesterFactory);

    factoryParam.setParameter("k", String.valueOf(k));
    factoryParam.setParameter("s", String.valueOf(s));
    factoryParam.setParameter("strategy", strategy);

    this.skstrings= mergeConditionTesterFactory.create();
  }


  @Override
  public List<List<List<State<T>>>> getMergableStates(final State<T> state1, final State<T> state2, final Automaton<T> automaton) throws InterruptedException {
    List<List<List<State<T>>>> alternatives = this.skstrings.getMergableStates(state1, state2, automaton);
    if (!alternatives.isEmpty()) {
      return alternatives;
    }
    alternatives = deterministic.getMergableStates(state1, state2, automaton);
    return alternatives;
  }
}

