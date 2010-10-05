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

package cz.cuni.mff.ksi.jinfer.crudemdl.clustering;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.StructuralAbstractNode;

/**
 * Factory interface for Clusterer interface. Implementing factories
 * should be annotated as
 * \@ServiceProvider(service = ClustererFactory.class)
 * 
 * Simplifier will lookup installed implementations and choose one 
 * according to user settings. Then calls create to obtain instance of
 * Clusterer class.
 *
 * @author anti
 */
public interface ClustererFactory extends NamedModule, Capabilities {
  Clusterer<StructuralAbstractNode> create();
}