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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.khgrams;

import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
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
@ServiceProvider(service = AutomatonSimplifierFactory.class)
public class KHgramsFactory implements AutomatonSimplifierFactory {

  private static final Logger LOG = Logger.getLogger(KHgramsFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierKHgrams";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "k,h-grams";
  /** TODO anti comment */
  public static final String PROPERTIES_K = "kvalue";
  /** TODO anti comment */
  public static final String PROPERTIES_K_DEFAULT = "2";
  /** TODO anti comment */
  public static final String PROPERTIES_H = "hvalue";
  /** TODO anti comment */
  public static final String PROPERTIES_H_DEFAULT = "1";

  @Override
  public <T> AutomatonSimplifier<T> create() {
    LOG.debug("Creating new " + NAME);
    Properties p = RunningProject.getActiveProjectProps(getName());
    return new KHgrams<T>(Integer.parseInt(p.getProperty(PROPERTIES_K, PROPERTIES_K_DEFAULT)), Integer.parseInt(p.getProperty(PROPERTIES_H, PROPERTIES_H_DEFAULT)));
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
    return getDisplayName();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getUserModuleDescription() {
    return "Simplify automaton to fully k,h-context automaton in linear time.";
  }
}
