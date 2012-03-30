/*
 *  Copyright (C) 2010 rio
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
package cz.cuni.mff.ksi.jinfer.base;

import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.LogLevelUtils;
import java.awt.Color;
import java.io.IOException;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.openide.modules.ModuleInstall;
import org.openide.windows.IOColorPrint;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 *
 * @author rio
 */
public class Installer extends ModuleInstall {

  private static final long serialVersionUID = 54612321l;
  private static Logger LOG;

  private static class Log4jOutputWindowAppender extends AppenderSkeleton {

    private Log4jOutputWindowAppender(final Layout layout) {
      super();
      this.setLayout(layout);
    }

    @Override
    protected void append(final LoggingEvent le) {
      final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
      if (io.isClosed()) {
        io.select();
      }

      final Color textColor = getLogLevelColor(le.getLevel());

      final String lgName = le.getLoggerName();
      final String message = le.getLevel().toString()
              + " [" + lgName.substring(lgName.lastIndexOf('.') + 1) + "]: "
              + this.layout.format(le);
      if (IOColorPrint.isSupported(io)) {
        try {
          IOColorPrint.print(io, message, textColor);
        } catch (IOException ex) {
          io.getOut().print(message);
          io.getOut().close();
        }
      } else {
        io.getOut().print(message);
        io.getOut().close();
      }
    }

    private Color getLogLevelColor(final Level level) {
      Color textColor;
      switch (level.toInt()) {
        case Level.TRACE_INT:
          textColor = LogLevelUtils.getColorTrace();
          break;
        case Level.DEBUG_INT:
          textColor = LogLevelUtils.getColorDebug();
          break;
        case Level.INFO_INT:
          textColor = LogLevelUtils.getColorInfo();
          break;
        case Level.WARN_INT:
          textColor = LogLevelUtils.getColorWarn();
          break;
        case Level.ERROR_INT:
          textColor = LogLevelUtils.getColorError();
          break;
        case Level.FATAL_INT:
          textColor = LogLevelUtils.getColorFatal();
          break;
        default:
          textColor = Color.BLACK;
          break;
      }
      return textColor;
    }

    @Override
    public boolean requiresLayout() {
      return true;
    }

    @Override
    public void close() {
      // nothing needed
    }
  }

  @Override
  public void restored() {
    /* configure log4j */
    final Logger ROOTLOG = Logger.getRootLogger();
    ROOTLOG.setLevel(Level.ALL);

    // configure appender to the Output window
    final EnhancedPatternLayout outputWindowLayout = new EnhancedPatternLayout("%m%n");
    final AppenderSkeleton outputWindowAppender = new Log4jOutputWindowAppender(outputWindowLayout);
    outputWindowAppender.setThreshold(ROOTLOG.getLevel());
    outputWindowAppender.setName("console");
    ROOTLOG.addAppender(outputWindowAppender);

    LOG = Logger.getLogger(Installer.class);

    // configure appender to a logfile
    final EnhancedPatternLayout fileLayout = new EnhancedPatternLayout(
            "(%d{dd MMM yyyy HH:mm:ss,SSS}) %p [%t] %c (%F:%L) - %m%n");
    try {
      final String logfileName = FileUtils.JINFER_DIR + "/jinfer.errors.log";
      final RollingFileAppender logfileAppender = new RollingFileAppender(fileLayout, logfileName);
      // keep max 1 old logfile
      logfileAppender.setMaxBackupIndex(1);
      // max file size if 100KB
      logfileAppender.setMaximumFileSize(100 * 1024);
      // log to file only errors and stronger levels
      logfileAppender.setThreshold(Level.WARN);
      logfileAppender.setName("file");

      ROOTLOG.addAppender(logfileAppender);

      LOG.info("Log initialized.");
    } catch (IOException exc) {
      LOG.error("Cannot log to file, logfile disabled.", exc);
    }

    LOG.info("Base module loaded.");
  }

  @Override
  public boolean closing() {
    LOG.info("Closing Base module.");
    return true;
  }
}
