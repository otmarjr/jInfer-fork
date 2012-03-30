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

package cz.cuni.mff.ksi.jinfer.basicxsd.preprocessing;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.utils.TopologicalSort;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.log4j.Logger;

/**
 * Provides preprocessing of IG grammar.
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
  private final boolean generateGlobal;
  /// Number of occurrences of element to consider it as a global type.
  private final int numberToGlobal;


  /**
   * Constructs an instance and performs preprocessing without determination
   * of global element types. Results can be retrieved by public methods.
   *
   * @param elements A non empty list of elements.
   */
  public Preprocessor(final List<Element> elements) throws InterruptedException {
    this(elements, Integer.MIN_VALUE);
  }

  /**
   * Constructs an instance and performs preprocessing with determination
   * of global element types. Result can be retrieved by a public method.
   *
   * @param elements Input grammar - a non empty list of elements.
   * @param numberToGlobal Number of occurrences of an element to consider its type
   * as a global type. Passing of a non positive integer means no generation of global types.
   */
  public Preprocessor(final List<Element> elements, final int numberToGlobal)
      throws InterruptedException {
    if (elements.isEmpty()) {
      throw new IllegalStateException("An empty grammar was passed in.");
    }
    this.originalElements = elements;
    this.generateGlobal = numberToGlobal > 0 ? true : false;
    this.numberToGlobal = numberToGlobal;
    run();
  }

  /**
   * Need to be called before calling of any other public method.
   */
  private void run() throws InterruptedException {
    assert (!originalElements.isEmpty());

    // sort elements topologically
    final TopologicalSort ts = new TopologicalSort(originalElements);
    final List<Element> toposortedElementsWithUnused = ts.sort();

    // count occurrences of elements
    final Map<String, Integer> elementOccurrenceCounts = countOccurrences(toposortedElementsWithUnused);

    toposortedElements = removeUnused(toposortedElementsWithUnused, elementOccurrenceCounts);
    // presence of any unused elements means there is more than one top level element
    if (!toposortedElements.equals(toposortedElementsWithUnused)) {
      LOG.warn("XSD Exporter: Grammar contains more top level elements, proper Schema cannot be inferred.");
    }

    // decide which elements will be global
    globalElementFlags = makeGlobalFlags(toposortedElements, elementOccurrenceCounts);
  }

  /**
   * Retrieves result of performed preprocessing.
   * @return Result of preprocessing.
   */
  public PreprocessingResult getResult() {
    return new PreprocessingResult(originalElements, toposortedElements, globalElementFlags);
  }

  /**
   * Returns an element reference by its name.
   *
   * @param elementName Name of a desired element.
   * @return Valid reference or <code>null</code> if the element is not present in the grammar.
   */
  private Element getElementByName(final String elementName) {
    for (Element element : originalElements) {
      if (element.getName().equalsIgnoreCase(elementName)) {
        return element;
      }
    }
    return null;
  }

  /** Counts numbers of occurrences of given elements in IG.
   *
   * @param toposortedElements topologically sorted list of elements
   * @return counts of occurrences mapped to names of elements
   */
  private Map<String, Integer> countOccurrences(final List<Element> toposortedElements) {
    final Map<String, Integer> occurrenceCounts = new HashMap<String, Integer>();
    final Stack<Element> recursionStack = new Stack<Element>();

    // initialize counts to 0s
    for (Element e : toposortedElements) {
      occurrenceCounts.put(e.getName(), 1);
    }

    // run recursion from the top element
    final Element topElement = toposortedElements.get(toposortedElements.size() - 1);
    countOccurrencesRecursion(toposortedElements, occurrenceCounts, topElement, recursionStack);

    return occurrenceCounts;
  }

  /** Counts numbers of occurrences of given elements in IG.
   *
   * @param elements topologically sorted elements
   * @param occurrenceCounts output parameter - holds counts of occurrences, at start it must be initialized to 0s
   * @param root element to start recursion at
   * @param recursionStack stack of elements as they are processed, helps avoid infinite recursion
   */
  private void countOccurrencesRecursion(final List<Element> elements, final Map<String, Integer> occurrenceCounts, final Element root, final Stack<Element> recursionStack) {
    for (Element element : recursionStack) {
      if (element == root) {
        // Cycle detected. It indicates recursive element, so the element's occurrence is set to maxint.
        occurrenceCounts.put(root.getName(), Integer.MAX_VALUE);
        return;
      }
    }
    recursionStack.push(root);

    // Increase occurence count of root element. In case, it equals MAX_VALUE (recursive element),
    // do not increase because of overflow.
    final String rootName = root.getName();
    final int rootCount = occurrenceCounts.get(rootName);
    if (rootCount < Integer.MAX_VALUE) {
      occurrenceCounts.put(root.getName(), occurrenceCounts.get(root.getName()) + 1);
    }

    for (AbstractStructuralNode node : root.getSubnodes().getTokens()) {
      if (node.getType().equals(StructuralNodeType.ELEMENT)) {
        countOccurrencesRecursion(elements, occurrenceCounts, getElementByName(node.getName()), recursionStack);
      }
    }

    recursionStack.pop();
  }

  /**
   * From a given list of elements and map of their occurrence counts creates
   * a new list of elements. The new list holds only elements with occurrence
   * count at least one.
   * Order of elements is retained -> retains topological order.
   *
   * @param elements List of elements that can contains unused elements.
   * @param occurrenceCounts Occurrence counts of elements in IG.
   * @return List of elements which occurrence count is at least one.
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
      if (generateGlobal) {
        final boolean isGlobal = (occurrenceCounts.get(element.getName()) >= numberToGlobal) ? true : false;
        globalFlags.put(element.getName(), isGlobal);
      } else {
        globalFlags.put(element.getName(), false);
      }
    }

    return globalFlags;
  }
}
