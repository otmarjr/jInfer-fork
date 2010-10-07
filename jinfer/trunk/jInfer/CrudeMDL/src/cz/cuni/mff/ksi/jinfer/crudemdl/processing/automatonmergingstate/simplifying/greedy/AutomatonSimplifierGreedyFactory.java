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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.greedy;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Collections;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = AutomatonSimplifierFactory.class)
public class AutomatonSimplifierGreedyFactory implements AutomatonSimplifierFactory {

  @Override
  public <T> AutomatonSimplifier<T> create() {
    return new AutomatonSimplifierGreedy<T>(getName());
  }

  @Override
  public String getName() {
    return "AutomatonSimplifierGreedy";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public String getDisplayModuleDescription() {
    StringBuilder sb = new StringBuilder(getName());
    sb.append(" takes one MergeConditionTester and merges all states"
            + " that can be merged. E.g. with k,h-context condition tester"
            + " it defacto creates k,h-context automaton.");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }
}
