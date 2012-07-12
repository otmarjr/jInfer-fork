/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.twostep.processing.alternations;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessorFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for a trivial implementation of
 * {@link cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor} -
 * simply returns all possible right sides as alternation in the resulting rule.
 *
 * @author vektor
 */
@ServiceProvider(service = ClusterProcessorFactory.class)
public class AlternationsFactory implements ClusterProcessorFactory {

  private static final Logger LOG = Logger.getLogger(AlternationsFactory.class);
  public static final String NAME = "TwoStepClusterProcessorAlternations";
  public static final String DISPLAY_NAME = "Alternations";

  @Override
  public ClusterProcessor<AbstractStructuralNode> create() {
    LOG.debug("Creating new " + NAME);
    return new Alternations();
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
  public List<String> getCapabilities() {
    return Collections.<String>emptyList();
  }

  @Override
  public String getUserModuleDescription() {
    return "A trivial implementation of Cluster Processor. Simply takes all "
            + "right sides from rules in a cluster and converts them to an "
            + "alternation for the resulting element.";
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
