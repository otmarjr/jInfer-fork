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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
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

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public ClusterProcessor<AbstractNode> create() {
    return new ClusterProcessorAutomatonMergingState();
  }
}
