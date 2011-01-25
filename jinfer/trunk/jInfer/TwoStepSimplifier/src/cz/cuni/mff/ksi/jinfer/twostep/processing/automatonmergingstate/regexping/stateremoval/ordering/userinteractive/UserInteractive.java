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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.userinteractive;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.StatePickingVisualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutHelperFactory;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.StatePickingComponent;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.StateRemovalRegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.Orderer;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 * Not using it, we don't have proper GUI - one click removing states.
 *
 * @author anti
 */
public class UserInteractive<T> implements Orderer<T> {

  private static final Logger LOG = Logger.getLogger(UserInteractive.class);

  @Override
  public State<Regexp<T>> getStateToRemove(StateRemovalRegexpAutomaton<T> automaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException {
    //final AutoEditor<Regexp<T>> gui= new AutoEditor<Regexp<T>>(symbolToString);
    Transformer<Step<Regexp<T>>, String> t = new Transformer<Step<Regexp<T>>, String>() {

      @Override
      public String transform(Step<Regexp<T>> step) {
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
    StatePickingVisualizer<Regexp<T>> visualizer = new StatePickingVisualizer<Regexp<T>>(LayoutHelperFactory.createUserLayout(automaton, t), t, component);
    component.setVisualizer(visualizer);
    do {
      AutoEditor.drawComponentAndWaitForGUI(component);
      removeState = component.getPickedState();

      if ((removeState.equals(automaton.getSuperFinalState())) || (removeState.equals(automaton.getSuperInitialState()))) {
        // TODO anti this text
        component.setLabel("Do not select initial and final states.");
        continue;
      }
      LOG.debug("AUTO EDITOR selected: " + removeState.toString());
      return removeState;
    } while (true);
  }
}
