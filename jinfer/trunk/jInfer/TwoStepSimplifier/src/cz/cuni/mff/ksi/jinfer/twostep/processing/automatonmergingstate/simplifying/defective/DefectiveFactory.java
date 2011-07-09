/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = AutomatonSimplifierFactory.class)
public class DefectiveFactory implements AutomatonSimplifierFactory {

  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierDefective";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Defective";
  public static final String PROPERTIES_AUTOMATON_SIMPLIFIER = "automaton-simplifier";
  public static final String PROPERTIES_AUTOMATON_SIMPLIFIER_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierGreedyMDL";
  public static final String PROPERTIES_DEFECTIVE = "suspection";
  public static final String PROPERTIES_DEFECTIVE_DEFAULT = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierDefectiveDefectiveMDL";

  @Override
  public <T> AutomatonSimplifier<T> create() {
    return new Defective<T>(getAutomatonSimplifierFactory(), getDefectiveAutomatonSimplifierFactory());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append("(");
    sb.append(getAutomatonSimplifierFactory().getModuleDescription());
    sb.append(",");
    sb.append(getDefectiveAutomatonSimplifierFactory().getModuleDescription());
    sb.append(")");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getUserModuleDescription() {
    return "TODO anti write sth";
  }

  private AutomatonSimplifierFactory getAutomatonSimplifierFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class, p.getProperty(PROPERTIES_AUTOMATON_SIMPLIFIER, PROPERTIES_AUTOMATON_SIMPLIFIER_DEFAULT));
  }

  private DefectiveAutomatonSimplifierFactory getDefectiveAutomatonSimplifierFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(DefectiveAutomatonSimplifierFactory.class, p.getProperty(PROPERTIES_DEFECTIVE, PROPERTIES_DEFECTIVE_DEFAULT));
  }
}
