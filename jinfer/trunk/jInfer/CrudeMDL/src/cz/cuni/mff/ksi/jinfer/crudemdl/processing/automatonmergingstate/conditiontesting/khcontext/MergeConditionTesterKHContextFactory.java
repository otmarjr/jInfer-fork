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
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.conditiontesting.MergeCondidionTester;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = MergeConditionTesterFactory.class)
public class MergeConditionTesterKHContextFactory implements MergeConditionTesterFactory {
  @Override
  public String getName() {
    return "MergeConditionTesterKHContext";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public <T> MergeCondidionTester<T> create() {
    return new MergeConditionTesterKHContext<T>();
  }

}
