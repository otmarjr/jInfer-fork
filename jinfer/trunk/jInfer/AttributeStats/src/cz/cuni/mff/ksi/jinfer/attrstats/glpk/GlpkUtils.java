/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.attrstats.glpk;

import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;

/**
 * Utility class for GLPK binding.
 *
 * @author vektor
 */
public final class GlpkUtils {

  private GlpkUtils() {

  }

  /**
   * Returns the full path to the GLPK Solver binary in a String.
   * TODO vektor Make this configurable!
   *
   * @return Full path to the GLPK Solver binary.
   */
  public static String getPath() {
    return "C:\\Program Files (x86)\\GnuWin32\\bin\\glpsol.exe";
  }

  /**
   * Verifies, whether the GLPK binary is valid. See
   * {@link FileUtils#isBinaryValid(String, String, String, boolean) } for details.
   *
   * @return <code>True</code> if the binary found at path provided by
   * {@link GlpkUtils#getPath() } is a valid GLPK Solver binary, <code>false</code>
   * otherwise.
   */
  public static boolean isBinaryValid() {
    return FileUtils.isBinaryValid(getPath(), "-v", "GLPSOL: GLPK LP/MIP Solver", false);
  }

  /**
   * Returns a "name" for the provided attribute mapping.
   *
   * @param mapping Mapping to get a name for.
   * @return Name of the provided mapping constructed in the following fashion:
   * <code>element name-attribute name</code>
   */
  public static String getName(final AttributeMappingId mapping) {
    return mapping.getElement() + "-" +mapping.getAttribute();
  }

}
