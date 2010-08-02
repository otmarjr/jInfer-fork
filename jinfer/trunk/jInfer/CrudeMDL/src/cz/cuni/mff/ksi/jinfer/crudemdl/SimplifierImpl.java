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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

class State<T> {
  private Map< T, Step<T>> outSteps;
  private List<Step<T>> inSteps;
  private Integer finalCount;
  private Integer name;
  private Automaton<T> myAutomaton;

  State(Integer finalCount, Integer name, Automaton<T> myAutomaton) {
    this.inSteps= new LinkedList<Step<T>>();
    this.outSteps= new HashMap<T, Step<T>>();
    this.finalCount= finalCount;
    this.name= name;
    this.myAutomaton= myAutomaton;
  }

  /**
   * @return the outSteps
   */
  public Map<T, Step<T>> getOutSteps() {
    return outSteps;
  }

  /**
   * @param outSteps the outSteps to set
   */
  public void setOutSteps(Map<T, Step<T>> outSteps) {
    this.outSteps = outSteps;
  }

  /**
   * @return the inSteps
   */
  public List<Step<T>> getInSteps() {
    return inSteps;
  }

  /**
   * @param inSteps the inSteps to set
   */
  public void setInSteps(List<Step<T>> inSteps) {
    this.inSteps = inSteps;
  }

  /**
   * @return the finalCount
   */
  public Integer getFinalCount() {
    return finalCount;
  }

  /**
   * @param finalCount the finalCount to set
   */
  public void setFinalCount(Integer finalCount) {
    this.finalCount = finalCount;
  }

  void incFinalCount() {
    this.setFinalCount(this.getFinalCount() + 1);
  }

  public void addInStep(Step<T> inStep) {
    this.inSteps.add(inStep);
  }

  public void addInStepsAll(Collection<Step<T>> inSteps) {
    this.inSteps.addAll(inSteps);
  }

  public void addOutStep(Step<T> outStep) {
    this.outSteps.put(outStep.getAcceptSymbol(), outStep);
  }

  State<T> buildPTAOnSymbol(T symbol) {
    if (this.outSteps.containsKey(symbol)) {
      Step<T> outStep= this.outSteps.get(symbol);
      outStep.incUseCount();
      return outStep.getDestination();
    } else {
      State<T> newState= new State<T>(0, this.name + 1, this.myAutomaton);
      this.myAutomaton.addNewStateCreated(newState);
      Step<T> newOutStep= new Step<T>(symbol, this, newState, 1);
      this.addOutStep(newOutStep);
      newState.addInStep(newOutStep);
      return newState;
    }
  }

  public List<Pair<State<T>, State<T>>> find21contexts() {
    List<Pair<State<T>, State<T>>> contexts= new LinkedList<Pair<State<T>, State<T>>>();
    for (Step<T> inStep: this.inSteps) {
      for (Step<T> secondInStep: inStep.getSource().getInSteps()) {
        contexts.add(new Pair(
                secondInStep.getSource(),
                inStep.getSource()
                ));
      }
    }
    return contexts;
  }

  @Override
  public String toString() {
  //  return super.toString();
    StringBuilder sb = new StringBuilder("[");
    sb.append(this.getName());
    sb.append("|");
    sb.append(this.finalCount);
    sb.append("] steps:\n");
    for (T symbol : this.outSteps.keySet()) {
      sb.append("on ");
      sb.append(this.outSteps.get(symbol));
      sb.append(" -> ");
      sb.append(this.outSteps.get(symbol).getDestination().getName());
      sb.append("\n");
    }
    sb.append("\n");
    Set<State<T>> outStates= new HashSet<State<T>>();
    for (Step<T> step : this.outSteps.values()) {
      outStates.add(step.getDestination());
    }
    return sb.toString();
  }

  /**
   * @return the name
   */
  public Integer getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(Integer name) {
    this.name = name;
  }
}

class Step<T> {
  private T acceptSymbol;
  private Integer useCount;
  private State<T> source;
  private State<T> destination;

  Step(T acceptSymbol, State<T> source, State<T> destination, Integer useCount) {
    this.acceptSymbol= acceptSymbol;
    this.useCount= useCount;
    this.source= source;
    this.destination= destination;
  }

  /**
   * @return the acceptSymbol
   */
  public T getAcceptSymbol() {
    return acceptSymbol;
  }

  /**
   * @param acceptSymbol the acceptSymbol to set
   */
  public void setAcceptSymbol(T acceptSymbol) {
    this.acceptSymbol = acceptSymbol;
  }

  /**
   * @return the useCount
   */
  public Integer getUseCount() {
    return useCount;
  }

  /**
   * @param useCount the useCount to set
   */
  public void setUseCount(Integer useCount) {
    this.useCount = useCount;
  }

  public void incUseCount() {
    this.incUseCount(1);
  }

  public void incUseCount(Integer i) {
    this.setUseCount(this.getUseCount() + i);
  }

  /**
   * @return the source
   */
  public State<T> getSource() {
    return source;
  }

  /**
   * @param source the source to set
   */
  public void setSource(State<T> source) {
    this.source = source;
  }

  /**
   * @return the destination
   */
  public State<T> getDestination() {
    return destination;
  }

  /**
   * @param destination the destination to set
   */
  public void setDestination(State<T> destination) {
    this.destination = destination;
  }

  @Override
  public String toString() {
    //return super.toString();
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append(this.acceptSymbol);
    sb.append("|");
    sb.append(this.useCount);
    sb.append("}");
    return sb.toString();
  }
}

class Automaton<T> {
  private State<T> initialState;
  private List<State<T>> states;

  Automaton() {
    this.initialState= new State<T>(0, 1, this);
    this.states= new LinkedList<State<T>>();
    this.states.add(this.initialState);
  }

  /**
   * @return the initialState
   */
  public State<T> getInitialState() {
    return initialState;
  }

  /**
   * @param initialState the initialState to set
   */
  public void setInitialState(State<T> initialState) {
    this.initialState = initialState;
  }

  @Override
  public String toString() {
//    return super.toString();
    StringBuilder sb = new StringBuilder("Automaton\n");
    for (State<T> state: this.states) {
    sb.append(state);
    }
    return sb.toString();
  }

  public void addNewStateCreated(State<T> newState) {
    this.states.add(newState);
  }

  public void mergeStates(State<T> mainState, State<T> mergedState) {
    List<Step<T>> mergedStateInSteps= mergedState.getInSteps();
    for (Step<T> mergedStateInStep : mergedStateInSteps) {
      mergedStateInStep.setDestination(mainState);
    }
    Map<T, Step<T>> mergedStateOutSteps= mergedState.getOutSteps();
    for (T symbol : mergedStateOutSteps.keySet()) {
      Step<T> mergedStateOutStep= mergedStateOutSteps.get(symbol);
      if (mainState.getOutSteps().containsKey(symbol)) {
        State<T> mainStateDestination= mainState.getOutSteps().get(symbol).getDestination();
        State<T> mergedStateDestination= mergedStateOutStep.getDestination();
        if (!mainStateDestination.equals(mergedStateDestination)) {
          this.mergeStates(mainStateDestination, mergedStateDestination);
        }
        mainState.getOutSteps().get(symbol).incUseCount(mergedStateOutStep.getUseCount());
      } else {
        mergedStateOutStep.setSource(mainState);
        mainState.addOutStep(mergedStateOutStep);
      }
    }
    this.states.remove(mergedState);
    // TODO check loops
  }

  public void mergeStates(State<T> mainState, List<State<T>> mergedStates) {
    for (State<T> mergedState : mergedStates) {
      this.mergeStates(mainState, mergedState);
    }
  }

  private Boolean hasIntersection(List<Pair<State<T>, State<T>>> contextsA, List<Pair<State<T>, State<T>>> contextsB) {
    for (Pair<State<T>, State<T>> contextA : contextsA) {
      for (Pair<State<T>, State<T>> contextB : contextsB) {
        if (
                contextA.getFirst().equals(contextB.getFirst())&&
                contextA.getSecond().equals(contextB.getSecond())
                ) {
          return true;
        }
      }
    }
    return false;
  }

  public void make21context() {
    Deque<State<T>> toTestStates = new LinkedList<State<T>>();
    toTestStates.addAll(this.states);
    State<T> toTestState;
    List<State<T>> toMergeStates= new LinkedList<State<T>>();
    while (!toTestStates.isEmpty()) {
      toTestState= toTestStates.removeFirst();
      List<Pair<State<T>, State<T>>> testStateKHcontexts= toTestState.find21contexts();
      if (testStateKHcontexts.size() == 0) {
        continue;
      }
      for (State<T> anotherState : this.states) {
        if (anotherState.equals(toTestState)) {
          continue;
        }
        List<Pair<State<T>, State<T>>> anotherStateKHcontexts= anotherState.find21contexts();
        if (this.hasIntersection(testStateKHcontexts, anotherStateKHcontexts)) {
          toMergeStates.add(anotherState);
        }
      }
      // merge them
      if (toMergeStates.isEmpty()) {
        continue;
      } else {
        this.mergeStates(toTestState, toMergeStates);
        toTestStates.removeAll(toMergeStates);
        toMergeStates.clear();
      }
    }
  }
}

class Cluster<T> {
  private T representant;
  private Set<T> members;

  Cluster(T representant) {
    this.representant= representant;
    this.members= new HashSet<T>();
    this.members.add(representant);
  }

  /**
   * @return the representant
   */
  public T getRepresentant() {
    return representant;
  }

  /**
   * @param representant the representant to set
   */
  public void setRepresentant(T representant) {
    this.representant = representant;
  }

  /**
   * @return the members
   */
  public Set<T> getMembers() {
    return members;
  }

  /**
   * @param members the members to set
   */
  public void setMembers(Set<T> members) {
    this.members = members;
  }

  public Boolean isMember(T item) {
    return this.members.contains(item);
  }

  public void add(T item) {
    this.members.add(item);
  }

  @Override
  public String toString() {
//    return super.toString();
    StringBuilder sb = new StringBuilder("Cluster\n");
    sb.append("representant: ");
    sb.append(this.representant);
    sb.append("\nmembers: ");
    for (T member: this.members) {
      sb.append(member);
    }
    return sb.toString();
  }
}

abstract class AbstractClusterer<T> {
  /*
   * Add x to some cluster, find the right one or create new. Depends on clustering algorithm.
   * One can do classify on demand, or can create a method that builds just one cluster on classify
   * and then call some own method to 'reclassify' - do the real clustering. Opposite is also in mind
   * do cluster for each x, then apply own merging clustering algorithm. The algorithms should be then
   * added in own methods.
   */
  public abstract void add(T item);
  public abstract void addAll(List<T> items);
  public abstract List<Cluster<T>> cluster();
  public abstract T getRepresentantForItem(T item);
}

class InameClusterer extends AbstractClusterer<AbstractNode> {
  List<Cluster<AbstractNode>> clusters;

  InameClusterer() {
    this.clusters= new LinkedList<Cluster<AbstractNode>>();
  }

  /*
   * Add action, add item to some cluster. In our method - elements are clustered by
   * name (ignore case). DeFacto clustering happens at this method - when adding, all
   * clusters are scanned, if there is one with representant of same name, item is added
   * to it. If not, new cluster with item as representant is added.
   */
  @Override
  public void add(AbstractNode item) {
    Boolean found= false;
    for (Cluster<AbstractNode> cluster : this.clusters) {
      AbstractNode representant= cluster.getRepresentant();
      if (representant.isSimpleData()) {
        cluster.add(item);
        found= true;
        break;
      } else if (representant.isElement()) {
        if (representant.getName().equalsIgnoreCase(item.getName())) {
          cluster.add(item);
          found= true;
          break;
        }
      }
    }
    if (!found) {
      this.clusters.add(
              new Cluster<AbstractNode>(item)
              );
    }
  }

  @Override
  public void addAll(List<AbstractNode> items) {
    for (AbstractNode node : items) {
      this.add(node);
    }
  }

  /*
   * In this method no magic is found, clustering happens already when items are added.
   */
  @Override
  public List<Cluster<AbstractNode>> cluster() {
    return this.clusters;
  }

  @Override
  public AbstractNode getRepresentantForItem(AbstractNode item) {
    for (Cluster<AbstractNode> cluster : this.clusters) {
      if (cluster.isMember(item)) {
        return cluster.getRepresentant();
      }
    }
    throw new IllegalArgumentException("Node " + item.toString() + " is not in clusters, it wasn't added, i can't find it.");
  }
}

/**
 *
 * @author anti
 */
@ServiceProvider(service = Simplifier.class)
public class SimplifierImpl implements Simplifier {
  private static final Logger LOG = Logger.getLogger(Simplifier.class);

  @Override
  public String getModuleName() {
    return "CrudeMDL";
  }

  @Override
  public void start(final List<AbstractNode> initialGrammar, final SimplifierCallback callback) {
// TODO remove this line when finished, now here to pass this module
//    callback.finished( new ArrayList<AbstractNode>() );

    for (AbstractNode node : initialGrammar) {
      if (!NodeType.ELEMENT.equals(node.getType())) {
        StringBuilder sb = new StringBuilder("Initial grammar contains rule with ");
        sb.append(node.getType().toString());
        sb.append(" as left side.");
        throw new IllegalArgumentException(sb.toString());
      }
      if (node == null) {
        throw new IllegalArgumentException("Got null as left side in grammar.");
      }
    }

    InameClusterer clusterer = new InameClusterer();
    clusterer.addAll(initialGrammar);
    List<Cluster<AbstractNode>> clusters = clusterer.cluster();

    List<AbstractNode> finalGrammar= new LinkedList<AbstractNode>();

    for (Cluster<AbstractNode> cluster : clusters) {
      if (!cluster.getRepresentant().isElement()) {
        LOG.info(cluster);
        continue; // we deal only with elements for now
      }

      // construct PTA
      Set<AbstractNode> elementInstances= cluster.getMembers();

      Automaton<AbstractNode> automaton = new Automaton<AbstractNode>();
      SimpleData universalSimpleData= new SimpleData(new ArrayList<String>(), "SIMPLE REPRE", new HashMap<String, Object>(), "", new ArrayList<String>());

      for (AbstractNode instance : elementInstances) {
        Element element = (Element) instance;
        Regexp<AbstractNode> rightSide= element.getSubnodes();
        List<AbstractNode> rightSideTokens= rightSide.getTokens();

        State x = automaton.getInitialState();

        for (AbstractNode token : rightSideTokens) {
          if (token.isSimpleData()) {
            x= x.buildPTAOnSymbol(universalSimpleData);
          } else {
            AbstractNode representant= clusterer.getRepresentantForItem(token);
            x= x.buildPTAOnSymbol(representant);
          }
        }
        x.incFinalCount();
      }
      LOG.fatal(cluster.getRepresentant());
      LOG.fatal(automaton);
      
      // simplify
      automaton.make21context();
      LOG.fatal("LAKHGFKJLHGKLAHLKG\n");
      LOG.fatal(automaton);


      // convert to regex
      // add to list
    }
//    callback.finished( new ArrayList<AbstractNode>(elements.values()) );
    callback.finished( new ArrayList<AbstractNode>() );
  }
}