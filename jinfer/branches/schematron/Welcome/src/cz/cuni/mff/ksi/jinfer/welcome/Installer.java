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
package cz.cuni.mff.ksi.jinfer.welcome;

import java.util.Set;
import org.openide.modules.ModuleInstall;
import org.openide.windows.TopComponent;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 * 
 * @author sviro
 */
public class Installer extends ModuleInstall {

  private static final long serialVersionUID = 353356475;

  @Override
  public boolean closing() {
    WelcomeTopComponent topComp = null;
    final Set<TopComponent> tcs = TopComponent.getRegistry().getOpened();
    for (TopComponent tc : tcs) {
      if (tc instanceof WelcomeTopComponent) {
        topComp = (WelcomeTopComponent) tc;
        break;
      }
    }
    if (WelcomeTopComponent.getDefault().isShowOnStartup()) {
      if (topComp == null) {
        topComp = WelcomeTopComponent.findInstance();
      }
      // activate welcome screen at shutdown to avoid editor initialization
      // before the welcome screen is activated again at startup
      topComp.open();
      topComp.requestActive();
    } else if (topComp != null) {
      topComp.close();
    }
    return super.closing();
  }
}
