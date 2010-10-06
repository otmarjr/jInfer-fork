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

import cz.cuni.mff.ksi.jinfer.base.objects.StructuralAbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.Shortener;
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

  private static final Logger LOG = Logger.getLogger(KleeneProcessor.class);
  private final int threshold;

  public SimpleKP(final int threshold) {
    this.threshold = threshold;
  }

  @Override
  public List<StructuralAbstractNode> kleeneProcess(final List<StructuralAbstractNode> rules)
          throws InterruptedException {
    LOG.info("Simplifier: " + threshold + " and more repetitions will be collapsed to a Kleene star.");

    final List<StructuralAbstractNode> ret = new ArrayList<StructuralAbstractNode>(rules.size());
    for (final StructuralAbstractNode root : rules) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final Element e = (Element) root;
      ret.add(new Shortener().simplify(new Element(
              e.getContext(),
              e.getName(),
              e.getMetadata(),
              processTree(e.getSubnodes()))));
    }
    return ret;
  }

  private Regexp<StructuralAbstractNode> processTree(final Regexp<StructuralAbstractNode> root) {
    switch (root.getType()) {
      case TOKEN:
        return root;
      case CONCATENATION:
        return processConcat(root);
      case ALTERNATION:
        final List<Regexp<StructuralAbstractNode>> newChildren = new ArrayList<Regexp<StructuralAbstractNode>>(root.getChildren().size());
        for (final Regexp<StructuralAbstractNode> branch : root.getChildren()) {
          newChildren.add(processConcat(branch));
        }
        return Regexp.getAlternation(newChildren);
      default:
        throw new IllegalArgumentException();
    }
  }

  private Regexp<StructuralAbstractNode> processConcat(final Regexp<StructuralAbstractNode> root) {
    if (root.isToken()) {
      return root;
    }
    if (!root.isConcatenation()) {
      throw new IllegalArgumentException();
    }
    final List<Regexp<StructuralAbstractNode>> retChildren = new ArrayList<Regexp<StructuralAbstractNode>>();
    final List<Regexp<StructuralAbstractNode>> buffer = new ArrayList<Regexp<StructuralAbstractNode>>();

    int i = 0;
    Regexp<StructuralAbstractNode> last = null;
    while (true) {
      if (i >= root.getChildren().size()) {
        closeGroup(buffer, retChildren);
        break;
      }
      final Regexp<StructuralAbstractNode> current = root.getChild(i);
      if (equalTokenRegexps(last, current)) {
        // increment count
        buffer.add(current);
      } else {
        // close the last loop
        closeGroup(buffer, retChildren);
        // start a new loop
        last = current;
        buffer.clear();
        buffer.add(current);
      }

      i++;
    }
    final Regexp<StructuralAbstractNode> r = Regexp.getMutable();
    r.setType(RegexpType.CONCATENATION);
    r.getChildren().addAll(retChildren);
    r.setInterval(RegexpInterval.getOnce());
    r.setImmutable();
    return r;
  }

  private static boolean equalTokenRegexps(final Regexp<StructuralAbstractNode> last,
          final Regexp<StructuralAbstractNode> current) {
    return last != null
            && current != null
            && last.isToken()
            && current.isToken()
            && last.getContent().isElement()
            && current.getContent().isElement()
            && BaseUtils.equalTokens(last, current);
  }

  private void closeGroup(final List<Regexp<StructuralAbstractNode>> buffer,
          final List<Regexp<StructuralAbstractNode>> retChildren) {
    if (BaseUtils.isEmpty(buffer)) {
      return;
    }
    if (buffer.size() < threshold) {
      retChildren.addAll(buffer);
      return;
    }
    // TODO vektor Accumulate!
    retChildren.add(new Regexp<StructuralAbstractNode>(
            buffer.get(0).getContent(), null,
            RegexpType.TOKEN, RegexpInterval.getKleeneStar()));
  }
}
