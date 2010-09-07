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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.State;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.Step;
import java.util.Set;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class RegexpAutomatonSimplifierStateRemoval<T> implements RegexpAutomatonSimplifier<T> {
  @Override
  public Regexp<T> simplify(RegexpAutomaton<T> inputAutomaton) throws InterruptedException {
    RegexpAutomatonStateRemoval<T> stateRemovalAutomaton= new RegexpAutomatonStateRemoval<T>(inputAutomaton);

    RegexpAutomatonSimplifierStateRemovalOrderer<T> orderer= new RegexpAutomatonSimplifierStateRemovalOrdererWeighted<T>();
    while (stateRemovalAutomaton.getDelta().size() > 2) {
      State<Regexp<T>> toRemoveState= orderer.getStateToRemove(stateRemovalAutomaton);

      stateRemovalAutomaton.removeState(toRemoveState);
    }
    Set<Step<Regexp<T>>> regexpSteps= stateRemovalAutomaton.getDelta().get(
            stateRemovalAutomaton.getSuperInitialState()
            );
    assert regexpSteps.size() == 1;
    for (Step<Regexp<T>> step : regexpSteps) {
      return step.getAcceptSymbol();
    }
    return null; // non-reachable statement TODO anti wrong set one member handling
  }
}
