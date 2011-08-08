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
package cz.cuni.mff.ksi.jinfer.twostep.clustering.withattributesiname;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.twostep.clustering.ClustererWithAttributes;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory class for {@link Iname}.
 *
 * Has capability "attributeClusters" and informs about it in {@link getCapabilities}().
 *
 * {@see ClustererWithAttributes}
 * @author anti
 */
@ServiceProvider(service = ClustererFactory.class)
public class InameFactory implements ClustererFactory {

  private static final Logger LOG = Logger.getLogger(InameFactory.class);
  /**
   * Canonical name.
   */
  public static final String NAME = "TwoStepClustererWithAttributesIname";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Iname (with attributes)";

  @Override
  public Clusterer<AbstractStructuralNode> create() {
    LOG.debug("Creating new " + NAME);
    return new Iname();
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
    return Arrays.asList("attributeClusters", "can.handle.complex.regexps");
  }

  @Override
  public String getUserModuleDescription() {
    final StringBuilder sb = new StringBuilder(getDisplayName());
    sb.append(" clusters Elements in initial grammar. It considers two elements"
            + " equivalent exactly when their names equals (ignoring case)."
            + " Attributes are handled same way. It does not provide any heuristic to find out, whether"
            + " some attributes are same across different elements.");
    return sb.toString();
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
