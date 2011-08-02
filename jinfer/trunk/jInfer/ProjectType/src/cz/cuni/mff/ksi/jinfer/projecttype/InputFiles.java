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
import cz.cuni.mff.ksi.jinfer.projecttype.jaxb.Tfds;
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
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 * Class for storing and loading {@link Input} into/from project folder.
 * @author sviro
 */
public final class InputFiles {

  private static final Logger LOG = Logger.getLogger(InputFiles.class);

  private InputFiles() {
  }

  /**
   * Writes project {@link Input} to the Output Stream in a XML format.
   *
   * @param outputStream The Output Stream.
   * @param input Input to store into provided Output Stream.
   */
  public static void store(final OutputStream outputStream, final Input input, final File projectDir) {
    final ObjectFactory objFactory = new ObjectFactory();

    final Tjinfer jinfer = objFactory.createTjinfer();

    final Txml xml = objFactory.createTxml();
    jinfer.setXml(xml);

    final Tschemas schemas = objFactory.createTschemas();
    jinfer.setSchemas(schemas);

    final Tqueries queries = objFactory.createTqueries();
    jinfer.setQueries(queries);

    final Tfds fds = objFactory.createTfds();
    jinfer.setFds(fds);

    writeCollection(objFactory, input.getDocuments(), xml.getFile(), projectDir);
    writeCollection(objFactory, input.getSchemas(), schemas.getFile(), projectDir);
    writeCollection(objFactory, input.getQueries(), queries.getFile(), projectDir);
    writeCollection(objFactory, input.getFunctionalDependencies(), fds.getFile(), projectDir);

    final JAXBElement<Tjinfer> jinferInput = objFactory.createJinferinput(jinfer);

    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tjinfer.class.getClassLoader()); //NOPMD
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
  private static void readCollection(final List<Tfile> inputFiles, final Collection<File> collection, final File projectDir) {
    for (Tfile tfile : inputFiles) {
      File file = new File(tfile.getLoc());
      if (!file.isAbsolute()) {
        file = new File(projectDir, file.getPath());
        file = FileUtil.normalizeFile(file);
      }

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
          final List<Tfile> OutputFiles, final File projectDir) {
    for (File file : collection) {
      final Tfile fileType = objFactory.createTfile();
      boolean isRelative = false;
      try {
        String canonicalPath = file.getCanonicalPath();
        String canonicalDirPath = projectDir.getCanonicalPath();
        if (file.exists() && canonicalPath.startsWith(canonicalDirPath)) {
          file = new File("./" + canonicalPath.substring(canonicalDirPath.length()));
          isRelative = true;
        }
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }

      if (isRelative) {
        fileType.setLoc(file.getPath());
      } else {
        fileType.setLoc(file.getAbsolutePath());
      }
      OutputFiles.add(fileType);
    }
  }

  /**
   * Loads and store {@link Input} from the XML Input Stream.
   *
   * @param inputStream The Input Stream.
   * @param input Input to store data.
   * @throws IOException if an error occurred when reading from the Input Stream.
   */
  public static void load(final InputStream inputStream, final Input input, final File projectDir) throws IOException {
    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tjinfer.class.getClassLoader()); //NOPMD
    try {
      final JAXBContext context = JAXBContext.newInstance(Tjinfer.class.getPackage().getName());
      final Unmarshaller unmarshaller = context.createUnmarshaller();
      @SuppressWarnings("unchecked")
      final JAXBElement<Tjinfer> jinferElement = (JAXBElement<Tjinfer>) unmarshaller.unmarshal(
              inputStream);
      final Tjinfer jinfer = jinferElement.getValue();

      readCollection(jinfer.getXml().getFile(), input.getDocuments(), projectDir);
      readCollection(jinfer.getSchemas().getFile(), input.getSchemas(), projectDir);
      readCollection(jinfer.getQueries().getFile(), input.getQueries(), projectDir);
      if (jinfer.getFds() != null) {
        readCollection(jinfer.getFds().getFile(), input.getFunctionalDependencies(), projectDir);
      }

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
