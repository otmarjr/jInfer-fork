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
package cz.cuni.mff.ksi.jinfer.moduleselector;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.ModuleSelection;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of ModuleSelection: looks for selected modules in
 * NB configuration, if it finds nothing, uses the first module available.
 * 
 * @author vektor
 */
@ServiceProvider(service = ModuleSelection.class)
public class ModuleSelectionImpl implements ModuleSelection {

  private static final Logger LOG = Logger.getLogger(ModuleSelectionImpl.class.getCanonicalName());

  @Override
  public String getIGGenerator() {
    final String ret = NbPreferences.forModule(JinferOPTPanel.class).get("PreferredIGGenerator", null);
    if (ret != null) {
      return ret;
    }
    LOG.log(Level.WARNING, "PreferredIGGenerator not found, will use the first one available.");
    return Lookup.getDefault().lookupAll(IGGenerator.class).iterator().next().getModuleName();
  }

  @Override
  public String getSimplifier() {
    final String ret = NbPreferences.forModule(JinferOPTPanel.class).get("PreferredSimplifier", null);
    if (ret != null) {
      return ret;
    }
    LOG.log(Level.WARNING, "PreferredSimplifier not found, will use the first one available.");
    return Lookup.getDefault().lookupAll(Simplifier.class).iterator().next().getModuleName();
  }

  @Override
  public String getSchemaGenerator() {
    final String ret = NbPreferences.forModule(JinferOPTPanel.class).get("PreferredSchemaGenerator", null);
    if (ret != null) {
      return ret;
    }
    LOG.log(Level.WARNING, "PreferredSchemaGenerator not found, will use the first one available.");
    return Lookup.getDefault().lookupAll(SchemaGenerator.class).iterator().next().getModuleName();
  }
}
