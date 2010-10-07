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

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory class for ClusterProcessorAutomatonMergingState.
 *
 * @author anti
 */
@ServiceProvider(service = ClusterProcessorFactory.class)
public class ClusterProcessorAutomatonMergingStateFactory implements ClusterProcessorFactory {
  @Override
  public String getName() {
    return "ClusterProcessorAutomatonMergingState";
  }

  // TODO anti more comment when attributes are completed
  @Override
  public String getModuleDescription() {
    StringBuilder sb = new StringBuilder(getName());
    sb.append(" constructs prefix tree automaton from positive examples"
            + " in the cluster. The it selects AutomatonSimplifier class,"
            + " to which it passes automaton to merge some states. AutomatonSimplifier"
            + " is believed to return some sort of generalized automaton."
            + " This generalized automaton is then sent to RegexpAutomatonSimplifier"
            + " class, which has to create regular expression from automaton somehow."
            + " This regular expression is returned as grammar for cluster of elements."
            + " Attributes are processed separately.");
    return sb.toString();

  }

  @Override
  public ClusterProcessor<AbstractStructuralNode> create() {
    return new ClusterProcessorAutomatonMergingState(getName());
  }
}
