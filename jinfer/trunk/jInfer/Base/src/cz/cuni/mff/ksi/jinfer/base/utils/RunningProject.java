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
 *
 * @author sviro
 */
public final class RunningProject {

  private static Project project = null;

  private RunningProject() {}

  public static boolean setActiveProject(final Project project) {
    if (!isActiveProject()) {
      RunningProject.project = project;
      return true;
    }
    return false;
  }

  public static void removeActiveProject() {
    if (RunningProject.project != null) {
      RunningProject.project = null;
    }
  }

  public static Project getActiveProject() {
    return project;
  }

  public static boolean isActiveProject() {
    return project != null;
  }

  public static Properties getActiveProjectProps() {
    if (isActiveProject()) {
      return project.getLookup().lookup(Properties.class);
    }

    return null;
  }
  

}
