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

package cz.cuni.mff.ksi.jinfer.crudemdl.moduleselection;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.openide.util.Lookup;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class Lookuper<F extends NamedModule> {
  private Class<F> clazz;
  private String propertyName;

  public Lookuper(Class<F> clazz, final String propertyName) {
    this.clazz= clazz;
    this.propertyName= propertyName;
  }

  public F lookupF() {
    Properties properties= RunningProject.getActiveProjectProps();
    String name= properties.getProperty(this.propertyName);
    
    F result = null;
    for (F factory : lookupFs()) {
      if (factory.getName().equals(name)) {
        return factory;
      } else if (result == null) {
        result= factory;
      }
    }
    if (result == null) {
      throw new MissingModuleException(name);
    }
    return result;
  }

  public List<String> lookupFNames() {
    final List<String> list = new ArrayList<String>();
    for (F factory : lookupFs()) {
      list.add(factory.getName());
    }
    return list;
  }

  private List<F> lookupFs() {
    final List<F> result = new ArrayList<F>(
            Lookup.getDefault().lookupAll(clazz));
    Collections.sort(result, ModuleNameComparator.getComparator());
    return result;
  }

}
