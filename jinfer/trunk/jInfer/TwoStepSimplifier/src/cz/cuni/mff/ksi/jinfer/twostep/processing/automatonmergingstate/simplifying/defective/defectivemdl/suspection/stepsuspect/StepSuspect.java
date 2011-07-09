/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl.suspection.stepsuspect;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl.suspection.Suspection;
import java.util.Iterator;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class StepSuspect<T> implements Suspection<T> {

  private List<List<T>> inputStrings;
  private SymbolToString<T> symbolToString;
  private Automaton<T> inputAutomaton;
  private Iterator<State<T>> stateIterator;
  private Iterator<Step<T>> stepIterator;
  private State<T> actualState;
  private boolean emptyAut;

  @Override
  public void setInputStrings(List<List<T>> inputStrings) {
    this.inputStrings = inputStrings;

  }

  @Override
  public void setSymbolToString(SymbolToString<T> symbolToString) {

    this.symbolToString = symbolToString;
  }

  @Override
  public void setInputAutomaton(Automaton<T> inputAutomaton) {
    this.inputAutomaton = inputAutomaton;
    this.stateIterator = inputAutomaton.getDelta().keySet().iterator();
    if (stateIterator.hasNext()) {
      this.actualState = stateIterator.next();
      this.stepIterator = inputAutomaton.getDelta().get(actualState).iterator();
      this.emptyAut = false;
    } else {
      this.emptyAut = true;
    }
  }

  @Override
  public List<List<T>> getNextSuspectedInputStrings() {
    if (emptyAut) {
      return null;
    }
    while (!stepIterator.hasNext()) {
      if (!stateIterator.hasNext()) {
        return null;
      }
      this.actualState = stateIterator.next();
      this.stepIterator = inputAutomaton.getDelta().get(actualState).iterator();
    }
    Step<T> step = stepIterator.next();
    return step.getInputStrings();
  }
}
