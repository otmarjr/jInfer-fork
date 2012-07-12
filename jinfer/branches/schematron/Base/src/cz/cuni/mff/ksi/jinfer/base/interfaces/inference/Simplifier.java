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
package cz.cuni.mff.ksi.jinfer.base.interfaces.inference;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface of a Simplifier module.
 *
 * <p>Simplifier is the second module in the inference process. It
 * receives a raw list of initial grammar rules and should output a simplified
 * yet same grammar. The exact definition of "simplified" varies greatly among
 * different algorithms; "same grammar" means that the languages they describe
 * should be the same.</p>
 * 
 * @author vektor
 */
public interface Simplifier extends NamedModule, Capabilities {

  /**
   * Start the grammar simplification process. This method is called by the
   * Runner module as the second stage of inference.
   * 
   * @param initialGrammar Initial Grammar to be simplified.
   * @param callback A callback object. When the initial grammar is simplified,
   *  the resulting list of rules must be sent to the next stage by calling
   *  the <code>finished()</code> method of this object.
   */
  void start(final List<Element> initialGrammar,
          final SimplifierCallback callback) throws InterruptedException;
}
