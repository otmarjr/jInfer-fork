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
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.io.IOException;
import java.io.OutputStream;
import org.netbeans.api.actions.Openable;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 * Handler for adding output files into jInfer project output folder.
 * @author sviro
 */
public class OutputHandlerImpl implements OutputHandler {

  private final JInferProject project;

  public OutputHandlerImpl(final JInferProject project) {
    this.project = project;
  }

  @Override
  public void addOutput(final String name, final String data, final String extension) {
    try {
      final FileObject outputFolder = project.getOutputFolder(true);
      int min = 1;
      while (true) {
        if (outputFolder.getFileObject(name + min, extension) == null) {
          break;
        }
        min++;
      }
      final FileObject output = outputFolder.createData(name + min, extension);
      final OutputStream out = output.getOutputStream();
      out.write(data.getBytes());
      out.flush();
      out.close();
      DataObject.find(output).getLookup().lookup(Openable.class).open();
      outputFolder.refresh();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }
}
