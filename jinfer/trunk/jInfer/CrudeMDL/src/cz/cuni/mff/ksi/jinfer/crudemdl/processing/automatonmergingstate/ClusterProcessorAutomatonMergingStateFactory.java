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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory class for ClusterProcessorAutomatonMergingState.
 *
 * @author anti
 */
@ServiceProvider(service = ClusterProcessorFactory.class)
public class ClusterProcessorAutomatonMergingStateFactory implements ClusterProcessorFactory {
  private static final Logger LOG = Logger.getLogger(ClusterProcessorAutomatonMergingStateFactory.class);

  public static final String NAME = "ClusterProcessorAutomatonMergingState";
  public static final String PROPERTIES_AUTOMATON_SIMPLIFIER = "automaton-simplifier";
  public static final String PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER = "regexp-automaton-simplifier";

  @Override
  public ClusterProcessor<AbstractStructuralNode> create() {
    LOG.debug("Creating new ClusterProcessorAutomatonMergingState.");
    return new ClusterProcessorAutomatonMergingState(getAutomatonSimplifierFactory(), getRegexpAutomatonSimplifierFactory());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb= new StringBuilder(getName());
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
            p.getProperty(PROPERTIES_AUTOMATON_SIMPLIFIER));
  }

  private RegexpAutomatonSimplifierFactory getRegexpAutomatonSimplifierFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(RegexpAutomatonSimplifierFactory.class,
            p.getProperty(PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER));
  }

  @Override
  public String getDisplayModuleDescription() {
    final StringBuilder sb = new StringBuilder(getName());
    sb.append(" constructs prefix tree automaton from positive examples"
            + " in the cluster. The it selects AutomatonSimplifier class,"
            + " to which it passes automaton to merge some states. AutomatonSimplifier"
            + " is believed to return some sort of generalized automaton."
            + " This generalized automaton is then sent to RegexpAutomatonSimplifier"
            + " class, which has to create regular expression from automaton somehow."
            + " This regular expression is returned as grammar for cluster of elements.");
    return sb.toString();

  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }
}
