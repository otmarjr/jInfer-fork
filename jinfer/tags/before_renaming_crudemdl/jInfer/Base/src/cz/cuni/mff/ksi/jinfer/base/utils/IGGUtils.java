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
package cz.cuni.mff.ksi.jinfer.base.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities and constants for IGG.
 *
 * @author vektor
 */
public final class IGGUtils {

  private IGGUtils() {
  }

  /** Metadata string meaning "Node comes from a schema". */
  public static final String FROM_SCHEMA = "from.schema";
  /** Metadata string meaning "Node comes from a query". */
  public static final String FROM_QUERY = "from.query";
  /** Metadata string meaning "Node is a sentinel". */
  public static final String IS_SENTINEL = "is.sentinel";
  /** Metadata string meaning "Original schema data". */
  public static final String SCHEMA_DATA = "schema.data";

  /** Metadata map containing a single piece of metadata: "Node comes from a schema". */
  public static final Map<String, Object> ATTR_FROM_SCHEMA = new HashMap<String, Object>(1);

  static {
    ATTR_FROM_SCHEMA.put(FROM_SCHEMA, Boolean.TRUE);
  }

  /** Metadata map containing a single piece of metadata: "Node comes from a query". */
  public static final Map<String, Object> ATTR_FROM_QUERY = new HashMap<String, Object>(1);

  static {
    ATTR_FROM_QUERY.put(FROM_QUERY, Boolean.TRUE);
  }

  /** Metadata map containing a single piece of metadata: "Node is a sentinel". */
  public static final Map<String, Object> METADATA_SENTINEL = new HashMap<String, Object>(1);

  static {
    METADATA_SENTINEL.put(IS_SENTINEL, Boolean.TRUE);
  }

}
