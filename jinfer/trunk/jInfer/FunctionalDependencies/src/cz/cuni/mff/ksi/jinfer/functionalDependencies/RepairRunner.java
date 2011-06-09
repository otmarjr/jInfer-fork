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
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import org.apache.log4j.Logger;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import static cz.cuni.mff.ksi.jinfer.base.utils.AsynchronousUtils.runAsync;

/**
 *
 * @author sviro
 */
public class RepairRunner {
  
  private static final Logger LOG = Logger.getLogger(RepairRunner.class);
  
  public void run() {
    runAsync(new Runnable() {

      @Override
      public void run() {
        LOG.info("Repair successfully started and finished");
        RunningProject.removeActiveProject();
      }
    }, "Retreiving XML Tree");
  }
  
}
