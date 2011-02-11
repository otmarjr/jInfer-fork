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

package cz.cuni.mff.ksi.jinfer.basicxsd;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;

/**
 * TODO rio comment
 * @author rio
 */
public final class RootElementExporter extends AbstractElementsExporter {

  private static final int ROOT_ELEMENT_MINOCCURS = 1;
  private static final int ROOT_ELEMENT_MAXOCCURS = 1;

  public RootElementExporter(final PreprocessingResult preprocessingResult, final Indentator indentator) {
    super(preprocessingResult, indentator);
  }

  @Override
  public void run() throws InterruptedException {
    processElement(preprocessingResult.getRootElement(), RegexpInterval.getBounded(ROOT_ELEMENT_MINOCCURS, ROOT_ELEMENT_MAXOCCURS));
  }
}
