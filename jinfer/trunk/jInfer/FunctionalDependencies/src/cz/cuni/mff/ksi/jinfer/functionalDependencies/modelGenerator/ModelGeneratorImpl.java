/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies.modelGenerator;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.InitialModel;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.XMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGenerator;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGeneratorCallback;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author sviro
 */
@ServiceProvider(service = ModelGenerator.class)
public class ModelGeneratorImpl implements ModelGenerator {

  private static final Logger LOG = Logger.getLogger(ModelGeneratorImpl.class);

  @Override
  public void start(Input input, ModelGeneratorCallback callback) throws InterruptedException {
    InitialModel result = new InitialModel();

    result.addFD(this.<FD>getDataFromInput(input.getFunctionalDependencies(), FD.class));
    result.addXMLTree(this.<XMLTree>getDataFromInput(input.getDocuments(), XMLTree.class));

    callback.finished(result);
    return;
  }

  private <T> List<T> getDataFromInput(final Collection<File> files, Class<?> clazz) throws InterruptedException {
    if (BaseUtils.isEmpty(files)) {
      return Collections.<T>emptyList();
    }
    final List<T> result = new ArrayList<T>();

    Processor<T> processor = ModelGeneratorImpl.<T>getProcessor(clazz);

    if (processor != null) {
      for (File file : files) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        try {
          result.addAll(processor.process(new FileInputStream(file)));
        } catch (FileNotFoundException ex) {
          throw new RuntimeException("File not found: " + file.getAbsolutePath(), ex);
        }
      }
    } else {
      LOG.error("No processor for " + clazz.toString() + " has been found.");
    }
    return result;
  }
  
  private static <T> Processor<T> getProcessor(Class<?> clazz) {
    for (final Processor p : Lookup.getDefault().lookupAll(Processor.class)) {
      if (p.getResultType().equals(clazz)) {
        return (Processor<T>) p;
      }
    }

    return null;
  }
}
