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
import org.openide.util.lookup.ServiceProvider;

class State<T> {
  private Map< T, Step<T>> outSteps;
  private List<Step<T>> inSteps;
  private Integer finalCount;

  State(Step<T> inStep, Integer finalCount) {
    this(finalCount);
    this.inSteps.add(inStep);
  }

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
}

class Step<T> {
  private T acceptSymbol;
  private Integer useCount;

  Step(T acceptSymbol) {
    this(acceptSymbol, 0);
  }

  Step(T acceptSymbol, Integer useCount) {
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
}

class InameClusterer extends AbstractClusterer<Element> {
  List<Cluster<Element>> clusters;

  InameClusterer() {
    this.clusters= new LinkedList<Cluster<Element>>();
  }

  /*
   * Add action, add item to some cluster. In our method - elements are clustered by
   * name (ignore case). DeFacto clustering happens at this method - when adding, all
   * clusters are scanned, if there is one with representant of same name, item is added
   * to it. If not, new cluster with item as representant is added.
   */
  @Override
  public void add(Element item) {
    Boolean found= false;
    for (Cluster<Element> cluster : this.clusters) {
      if (
              cluster.getRepresentant().getName().equalsIgnoreCase(item.getName())
              ) {
        cluster.add(item);
        found= true;
        break;
      }
    }
    if (!found) {
      this.clusters.add(
              new Cluster<Element>(item)
              );
    }
  }

  @Override
  public void addAll(List<Element> items) {
    for (Element el : items) {
      this.add(el);
    }
  }

  /*
   * In this method no magic is found, clustering happens already when items are added.
   */
  @Override
  public List<Cluster<Element>> cluster() {
    return this.clusters;
  }
}

/**
 *
 * @author anti
 */
@ServiceProvider(service = Simplifier.class)
public class SimplifierImpl implements Simplifier {

  @Override
  public String getModuleName() {
    return "CrudeMDL";
  }

  @Override
  public void start(List<AbstractNode> initialGrammar, SimplifierCallback callback) {
// TODO remove this line when finished, now here to pass this module
    callback.finished( new ArrayList<AbstractNode>() );

    List<Element> elementGrammar= new LinkedList<Element>();

    Element el = null;
    for (AbstractNode node : initialGrammar) {
      if (!NodeType.ELEMENT.equals(node.getType())) {
        StringBuilder sb = new StringBuilder("Initial grammar contains rule with ");
        sb.append(node.getType().toString());
        sb.append(" as left side.");
        throw new IllegalArgumentException(sb.toString());
      }

      el= (Element) node;
      elementGrammar.add(el);
    }

    InameClusterer clusterer = new InameClusterer();
    clusterer.addAll(elementGrammar);
    List<Cluster<Element>> clusters = clusterer.cluster();

    List<AbstractNode> finalGrammar= new LinkedList<AbstractNode>();

    for (Cluster<Element> cluster : clusters) {
      // construct PTA
      Set<Element> elementInstances= cluster.getMembers();

      Automaton<Element> automaton = new Automaton<Element>();

      for (Element instance : elementInstances) {
        Regexp<AbstractNode> rightSide= instance.getSubnodes();

        State x = automaton.getInitialState();

        
      }

      // simplify
      // convert to regex
      // add to list
    }
//    callback.finished( new ArrayList<AbstractNode>(elements.values()) );
  }
}