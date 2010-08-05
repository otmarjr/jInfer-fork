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
import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/** TODO translate
 * Preprocessing vstupu z IGG pred samotnym generovanim schemy. Dostane list
 * elementov, vyhodi z neho nedosiahnutelne, poznaci, ak odhali viac top level
 * elementov, rozhodne, ktore elementy by sa definovat globalne ako typy.
 *
 * Pred pouzitim verejnych metod je nutne zavolat run().
 *
 * @author rio
 */
public final class Preprocessor {
  private static final Logger LOG = Logger.getLogger(Preprocessor.class);

  /// Input list of elements.
  private final List<Element> originalElements;
  /// Topological sorted list of elements without unused elements.
  private List<Element> toposortedElements = null;
  /** For each element provides information whether it should be defined as
   * global type or not.
   */
  private Map<String, Boolean> globalElementFlags = null;

  // TODO rio nastavovat optionom?
  /// Number of occurrences of element to consider it as a global type.
  private final static int NUMBER_OF_OCCURRENCES_TO_GLOBAL = 2;

  /** Constructor
   *
   * @param elements Non empty list of elements from IGG.
   */
  public Preprocessor(final List<Element> elements) {
    assert (!elements.isEmpty());
    originalElements = elements;
  }

  /** Need to be called before calling of any other public method.
   */
  public void run() {
    assert (!originalElements.isEmpty());

    // sort elements topologically
    final TopologicalSort ts = new TopologicalSort(originalElements);
    final List<Element> toposortedElementsWithUnused = ts.sort();

    // count occurrences of elements
    final Map<String, Integer> elementOccurrenceCounts = countOccurrences(toposortedElementsWithUnused);
    // TODO rio remove debug log
    LOG.info(elementOccurrenceCounts);
    toposortedElements = removeUnused(toposortedElementsWithUnused, elementOccurrenceCounts);
    // presence of any unused elements means there is more than one top level element
    if (!toposortedElements.equals(toposortedElementsWithUnused)) {
      LOG.warn("XSD Exporter: Grammar contains more top level elements, proper Schema cannot be inferred.");
    }

    // decide which elements will be global
    globalElementFlags = makeGlobalFlags(toposortedElements, elementOccurrenceCounts);

    // TODO rio ?? make string representation of elements ??
  }

  /** Returns element by name.
   *
   * @param elementName name of element
   * @return element or null if element with specified name is not in IG
   */
  public Element getElementByName(final String elementName) {
    for (Element element : originalElements) {
      if (element.getName().equalsIgnoreCase(elementName)) {
        return element;
      }
    }
    return null;
  }

  /** Decides whether a given element should be defined as a global type.
   *
   * @param elementName name of an element
   * @return true of false
   */
  public boolean isElementGlobal(final String elementName) {
    return globalElementFlags.get(elementName).booleanValue();
  }

  /** Returns a top level element.
   *
   * @return top level element
   */
  public Element getTopElement() {
    return toposortedElements.get(toposortedElements.size() - 1);
  }

  /** Return list of elements that should be defined as global types.
   *
   * @return list of elements that should be defined as global types
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

  /** Counts numbers of occurrences of given elements in IG.
   *
   * @param toposortedElements topologically sorted list of elements
   * @return counts of occurrences mapped to names of elements
   */
  private Map<String, Integer> countOccurrences(final List<Element> toposortedElements) {
    final Map<String, Integer> occurrenceCounts = new HashMap<String, Integer>();
    
    // initialize counts to 0s
    for (Element e : toposortedElements) {
      occurrenceCounts.put(e.getName(), 0);
    }
    
    // run recursion from the top element
    final Element topElement = toposortedElements.get(toposortedElements.size() - 1);
    countOccurrencesRecursion(toposortedElements, occurrenceCounts, topElement);

    return occurrenceCounts;
  }

  /** Counts numbers of occurrences of given elements in IG.
   *
   * @param elements topologically sorted elements
   * @param occurrenceCounts output parameter - holds counts of occurrences, at start it must be initialized to 0s
   * @param root element to start recursion at
   */
  private void countOccurrencesRecursion(final List<Element> elements, final Map<String, Integer> occurrenceCounts, final Element root) {
    occurrenceCounts.put(root.getName(), occurrenceCounts.get(root.getName()) + 1);
    for (AbstractNode node : root.getSubnodes().getTokens()) {
      if (node.getType().equals(NodeType.ELEMENT)) {
        countOccurrencesRecursion(elements, occurrenceCounts, getElementByName(node.getName()));
      }
    }
  }

  /** TODO rio translate
   * Dostane list elementov a ich pocty vyskytov a vrati novy list, kde su len pouzite elementy (vyskyt > 0).
   * Zachovava poradie -> zachovava topologicke usporiadanie.
   *
   * @param elements list of elements that can contains unused elements
   * @param occurrenceCounts occurrence counts of elements in IG
   * @return list of elements which occurrence count is at least one
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

  /** Decides which elements should be defined as global types.
   *
   * @param elements list of elements
   * @param occurrenceCounts occurrence counts of elements in IG
   * @return flags indicating global definition mapped to names of elements
   */
  private Map<String, Boolean> makeGlobalFlags(final List<Element> elements, final Map<String, Integer> occurrenceCounts) {
    final Map<String, Boolean> globalFlags = new HashMap<String, Boolean>();
    
    for (Element element : elements) {
      final boolean isGlobal = (occurrenceCounts.get(element.getName()) >= NUMBER_OF_OCCURRENCES_TO_GLOBAL) ? true : false;
      globalFlags.put(element.getName(), isGlobal);
    }

    return globalFlags;
  }
}
