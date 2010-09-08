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
package cz.cuni.mff.ksi.jinfer.crudemdl;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.ClusterProcessorFactory;
import java.util.List;

/**
 * Class providing methods for lookup modules important for inference.
 * @author anti
 */
public final class SimplifierModuleSelection {
  public static Clusterer<AbstractNode> getClusterer(final String name) {
    Lookuper<ClustererFactory> l= new Lookuper<ClustererFactory>(ClustererFactory.class);
    ClustererFactory result = l.lookupF(name);
    return result.create();
  }

  public static List<String> lookupClustererNames() {
    return new Lookuper<ClustererFactory>(ClustererFactory.class).lookupFNames();
  }

  public static ClusterProcessor<AbstractNode> getClusterProcessor(final String name) {
    Lookuper<ClusterProcessorFactory> l= new Lookuper<ClusterProcessorFactory>(ClusterProcessorFactory.class);
    ClusterProcessorFactory result = l.lookupF(name);
    return result.create();
  }

  public static List<String> lookupClusterProcessorNames() {
    return new Lookuper<ClusterProcessorFactory>(ClusterProcessorFactory.class).lookupFNames();
  }
}
