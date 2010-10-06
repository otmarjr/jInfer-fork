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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.objects.Attribute;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.KHContextMergeConditionTester;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomatonSimplifierStateRemoval;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.GreedyAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.MergeCondidionTester;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
public class ClusterProcessorAutomatonMergingState implements ClusterProcessor<AbstractStructuralNode> {
  private static final Logger LOG = Logger.getLogger(ClusterProcessorAutomatonMergingState.class);

  @Override
  public AbstractStructuralNode processCluster(final Clusterer<AbstractStructuralNode> clusterer, final Cluster<AbstractStructuralNode> cluster) throws InterruptedException {
    // 3.1 construct PTA
    final Automaton<AbstractStructuralNode> automaton = new Automaton<AbstractStructuralNode>(true);

    for (AbstractStructuralNode instance : cluster.getMembers()) {
      final Element element = (Element) instance;
      final Regexp<AbstractStructuralNode> rightSide= element.getSubnodes();

      if (!rightSide.isConcatenation()) {
        if (rightSide.isLambda()) {
          continue;
        }
        throw new IllegalArgumentException("Right side of rule at element: " + element.toString() + " is not a concatenation regexp. It is " + element.toString());
      }

      final List<AbstractStructuralNode> rightSideTokens= rightSide.getTokens();

      final List<AbstractStructuralNode> symbolString= new LinkedList<AbstractStructuralNode>();
      for (AbstractStructuralNode token : rightSideTokens) {
        symbolString.add(clusterer.getRepresentantForItem(token) );
     }
      automaton.buildPTAOnSymbol(symbolString);
    }

    LOG.debug("--- AutomatonMergingStateProcessor on element:");
    LOG.debug(cluster.getRepresentant());
    LOG.debug(">>> PTA automaton:");
    LOG.debug(automaton);
    LOG.debug("AUTO EDITOR: " + new AutoEditor<AbstractStructuralNode>().drawAutomatonToPickTwoStates(automaton));

    // 3.2 simplify by merging states
    final AutomatonSimplifier<AbstractStructuralNode> automatonSimplifier= new GreedyAutomatonSimplifier<AbstractStructuralNode>();
    final List<MergeCondidionTester<AbstractStructuralNode>> l= new ArrayList<MergeCondidionTester<AbstractStructuralNode>>();
    l.add(new KHContextMergeConditionTester<AbstractStructuralNode>(2, 1));
    final Automaton<AbstractStructuralNode> simplifiedAutomaton= automatonSimplifier.simplify(automaton,  l);
    LOG.debug(">>> After 2,1-context:");
    LOG.debug(simplifiedAutomaton);
//    AutoEditor.drawAutomaton(simplifiedAutomaton);

    // 3.3 convert to regexpautomaton
    final RegexpAutomaton<AbstractStructuralNode> regexpAutomaton= new RegexpAutomaton<AbstractStructuralNode>(simplifiedAutomaton);
    LOG.debug(">>> After regexpautomaton created:");
    LOG.debug(regexpAutomaton);
//    AutoEditor.drawAutomaton(regexpAutomaton);
    final RegexpAutomatonSimplifier<AbstractStructuralNode> regexpAutomatonSimplifier= new RegexpAutomatonSimplifierStateRemoval<AbstractStructuralNode>();
    final Regexp<AbstractStructuralNode> regexp= regexpAutomatonSimplifier.simplify(regexpAutomaton);
    LOG.debug(">>> And the regexp is:");
    LOG.debug(regexp);
    LOG.debug("--- End");

    // 3.4 return element with regexp
    return new Element(
          cluster.getRepresentant().getContext(),
          cluster.getRepresentant().getName(),
          cluster.getRepresentant().getMetadata(),
          regexp,
          new ArrayList<Attribute>()
          );
  }
}
