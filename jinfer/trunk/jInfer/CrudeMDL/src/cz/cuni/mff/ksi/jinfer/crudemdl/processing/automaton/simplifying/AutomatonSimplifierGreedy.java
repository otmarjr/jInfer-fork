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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying;

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class AutomatonSimplifierGreedy<T> implements AutomatonSimplifier<T> {
  private static final Logger LOG = Logger.getLogger(AutomatonSimplifierGreedy.class);
  private String moduleName;

  public AutomatonSimplifierGreedy(String moduleName) {
    this.moduleName= moduleName;
  }

  private MergeConditionTesterFactory getMergeConditionTesterFactory() {
    final Properties p = RunningProject.getActiveProjectProps(moduleName);

    return ModuleSelectionHelper.lookupImpl(MergeConditionTesterFactory.class, p.getProperty("merge-condition-tester"));
  }

  /**
   * Simplify by merging states greedily. Condition to merge states is tested in provided
   * mergedConditionTesters.
   *
   * Loops until there are no more states to merge by each tester (until each pair of states tested
   * by mergeCondidionTester is returned empty list of states to merge.
   *
   * @param mergeCondidionTesters
   * @param inputAutomaton
   * @throws InterruptedException
   */
  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton) throws InterruptedException {
    MergeConditionTesterFactory f = getMergeConditionTesterFactory();

    final List<MergeCondidionTester<T>> l= new ArrayList<MergeCondidionTester<T>>();
    l.add(f.<T>create());
    return this.simplify(inputAutomaton, l);
  }


  private Automaton<T> simplify(final Automaton<T> inputAutomaton, final List<MergeCondidionTester<T>> mergeConditionTesters) throws InterruptedException {
    final Map<State<T>, Set<Step<T>>> delta= inputAutomaton.getDelta();
    final Map<State<T>, Set<Step<T>>> reverseDelta= inputAutomaton.getReverseDelta();

    boolean search= true;
    while (search) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      search= false;
      List<List<Pair<State<T>, State<T>>>> mergableStates= new LinkedList<List<Pair<State<T>, State<T>>>>();
      boolean found= false;
      for (State<T> mainState : delta.keySet()) {
        for (State<T> mergedState : delta.keySet()) {
          for (MergeCondidionTester<T> mergeConditionTester : mergeConditionTesters) {
            mergableStates.addAll(mergeConditionTester.getMergableStates(mainState, mergedState, inputAutomaton));
          }
          if (!mergableStates.isEmpty()) {
            found= true;
            break;
          }
        }
        if (found) {
          break; // get out of searching when found already
        }
      }
      if (found) {
        LOG.debug("Found states to merge\n");
        for (List<Pair<State<T>, State<T>>> mergableAlternative : mergableStates) {
          for (Pair<State<T>, State<T>> mergePair : mergableAlternative) {
            LOG.debug("Merging states: " + mergePair.getFirst() + " " + mergePair.getSecond() + "\n");
            inputAutomaton.mergeStates(mergePair.getFirst(), mergePair.getSecond());
          }
        }
        mergableStates.clear();
        search= true;
      }
    }
    return inputAutomaton;
  }
}
