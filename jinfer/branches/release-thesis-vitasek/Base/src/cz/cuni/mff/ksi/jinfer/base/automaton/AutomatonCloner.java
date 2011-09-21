/*
 *  Copyright (C) 2010 anti
 * 
 *  newAutomaton program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  newAutomaton program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with newAutomaton program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.cuni.mff.ksi.jinfer.base.automaton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AutomatonCloner is helper class, which does just what it's name says.
 * Given one automaton build with generic type <A> it constructs automaton
 * build with generic type <B>, when given class AutomatonClonerSymbolConverter<A, B>.
 *
 * It deeply clones = constructs the delta function, sets of states, mergedStates...
 * whole internal structure of automaton.
 *
 * It can be used to convert from automaton with AbstractNode on transitions
 * to automaton with Regexp<AbstractNode> on transitions, aso.
 *
 * @author anti
 */
public class AutomatonCloner<A, B> {
  private Map<State<A>, State<B>> stateConversionMap;
  private Map<State<B>, State<A>> reverseStateConversionMap;
  
  public Automaton<B> convertAutomaton(final Automaton<A> anotherAutomaton, final AutomatonClonerSymbolConverter<A, B> symbolConverter) {
    final Automaton<B> newAutomaton= new Automaton<B>(false);

    this.convertAutomaton(anotherAutomaton, newAutomaton, symbolConverter);
    
    return newAutomaton;
  }

  public void convertAutomaton(final Automaton<A> anotherAutomaton, final Automaton<B> newAutomaton, final AutomatonClonerSymbolConverter<A, B> symbolConverter) {
    /* other states */
    final Map<State<A>, Set<Step<A>>> anotherDelta= anotherAutomaton.getDelta();
    
    stateConversionMap = new LinkedHashMap<State<A>, State<B>>();
    reverseStateConversionMap = new LinkedHashMap<State<B>, State<A>>();
    for (State<A> anotherState : anotherDelta.keySet()) {
      final State<B> newState=new State<B>(
                anotherState.getFinalCount(), anotherState.getName()
              );
      stateConversionMap.put(anotherState, newState);
      reverseStateConversionMap.put(newState, anotherState);
      newAutomaton.delta.put(newState, new LinkedHashSet<Step<B>>());
      newAutomaton.reverseDelta.put(newState, new LinkedHashSet<Step<B>>());
      newAutomaton.reverseMergedStates.put(newState, new LinkedHashSet<State<B>>());
      newAutomaton.nameMap.put(newState.getName(), newState);
      for (State<A> anotherState2 : anotherAutomaton.getReverseMergedStates().get(anotherState)) {
        final State<B> newState2=new State<B>(
                  anotherState2.getFinalCount(), anotherState2.getName()
                );
        stateConversionMap.put(anotherState2, newState2);
        reverseStateConversionMap.put(newState2, anotherState2);
        newAutomaton.nameMap.put(newState2.getName(), newState2);
        newAutomaton.reverseMergedStates.get(newState).add(newState2);
      }
    }

    for (State<A> anotherState : anotherDelta.keySet()) {
      final State<B> myState= stateConversionMap.get(anotherState);
      for (Step<A> anotherStep : anotherDelta.get(anotherState)) {
        final B newSymbol= symbolConverter.convertSymbol(anotherStep.getAcceptSymbol());

        final Step<B> newStep= new Step<B>(
                newSymbol,
                stateConversionMap.get(anotherStep.getSource()),
                stateConversionMap.get(anotherStep.getDestination()),
                anotherStep.getUseCount(),
                anotherStep.getMinUseCount()
                );
        for (List<A> str : anotherStep.getInputStrings()) {
          List<B> j = new ArrayList<B>(str.size());
          for (A i : str) {
            j.add(symbolConverter.convertSymbol(i));
          }
          newStep.addInputString(j);
        }

        newAutomaton.delta.get(myState).add(newStep);
        newAutomaton.reverseDelta.get(newStep.getDestination()).add(newStep);
      }
    }


    final Map<State<A>, State<A>> anotherMergedStates= anotherAutomaton.getMergedStates();
    for (State<A> anotherState : anotherMergedStates.keySet()) {
      newAutomaton.mergedStates.put(
              stateConversionMap.get(anotherState),
              stateConversionMap.get(anotherMergedStates.get(anotherState))
              );
      newAutomaton.nameMap.put(
              stateConversionMap.get(anotherState).getName(), 
              stateConversionMap.get(anotherState));
    }

    newAutomaton.initialState= stateConversionMap.get(anotherAutomaton.getInitialState());
    newAutomaton.newStateName= anotherAutomaton.getNewStateName();
  }

  public Map<State<B>, State<A>> getReverseStateConversionMap() {
    return reverseStateConversionMap;
  }

  public Map<State<A>, State<B>> getStateConversionMap() {
    return stateConversionMap;
  }
  
  
}
