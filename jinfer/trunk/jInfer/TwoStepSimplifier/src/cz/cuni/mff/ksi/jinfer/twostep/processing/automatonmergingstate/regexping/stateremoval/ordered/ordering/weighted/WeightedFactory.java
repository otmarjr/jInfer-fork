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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.weighted;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordered.ordering.OrdererFactory;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link Weighted}.
 *
 * @author anti
 */
@ServiceProvider(service = OrdererFactory.class)
public class WeightedFactory implements OrdererFactory {

  /**
   * Canonical name
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateRegexpAutomatonSimplifierStateRemovalOrdererWeighted";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Weighted";
  private static final Logger LOG = Logger.getLogger(WeightedFactory.class);
  public static final String PROPERTIES_REVAL = "regexp-evaluator";
  public static final String PROPERTIES_REVAL_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateRegexpEvaluatorToStringSize";
  

  @Override
  public <T> Orderer<T> create() {
    LOG.debug("Creating new " + NAME);
    return new Weighted<T>(getRevalFactory());
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
    sb.append(" weights states and returns state with minimum weight to be removed."
            + " Weight of the state is the sum of length of all regular expressions"
            + " on in-transitions, out-transitions and loops.");
    return sb.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
  
  private RegexpEvaluatorFactory getRevalFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(RegexpEvaluatorFactory.class,
            p.getProperty(PROPERTIES_REVAL, PROPERTIES_REVAL_DEFAULT));
    
  }
}
