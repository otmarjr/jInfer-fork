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

package cz.cuni.mff.ksi.jinfer.trivialsimplifier.kleening;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.options.ConfigPanel;
import cz.cuni.mff.ksi.jinfer.trivialsimplifier.processing.CPTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * A simple Kleene processor - if it finds more or equal than THRESHOLD
 * identical elements in a row, it replaces them with their Kleene star.
 * 
 * @author vektor
 */
public class SimpleKP implements KleeneProcessor {

  private final int threshold;

  public SimpleKP() {
    threshold = Preferences.userNodeForPackage(ConfigPanel.class).getInt("kleene.repetitions", 3);
  }

  @Override
  public List<AbstractNode> kleeneProcess(final List<AbstractNode> rules) {
    final InputOutput io = IOProvider.getDefault().getIO("jInfer", false);
    io.getOut().println("Simplifier: " + threshold + " and more repetitions will be collapsed to a Kleene star.");

    final List<AbstractNode> ret = new ArrayList<AbstractNode>(rules.size());
    for (final AbstractNode root : rules) {
      final Element e  = (Element) root;
      ret.add(new Element(
              e.getContext(),
              e.getName(),
              e.getAttributes(), // TODO here we lose the attributes
              processTree(((Element) root).getSubnodes())));
    }
    return ret;
  }

  private Regexp<AbstractNode> processTree(final Regexp<AbstractNode> root) {
    switch (root.getType()) {
      case CONCATENATION:
        return processConcat(root);
      case ALTERNATION:
        final List<Regexp<AbstractNode>> newChildren = new ArrayList<Regexp<AbstractNode>>(root.getChildren().size());
        for (final Regexp<AbstractNode> branch : root.getChildren()) {
          newChildren.add(processConcat(branch));
        }
        return new Regexp<AbstractNode>(null, newChildren, RegexpType.ALTERNATION);
      default:
        throw new IllegalArgumentException();
    }
  }

  private Regexp<AbstractNode> processConcat(final Regexp<AbstractNode> root) {
    if (!RegexpType.CONCATENATION.equals(root.getType())) {
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
      final Regexp<AbstractNode> current = root.getChildren().get(i);
      if (equalRegexps(last, current)) {
        // increment count
        groupSize++;
      }
      else {
        // close the last loop
        closeGroup(last, groupSize, retChildren);
        // start a new loop
        groupSize = 1;
        last = current;
      }
      
      i++;
    }
    return new Regexp<AbstractNode>(null, retChildren, RegexpType.CONCATENATION);
  }

  private boolean equalRegexps(final Regexp<AbstractNode> last, final Regexp<AbstractNode> current) {
    if (last != null
            && current != null
            && RegexpType.TOKEN.equals(last.getType())
            && RegexpType.TOKEN.equals(current.getType())
            && NodeType.ELEMENT.equals(last.getContent().getType())
            && NodeType.ELEMENT.equals(current.getContent().getType())
            && CPTrie.equalTokens(last, current)) {
      return true;
    }
    return false;
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
