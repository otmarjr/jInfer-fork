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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering;

import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of a clusterer factory - creates a name clusterer.
 *
 * @author vektor
 */
@ServiceProvider(service = ClustererFactory.class)
public class NameClustererFactory implements ClustererFactory {

  @Override
  public Clusterer create() {
    return new NameClusterer();
  }

  @Override
  public String getModuleName() {
    return "Name clusterer";
  }

  @Override
  public String getCommentedSchema() {
    return getModuleName();
  }
}
