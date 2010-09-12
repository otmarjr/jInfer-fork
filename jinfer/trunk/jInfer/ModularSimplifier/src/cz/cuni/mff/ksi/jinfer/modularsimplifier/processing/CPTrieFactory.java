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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.processing;

import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of a cluster processor factory - creates a Trie CP.
 * 
 * @author vektor
 */
@ServiceProvider(service = ClusterProcessorFactory.class)
public class CPTrieFactory implements ClusterProcessorFactory {

  @Override
  public ClusterProcessor create() {
    return new CPTrie();
  }

  @Override
  public String getModuleName() {
    return "Trie";
  }

  @Override
  public String getCommentedSchema() {
    return getModuleName();
  }
}
