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

import cz.cuni.mff.ksi.jinfer.attrstats.options.AttrStatsPanel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import org.openide.util.NbPreferences;

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
    return NbPreferences.forModule(AttrStatsPanel.class).get(AttrStatsPanel.BINARY_PATH_PROP, AttrStatsPanel.BINARY_PATH_DEFAULT);
  }

  /**
   * Verifies whether the GLPK binary is valid. See
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
   * Verifies whether the provided path to GLPK binary is valid. See
   * {@link FileUtils#isBinaryValid(String, String, String, boolean) } for details.
   *
   * @param binaryPath Path to the binary to check.
   * @return <code>True</code> if the binary found at path provided is a valid
   * GLPK Solver binary, <code>false</code> otherwise.
   */
  public static boolean isBinaryValid(final String binaryPath) {
    return FileUtils.isBinaryValid(binaryPath, "-v", "GLPSOL: GLPK LP/MIP Solver", true);
  }

  /**
   * Returns a "name" for the provided attribute mapping.
   *
   * @param mapping Mapping to get a name for.
   * @return Name of the provided mapping constructed in the following fashion:
   * <code>element name-attribute name</code>
   */
  public static String getName(final AttributeMappingId mapping) {
    final String name = mapping.getElement() + "-" +mapping.getAttribute();
    return name.replace(':', '-');
  }

}
