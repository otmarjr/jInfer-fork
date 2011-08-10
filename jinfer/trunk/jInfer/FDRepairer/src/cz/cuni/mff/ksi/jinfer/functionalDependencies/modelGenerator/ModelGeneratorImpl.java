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
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.InitialModel;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.Path;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGenerator;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.ModelGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.weights.Tweight;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of the Model generator module.
 * @author sviro
 */
@ServiceProvider(service = ModelGenerator.class)
public class ModelGeneratorImpl implements ModelGenerator {

  private static final Logger LOG = Logger.getLogger(ModelGeneratorImpl.class);

  @Override
  public void start(Input input, ModelGeneratorCallback callback) throws InterruptedException {
    InitialModel result = new InitialModel();

    result.addFD(getFDFromInput(input.getFunctionalDependencies()));
    result.addTree(getDataFromInput(input.getDocuments()));
    getWeightsFromInput(input.getFunctionalDependencies(), result.getTrees());
    callback.finished(result);
  }

  private List<FD> getFDFromInput(final Collection<File> files) throws InterruptedException {
    if (BaseUtils.isEmpty(files)) {
      return Collections.<FD>emptyList();
    }
    final List<FD> result = new ArrayList<FD>();

    Processor<FD> processor = ModelGeneratorImpl.<FD>getProcessor(FD.class);

    if (processor != null) {
      for (File file : files) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }

        if (FileUtils.getExtension(file.getName()).equals(processor.getExtension())) {
          try {
            result.addAll(processor.process(new FileInputStream(file)));
          } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + file.getAbsolutePath(), ex);
          }
        }
      }
    } else {
      LOG.error("No processor for FD has been found.");
    }
    return result;
  }

  private List<RXMLTree> getDataFromInput(final Collection<File> documents) throws InterruptedException {
    if (BaseUtils.isEmpty(documents)) {
      return Collections.<RXMLTree>emptyList();
    }
    final List<RXMLTree> result = new ArrayList<RXMLTree>();

    Processor<RXMLTree> xmlProcessor = ModelGeneratorImpl.<RXMLTree>getProcessor(RXMLTree.class);
    Processor<Path> pathProcessor = ModelGeneratorImpl.<Path>getProcessor(Path.class);

    if (xmlProcessor != null && pathProcessor != null) {
      for (File file : documents) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
        try {
          List<RXMLTree> xmlTrees = xmlProcessor.process(new FileInputStream(file));
          if (!xmlTrees.isEmpty()) {
            xmlTrees.get(0).setPaths(pathProcessor.process(new FileInputStream(file)));
          }
          result.addAll(xmlTrees);
        } catch (FileNotFoundException ex) {
          LOG.error(ex);
          throw new InterruptedException("File not found: " + file.getAbsolutePath());
        }
      }
    } else {
      LOG.error("No processor for XML has been found.");
      throw new InterruptedException("No processor for Weights has been found.");
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private static <T> Processor<T> getProcessor(Class<?> clazz) {
    for (final Processor<?> p : Lookup.getDefault().lookupAll(Processor.class)) {
      if (p.getResultType().equals(clazz)) {
        return (Processor<T>)p;
      }
    }

    return null;
  }

  private void getWeightsFromInput(Collection<File> queries, List<RXMLTree> trees) throws InterruptedException {
    if (BaseUtils.isEmpty(queries) || BaseUtils.isEmpty(trees)) {
      return;
    }

    Processor<Tweight> weightProcessor = ModelGeneratorImpl.<Tweight>getProcessor(Tweight.class);
    if (weightProcessor != null) {
      for (File file : queries) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }

        if (FileUtils.getExtension(file.getName()).equals(weightProcessor.getExtension())) {
          try {
            List<Tweight> weights = weightProcessor.process(new FileInputStream(file));

            for (RXMLTree tree : trees) {
              tree.setWeights(weights);
            }
          } catch (FileNotFoundException ex) {
            LOG.error(ex);
            throw new InterruptedException("File not found: " + file.getAbsolutePath());
          }
        }
      }
    } else {
      LOG.error("No processor for Weights has been found.");
      throw new InterruptedException("No processor for Weights has been found.");
    }
  }
}
