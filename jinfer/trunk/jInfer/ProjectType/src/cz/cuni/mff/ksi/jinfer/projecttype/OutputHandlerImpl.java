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
package cz.cuni.mff.ksi.jinfer.projecttype;

import cz.cuni.mff.ksi.jinfer.base.interfaces.OutputHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.netbeans.api.actions.Openable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 * Handler for adding output files into jInfer project output folder.
 * @author sviro
 */
public class OutputHandlerImpl implements OutputHandler {

  private static final Logger LOG = Logger.getLogger(OutputHandler.class);
  private static final String NUMBER_PATTERN = "\\{n\\}";
  private static final String DATE_PATTERN = "\\{date\\}";
  private final JInferProject project;

  public OutputHandlerImpl(final JInferProject project) {
    this.project = project;
  }

  @Override
  public void addOutput(final String name, final String data, final String extension,
          final boolean showOutput) {
    OutputStream out = null;
    try {
      final FileObject outputFolder = project.getOutputFolder(true);
      String proccessedName = generateDate(name);
      proccessedName = generateNumbering(proccessedName, extension, outputFolder);
      FileObject output;
      try {
        output = outputFolder.createData(proccessedName, extension);
      } catch (IOException ex) {
        LOG.error(org.openide.util.NbBundle.getMessage(OutputHandlerImpl.class, "schemaName.exists", proccessedName, extension));

        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
              org.openide.util.NbBundle.getMessage(OutputHandlerImpl.class, "schemaName.exists", proccessedName, extension),
              NotifyDescriptor.ERROR_MESSAGE));

        return;
      }
      out = output.getOutputStream();
      out.write(data.getBytes());
      out.flush();
      out.close();
      if (showOutput) {
        DataObject.find(output).getLookup().lookup(Openable.class).open();
      }
      outputFolder.refresh();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    } 
  }

  private String generateDate(final String name) {
    final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
    final String date = df.format(new Date());

    return name.replaceAll(DATE_PATTERN, date);
  }

  private String generateNumbering(final String proccessedName, final String extension,
          final FileObject outputFolder) {
    if (proccessedName.contains("{n}")) {
      int min = 1;
      while (true) {
        if (outputFolder.getFileObject(proccessedName.replaceFirst(NUMBER_PATTERN, String.valueOf(
                min)), extension) == null) {
          break;
        }
        min++;
      }

      return proccessedName.replaceFirst(NUMBER_PATTERN, String.valueOf(min));
    } else {
      return proccessedName;
    }
  }
}
