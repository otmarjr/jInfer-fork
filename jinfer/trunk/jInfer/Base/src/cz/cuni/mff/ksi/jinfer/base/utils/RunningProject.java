/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.base.utils;

import java.util.Properties;
import org.netbeans.api.project.Project;

/**
 * Class for determining which jInfer Project actually runs inference.
 *
 * @author sviro
 */
public final class RunningProject {

  private static Project project = null;

  private RunningProject() {
  }

  /**
   * Sets project as active and return true if there is no active project, otherwise return false.
   *
   * @param project to be set as active.
   * @return true if project is set to active, otherwise returns false.
   */
  public synchronized static boolean setActiveProject(final Project project) {
    if (!isActiveProject()) {
      RunningProject.project = project;
      return true;
    }
    return false;
  }

  /**
   * Removes active project.
   *
   */
  public static void removeActiveProject() {
    RunningProject.project = null;
  }

  /**
   * Gets active project.
   *
   * @return Active project.
   */
  public static Project getActiveProject() {
    return project;
  }

  /**
   * Returns whether active project is set or not.
   *
   * @return true if active project is set, otherwise returns false.
   */
  public static boolean isActiveProject() {
    return project != null;
  }

  /**
   * Gets Properties of active project.
   *
   * @return propeties of active project.
   */
  public static Properties getActiveProjectProps() {
    if (isActiveProject()) {
      return project.getLookup().lookup(Properties.class);
    }

    return null;
  }
}
