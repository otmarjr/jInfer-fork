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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.automatonNaive;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.universalCodeForIntegers.UniversalCodeForIntegers;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class StepPath<T> {

  private Automaton<T> aut;
  private State<T> lastState;
  private StepPath<T> predecessor;
  private Step<T> myStep;
  private double probability;

  public StepPath(Automaton<T> aut) {
    this.aut = aut;
    this.predecessor = null;
    this.myStep = null;
    this.lastState = aut.getInitialState();
    this.probability= 1.0;
   }

  public StepPath(Automaton<T> aut, StepPath<T> predecessor, Step<T> myStep, double probability) {
    this.aut = aut;
    this.predecessor = predecessor;
    this.myStep = myStep;
    this.lastState = myStep.getDestination();
    this.probability= probability;
  }

  public StepPath(Automaton<T> aut, StepPath<T> predecessor, double probability) {
    this.aut = aut;
    this.predecessor = predecessor;
    this.myStep = null;
    this.lastState = null;
    this.probability= probability;
    assert probability > 0;
    assert probability <= 1;
  }

  public List<StepPath<T>> suffix(T symbol) {
    List<StepPath<T>> result = new LinkedList<StepPath<T>>();
    double unity = 0;
    for (Step<T> step : aut.getDelta().get(lastState)) {
      unity += step.getUseCount();
    }
    unity += lastState.getFinalCount();

    for (Step<T> outStep : aut.getDelta().get(lastState)) {
      if (outStep.getAcceptSymbol().equals(symbol)) {
        result.add(
                new StepPath<T>(aut, this, outStep, outStep.getUseCount() / unity));
      }
    }
    assert probability > 0;
    assert probability <= 1;
    return result;
  }

  public List<StepPath<T>> finalState() {
    List<StepPath<T>> result = new LinkedList<StepPath<T>>();
    double unity = 0;
    for (Step<T> step : aut.getDelta().get(lastState)) {
      unity += step.getUseCount();
    }
    unity += lastState.getFinalCount();
    
    if (this.lastState.getFinalCount() > 0) {
      result.add(
              new StepPath<T>(aut, this, lastState.getFinalCount() / unity)
              );
    }
    assert probability > 0;
    assert probability <= 1;
    return result;
  }

  public double getMDL() {
    double result = -UniversalCodeForIntegers.log2(this.probability);
    assert result >= 0;
    if (this.predecessor != null) {
      result+= this.predecessor.getMDL();
    }
    return result;
  }
}
