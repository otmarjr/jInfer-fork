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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
  /** Metadata string meaning "Attribute is required". */
  public static final String REQUIRED = "required";
  /** Metadata representing the name of the file where this node originated from. */
  public static final String FILE_ORIGIN = "file.origin";
  /**
   * Metadata string meaning "Attribute is ID".
   *
   * See <a href="http://www.w3.org/TR/REC-xml/#id ">DTD reference</a> and
   * <a href="http://www.w3.org/TR/xmlschema11-2/#ID">XSD reference</a> for
   * further information.
   */
  public static final String IS_ID = "is.ID";

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

  /**
   * Empty, unmodifiable context.
   */
  public static final List<String> EMPTY_CONTEXT = Collections.unmodifiableList(new ArrayList<String>(0));

  /**
   * Returns flag if the provided regexp is a simple concatenation.
   * Regexp is a simple concatenation if all these conditions hold:
   * <ul>
   *  <li>Its type is {@link RegexpType#CONCATENATION}.</li>
   *  <li>Its interval is "once" ({@link RegexpInterval#isOnce()}).</li>
   *  <li>
   *   For each of its children holds:
   *    <ul>
   *      <li>The child is {@link RegexpType#TOKEN}</li>
   *      <li>The child's interval is "once" ({@link RegexpInterval#isOnce()}).</li>
   *    </ul>
   *  </li>
   * </ul>
   *
   * @param r Regexp to be examined.
   * @return True if the regexp is a simple concatenation, false otherwise.
   */
  public static boolean isSimpleConcatenation(
          final Regexp<AbstractStructuralNode> r) {
    if (r == null) {
      throw new IllegalArgumentException("Regexp must not be null.");
    }

    if (!r.isConcatenation()) {
      return false;
    }
    if (!r.getInterval().isOnce()) {
      return false;
    }
    for (final Regexp<AbstractStructuralNode> child : r.getChildren()) {
      if (!child.isToken()) {
        return false;
      }
      if (!child.getInterval().isOnce()) {
        return false;
      }
    }
    return true;
  }
}
