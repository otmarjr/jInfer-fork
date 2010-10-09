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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.khcontext;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.MergeConditionTesterFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.MergeConditionTester;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for MergeConditionTesterKHContext.
 *
 * @author anti
 */
@ServiceProvider(service = MergeConditionTesterFactory.class)
public class MergeConditionTesterKHContextFactory implements MergeConditionTesterFactory {
  private static final Logger LOG = Logger.getLogger(MergeConditionTesterKHContextFactory.class);
  private int parameterK = 0;
  private int parameterH = 0;

  @Override
  public <T> MergeConditionTester<T> create() {
    LOG.debug("Creating new MergeConditionTesterKHContext.");
    return new MergeConditionTesterKHContext<T>(parameterK, parameterH);
  }

  @Override
  public String getName() {
    return "MergeConditionTesterKHContext";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    return Arrays.asList("parameters");
  }

  // TODO anti write here
  @Override
  public String getDisplayModuleDescription() {
    return "TODO anti write here";
  }

  @Override
  public List<String> getParameterNames() {
    return Arrays.<String>asList("k", "h");
  }

  @Override
  public String getParameterDisplayDescription(String parameterName) {
    if ("k".equals(parameterName)) {
      return "k in k,h-context. That is the number of transitions that have to"
              + "be same (by means of symbols) before state.";
    }
    if ("h".equals(parameterName)) {
      return "h in k,h-context. That is the number of preceeding states merged,"
              + " not including the states tested for k,h-context equivality.";
    }
    throw new IllegalArgumentException("Asking for a description of unknown parameter");
  }

  @Override
  public void setParameter(String parameterName, int newValue) {
    if ("k".equals(parameterName)) {
      this.parameterK= newValue;
    }
    if ("h".equals(parameterName)) {
      this.parameterH= newValue;
    }
  }

}
