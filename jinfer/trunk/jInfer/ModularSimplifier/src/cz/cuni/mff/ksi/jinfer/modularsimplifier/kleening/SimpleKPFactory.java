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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening;

import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.properties.PropertiesPanel;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of a Kleene processor factory - creates a Simple KP.
 * 
 * @author vektor
 */
@ServiceProvider(service = KleeneProcessorFactory.class)
public class SimpleKPFactory implements KleeneProcessorFactory {

  @Override
  public KleeneProcessor create() {
    return new SimpleKP(Integer.parseInt(RunningProject.getActiveProjectProps().getProperty(
            PropertiesPanel.KLEENE_REPETITIONS,
            Integer.toString(PropertiesPanel.KLEENE_REPETITIONS_DEFAULT))));
  }

  @Override
  public String getModuleName() {
    return "Simple Kleene processor";
  }

  @Override
  public String getCommentedSchema() {
    return getModuleName();
  }
}
