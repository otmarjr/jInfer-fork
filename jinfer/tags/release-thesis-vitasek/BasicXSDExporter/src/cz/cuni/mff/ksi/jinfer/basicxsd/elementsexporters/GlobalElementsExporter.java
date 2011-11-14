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

package cz.cuni.mff.ksi.jinfer.basicxsd.elementsexporters;

import cz.cuni.mff.ksi.jinfer.basicxsd.preprocessing.PreprocessingResult;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.basicxsd.Indentator;
import cz.cuni.mff.ksi.jinfer.basicxsd.InterruptChecker;
import cz.cuni.mff.ksi.jinfer.basicxsd.utils.TypeCategory;
import cz.cuni.mff.ksi.jinfer.basicxsd.utils.TypeUtils;
import java.util.List;

/**
 * Exporter which retrieves global elements from a preprocessing result and performs
 * their exportation. Global elements are exported as global types definitions.
 * @author rio
 */
public final class GlobalElementsExporter extends AbstractElementsExporter {

  /**
   * Constructor.
   * @param preprocessingResult Result of preprocessing to retrieve global elements.
   * @param indentator Instance of {@see Indentator} to be used to indent output.
   */
  public GlobalElementsExporter(final PreprocessingResult preprocessingResult, final Indentator indentator) {
    super(preprocessingResult, indentator);
  }

  @Override
  public void run() throws InterruptedException {
    final List<Element> globalElements = preprocessingResult.getGlobalElements();
    for (Element globalElement : globalElements) {
      InterruptChecker.checkInterrupt();
      processGlobalElement(globalElement);
    }
  }

  /**
   * Defines element's type globally.
   *
   * @param element
   * @throws InterruptedException
   */
  private void processGlobalElement(final Element element) throws InterruptedException {
    InterruptChecker.checkInterrupt();

    // If element is of a built-in type don't define it.
    if (TypeUtils.isOfBuiltinType(element)) {
      return;
    }

    final TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("<xs:simpleType name=\"");
        break;
      case COMPLEX:
        indentator.indent("<xs:complexType name=\"");
        break;
      default:
        throw new IllegalStateException("Unknown or illegal enum member.");
    }
    indentator.append(typenamePrefix);
    indentator.append(element.getName());
    indentator.append(typenamePostfix);
    indentator.append("\"");

    if (TypeUtils.hasMixedContent(element)) {
      indentator.append(" mixed=\"true\"");
    }

    indentator.append(">\n");

    indentator.increaseIndentation();

    processElementContent(element);

    indentator.decreaseIndentation();

    switch (typeCategory) {
      case SIMPLE:
        indentator.indent("</xs:simpleType>\n");
        break;
      case COMPLEX:
        indentator.indent("</xs:complexType>\n");
        break;
      default:
        throw new IllegalStateException("Unknown of illegal enum member.");
    }

    indentator.append("\n");
  }
}
