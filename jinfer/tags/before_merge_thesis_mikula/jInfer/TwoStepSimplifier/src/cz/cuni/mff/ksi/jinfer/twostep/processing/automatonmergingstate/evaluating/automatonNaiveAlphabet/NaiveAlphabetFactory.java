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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.automatonNaiveAlphabet;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluator;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.AutomatonEvaluatorFactory;
import java.util.Collections;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = AutomatonEvaluatorFactory.class)
public class NaiveAlphabetFactory implements AutomatonEvaluatorFactory {

  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateAutomatonEvaluatorNaiveAlphabet";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Naive Alphabet";

  @Override
  public <T> AutomatonEvaluator<T> create() {
    return new NaiveAlphabet<T>();
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
    return "MDL on automaton, naive approach.";
    // TODO anti elaborate more
  }
}
