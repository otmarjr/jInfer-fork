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
package cz.cuni.mff.ksi.jinfer.twostep;

import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.Simplifier;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SimplifierCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleanerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * TwoStepSimplifier works in two step for simplification.
 * First it searches for a suitable clusterer submodule, to which it passes whole initialGrammar.
 * Clusterer is responsible to cluster elements properly. Cluster of elements
 * is then considered to be one, and the same element, with various instances
 * in input files.
 *
 * For every cluster of elements, the clusterProcessor submodule is called.
 * Given the clusterer and list of observed positive examples (grammar = list
 * of elements), cluster processor is expected to produce one Element instance,
 * on which proper definition of regular expression representing content model
 * of the element children will be held.
 *
 * The attributes of elements in the cluster are processed separately afterwards.
 * Currently, only simple processing is done - required/optional. This will be
 * extended in future to separated attribute processor submodule.
 * TODO anti Comment when submodule attributeProcessor done
 * @author anti
 */
@ServiceProvider(service = Simplifier.class)
public class TwoStepSimplifierFactory implements Simplifier {
  /**
   * Name of the module in constant, for use in classes in this module.
   */
  public static final String NAME = "TwoStepSimplifier";
  /**
   * TODO anti comment
   */
  public static final String DISPLAY_NAME = "TwoStep";
  /**
   * Property name of clusterer submodule.
   */
  public static final String PROPERTIES_CLUSTERER = "clusterer";
  /** TODO anti comment
   * 
   */
  public static final String PROPERTIES_CLUSTERER_DEFAULT = "TwoStepClustererWithAttributesIname";
  /**
   * Property name of cluster processor submodule.
   */
  public static final String PROPERTIES_CLUSTER_PROCESSOR = "cluster-processor";
  /** TODO anti comment
   * 
   */
  public static final String PROPERTIES_CLUSTER_PROCESSOR_DEFAULT = "TwoStepClusterProcessorAutomatonMergingState";
  /**
   * Property name of regular expression cleaner submodule.
   */
  public static final String PROPERTIES_CLEANER = "cleaner";
  /** TODO anti comment
   * 
   */
  public static final String PROPERTIES_CLEANER_DEFAULT = "TwoStepRegularExpressionCleanerNull";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append("(");
    sb.append(getClustererFactory().getModuleDescription());
    sb.append(", ");
    sb.append(getClusterProcessorFactory().getModuleDescription());
    sb.append(", ");
    sb.append(getRegularExpressionCleanerFactory().getModuleDescription());
    sb.append(")");
    return sb.toString();
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.emptyList();
  }

  private ClustererFactory getClustererFactory() {
    final Properties p = RunningProject.getActiveProjectProps(this.getName());

    return ModuleSelectionHelper.lookupImpl(ClustererFactory.class, p.getProperty(PROPERTIES_CLUSTERER, PROPERTIES_CLUSTERER_DEFAULT));
  }

  private ClusterProcessorFactory getClusterProcessorFactory() {
    final Properties p = RunningProject.getActiveProjectProps(this.getName());

    return ModuleSelectionHelper.lookupImpl(ClusterProcessorFactory.class, p.getProperty(PROPERTIES_CLUSTER_PROCESSOR, PROPERTIES_CLUSTER_PROCESSOR_DEFAULT));
  }

  private RegularExpressionCleanerFactory getRegularExpressionCleanerFactory() {
    final Properties p = RunningProject.getActiveProjectProps(this.getName());

    return ModuleSelectionHelper.lookupImpl(RegularExpressionCleanerFactory.class, p.getProperty(PROPERTIES_CLEANER, PROPERTIES_CLEANER_DEFAULT));
  }

  @Override
  public void start(final List<Element> initialGrammar, final SimplifierCallback callback) throws InterruptedException {
    final TwoStepSimplifier simplifier= new TwoStepSimplifier(
            getClustererFactory(), getClusterProcessorFactory(), getRegularExpressionCleanerFactory()
            );
    callback.finished(
            simplifier.simplify(initialGrammar)
            );
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
