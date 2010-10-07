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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering.RegexpAutomatonSimplifierStateRemovalOrdererFactory;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = RegexpAutomatonSimplifierFactory.class)
public class RegexpAutomatonSimplifierStateRemovalFactory implements RegexpAutomatonSimplifierFactory {
  public static final String NAME = "RegexpAutomatonSimplifierStateRemoval";
  public static final String PROPERTIES_ORDERER = "orderer";

  @Override
  public <T> RegexpAutomatonSimplifier<T> create() {
    return new RegexpAutomatonSimplifierStateRemoval<T>(
            this.getRegexpAutomatonSimplifierStateRemovalOrdererFactory());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb = new StringBuilder(getName());
    sb.append("(");
    sb.append(getRegexpAutomatonSimplifierStateRemovalOrdererFactory().getModuleDescription());
    sb.append(")");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getDisplayModuleDescription() {
    final StringBuilder sb = new StringBuilder(getName());
    sb.append(" generates regular expression from automaton by sequentially"
            + " removing states from it. When a state is removed, all transitions"
            + " to and from it are collapsed. All loops are collapsed to one"
            + " with alternation of old loops. Then, for each pair of in-transition"
            + " and out-transition, new transition from in-transition source, to"
            + " out-transition destination is created. The regular expression on"
            + " this new transition is concatenation of in, loop, out regular"
            + " expressions. This module needs RegexpAutomatonSimplifierStateRemovalOrderer"
            + " to obtain sequence of states to remove (different ordeing results"
            + " in different regular expressions.");
    return sb.toString();
  }

  private RegexpAutomatonSimplifierStateRemovalOrdererFactory getRegexpAutomatonSimplifierStateRemovalOrdererFactory() {
    final Properties p = RunningProject.getActiveProjectProps(getName());

    return ModuleSelectionHelper.lookupImpl(RegexpAutomatonSimplifierStateRemovalOrdererFactory.class,
            p.getProperty(PROPERTIES_ORDERER));
  }

}
