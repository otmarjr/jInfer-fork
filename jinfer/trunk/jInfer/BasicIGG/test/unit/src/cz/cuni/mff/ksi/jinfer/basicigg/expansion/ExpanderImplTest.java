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
package cz.cuni.mff.ksi.jinfer.basicigg.expansion;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.EqualityUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the expander.
 * Basically, we want to test:
 * <ul>
 *   <li>Elements with subnodes that are already simple regexp must not be expanded.</li>
 *   <li>Everything else must be expanded.</li>
 * </ul>
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class ExpanderImplTest {

  @Test(expected = IllegalArgumentException.class)
  public void testExpandNull() {
    System.out.println("expandNull");
    new ExpanderImpl().expand(null);
  }

  @Test
  public void testExpandNoOp() {
    System.out.println("expandNoOp");
    final List<Element> grammar = new ArrayList<Element>();

    final List<Regexp<AbstractStructuralNode>> okChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getLambda())));
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getLambda())));
    okChildren.add(Regexp.<AbstractStructuralNode>getToken(
            TestUtils.getElement("e3", Regexp.<AbstractStructuralNode>getLambda())));

    final Element e1 = TestUtils.getElement("e1", Regexp.getConcatenation(okChildren));
    final Element e2 = TestUtils.getElement("e2", Regexp.getConcatenation(okChildren));

    assertNotSame(e1, e2);

    grammar.add(e1);

    final List<Element> ret1 = new ExpanderImpl().expand(grammar);
    assertTrue(EqualityUtils.sameElements(e1, ret1.get(0), EqualityUtils.IGNORE_METADATA));

    grammar.add(e2);

    final List<Element> ret2 = new ExpanderImpl().expand(grammar);
    assertTrue(EqualityUtils.sameElements(e1, ret2.get(0), EqualityUtils.IGNORE_METADATA));
    assertTrue(EqualityUtils.sameElements(e2, ret2.get(1), EqualityUtils.IGNORE_METADATA));
  }

  private static final String[] EXPANDED = {
    "ABCFFF",
    "AAABCDEFFFFF",
    "ABCDEDEDEGGG",
    "AAABCGGGGG",
    "ABCDEHIHIHI",
    "AAABCDEDEDEHIHIHIHIHI"
  };

  /**
   * Tests a real beauty of a regexp: 
   * <code>(A+, B, C, (D, E)*, (F | G | (H, I)){3, 5})</code>.
   * The surrounding element is not interesting and the whole grammar consist of
   * only one element.
   *
   * <p>
   * Words for each child of the enclosing concatenation are:
   * <ul>
   *   <li><code>A+</code>: <code>{A, AA}</code></li>
   *   <li><code>B, C</code>: <code>{BC}</code></li>
   *   <li><code>(D, E)*</code>: <code>{λ, DE, DEDEDE}</code></li>
   *   <li><code>(F | G | (H, I)){3, 5}</code>: <code>{FFF, FFFFF, GGG, GGGGG, HIHIHI, HIHIHIHIHI}</code></li>
   * </ul>
   * </p>
   *
   * <p>
   * A total of 6 words (elements) should be generated
   * (the last child consists of 6 words).
   * These words should be:
   * <ol>
   *   <li><code>ABCFFF</code></li>
   *   <li><code>AAABCDEFFFFF</code></li>
   *   <li><code>ABCDEDEDEGGG</code></li>
   *   <li><code>AAABCGGGGG</code></li>
   *   <li><code>ABCDEHIHIHI</code></li>
   *   <li><code>AAABCDEDEDEHIHIHIHIHI</code></li>
   * </or>
   * </p>
   */
  @Test
  public void testExpand() {
    System.out.println("expand");

    final List<Regexp<AbstractStructuralNode>> innerConcatChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    innerConcatChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("D")));
    innerConcatChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("E")));

    final List<Regexp<AbstractStructuralNode>> alternationChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    alternationChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("F")));
    alternationChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("G")));

    final List<Regexp<AbstractStructuralNode>> altConcatChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    altConcatChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("H")));
    altConcatChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("I")));

    alternationChildren.add(
            Regexp.<AbstractStructuralNode>getConcatenation(altConcatChildren));


    final List<Regexp<AbstractStructuralNode>> concatChildren = new ArrayList<Regexp<AbstractStructuralNode>>();

    concatChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("A"), RegexpInterval.getKleeneCross()));
    concatChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("B")));
    concatChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("C")));
    
    concatChildren.add(
            Regexp.<AbstractStructuralNode>getConcatenation(innerConcatChildren, RegexpInterval.getKleeneStar()));

    concatChildren.add(
            Regexp.<AbstractStructuralNode>getAlternation(alternationChildren, RegexpInterval.getBounded(3, 5)));


    final Regexp<AbstractStructuralNode> subnodes = Regexp.getConcatenation(concatChildren);

    final Element e = TestUtils.getElement("e", subnodes);

    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(e);
    final List<Element> result = new ExpanderImpl().expand(grammar);
    assertEquals(6, result.size());

    for (int i = 0; i < 6; i++) {
      final Element element = result.get(i);
      final String expected = EXPANDED[i];
      System.out.println("Iteration " + i + ", expecting " + expected);
      assertEquals(expected, subnodesToString(element));
    }
  }

  private static String subnodesToString(final Element e) {
    final StringBuilder ret = new StringBuilder();
    for (final Regexp<AbstractStructuralNode> r : e.getSubnodes().getChildren()) {
      ret.append(r.getContent().getName());
    }
    return ret.toString();
  }
}
