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
package cz.cuni.mff.ksi.jinfer.projecttype;

import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.projecttype.jaxb.ObjectFactory;
import cz.cuni.mff.ksi.jinfer.projecttype.jaxb.Tfile;
import cz.cuni.mff.ksi.jinfer.projecttype.jaxb.Tjinfer;
import cz.cuni.mff.ksi.jinfer.projecttype.jaxb.Tqueries;
import cz.cuni.mff.ksi.jinfer.projecttype.jaxb.Tschemas;
import cz.cuni.mff.ksi.jinfer.projecttype.jaxb.Txml;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.UnmarshalException;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Class for storing and loading {@link Input} into/from project folder.
 * @author sviro
 */
public final class InputFiles {

  private static final Logger LOG = Logger.getLogger(InputFiles.class);

  private InputFiles() {
  }

  /**
   * Writes project {@link Input} to the output stream in a XML format.
   *
   * @param outputStream an output stream.
   * @param input Input to store into provided output stream.
   */
  public static void store(final OutputStream outputStream, final Input input) {
    final ObjectFactory objFactory = new ObjectFactory();

    final Tjinfer jinfer = objFactory.createTjinfer();

    final Txml Txml = objFactory.createTxml();
    jinfer.setXml(Txml);

    final Tschemas Tschemas = objFactory.createTschemas();
    jinfer.setSchemas(Tschemas);

    final Tqueries Tqueries = objFactory.createTqueries();
    jinfer.setQueries(Tqueries);

    writeCollection(objFactory, input.getDocuments(), Txml.getFile());
    writeCollection(objFactory, input.getSchemas(), Tschemas.getFile());
    writeCollection(objFactory, input.getQueries(), Tqueries.getFile());

    final JAXBElement<Tjinfer> jinferInput = objFactory.createJinferinput(jinfer);

    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tjinfer.class.getClassLoader());
    try {
      final JAXBContext context = JAXBContext.newInstance(Tjinfer.class.getPackage().getName());
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(jinferInput, outputStream);
    } catch (JAXBException ex) {
      LOG.error(ex);
    } finally {
      Thread.currentThread().setContextClassLoader(orig);
    }

    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (IOException ex) {
        LOG.error(ex);
      }
    }

  }

  /**
   * Add files from input.files xml representation to collection.
   *
   * @param inputFiles to be added to collection.
   * @param collection to add files to.
   */
  private static void readCollection(final List<Tfile> inputFiles, final Collection<File> collection) {
    for (Tfile tfile : inputFiles) {
      final File file = new File(tfile.getLoc());
      if (file.exists()) {
        collection.add(file);
      }
    }
  }

  /**
   * Add files from input into input.files xml representation.
   *
   * @param objFactory factory to create file type in xml.
   * @param collection to get files from.
   * @param OutputFiles list to add file type.
   */
  private static void writeCollection(final ObjectFactory objFactory, final Collection<File> collection,
          final List<Tfile> OutputFiles) {
    for (File file : collection) {
      final Tfile fileType = objFactory.createTfile();
      fileType.setLoc(file.getAbsolutePath());
      OutputFiles.add(fileType);
    }
  }

  /**
   * Loads and store {@link Input} from the XML input stream.
   *
   * @param inputStream the input stream.
   * @param input Input to store data.
   * @throws IOException if an error occurred when reading from the input stream.
   */
  public static void load(final InputStream inputStream, final Input input) throws IOException {
    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tjinfer.class.getClassLoader());
    try {
      final JAXBContext context = JAXBContext.newInstance(Tjinfer.class.getPackage().getName());
      final Unmarshaller unmarshaller = context.createUnmarshaller();
      @SuppressWarnings("unchecked")
      final JAXBElement<Tjinfer> jinferElement = (JAXBElement<Tjinfer>) unmarshaller.unmarshal(
              inputStream);
      final Tjinfer jinfer = jinferElement.getValue();

      readCollection(jinfer.getXml().getFile(), input.getDocuments());
      readCollection(jinfer.getSchemas().getFile(), input.getSchemas());
      readCollection(jinfer.getQueries().getFile(), input.getQueries());

      if (inputStream != null) {
        inputStream.close();
      }
    } catch (UnmarshalException ex) {
      DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message(
              org.openide.util.NbBundle.getMessage(Input.class, "Input.fileBroken.message"),
              NotifyDescriptor.INFORMATION_MESSAGE));
    } catch (JAXBException ex) {
      LOG.error(ex);
    } finally {
      Thread.currentThread().setContextClassLoader(orig);
    }

  }
}
