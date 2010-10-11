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
package cz.cuni.mff.ksi.jinfer.crudemdl.processing.trie;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author vektor
 */
public class ClusterProcessorTrieFactory implements ClusterProcessorFactory {

  public static final String NAME = "ClusterProcessorTrie";

  @Override
  public ClusterProcessor<AbstractStructuralNode> create() {
    return new ClusterProcessorTrie();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return NAME;
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayModuleDescription() {
    return "Processes the input cluster by creating a prefix tree (trie) of all "
            + "the right sides of input rules.";
  }
}
