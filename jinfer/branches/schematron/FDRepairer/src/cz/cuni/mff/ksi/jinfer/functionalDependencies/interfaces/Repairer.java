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

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.InitialModel;

/**
 * Interface of a Repairer module.
 *
 * <p>Repairer is the second, middle module in the repair process. It
 * receives a initial model and should output a repaired XML tree.</p>
 * 
 * @author sviro
 */
public interface Repairer extends NamedModule {
  
  /**
   * Start the initial model repairing process. This method is called by the
   * RepairRunner module as the second stage of repair.
   * 
   * @param model Initial model to be repaired.
   * @param callback A callback object. When the initial model is repaired,
   *  the resulting XML tree must be sent to the last stage by calling
   *  the <code>finished()</code> method of this object.
   * @throws InterruptedException 
   */
  void start(final InitialModel model, RepairerCallback callback) throws InterruptedException;
}
