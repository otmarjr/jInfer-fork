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
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

class State<T> {
  private Map< T, Step<T>> outSteps;
  private List<Step<T>> inSteps;
  private Integer finalCount;

  State(Integer finalCount) {
    this.inSteps= new LinkedList<Step<T>>();
    this.outSteps= new HashMap<T, Step<T>>();
    this.finalCount= finalCount;
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

  public void addOutStep(Step<T> outStep) {
    this.outSteps.put(outStep.getAcceptSymbol(), outStep);
  }

  State<T> buildPTAOnSymbol(T symbol) {
    if (this.outSteps.containsKey(symbol)) {
      Step<T> outStep= this.outSteps.get(symbol);
      outStep.incUseCount();
      return outStep.getDestination();
    } else {
      State<T> newState= new State<T>(0);
      Step<T> newOutStep= new Step<T>(symbol, this, newState, 1);
      this.addOutStep(newOutStep);
      newState.addInStep(newOutStep);
      return newState;
    }
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
    this.setUseCount(this.getUseCount() + 1);
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
}

class Automaton<T> {
  private State<T> initialState;

  Automaton() {
    initialState= new State<T>(0);
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
}

class Cluster<T> {
  private T representant;
  private Set<T> members;

  Cluster(T representant) {
    this.representant= representant;
    this.members= new TreeSet<T>();
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
  public void start(List<AbstractNode> initialGrammar, SimplifierCallback callback) {
// TODO remove this line when finished, now here to pass this module
    callback.finished( new ArrayList<AbstractNode>() );

    for (AbstractNode node : initialGrammar) {
      if (!NodeType.ELEMENT.equals(node.getType())) {
        StringBuilder sb = new StringBuilder("Initial grammar contains rule with ");
        sb.append(node.getType().toString());
        sb.append(" as left side.");
        throw new IllegalArgumentException(sb.toString());
      }
    }

    InameClusterer clusterer = new InameClusterer();
    clusterer.addAll(initialGrammar);
    List<Cluster<AbstractNode>> clusters = clusterer.cluster();

    List<AbstractNode> finalGrammar= new LinkedList<AbstractNode>();

    for (Cluster<AbstractNode> cluster : clusters) {
      if (!cluster.getRepresentant().isElement()) {
        continue; // we deal only with elements for now
      }

      // construct PTA
      Set<AbstractNode> elementInstances= cluster.getMembers();

      Automaton<AbstractNode> automaton = new Automaton<AbstractNode>();

      for (AbstractNode instance : elementInstances) {
        Element element = (Element) instance;
        Regexp<AbstractNode> rightSide= element.getSubnodes();
        List<AbstractNode> rightSideTokens= rightSide.getTokens();

        State x = automaton.getInitialState();

        for (AbstractNode token : rightSideTokens) {
          AbstractNode representant= clusterer.getRepresentantForItem(token);
          x= x.buildPTAOnSymbol(representant);
        }
        x.incFinalCount();
      }
      LOG.fatal(automaton);
      // simplify
      // convert to regex
      // add to list
    }
//    callback.finished( new ArrayList<AbstractNode>(elements.values()) );
  }
}