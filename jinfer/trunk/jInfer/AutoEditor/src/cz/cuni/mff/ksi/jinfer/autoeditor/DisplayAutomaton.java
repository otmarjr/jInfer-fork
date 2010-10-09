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

package cz.cuni.mff.ksi.jinfer.autoeditor;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class DisplayAutomaton<T> extends Automaton<T> {
  private Map<State<T>, Integer> minDepths;
  private Map<State<T>, Step<T>> minSteps;

  private int recursion(State<T> state) {
    if (state.equals(initialState)) {
      minSteps.put(state, null);
      minDepths.put(state, 0);
      return 0;
    }
    int min= Integer.MAX_VALUE;
    Step<T> minStep= null;
    for (Step<T> inStep : reverseDelta.get(state)) {
      int depth= recursion(inStep.getSource());
      if (depth < min) {
        min= depth;
        minStep= inStep;
      }
    }
    minSteps.put(state, minStep);
    minDepths.put(state, min + 1);
    return min + 1;
  }

  private void search() {
    minDepths= new HashMap<State<T>, Integer>();
    minSteps= new HashMap<State<T>, Step<T>>();
    for (State<T> state : delta.keySet()) {
      recursion(state);
    }
  }

  public Map<State<T>, Point2D> getMap() {
    this.search();

    List<State<T>> l = new ArrayList<State<T>>(delta.keySet());
    Collections.sort(l, new Comparator<State<T>>() {
      @Override
      public int compare(State<T> o1, State<T> o2) {
        if (minDepths.get(o1) < minDepths.get(o2)) {
          return -1;
        } else if (minDepths.get(o1) > minDepths.get(o2)) {
          return 1;
        }
        return o1.getName() - o2.getName();
      }
    });

    Map<State<T>, Point2D> m= new HashMap<State<T>, Point2D>();
    int level= 0;
    int y= 0;
    for (State<T> state : l) {
      if (minDepths.get(state) > level) {
        y= 0;
        level= minDepths.get(state);
      }
      m.put(state,
              new Point(40 + minDepths.get(state)*40,
              200-y*40
              ));
      y++;
    }
    return m;
  }

  public DisplayAutomaton(final Automaton<T> anotherAutomaton) {
    super(anotherAutomaton);
  }
}
