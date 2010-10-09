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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.weighted;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrderer;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrdererFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for RegexpAutomatonSimplifierStateRemovalOrdererWeighted.
 *
 * @author anti
 */
@ServiceProvider(service = RegexpAutomatonSimplifierStateRemovalOrdererFactory.class)
public class RegexpAutomatonSimplifierStateRemovalOrdererWeightedFactory implements RegexpAutomatonSimplifierStateRemovalOrdererFactory {
  private static final Logger LOG = Logger.getLogger(RegexpAutomatonSimplifierStateRemovalOrdererWeightedFactory.class);

  @Override
  public <T> RegexpAutomatonSimplifierStateRemovalOrderer<T> create() {
    LOG.debug("Creating new RegexpAutomatonSimplifierStateRemovalOrdererWeighted.");
    return new RegexpAutomatonSimplifierStateRemovalOrdererWeighted<T>();
  }

  @Override
  public String getName() {
    return "RegexpAutomatonSimplifierStateRemovalOrdererWeighted";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayModuleDescription() {
    final StringBuilder sb= new StringBuilder(getName());
    sb.append(" weights states and returns state with minimum weight to be removed."
            + " Weight of the state is the sum of length of all regular expressions"
            + " on in-transitions, out-transitions and loops.");
    return sb.toString();
  }
}
