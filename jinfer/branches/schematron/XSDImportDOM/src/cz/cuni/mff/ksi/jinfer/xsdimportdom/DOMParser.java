/*
 *  Copyright (C) 2010 reseto
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
package cz.cuni.mff.ksi.jinfer.xsdimportdom;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Expander;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.InterruptChecker;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDUtility;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class responsible for creating Initial Grammar rules from XSD Schema.
 * Provides XSD Schema parsing support using Xerces DOM parser from SUN.
 * <p>
 * Xerces DOM parser is used to build the DOM tree from a stream,
 * this DOM tree is then passed to {@link DOMHandler } class
 * to create rule-trees for each of the top level <i>element</i> tags.
 * After the rule-trees are complete, rules from each rule-tree are extracted.
 * The union of these extracted rules are the IG rules.
 * IG rules may contain complex regular expressions (depending on a particular schema)
 * and may need to be expanded using {@link Expander } before they can be simplified.
 * </p>
 * Please read package info.
 * @author reseto
 */
@ServiceProvider(service = XSDParser.class)
public class DOMParser implements XSDParser {

  /**
   * Internal name of the service provider.
   * This is the default parser for importing XSD Schemas.
   * Its name is used verbatim in XSDImportPropertiesPanel.
   * It's best not to change this name!
   */
  private static final String NAME = "DOMParser";
  /**
   * Visible name for this parser.
   */
  private static final String DISPLAY_NAME = "DOM Parser";

  private static final Logger LOG = Logger.getLogger(DOMParser.class);

  @Override
  public List<Element> parse(final InputStream stream) throws XSDException, InterruptedException {
    LOG.setLevel(XSDImportSettings.getLogLevel());

    final List<Element> rules = new ArrayList<Element>();

    if (stream != null) {
      try {
        final com.sun.org.apache.xerces.internal.parsers.DOMParser
          parser = new com.sun.org.apache.xerces.internal.parsers.DOMParser();

        parser.parse(new InputSource(stream));
        final Document doc = parser.getDocument();

        InterruptChecker.checkInterrupt();

        final DOMHandler handler = new DOMHandler();
        final List<Element> ruleTrees = handler.createRuleTrees(doc.getDocumentElement());

        for (Element el : ruleTrees) {
          final List<Element> elementRules = new ArrayList<Element>();
          rules.add(el);
          XSDUtility.getRulesFromElement(el, elementRules);
          rules.addAll(elementRules);
          InterruptChecker.checkInterrupt();
        }

        return rules;
      } catch (SAXException ex) {
        Exceptions.printStackTrace(ex);
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return rules;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getModuleDescription() {
    return NbBundle.getMessage(DOMParser.class, "Module.Description");
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }
}
