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
 *
 * @author reseto
 */
public final class LogLevels {

  // TODO reseto Comment!

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

  public static ComboBoxModel getDefaultModel() {
    return new DefaultComboBoxModel(getList().toArray());
  }

  public static String getRootLogLevel() {
    return Logger.getRootLogger().getLevel().toString();
  }

  public static void setRootLogLevel(final String level) {
    Logger.getRootLogger().setLevel(Level.toLevel(level, Level.ALL));
  }

  public static String getFileThreshold() {
    final Level lev = Level.toLevel(((AppenderSkeleton) Logger.getRootLogger().getAppender("file")).getThreshold().toInt());
    return lev.toString();
  }

  public static String getConsoleThreshold() {
    final Level lev = Level.toLevel(((AppenderSkeleton) Logger.getRootLogger().getAppender("console")).getThreshold().toInt());
    return lev.toString();
  }

  public static void setFileThreshold(final String level) {
    ((AppenderSkeleton) Logger.getRootLogger().getAppender("file")).setThreshold(Level.toLevel(level, Level.WARN));
  }

  public static void setConsoleThreshold(final String level) {
    ((AppenderSkeleton) Logger.getRootLogger().getAppender("console")).setThreshold(Level.toLevel(level, Logger.getRootLogger().getLevel()));
  }
}
