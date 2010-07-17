package cz.cuni.mff.ksi.jinfer.projecttype;

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
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author sviro
 */
public class Input {

  private final Collection<FileObject> documents;
  private final Collection<FileObject> schemas;
  private final Collection<FileObject> queries;

  public Input(final Collection<FileObject> documents, final Collection<FileObject> schemas, final Collection<FileObject> queries) {
    this.documents = documents;
    this.schemas = schemas;
    this.queries = queries;
  }

  public Input() {
    this(new ArrayList<FileObject>(), new ArrayList<FileObject>(), new ArrayList<FileObject>());
  }

  public Collection<FileObject> getDocuments() {
    return documents;
  }

  public Collection<FileObject> getQueries() {
    return queries;
  }

  public Collection<FileObject> getSchemas() {
    return schemas;
  }

  public void store(final FileOutputStream fileOutputStream) {
    final PrintWriter out = new PrintWriter(fileOutputStream);

    writeCollection(documents, out);
    writeCollection(schemas, out);
    writeCollection(queries, out);

    if (out != null) {
      out.close();
    }

  }

  private void writeCollection(final Collection<FileObject> collection, final PrintWriter out) {
    for (final Iterator<FileObject> it = collection.iterator(); it.hasNext();) {
      final FileObject fileObject = it.next();
      out.print(FileUtil.toFile(fileObject).getAbsolutePath());
      if (it.hasNext()) {
        out.print(",");
      }
    }
    out.println();
  }

  private void readCollection(final Collection<FileObject> collection, final String line) {
    if ("".equals(line)) {
      return;
    }
    final String[] filePaths = line.split(",");

    for (String filePath : filePaths) {
      collection.add(FileUtil.toFileObject(FileUtil.normalizeFile(new File(filePath))));
    }
  }

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
