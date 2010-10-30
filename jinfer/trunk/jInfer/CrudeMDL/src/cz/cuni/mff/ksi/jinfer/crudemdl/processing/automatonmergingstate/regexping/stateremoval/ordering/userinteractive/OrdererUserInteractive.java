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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.userinteractive;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.AutomatonVisualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.SymbolToString;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.examples.OneButtonComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.RegexpAutomatonStateRemoval;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrderer;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 * Not using it, we don't have proper GUI - one click removing states.
 *
 * @author anti
 */
public class OrdererUserInteractive<T> implements RegexpAutomatonSimplifierStateRemovalOrderer<T> {
  private static final Logger LOG = Logger.getLogger(OrdererUserInteractive.class);
  
  @Override
  public State<Regexp<T>> getStateToRemove(RegexpAutomatonStateRemoval<T> automaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    //final AutoEditor<Regexp<T>> gui= new AutoEditor<Regexp<T>>(symbolToString);
    List<State<Regexp<T>>> removeLst;
    do {
      final AutomatonVisualizer<Regexp<T>> visualizer = new AutomatonVisualizer<Regexp<T>>(automaton, symbolToString);
      final OneButtonComponent component = new OneButtonComponent("Please select states to be merged together");
      removeLst = AutoEditor.drawAutomatonToPickStates(component, visualizer);

      if (removeLst == null) {
        continue;
      }
      if (removeLst.size()==1) {
        State<Regexp<T>> sel = removeLst.get(0);
        if ((sel.equals(automaton.getSuperFinalState()))||(sel.equals(automaton.getSuperInitialState()))) {
          continue;
        }
        LOG.debug("AUTO EDITOR selected: " + removeLst.toString());
        return removeLst.get(0);
      }
    } while (true);
  }

}
