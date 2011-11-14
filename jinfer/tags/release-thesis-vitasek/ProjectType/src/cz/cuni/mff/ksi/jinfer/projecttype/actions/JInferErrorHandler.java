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
package cz.cuni.mff.ksi.jinfer.projecttype.actions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Line;
import org.openide.windows.IOProvider;
import org.openide.windows.IOSelect;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class for handling errors from {@link ValidateAction}.
 * @author sviro
 */
public class JInferErrorHandler extends DefaultHandler {
  /**
   * Title of Validation output window.
   */
  public static final String JINFER_VALIDATION_TITLE = "jInfer validation result";

  private static final Logger LOG = Logger.getLogger(ValidateAction.class);
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
  private Boolean result;
  private final FileObject file;

  private static class ValiadtionOutputListener implements OutputListener {

    private final FileObject file;
    private final int lineNumber;
    private final int columnNumber;

    public ValiadtionOutputListener(final FileObject file, final int lineNumber,
            final int columnNumber) {
      this.file = file;
      this.lineNumber = lineNumber;
      this.columnNumber = columnNumber;
    }

    @Override
    public void outputLineSelected(final OutputEvent ev) {
      //
    }

    @Override
    public void outputLineAction(final OutputEvent ev) {
      try {
        final DataObject dataObj = DataObject.find(file);

        final LineCookie lineCookie = dataObj.getCookie(LineCookie.class);
        final Line original = lineCookie.getLineSet().getOriginal(lineNumber - 1);
        original.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS, columnNumber - 1);
      } catch (final DataObjectNotFoundException ex) {
        LOG.error(ex);
        throw new RuntimeException(ex);
      }

    }

    @Override
    public void outputLineCleared(final OutputEvent ev) {
      //
    }
  }

  /**
   * Default constructor. If {@link FileObject} provided as parameter is not null,
   * error handler tries to print line and columnt number where the error occurs in provided file.
   * This information is printed in validation output window as a link to this file.
   * @param file FileObject for which some error occured.
   */
  public JInferErrorHandler(final FileObject file) {
    super();
    this.file = file;
    result = true;
  }

  private void printErrorMessage(final SAXParseException e) {
    if (result) {
      InputOutput ioResult = IOProvider.getDefault().getIO(JINFER_VALIDATION_TITLE, false);

      if (ioResult.isClosed()) {
        ioResult = IOProvider.getDefault().getIO(JINFER_VALIDATION_TITLE, true);
      }

      ioResult.getOut().print("[" + DATE_FORMAT.format(new Date()) + "] " + e.getMessage());

      final int realLineNum = e.getLineNumber();
      if (realLineNum > 0 && file != null) {
        try {
          ioResult.getOut().
                  println("(" + file.getName() + "." + file.getExt() + ":" + realLineNum + "/" + e.
                  getColumnNumber() + ")",
                  new ValiadtionOutputListener(file, realLineNum, e.getColumnNumber()));
        } catch (IOException ex) {
          LOG.error(ex);
        }
      } else {
        ioResult.getOut().println();
      }

      IOSelect.select(ioResult, EnumSet.allOf(IOSelect.AdditionalOperation.class));
      ioResult.getOut().close();
      result = false;
    }

  }

  @Override
  public void error(final SAXParseException e) throws SAXException {
    printErrorMessage(e);
    throw e;
  }

  @Override
  public void fatalError(final SAXParseException e) throws SAXException {
    printErrorMessage(e);
    throw e;
  }

  /**
   * Get result of the validation. If some error occurs, <tt>false</tt> is returned.
   * @return <tt>true</tt> if validation was successful, otherwise return <tt>false</tt>.
   */
  public Boolean getResult() {
    return result;
  }
}
