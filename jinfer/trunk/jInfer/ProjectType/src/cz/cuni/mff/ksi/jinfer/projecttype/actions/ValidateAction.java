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
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
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
import org.xml.sax.InputSource;
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
      result = new Boolean(true);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
      result = new Boolean(false);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
      result = new Boolean(false);
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

  private boolean validateDTD(final FileObject xmlFile, final FileObject schemaFile) {
    // rewrite input file...
    final StringWriter rewrite_out = new StringWriter();
    final XMLEventFactory xef = XMLEventFactory.newInstance();
    final XMLOutputFactory xof = XMLOutputFactory.newInstance();
    final XMLInputFactory xif = XMLInputFactory.newInstance();
    final XMLEventReader er;
    try {
      er = xif.createXMLEventReader(new StreamSource(FileUtil.toFile(xmlFile)));
      final XMLEventWriter ew = xof.createXMLEventWriter(rewrite_out);
      while (er.hasNext()) {
        XMLEvent e = er.nextEvent();
        if (e.isStartElement()) {
          // creatre new DTD
          DTD dtd = xef.createDTD("<!DOCTYPE jnlp"
                  + " SYSTEM \"" + FileUtil.toFile(schemaFile).toURI() + "\">");
          ew.add(dtd);
          ew.add(e);
          break;
        } else if (e instanceof DTD) {
          // skip original DTD
        } else {
          // write event as is
          ew.add(e);
        }
      }
      // write all left input events...
      ew.add(er);
      ew.flush();
      ew.close();
    } catch (XMLStreamException ex) {
      Exceptions.printStackTrace(ex);
    }


    // System.out.println(rewrite_out.toString());
    StringReader rewritten_in = new StringReader(rewrite_out.toString());
    // validate...
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setValidating(true);
    SAXParser sp;
    try {
      sp = spf.newSAXParser();
      InputSource is = new InputSource(rewritten_in);
      JInferErrorHandler handler = new JInferErrorHandler();
      sp.parse(is, handler);

      return handler.getResult().booleanValue();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (ParserConfigurationException ex) {
      Exceptions.printStackTrace(ex);
    } catch (SAXException ex) {
      Exceptions.printStackTrace(ex);
    }

    return false;
  }

  private boolean validate(final FileObject xmlFile, final FileObject schemaFile) {
    final String ext = schemaFile.getExt();
    String schemaLanguage = null;
    if ("xsd".equalsIgnoreCase(ext)) {
      schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;
    } else if ("dtd".equalsIgnoreCase(ext)) {
      //TODO sviro fix DTD validation
      return false;

      //return validateDTD(xmlFile, schemaFile);
    }

    if (schemaLanguage != null) {
      final SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage);
      try {
        final Schema schema = factory.newSchema(FileUtil.toFile(schemaFile));

        final Validator validator = schema.newValidator();
        final Source source = new StreamSource(FileUtil.toFile(xmlFile));

        validator.validate(source);

      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      } catch (SAXException ex) {
        final InputOutput ioResult = IOProvider.getDefault().getIO("jInfer validation result", false);
        ioResult.getOut().println(ex);
        ioResult.getOut().close();
        return false;
      }

      return true;
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
