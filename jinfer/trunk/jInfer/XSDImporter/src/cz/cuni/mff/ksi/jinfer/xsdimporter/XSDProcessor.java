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

import cz.cuni.mff.ksi.jinfer.advancedruledisplayer.AdvancedRuleDisplayer;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Expander;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDParser;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Class providing logic for IG retrieval from XSD schemas.
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * @author reseto
 */
@ServiceProvider(service = Processor.class)
public class XSDProcessor implements Processor {

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
  public List<Element> process(final InputStream stream) {
    final XSDImportSettings settings = new XSDImportSettings();

    LOG.setLevel(settings.logLevel());
    final XSDParser parser = settings.getParser();

    try {
      if (parser != null) {
        //SAX or DOM parser selected
        LOG.debug(NbBundle.getMessage(XSDProcessor.class, "Debug.ParsingMethod", parser.getDisplayName()));
        parser.process(stream);
        final List<Element> rules = parser.getRules();
        printDebugInfo(settings, rules, "AfterParsing");
        // TODO reseto: remove AdvRuleDisplayer and also the module dependency
        AdvancedRuleDisplayer.showRulesAsync("Parsed", new CloneHelper().cloneGrammar(rules), true);
        // if the next module cannot handle complex regexps, help it by expanding our result
        if (!RunningProject.getNextModuleCaps().getCapabilities().contains(Capabilities.CAN_HANDLE_COMPLEX_REGEXPS)) {
          // lookup expander
          final Expander expander = Lookup.getDefault().lookup(Expander.class);
          final List<Element> rulesExpanded = expander.expand(rules);
          printDebugInfo(settings, rulesExpanded, "AfterExpanding");
          //TODO remove
          AdvancedRuleDisplayer.showRulesAsync("Expanded", new CloneHelper().cloneGrammar(rulesExpanded), true);
          // return expanded
          return rulesExpanded;
        }
        // return not expanded rules
        return rules;
      } else {
        //no parser selected
        LOG.error(NbBundle.getMessage(XSDProcessor.class, "Error.NoParser"));
        return Collections.emptyList();
      }
    } catch (final XSDException e) {
      if (settings.stopOnError()) {
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
  private void printDebugInfo(final XSDImportSettings settings, final List<Element> rules, final String stateMessageName) {
    if (settings.isVerbose()) {
      LOG.debug(NbBundle.getMessage(XSDProcessor.class, "Debug.Rules." + stateMessageName + ".FullMsg", rules.size()));
      for (Element elem : rules) {
        LOG.debug(elem.toString());
      }
    } else {
      LOG.debug(NbBundle.getMessage(XSDProcessor.class, "Debug.Rules." + stateMessageName + ".ShortMsg", rules.size()));
    }
  }

  @Override
  public boolean processUndefined() {
    return false;
  }
}
