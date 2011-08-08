/*
 * Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces;

import cz.cuni.mff.ksi.jinfer.base.objects.Input;

/**
 * Interface of an initial model Generator module.
 *
 * <p>Initial model Generator is the first module in repairing process. Its task
 * is to create a tree representation the input files, and  representation of
 * functional dependencies.
 * After the model is retreived, it is sent to the next inference stages
 * via the callback.</p>
 * @author sviro
 */
public interface ModelGenerator {
  
  /**
   * Start the initial model generation process. This method is called by the RepairRunner
   * as the first stage of repair.
   *
   * @param input Input data. Implementation of ModelGenerator should retrieve the
   *  representation of XML tree and functional dependencies from this input alone.
   * @param callback A callback object. After the initial model is retrieved, it
   *  must be sent to the next stages by calling the <code>finished()</code>
   *  method of this object.
   * @throws InterruptedException
   */
  void start(final Input input, final ModelGeneratorCallback callback) throws InterruptedException;
  
}
