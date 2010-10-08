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

package cz.cuni.mff.ksi.jinfer.crudemdl.processing.passrepresentant;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import java.util.Collections;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory class for ClusterProcessorPassRepresentant.
 *
 * @author anti
 */
@ServiceProvider(service = ClusterProcessorFactory.class)
public class ClusterProcessorPassRepresentantFactory implements ClusterProcessorFactory {

  @Override
  public ClusterProcessor<AbstractStructuralNode> create() {
    return new ClusterProcessorPassRepresentant();
  }

  @Override
  public String getName() {
    return "ClusterProcessorPassRepresentant";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public String getDisplayModuleDescription() {
    final StringBuilder sb = new StringBuilder(getName());
    sb.append(" is really dummy processor of element cluster. The grammar"
            + " inferred for whole cluster of elements it returns"
            + " is just the representant of cluster. That is, no inferring"
            + " occurs at all. Just return representant as the grammar"
            + " for cluster being processed.");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }
}
