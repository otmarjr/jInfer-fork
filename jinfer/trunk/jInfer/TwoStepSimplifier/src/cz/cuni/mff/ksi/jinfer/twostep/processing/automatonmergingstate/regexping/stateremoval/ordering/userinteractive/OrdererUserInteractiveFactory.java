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

package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.userinteractive;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrdererFactory;
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
  public static final String NAME = "OrdererUserInteractive";
  public static final String DISPLAY_NAME = "Orderer User Interactive";
  private static final Logger LOG = Logger.getLogger(OrdererUserInteractiveFactory.class);
  
  @Override
  public <T> RegexpAutomatonSimplifierStateRemovalOrderer<T> create() {
    LOG.debug("Creating new RegexpAutomatonSimplifierStateRemovalOrdererWeighted.");
    return new OrdererUserInteractive<T>();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return getDisplayName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  // TODO anti elaborate more
  @Override
  public String getUserModuleDescription() {
    StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" orders states in automaton by prompting user which state remove first.");
    return sb.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

}
