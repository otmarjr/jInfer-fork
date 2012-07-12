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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessorFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory class for {@link AutomatonMergingState}.
 *
 * @author anti
 */
@ServiceProvider(service = ClusterProcessorFactory.class)
public class AutomatonMergingStateFactory implements ClusterProcessorFactory {

  private static final Logger LOG = Logger.getLogger(AutomatonMergingStateFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingState";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Automaton Merging State";
  /**
   * Property name (in configuration file) of first submodule - {@link AutomatonSimplifier}
   */
  public static final String PROPERTIES_AUTOMATON_SIMPLIFIER = "automaton-simplifier";
  /**
   * Default {@link AutomatonSimplifier} implementation if none is set.
   */
  public static final String PROPERTIES_AUTOMATON_SIMPLIFIER_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierGreedyMDL";
  /**
   * Property name (in configuration file) of second submodule - {@link RegexpAutomatonSimplifier}
   */
  public static final String PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER = "regexp-automaton-simplifier";
  /**
   * Default {@link RegexpAutomatonSimplifier} implementation if none is set.
   */
  public static final String PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateRegexpAutomatonSimplifierStateRemovalOrdered";

  @Override
  public ClusterProcessor<AbstractStructuralNode> create() {
    LOG.debug("Creating new " + NAME);
    return new AutomatonMergingState(getAutomatonSimplifierFactory(), getRegexpAutomatonSimplifierFactory());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append("(");
    sb.append(getAutomatonSimplifierFactory().getModuleDescription());
    sb.append(", ");
    sb.append(getRegexpAutomatonSimplifierFactory().getModuleDescription());
    sb.append(")");
    return sb.toString();
  }

  private AutomatonSimplifierFactory getAutomatonSimplifierFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class,
            p.getProperty(PROPERTIES_AUTOMATON_SIMPLIFIER, PROPERTIES_AUTOMATON_SIMPLIFIER_DEFAULT));
  }

  private RegexpAutomatonSimplifierFactory getRegexpAutomatonSimplifierFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(RegexpAutomatonSimplifierFactory.class,
            p.getProperty(PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER, PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER_DEFAULT));
  }

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" constructs prefix tree automaton from positive examples");
    sb.append(" in the cluster. The it selects AutomatonSimplifier class,");
    sb.append(" to which it passes automaton to merge some states. AutomatonSimplifier");
    sb.append(" is believed to return some sort of generalized automaton.");
    sb.append(" This generalized automaton is then sent to RegexpAutomatonSimplifier");
    sb.append(" class, which has to create regular expression from automaton somehow.");
    sb.append(" This regular expression is returned as grammar for cluster of elements.");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Arrays.asList("can.handle.complex.regexps");
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
