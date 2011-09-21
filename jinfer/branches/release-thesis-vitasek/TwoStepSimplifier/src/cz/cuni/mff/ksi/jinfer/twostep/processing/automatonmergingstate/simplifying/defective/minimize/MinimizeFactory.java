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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.defective.minimize;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link Minimize}.
 *
 * @author anti
 */
@ServiceProvider(service = AutomatonSimplifierFactory.class)
public class MinimizeFactory implements AutomatonSimplifierFactory {

  private static final Logger LOG = Logger.getLogger(MinimizeFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierDefectiveMinimize";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Minimize only";

  @Override
  public <T> AutomatonSimplifier<T> create() {
    LOG.debug("Creating new " + NAME);
    return new Minimize<T>();
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
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" takes one <tt><i>MergeConditionTester</i></tt> and merges all states"
            + " that can be merged. E.g. with <i>k,h</i>-context condition tester"
            + " it defacto creates <i>k,h</i>-context automaton.");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
