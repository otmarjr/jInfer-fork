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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.userinteractive;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.StatesPickingVisualizer;
import cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.LayoutHelperFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.autoeditor.gui.component.StatesPickingComponent;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Logger;

/**
 * Renders automaton to user and merges returned list of states user
 * had selected.
 *
 * And cycle goes on. If user select no states,
 * it is considered that user is satisfied with automaton, so automaton is returned
 * in current form.
 *
 * @author anti
 */
@SuppressWarnings("PMD.MethodArgumentCouldBeFinal")
public class UserInteractive<T> implements AutomatonSimplifier<T> {

  private static final Logger LOG = Logger.getLogger(UserInteractive.class);
  private boolean askUser = true;

  @Override
  public Automaton<T> simplify(final Automaton<T> inputAutomaton, final SymbolToString<T> symbolToString) throws InterruptedException {
    return simplify(inputAutomaton, symbolToString, "");
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, final SymbolToString<T> symbolToString, final String elementName) throws InterruptedException {
    if (!askUser) {
      return inputAutomaton;
    }

    List<State<T>> mergeLst;
    final Transformer<Step<T>, String> t = new Transformer<Step<T>, String>() {

      @Override
      public String transform(Step<T> step) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(symbolToString.toString(step.getAcceptSymbol()));
        sb.append("|");
        sb.append(String.valueOf(step.getUseCount()));
        sb.append("}");
        return sb.toString();
      }
    };

    Boolean selectTwo = false;
    do {
      final StatesPickingVisualizer<T> visualizer = new StatesPickingVisualizer<T>(LayoutHelperFactory.createUserLayout(inputAutomaton, t), t);

      // custom plugins
      /*final PluggableVisualizer<T> visualizer = new PluggableVisualizer<T>(new GridLayout<T>(inputAutomaton));
      visualizer.setEdgeLabelTransformer(new Transformer<Step<T>, String>() {
      @Override
      public String transform(Step<T> step) {
      return symbolToString.toString(step.getAcceptSymbol());
      }
      });
      for (final GraphMousePlugin mousePlugin : AutoEditor.getDefaultGraphMousePlugins()) {
      visualizer.addGraphMousePlugin(mousePlugin);
      }
      visualizer.addGraphMousePlugin(new VerticesPickingGraphMousePlugin());
       */

      final StatesPickingComponent<T> panel = new StatesPickingComponent<T>();
      panel.setVisualizer(visualizer);
      if (selectTwo) {
        panel.setLabel("Automaton for element <" + elementName + ">. Please select states to be merged and click continue. Select at least 2 states.");
      } else {
        panel.setLabel("Automaton for element <" + elementName + ">. Please select states to be merged and click continue.");
      }
      AutoEditor.drawComponentAndWaitForGUI(panel);
      mergeLst = panel.getPickedStates();

      if ((!BaseUtils.isEmpty(mergeLst)) && (mergeLst.size() >= 2)) {
        LOG.debug("AUTO EDITOR selected: " + mergeLst.toString());
        inputAutomaton.mergeStates(mergeLst);
        LOG.debug("After merge:");
        LOG.debug(inputAutomaton);
        selectTwo = false;
      } else if (mergeLst.size() < 2) {
        selectTwo = true;
      }

      if (!panel.shallAskUser()) {
        askUser = false;
        break;
      }
    } while (!BaseUtils.isEmpty(mergeLst));
    return inputAutomaton;
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, List<List<T>> inputStrings) throws InterruptedException {
    return simplify(inputAutomaton, symbolToString);
  }

  @Override
  public Automaton<T> simplify(Automaton<T> inputAutomaton, SymbolToString<T> symbolToString, String elementName, List<List<T>> inputStrings) throws InterruptedException {
    return simplify(inputAutomaton, symbolToString, elementName);
  }
}
