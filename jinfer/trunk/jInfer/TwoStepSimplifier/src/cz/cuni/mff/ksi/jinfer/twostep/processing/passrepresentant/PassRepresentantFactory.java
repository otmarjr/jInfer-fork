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
package cz.cuni.mff.ksi.jinfer.twostep.processing.passrepresentant;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessorFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory class for PassRepresentant.
 *
 * @author anti
 */
@ServiceProvider(service = ClusterProcessorFactory.class)
public class PassRepresentantFactory implements ClusterProcessorFactory {

  private static final Logger LOG = Logger.getLogger(PassRepresentantFactory.class);
  public static final String NAME = "TwoStepClusterProcessorPassRepresentant";
  public static final String DISPLAY_NAME = "PassRepresentant";

  @Override
  public ClusterProcessor<AbstractStructuralNode> create() {
    LOG.debug("Creating new ClusterProcessorPassRepresentant.");
    return new PassRepresentant();
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

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
