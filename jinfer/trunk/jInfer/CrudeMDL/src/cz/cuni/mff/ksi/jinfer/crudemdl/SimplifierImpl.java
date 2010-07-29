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

class Clusterer {
  static List<Pair<Element, List<Element>>> cluster(final List<Element> initialGrammar) {
    final List<Pair<Element, List<Element>>> clusters= new LinkedList<Pair<Element, List<Element>>>();

    for (Element element : initialGrammar) {
      Boolean found= false;
      for (Pair<Element, List<Element>> cluster : clusters) {
        Element representant= cluster.getFirst();
        if (representant.getName().equalsIgnoreCase(element.getName())) {
          found= true;
          cluster.getSecond().add(element);
        }
        if (!found) {
          List<Element> l = new LinkedList<Element>();
          l.add(element);
          clusters.add(
                  new Pair<Element, List<Element>>(element, l)
                  );
        }
      }
    }
   return clusters;
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

    List<Pair<Element, List<Element>>> clusters = Clusterer.cluster(elementGrammar);

    List<AbstractNode> finalGrammar= new LinkedList<AbstractNode>();

    for (Pair<Element, List<Element>> cluster : clusters) {
      // construct PTA
      List<Element> elementInstances= cluster.getSecond();

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
