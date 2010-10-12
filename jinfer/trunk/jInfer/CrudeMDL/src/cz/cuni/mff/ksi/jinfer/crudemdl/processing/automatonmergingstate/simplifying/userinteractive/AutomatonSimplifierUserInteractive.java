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
import cz.cuni.mff.ksi.jinfer.autoeditor.BububuEditor;
import cz.cuni.mff.ksi.jinfer.autoeditor.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Works simply - renders automaton to user. When returned list of states user
 * had selected, merges them. And cycle goes on. If user select no states,
 * it is considered that user is satisfied with automaton, so automaton is returned
 * in current form.
 *
 * @author anti
 */
public class AutomatonSimplifierUserInteractive<T> implements AutomatonSimplifier<T> {
  private static final Logger LOG = Logger.getLogger(AutomatonSimplifierUserInteractive.class);
  
  @Override
  public Automaton<T> simplify(final Automaton<T> inputAutomaton, final SymbolToString<T> symbolTostring) throws InterruptedException {
    final AutoEditor<T> gui= new AutoEditor<T>(symbolTostring);
    List<State<T>> mergeLst;
    do {
      mergeLst = gui.drawAutomatonToPickStates(inputAutomaton);

      if (!BaseUtils.isEmpty(mergeLst)) {
        LOG.debug("AUTO EDITOR selected: " + mergeLst.toString());
        inputAutomaton.mergeStates(mergeLst);
        LOG.debug("After merge:");
        LOG.debug(inputAutomaton);
      }
    } while (!BaseUtils.isEmpty(mergeLst));
    return inputAutomaton;
  }
}
