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
import java.util.List;

/**
 * Model for initial data needed for repairing. 
 * This model contains list of {@link RXMLTree} and functional dependencies.
 * @author sviro
 */
public class InitialModel {

  private final List<FD> functionalDependencies;
  private final List<RXMLTree> trees;

  /**
   * Default constructor of initial model.
   */
  public InitialModel() {
    functionalDependencies = new ArrayList<FD>();
    trees = new ArrayList<RXMLTree>();
  }
  
  /**
   * Add list of functional dependencies into the model.
   * @param fDs List to be added to initial model.
   */
  public void addFD(final List<FD> fDs) {
    functionalDependencies.addAll(fDs);
  }

  /**
   * Get list of functional dependencies contained in this model.
   * @return List of functional dependencies.
   */
  public List<FD> getFunctionalDependencies() {
    return functionalDependencies;
  }

  /**
   * Get list of trees contained in this model.
   * @return List of trees.
   */
  public List<RXMLTree> getTrees() {
    return trees;
  }

  /**
   * Add list of {@link RXMLTree} into the model.
   * @param xmlTrees List of trees to be added to the model.
   */
  public void addTree(final List<RXMLTree> xmlTrees) {
    trees.addAll(xmlTrees);
  }
  
  /**
   * Get number of functional dependencies contained in this model.
   * @return Number of functional dependencies.
   */
  public int getFDsCount() {
    return functionalDependencies.size();
  }
  
  /**
   * Get number of {@link RXMLTree} contained in this model.
   * @return Number of {@link RXMLTree}.
   */
  public int getTreesCount() {
    return trees.size();
  }
}