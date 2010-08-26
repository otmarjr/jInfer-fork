/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.base.objects;

import cz.cuni.mff.ksi.jinfer.base.jaxb.ObjectFactory;
import cz.cuni.mff.ksi.jinfer.base.jaxb.Tfile;
import cz.cuni.mff.ksi.jinfer.base.jaxb.Tjinfer;
import cz.cuni.mff.ksi.jinfer.base.jaxb.Tqueries;
import cz.cuni.mff.ksi.jinfer.base.jaxb.Tschemas;
import cz.cuni.mff.ksi.jinfer.base.jaxb.Txml;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;

/**
 * Documents, schemas, queries etc constituting the input for inference.
 * Immutable class.
 * 
 * @author vektor
 */
public class Input {

  private final Collection<File> documents;
  private final Collection<File> schemas;
  private final Collection<File> queries;

  public Input(final Collection<File> documents,
          final Collection<File> schemas,
          final Collection<File> queries) {
    this.documents = documents;
    this.schemas = schemas;
    this.queries = queries;
  }

  public Input() {
    this(new ArrayList<File>(), new ArrayList<File>(), new ArrayList<File>());
  }

  /**
   * Removes files from collection which is not present on disk.
   *
   * @param collection Collection to be removed files from.
   */
  public static void removeNonExistFiles(final Collection<File> collection) {
    for (final Iterator<File> it = collection.iterator(); it.hasNext();) {
      final File file = it.next();
      if (!file.exists()) {
        it.remove();

        DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message(
                org.openide.util.NbBundle.getMessage(Input.class, "Input.deletedInputFiles.message"),
                NotifyDescriptor.INFORMATION_MESSAGE));
      }
    }

  }

  public Collection<File> getDocuments() {
    //removeNonExistFiles(documents);
    return documents;
  }

  public Collection<File> getSchemas() {
    //removeNonExistFiles(schemas);
    return schemas;
  }

  public Collection<File> getQueries() {
    //removeNonExistFiles(queries);
    return queries;
  }

  /**
   * Writes this input files to the output stream in a format suitable for loading
   * into an Input using the load(InputStream) method.
   *
   * @param fileOutputStream an output stream.
   */
  public void store(final FileOutputStream fileOutputStream) {
    final ObjectFactory objFactory = new ObjectFactory();

    final Tjinfer jinfer = objFactory.createTjinfer();

    final Txml Txml = objFactory.createTxml();
    jinfer.setXml(Txml);

    final Tschemas Tschemas = objFactory.createTschemas();
    jinfer.setSchemas(Tschemas);

    final Tqueries Tqueries = objFactory.createTqueries();
    jinfer.setQueries(Tqueries);

    writeCollection(objFactory, documents, Txml.getFile());
    writeCollection(objFactory, schemas, Tschemas.getFile());
    writeCollection(objFactory, queries, Tqueries.getFile());

    final JAXBElement<Tjinfer> jinferInput = objFactory.createJinferinput(jinfer);

    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tjinfer.class.getClassLoader());
    try {
      final JAXBContext context = JAXBContext.newInstance(Tjinfer.class.getPackage().getName());
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(jinferInput, fileOutputStream);
    } catch (JAXBException ex) {
      Exceptions.printStackTrace(ex);
    } finally {
      Thread.currentThread().setContextClassLoader(orig);
    }

    if (fileOutputStream != null) {
      try {
        fileOutputStream.close();
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }

  }

  private void readCollection(final List<Tfile> inputFiles, final Collection<File> collection) {
    for (Tfile tfile : inputFiles) {
      final File file = new File(tfile.getLoc());
      if (file.exists()) {
        collection.add(file);
      }
    }
  }

  private void writeCollection(final ObjectFactory objFactory, final Collection<File> collection,
          final List<Tfile> OutputFiles) {
    for (File file : collection) {
      final Tfile fileType = objFactory.createTfile();
      fileType.setLoc(file.getAbsolutePath());
      OutputFiles.add(fileType);
    }
  }

  /**
   * Reads an Input from the input byte stream.
   *
   * @param inputStream the input stream.
   * @throws IOException if an error occurred when reading from the input stream.
   */
  public void load(final InputStream inputStream) throws IOException {
    final ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(Tjinfer.class.getClassLoader());
    try {
      final JAXBContext context = JAXBContext.newInstance(Tjinfer.class.getPackage().getName());
      final Unmarshaller unmarshaller = context.createUnmarshaller();
      final JAXBElement<Tjinfer> jinferElement = (JAXBElement<Tjinfer>) unmarshaller.unmarshal(
              inputStream);
      final Tjinfer jinfer = jinferElement.getValue();

      readCollection(jinfer.getXml().getFile(), documents);
      readCollection(jinfer.getSchemas().getFile(), schemas);
      readCollection(jinfer.getQueries().getFile(), queries);

      if (inputStream != null) {
        inputStream.close();
      }

    } catch (JAXBException ex) {
      Exceptions.printStackTrace(ex);
    } finally {
      Thread.currentThread().setContextClassLoader(orig);
    }

  }
}
