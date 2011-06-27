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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class StepPaths<T> {
  private List<StepPath<T>> paths;

  public StepPaths(Automaton<T> aut) {
    this.paths= new LinkedList<StepPath<T>>();
    this.paths.add(new StepPath<T>(aut));
  }
  
  public void suffix(T symbol) {
    List<StepPath<T>> newPaths = new LinkedList<StepPath<T>>();
    for (StepPath<T> sp : this.paths) {
      newPaths.addAll(sp.suffix(symbol));
    }
    this.paths= newPaths;
  }
  
  public void finalState() {
    List<StepPath<T>> newPaths = new LinkedList<StepPath<T>>();
    for (StepPath<T> sp : this.paths) {
      newPaths.addAll(sp.finalState());
    }
    this.paths= newPaths;
  }

  public List<StepPath<T>> getPaths() {
    return Collections.unmodifiableList(paths);
  }
  
  public double getMinimumMDL() {
    double minD= Double.MAX_VALUE;
    for (StepPath<T> sp : this.paths) {
      double thisD = sp.getMDL();
      if (thisD < minD) {
        minD = thisD;
      }      
    }
    return minD;
  }
}
