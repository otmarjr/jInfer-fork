/*
 *  Copyright (C) 2011 rio
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

package cz.cuni.mff.ksi.jinfer.basicxsd.preprocessing;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a result of preprocessing.
 * @author rio
 */
public final class PreprocessingResult {

  private final List<Element> originalElements;
  private final List<Element> toposortedElements;
  private final Map<String, Boolean> globalElementFlags;

  PreprocessingResult(final List<Element> originalElements, final List<Element> toposortedElements, final Map<String, Boolean> globalElementFlags) {
    this.originalElements = originalElements;
    this.toposortedElements = toposortedElements;
    this.globalElementFlags = globalElementFlags;
  }

  /**
   * Returns an element reference by its name.
   * @param elementName Name of a desired element.
   * @return Valid reference or <code>null</code> if the element is not present in the grammar.
   */
  public Element getElementByName(final String elementName) {
    for (Element element : originalElements) {
      if (element.getName().equalsIgnoreCase(elementName)) {
        return element;
      }
    }
    return null;
  }

  /**
   * Decides whether a given element's type can be considered a global type.
   * @param elementName Name of an element.
   */
  public boolean isElementGlobal(final String elementName) {
    return globalElementFlags.get(elementName).booleanValue();
  }

  /**
   * Returns a list of global elements. Global elements are those which types
   * have been considered global types.
   * @return Global elements.
   */
  public List<Element> getGlobalElements() {
    final List<Element> globalElements = new LinkedList<Element>();
    for (Element element : toposortedElements) {
      if (isElementGlobal(element.getName())) {
        globalElements.add(element);
      }
    }
    return globalElements;
  }

  /**
   * Returns the root element.
   * @return Root element.
   */
  public Element getRootElement() {
    return toposortedElements.get(toposortedElements.size() - 1);
  }
}
