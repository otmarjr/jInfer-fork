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

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.mergecondition.KHContextMergeConditionTester;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.mergecondition.MergeCondidionTester;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class AutomatonMergingStateProcessor implements ElementProcessor<AbstractNode>{
  private static final Logger LOG = Logger.getLogger(AutomatonMergingStateProcessor.class);

  @Override
  public AbstractNode processElement(final Clusterer<AbstractNode> clusterer, final Cluster<AbstractNode> cluster) throws InterruptedException {
    // 3.1 construct PTA
    final Automaton<AbstractNode> automaton = new Automaton<AbstractNode>(true);

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
          continue;
        }
        symbolString.add( clusterer.getRepresentantForItem(token) );
      }
      automaton.buildPTAOnSymbol(symbolString);
    }
    LOG.debug("--- AutomatonMergingStateProcessor on element:");
    LOG.debug(cluster.getRepresentant());
    LOG.debug(">>> PTA automaton:");
    LOG.debug(automaton);

    // 3.2 simplify by merging states
    automaton.simplify( new KHContextMergeConditionTester<AbstractNode>(2, 1) );
    LOG.debug(">>> After 2-context:");
    LOG.debug(automaton);

    // 3.3 convert to regexpautomaton
    final RegexpAutomaton regexpAutomaton= new RegexpAutomaton(automaton);
    LOG.debug(">>> After regexpautomaton created:");
    LOG.debug(regexpAutomaton);
    regexpAutomaton.makeRegexpForm();
    LOG.debug(">>> After regexp automaton conversion:");
    LOG.debug(regexpAutomaton);
    LOG.debug(">>> And the regexp is:");
    LOG.debug(regexpAutomaton.getRegexp());
    LOG.debug("--- End");
    
    // 3.4 return element with regexp
    return new Element(
          cluster.getRepresentant().getContext(),
          cluster.getRepresentant().getName(),
          cluster.getRepresentant().getAttributes(),
          regexpAutomaton.getRegexp()
           );
  }
}
