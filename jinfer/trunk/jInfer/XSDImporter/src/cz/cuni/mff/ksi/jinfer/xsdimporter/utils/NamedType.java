/*
 *  Copyright (C) 2010 reseto
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
package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reseto
 */
public class NamedType {

  private Element container;
  private List<Element> rules;
  private boolean alreadyCopied = false;

  public NamedType() {
    this.container = null;
    this.rules = new ArrayList<Element>(0);
  }

  public boolean isAlreadyCopied() {
    return alreadyCopied;
  }

  public void setAlreadyCopied() {
    this.alreadyCopied = true;
  }

  public Element getContainer() {
    return container;
  }

  public void setContainer(Element container) {
    this.container = container;
  }

  public List<Element> getRules() {
    return rules;
  }

  public void setRules(List<Element> rules) {
    this.rules = rules;
  }
}
