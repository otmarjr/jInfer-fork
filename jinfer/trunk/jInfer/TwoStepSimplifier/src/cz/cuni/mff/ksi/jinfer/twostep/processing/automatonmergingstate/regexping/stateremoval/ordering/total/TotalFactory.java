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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.total;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.OrdererFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link Total}.
 *
 * @author anti
 */
@ServiceProvider(service = OrdererFactory.class)
public class TotalFactory implements OrdererFactory {

  /**
   * Canonical name
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateRegexpAutomatonSimplifierStateRemovalOrdererTotal";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Total";
  private static final Logger LOG = Logger.getLogger(TotalFactory.class);

  @Override
  public <T> Orderer<T> create() {
    LOG.debug("Creating new " + NAME);
    return new Total<T>();
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

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" weights states and returns state with minimum weight to be removed. Performs weighting"
            + " on initial regular expression automaton, giving total ordering before algorithm proceeds."
            + " Changes to automaton don't affect ordering.");
    return sb.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}