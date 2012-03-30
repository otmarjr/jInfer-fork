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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.heuristic;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluatorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for RegexpAutomatonSimplifierStateRemoval.
 *
 * @author anti
 */
@ServiceProvider(service = RegexpAutomatonSimplifierFactory.class)
public class StateRemovalHeuristicFactory implements RegexpAutomatonSimplifierFactory {

  private static final Logger LOG = Logger.getLogger(StateRemovalHeuristicFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateRegexpAutomatonSimplifierStateRemovalHeuristic";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "State Removal Heuristic";
  /**
   * Property name of orderer submodule.
   */
  public static final String PROPERTIES_REVAL = "regexp-evaluator";
  /**
   * Default orderer when none selected.
   */
  public static final String PROPERTIES_REVAL_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateRegexpEvaluatorToStringSize";

  @Override
  public <T> RegexpAutomatonSimplifier<T> create() {
    LOG.debug("Creating new " + NAME);
    return new StateRemovalHeuristic<T>(getREvalFactory());
  }

  @Override
  public String getName() {
    return NAME;
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
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" generates regular expression from automaton by sequentially"
            + " removing states from it.");
    return sb.toString();
  }

  private RegexpEvaluatorFactory getREvalFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(RegexpEvaluatorFactory.class,
            p.getProperty(PROPERTIES_REVAL, PROPERTIES_REVAL_DEFAULT));
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
