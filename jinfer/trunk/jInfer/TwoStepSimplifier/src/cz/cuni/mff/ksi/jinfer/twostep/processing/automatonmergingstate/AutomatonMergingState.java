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
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.defectivemdl.DefectiveMDLFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Extensible implementation of merging state algorithm.
 *
 * Class providing method for inferring DTD for single element. In this implementation
 * nondeterministic finite automaton is used.
 * <p>
 * First prefix-tree automaton is constructed using cluster members as positive
 * examples. Then, given {@link AutomatonSimplifier}, merging of states occurs.
 * <p>
 * After that, automaton is passed to {@link RegexpAutomatonSimplifier} to obtain
 * regular expression from it.
 * 
 * @author anti
 */
public class AutomatonMergingState implements ClusterProcessor<AbstractStructuralNode> {

  private static final Logger LOG = Logger.getLogger(AutomatonMergingState.class);
  private final AutomatonSimplifier<AbstractStructuralNode> automatonSimplifier;
  private final RegexpAutomatonSimplifier<AbstractStructuralNode> regexpAutomatonSimplifier;
  private final AutomatonSimplifier<AbstractStructuralNode> defective = (new DefectiveMDLFactory()).<AbstractStructuralNode>create();

  private class AbstractStructuralNodeSymbolToString implements SymbolToString<AbstractStructuralNode> {

    @Override
    public String toString(final AbstractStructuralNode symbol) {
      return symbol.isElement() ? symbol.getName() : "#CDATA";
    }
  };

  private class RegexpAbstractSymbolToString implements SymbolToString<Regexp<AbstractStructuralNode>> {

    private final AbstractStructuralNodeSymbolToString g = new AbstractStructuralNodeSymbolToString();

    @Override
    public String toString(final Regexp<AbstractStructuralNode> symbol) {
      switch (symbol.getType()) {
        case LAMBDA:
          return symbol.toString();
        case TOKEN:
          return g.toString(symbol.getContent());
        case CONCATENATION:
          return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(symbol.getChildren(), ",",
                  new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {

                    @Override
                    public String toString(final Regexp<AbstractStructuralNode> t) {
                      return RegexpAbstractSymbolToString.this.toString(t);
                    }
                  });
        case ALTERNATION:
          return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(symbol.getChildren(), "|",
                  new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {

                    @Override
                    public String toString(final Regexp<AbstractStructuralNode> t) {
                      return RegexpAbstractSymbolToString.this.toString(t);
                    }
                  });
        case PERMUTATION:
          return CollectionToString.<Regexp<AbstractStructuralNode>>colToString(symbol.getChildren(), "&",
                  new CollectionToString.ToString<Regexp<AbstractStructuralNode>>() {

                    @Override
                    public String toString(final Regexp<AbstractStructuralNode> t) {
                      return RegexpAbstractSymbolToString.this.toString(t);
                    }
                  });
      }
      return symbol.toString();
    }
  };
  private final AbstractStructuralNodeSymbolToString elementSymbolToString;
  private final RegexpAbstractSymbolToString regexpAbstractToString;

  /**
   * Construct and set all submodule factories.
   *
   * @param automatonSimplifierFactory factory of {@link AutomatonSimplifier} submodule.
   * @param regexpAutomatonSimplifierFactory factory of {@link RegexpAutomatonSimplifier} submodule.
   */
  public AutomatonMergingState(
          final AutomatonSimplifierFactory automatonSimplifierFactory,
          final RegexpAutomatonSimplifierFactory regexpAutomatonSimplifierFactory) {
    this.automatonSimplifier = automatonSimplifierFactory.<AbstractStructuralNode>create();
    this.regexpAutomatonSimplifier = regexpAutomatonSimplifierFactory.<AbstractStructuralNode>create();
    this.elementSymbolToString = new AbstractStructuralNodeSymbolToString();
    this.regexpAbstractToString = new RegexpAbstractSymbolToString();
  }

  private Regexp<AbstractStructuralNode> convertRegexpToRepresentant(Clusterer<AbstractStructuralNode> clusterer, Regexp<AbstractStructuralNode> regexp) {
    switch (regexp.getType()) {
      case LAMBDA:
        throw new IllegalArgumentException("Lambda lambada");
      case TOKEN:
        return Regexp.<AbstractStructuralNode>getToken(clusterer.getRepresentantForItem(regexp.getContent()), regexp.getInterval());
      case CONCATENATION:
      case ALTERNATION:
        List<Regexp<AbstractStructuralNode>> newCh = new ArrayList<Regexp<AbstractStructuralNode>>(regexp.getChildren().size());
        for (Regexp<AbstractStructuralNode> ch : regexp.getChildren()) {
          newCh.add(convertRegexpToRepresentant(clusterer, ch));
        }
        return new Regexp<AbstractStructuralNode>(null,
                newCh,
                regexp.getType(), regexp.getInterval());
      case PERMUTATION:
        throw new IllegalArgumentException("Can't handle permutation input.");
      default:
        throw new IllegalArgumentException("Unknown regexp type.");
    }
  }

  @Override
  public AbstractStructuralNode processCluster(
          final Clusterer<AbstractStructuralNode> clusterer,
          final List<AbstractStructuralNode> rules) throws InterruptedException {
    if (rules.isEmpty()) {
      throw new IllegalArgumentException("Rules has to be non-empty.");
    }
    // 3.1 construct PTA
    final Automaton<AbstractStructuralNode> automaton = new Automaton<AbstractStructuralNode>(true);

    List<List<AbstractStructuralNode>> inputStrings = new LinkedList<List<AbstractStructuralNode>>();
    for (final AbstractStructuralNode instance : rules) {
      if (instance.getMetadata().containsKey(IGGUtils.IS_SENTINEL)) {
        continue;
      }
      final Element element = (Element) instance;
      final Regexp<AbstractStructuralNode> rightSide = element.getSubnodes();

      if (element.getMetadata().containsKey("from.schema")) {
        Regexp<AbstractStructuralNode> representantRegexp = convertRegexpToRepresentant(clusterer, rightSide);
        automaton.buildPTAOnRegexp(representantRegexp);
      } else {
        if (!rightSide.isConcatenation()) {
          throw new IllegalArgumentException("Right side of rule at element: " + element.toString() + " is not a concatenation regexp. It is " + element.toString());
        }

        final List<AbstractStructuralNode> rightSideTokens = rightSide.getTokens();

        final List<AbstractStructuralNode> symbolString = new LinkedList<AbstractStructuralNode>();
        for (AbstractStructuralNode token : rightSideTokens) {
          symbolString.add(clusterer.getRepresentantForItem(token));
        }
        automaton.buildPTAOnSymbol(symbolString);
        inputStrings.add(symbolString);
      }
    }
    LOG.debug("--- AutomatonMergingStateProcessor on element:");
    LOG.debug(rules.get(0));
    LOG.debug(">>> PTA automaton:");
    LOG.debug(automaton);

    // 3.2 simplify by merging states
    Automaton<AbstractStructuralNode> simplifiedAutomaton = automatonSimplifier.simplify(automaton, elementSymbolToString, clusterer.getRepresentantForItem(rules.get(0)).getName(), inputStrings);
    LOG.debug(">>> After automaton simplifying:");
    LOG.debug(simplifiedAutomaton);

    // 3.3 convert to regexpautomaton
    final RegexpAutomaton<AbstractStructuralNode> regexpAutomaton = new RegexpAutomaton<AbstractStructuralNode>(simplifiedAutomaton);
    LOG.debug(">>> After regexpautomaton created:");
    LOG.debug(regexpAutomaton);
    final Regexp<AbstractStructuralNode> regexp = regexpAutomatonSimplifier.simplify(regexpAutomaton, regexpAbstractToString);
    LOG.debug(">>> And the regexp is:");
    LOG.debug(regexp);
    LOG.debug("--- End");

    // 3.4 return element with regexp
    return new Element(
            new ArrayList<String>(),
            rules.get(0).getName(),
            new HashMap<String, Object>(),
            regexp,
            new ArrayList<Attribute>());
  }
}
