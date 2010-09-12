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
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.interfaces.SchemaGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Simplifier;
import cz.cuni.mff.ksi.jinfer.runner.MissingModuleException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openide.util.Lookup;

/**
 * Class providing methods for lookup modules important for inference.
 * @author sviro
 */
// TODO sviro Rewrite using ModuleSelectionHelper
public final class ModuleSelection {

  // TODO sviro Write things like this as constants (ask vektor if unclear)
  private static Comparator<NamedModule> getComparator() {
    return new Comparator<NamedModule>() {

      @Override
      public int compare(final NamedModule o1, final NamedModule o2) {
        final String s1 = o1.getName();
        final String s2 = o2.getName();
        return s1.compareTo(s2);
      }
    };
  }


  private ModuleSelection() {
  }

  /**
   * 
   * @param name
   * @return
   */
  public static IGGenerator lookupIGGenerator(final String name) {
    final List<IGGenerator> igGenerators = lookupIGGenerators();
    if (igGenerators.isEmpty()) {
      throw new MissingModuleException("IG generator module not found.");
    }
    
    IGGenerator result = null;
    
    for (IGGenerator iGGenerator : igGenerators) {
      if (result == null) {
        result = iGGenerator;
      }
      
      if (iGGenerator.getName().equals(name)) {
        return iGGenerator;
      }
    }

    return result;
  }

  /**
   * 
   * @param name
   * @return
   */
  public static Simplifier lookupSimplifier(final String name) {
    final List<Simplifier> simplifiers = lookupSimplifiers();
    if (simplifiers.isEmpty()) {
      throw new MissingModuleException("Simplifier module not found.");
    }

    Simplifier result = null;

    for (Simplifier simplifier : simplifiers) {
      if (result == null) {
        result = simplifier;
      }

      if (simplifier.getName().equals(name)) {
        return simplifier;
      }
    }

    return result;
  }

  /**
   * 
   * @param name
   * @return
   */
  public static SchemaGenerator lookupSchemaGenerator(final String name) {
    final List<SchemaGenerator> schemaGenerators = lookupSchemaGenerators();
    if (schemaGenerators.isEmpty()) {
      throw new MissingModuleException("Schema generator module not found.");
    }

    SchemaGenerator result = null;

    for (SchemaGenerator schemaGenerator : schemaGenerators) {
      if (result == null) {
        result = schemaGenerator;
      }

      if (schemaGenerator.getName().equals(name)) {
        return schemaGenerator;
      }
    }

    return result;
  }

  /**
   * 
   * @return
   */
  public static List<String> lookupIGGeneratorNames() {
    final List<String> list = new ArrayList<String>();
    for (IGGenerator igGenerator : lookupIGGenerators()) {
      list.add(igGenerator.getName());
    }
    return list;
  }

  /**
   * 
   * @return
   */
  public static List<String> lookupSimplifierNames() {
    final List<String> list = new ArrayList<String>();
    for (Simplifier simplif : lookupSimplifiers()) {
      list.add(simplif.getName());
    }
    return list;
  }

  /**
   * 
   * @return
   */
  public static List<String> lookupSchemaGeneratorNames() {
    final List<String> list = new ArrayList<String>();
    for (SchemaGenerator sg : lookupSchemaGenerators()) {
      list.add(sg.getName());
    }
    return list;
  }

  private static List<Simplifier> lookupSimplifiers() {
    final Lookup lkp = Lookup.getDefault();
    final List<Simplifier> result = new ArrayList<Simplifier>(lkp.lookupAll(Simplifier.class));
    Collections.sort(result, getComparator());
    return result;
  }

  private static List<IGGenerator> lookupIGGenerators() {
    final Lookup lkp = Lookup.getDefault();
    final List<IGGenerator> result = new ArrayList<IGGenerator>(lkp.lookupAll(IGGenerator.class));
    Collections.sort(result, getComparator());
    return result;
  }

  private static List<SchemaGenerator> lookupSchemaGenerators() {
    final Lookup lkp = Lookup.getDefault();
    final List<SchemaGenerator> result = new ArrayList<SchemaGenerator>(lkp.lookupAll(SchemaGenerator.class));
    Collections.sort(result, getComparator());
    return result;
  }

}
