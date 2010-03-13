/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author vektor
 */
public class ModuleSelectionImpl implements ModuleSelection {

  private static final Logger LOG = Logger.getLogger(ModuleSelection.class.getCanonicalName());

  @Override
  public String getIGGenerator() {
    final String ret = NbPreferences.forModule(JinferOPTPanel.class).get("PreferredIGGenerator", null);
    if (ret != null) {
      return ret;
    }
    LOG.log(Level.WARNING, "Preferred IGGenerator not found, will try the first one available.");
    final Lookup lkp = Lookups.forPath("IGGeneratorProviders");
    return lkp.lookupAll(IGGenerator.class).iterator().next().getModuleName();
  }

  @Override
  public String getSimplifier() {
    final String ret = NbPreferences.forModule(JinferOPTPanel.class).get("PreferredSimplifier", null);
    if (ret != null) {
      return ret;
    }
    LOG.log(Level.WARNING, "Preferred Simplifier not found, will try the first one available.");
    final Lookup lkp = Lookups.forPath("SimplifierProviders");
    return lkp.lookupAll(Simplifier.class).iterator().next().getModuleName();
  }

  @Override
  public String getSchemaGenerator() {
    final String ret = NbPreferences.forModule(JinferOPTPanel.class).get("PreferredSchemaGenerator", null);
    if (ret != null) {
      return ret;
    }
    LOG.log(Level.WARNING, "Preferred SchemaGenerator not found, will try the first one available.");
    final Lookup lkp = Lookups.forPath("SchemaGeneratorProviders");
    return lkp.lookupAll(SchemaGenerator.class).iterator().next().getModuleName();
  }

}