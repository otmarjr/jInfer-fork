/*
 *  Copyright (C) 2010 rio
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
package cz.cuni.mff.ksi.jinfer.base;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

  private static final long serialVersionUID = 54612321l;

  private static final Logger LOG = Logger.getLogger(ModuleInstall.class.getName());

  @Override
  public void restored() {
    PropertyConfigurator.configure("log4j.properties");
    LOG.info("Base module loaded.");
  }

  @Override
  public boolean closing() {
    LOG.info("Closing Base module.");
    return true;
  }


}
