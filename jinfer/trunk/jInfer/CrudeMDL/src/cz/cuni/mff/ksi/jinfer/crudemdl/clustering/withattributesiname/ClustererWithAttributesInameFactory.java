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

package cz.cuni.mff.ksi.jinfer.crudemdl.clustering.withattributesiname;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.ClustererFactory;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory class for ClustererWithAttributesIname class.
 *
 * Has capability "attributeClusters" and informs about it in getCapabilities().
 * @author anti
 */
@ServiceProvider(service = ClustererFactory.class)
public class ClustererWithAttributesInameFactory implements ClustererFactory {
  @Override
  public Clusterer<AbstractStructuralNode> create() {
    return new ClustererWithAttributesIname();
  }

  @Override
  public String getName() {
    return "ClustererInameWithAttributes";
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

  @Override
  public List<String> getCapabilities() {
    final List<String> l= new ArrayList<String>();
    l.add("attributeClusters");
    return l;
  }
}
