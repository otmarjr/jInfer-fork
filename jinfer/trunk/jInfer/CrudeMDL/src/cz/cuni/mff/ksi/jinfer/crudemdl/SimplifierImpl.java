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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

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
        LOG.info(cluster); // TODO assertion here
        continue; // we deal only with elements for now
      }

      // construct PTA
      final Set<AbstractNode> elementInstances= cluster.getMembers();

      final Automaton<AbstractNode> automaton = new Automaton<AbstractNode>();
      final SimpleData universalSimpleData= new SimpleData(new ArrayList<String>(), "PCDATA", new HashMap<String, Object>(), "", new ArrayList<String>());

      for (AbstractNode instance : elementInstances) {
        final Element element = (Element) instance;
        final Regexp<AbstractNode> rightSide= element.getSubnodes();
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
      LOG.fatal(cluster.getRepresentant());
      LOG.fatal(automaton);
      
      // simplify
      automaton.make21context();
      LOG.fatal("LAKHGFKJLHGKLAHLKG\n");
      LOG.fatal(automaton);

      // convert to regex
    //  Automaton<Regexp<AbstractNode>> regexpAutomaton= new Automaton<Regexp<AbstractNode>>();
   //   regexpAutomaton.constructFrom(automaton);

      // add to list
    }
//    callback.finished( new ArrayList<AbstractNode>(elements.values()) );
    callback.finished( new ArrayList<AbstractNode>() );
  }
}