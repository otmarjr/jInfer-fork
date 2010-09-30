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

package cz.cuni.mff.ksi.jinfer.crudemdl.implementations.clusterprocessing.automatonmergingstate;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.KHContextMergeConditionTester;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.crudemdl.clusterprocessing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomatonSimplifierStateRemoval;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.GreedyAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.MergeCondidionTester;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Class providing method for inferring DTD for single element. In this implementation
 * deterministic finite automaton is used.
 * First Prefix-Tree automaton is constructed using cluster.members() as positive
 * examples. Then, given KHContextMergeConditionTester, merging of states occurs
 * until there are no more states to merge. Currently k=2, h=1, so
 * producing 2,1-context automaton by merging.
 *
 * @author anti
 */
public class ClusterProcessorAutomatonMergingState implements ClusterProcessor<AbstractNode> {
  private static final Logger LOG = Logger.getLogger(ClusterProcessorAutomatonMergingState.class);

  @Override
  public AbstractNode processCluster(final Clusterer<AbstractNode> clusterer, final Cluster<AbstractNode> cluster) throws InterruptedException {
    // 3.1 construct PTA
    final Automaton<AbstractNode> automaton = new Automaton<AbstractNode>(true);

    final Map<AbstractNode, Integer> attributesMap= new HashMap<AbstractNode, Integer>();
    for (AbstractNode instance : cluster.getMembers()) {
      final Element element = (Element) instance;
      final Regexp<AbstractNode> rightSide= element.getSubnodes();

      if (!rightSide.isConcatenation()) {
        throw new IllegalArgumentException("Right side of rule at element: " + element.toString() + " is not a concatenation regexp.");
      }

      final List<AbstractNode> rightSideTokens= rightSide.getTokens();

      final List<AbstractNode> symbolString= new LinkedList<AbstractNode>();
      for (AbstractNode token : rightSideTokens) {
        if (token.isAttribute()) {
          Attribute tokenA= (Attribute) token;
          AbstractNode attRepresentant= clusterer.getRepresentantForItem(tokenA);
          if (attributesMap.containsKey(attRepresentant)) {
            attributesMap.put(attRepresentant, attributesMap.get(attRepresentant) + 1);
            Attribute attRepresentantA = (Attribute) attRepresentant;
            attRepresentantA.getContent().addAll(tokenA.getContent());
          } else {
            attributesMap.put(attRepresentant, Integer.valueOf(1));
          }
        } else {
          symbolString.add( clusterer.getRepresentantForItem(token) );
        }
      }
      automaton.buildPTAOnSymbol(symbolString);
    }

    for (AbstractNode attribute : attributesMap.keySet()) {
      if (attributesMap.get(attribute) < cluster.getMembers().size()) {
        attribute.getMetadata().remove("required");
      }
    }

    LOG.debug("--- AutomatonMergingStateProcessor on element:");
    LOG.debug(cluster.getRepresentant());
    LOG.debug(">>> PTA automaton:");
    LOG.debug(automaton);
    AutoEditor.drawAutomatonAsync(automaton);

    // 3.2 simplify by merging states
    final AutomatonSimplifier<AbstractNode> automatonSimplifier= new GreedyAutomatonSimplifier<AbstractNode>();
    final List<MergeCondidionTester<AbstractNode>> l= new ArrayList<MergeCondidionTester<AbstractNode>>();
    l.add(new KHContextMergeConditionTester<AbstractNode>(2, 1));
    final Automaton<AbstractNode> simplifiedAutomaton= automatonSimplifier.simplify(automaton,  l);
    LOG.debug(">>> After 2,1-context:");
    LOG.debug(simplifiedAutomaton);
//    AutoEditor.drawAutomatonAsync(simplifiedAutomaton);

    // 3.3 convert to regexpautomaton
    final RegexpAutomaton<AbstractNode> regexpAutomaton= new RegexpAutomaton<AbstractNode>(simplifiedAutomaton);
    LOG.debug(">>> After regexpautomaton created:");
    LOG.debug(regexpAutomaton);
//    AutoEditor.drawAutomatonAsync(regexpAutomaton);
    final RegexpAutomatonSimplifier<AbstractNode> regexpAutomatonSimplifier= new RegexpAutomatonSimplifierStateRemoval<AbstractNode>();
    final Regexp<AbstractNode> regexp= regexpAutomatonSimplifier.simplify(regexpAutomaton);
    LOG.debug(">>> And the regexp is:");
    LOG.debug(regexp);
    LOG.debug("--- End");

    List<Regexp<AbstractNode>> attTokens= new ArrayList<Regexp<AbstractNode>>();
    for (AbstractNode att : attributesMap.keySet()) {
      attTokens.add(
              Regexp.<AbstractNode>getToken(att)
              );
    }

    final Regexp<AbstractNode> regexpAttributes=
            Regexp.<AbstractNode>getConcatenation( attTokens );
    LOG.debug(">>> Attributes regexp is:");
    LOG.debug(regexpAttributes);

    attTokens.add(regexp);

    // 3.4 return element with regexp
    return /*(new Shortener()).simplify(*/ new Element(
          cluster.getRepresentant().getContext(),
          cluster.getRepresentant().getName(),
          cluster.getRepresentant().getMetadata(),
          Regexp.<AbstractNode>getConcatenation( attTokens )
          );//);
  }
}
