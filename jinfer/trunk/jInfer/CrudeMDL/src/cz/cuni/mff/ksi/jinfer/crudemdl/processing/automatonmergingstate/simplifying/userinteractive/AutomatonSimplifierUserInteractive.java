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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.userinteractive;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class AutomatonSimplifierUserInteractive<T> implements AutomatonSimplifier<T> {
  private static final Logger LOG = Logger.getLogger(AutomatonSimplifierUserInteractive.class);
  
  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton) throws InterruptedException {
    AutoEditor<T> gui= new AutoEditor<T>();
    Pair<State<T>, State<T>> mergePair;
    do {
      mergePair = gui.drawAutomatonToPickTwoStates(inputAutomaton);

      if (mergePair != null) {
        LOG.debug("AUTO EDITOR selected: " + mergePair.toString());
        inputAutomaton.mergeStates(mergePair.getFirst(), mergePair.getSecond());
        LOG.debug("After merge:"
                );
        LOG.debug(inputAutomaton);
      }
    } while (mergePair != null);
    return inputAutomaton;
  }
}
