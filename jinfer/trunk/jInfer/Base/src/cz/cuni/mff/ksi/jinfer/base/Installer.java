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

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggingEvent;
import org.openide.modules.ModuleInstall;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

  private static final long serialVersionUID = 54612321l;
  
  private static final Logger LOG = Logger.getLogger(ModuleInstall.class);

  private class Log4jOutputWindowAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent le) {
      if (this.layout == null) {
        // TODO rio nejaky error
      }

      final String message = this.layout.format(le);
      final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
      io.getOut().print(message);
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
    // Configure log4j
    PropertyConfigurator.configure("log4j.properties");
    PatternLayout outputWindowLayout = new PatternLayout();
    outputWindowLayout.setConversionPattern("%p [%t] (%F:%L) - %m%n");
    Appender outputWindowAppender = new Log4jOutputWindowAppender();
    outputWindowAppender.setLayout(outputWindowLayout);
    Logger ROOTLOG = Logger.getRootLogger();
    ROOTLOG.addAppender(outputWindowAppender);
    LOG.info("Log initialized.");

    LOG.info("Base module loaded.");
  }

  @Override
  public boolean closing() {
    LOG.info("Closing Base module.");
    return true;
  }
}
