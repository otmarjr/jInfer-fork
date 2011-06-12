/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author sviro
 */
public class InitialModel {

  private final List<FD> functionalDependencies;
  private final List<RXMLTree> trees;

  public InitialModel() {
    functionalDependencies = new ArrayList<FD>();
    trees = new ArrayList<RXMLTree>();
  }
  
  public void addFD(List<FD> fDs) {
    functionalDependencies.addAll(fDs);
  }

  public List<FD> getFunctionalDependencies() {
    return functionalDependencies;
  }

  public List<RXMLTree> getTrees() {
    return trees;
  }
  
  

  public void addXMLTree(List<XMLTree> xmlTrees) {
    trees.addAll(createRXMLTrees(xmlTrees));
  }

  private Collection<? extends RXMLTree> createRXMLTrees(List<XMLTree> xmlTrees) {
    List<RXMLTree> result = new ArrayList<RXMLTree>();
    for (XMLTree xmlTree : xmlTrees) {
      result.add(new RXMLTree(xmlTree));
    }
    
    return result;
  }
  
  public List<String> getPaths(final RXMLTree rXmlTree) {
    int treeIndex = trees.indexOf(rXmlTree);
    return trees.get(treeIndex).getXmlTree().getPaths();
  }
  
  public int getFDsCount() {
    return functionalDependencies.size();
  }
  
  public int getTreesCount() {
    return trees.size();
  }
}
