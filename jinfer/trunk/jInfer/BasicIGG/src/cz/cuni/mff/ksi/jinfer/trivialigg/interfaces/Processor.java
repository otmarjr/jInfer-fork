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
package cz.cuni.mff.ksi.jinfer.trivialigg.interfaces;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import java.io.InputStream;
import java.util.List;

/**
 * Interface of any class that can take an InputStream and produce IG rules from it.
 * 
 * @author vektor
 */
public interface Processor {

  /**
   * Returns the rules contained in the input.
   * 
   * @param s Input of any arbitrary type.
   * @return Rules contained within. Empty list if there are no rules or an error occurs.
   */
  List<AbstractNode> process(final InputStream s);
}
