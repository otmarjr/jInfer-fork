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
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
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
    new SimpleKP(THRESHOLD).kleeneProcess(null);
  }

  @Test
  public void testKleeneProcessOneRuleOneChild() {
    System.out.println("testKleeneProcessOneRuleOneChild");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>(1);

    final Regexp<AbstractNode> subnodes = Regexp.getConcatenation(Arrays.asList(Regexp.getToken((AbstractNode) new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation()))));

    final AbstractNode rule = new Element(null, "e1", null, subnodes);

    rules.add(rule);

    final List<AbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
    assertEquals(result.size(), 1);
    final AbstractNode n = result.get(0);
    assertEquals(NodeType.ELEMENT, n.getType());
    final Element e = (Element) n;
    assertNotNull(e.getSubnodes());
    assertEquals(RegexpType.CONCATENATION, e.getSubnodes().getType());
    assertEquals(e.getSubnodes().getChildren().size(), 1);
    final AbstractNode n2 = e.getSubnodes().getChild(0).getContent();
    assertElement(n2, "e2");
  }

  @Test
  public void testKleeneProcessOneRuleMoreChildrenNoCollapse() {
    System.out.println("testKleeneProcessOneRuleMoreChildrenNoCollapse");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final Regexp<AbstractNode> subnodes1 = Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode) new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e4", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e5", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))));
    final AbstractNode rule1 = new Element(null, "e1", null, subnodes1);
    rules.add(rule1);

    final List<AbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
    assertEquals(1, result.size());
    final AbstractNode n = result.get(0);
    assertEquals(NodeType.ELEMENT, n.getType());
    final Element e = (Element) n;
    assertNotNull(e.getSubnodes());
    assertEquals(RegexpType.CONCATENATION, e.getSubnodes().getType());
    assertEquals(5, e.getSubnodes().getChildren().size());
    assertElement(e.getSubnodes().getChild(0).getContent(), "e2");
    assertElement(e.getSubnodes().getChild(1).getContent(), "e3");
    assertElement(e.getSubnodes().getChild(2).getContent(), "e4");
    assertElement(e.getSubnodes().getChild(3).getContent(), "e5");
    assertElement(e.getSubnodes().getChild(4).getContent(), "e6");
  }

  @Test
  public void testKleeneProcessOneRuleMoreChildrenCollapse() {
    System.out.println("testKleeneProcessOneRuleMoreChildrenNoCollapse");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final AbstractNode rule1 = new Element(null, "e1", null, getSubnodes0());
    rules.add(rule1);

    final List<AbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);

    assertEquals(1, result.size());
    final AbstractNode n = result.get(0);
    assertEquals(NodeType.ELEMENT, n.getType());
    final Element e = (Element) n;
    assertNotNull(e.getSubnodes());
    assertEquals(RegexpType.CONCATENATION, e.getSubnodes().getType());
    assertEquals(4, e.getSubnodes().getChildren().size());
    assertElement(e.getSubnodes().getChild(0).getContent(), "e2");
    assertEquals(RegexpType.KLEENE, e.getSubnodes().getChild(1).getType());
    assertElement(e.getSubnodes().getChild(1).getChild(0).getContent(), "e3");
    assertElement(e.getSubnodes().getChild(2).getContent(), "e6");
    assertElement(e.getSubnodes().getChild(3).getContent(), "e6");
  }

  @Test
  public void testKleeneProcessMoreRulesMoreChildren() {
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final AbstractNode rule1 = new Element(null, "e1", null, getSubnodes1());
    rules.add(rule1);

    final AbstractNode rule2 = new Element(null, "ehm1", null, getSubnodes2());
    rules.add(rule2);

    final List<AbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);
    assertEquals(result.size(), 2);
    final AbstractNode n0 = result.get(0);
    assertEquals(NodeType.ELEMENT, n0.getType());
    final Element e0 = (Element) n0;
    assertNotNull(e0.getSubnodes());
    assertEquals(RegexpType.CONCATENATION, e0.getSubnodes().getType());
    assertEquals(3, e0.getSubnodes().getChildren().size());

    final AbstractNode n1 = result.get(1);
    assertEquals(NodeType.ELEMENT, n1.getType());
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
  }

  @Test
  public void testKleeneProcessEpic() {
    System.out.println("testKleeneProcessEpic");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final AbstractNode rule1 = new Element(null, "rule1", null, getSubnodes0());
    rules.add(rule1);

    final Regexp<AbstractNode> subnodes2 = Regexp.getAlternation(Arrays.asList(
            getSubnodes1(),
            getSubnodes2()));
    final AbstractNode rule2 = new Element(null, "rule2", null, subnodes2);
    rules.add(rule2);


    final List<AbstractNode> result = new SimpleKP(THRESHOLD).kleeneProcess(rules);

    assertEquals(result.size(), 2);
    final AbstractNode n1 = result.get(0);
    assertEquals(n1.getType(), NodeType.ELEMENT);
    final Element e1 = (Element) n1;
    assertNotNull(e1.getSubnodes());
    assertEquals(e1.getSubnodes().getType(), RegexpType.CONCATENATION);
    assertEquals(e1.getSubnodes().getChildren().size(), 4);

    assertElement(e1.getSubnodes().getChild(0).getContent(), "e2");

    assertEquals(RegexpType.KLEENE, e1.getSubnodes().getChild(1).getType());
    assertElement(e1.getSubnodes().getChild(1).getChild(0).getContent(), "e3");

    assertElement(e1.getSubnodes().getChild(2).getContent(), "e6");

    assertElement(e1.getSubnodes().getChild(3).getContent(), "e6");

    final AbstractNode n2 = result.get(1);
    assertEquals(n2.getType(), NodeType.ELEMENT);
    final Element e2 = (Element) n2;
    assertNotNull(e2.getSubnodes());
    assertEquals(e2.getSubnodes().getType(), RegexpType.ALTERNATION);
    assertEquals(e2.getSubnodes().getChildren().size(), 2);

    final Regexp<AbstractNode> ch1 = e2.getSubnodes().getChild(0);
    final Regexp<AbstractNode> ch2 = e2.getSubnodes().getChild(1);
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
    System.out.println("testKleeneProcessError");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();


    final Element b1 = new Element(null, "b", null, Regexp.<AbstractNode>getConcatenation());
    final Element b2 = new Element(null, "b", null, Regexp.<AbstractNode>getConcatenation());
    b2.getSubnodes().addChild(Regexp.<AbstractNode>getToken(new SimpleData(null, "text", null, null, new ArrayList<String>())));
    final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>(2);
    children.add(Regexp.<AbstractNode>getToken(b1));
    children.add(Regexp.<AbstractNode>getToken(b2));
    final Element root = new Element(null, "root", null, Regexp.getConcatenation(children));

    rules.add(root);

    final KleeneProcessor kp = new SimpleKP(THRESHOLD);

    final List<AbstractNode> processed = kp.kleeneProcess(rules);
    assertEquals(1, processed.size());
    assertElement(processed.get(0), "root");
    final Element root2 = (Element) processed.get(0);

    assertEquals(2, root2.getSubnodes().getChildren().size());
    assertElement(root2.getSubnodes().getChild(0).getContent(), "b");
    assertElement(root2.getSubnodes().getChild(1).getContent(), "b");
    final Element b12 = (Element) root2.getSubnodes().getChild(0).getContent();
    final Element b22 = (Element) root2.getSubnodes().getChild(1).getContent();
    assertEquals(0, b12.getSubnodes().getChildren().size());
    assertEquals(1, b22.getSubnodes().getChildren().size());
    assertEquals(NodeType.SIMPLE_DATA, b22.getSubnodes().getChild(0).getContent().getType());
    final SimpleData sd = (SimpleData) b22.getSubnodes().getChild(0).getContent();
    assertEquals("text", sd.getName());
  }

  /**
   * Tests
   * <a>
   *  <b/>
   *  <b/>
   *  <b>text</b>
   * </a>
   *
   * After collapse of the three &lt;b&gt;'s the #PCDATA should be retained.
   */
  @Test
  public void testKleeneProcessError2() {
    System.out.println("testKleeneProcessError2");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();


    final Element b1 = new Element(null, "b", null, Regexp.<AbstractNode>getConcatenation());
    final Element b15 = new Element(null, "b", null, Regexp.<AbstractNode>getConcatenation());
    final Element b2 = new Element(null, "b", null, Regexp.<AbstractNode>getConcatenation());
    b2.getSubnodes().addChild(Regexp.<AbstractNode>getToken(new SimpleData(null, "text", null, null, new ArrayList<String>())));
    final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>(2);
    children.add(Regexp.<AbstractNode>getToken(b1));
    children.add(Regexp.<AbstractNode>getToken(b15));
    children.add(Regexp.<AbstractNode>getToken(b2));
    final Element root = new Element(null, "root", null, Regexp.getConcatenation(children));

    rules.add(root);

    System.out.println(root.toString());

    final KleeneProcessor kp = new SimpleKP(THRESHOLD);

    final List<AbstractNode> processed = kp.kleeneProcess(rules);
    assertEquals(1, processed.size());
    assertElement(processed.get(0), "root");
    final Element root2 = (Element) processed.get(0);

    System.out.println(root2.toString());

    // TODO vektor Repair!
//    assertEquals(1, root2.getSubnodes().getChildren().size());
//    assertElement(root2.getSubnodes().getChild(0).getChild(0).getContent(), "b");
//    final Element b12 = (Element) root2.getSubnodes().getChild(0).getChild(0).getContent();
//    assertEquals(1, b12.getSubnodes().getChildren().size());
//    assertEquals(NodeType.SIMPLE_DATA, b12.getSubnodes().getChild(0).getContent().getType());
//    final SimpleData sd = (SimpleData) b12.getSubnodes().getChild(0).getContent();
//    assertEquals("text", sd.getName());
  }

  private static Regexp<AbstractNode> getSubnodes0() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode) new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))));
  }

  private static Regexp<AbstractNode> getSubnodes1() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))));
  }

  private static Regexp<AbstractNode> getSubnodes2() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode) new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))));
  }

  private static void assertElement(final AbstractNode n, final String expectedName) {
    assertNotNull(n);
    assertEquals(NodeType.ELEMENT, n.getType());
    assertEquals(expectedName, n.getName());
  }
}
