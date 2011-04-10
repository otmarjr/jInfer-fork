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

package cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import java.io.InputStream;
import java.util.List;

/**
 * Interface providing common API for XSD importers.
 * @author reseto
 */
public interface XSDParser extends NamedModule {

  /**
   * Parses input Schema and returns the rules extracted from it.
   * @param stream Stream containing XSDSchema document.
   * @return Rules contained within. Empty list if there are no rules or an error occurs.
   * @throws XSDException When parsing error occurs.
   * @throws InterruptedException When user cancels the operation.
   */
  List<Element> parse(final InputStream stream) throws XSDException, InterruptedException;
  
}
