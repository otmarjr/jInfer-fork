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
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGenerator;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGeneratorCallback;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
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

    result.addFD(getFDsFromInput(input.getFunctionalDependencies()));

    callback.finished(result);
    return;
  }

  private List<FD> getFDsFromInput(final Collection<File> files) throws InterruptedException {
    if (BaseUtils.isEmpty(files)) {
      return new ArrayList<FD>(0);
    }
    final List<FD> result = new ArrayList<FD>();

    Processor<FD> fdProcessor = getFDProcessor();

    if (fdProcessor != null) {
      for (File file : files) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        try {
          result.addAll(fdProcessor.process(new FileInputStream(file)));
        } catch (FileNotFoundException ex) {
          throw new RuntimeException("File not found: " + file.getAbsolutePath(), ex);
        }
      }
    } else {
      LOG.error("No processor for functional dependencies has been found.");
    }
    return result;
  }

  private Processor<FD> getFDProcessor() {
    for (final Processor p : Lookup.getDefault().lookupAll(Processor.class)) {
      if (p.getResultType().equals(FD.class)) {
        return p;
      }
    }

    return null;
  }
}
