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
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class StepPath<T> {
  private static final Logger LOG = Logger.getLogger(StepPath.class
          );
  private Automaton<T> aut;
  private State<T> lastState;
  private double mdl;

  public StepPath(Automaton<T> aut) {
    this.aut = aut;
    this.lastState= aut.getInitialState();
   }

  public void suffix(T symbol) {
      double unity = 0;
      for (Step<T> step : aut.getDelta().get(lastState)) {
        unity += step.getUseCount();
      }
      unity += lastState.getFinalCount();

      boolean visited = false;
      for (Step<T> outStep : aut.getDelta().get(lastState)) {
         if (outStep.getAcceptSymbol().equals(symbol)) {
           if (visited) {
             LOG.fatal("TUUUUUUUUU");
             break;
           }
           lastState = outStep.getDestination();
           mdl-= UniversalCodeForIntegers.log2(outStep.getUseCount() / unity);
           visited = true;
        }
      }
  }

  public void finalState() {
      double unity = 0;
      for (Step<T> step : aut.getDelta().get(lastState)) {
        unity += step.getUseCount();
      }
      unity += lastState.getFinalCount();
    
      if (lastState.getFinalCount() > 0) {
        mdl-= UniversalCodeForIntegers.log2(lastState.getFinalCount() / unity);
      }
  }

  public double getMDL() {
    return mdl;
  }
}
