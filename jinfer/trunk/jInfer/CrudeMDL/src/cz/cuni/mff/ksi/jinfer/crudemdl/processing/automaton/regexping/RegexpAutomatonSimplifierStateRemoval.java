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

import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * One of methods for deriving regexp from RegexpAutomaton is by removing states
 * and combining regexps on transitions properly.
 *
 * Uses RegexpAutomatonStateRemoval automaton implementation of automaton, to
 * which it supplies states to remove in "correct" order.
 * Order is determined by implementation of interface
 * RegexpAutomatonSimplifierStateRemovalOrderer.
 *
 * @author anti
 */
public class RegexpAutomatonSimplifierStateRemoval<T> implements RegexpAutomatonSimplifier<T> {
  private static final Logger LOG = Logger.getLogger(RegexpAutomatonSimplifierStateRemoval.class);
  
  @Override
  public Regexp<T> simplify(RegexpAutomaton<T> inputAutomaton) throws InterruptedException {
    RegexpAutomatonStateRemoval<T> stateRemovalAutomaton= new RegexpAutomatonStateRemoval<T>(inputAutomaton);

    RegexpAutomatonSimplifierStateRemovalOrderer<T> orderer= new RegexpAutomatonSimplifierStateRemovalOrdererWeighted<T>();
    while (stateRemovalAutomaton.getDelta().size() > 2) {
      State<Regexp<T>> toRemoveState= orderer.getStateToRemove(stateRemovalAutomaton);

      stateRemovalAutomaton.removeState(toRemoveState);
    }
    stateRemovalAutomaton.finalStep();
    Set<Step<Regexp<T>>> regexpSteps= stateRemovalAutomaton.getDelta().get(
            stateRemovalAutomaton.getSuperInitialState()
            );
    LOG.debug("After RegexpAutomatonSimplifierStateRemoval simplify:");
    LOG.debug(stateRemovalAutomaton);
    if (!(regexpSteps.size() == 1)) {
      throw  new IllegalStateException("There should be only one step in whole automaton.");
    }
    return regexpSteps.iterator().next().getAcceptSymbol();
  }
}
