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
import cz.cuni.mff.ksi.jinfer.base.objects.Input;

/**
 * Interface of an IGGenerator module.
 *
 * <p>Initial Grammar Generator is the first module in inference process. Its task
 * is to create a list of rules from the input files, schemas and queries.
 * After the rules are retrieved, they are sent to the next inference stages
 * via the callback.</p>
 * 
 * @author vektor
 */
public interface IGGenerator extends NamedModule, Capabilities {

  /**
   * Start the IG generation process. This method is called by the Runner module
   * as the first stage of inference.
   *
   * @param input Input data. Implementation of IGGenerator should retrieve the
   *  list of initial grammar rules from this input alone.
   * @param callback A callback object. After all the IG rules are retrieved, their
   *  list must be sent to the next stages by calling the <code>finished()</code>
   *  method of this object.
   * @throws InterruptedException
   */
  void start(final Input input, final IGGeneratorCallback callback)
          throws InterruptedException;
}
