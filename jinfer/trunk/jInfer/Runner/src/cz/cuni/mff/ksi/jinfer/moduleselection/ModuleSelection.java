/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.moduleselection;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.runner.MissingModuleException;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author sviro
 */
public final class ModuleSelection {

  public static final String MODULE_SELECTION_INITIAL_GRAMMAR = "moduleselector.initialgrammar";
  public static final String MODULE_SELECTION_SIMPIFIER = "moduleselector.simplifier";
  public static final String MODULE_SELECTION_SCHEMA_GENERATOR = "moduleselector.schemagenerator";

  private ModuleSelection() {
  }

  /**
   * 
   * @param name
   * @return
   */
  public static IGGenerator lookupIGGenerator(final String name) {
    final Lookup lkp = Lookup.getDefault();
    IGGenerator result = null;
    for (final IGGenerator igGenerator : lkp.lookupAll(IGGenerator.class)) {
      if (result == null) {
        result = igGenerator;
      }

      if (igGenerator.getModuleName().equals(name)) {
        return igGenerator;
      }
    }

    if (result != null) {
      return result;
    }
    throw new MissingModuleException("IG generator module not found.");
  }

  /**
   * 
   * @param name
   * @return
   */
  public static Simplifier lookupSimplifier(final String name) {
    final Lookup lkp = Lookup.getDefault();
    Simplifier result = null;
    for (final Simplifier simplifier : lkp.lookupAll(Simplifier.class)) {
      if (result == null) {
        result = simplifier;
      }
      if (simplifier.getModuleName().equals(name)) {
        return simplifier;
      }
    }

    if (result != null) {
      return result;
    }
    throw new MissingModuleException("Simplifier module not found.");
  }

  /**
   * 
   * @param name
   * @return
   */
  public static SchemaGenerator lookupSchemaGenerator(final String name) {
    final Lookup lkp = Lookup.getDefault();
    SchemaGenerator result = null;
    for (final SchemaGenerator schemaGenerator : lkp.lookupAll(SchemaGenerator.class)) {
      if (result == null) {
        result = schemaGenerator;
      }
      if (schemaGenerator.getModuleName().equals(name)) {
        return schemaGenerator;
      }
    }

    if (result != null) {
      return result;
    }
    throw new MissingModuleException("Schema generator module not found.");
  }

  /**
   * 
   * @return
   */
  public static List<String> lookupIGGenerators() {
    final List<String> list = new ArrayList<String>();
    final Lookup lkp = Lookup.getDefault();
    for (IGGenerator igGenerator : lkp.lookupAll(IGGenerator.class)) {
      list.add(igGenerator.getModuleName());
    }
    return list;
  }

  /**
   * 
   * @return
   */
  public static List<String> lookupSimplifiers() {
    final List<String> list = new ArrayList<String>();
    final Lookup lkp = Lookup.getDefault();
    for (Simplifier simplif : lkp.lookupAll(Simplifier.class)) {
      list.add(simplif.getModuleName());
    }
    return list;
  }

  /**
   * 
   * @return
   */
  public static List<String> lookupSchemaGenerators() {
    final List<String> list = new ArrayList<String>();
    final Lookup lkp = Lookup.getDefault();
    for (SchemaGenerator sg : lkp.lookupAll(SchemaGenerator.class)) {
      list.add(sg.getModuleName());
    }
    return list;
  }
}
