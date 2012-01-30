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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk;

import cz.cuni.mff.ksi.jinfer.iss.options.ISSPanel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import org.apache.log4j.Logger;
import org.openide.util.NbPreferences;

/**
 * Utility class for GLPK binding.
 *
 * @author vektor
 */
public final class GlpkUtils {

  private static final String CMD_OPTS = "-v";
  private static final String EXPECTED = "GLPSOL: GLPK LP/MIP Solver";
  private static final String TMP = System.getProperty("java.io.tmpdir");
  public static final String INPUT = TMP + "/glpk_input.txt";
  public static final String OUTPUT = TMP + "/glpk_output.txt";

  private static final Logger LOG = Logger.getLogger(GlpkUtils.class);

  private GlpkUtils() {

  }

  /**
   * Returns the full path to the GLPK Solver binary in a String.
   *
   * @return Full path to the GLPK Solver binary.
   */
  public static String getPath() {
    return NbPreferences.forModule(ISSPanel.class).get(ISSPanel.BINARY_PATH_PROP, ISSPanel.BINARY_PATH_DEFAULT);
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
    return FileUtils.isBinaryValid(getPath(), CMD_OPTS, EXPECTED, false);
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
    return FileUtils.isBinaryValid(binaryPath, CMD_OPTS, EXPECTED, true);
  }

  /**
   * Returns the first line that GLPK returns when run with "-v" option,
   * hopefully containing the version of GLPK.
   *
   * @return String containing GLPK identification and version.
   * <code>null</code> if something goes wrong.
   */
  public static String getVersion() {
    return FileUtils.getBinaryResult(getPath(), CMD_OPTS);
  }

  /**
   * Returns a "name" for the provided attribute mapping.
   *
   * @param mapping Mapping to get a name for.
   *
   * @return Name of the provided mapping constructed in the following fashion:
   * <code>element name-attribute name</code>
   */
  public static String getName(final AttributeMappingId mapping) {
    return getName(mapping, '-');
  }

  /**
   * Returns a "name" for the provided attribute mapping, using the provided
   * separator character.
   *
   * @param mapping Mapping to get a name for.
   * @param separator Separator to use.
   *
   * @return Name of the provided mapping constructed in the following fashion:
   * <code>element name{separator}attribute name</code>
   */
  public static String getName(final AttributeMappingId mapping,
          final char separator) {
    final String name = mapping.getElement() + separator + mapping.getAttribute();
    return name
            .replace(':', separator)
            .replace('-', separator);
  }

  public static String[] getParameters(final int timeLimit) {
    if (timeLimit == 0) {
      LOG.info("Run time not limited.");
      return new String[] {
              GlpkUtils.getPath(),
              "--math",
              INPUT,
              "-o",
              OUTPUT
      };
    }
    LOG.info("Time limit set to " + timeLimit + " seconds.");
    return new String[] {
              GlpkUtils.getPath(),
              "--math",
              "--tmlim",
              String.valueOf(timeLimit),
              INPUT,
              "-o",
              OUTPUT
    };
  }
}
