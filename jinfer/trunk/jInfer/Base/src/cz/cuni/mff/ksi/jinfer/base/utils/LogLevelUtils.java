/*
 *  Copyright (C) 2010 sviro
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

import cz.cuni.mff.ksi.jinfer.base.options.LoggerPanel;
import java.awt.Color;
import org.apache.log4j.Level;
import org.openide.util.NbPreferences;

/**
 * Utility class for getting Colors of log levels set in options for Logger.
 * @author sviro
 */
public final class LogLevelUtils {

  private LogLevelUtils() {
  }
  public static final Color TRACE_DEFAULT = Color.GRAY;
  public static final Color DEBUG_DEFAULT = new Color(7, 105, 45);
  public static final Color INFO_DEFAULT = Color.BLACK;
  public static final Color WARN_DEFAULT = Color.ORANGE;
  public static final Color ERROR_DEFAULT = Color.RED;
  public static final Color FATAL_DEFAULT = Color.MAGENTA;

  /**
   * Get {@link Color} for {@link Level#TRACE} log level.
   * @return {@link Color} for {@link Level#TRACE} log level.
   */
  public static Color getColorTrace() {
    return Color.decode(NbPreferences.forModule(LoggerPanel.class).get(LoggerPanel.TRACE_COLOR, String.valueOf(TRACE_DEFAULT.getRGB())));
  }

  /**
   * Get {@link Color} for {@link Level#DEBUG} log level.
   * @return {@link Color} for {@link Level#DEBUG} log level.
   */
  public static Color getColorDebug() {
    return Color.decode(NbPreferences.forModule(LoggerPanel.class).get(LoggerPanel.DEBUG_COLOR, String.valueOf(DEBUG_DEFAULT.getRGB())));
  }

  /**
   * Get {@link Color} for {@link Level#INFO} log level.
   * @return {@link Color} for {@link Level#INFO} log level.
   */
  public static Color getColorInfo() {
    return Color.decode(NbPreferences.forModule(LoggerPanel.class).get(LoggerPanel.INFO_COLOR, String.valueOf(INFO_DEFAULT.getRGB())));
  }

  /**
   * Get {@link Color} for {@link Level#WARN} log level.
   * @return {@link Color} for {@link Level#WARN} log level.
   */
  public static Color getColorWarn() {
    return Color.decode(NbPreferences.forModule(LoggerPanel.class).get(LoggerPanel.WARN_COLOR, String.valueOf(WARN_DEFAULT.getRGB())));
  }

  /**
   * Get {@link Color} for {@link Level#FATAL} log level.
   * @return {@link Color} for {@link Level#FATAL} log level.
   */
  public static Color getColorError() {
    return Color.decode(NbPreferences.forModule(LoggerPanel.class).get(LoggerPanel.ERROR_COLOR, String.valueOf(ERROR_DEFAULT.getRGB())));
  }

  /**
   * Get {@link Color} for {@link Level#FATAL} log level.
   * @return {@link Color} for {@link Level#FATAL} log level.
   */
  public static Color getColorFatal() {
    return Color.decode(NbPreferences.forModule(LoggerPanel.class).get(LoggerPanel.FATAL_COLOR, String.valueOf(FATAL_DEFAULT.getRGB())));
  }
}
