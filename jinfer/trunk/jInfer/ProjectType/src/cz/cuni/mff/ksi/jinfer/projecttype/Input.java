
package cz.cuni.mff.ksi.jinfer.projecttype;

import java.util.Collection;
import org.openide.filesystems.FileObject;

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

  public Collection<FileObject> getDocuments() {
    return documents;
  }

  public Collection<FileObject> getQueries() {
    return queries;
  }

  public Collection<FileObject> getSchemas() {
    return schemas;
  }

  
}
