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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class StepRemoveAutomaton<T> extends Automaton<T> {
  public StepRemoveAutomaton(final Automaton<T> anotherAutomaton) {
    super(anotherAutomaton);
  }
  
  public void tryRemoveStep(Step<T> step) {
    this.delta.get(step.getSource()).remove(step);
  }
  
  public void undoRemoveStep(Step<T> step) {
    this.delta.get(step.getSource()).add(step);
  }
}
