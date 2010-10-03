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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class RegexpAutomatonStateRemoval<T> extends RegexpAutomaton<T> {
  private static final Logger LOG = Logger.getLogger(RegexpAutomatonStateRemoval.class);
  
  private State<Regexp<T>> superFinalState;
  private State<Regexp<T>> superInitialState;

  /**
   * Given RegexpAutomaton, creates state removal automaton with superfinal state.
   * 
   * @param anotherAutomaton
   */
  public RegexpAutomatonStateRemoval(final RegexpAutomaton<T> anotherAutomaton) {
    super(anotherAutomaton);
    this.createSuperInitialState();
    this.createSuperFinalState();
  }

  /**
   * Clear cloning constructor.
   * @param anotherAutomaton
   */
  public RegexpAutomatonStateRemoval(final RegexpAutomatonStateRemoval<T> anotherAutomaton) {
    super(anotherAutomaton);
  }

  private void createSuperInitialState() {
    this.superInitialState= this.createNewState();

    final Step<Regexp<T>> newStep= new Step<Regexp<T>>(
            Regexp.<T>getLambda(), this.superInitialState, this.initialState, 1);
    this.delta.get(this.superInitialState).add(newStep);
    this.reverseDelta.get(this.initialState).add(newStep);
  }

  private void createSuperFinalState() {
    this.superFinalState= this.createNewState();

    for (State<Regexp<T>> state : this.delta.keySet()) {
      if (state.equals(this.superFinalState)) {
        continue;
      }
      if (state.getFinalCount() > 0) {
        final Step<Regexp<T>> newStep= new Step<Regexp<T>>(
                Regexp.<T>getLambda(), state, this.superFinalState, 1);
        this.delta.get(state).add(newStep);
        this.reverseDelta.get(this.superFinalState).add(newStep);
      }
    }
  }

  private Step<Regexp<T>> collapseStateLoops(final State<Regexp<T>> state) {
    final List<Step<Regexp<T>>> loopSteps= new LinkedList<Step<Regexp<T>>>();
    for (Step<Regexp<T>> step : this.delta.get(state)) {
      if (step.getDestination().equals(state)) {
        loopSteps.add(step);
      }
    }

    if (loopSteps.isEmpty()) {
      return null;
    }

    Step<Regexp<T>> newLoopStep;
    final List<Regexp<T>> loopChildren= new ArrayList<Regexp<T>>();

    if (loopSteps.size() == 1) {
      final Step<Regexp<T>> oldLoopStep= loopSteps.get(0);

      Regexp<T> symbol= oldLoopStep.getAcceptSymbol();
      newLoopStep=
              new Step<Regexp<T>>(
                new Regexp<T>(
                  symbol.getContent(), symbol.getChildren(),
                  symbol.getType(), RegexpInterval.getKleeneStar()),
                state,
                state,
                1);
      this.delta.get(state).remove(oldLoopStep);
      this.reverseDelta.get(state).remove(oldLoopStep);
      return newLoopStep;
    }

    for (Step<Regexp<T>> oldLoopStep : loopSteps) {
      loopChildren.add(oldLoopStep.getAcceptSymbol());
    }

    final Regexp<T> loopRegexpAlt= Regexp.<T>getAlternation(loopChildren, RegexpInterval.getKleeneStar());
    for (Step<Regexp<T>> oldLoopStep : loopSteps) {
      this.delta.get(state).remove(oldLoopStep);
      this.reverseDelta.get(state).remove(oldLoopStep);
    }
    newLoopStep= new Step<Regexp<T>>(loopRegexpAlt, state, state, 1);
    return newLoopStep;
  }

  private void collapseStateInSteps(final State<Regexp<T>> state) {
    final List<Step<Regexp<T>>> inSteps= new LinkedList<Step<Regexp<T>>>();

    for (Step<Regexp<T>> step : this.reverseDelta.get(state)) {
        inSteps.add(step);
    }

    assert !inSteps.isEmpty();
    /* inSteps */
    if (inSteps.size() == 1) {
      return;
    }

    /* Bucketing according to source of step */
    final Map<State<Regexp<T>>, List<Step<Regexp<T>>>> inStepBuckets= new HashMap<State<Regexp<T>>, List<Step<Regexp<T>>>>();
    for (Step<Regexp<T>> inStep : inSteps) {
      if (!inStepBuckets.containsKey(inStep.getSource())) {
        inStepBuckets.put(inStep.getSource(), new LinkedList<Step<Regexp<T>>>());
      }
      inStepBuckets.get(inStep.getSource()).add(inStep);
    }

    /* in each bucket collapse to alternation - if there are more the one steps */
    for (State<Regexp<T>> inBucketSourceState : inStepBuckets.keySet()) {

      if (inStepBuckets.get(inBucketSourceState).size() == 1) {
        continue; // don't process buckets with only one member
      }

      /* collapse to alternation */
      final List<Regexp<T>> inStepRegexps= new LinkedList<Regexp<T>>();
      for (Step<Regexp<T>> inBucketStep : inStepBuckets.get(inBucketSourceState)) {
        inStepRegexps.add(inBucketStep.getAcceptSymbol());
        this.delta.get(inBucketSourceState).remove(inBucketStep);
        this.reverseDelta.get(state).remove(inBucketStep);
      }
      /* build up new instep with alternation regex */
      final Regexp<T> newInRegexp= Regexp.<T>getAlternation(inStepRegexps);
      final Step<Regexp<T>> newInStep= new Step<Regexp<T>>(newInRegexp, inBucketSourceState, state, 1);
      this.delta.get(inBucketSourceState).add(newInStep);
      this.reverseDelta.get(state).add(newInStep);

    }

  }

  private void collapseStateOutSteps(final State<Regexp<T>> state) {
    final List<Step<Regexp<T>>> outSteps= new LinkedList<Step<Regexp<T>>>();

    for (Step<Regexp<T>> step : this.delta.get(state)) {
        outSteps.add(step);
    }

    assert !outSteps.isEmpty();
    if (outSteps.size() == 1) {
      return;
    }

    /* outSteps */
    final Map<State<Regexp<T>>, List<Step<Regexp<T>>>> outStepBuckets= new HashMap<State<Regexp<T>>, List<Step<Regexp<T>>>>();
    /* bucket according to outstep destination */
    for (Step<Regexp<T>> outStep : outSteps) {
      if (!outStepBuckets.containsKey(outStep.getDestination())) {
        outStepBuckets.put(outStep.getDestination(), new LinkedList<Step<Regexp<T>>>());
      }
      outStepBuckets.get(outStep.getDestination()).add(outStep);
    }

    /* in each bucket collapse to alternation - if there are more the one steps */
    for (State<Regexp<T>> outBucketDestinationState : outStepBuckets.keySet()) {
      if (outStepBuckets.get(outBucketDestinationState).size() == 1) {
        continue;
      }

      /* collapse to alternation */
      final List<Regexp<T>> outStepRegexps= new LinkedList<Regexp<T>>();
      boolean isOptional= false;
      for (Step<Regexp<T>> outBucketStep : outStepBuckets.get(outBucketDestinationState)) {
        // TODO anti Ak lambda medzi nimi, je nepovinny
        if (outBucketStep.getAcceptSymbol().isLambda()) {
          isOptional= true;
        } else {
          outStepRegexps.add(outBucketStep.getAcceptSymbol());
        }
        this.reverseDelta.get(outBucketDestinationState).remove(outBucketStep);
        this.delta.get(state).remove(outBucketStep);
      }
      /* build up new instep with alternation regex */
      RegexpInterval interval;
      if (isOptional) {
        interval= RegexpInterval.getOptional();
      } else {
        interval= RegexpInterval.getOnce();
      }
      final Regexp<T> newOutRegexp;
      // TODO anti loosing interval of token inside!
      if (outStepRegexps.size() == 1) {
        final Regexp<T> oldOutRegexp= outStepRegexps.get(0);
        newOutRegexp= new Regexp<T>(oldOutRegexp.getContent(), oldOutRegexp.getChildren(), oldOutRegexp.getType(),
          interval);
      } else {
        newOutRegexp= Regexp.<T>getAlternation(outStepRegexps, interval);
      }
      final Step<Regexp<T>> newOutStep= new Step<Regexp<T>>(newOutRegexp, state, outBucketDestinationState, 1);
      this.reverseDelta.get(outBucketDestinationState).add(newOutStep);
      this.delta.get(state).add(newOutStep);
    }

  }

  private Step<Regexp<T>> collapseStateParallelSteps(final State<Regexp<T>> state) {
    final Step<Regexp<T>> newLoopStep= this.collapseStateLoops(state);

    this.collapseStateInSteps(state);
    this.collapseStateOutSteps(state);
    return newLoopStep;
  }

  public void removeState(final State<Regexp<T>> state) {
    final Step<Regexp<T>> loopStep= this.collapseStateParallelSteps(state);
    final List<Step<Regexp<T>>> inSteps= new LinkedList<Step<Regexp<T>>>();

    for (Step<Regexp<T>> step : this.reverseDelta.get(state)) {
        inSteps.add(step);
    }

    final List<Step<Regexp<T>>> outSteps= new LinkedList<Step<Regexp<T>>>();

    for (Step<Regexp<T>> step : this.delta.get(state)) {
        outSteps.add(step);
    }

    for (Step<Regexp<T>> inStep : inSteps) {
      for (Step<Regexp<T>> outStep : outSteps) {

        final List<Regexp<T>> newRegexpChildren= new LinkedList<Regexp<T>>();
        if (!inStep.getAcceptSymbol().isLambda()) {
          newRegexpChildren.add(inStep.getAcceptSymbol());
        }

        if (loopStep != null) {
          newRegexpChildren.add(loopStep.getAcceptSymbol());
        }
        if (!outStep.getAcceptSymbol().isLambda()) {
          newRegexpChildren.add(outStep.getAcceptSymbol());
        }

        Regexp<T> newRegexp;
        if (newRegexpChildren.isEmpty()) {
          newRegexp= Regexp.<T>getLambda();
        } else if (newRegexpChildren.size() == 1) {
          newRegexp= newRegexpChildren.get(0);
        } else {
          newRegexp= Regexp.<T>getConcatenation(newRegexpChildren);
        }
        final Step<Regexp<T>> newStep=
                new Step<Regexp<T>>(newRegexp, inStep.getSource(), outStep.getDestination(), 1);

        this.delta.get(inStep.getSource()).add(newStep);
        this.reverseDelta.get(outStep.getDestination()).add(newStep);
      }
    }
    for (Step<Regexp<T>> inStep : this.reverseDelta.get(state)) {
      this.delta.get(inStep.getSource()).remove(inStep);
    }
    this.reverseDelta.remove(state);

    for (Step<Regexp<T>> outStep : this.delta.get(state)) {
      this.reverseDelta.get(outStep.getDestination()).remove(outStep);
    }
    this.delta.remove(state);
    LOG.debug(this);
  }

  public void finalStep() {
    this.collapseStateOutSteps(this.superInitialState);
  }

  /**
   *
   * @return superFinalState
   */
  public State<Regexp<T>> getSuperFinalState() {
    return superFinalState;
  }

  /**
   *
   * @return superInitialState
   */
  public State<Regexp<T>> getSuperInitialState() {
    return superInitialState;
  }

  /**
   * TODO anti Comment!
   * 
   * @param state
   * @return
   */
  public List<Step<Regexp<T>>> getLoopSteps(final State<Regexp<T>> state) {
    final List<Step<Regexp<T>>> loopSteps= new LinkedList<Step<Regexp<T>>>();
    for (Step<Regexp<T>> step : this.delta.get(state)) {
      if (step.getDestination().equals(state)) {
        loopSteps.add(step);
      }
    }
    return loopSteps;
  }
}
