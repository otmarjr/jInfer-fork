/*
 * Copyright (C) 2012 rio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.base.interfaces.inference;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface defining a response to NonGrammaticalInputProcessor finishing its
 * work.
 * 
 * @author rio
 */
public interface NonGrammaticalInputProcessorCallback {
  
  /**
   * This method is called by a NonGrammaticalInputProcessor implementation,
   * after it has finished its work.
   * 
   * @param grammar Merged simplified grammar.
   */
  void finished(final List<Element> grammar);
}
