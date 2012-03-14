/*
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

/*
 * This code originates from Jiří Schejbal's master thesis. Jiří Schejbal
 * is also the author of the original version of this code.
 * With his approval, we use his code in jInfer and we slightly modify it to
 * suit our cause.
 */
package cz.cuni.mff.ksi.jinfer.xqueryimporter.xqanalyzer;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ModuleNode;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This class converts the XQuery programs into their XML representation.
 * 
 * The class uses the XQuery lexical scanner and XQuery parser for conversion.
 * The XQuery comments are stripped from input character stream to avoid the
 * problems with lexical analysis.
 *
 * @author Jiri Schejbal
 *
 * @see XQLexer
 * @see XQParser
 * @see XQCommentStripReader
 */
public class XQConverter {

  /**
   * Reference to XQuery parser.
   */
  private XQParser parser;
  /**
   * Reference to reader that strips XQuery comments from input stream.
   */
  private XQCommentStripReader commentReader;

  /**
   * XML validator of created XML representation.
   */
  //private Validator validator = null;
  /**
   * Creates a new XQueryConverter component.
   *
   * It also creates the XQuery lexical scanner and XQuery parser and connects
   * them to the specified reader.
   *
   * @param reader Reader containing an XQuery program.
   */
  public XQConverter(Reader reader) {
    commentReader = new XQCommentStripReader(reader);
    parser = new XQParser(new XQLexer(commentReader));
  }

  /**
   * Creates a new XQueryConverter component.
   *
   * It also creates the XQuery lexical scanner and XQuery parser and connects
   * them to the specified reader.
   *
   * @param inputStream Input stream containing an XQuery program.
   */
  public XQConverter(InputStream inputStream) {
    this(new InputStreamReader(inputStream));
  }

  /**
   * Converts XQuery program into its XML representation.
   *
   * Parses the XQuery program on the input specified by constructor. The
   * created XML representation can be reached by method
   * <code>getDocument()</code>.
   *
   * @throws XQParseException if scanning or parsing fails.
   */
  public void convert() throws XQParseException {
    try {
      parser.parse();
    } catch (XQParseException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new XQParseException(parser.getErrorLine(),
              parser.getErrorColumn(), ex.getMessage());
    }
    if (parser.parsingFailed()) {
      throw new XQParseException(parser.getErrorLine(),
              parser.getErrorColumn(), parser.getFailMsg());
    }
    if (commentReader.getNesting() > 0) {
      throw new XQParseException(-1, -1, "Unterminated comment.");
    }
  }

  /**
   * Creates XML validator for XML representation.
   */
  /*private void createValidator() {
  SchemaFactory factory =
  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
  Source schemaSource =
  new StreamSource(getClass().getResourceAsStream(schemaFile));
  Schema schema = null;
  try {
  schema = factory.newSchema(schemaSource);
  } catch (SAXException ex) {
  throw new Error(ex);
  }
  validator = schema.newValidator();
  }*/
  /**
   * Validates created XML representation againts its XML schema.
   *
   * @throws IOException if the validator causes IOException.
   * @throws SAXException if the validator causes SAXException.
   */
  /*public void validate() throws IOException, SAXException {
  if (validator == null) {
  createValidator();
  }
  validator.validate(new DOMSource(getDocument()));
  }*/
  /**
   * Gets XML representation in form ox XML document.
   *
   * If this method is called before method <code>convert</code>, an empty
   * document is returned.
   *
   * @return XML representation of XQuery program on the input.
   */
  /*public Document getDocument() {
  return parser.getDocument();
  }*/
  public ModuleNode getModuleNode() {
    return parser.getModuleNode();
  }
  /**
   * Writes the XML representation of XQuery program into the output stream.
   *
   * If this method is called before method <code>convert</code>, an empty
   * document is returned.
   *
   * @param outputStream The Output stream the XML representation is written
   *     into.
   *
   * @throws TransformerException If an unrecoverable error occurs
   *   during the course of the transformation of XML document.
   */
  /*public void writeDocument(
  OutputStream outputStream
  ) throws TransformerException {
  writeDocument(new OutputStreamWriter(outputStream));
  }*/
  /**
   * Writes the XML representation of XQuery program into thw writer.
   *
   * If this method is called before method <code>convert</code>, an empty
   * document is returned.
   *
   * @param writer The writer the XML representation is written to.
   *
   * @throws TransformerException If an unrecoverable error occurs
   *   during the course of the transformation of XML document.
   */
  /*public void writeDocument(Writer writer) throws TransformerException {
  TransformerFactory transformerFactory =
  TransformerFactory.newInstance();
  Transformer transformer = transformerFactory.newTransformer();
  transformer.setOutputProperty("method", "xml");
  transformer.setOutputProperty("indent", "yes");
  transformer.setOutputProperty(
  "{http://xml.apache.org/xslt}indent-amount", "2"
  );
  transformer.transform(
  new DOMSource(getDocument()), new StreamResult(writer)
  );
  }*/
}
