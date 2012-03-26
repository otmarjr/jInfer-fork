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

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface of a NonGrammaticalInputProcessor module.
 *
 * <p>NonGrammaticalInputProcessor is the third module in the inference process.
 * It receives the simplified grammar and input files. Non-grammatical
 * information and statements are extracted from the input files, and then,
 * they are merged to the grammar. Modifications of the grammar rules according
 * to the extracted information are also possible.</p>
 * 
 * @author rio
 */
public interface NonGrammaticalInputProcessor extends NamedModule, Capabilities {
  
  /**
   * Start the non-grammatical input processor. This method is called by the
   * Runner module as the third stage of inference.
   * 
   * @param input Input data. Implementation of NonGrammaticalInputProcessor
   *  may retrieve a non-grammatical information from the input data.
   * @param grammar Simplified grammar to be merged with the extracted information.
   * @param callback A callback object. When the grammar is merged, it is sent
   *  to the last stage by calling the <code>finished()</code> method of this 
   *  object.
   */
  void start(final Input input, final List<Element> grammar,
          final NonGrammaticalInputProcessorCallback callback) throws InterruptedException;
}
