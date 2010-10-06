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
package cz.cuni.mff.ksi.jinfer.base.objects;

import java.util.List;

/**
 * Representation of a cluster of rules.
 *  
 * @author vektor
 */
public class Cluster {

  /** A rule that represents this cluster. Should be also contained in content. */
  private final AbstractStructuralNode representant;
  
  /** Rules in this cluster. */
  private final List<AbstractStructuralNode> content;

  public Cluster(final AbstractStructuralNode representant, final List<AbstractStructuralNode> content) {
    this.representant = representant;
    this.content = content;
  }

  public AbstractStructuralNode getRepresentant() {
    return representant;
  }

  public List<AbstractStructuralNode> getContent() {
    return content;
  }

}
