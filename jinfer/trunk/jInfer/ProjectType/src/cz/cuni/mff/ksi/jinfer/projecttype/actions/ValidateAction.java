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
import org.apache.log4j.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Action for validating input XML files against DTD or XML Schema. This action
 * is availible from context menu of input/output files only if there is at least
 * one XML file and exactly one schema file(DTD or XML schema) selected.
 * 
 * @author sviro
 */
public final class ValidateAction extends NodeAction {

  private static final long serialVersionUID = 3123414;

  private static final Logger LOG = Logger.getLogger(ValidateAction.class);
  private static ValidateAction action = null;
  private final String[] schemas = new String[]{"xsd", "dtd"};

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
    final DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();

    final JInferErrorHandler errorHandler = new JInferErrorHandler(null);


    final String schemaText = schemaFile.asText();
    final String schema = schemaText.replaceFirst("<\\?xml.*?\\?>", "");

    final InputStream inputStream = new FileInputStream(FileUtil.toFile(xmlFile));
    final DoctypeChangerStream changer = new DoctypeChangerStream(inputStream);
    changer.setGenerator(new DoctypeGenerator() {

      @Override
      public Doctype generate(final Doctype dctp) {
        if (dctp != null && dctp.getRootElement() != null) {
          return new DoctypeImpl(dctp.getRootElement(), null, null, schema);
        }

        DocumentBuilder builder;
        try {
          fact.setValidating(false);
          builder = fact.newDocumentBuilder();
          builder.setErrorHandler(errorHandler);
          final Document doc = builder.parse(FileUtil.toFile(xmlFile));
          final String rootNode = doc.getDocumentElement().getNodeName();
          return new DoctypeImpl(rootNode, null, null, schema);
        } catch (ParserConfigurationException ex) {
          LOG.error(ex);
        } catch (SAXException ex) { //NOPMD
          //nothing to do
        } catch (IOException ex) {  //NOPMD
          //nothing to do
        }
        return dctp;
      }
    });

    if (changer != null) {

      fact.setNamespaceAware(true);
      fact.setValidating(true);
      final DocumentBuilder builder = fact.newDocumentBuilder();
      builder.setErrorHandler(errorHandler);
      builder.parse(changer);

      return errorHandler.getResult();
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
        factory.setErrorHandler(new JInferErrorHandler(schemaFile));

        final Schema schema = factory.newSchema(FileUtil.toFile(schemaFile));

        final Validator validator = schema.newValidator();
        final JInferErrorHandler errorHandler = new JInferErrorHandler(xmlFile);
        validator.setErrorHandler(errorHandler);
        final Source source = new StreamSource(FileUtil.toFile(xmlFile));
        validator.validate(source);

        return errorHandler.getResult();
      }

    } catch (IOException ex) {
      LOG.error(ex);
      //throw new RuntimeException(ex);
    } catch (SAXException ex) { //NOPMD
      //do nothing, error hanlder catch all SAXException
    } catch (ParserConfigurationException ex) {
      LOG.error(ex);
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

      final boolean isSchemaNode = isSchemaFile(node);

      if (isSchemaNode && isSchemaSelected) {
        return false;
      }

      if (isSchemaNode && !isSchemaSelected) {
        isSchemaSelected = true;
        continue;
      }

      final boolean isXMLNode = FolderType.DOCUMENT.getName().equals(
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
