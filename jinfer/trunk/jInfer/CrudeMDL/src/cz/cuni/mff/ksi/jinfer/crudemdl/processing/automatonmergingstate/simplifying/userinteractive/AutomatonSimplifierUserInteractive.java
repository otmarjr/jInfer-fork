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
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.statespickingvisualizer.StatesPickingVisualizer;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.examples.StatesPickingComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;
import org.apache.commons.collections15.Transformer;
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
  public Automaton<T> simplify(final Automaton<T> inputAutomaton, final SymbolToString<T> symbolToString) throws InterruptedException {
    List<State<T>> mergeLst;
    do {
      // graphviz
      //final StatesPickingVisualizer<T> visualizer = new StatesPickingVisualizer<T>(new GraphvizLayout<T>(inputAutomaton, symbolTostring));
      // vyhnanovska
      //final StatesPickingVisualizer<T> visualizer = new StatesPickingVisualizer<T>(new GridLayout<T>(inputAutomaton, symbolTostring));
      // default - vyhnanovska
      final StatesPickingVisualizer<T> visualizer = new StatesPickingVisualizer<T>(inputAutomaton, new Transformer<Step<T>, String>() {

        @Override
        public String transform(Step<T> step) {
          return symbolToString.toString(step.getAcceptSymbol());
        }
      });

      final StatesPickingComponent panel = new StatesPickingComponent();
      AutoEditor.drawComponentAndWaitForGUI(panel, visualizer);
      mergeLst = panel.getPickedStates();

      if ((!BaseUtils.isEmpty(mergeLst)) && (mergeLst.size() >= 2)) {
        LOG.debug("AUTO EDITOR selected: " + mergeLst.toString());
        inputAutomaton.mergeStates(mergeLst);
        LOG.debug("After merge:");
        LOG.debug(inputAutomaton);
      }
    } while (!BaseUtils.isEmpty(mergeLst));
    return inputAutomaton;
  }
}
