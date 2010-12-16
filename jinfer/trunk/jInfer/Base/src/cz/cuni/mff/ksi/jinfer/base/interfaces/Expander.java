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
package cz.cuni.mff.ksi.jinfer.base.interfaces;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface of a (sub)module providing expansion of complex regular expressions
 * into simple concatenations. (Most simplifiers want their input in exactly
 * that format.) The class must work as a singleton, subsequent calls to
 * {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Expander#expand(java.util.List) }
 * will be on the same instance.
 *
 * @author vektor
 */
public interface Expander {

  /**
   * Expands complicated regular expressions in the provided initial grammar
   * and returns a new grammar containing only concatenations on the right hand
   * sides.
   * 
   * @param grammar Grammar to be simplified.
   * @return Simplified grammar.
   */
  List<Element> expand(final List<Element> grammar);

}
