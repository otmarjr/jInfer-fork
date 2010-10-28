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

import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import net.socialchange.doctype.Doctype;
import net.socialchange.doctype.DoctypeChangerStream;
import net.socialchange.doctype.DoctypeGenerator;
import net.socialchange.doctype.DoctypeImpl;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.text.Line;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.openide.windows.IOProvider;
import org.openide.windows.IOSelect;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author sviro
 */
public final class ValidateAction extends NodeAction {

  private static ValidateAction action = null;
  private final String[] schemas = new String[]{"xsd", "dtd"};

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
    public void outputLineSelected(OutputEvent ev) {
      //
    }

    @Override
    public void outputLineAction(OutputEvent ev) {
      try {
        final DataObject dataObj = DataObject.find(file);

        final LineCookie lineCookie = dataObj.getCookie(LineCookie.class);
        final Line original = lineCookie.getLineSet().getOriginal(lineNumber - 1);
        original.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS, columnNumber - 1);
      } catch (DataObjectNotFoundException ex) {
        //TODO sviro
        Exceptions.printStackTrace(ex);
      }

    }

    @Override
    public void outputLineCleared(OutputEvent ev) {
      //
    }
  }

  private static class JInferErrorHandler extends DefaultHandler {

    private Boolean result;
    private final FileObject file;
    private final int lineDiff;

    public JInferErrorHandler(final FileObject file, final int lineDiff) {
      super();
      this.file = file;
      this.lineDiff = lineDiff;
      result = true;
    }

    @Override
    public void error(final SAXParseException e) throws SAXException {
      final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer validation result", false);
      printErrorMessage(ioResult, e);
      result = false;
    }

    @Override
    public void fatalError(final SAXParseException e) throws SAXException {
      final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer validation result", false);
      printErrorMessage(ioResult, e);
      result = false;
    }

    private void printErrorMessage(final InputOutput ioResult, final SAXParseException e) {
      final int realLineNum = e.getLineNumber() - lineDiff;
      if (realLineNum > 0) {
        ioResult.getOut().print(e.getMessage());

        try {
          ioResult.getOut().
                  println("(" + file.getName() + "." + file.getExt() + ":" + realLineNum + "/" + e.
                  getColumnNumber() + ")",
                  new ValiadtionOutputListener(file, realLineNum, e.getColumnNumber()));
        } catch (IOException ex) {
          // TODO sviro what to do here?? (previously anonym todo, given to sviro by vektor)
          ioResult.getOut().close();
        }
      }
      ioResult.getOut().close();
    }

    public Boolean getResult() {
      return result;
    }
  }

  private ValidateAction() {
    super();
  }

  public static synchronized ValidateAction getInstance() {
    if (action == null) {
      action = new ValidateAction();
    }

    return action;
  }

  @Override
  protected void performAction(final Node[] activatedNodes) {
    FileObject schemaFile = null;
    final List<FileObject> xmlFiles = new ArrayList<FileObject>();
    for (Node node : activatedNodes) {
      final DataObject dObject = node.getLookup().lookup(DataObject.class);
      if (dObject != null) {
        final FileObject fileObject = dObject.getPrimaryFile();
        if (isSchemaFile(fileObject)) {
          schemaFile = fileObject;
          continue;
        }

        xmlFiles.add(fileObject);
      }
    }

    for (FileObject fileObject : xmlFiles) {
      final boolean result = validate(fileObject, schemaFile);
      if (!result) {
        DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message(
                org.openide.util.NbBundle.getMessage(ValidateAction.class, "ValidateAction.notValid"),
                NotifyDescriptor.ERROR_MESSAGE));
        return;
      }
    }

    DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message(
            org.openide.util.NbBundle.getMessage(ValidateAction.class, "ValidateAction.valid"),
            NotifyDescriptor.INFORMATION_MESSAGE));

  }

  private boolean isSchemaFile(final FileObject file) {
    final String ext = file.getExt();
    return Arrays.asList(schemas).contains(ext);
  }

  private boolean validateDTD(final FileObject xmlFile, final FileObject schemaFile) throws
          IOException, SAXException, ParserConfigurationException {
    DoctypeChangerStream changer = null;

    final InputStream inputStream = new FileInputStream(FileUtil.toFile(xmlFile));

    final DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    builder = fact.newDocumentBuilder();
    final Document doc = builder.parse(FileUtil.toFile(xmlFile));

    final String rootNode = doc.getDocumentElement().getNodeName();
    String schemaText = schemaFile.asText();
    final String schema = schemaText.replaceFirst("<\\?xml.*?\\?>", "");

    changer = new DoctypeChangerStream(inputStream);
    changer.setGenerator(new DoctypeGenerator() {

      @Override
      public Doctype generate(final Doctype dctp) {
        return new DoctypeImpl(rootNode, null, null, schema);
      }
    });

    if (changer != null) {
      final int lineDiff = schemaFile.asLines().size() + (doc.getDoctype() == null ? 2 : 1) + 1;

      fact.setNamespaceAware(true);
      fact.setValidating(true);
      builder = fact.newDocumentBuilder();
      final JInferErrorHandler handler = new JInferErrorHandler(xmlFile, lineDiff);
      builder.setErrorHandler(handler);
      builder.parse(changer);

      return handler.getResult().booleanValue();
    }

    return false;
  }

  private boolean validate(final FileObject xmlFile, final FileObject schemaFile) {
    final String ext = schemaFile.getExt();
    String schemaLanguage = null;

    try {
      if ("xsd".equalsIgnoreCase(ext)) {
        schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      } else if ("dtd".equalsIgnoreCase(ext)) {
        return validateDTD(xmlFile, schemaFile);
      }

      if (schemaLanguage != null) {
        final SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage);

        final Schema schema = factory.newSchema(FileUtil.toFile(schemaFile));

        final Validator validator = schema.newValidator();
        final JInferErrorHandler errorHandler = new JInferErrorHandler(xmlFile, 0);
        validator.setErrorHandler(errorHandler);
        final Source source = new StreamSource(FileUtil.toFile(xmlFile));

        validator.validate(source);

        return errorHandler.getResult();
      }

    } catch (IOException ex) {
      //TODO sviro
      Exceptions.printStackTrace(ex);
    } catch (SAXException ex) {
      final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer validation result", false);
      ioResult.getOut().println(ex);
      IOSelect.select(ioResult, EnumSet.allOf(IOSelect.AdditionalOperation.class));
      ioResult.getOut().close();
      return false;
    } catch (ParserConfigurationException ex) {
      //TODO sviro
      Exceptions.printStackTrace(ex);
    }
    return false;
  }

  @Override
  protected boolean enable(final Node[] activatedNodes) {
    boolean result = true;
    boolean isXMLSelected = false;
    boolean isSchemaSelected = false;
    for (Node node : activatedNodes) {
      if (isFolder(node)) {
        return false;
      }

      boolean isSchemaNode = isSchemaFile(node);

      if (isSchemaNode && isSchemaSelected) {
        return false;
      }

      if (isSchemaNode && !isSchemaSelected) {
        isSchemaSelected = true;
        continue;
      }

      final boolean isXMLNode = FolderType.XML.getName().equals(
              node.getParentNode().getDisplayName());

      if (isXMLNode) {
        isXMLSelected = true;
      } else {
        result = false;
      }
    }

    if (!isSchemaSelected || !isXMLSelected) {
      return false;
    }

    return result;
  }

  private boolean isFolder(final Node node) {
    boolean result = false;

    final DataObject dataObject = node.getLookup().lookup(DataObject.class);
    if (dataObject != null) {
      final FileObject fileObject = dataObject.getPrimaryFile();
      if (fileObject.isFolder()) {
        result = true;
      }
    } else {
      result = true;
    }

    return result;
  }

  private boolean isSchemaFile(final Node node) {
    boolean result = false;

    final DataObject dataObject = node.getLookup().lookup(DataObject.class);
    if (dataObject != null) {
      final FileObject fileObject = dataObject.getPrimaryFile();
      if (fileObject.isData()) {
        result = isSchemaFile(fileObject);
      }
    }

    return result;
  }

  @Override
  public String getName() {
    return "Validate document vs. schema";
  }

  @Override
  public HelpCtx getHelpCtx() {
    return null;
  }
}
