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

import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import java.util.List;

/**
 * Interface of a RepairedXMLGenerator module.
 *
 * <p>Repaired XML Generator is the last module in the repair process. Its task is
 * to transform the repaired XML tree into a textual XML representation. 
 * After the XML is created, it is sent to the RepairRunner module
 * via the callback.</p>
 * 
 * @author sviro
 */
public interface RepairedXMLGenerator {
  
  /**
   * Start the XML export process. This method is called by the RepairRunner module
   * as the last stage of repair.
   * 
   * @param repairedTrees Repaired XML trees to be exported as XML data.
   * @param callback A callback object. After the XML data is created, it must be
   *  returned to the caller by invoking the <code>finished()</code>
   *  method on this object.
   * @throws InterruptedException 
   */
  void start(final List<RXMLTree> repairedTrees, final RepairedXMLGeneratorCallback callback) throws InterruptedException;
}
