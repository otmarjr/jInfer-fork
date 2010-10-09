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
package cz.cuni.mff.ksi.jinfer.base.interfaces;

import java.util.List;

/**
 * Interface of inference modules that have capabilities.
 * TODO vektor Comment more, how are capabilities represented, is this optional
 * or required to implement this?
 * anti: i suppose for required, once upon a time, one would like to implement
 * new chain of modules, and how can he find if lookup found new module, or old
 * one. he can add capabilities to new one, but if old one doesn't even implement
 * he cannot ask for them to find out, if he is using new or old.
 * 
 * @author vektor
 */
public interface Capabilities {

  /**
   * Returns the list of capabilities (strings) of this module.
   * 
   * @return List of capability names
   */
  List<String> getCapabilities();

}
