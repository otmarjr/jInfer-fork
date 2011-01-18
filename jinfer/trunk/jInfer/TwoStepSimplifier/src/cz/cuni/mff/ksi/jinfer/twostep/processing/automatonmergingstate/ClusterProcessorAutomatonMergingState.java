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

package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Class providing method for inferring DTD for single element. In this implementation
 * deterministic finite automaton is used.
 * First Prefix-Tree automaton is constructed using cluster.members() as positive
 * examples. Then, given MergeConditionTesterKHContext, merging of states occurs
 * until there are no more states to merge. Currently k=2, h=1, so
 * producing 2,1-context automaton by merging.
 *
 * @author anti
 */
public class ClusterProcessorAutomatonMergingState implements ClusterProcessor<AbstractStructuralNode> {
  private static final Logger LOG = Logger.getLogger(ClusterProcessorAutomatonMergingState.class);
  private final AutomatonSimplifier<AbstractStructuralNode> automatonSimplifier;
  private final RegexpAutomatonSimplifier<AbstractStructuralNode> regexpAutomatonSimplifier;

  private class AbstractStructuralNodeSymbolToString implements SymbolToString<AbstractStructuralNode> {
    @Override
    public String toString(AbstractStructuralNode symbol) {
      return symbol.isElement() ? symbol.getName() : "#CDATA";
    }
  };

  private class RegexpAbstractSymbolToString implements SymbolToString<Regexp<AbstractStructuralNode>> {
    private AbstractStructuralNodeSymbolToString g= new AbstractStructuralNodeSymbolToString();

    @Override
    public String toString(Regexp<AbstractStructuralNode> symbol) {
      switch (symbol.getType()) {
        case LAMBDA:
          return symbol.toString();
        case TOKEN:
          return g.toString(symbol.getContent());
        case CONCATENATION:
          return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(symbol.getChildren(), ",",
            new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {
              @Override
              public String toString(Regexp<AbstractStructuralNode> t) {
                return RegexpAbstractSymbolToString.this.toString(t);
              }
          });
        case ALTERNATION:
          return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(symbol.getChildren(), "|",
            new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {
              @Override
              public String toString(Regexp<AbstractStructuralNode> t) {
                return RegexpAbstractSymbolToString.this.toString(t);
              }
          });
        case PERMUTATION:
          return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(symbol.getChildren(), "&",
            new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {
              @Override
              public String toString(Regexp<AbstractStructuralNode> t) {
                return RegexpAbstractSymbolToString.this.toString(t);
              }
          });
      }
      return symbol.toString();
    }
  };

  private final AbstractStructuralNodeSymbolToString elementSymbolToString;
  private final RegexpAbstractSymbolToString regexpAbstractToString;

  public ClusterProcessorAutomatonMergingState(
          final AutomatonSimplifierFactory automatonSimplifierFactory,
          final RegexpAutomatonSimplifierFactory regexpAutomatonSimplifierFactory) {
    this.automatonSimplifier= automatonSimplifierFactory.<AbstractStructuralNode>create();
    this.regexpAutomatonSimplifier= regexpAutomatonSimplifierFactory.<AbstractStructuralNode>create();
    this.elementSymbolToString= new AbstractStructuralNodeSymbolToString();
    this.regexpAbstractToString= new RegexpAbstractSymbolToString();
  }

  @Override
  public AbstractStructuralNode processCluster(final Clusterer<AbstractStructuralNode> clusterer, final List<AbstractStructuralNode> rules) throws InterruptedException {
    if (rules.isEmpty()) {
      throw new IllegalArgumentException("Rules has to be non-empty.");
    }
    // 3.1 construct PTA
    final Automaton<AbstractStructuralNode> automaton = new Automaton<AbstractStructuralNode>(true);

    for (AbstractStructuralNode instance : rules) {
      final Element element = (Element) instance;
      final Regexp<AbstractStructuralNode> rightSide= element.getSubnodes();

      if (!rightSide.isConcatenation()) {
        throw new IllegalArgumentException("Right side of rule '" + element.getName() + "' is not a concatenation regexp, it is: " + element.getSubnodes().toString());
      }

      final List<AbstractStructuralNode> rightSideTokens= rightSide.getTokens();

      final List<AbstractStructuralNode> symbolString= new LinkedList<AbstractStructuralNode>();
      for (AbstractStructuralNode token : rightSideTokens) {
        symbolString.add(clusterer.getRepresentantForItem(token));
     }
      automaton.buildPTAOnSymbol(symbolString);
    }
    LOG.debug("--- AutomatonMergingStateProcessor on element:");
    LOG.debug(rules.get(0));
    LOG.debug(">>> PTA automaton:");
    LOG.debug(automaton);

    // 3.2 simplify by merging states
    final Automaton<AbstractStructuralNode> simplifiedAutomaton= automatonSimplifier.simplify(automaton, elementSymbolToString);
    LOG.debug(">>> After automaton simplifying:");
    LOG.debug(simplifiedAutomaton);

    // 3.3 convert to regexpautomaton
    final RegexpAutomaton<AbstractStructuralNode> regexpAutomaton= new RegexpAutomaton<AbstractStructuralNode>(simplifiedAutomaton);
    LOG.debug(">>> After regexpautomaton created:");
    LOG.debug(regexpAutomaton);
    final Regexp<AbstractStructuralNode> regexp= regexpAutomatonSimplifier.simplify(regexpAutomaton, regexpAbstractToString);
    LOG.debug(">>> And the regexp is:");
    LOG.debug(regexp);
    LOG.debug("--- End");

    // 3.4 return element with regexp
    return new Element(
          new ArrayList<String>(),
          rules.get(0).getName(),
          new HashMap<String, Object>(),
          regexp,
          new ArrayList<Attribute>()
          );
  }
}
