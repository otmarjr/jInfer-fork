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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

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

  public Collection<File> getDocuments() {
    return documents;
  }

  public Collection<File> getSchemas() {
    return schemas;
  }

  public Collection<File> getQueries() {
    return queries;
  }

}
