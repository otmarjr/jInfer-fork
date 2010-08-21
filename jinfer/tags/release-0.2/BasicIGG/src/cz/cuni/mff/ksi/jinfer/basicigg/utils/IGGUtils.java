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
package cz.cuni.mff.ksi.jinfer.basicigg.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for IGG.
 *
 * @author vektor
 */
public final class IGGUtils {

  private IGGUtils() {
  }
  public static final Map<String, Object> ATTR_FROM_SCHEMA = new HashMap<String, Object>(1);

  static {
    ATTR_FROM_SCHEMA.put("from.schema", Boolean.TRUE);
  }
  public static final Map<String, Object> ATTR_FROM_QUERY = new HashMap<String, Object>(1);

  static {
    ATTR_FROM_SCHEMA.put("from.query", Boolean.TRUE);
  }
}
