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
import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrerFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessorFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * TwoStepSimplifier is modular, extensible implementation.
 * 
 * Factory class for the worker {@link TwoStepSimplifier}.
 * 
 * @author anti
 */
@ServiceProvider(service = Simplifier.class)
public class TwoStepSimplifierFactory implements Simplifier {

  /**
   * Name of the module in constant, for use in classes in this module.
   */
  public static final String NAME = "TwoStepSimplifier";
  /**
   * Name displayed to user in properties panels.
   */
  public static final String DISPLAY_NAME = "TwoStep";
  /**
   * Property name of clusterer submodule.
   */
  public static final String PROPERTIES_CLUSTERER = "clusterer";
  /** 
   * Default clusterer to be used if none selected.
   */
  public static final String PROPERTIES_CLUSTERER_DEFAULT = "TwoStepClustererWithAttributesIname";
  /**
   * Property name of cluster processor submodule.
   */
  public static final String PROPERTIES_CLUSTER_PROCESSOR = "cluster-processor";
  /** 
   * Default cluster processor to be used if none selected.
   */
  public static final String PROPERTIES_CLUSTER_PROCESSOR_DEFAULT = "TwoStepClusterProcessorAutomatonMergingState";
  /**
   * Property name of regular expression cleaner submodule.
   */
  public static final String PROPERTIES_CLEANER = "cleaner";
  /** 
   * Default cleaner to be used if none selected.
   */
  public static final String PROPERTIES_CLEANER_DEFAULT = "TwoStepRegularExpressionCleanerChained";
  /**
   * Property name of content inferrer submodule.
   */
  public static final String PROPERTIES_CONTENT_INFERRER = "content-inferrer";
  /** 
   * Default content inferrer to be used if none selected.
   */
  public static final String PROPERTIES_CONTENT_INFERRER_DEFAULT = "TwoStepContentInferrerSimple";

  /**
   * Canonical name
   *
   * @return name
   */
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
    LinkedList<String> ab = new LinkedList<String>();
    ab.addAll(getClustererFactory().getCapabilities());
    ab.retainAll(getClusterProcessorFactory().getCapabilities());
    return ab;
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

  private ContentInferrerFactory getContentInferrerFactory() {
    final Properties p = RunningProject.getActiveProjectProps(this.getName());

    return ModuleSelectionHelper.lookupImpl(ContentInferrerFactory.class, p.getProperty(PROPERTIES_CONTENT_INFERRER, PROPERTIES_CONTENT_INFERRER_DEFAULT));
  }

  @Override
  public void start(final List<Element> grammar, final SimplifierCallback callback) throws InterruptedException {
    final TwoStepSimplifier simplifier = new TwoStepSimplifier(
            getClustererFactory(),
            getClusterProcessorFactory(),
            getRegularExpressionCleanerFactory(),
            getContentInferrerFactory());
    final List<Element> simplifiedGrammar = simplifier.simplify(grammar);
    callback.finished(simplifiedGrammar);
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
