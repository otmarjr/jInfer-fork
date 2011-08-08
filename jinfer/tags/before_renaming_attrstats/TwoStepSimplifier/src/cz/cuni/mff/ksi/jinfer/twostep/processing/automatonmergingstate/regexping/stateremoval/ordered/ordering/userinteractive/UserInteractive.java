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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.userinteractive;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.StatePickingVisualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutHelperFactory;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.StatePickingComponent;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.Orderer;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Logger;

/**
 * Presents automaton to user and lets him select state to be remove first.
 *
 * @author anti
 */
public class UserInteractive<T> implements Orderer<T> {

  private static final Logger LOG = Logger.getLogger(UserInteractive.class);

  @Override
  public State<Regexp<T>> getStateToRemove(
          final StateRemovalRegexpAutomaton<T> automaton,
          final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    final Transformer<Step<Regexp<T>>, String> t = new Transformer<Step<Regexp<T>>, String>() {

      @Override
      public String transform(final Step<Regexp<T>> step) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(symbolToString.toString(step.getAcceptSymbol()));
        sb.append("|");
        sb.append(String.valueOf(step.getUseCount()));
        sb.append("}");
        return sb.toString();
      }
    };

    State<Regexp<T>> removeState;
    final StatePickingComponent<Regexp<T>> component = new StatePickingComponent<Regexp<T>>();
    final StatePickingVisualizer<Regexp<T>> visualizer = new StatePickingVisualizer<Regexp<T>>(LayoutHelperFactory.createUserLayout(automaton, t), t, component, automaton.getSuperInitialState(), automaton.getSuperFinalState());
    component.setVisualizer(visualizer);
    do {
      AutoEditor.drawComponentAndWaitForGUI(component);
      removeState = component.getPickedState();

      if ((removeState.equals(automaton.getSuperFinalState())) || (removeState.equals(automaton.getSuperInitialState()))) {
        component.setLabel("Do not select superInitial and superFinal states.");
        continue;
      }
      LOG.debug("AUTO EDITOR selected: " + removeState.toString());
      return removeState;
    } while (true);
  }
}
