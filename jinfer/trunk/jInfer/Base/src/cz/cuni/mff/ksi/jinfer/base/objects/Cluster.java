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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Representation of a cluster of rules.
 *  
 * @author vektor
 */
// TODO vektor remove this when noone will be using it
public class Cluster {

  /** A rule that represents this cluster. Should be also contained in content. */
  private final Element representant;
  
  /** Rules in this cluster. */
  private final List<Element> content;

  public Cluster(final Element representant, final List<Element> content) {
    this.representant = representant;
    this.content = content;
  }

  public Element getRepresentant() {
    return representant;
  }

  public List<Element> getContent() {
    return content;
  }

}
