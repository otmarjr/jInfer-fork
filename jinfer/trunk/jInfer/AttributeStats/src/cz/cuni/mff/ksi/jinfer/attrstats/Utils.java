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
package cz.cuni.mff.ksi.jinfer.attrstats;

import java.text.Format;
import java.text.NumberFormat;

/**
 * Attribute statistics module utils.
 *
 * @author vektor
 */
public final class Utils {

  private Utils() {

  }

  /**
   * Formatter that formats numbers to a maximum of 5 fraction digits.
   */
  public static final Format FORMAT = NumberFormat.getInstance();
  static {
    ((NumberFormat)FORMAT).setMaximumFractionDigits(5);
  }
  public static final String NA = "N/A";

  /**
   * Converts a boolean to {@link String} in form yes/no.
   *
   * @param b Boolean to be converted.
   * @return "yes" or "no".
   */
  public static String boolToString(final boolean b) {
    return b ? "yes" : "no";
  }

}
