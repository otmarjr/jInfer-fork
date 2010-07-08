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

  public Input(Collection<FileObject> documents, Collection<FileObject> schemas, Collection<FileObject> queries) {
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

  void store(FileOutputStream fileOutputStream) {
    PrintWriter out = new PrintWriter(fileOutputStream);

    writeCollection(documents, out);
    writeCollection(schemas, out);
    writeCollection(queries, out);

    if (out != null) {
      out.close();
    }

  }

  private void writeCollection(Collection<FileObject> collection, PrintWriter out) {
    for (Iterator<FileObject> it = collection.iterator(); it.hasNext();) {
      FileObject fileObject = it.next();
      out.print(FileUtil.toFile(fileObject).getAbsolutePath());
      if (it.hasNext()) {
        out.print(",");
      }
    }
    out.println();
  }

  private void readCollection(Collection<FileObject> collection, String line) {
    if ("".equals(line)) {
      return;
    }
    String[] filePaths = line.split(",");

    for (String filePath : filePaths) {
      collection.add(FileUtil.toFileObject(FileUtil.normalizeFile(new File(filePath))));
    }
  }

  void load(InputStream inputStream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

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
