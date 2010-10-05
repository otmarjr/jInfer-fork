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
import cz.cuni.mff.ksi.jinfer.base.objects.StructuralNodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.SimpleData;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings({"PMD.SystemPrintln", "unchecked"})
public class SimpleKPTest {

  private static final int THRESHOLD = 3;

  @Test(expected = NullPointerException.class)
  public void testKleeneProcessNull() {
    System.out.println("testKleeneProcessNull");
    try {
      new SimpleKP(THRESHOLD).kleeneProcess(null);
    } catch (final InterruptedException ex) {
      fail("Interrupted");
    }
  }

  @Test
  public void testKleeneProcessOneRuleOneChild() {
    try {
      System.out.println("testKleeneProcessOneRuleOneChild");
      final List<StructuralAbstractNode> rules = new ArrayList<StructuralAbstractNode>(1);
      final Regexp<StructuralAbstractNode> subnodes = Regexp.getConcatenation(Arrays.asList(Regexp.getToken((StructuralAbstractNode) new Element(null, "e2", null, Regexp.<StructuralAbstractNode>getConcatenation()))));
      final StructuralAbstractNode rule = new Element(null, "e1", null, subnodes);
      rules.add(rule);
      final List<StructuralAbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
      assertEquals(result.size(), 1);
      final StructuralAbstractNode n = result.get(0);
      assertEquals(StructuralNodeType.ELEMENT, n.getType());
      final Element e = (Element) n;
      assertNotNull(e.getSubnodes());
      assertEquals(RegexpType.TOKEN, e.getSubnodes().getType());
      final StructuralAbstractNode n2 = e.getSubnodes().getContent();
      assertElement(n2, "e2");
    } catch (InterruptedException ex) {
      fail("Interrupted");
    }
  }

  @Test
  public void testKleeneProcessOneRuleMoreChildrenNoCollapse() {
    try {
      System.out.println("testKleeneProcessOneRuleMoreChildrenNoCollapse");
      final List<StructuralAbstractNode> rules = new ArrayList<StructuralAbstractNode>();
      final Regexp<StructuralAbstractNode> subnodes1 = Regexp.getConcatenation(Arrays.asList(Regexp.getToken((StructuralAbstractNode) new Element(null, "e2", null, Regexp.<StructuralAbstractNode>getConcatenation())), Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())), Regexp.getToken((StructuralAbstractNode) new Element(null, "e4", null, Regexp.<StructuralAbstractNode>getConcatenation())), Regexp.getToken((StructuralAbstractNode) new Element(null, "e5", null, Regexp.<StructuralAbstractNode>getConcatenation())), Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation()))));
      final StructuralAbstractNode rule1 = new Element(null, "e1", null, subnodes1);
      rules.add(rule1);
      final List<StructuralAbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
      assertEquals(1, result.size());
      final StructuralAbstractNode n = result.get(0);
      assertEquals(StructuralNodeType.ELEMENT, n.getType());
      final Element e = (Element) n;
      assertNotNull(e.getSubnodes());
      assertEquals(RegexpType.CONCATENATION, e.getSubnodes().getType());
      assertEquals(5, e.getSubnodes().getChildren().size());
      assertElement(e.getSubnodes().getChild(0).getContent(), "e2");
      assertElement(e.getSubnodes().getChild(1).getContent(), "e3");
      assertElement(e.getSubnodes().getChild(2).getContent(), "e4");
      assertElement(e.getSubnodes().getChild(3).getContent(), "e5");
      assertElement(e.getSubnodes().getChild(4).getContent(), "e6");
    } catch (InterruptedException ex) {
      fail("Interrupted");
    }
  }

  @Test
  public void testKleeneProcessOneRuleMoreChildrenCollapse() {
    try {
      System.out.println("testKleeneProcessOneRuleMoreChildrenNoCollapse");
      final List<StructuralAbstractNode> rules = new ArrayList<StructuralAbstractNode>();
      final StructuralAbstractNode rule1 = new Element(null, "e1", null, getSubnodes0());
      rules.add(rule1);
      final List<StructuralAbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
      assertEquals(1, result.size());
      final StructuralAbstractNode n = result.get(0);
      assertEquals(StructuralNodeType.ELEMENT, n.getType());
      final Element e = (Element) n;
      assertNotNull(e.getSubnodes());
      assertEquals(RegexpType.CONCATENATION, e.getSubnodes().getType());
      assertEquals(4, e.getSubnodes().getChildren().size());
      assertElement(e.getSubnodes().getChild(0).getContent(), "e2");
      assertEquals(RegexpType.KLEENE, e.getSubnodes().getChild(1).getType());
      assertElement(e.getSubnodes().getChild(1).getChild(0).getContent(), "e3");
      assertElement(e.getSubnodes().getChild(2).getContent(), "e6");
      assertElement(e.getSubnodes().getChild(3).getContent(), "e6");
    } catch (InterruptedException ex) {
      fail("Interrupted");
    }
  }

  @Test
  public void testKleeneProcessMoreRulesMoreChildren() {
    try {
      final List<StructuralAbstractNode> rules = new ArrayList<StructuralAbstractNode>();
      final StructuralAbstractNode rule1 = new Element(null, "e1", null, getSubnodes1());
      rules.add(rule1);
      final StructuralAbstractNode rule2 = new Element(null, "ehm1", null, getSubnodes2());
      rules.add(rule2);
      final List<StructuralAbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
      assertEquals(result.size(), 2);
      final StructuralAbstractNode n0 = result.get(0);
      assertEquals(StructuralNodeType.ELEMENT, n0.getType());
      final Element e0 = (Element) n0;
      assertNotNull(e0.getSubnodes());
      assertEquals(RegexpType.CONCATENATION, e0.getSubnodes().getType());
      assertEquals(3, e0.getSubnodes().getChildren().size());
      final StructuralAbstractNode n1 = result.get(1);
      assertEquals(StructuralNodeType.ELEMENT, n1.getType());
      final Element e1 = (Element) n1;
      assertNotNull(e1.getSubnodes());
      assertEquals(RegexpType.CONCATENATION, e1.getSubnodes().getType());
      assertEquals(2, e1.getSubnodes().getChildren().size());
      assertEquals(RegexpType.KLEENE, e0.getSubnodes().getChild(0).getType());
      assertElement(e0.getSubnodes().getChild(0).getChild(0).getContent(), "e3");
      assertElement(e0.getSubnodes().getChild(1).getContent(), "e6");
      assertElement(e0.getSubnodes().getChild(1).getContent(), "e6");
      assertEquals(RegexpType.KLEENE, e1.getSubnodes().getChild(0).getType());
      assertElement(e1.getSubnodes().getChild(0).getChild(0).getContent(), "e3");
      assertEquals(RegexpType.KLEENE, e1.getSubnodes().getChild(1).getType());
      assertElement(e1.getSubnodes().getChild(1).getChild(0).getContent(), "e6");
    } catch (InterruptedException ex) {
      fail("Interrupted");
    }
  }

  @Test
  public void testKleeneProcessEpic() {
    try {
      System.out.println("testKleeneProcessEpic");
      final List<StructuralAbstractNode> rules = new ArrayList<StructuralAbstractNode>();
      final StructuralAbstractNode rule1 = new Element(null, "rule1", null, getSubnodes0());
      rules.add(rule1);
      final Regexp<StructuralAbstractNode> subnodes2 = Regexp.getAlternation(Arrays.asList(getSubnodes1(), getSubnodes2()));
      final StructuralAbstractNode rule2 = new Element(null, "rule2", null, subnodes2);
      rules.add(rule2);
      final List<StructuralAbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
      assertEquals(result.size(), 2);
      final StructuralAbstractNode n1 = result.get(0);
      assertEquals(n1.getType(), StructuralNodeType.ELEMENT);
      final Element e1 = (Element) n1;
      assertNotNull(e1.getSubnodes());
      assertEquals(e1.getSubnodes().getType(), RegexpType.CONCATENATION);
      assertEquals(e1.getSubnodes().getChildren().size(), 4);
      assertElement(e1.getSubnodes().getChild(0).getContent(), "e2");
      assertEquals(RegexpType.KLEENE, e1.getSubnodes().getChild(1).getType());
      assertElement(e1.getSubnodes().getChild(1).getChild(0).getContent(), "e3");
      assertElement(e1.getSubnodes().getChild(2).getContent(), "e6");
      assertElement(e1.getSubnodes().getChild(3).getContent(), "e6");
      final StructuralAbstractNode n2 = result.get(1);
      assertEquals(n2.getType(), StructuralNodeType.ELEMENT);
      final Element e2 = (Element) n2;
      assertNotNull(e2.getSubnodes());
      assertEquals(e2.getSubnodes().getType(), RegexpType.ALTERNATION);
      assertEquals(e2.getSubnodes().getChildren().size(), 2);
      final Regexp<StructuralAbstractNode> ch1 = e2.getSubnodes().getChild(0);
      final Regexp<StructuralAbstractNode> ch2 = e2.getSubnodes().getChild(1);
      assertEquals(RegexpType.CONCATENATION, ch1.getType());
      assertEquals(RegexpType.CONCATENATION, ch2.getType());
      assertEquals(3, ch1.getChildren().size());
      assertEquals(2, ch2.getChildren().size());
      assertEquals(RegexpType.KLEENE, ch1.getChild(0).getType());
      assertEquals(RegexpType.TOKEN, ch1.getChild(1).getType());
      assertEquals(RegexpType.TOKEN, ch1.getChild(2).getType());
      assertEquals(RegexpType.KLEENE, ch2.getChild(0).getType());
      assertEquals(RegexpType.KLEENE, ch2.getChild(1).getType());
      assertElement(ch1.getChild(0).getChild(0).getContent(), "e3");
      assertElement(ch1.getChild(1).getContent(), "e6");
      assertElement(ch1.getChild(2).getContent(), "e6");
      assertElement(ch2.getChild(0).getChild(0).getContent(), "e3");
      assertElement(ch2.getChild(1).getChild(0).getContent(), "e6");
    } catch (InterruptedException ex) {
      fail("Interrupted");
    }
  }

  /**
   * Tests
   * <a>
   *  <b/>
   *  <b>text</b>
   * </a>
   *
   * Second &lt;b&gt; should retain its #PCDATA in the result.
   */
  @Test
  public void testKleeneProcessError() {
    try {
      System.out.println("testKleeneProcessError");
      final List<StructuralAbstractNode> rules = new ArrayList<StructuralAbstractNode>();
      final Element b1 = new Element(null, "b", null, Regexp.<StructuralAbstractNode>getConcatenation());
      final Element b2 = new Element(null, "b", null, Regexp.<StructuralAbstractNode>getConcatenation());
      b2.getSubnodes().addChild(Regexp.<StructuralAbstractNode>getToken(new SimpleData(null, "text", null, null, new ArrayList<String>())));
      final List<Regexp<StructuralAbstractNode>> children = new ArrayList<Regexp<StructuralAbstractNode>>(2);
      children.add(Regexp.<StructuralAbstractNode>getToken(b1));
      children.add(Regexp.<StructuralAbstractNode>getToken(b2));
      final Element root = new Element(null, "root", null, Regexp.getConcatenation(children));
      rules.add(root);
      final KleeneProcessor kp = new SimpleKP(THRESHOLD);
      final List<StructuralAbstractNode> processed = kp.kleeneProcess(rules);
      assertEquals(1, processed.size());
      assertElement(processed.get(0), "root");
      final Element root2 = (Element) processed.get(0);
      assertEquals(2, root2.getSubnodes().getChildren().size());
      assertElement(root2.getSubnodes().getChild(0).getContent(), "b");
      assertElement(root2.getSubnodes().getChild(1).getContent(), "b");
      final Element b12 = (Element) root2.getSubnodes().getChild(0).getContent();
      final Element b22 = (Element) root2.getSubnodes().getChild(1).getContent();
      assertEquals(0, b12.getSubnodes().getChildren().size());
      assertEquals(0, b22.getSubnodes().getChildren().size());
      assertEquals(RegexpType.TOKEN, b22.getSubnodes().getType());
      assertEquals(StructuralNodeType.SIMPLE_DATA, b22.getSubnodes().getContent().getType());
      final SimpleData sd = (SimpleData) b22.getSubnodes().getContent();
      assertEquals("text", sd.getName());
    } catch (InterruptedException ex) {
      fail("Interrupted");
    }
  }

  private static Regexp<StructuralAbstractNode> getSubnodes0() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e2", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation()))));
  }

  private static Regexp<StructuralAbstractNode> getSubnodes1() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation()))));
  }

  private static Regexp<StructuralAbstractNode> getSubnodes2() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e3", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation())),
            Regexp.getToken((StructuralAbstractNode) new Element(null, "e6", null, Regexp.<StructuralAbstractNode>getConcatenation()))));
  }

  private static void assertElement(final StructuralAbstractNode n, final String expectedName) {
    assertNotNull(n);
    assertEquals(StructuralNodeType.ELEMENT, n.getType());
    assertEquals(expectedName, n.getName());
  }
}
