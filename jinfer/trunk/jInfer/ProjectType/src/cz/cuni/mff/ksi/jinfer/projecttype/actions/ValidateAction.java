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

import cz.cuni.mff.ksi.jinfer.projecttype.nodes.FolderType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
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

  private static class JInferErrorHandler extends DefaultHandler {

    private Boolean result;

    public JInferErrorHandler() {
      super();
      result = true;
    }

    @Override
    public void error(final SAXParseException e) throws SAXException {
      final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer validation result", false);
      ioResult.getOut().println(e);
      ioResult.getOut().close();
      result = false;
    }

    @Override
    public void fatalError(final SAXParseException e) throws SAXException {
      result = false;
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
      final DataObject dObject = (DataObject) node.getLookup().lookup(DataObject.class);
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
    if ("xsd".equalsIgnoreCase(ext) || "dtd".equalsIgnoreCase(ext)) {
      return true;
    }

    return false;
  }

  private String getDTDRootNode(final File xmlFile) throws ParserConfigurationException, SAXException, IOException {
    final DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    builder = fact.newDocumentBuilder();
    final Document doc = builder.parse(xmlFile);
    return doc.getDocumentElement().getNodeName();
  }

  private boolean validateDTD(final FileObject xmlFile, final FileObject schemaFile) throws
          IOException, SAXException, ParserConfigurationException {
    DoctypeChangerStream changer = null;

    final InputStream inputStream = new FileInputStream(FileUtil.toFile(xmlFile));

    final String rootNode = getDTDRootNode(FileUtil.toFile(xmlFile));
    final String schemaText = schemaFile.asText();
    changer = new DoctypeChangerStream(inputStream);
    changer.setGenerator(new DoctypeGenerator() {

      @Override
      public Doctype generate(final Doctype dctp) {
        return new DoctypeImpl(rootNode, null, null, schemaText);
      }
    });


    if (changer != null) {
      final SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(true);
      SAXParser sp;

      sp = spf.newSAXParser();
      final JInferErrorHandler handler = new JInferErrorHandler();
      sp.parse(changer, handler);

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
        final Source source = new StreamSource(FileUtil.toFile(xmlFile));

        validator.validate(source);

        return true;
      }

    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (SAXException ex) {
      final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer validation result", false);
      ioResult.getOut().println(ex);
      ioResult.getOut().close();
      return false;
    } catch (ParserConfigurationException ex) {
      Exceptions.printStackTrace(ex);
    }
    return false;
  }

  @Override
  protected boolean enable(final Node[] activatedNodes) {
    boolean result = true;
    boolean isSchemaSelected = false;
    for (Node node : activatedNodes) {
      final boolean isSchemaNode = FolderType.SCHEMA.getName().equals(
              node.getParentNode().getDisplayName());

      if (isSchemaSelected && isSchemaNode) {
        return false;
      }

      if (!isSchemaSelected && isSchemaNode) {
        isSchemaSelected = true;
        continue;
      }

      final boolean isXMLNode = FolderType.XML.getName().equals(
              node.getParentNode().getDisplayName());

      if (!isXMLNode) {
        result = false;
      }
    }

    if (!isSchemaSelected) {
      return false;
    }

    return result;
  }

  @Override
  public String getName() {
    return "Validate...";
  }

  @Override
  public HelpCtx getHelpCtx() {
    return null;
  }
}
