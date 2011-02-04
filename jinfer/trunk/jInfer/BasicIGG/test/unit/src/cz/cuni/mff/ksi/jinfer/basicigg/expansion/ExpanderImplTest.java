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

import java.util.Arrays;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.EqualityUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.ArrayList;
import java.util.Collections;
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

  private static final String[] CONCAT_EXPANDED = {
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
   *   <li><code>A+</code>: <code>{A, AAA}</code></li>
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
      final String expected = CONCAT_EXPANDED[i];
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

  private static final String[] PERMUT_EXPANDED = {
    "",
    "ABCFFF",
    "AAABCDEFFFFF",
    "ABCDEDEDEGGG",
    "AAABCGGGGG",
    "ABCDEHIHIHI",
    "AAABCDEDEDEHIHIHIHIHI",
    "FFFCBA",
    "FFFFFDECBAAA",
    "GGGDEDEDECBA",
    "GGGGGCBAAA",
    "HIHIHIDECBA",
    "HIHIHIHIHIDEDEDECBAAA"
  };

  /**
   * Tests a real beauty of a regexp for expansion of permutation:
   * <code>(A+& B& C& (D, E)*& (F | G | (H, I)){3, 5})?</code>.
   * The surrounding element is not interesting and the whole grammar consist of
   * only one element.
   *
   * <p>
   * Words for each child of the enclosing concatenation are:
   * <ul>
   *   <li><code>A+</code>: <code>{A, AAA}</code></li>
   *   <li><code>B, C</code>: <code>{BC}</code></li>
   *   <li><code>(D, E)*</code>: <code>{λ, DE, DEDEDE}</code></li>
   *   <li><code>(F | G | (H, I)){3, 5}</code>: <code>{FFF, FFFFF, GGG, GGGGG, HIHIHI, HIHIHIHIHI}</code></li>
   * </ul>
   * </p>
   *
   * <p>
   * A total of 13 words (elements) should be generated:
   * <ol>
   *   <li><code>(empty)</code></li>
   *   <li><code>ABCFFF</code></li>
   *   <li><code>AAABCDEFFFFF</code></li>
   *   <li><code>ABCDEDEDEGGG</code></li>
   *   <li><code>AAABCGGGGG</code></li>
   *   <li><code>ABCDEHIHIHI</code></li>
   *   <li><code>AAABCDEDEDEHIHIHIHIHI</code></li>
   *   <li><code>FFFCBA</code></li>
   *   <li><code>FFFFFDECBAAA</code></li>
   *   <li><code>GGGDEDEDECBA</code></li>
   *   <li><code>GGGGGCBAAA</code></li>
   *   <li><code>HIHIHIDECBA</code></li>
   *   <li><code>HIHIHIHIHIDEDEDECBAAA</code></li>
   * </or>
   * </p>
   */
    @Test
    public void testPermutation() {
    System.out.println("permutation");

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


    final List<Regexp<AbstractStructuralNode>> permutChildren = new ArrayList<Regexp<AbstractStructuralNode>>();

    permutChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("A"), RegexpInterval.getKleeneCross()));
    permutChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("B")));
    permutChildren.add(
            Regexp.<AbstractStructuralNode>getToken(TestUtils.getElement("C")));

    permutChildren.add(
            Regexp.<AbstractStructuralNode>getConcatenation(innerConcatChildren, RegexpInterval.getKleeneStar()));

    permutChildren.add(
            Regexp.<AbstractStructuralNode>getAlternation(alternationChildren, RegexpInterval.getBounded(3, 5)));

    final Regexp<AbstractStructuralNode> subnodes = new Regexp<AbstractStructuralNode>(null, permutChildren, RegexpType.PERMUTATION, RegexpInterval.getOptional());

    final Element e = TestUtils.getElement("e", subnodes);

    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(e);
    final List<Element> result = new ExpanderImpl().expand(grammar);
    final int EXPECTED_SIZE = PERMUT_EXPANDED.length;
    assertEquals(EXPECTED_SIZE, result.size());

    for (int i = 0; i < EXPECTED_SIZE; i++) {
      final Element element = result.get(i);
      final String expected = PERMUT_EXPANDED[i];
      System.out.println("Iteration " + i + ", expecting " + expected);
      assertEquals(expected, subnodesToString(element));
    }
  }
   
  @Test
  public void testXMLLikeData0() {
    System.out.println("testXMLLikeData0");

    // we want something trivial, just a->nothing
    final Element a = TestUtils.getElement("a",
            Regexp.getConcatenation(Collections.<Regexp<AbstractStructuralNode>>emptyList()));
    final List<Element> grammar = Arrays.asList(a);

    final List<Element> result = new ExpanderImpl().expand(grammar);

    assertEquals(1, result.size());

    assertEquals(a, result.get(0));
  }

  @Test
  public void testXMLLikeData1() {
    System.out.println("testXMLLikeData1");
    
    // we want to create situation like this a->b->c
    final Element c = TestUtils.getElement("c");
    final Regexp<AbstractStructuralNode> bSubs = Regexp.getConcatenation(
            Arrays.<Regexp<AbstractStructuralNode>>asList(Regexp.<AbstractStructuralNode>getToken(c)));
    final Element b = TestUtils.getElement("b", bSubs);
    final Regexp<AbstractStructuralNode> aSubs = Regexp.getConcatenation(
            Arrays.<Regexp<AbstractStructuralNode>>asList(Regexp.<AbstractStructuralNode>getToken(b)));
    final Element a = TestUtils.getElement("a", aSubs);

    final List<Element> grammar = Arrays.asList(a, b);

    final List<Element> result = new ExpanderImpl().expand(grammar);

    assertEquals(2, result.size());

    // now we expect to have 2 rules like this:
    // a->b (b sentinel)
    // b->c (c sentinel)

    final Element rule1 = result.get(0);
    assertTrue(EqualityUtils.sameElements(rule1, a, EqualityUtils.IGNORE_SUBNODES));
    assertTrue(EqualityUtils.sameElements(
            (Element)rule1.getSubnodes().getChild(0).getContent(),
            b, EqualityUtils.IGNORE_SUBNODES | EqualityUtils.IGNORE_METADATA));
    assertTrue(((Element)rule1.getSubnodes().getChild(0).getContent()).getSubnodes().isLambda());

    assertEquals(Boolean.TRUE, rule1.getSubnodes().getChild(0).getContent().getMetadata().get(IGGUtils.IS_SENTINEL));

    final Element rule2 = result.get(1);
    assertTrue(EqualityUtils.sameElements(rule2, b, EqualityUtils.IGNORE_SUBNODES));
    assertTrue(EqualityUtils.sameElements(
            (Element)rule2.getSubnodes().getChild(0).getContent(),
            c, EqualityUtils.IGNORE_SUBNODES | EqualityUtils.IGNORE_METADATA));
    assertTrue(((Element)rule2.getSubnodes().getChild(0).getContent()).getSubnodes().isLambda());

    assertEquals(Boolean.TRUE, rule2.getSubnodes().getChild(0).getContent().getMetadata().get(IGGUtils.IS_SENTINEL));
  }
}
