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
package cz.cuni.mff.ksi.jinfer.twostep.contentinfering.pattern;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrer;
import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.AutomatonMergingStateFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = ContentInferrerFactory.class)
public class PatternFactory implements ContentInferrerFactory {

  private static final Logger LOG = Logger.getLogger(AutomatonMergingStateFactory.class);
  /** 
   * Canonical name.
   */
  public static final String NAME = "TwoStepContentInferrerPattern";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "String pattern facet";
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

  private AutomatonSimplifierFactory getAutomatonSimplifierFactory() {
    final Properties p = RunningProject.getActiveProjectProps("TwoStepClusterProcessorAutomatonMergingState");

    return ModuleSelectionHelper.lookupImpl(AutomatonSimplifierFactory.class,
            p.getProperty(PROPERTIES_AUTOMATON_SIMPLIFIER, PROPERTIES_AUTOMATON_SIMPLIFIER_DEFAULT));
  }

  private RegexpAutomatonSimplifierFactory getRegexpAutomatonSimplifierFactory() {
    final Properties p = RunningProject.getActiveProjectProps("TwoStepClusterProcessorAutomatonMergingState");

    return ModuleSelectionHelper.lookupImpl(RegexpAutomatonSimplifierFactory.class,
            p.getProperty(PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER, PROPERTIES_REGEXP_AUTOMATON_SIMPLIFIER_DEFAULT));
  }
  
  @Override
  public ContentInferrer create() {
    LOG.debug("Creating new " + NAME);
    return new Pattern(getAutomatonSimplifierFactory(), getRegexpAutomatonSimplifierFactory());
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
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getUserModuleDescription() {
    return "Infers xs:string type with pattern facet, infers regular expression for pattern facet.";
  }
}
