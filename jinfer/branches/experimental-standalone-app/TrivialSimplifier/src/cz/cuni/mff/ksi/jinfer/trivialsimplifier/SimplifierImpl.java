/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.trivialsimplifier;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of Simplifier interface.
 * 
 * @author vektor
 */
public class SimplifierImpl implements Simplifier {

  @Override
  public String getModuleName() {
    return "Trivial Simplifier";
  }

  private ClusterProcessor getClusterProcessor() {
    return new CPTrie();
  }

  private Clusterer getClusterer() {
    return new NameClusterer();
  }

  @Override
  public void start(final List<AbstractNode> initialGrammar, final SimplifierCallback callback) {
    final List<Pair<AbstractNode, List<AbstractNode>>> clustered = getClusterer().cluster(initialGrammar);

    callback.finished(getClusterProcessor().processClusters(clustered));
  }

}
