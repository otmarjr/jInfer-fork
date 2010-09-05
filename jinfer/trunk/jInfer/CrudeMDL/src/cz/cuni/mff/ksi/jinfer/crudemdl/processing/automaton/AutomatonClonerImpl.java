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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class AutomatonClonerImpl<A, B> implements AutomatonCloner<A, B> {

  @Override
  public Automaton<B> convertAutomaton(final Automaton<A> anotherAutomaton, final AutomatonSymbolConverter<A, B> symbolConverter) {
    Automaton<B> newAutomaton= new Automaton<B>(false);

    this.convertAutomaton(anotherAutomaton, newAutomaton, symbolConverter);
    
    return newAutomaton;
  }

  @Override
  public void convertAutomaton(Automaton<A> anotherAutomaton, Automaton<B> newAutomaton, AutomatonSymbolConverter<A, B> symbolConverter) {
    /* other states */
    final Map<State<A>, Set<Step<A>>> anotherDelta= anotherAutomaton.getDelta();
    final Map<State<A>, Set<Step<A>>> anotherReverseDelta= anotherAutomaton.getReverseDelta();

    final Map<State<A>, State<B>> stateConversionMap= new HashMap<State<A>, State<B>>();
    for (State<A> anotherState : anotherDelta.keySet()) {
      final State<B> newState=new State<B>(
                anotherState.getFinalCount(), anotherState.getName()
              );
      stateConversionMap.put(anotherState, newState);
      newAutomaton.delta.put(newState, new HashSet<Step<B>>());
      newAutomaton.reverseDelta.put(newState, new HashSet<Step<B>>());
      newAutomaton.reverseMergedStates.put(newState, new HashSet<State<B>>());
      for (State<A> anotherState2 : anotherAutomaton.getReverseMergedStates().get(anotherState)) {
        final State<B> newState2=new State<B>(
                  anotherState.getFinalCount(), anotherState.getName()
                );
        stateConversionMap.put(anotherState2, newState2);
        newAutomaton.reverseMergedStates.get(newState).add(newState2);
      }
    }

    for (State<A> anotherState : anotherDelta.keySet()) {
      State<B> myState= stateConversionMap.get(anotherState);
      for (Step<A> anotherStep : anotherDelta.get(anotherState)) {
        B newSymbol= symbolConverter.convertSymbol(anotherStep.getAcceptSymbol());

        Step<B> newStep= new Step<B>(
                newSymbol,
                stateConversionMap.get(anotherStep.getSource()),
                stateConversionMap.get(anotherStep.getDestination()),
                anotherStep.getUseCount()
                );

        newAutomaton.delta.get(myState).add(newStep);
        newAutomaton.reverseDelta.get(newStep.getDestination()).add(newStep);
      }
    }


    Map<State<A>, State<A>> anotherMergedStates= anotherAutomaton.getMergedStates();
    for (State<A> state : anotherMergedStates.keySet()) {
      newAutomaton.mergedStates.put(
              stateConversionMap.get(state),
              stateConversionMap.get(anotherMergedStates.get(state))
              );
    }

    newAutomaton.initialState= stateConversionMap.get(anotherAutomaton.getInitialState());
    newAutomaton.newStateName= anotherAutomaton.getNewStateName();
  }
}
