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
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.Tdependencies;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * providing logic for initial model retrieval from functional dependencies.
 * @author sviro
 */
@ServiceProvider(service = Processor.class)
public class FDProcessor implements Processor<FD> {

  private static final Logger LOG = Logger.getLogger(FDProcessor.class);

  @Override
  public FolderType getFolder() {
    return FolderType.FD;
  }

  @Override
  public String getExtension() {
    return "fd";
  }

  @Override
  public boolean processUndefined() {
    return false;
  }

  @Override
  public List<FD> process(final InputStream inputStream) throws InterruptedException {
    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tdependencies.class.getClassLoader()); //NOPMD
    try {
      final JAXBContext context = JAXBContext.newInstance(Tdependencies.class.getPackage().getName());
      final Unmarshaller unmarshaller = context.createUnmarshaller();
      @SuppressWarnings("unchecked")
      final JAXBElement<Tdependencies> dependenciesElement = (JAXBElement<Tdependencies>) unmarshaller.unmarshal(
              inputStream);
      final Tdependencies dependencies = dependenciesElement.getValue();
      return dependencies.getDependency();
    } catch (UnmarshalException ex) {
      LOG.error("File with functional dependencies is broken");
      throw new InterruptedException();
    } catch (JAXBException ex) {
      LOG.error(ex);
      throw new InterruptedException();
    } catch (IllegalArgumentException ex) {
      LOG.error("Input stream must not be null.");
      throw new InterruptedException();
    }finally {
      Thread.currentThread().setContextClassLoader(orig);
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException ex) {
        //
      }
    }
  }

  @Override
  public Class<?> getResultType() {
    return FD.class;
  }
}
