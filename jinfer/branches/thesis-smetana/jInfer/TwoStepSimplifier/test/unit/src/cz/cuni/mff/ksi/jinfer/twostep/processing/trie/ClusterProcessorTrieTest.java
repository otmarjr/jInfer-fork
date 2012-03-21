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
package cz.cuni.mff.ksi.jinfer.twostep.processing.trie;

import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.Collections;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class ClusterProcessorTrieTest {

  /**
   * Will return a tree like this:
   *
   * 1
   * |
   * 2
   * |\
   * 3 4
   * | |\
   * 5 6 7
   */
  private Regexp<AbstractStructuralNode> getTestTree() {
    final List<Regexp<AbstractStructuralNode>> c0 = new ArrayList<Regexp<AbstractStructuralNode>>(3);
    c0.add(TestUtils.getToken(getElement("1")));
    c0.add(TestUtils.getToken(getElement("2")));
    final List<Regexp<AbstractStructuralNode>> c1 = new ArrayList<Regexp<AbstractStructuralNode>>(2);
    final List<Regexp<AbstractStructuralNode>> c2 = new ArrayList<Regexp<AbstractStructuralNode>>(2);
    c2.add(TestUtils.getToken(getElement("3")));
    c2.add(TestUtils.getToken(getElement("5")));
    c1.add(getConcatOf(c2));
    final List<Regexp<AbstractStructuralNode>> c3 = new ArrayList<Regexp<AbstractStructuralNode>>(2);
    c3.add(TestUtils.getToken(getElement("4")));
    final List<Regexp<AbstractStructuralNode>> c4 = new ArrayList<Regexp<AbstractStructuralNode>>(2);
    final List<Regexp<AbstractStructuralNode>> c5 = new ArrayList<Regexp<AbstractStructuralNode>>(1);
    c5.add(TestUtils.getToken(getElement("6")));
    c4.add(getConcatOf(c5));
    final List<Regexp<AbstractStructuralNode>> c6 = new ArrayList<Regexp<AbstractStructuralNode>>(1);
    c6.add(TestUtils.getToken(getElement("7")));
    c4.add(getConcatOf(c6));
    c3.add(getAltOf(c4));
    c1.add(getConcatOf(c3));
    c0.add(getAltOf(c1));
    return getConcatOf(c0);
  }

  @Test
  public final void test01() {
    final Regexp<AbstractStructuralNode> t = getTestTree();
    System.out.println(TestUtils.regexpToStr(t));
    assertEquals("(1{()},2{()},((3{()},5{()})|(4{()},((6{()})|(7{()})))))",
            TestUtils.regexpToStr(t));

    // add 1 - a split should occur right after it
    final List<Regexp<AbstractStructuralNode>> l1 = new ArrayList<Regexp<AbstractStructuralNode>>(1);
    l1.add(TestUtils.getToken(getElement("1")));
    final Regexp<AbstractStructuralNode> a1 = getConcatOf(l1);
    Trie.addBranchToTree(t, a1);
    System.out.println(TestUtils.regexpToStr(t));
    assertEquals("(1{()},(\u03BB|(2{()},((3{()},5{()})|(4{()},((6{()})|(7{()})))))))",
            TestUtils.regexpToStr(t));

    // add 7 - it should be added to the root
    final List<Regexp<AbstractStructuralNode>> l2 = new ArrayList<Regexp<AbstractStructuralNode>>(1);
    l2.add(TestUtils.getToken(getElement("7")));
    final Regexp<AbstractStructuralNode> a2 = getConcatOf(l2);
    Trie.addBranchToTree(t, a2);
    System.out.println(TestUtils.regexpToStr(t));
    assertEquals("(((1{()},(\u03BB|(2{()},((3{()},5{()})|(4{()},((6{()})|(7{()})))))))|(7{()})))",
            TestUtils.regexpToStr(t));

    // add 1 8
    final List<Regexp<AbstractStructuralNode>> l3 = new ArrayList<Regexp<AbstractStructuralNode>>(2);
    l3.add(TestUtils.getToken(getElement("1")));
    l3.add(TestUtils.getToken(getElement("8")));
    final Regexp<AbstractStructuralNode> a3 = getConcatOf(l3);
    Trie.addBranchToTree(t, a3);
    System.out.println(TestUtils.regexpToStr(t));
    assertEquals("(((1{()},(\u03BB|(2{()},((3{()},5{()})|(4{()},((6{()})|(7{()})))))|(8{()})))|(7{()})))",
            TestUtils.regexpToStr(t));

    // add 1 7 9
    final List<Regexp<AbstractStructuralNode>> l4 = new ArrayList<Regexp<AbstractStructuralNode>>(3);
    l4.add(TestUtils.getToken(getElement("1")));
    l4.add(TestUtils.getToken(getElement("7")));
    l4.add(TestUtils.getToken(getElement("9")));
    final Regexp<AbstractStructuralNode> a4 = getConcatOf(l4);
    Trie.addBranchToTree(t, a4);
    System.out.println(TestUtils.regexpToStr(t));
    assertEquals("(((1{()},(\u03BB|(2{()},((3{()},5{()})|(4{()},((6{()})|(7{()})))))|(8{()})|(7{()},9{()})))|(7{()})))",
            TestUtils.regexpToStr(t));

    // add 1 7 9 2
    final List<Regexp<AbstractStructuralNode>> l5 = new ArrayList<Regexp<AbstractStructuralNode>>(4);
    l5.add(TestUtils.getToken(getElement("1")));
    l5.add(TestUtils.getToken(getElement("7")));
    l5.add(TestUtils.getToken(getElement("9")));
    l5.add(TestUtils.getToken(getElement("2")));
    final Regexp<AbstractStructuralNode> a5 = getConcatOf(l5);
    Trie.addBranchToTree(t, a5);
    System.out.println(TestUtils.regexpToStr(t));
    assertEquals("(((1{()},(\u03BB|(2{()},((3{()},5{()})|(4{()},((6{()})|(7{()})))))|(8{()})|(7{()},9{()},(\u03BB|(2{()})))))|(7{()})))",
            TestUtils.regexpToStr(t));
  }

  /**
   * Test
   *
   * <a>
   *   <b/>
   *   <b>text</b>
   * </a>
   *
   * The text must not be lost.
   */
  @Test
  public final void testProblem() {
    final Element b1 = (Element) getElement("b");
    final Element b2 = (Element) getElement("b");
    b2.getSubnodes().addChild(
            Regexp.<AbstractStructuralNode>getToken(
            new SimpleData(
            Collections.<String>emptyList(), "text",
            Collections.<String, Object>emptyMap(), null,
            Collections.<String>emptyList())));
    Trie.addBranchToTree(b1.getSubnodes(), b2.getSubnodes());
    assertEquals(1, b1.getSubnodes().getChildren().size());
    assertEquals(StructuralNodeType.SIMPLE_DATA, b1.getSubnodes().getChild(0).getContent().getType());
  }

  private AbstractStructuralNode getElement(final String name) {
    final Element ret = Element.getMutable();
    ret.setName(name);
    ret.getSubnodes().setType(RegexpType.CONCATENATION);
    ret.getSubnodes().setInterval(RegexpInterval.getOnce());
    return ret;
  }

  private Regexp<AbstractStructuralNode> getConcatOf(final List<Regexp<AbstractStructuralNode>> from) {
    final Regexp<AbstractStructuralNode> ret = Regexp.getMutable();
    ret.setType(RegexpType.CONCATENATION);
    ret.setInterval(RegexpInterval.getOnce());
    ret.getChildren().addAll(from);
    return ret;
  }

  private Regexp<AbstractStructuralNode> getAltOf(final List<Regexp<AbstractStructuralNode>> from) {
    final Regexp<AbstractStructuralNode> ret = Regexp.getMutable();
    ret.setType(RegexpType.ALTERNATION);
    ret.setInterval(RegexpInterval.getOnce());
    ret.getChildren().addAll(from);
    return ret;
  }
}
