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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

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
    final PrintWriter out = new PrintWriter(fileOutputStream);

    writeCollection(documents, out);
    writeCollection(schemas, out);
    writeCollection(queries, out);

    if (out != null) {
      out.close();
    }

  }

  /**
   * Writes Files from collection to defined print writer.
   *
   * @param collection of files to write.
   * @param out an print writer.
   */
  private void writeCollection(final Collection<File> collection, final PrintWriter out) {
    for (final Iterator<File> it = collection.iterator(); it.hasNext();) {
      final File file = it.next();
      out.print(file.getAbsolutePath());
      if (it.hasNext()) {
        out.print(",");
      }
    }
    out.println();
  }

  /**
   * Parse line and adds Files created from parsed file paths to collection.
   *
   * @param collection for adding Files from parsed line.
   * @param line to be parsed.
   */
  private void readCollection(final Collection<File> collection, final String line) {
    if ("".equals(line)) {
      return;
    }
    final String[] filePaths = line.split(",");

    for (String filePath : filePaths) {
      File file = new File(filePath);
      if (file.exists()) {
        collection.add(file);
      }
    }
  }

  /**
   * Reads an Input from the input byte stream.
   *
   * @param inputStream the input stream.
   * @throws IOException if an error occurred when reading from the input stream.
   */
  public void load(final InputStream inputStream) throws IOException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    String l;
    int i = 0;
    while ((l = reader.readLine()) != null) {
      if (i == 0) {
        readCollection(documents, l);
      }
      if (i == 1) {
        readCollection(schemas, l);
      }
      if (i == 2) {
        readCollection(queries, l);
      }

      ++i;
    }

    if (reader != null) {
      reader.close();
    }
  }
}
