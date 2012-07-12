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
package cz.cuni.mff.ksi.jinfer.xsdimporter;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Class providing logic for IG retrieval from XSD schemas.
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * @author reseto
 */
@ServiceProvider(service = Processor.class)
public class XSDProcessor implements Processor<Element> {

  private static final Logger LOG = Logger.getLogger(XSDProcessor.class);

  @Override
  public FolderType getFolder() {
    return FolderType.SCHEMA;
  }

  @Override
  public String getExtension() {
    return "xsd";
  }

  @Override
  public List<Element> process(final InputStream stream) throws InterruptedException {
    LOG.setLevel(XSDImportSettings.getLogLevel());
    final XSDParser parser = XSDImportSettings.getParser();

    try {
      if (parser != null) {
        //SAX or DOM parser selected
        LOG.info(NbBundle.getMessage(XSDProcessor.class, "Info.ParsingMethod", parser.getDisplayName()));
        final List<Element> rules = parser.parse(stream);
        printDebugInfo(rules, "AfterParsing");

        return rules;
      } else {
        //no parser selected
        LOG.error(NbBundle.getMessage(XSDProcessor.class, "Error.NoParser"));
        return Collections.emptyList();
      }
    } catch (final XSDException e) {
      if (XSDImportSettings.isStopOnError()) {
        throw new RuntimeException(NbBundle.getMessage(XSDProcessor.class, "Exception.Parsing"), e);
      } else {
        LOG.error(NbBundle.getMessage(XSDProcessor.class, "Error.IgnoreParsing"), e);
        return Collections.emptyList();
      }
    }
  }

  /**
   * Prints information about number of rules after a specified stage of execution.
   * Either only the number of rules is displayed, or if verbose setting is enabled,
   * full rules are printed to log output.
   * @param settings Current settings of the module, toggling the verbose option.
   * @param rules List of rules to be displayed.
   * @param stateMessageName Part of the name of the message that defines current execution stage
   * (values "AfterParsing" and "AfterExpanding" are defined in bundle).
   */
  private void printDebugInfo(final List<Element> rules, final String stateMessageName) {
    if (XSDImportSettings.isVerbose()) {
      LOG.info(NbBundle.getMessage(XSDProcessor.class, "Debug.Rules." + stateMessageName + ".FullMsg", rules.size()));
      for (Element elem : rules) {
        LOG.debug(elem.toString());
      }
    } else {
      LOG.info(NbBundle.getMessage(XSDProcessor.class, "Debug.Rules." + stateMessageName + ".ShortMsg", rules.size()));
    }
  }

  @Override
  public boolean processUndefined() {
    return false;
  }
  
  @Override
    public Class<?> getResultType() {
        return Element.class;
    }
}
