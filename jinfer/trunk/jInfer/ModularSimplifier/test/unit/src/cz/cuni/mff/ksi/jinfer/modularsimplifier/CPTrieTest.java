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

package cz.cuni.mff.ksi.jinfer.modularsimplifier;

import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.TrieHelper;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
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
public class CPTrieTest {

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
  private Regexp<AbstractNode> getTestTree() {
    final List<Regexp<AbstractNode>> c0 = new ArrayList<Regexp<AbstractNode>>(3);
    c0.add(Regexp.getToken((AbstractNode)(new Element(null, "1", null, null))));
    c0.add(Regexp.getToken((AbstractNode)(new Element(null, "2", null, null))));
    final List<Regexp<AbstractNode>> c1 = new ArrayList<Regexp<AbstractNode>>(2);
    final List<Regexp<AbstractNode>> c2 = new ArrayList<Regexp<AbstractNode>>(2);
    c2.add(Regexp.getToken((AbstractNode)(new Element(null, "3", null, null))));
    c2.add(Regexp.getToken((AbstractNode)(new Element(null, "5", null, null))));
    c1.add(new Regexp<AbstractNode>(null, c2, RegexpType.CONCATENATION));
    final List<Regexp<AbstractNode>> c3 = new ArrayList<Regexp<AbstractNode>>(2);
    c3.add(Regexp.getToken((AbstractNode)(new Element(null, "4", null, null))));
    final List<Regexp<AbstractNode>> c4 = new ArrayList<Regexp<AbstractNode>>(2);
    final List<Regexp<AbstractNode>> c5 = new ArrayList<Regexp<AbstractNode>>(1);
    c5.add(Regexp.getToken((AbstractNode)(new Element(null, "6", null, null))));
    c4.add(new Regexp<AbstractNode>(null, c5, RegexpType.CONCATENATION));
    final List<Regexp<AbstractNode>> c6 = new ArrayList<Regexp<AbstractNode>>(1);
    c6.add(Regexp.getToken((AbstractNode)(new Element(null, "7", null, null))));
    c4.add(new Regexp<AbstractNode>(null, c6, RegexpType.CONCATENATION));
    c3.add(new Regexp<AbstractNode>(null, c4, RegexpType.ALTERNATION));
    c1.add(new Regexp<AbstractNode>(null, c3, RegexpType.CONCATENATION));
    c0.add(new Regexp<AbstractNode>(null, c1, RegexpType.ALTERNATION));
    return new Regexp<AbstractNode>(null, c0, RegexpType.CONCATENATION);
  }

  @Test
  public final void test01() {
    final Regexp<AbstractNode> t = getTestTree();
    System.out.println(t.toString());
    assertEquals("(1: ELEMENT,2: ELEMENT,((3: ELEMENT,5: ELEMENT,)|(4: ELEMENT,((6: ELEMENT,)|(7: ELEMENT,)|),)|),)",
            t.toString());

    // add 1 - a split should occur right after it
    final List<Regexp<AbstractNode>> l1 = new ArrayList<Regexp<AbstractNode>>(1);
    l1.add(Regexp.getToken((AbstractNode) new Element(null, "1", null, null)));
    final Regexp<AbstractNode> a1 = new Regexp<AbstractNode>(null, l1, RegexpType.CONCATENATION);
    TrieHelper.addBranchToTree(t, a1);
    System.out.println(t.toString());
    assertEquals("(1: ELEMENT,(()|(2: ELEMENT,((3: ELEMENT,5: ELEMENT,)|(4: ELEMENT,((6: ELEMENT,)|(7: ELEMENT,)|),)|),)|),)",
            t.toString());

    // add 7 - it should be added to the root
    final List<Regexp<AbstractNode>> l2 = new ArrayList<Regexp<AbstractNode>>(1);
    l2.add(Regexp.getToken((AbstractNode) new Element(null, "7", null, null)));
    final Regexp<AbstractNode> a2 = new Regexp<AbstractNode>(null, l2, RegexpType.CONCATENATION);
    TrieHelper.addBranchToTree(t, a2);
    System.out.println(t.toString());
    assertEquals("(((1: ELEMENT,(()|(2: ELEMENT,((3: ELEMENT,5: ELEMENT,)|(4: ELEMENT,((6: ELEMENT,)|(7: ELEMENT,)|),)|),)|),)|(7: ELEMENT,)|),)",
            t.toString());

    // add 1 8
    final List<Regexp<AbstractNode>> l3 = new ArrayList<Regexp<AbstractNode>>(2);
    l3.add(Regexp.getToken((AbstractNode) new Element(null, "1", null, null)));
    l3.add(Regexp.getToken((AbstractNode) new Element(null, "8", null, null)));
    final Regexp<AbstractNode> a3 = new Regexp<AbstractNode>(null, l3, RegexpType.CONCATENATION);
    TrieHelper.addBranchToTree(t, a3);
    System.out.println(t.toString());
    assertEquals("(((1: ELEMENT,(()|(2: ELEMENT,((3: ELEMENT,5: ELEMENT,)|(4: ELEMENT,((6: ELEMENT,)|(7: ELEMENT,)|),)|),)|(8: ELEMENT,)|),)|(7: ELEMENT,)|),)",
            t.toString());

    // add 1 7 9
    final List<Regexp<AbstractNode>> l4 = new ArrayList<Regexp<AbstractNode>>(3);
    l4.add(Regexp.getToken((AbstractNode) new Element(null, "1", null, null)));
    l4.add(Regexp.getToken((AbstractNode) new Element(null, "7", null, null)));
    l4.add(Regexp.getToken((AbstractNode) new Element(null, "9", null, null)));
    final Regexp<AbstractNode> a4 = new Regexp<AbstractNode>(null, l4, RegexpType.CONCATENATION);
    TrieHelper.addBranchToTree(t, a4);
    System.out.println(t.toString());
    assertEquals("(((1: ELEMENT,(()|(2: ELEMENT,((3: ELEMENT,5: ELEMENT,)|(4: ELEMENT,((6: ELEMENT,)|(7: ELEMENT,)|),)|),)|(8: ELEMENT,)|(7: ELEMENT,9: ELEMENT,)|),)|(7: ELEMENT,)|),)",
            t.toString());

    // add 1 7 9 2
    final List<Regexp<AbstractNode>> l5 = new ArrayList<Regexp<AbstractNode>>(4);
    l5.add(Regexp.getToken((AbstractNode) new Element(null, "1", null, null)));
    l5.add(Regexp.getToken((AbstractNode) new Element(null, "7", null, null)));
    l5.add(Regexp.getToken((AbstractNode) new Element(null, "9", null, null)));
    l5.add(Regexp.getToken((AbstractNode) new Element(null, "2", null, null)));
    final Regexp<AbstractNode> a5 = new Regexp<AbstractNode>(null, l5, RegexpType.CONCATENATION);
    TrieHelper.addBranchToTree(t, a5);
    System.out.println(t.toString());
    assertEquals("(((1: ELEMENT,(()|(2: ELEMENT,((3: ELEMENT,5: ELEMENT,)|(4: ELEMENT,((6: ELEMENT,)|(7: ELEMENT,)|),)|),)|(8: ELEMENT,)|(7: ELEMENT,9: ELEMENT,(()|(2: ELEMENT,)|),)|),)|(7: ELEMENT,)|),)",
            t.toString());
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
    final Element b1 = new Element(null, "b", null, Regexp.<AbstractNode>getConcatenation());
    final Element b2 = new Element(null, "b", null, Regexp.<AbstractNode>getConcatenation());
    b2.getSubnodes().addChild(Regexp.<AbstractNode>getToken(new SimpleData(null, "text", null, null, new ArrayList<String>(0))));
    TrieHelper.addBranchToTree(b1.getSubnodes(), b2.getSubnodes());
    assertEquals(1, b1.getSubnodes().getChildren().size());
    assertEquals(NodeType.SIMPLE_DATA, b1.getSubnodes().getChild(0).getContent().getType());
  }

}