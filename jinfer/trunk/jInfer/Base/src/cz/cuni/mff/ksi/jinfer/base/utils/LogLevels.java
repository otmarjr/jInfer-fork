/*
 *  Copyright (C) 2010 reseto
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

import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Helper class providing static methods for easier integration of log4j logging.
 * @author reseto
 */
public final class LogLevels {

  private LogLevels() {
    
  }

  private static List<String> levels;

  public static List<String> getList() {
    levels = new ArrayList<String>();
    levels.add(Level.OFF.toString());
    levels.add(Level.FATAL.toString());
    levels.add(Level.ERROR.toString());
    levels.add(Level.WARN.toString());
    levels.add(Level.INFO.toString());
    levels.add(Level.DEBUG.toString());
    levels.add(Level.TRACE.toString());
    levels.add(Level.ALL.toString());
    return levels;
  }

  /**
   * Creates a combo box model with all allowed log levels sorted by severity.
   * Level OFF is at the top, followed by most severe FATAL. There are 8 levels taken from log4j.Level.
   * @return New DefaultComboBoxModel with all 8 levels supported by log4j.
   * @see javax.swing.DefaultComboBoxModel
   * @see org.apache.log4j.Level
   */
  public static ComboBoxModel getDefaultModel() {
    return new DefaultComboBoxModel(getList().toArray());
  }

  /**
   * Get the log level which is currently set for all outputs.
   * Messages with priority semantically lower than this level will be discarded.
   * This setting also defines the default log level for every module.
   * @return Current ROOT log level.
   */
  public static String getRootLogLevel() {
    return Logger.getRootLogger().getLevel().toString();
  }

  /**
   * Set a new log level for all outputs.
   * Messages with priority semantically lower than this level will be discarded.
   * This setting also defines the default log level for every module.
   * @param level New ROOT and default log level.
   */
  public static void setRootLogLevel(final String level) {
    Logger.getRootLogger().setLevel(Level.toLevel(level, Level.ALL));
  }

  /**
   * Get the log level that is written to file output which is currently set.
   * @return Log level for file output.
   */
  public static String getFileThreshold() {
    final Level lev = Level.toLevel(((AppenderSkeleton) Logger.getRootLogger().getAppender("file")).getThreshold().toInt());
    return lev.toString();
  }

  /**
   * Get the log level that is written to console output which is currently set.
   * @return Log level for console output.
   */
  public static String getConsoleThreshold() {
    final Level lev = Level.toLevel(((AppenderSkeleton) Logger.getRootLogger().getAppender("console")).getThreshold().toInt());
    return lev.toString();
  }

  /**
   * Set the log level for messages written to file output.
   * @param level New file log level.
   */
  public static void setFileThreshold(final String level) {
    ((AppenderSkeleton) Logger.getRootLogger().getAppender("file")).setThreshold(Level.toLevel(level, Level.WARN));
  }

  /**
   * Set the log level for messages written to console output.
   * @param level New console log level.
   */
  public static void setConsoleThreshold(final String level) {
    ((AppenderSkeleton) Logger.getRootLogger().getAppender("console")).setThreshold(Level.toLevel(level, Logger.getRootLogger().getLevel()));
  }
}
