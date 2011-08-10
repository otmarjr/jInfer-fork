/*
 * Copyright (C) 2011 anti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.contentinfering.pattern;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.ContentNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.SymbolToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.RegexpAutomatonSimplifierFactory;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class Pattern implements ContentInferrer {
  private static final Logger LOG = Logger.getLogger(Pattern.class);
  private final AutomatonSimplifier<String> automatonSimplifier;
  private final RegexpAutomatonSimplifier<String> regexpAutomatonSimplifier;

  public Pattern(AutomatonSimplifierFactory automatonSimplifierFactory, RegexpAutomatonSimplifierFactory regexpAutomatonSimplifierFactory) {
    this.automatonSimplifier = automatonSimplifierFactory.<String>create();
    this.regexpAutomatonSimplifier = regexpAutomatonSimplifierFactory.<String>create();
  }

  @Override
  public String inferContentType(List<ContentNode> nodes) throws InterruptedException {
    Automaton<String> pta = new Automaton<String>(true);
    for (ContentNode node : nodes) {
      if (node.getContent().size() > 1) {
        throw new IllegalArgumentException("Content must be in one line.");
      }
      if (node.getContent().size() == 1) {
        pta.buildPTAOnSymbol(stringToListOfChars(node.getContent().get(0)));
      } else {
        return "empty";
      }
    }
    LOG.fatal(pta);

    Automaton<String> simplified = automatonSimplifier.simplify(pta, new SymbolToString<String>() {
      @Override
      public String toString(String symbol) {
        return symbol;
      }
    });
    LOG.fatal(simplified);

    RegexpAutomaton<String> regexpAutomaton = new RegexpAutomaton<String>(simplified);
    Regexp<String> regexp = regexpAutomatonSimplifier.simplify(regexpAutomaton,
            new SymbolToString<Regexp<String>>() {
              @Override
              public String toString(Regexp<String> symbol) {
                return symbol.toString();
              }
            });
    return regexp.toString();
  }

  private List<String> stringToListOfChars(String S) {
    List<String> result = new ArrayList<String>(S.length());
    for (int i = 0; i < S.length(); i++) {
      result.add(S.substring(i, i + 1));
    }
    return result;
  }
}
