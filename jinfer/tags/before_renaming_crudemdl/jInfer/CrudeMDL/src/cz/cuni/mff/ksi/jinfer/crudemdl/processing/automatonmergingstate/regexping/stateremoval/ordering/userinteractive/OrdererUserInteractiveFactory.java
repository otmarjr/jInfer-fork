/*
 *  Copyright (C) 2010 anti
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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.userinteractive;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrderer;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrdererFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
//@ServiceProvider(service = RegexpAutomatonSimplifierStateRemovalOrdererFactory.class)
public class OrdererUserInteractiveFactory implements RegexpAutomatonSimplifierStateRemovalOrdererFactory {
  private static final Logger LOG = Logger.getLogger(OrdererUserInteractiveFactory.class);
  
  @Override
  public <T> RegexpAutomatonSimplifierStateRemovalOrderer<T> create() {
    LOG.debug("Creating new RegexpAutomatonSimplifierStateRemovalOrdererWeighted.");
    return new OrdererUserInteractive<T>();
  }

  @Override
  public String getName() {
    return "OrdererUserInteractive";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  // TODO anti elaborate more
  @Override
  public String getUserModuleDescription() {
    StringBuilder sb = new StringBuilder(getName());
    sb.append(" orders states in automaton by prompting user which state remove first.");
    return sb.toString();
  }

}
