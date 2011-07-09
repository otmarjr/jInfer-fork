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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.combined;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class Combined<T> implements MergeConditionTester<T> {

  private final List<MergeConditionTester<T>> testers;

  public Combined(List<MergeConditionTesterFactory> factories) {
    this.testers = new LinkedList<MergeConditionTester<T>>();
    for (MergeConditionTesterFactory f : factories) {
      this.testers.add(f.<T>create());
    }
  }

  @Override
  public List<List<List<State<T>>>> getMergableStates(Automaton<T> automaton) throws InterruptedException {
    List<List<List<State<T>>>> result = new LinkedList<List<List<State<T>>>>();
    for (MergeConditionTester<T> t : this.testers) {
      result.addAll(t.getMergableStates(automaton));
    }
    return result;
  }
}
