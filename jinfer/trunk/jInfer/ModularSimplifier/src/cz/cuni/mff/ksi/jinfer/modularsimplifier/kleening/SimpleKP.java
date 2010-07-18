/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.CPTrie;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * A simple Kleene processor - if it finds more or equal than THRESHOLD
 * identical elements in a row, it replaces them with their Kleene star.
 * 
 * @author vektor
 */
public class SimpleKP implements KleeneProcessor {

  private static Logger LOG = Logger.getLogger(KleeneProcessor.class);
  private final int threshold = Integer.parseInt(RunningProject.getActiveProjectProps()
          .getProperty("modularsimplifier.kleene.repetitions", "3"));

  @Override
  public List<AbstractNode> kleeneProcess(final List<AbstractNode> rules) {
    LOG.info("Simplifier: " + threshold + " and more repetitions will be collapsed to a Kleene star.");

    final List<AbstractNode> ret = new ArrayList<AbstractNode>(rules.size());
    for (final AbstractNode root : rules) {
      final Element e = (Element) root;
      ret.add(new Element(
              e.getContext(),
              e.getName(),
              e.getAttributes(),
              processTree(e.getSubnodes())));
    }
    return ret;
  }

  private Regexp<AbstractNode> processTree(final Regexp<AbstractNode> root) {
    switch (root.getType()) {
      case TOKEN:
        return root;
      case CONCATENATION:
        return processConcat(root);
      case ALTERNATION:
        final List<Regexp<AbstractNode>> newChildren = new ArrayList<Regexp<AbstractNode>>(root.getChildren().size());
        for (final Regexp<AbstractNode> branch : root.getChildren()) {
          newChildren.add(processConcat(branch));
        }
        return Regexp.getAlternation(newChildren);
      default:
        throw new IllegalArgumentException();
    }
  }

  private Regexp<AbstractNode> processConcat(final Regexp<AbstractNode> root) {
    if (root.isToken()) {
      return root;
    }
    if (!root.isConcatenation()) {
      throw new IllegalArgumentException();
    }
    final List<Regexp<AbstractNode>> retChildren = new ArrayList<Regexp<AbstractNode>>();

    int i = 0;
    int groupSize = 0;
    Regexp<AbstractNode> last = null;
    while (true) {
      if (i >= root.getChildren().size()) {
        closeGroup(last, groupSize, retChildren);
        break;
      }
      final Regexp<AbstractNode> current = root.getChild(i);
      if (equalTokenRegexps(last, current)) {
        // increment count
        groupSize++;
      } else {
        // close the last loop
        closeGroup(last, groupSize, retChildren);
        // start a new loop
        groupSize = 1;
        last = current;
      }

      i++;
    }
    return Regexp.getConcatenation(retChildren);
  }

  private boolean equalTokenRegexps(final Regexp<AbstractNode> last, final Regexp<AbstractNode> current) {
    return last != null
            && current != null
            && last.isToken()
            && current.isToken()
            && last.getContent().isElement()
            && current.getContent().isElement()
            && CPTrie.equalTokens(last, current);
  }

  private void closeGroup(final Regexp<AbstractNode> current,
          final int groupSize, final List<Regexp<AbstractNode>> retChildren) {
    if (groupSize == 0) {
      return;
    }
    if (groupSize < threshold) {
      for (int i = 0; i < groupSize; i++) {
        retChildren.add(current);
      }
      return;
    }
    final List<Regexp<AbstractNode>> kleeneChild = new ArrayList<Regexp<AbstractNode>>(1);
    kleeneChild.add(current);
    retChildren.add(new Regexp<AbstractNode>(null,
            kleeneChild,
            RegexpType.KLEENE));
  }
}
