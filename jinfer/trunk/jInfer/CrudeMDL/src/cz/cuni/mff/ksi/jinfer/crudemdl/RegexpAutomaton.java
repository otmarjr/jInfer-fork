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

package cz.cuni.mff.ksi.jinfer.crudemdl;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author anti
 */
public class RegexpAutomaton extends Automaton<Regexp<AbstractNode>> {
  private static final Logger LOG = Logger.getLogger(RegexpAutomaton.class);
  private State<Regexp<AbstractNode>> superFinalState;

  public RegexpAutomaton(final Automaton<AbstractNode> anotherAutomaton) {
    super(false);
    this.newStateName= anotherAutomaton.getNewStateName();

    /* other states */
    final Map<State<AbstractNode>, Set<Step<AbstractNode>>> anotherDelta= anotherAutomaton.getDelta();
    final Map<State<AbstractNode>, Set<Step<AbstractNode>>> anotherReverseDelta= anotherAutomaton.getReverseDelta();

    final Map<State<AbstractNode>, State<Regexp<AbstractNode>>> stateConversionMap= new TreeMap<State<AbstractNode>, State<Regexp<AbstractNode>>>();
    for (State<AbstractNode> anotherState : anotherDelta.keySet()) {
      final State<Regexp<AbstractNode>> newState=new State<Regexp<AbstractNode>>(
                anotherState.getFinalCount(), anotherState.getName(), this
              );
      stateConversionMap.put(anotherState, newState);
      this.delta.put(newState, new HashSet<Step<Regexp<AbstractNode>>>());
      this.reverseDelta.put(newState, new HashSet<Step<Regexp<AbstractNode>>>());
    }

    final Map<Step<AbstractNode>, Step<Regexp<AbstractNode>>> stepConversionMap= new TreeMap<Step<AbstractNode>, Step<Regexp<AbstractNode>>>();
    for (State<AbstractNode> anotherState : anotherDelta.keySet()) {
      final State<Regexp<AbstractNode>> myState= stateConversionMap.get(anotherState);
      for (Step<AbstractNode> anotherStep : anotherDelta.get(anotherState)) {
        final Regexp<AbstractNode> newSymbol= Regexp.<AbstractNode>getToken(
                anotherStep.getAcceptSymbol()
                );

        final Step<Regexp<AbstractNode>> newStep= new Step<Regexp<AbstractNode>>(
                newSymbol,
                stateConversionMap.get(anotherStep.getSource()),
                stateConversionMap.get(anotherStep.getDestination()),
                anotherStep.getUseCount()
                );

        stepConversionMap.put(anotherStep, newStep);
        this.delta.get(myState).add(newStep);
      }
    }

    for (State<AbstractNode> anotherState : anotherReverseDelta.keySet()) {
      for (Step<AbstractNode> anotherStep : anotherReverseDelta.get(anotherState)) {
        this.reverseDelta.get(
                stateConversionMap.get(anotherState)
                ).add(
                stepConversionMap.get(anotherStep)
                );
      }
    }

    this.initialState= stateConversionMap.get(anotherAutomaton.getInitialState());
    this.superFinalState= null;
  }

  private void createSuperFinalState() {
    this.superFinalState= this.createNewState();

    for (State<Regexp<AbstractNode>> state : this.delta.keySet()) {
      if (state.getFinalCount() > 0) {
        final Step<Regexp<AbstractNode>> newStep= new Step<Regexp<AbstractNode>>(
                Regexp.<AbstractNode>getLambda(), state, this.superFinalState, 1);
        this.delta.get(state).add(newStep);
        this.reverseDelta.get(this.superFinalState).add(newStep);
      }
    }
  }

  private int getRegexpWeight(final Regexp<AbstractNode> regexp) {
    return regexp.getTokens().size();
  }

  private int getStateWeight(final State<Regexp<AbstractNode>> state) {
    int weight= 0;
    for (Step<Regexp<AbstractNode>> step : this.reverseDelta.get(state)) {
      weight+= this.getRegexpWeight(step.getAcceptSymbol());
    }

    for (Step<Regexp<AbstractNode>> step : this.delta.get(state)) {
      if (!step.getDestination().equals(state)) { // To prevent twice counting the loops
        weight+= this.getRegexpWeight(step.getAcceptSymbol());
      }
    }
    return weight;
  }

  private Step<Regexp<AbstractNode>> collapseStateLoops(final State<Regexp<AbstractNode>> state) {
    final List<Step<Regexp<AbstractNode>>> loopSteps= new LinkedList<Step<Regexp<AbstractNode>>>();
    for (Step<Regexp<AbstractNode>> step : this.delta.get(state)) {
      if (step.getDestination().equals(state)) {
        loopSteps.add(step);
      }
    }

    if (loopSteps.isEmpty()) {
      return null;
    }

    final Step<Regexp<AbstractNode>> newLoopStep;
    final List<Regexp<AbstractNode>> loopChildren= new ArrayList<Regexp<AbstractNode>>();

    if (loopSteps.size() == 1) {
      Step<Regexp<AbstractNode>> oldLoopStep= loopSteps.get(0);
      loopChildren.add(oldLoopStep.getAcceptSymbol());
      newLoopStep=
              new Step<Regexp<AbstractNode>>(
                Regexp.<AbstractNode>getKleene(loopChildren),
                state,
                state,
                1);
      this.delta.get(state).remove(oldLoopStep);
      this.reverseDelta.get(state).remove(oldLoopStep);
      return newLoopStep;
    }

    for (Step<Regexp<AbstractNode>> oldLoopStep : loopSteps) {
      loopChildren.add(oldLoopStep.getAcceptSymbol());
    }
    
    final Regexp<AbstractNode> loopRegexpAlt= Regexp.<AbstractNode>getAlternation(loopChildren);
    final List<Regexp<AbstractNode>> kleeneChildren= new ArrayList<Regexp<AbstractNode>>();
    kleeneChildren.add(loopRegexpAlt);
    final Regexp<AbstractNode> loopRegexpKleene= Regexp.<AbstractNode>getKleene(kleeneChildren);
    for (Step<Regexp<AbstractNode>> oldLoopStep : loopSteps) {
      this.delta.get(state).remove(oldLoopStep);
      this.reverseDelta.get(state).remove(oldLoopStep);
    }
    newLoopStep= new Step<Regexp<AbstractNode>>(loopRegexpKleene, state, state, 1);
    return newLoopStep;
  }

  private void collapseStateInSteps(final State<Regexp<AbstractNode>> state) {
    final List<Step<Regexp<AbstractNode>>> inSteps= new LinkedList<Step<Regexp<AbstractNode>>>();

    for (Step<Regexp<AbstractNode>> step : this.reverseDelta.get(state)) {
        inSteps.add(step);
    }

    assert !inSteps.isEmpty();
    /* inSteps */
    if (inSteps.size() == 1) {
      return;
    }

    /* Bucketing according to source of step */
    final Map<State<Regexp<AbstractNode>>, List<Step<Regexp<AbstractNode>>>> inStepBuckets= new TreeMap<State<Regexp<AbstractNode>>, List<Step<Regexp<AbstractNode>>>>();
    for (Step<Regexp<AbstractNode>> inStep : inSteps) {
      if (!inStepBuckets.containsKey(inStep.getSource())) {
        inStepBuckets.put(inStep.getSource(), new LinkedList<Step<Regexp<AbstractNode>>>());
      }
      inStepBuckets.get(inStep.getSource()).add(inStep);
    }

    /* in each bucket collapse to alternation - if there are more the one steps */
    for (State<Regexp<AbstractNode>> inBucketSourceState : inStepBuckets.keySet()) {

      if (inStepBuckets.get(inBucketSourceState).size() == 1) {
        continue; // don't process buckets with only one member
      }

      /* collapse to alternation */
      final List<Regexp<AbstractNode>> inStepRegexps= new LinkedList<Regexp<AbstractNode>>();
      for (Step<Regexp<AbstractNode>> inBucketStep : inStepBuckets.get(inBucketSourceState)) {
        inStepRegexps.add(inBucketStep.getAcceptSymbol());
        this.delta.get(inBucketSourceState).remove(inBucketStep);
        this.reverseDelta.get(state).remove(inBucketStep);
      }
      /* build up new instep with alternation regex */
      final Regexp<AbstractNode> newInRegexp= Regexp.<AbstractNode>getAlternation(inStepRegexps);
      final Step<Regexp<AbstractNode>> newInStep= new Step<Regexp<AbstractNode>>(newInRegexp, inBucketSourceState, state, 1);
      this.delta.get(inBucketSourceState).add(newInStep);
      this.reverseDelta.get(state).add(newInStep);

    }

  }

  private void collapseStateOutSteps(Step<Regexp<AbstractNode>> state) {
    final List<Step<Regexp<AbstractNode>>> outSteps= new LinkedList<Step<Regexp<AbstractNode>>>();

    for (Step<Regexp<AbstractNode>> step : this.delta.get(state)) {
        outSteps.add(step);
    }

    //TODO anti assertion here (steps are partitioned)

    assert !outSteps.isEmpty();
    if (outSteps.size() == 1) {
      return;
    }

    /* outSteps */
    final Map<State<Regexp<AbstractNode>>, List<Step<Regexp<AbstractNode>>>> outStepBuckets= new TreeMap<State<Regexp<AbstractNode>>, List<Step<Regexp<AbstractNode>>>>();
    /* bucket according to outstep destination */
    for (Step<Regexp<AbstractNode>> outStep : outSteps) {
      if (!outStepBuckets.containsKey(outStep.getDestination())) {
        outStepBuckets.put(outStep.getDestination(), new LinkedList<Step<Regexp<AbstractNode>>>());
      }
      outStepBuckets.get(outStep.getDestination()).add(outStep);
    }

    for (State<Regexp<AbstractNode>> outBucketDestinationState : outStepBuckets.keySet()) {
      if (outStepBuckets.get(outBucketDestinationState).size() == 1) {
        continue;
      }

      final List<Regexp<AbstractNode>> outStepRegexps= new LinkedList<Regexp<AbstractNode>>();
      for (Step<Regexp<AbstractNode>> outBucketStep : outStepBuckets.get(outBucketDestinationState)) {
        outStepRegexps.add(outBucketStep.getAcceptSymbol());
        this.reverseDelta.get(outBucketDestinationState).remove(outBucketStep);
        this.delta.get(state).remove(outBucketStep);
      }
      final Regexp<AbstractNode> newOutRegexp= Regexp.<AbstractNode>getAlternation(outStepRegexps);
      final Step<Regexp<AbstractNode>> newOutStep= new Step<Regexp<AbstractNode>>(newOutRegexp, state, outBucketDestinationState, 1);
      this.reverseDelta.get(outBucketDestinationState).add(newOutStep);
      this.delta.get(state).add(newOutStep);
    }
    
  }

  private Step<Regexp<AbstractNode>> collapseStateParallelSteps(final State<Regexp<AbstractNode>> state) {
    final Step<Regexp<AbstractNode>> newLoopStep= this.collapseStateLoops(state);

    this.collapseStateInSteps(state);
    this.collapseStateOutSteps(state);
    return newLoopStep;
  }

  private void regexRemoveState(State<Regexp<AbstractNode>> state, Step<Regexp<AbstractNode>> loopStep) {
    final List<Step<Regexp<AbstractNode>>> inSteps= new LinkedList<Step<Regexp<AbstractNode>>>();

    for (Step<Regexp<AbstractNode>> step : this.reverseDelta.get(state)) {
        inSteps.add(step);
    }

    final List<Step<Regexp<AbstractNode>>> outSteps= new LinkedList<Step<Regexp<AbstractNode>>>();

    for (Step<Regexp<AbstractNode>> step : this.delta.get(state)) {
        outSteps.add(step);
    }

    for (Step<Regexp<AbstractNode>> inStep : inSteps) {
      for (Step<Regexp<AbstractNode>> outStep : outSteps) {

        final List<Regexp<AbstractNode>> newRegexpChildren= new LinkedList<Regexp<AbstractNode>>();
        newRegexpChildren.add(inStep.getAcceptSymbol());
        if (loopStep != null) {
          newRegexpChildren.add(loopStep.getAcceptSymbol());
        }
        newRegexpChildren.add(outStep.getAcceptSymbol());

        Regexp<AbstractNode> newRegexp= Regexp.<AbstractNode>getConcatenation(newRegexpChildren);
        Step<Regexp<AbstractNode>> newStep=
                new Step<Regexp<AbstractNode>>(newRegexp, inStep.getSource(), outStep.getDestination(), 1);

        this.delta.get(inStep.getSource()).add(newStep);
        this.reverseDelta.get(outStep.getDestination()).add(newStep);
      }
    }
    for (Step<Regexp<AbstractNode>> inStep : this.reverseDelta.get(state)) {
      this.delta.get(inStep.getSource()).remove(inStep);
    }
    this.reverseDelta.remove(state);

    for (Step<Regexp<AbstractNode>> outStep : this.delta.get(state)) {
      this.reverseDelta.get(outStep.getDestination()).remove(outStep);
    }
    this.delta.remove(state);
  }

  public void makeRegexpForm() {
    this.createSuperFinalState();
    while (this.delta.keySet().size() > 2) {
      int minWeight= Integer.MAX_VALUE;
      State<Regexp<AbstractNode>> minState= null;
      for (State<Regexp<AbstractNode>> state : this.delta.keySet()) {
        if (state.equals(this.initialState)||state.equals(this.superFinalState)) {
          continue;//remove except initial state
        }
        final int stateWeight= this.getStateWeight(state);
        if (stateWeight < minWeight) {
          minWeight= stateWeight;
          minState= state;
        }
      }

      Step<Regexp<AbstractNode>> loopStep= this.collapseStateParallelSteps(minState);
      this.regexRemoveState(minState, loopStep);
    }
  }
}
