/*
 *  Copyright (C) 2010 rio
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

package cz.cuni.mff.ksi.jinfer.trivialxsd;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author rio
 */
public final class Preprocessing {
  private static final Logger LOG = Logger.getLogger(Preprocessing.class);

  private final List<Element> originalElements;
  private List<Element> toposortedElements = null;
  private Map<String, Boolean> globalElementFlags = null;

  private final static int NUMBER_OF_OCCURRENCES_TO_GLOBAL = 2;

  public Preprocessing(final List<Element> elements) {
    originalElements = elements;
  }

  public void run() {
    assert (!originalElements.isEmpty());

    // sort elements topologically
    final TopologicalSort ts = new TopologicalSort(originalElements);
    toposortedElements = ts.sort();

    // count occurrences of elements
    //
    final Map<String, Integer> elementOccurrenceCounts = new HashMap<String, Integer>();
    
    // initialize counts to 0s
    for (Element e : toposortedElements) {
      elementOccurrenceCounts.put(e.getName(), 0);
    }

    // run recursion from the top element
    final Element topElement = toposortedElements.get(toposortedElements.size() - 1);
    countOccurrencces(toposortedElements, elementOccurrenceCounts, topElement);
    // TODO rio remove debug log
    LOG.info(elementOccurrenceCounts);
    final List<Element> occurredElements = removeUnused(toposortedElements, elementOccurrenceCounts);
    if (!occurredElements.equals(toposortedElements)) {
      LOG.warn("XSD Exporter: Grammar contains more top level elements, proper Schema cannot be inferred.");
    }
    toposortedElements = occurredElements;

    // decide which elements will be global
    // TODO rio ?? make string representation of elements ??
    globalElementFlags = makeGlobalFlags(toposortedElements, elementOccurrenceCounts);
  }

  public boolean isGlobal(final String elementName) {
    return globalElementFlags.get(elementName);
  }

  public Element getElementByName(final String elementName) {
    for (Element element : toposortedElements) {
      if (element.getName().equalsIgnoreCase(elementName)) {
        return element;
      }
    }
    return null;
  }

  public boolean isElementGlobal(final String elementName) {
    return globalElementFlags.get(elementName).booleanValue();
  }

  public Element getTopElement() {
    return toposortedElements.get(toposortedElements.size() - 1);
  }

  /** Spocita pocty pouziti elementov dostupnych zo zadaneho korena.
   *
   * @param occurrenceCounts
   * @param e
   */
  private void countOccurrencces(final List<Element> elements, final Map<String, Integer> occurrenceCounts, final Element root) {
    occurrenceCounts.put(root.getName(), occurrenceCounts.get(root.getName()) + 1);
    for (AbstractNode node : root.getSubnodes().getTokens()) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        countOccurrencces(elements, occurrenceCounts, getElementByName(node.getName()));
      }
    }
  }

  /** Dostane list elementov a ich pocty vyskytov a vrati novy list, kde su len pouzite elementy (vyskyt > 0).
   * Zachovava poradie -> zachovava topologicke usporiadanie.
   *
   * @param elements
   * @param occurrenceCounts
   * @return
   */
  private List<Element> removeUnused(final List<Element> elements, final Map<String, Integer> occurrenceCounts) {
    final List<Element> occurredElements = new LinkedList<Element>();
    for (Element element : elements) {
      final String key = element.getName();
      if (occurrenceCounts.get(key) > 0) {
        occurredElements.add(element);
      }
    }
    return occurredElements;
  }

  private Map<String, Boolean> makeGlobalFlags(final List<Element> elements, final Map<String, Integer> occurrenceCounts) {
    final Map<String, Boolean> globalFlags = new HashMap<String, Boolean>();
    
    for (Element element : elements) {
      final boolean isGlobal = (occurrenceCounts.get(element.getName()) >= NUMBER_OF_OCCURRENCES_TO_GLOBAL) ? true : false;
      globalFlags.put(element.getName(), isGlobal);
    }

    return globalFlags;
  }
}
