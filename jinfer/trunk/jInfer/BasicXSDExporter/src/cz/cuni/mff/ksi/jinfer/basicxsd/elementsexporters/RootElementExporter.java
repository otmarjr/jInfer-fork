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
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.basicxsd.Indentator;

/**
 * Exporter which retrieves the root element from a preprocessing result and
 * performs recursive exportation. The whole element subtree is exported
 * either by defining elements "inline" or by using globally defined types.
 * Elements which will reference global types are also retrieved from the
 * result of preprocessing.
 * @author rio
 */
public final class RootElementExporter extends AbstractElementsExporter {

  private static final int ROOT_ELEMENT_MINOCCURS = 1;
  private static final int ROOT_ELEMENT_MAXOCCURS = 1;

  /**
   * Constructor.
   * @param preprocessingResult Result of preprocessing to retrieve the root elements
   *                            and global elements.
   * @param indentator Instance of {@see Indentator} to be used to indent output.
   */
  public RootElementExporter(final PreprocessingResult preprocessingResult, final Indentator indentator) {
    super(preprocessingResult, indentator);
  }

  @Override
  public void run() throws InterruptedException {
    processElement(preprocessingResult.getRootElement(), RegexpInterval.getBounded(ROOT_ELEMENT_MINOCCURS, ROOT_ELEMENT_MAXOCCURS));
  }
}
