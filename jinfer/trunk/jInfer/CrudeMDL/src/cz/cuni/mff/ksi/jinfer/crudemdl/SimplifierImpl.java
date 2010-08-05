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
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti comment
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

    for (AbstractNode node : initialGrammar) {
      if (!NodeType.ELEMENT.equals(node.getType())) {
        final StringBuilder sb = new StringBuilder("Initial grammar contains rule with ");
        sb.append(node.getType().toString());
        sb.append(" as left side.");
        throw new IllegalArgumentException(sb.toString());
      }
      if (node == null) {
        throw new IllegalArgumentException("Got null as left side in grammar.");
      }
    }

    final InameClusterer clusterer = new InameClusterer();
    clusterer.addAll(initialGrammar);
    final List<Cluster<AbstractNode>> clusters = clusterer.cluster();

    final List<AbstractNode> finalGrammar= new LinkedList<AbstractNode>();

    for (Cluster<AbstractNode> cluster : clusters) {
      if (!cluster.getRepresentant().isElement()) {
        // we deal only with elements for now, rules are generated only for elements
        continue;
      }

      // 1. construct PTA
      final Set<AbstractNode> elementInstances= cluster.getMembers();

      final Automaton<AbstractNode> automaton = new Automaton<AbstractNode>(true);
      final SimpleData universalSimpleData= new SimpleData(
              new ArrayList<String>(),
              "univ_simple",
              new HashMap<String, Object>(),
              "",
              new ArrayList<String>()
              );

      for (AbstractNode instance : elementInstances) {
        final Element element = (Element) instance;
        final Regexp<AbstractNode> rightSide= element.getSubnodes();
        if (!rightSide.isConcatenation()) {
          throw new IllegalArgumentException("Right side of rule at element: " + element.toString() + " is not a concatenation regexp.");
        }

        final List<AbstractNode> rightSideTokens= rightSide.getTokens();

        final List<AbstractNode> symbolString= new LinkedList<AbstractNode>();
        for (AbstractNode token : rightSideTokens) {
          if (token.isSimpleData()) {
            symbolString.add(universalSimpleData);
          } else {
            symbolString.add( clusterer.getRepresentantForItem(token) );
          }
        }
        automaton.buildPTAOnSymbol(symbolString);
      }
      LOG.setLevel(Level.DEBUG);
      LOG.debug("--- Simplifier on element:");
      LOG.debug(cluster.getRepresentant());
      LOG.debug(">>> PTA automaton:");
      LOG.debug(automaton);
      
      // simplify
      automaton.make21context();
      LOG.debug(">>> After 2-context:");
      LOG.debug(automaton);

      // convert to regex
      final RegexpAutomaton regexpAutomaton= new RegexpAutomaton(automaton);
      LOG.debug(">>> After regexpautomaton created:");
      LOG.debug(regexpAutomaton);
      regexpAutomaton.makeRegexpForm();
      LOG.debug(">>> After regexp automaton conversion:");
      LOG.debug(regexpAutomaton);
      LOG.debug(">>> And the regexp is:");
      LOG.debug(regexpAutomaton.getRegexp());
      LOG.debug("--- End");

      // add to list
      finalGrammar.add(
        new Element(
          cluster.getRepresentant().getContext(),
          cluster.getRepresentant().getName(),
          cluster.getRepresentant().getAttributes(),
          regexpAutomaton.getRegexp()
        )
      );
    }
    callback.finished( finalGrammar );
  }
}