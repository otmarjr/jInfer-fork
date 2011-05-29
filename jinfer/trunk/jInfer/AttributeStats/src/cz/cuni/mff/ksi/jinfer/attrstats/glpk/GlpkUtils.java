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
 * TODO vektor Comment!
 *
 * @author vektor
 */
public final class GlpkUtils {

  private GlpkUtils() {

  }

  /**
   * TODO vektor Comment!
   * TODO vektor Make this configurable!
   *
   * @return
   */
  public static String getPath() {
    return "C:\\Program Files (x86)\\GnuWin32\\bin\\glpsol.exe";
  }

  public static boolean isBinaryValid() {
    return FileUtils.isBinaryValid(getPath(), "-v", "GLPSOL: GLPK LP/MIP Solver", false);
  }

  public static String getName(final AttributeMappingId mapping) {
    return mapping.getElement() + "-" +mapping.getAttribute();
  }

}
