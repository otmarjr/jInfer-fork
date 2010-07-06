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
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings({"PMD.SystemPrintln", "unchecked"})
public class SimpleKPTest {

  @Test(expected = NullPointerException.class)
  public void testKleeneProcessNull() {
    System.out.println("testKleeneProcessNull");
    new SimpleKP().kleeneProcess(null);
  }

  @Test
  public void testKleeneProcessOneRuleOneChild() {
    System.out.println("testKleeneProcessOneRuleOneChild");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();
    
    final Regexp<AbstractNode> subnodes = Regexp.getConcatenation(Arrays.asList(Regexp.getToken((AbstractNode)new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation()))));

    final AbstractNode rule = new Element(null, "e1", null, subnodes);

    rules.add(rule);
    
    final List<AbstractNode> result = new SimpleKP().kleeneProcess(rules);
    assertEquals(result.size(), 1);
    final AbstractNode n = result.get(0);
    assertEquals(n.getType(), NodeType.ELEMENT);
    final Element e = (Element) n;
    assertNotNull(e.getSubnodes());
    assertEquals(e.getSubnodes().getType(), RegexpType.CONCATENATION);
    assertEquals(e.getSubnodes().getChildren().size(), 1);
    final AbstractNode n2 = e.getSubnodes().getChild(0).getContent();
    assertNotNull(n2);
    assertEquals(n2.getType(), NodeType.ELEMENT);
    assertEquals(n2.getName(), "e2");
  }

  @Test
  public void testKleeneProcessOneRuleMoreChildrenNoCollapse() {
    System.out.println("testKleeneProcessOneRuleMoreChildrenNoCollapse");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final Regexp<AbstractNode> subnodes1 = Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode)new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e4", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e5", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))
            ));
    final AbstractNode rule1 = new Element(null, "e1", null, subnodes1);
    rules.add(rule1);

    final List<AbstractNode> result = new SimpleKP().kleeneProcess(rules);
    assertEquals(result.size(), 1);
    final AbstractNode n = result.get(0);
    assertEquals(n.getType(), NodeType.ELEMENT);
    final Element e = (Element) n;
    assertNotNull(e.getSubnodes());
    assertEquals(e.getSubnodes().getType(), RegexpType.CONCATENATION);
    assertEquals(e.getSubnodes().getChildren().size(), 5);

    final AbstractNode c1 = e.getSubnodes().getChild(0).getContent();
    assertNotNull(c1);
    assertEquals(c1.getType(), NodeType.ELEMENT);
    assertEquals(c1.getName(), "e2");

    final AbstractNode c2 = e.getSubnodes().getChild(1).getContent();
    assertNotNull(c2);
    assertEquals(c2.getType(), NodeType.ELEMENT);
    assertEquals(c2.getName(), "e3");

    final AbstractNode c3 = e.getSubnodes().getChild(2).getContent();
    assertNotNull(c3);
    assertEquals(c3.getType(), NodeType.ELEMENT);
    assertEquals(c3.getName(), "e4");

    final AbstractNode c4 = e.getSubnodes().getChild(3).getContent();
    assertNotNull(c4);
    assertEquals(c4.getType(), NodeType.ELEMENT);
    assertEquals(c4.getName(), "e5");

    final AbstractNode c5 = e.getSubnodes().getChild(4).getContent();
    assertNotNull(c5);
    assertEquals(c5.getType(), NodeType.ELEMENT);
    assertEquals(c5.getName(), "e6");
  }

  @Test
  public void testKleeneProcessOneRuleMoreChildrenCollapse() {
    System.out.println("testKleeneProcessOneRuleMoreChildrenNoCollapse");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final AbstractNode rule1 = new Element(null, "e1", null, getSubnodes0());
    rules.add(rule1);

    final List<AbstractNode> result = new SimpleKP().kleeneProcess(rules);

    assertEquals(result.size(), 1);
    final AbstractNode n = result.get(0);
    assertEquals(n.getType(), NodeType.ELEMENT);
    final Element e = (Element) n;
    assertNotNull(e.getSubnodes());
    assertEquals(e.getSubnodes().getType(), RegexpType.CONCATENATION);
    assertEquals(e.getSubnodes().getChildren().size(), 4);

    final AbstractNode c1 = e.getSubnodes().getChild(0).getContent();
    assertNotNull(c1);
    assertEquals(c1.getType(), NodeType.ELEMENT);
    assertEquals(c1.getName(), "e2");

    final AbstractNode c2 = e.getSubnodes().getChild(1).getChild(0).getContent();
    assertEquals(RegexpType.KLEENE, e.getSubnodes().getChild(1).getType());
    assertNotNull(c2);
    assertEquals(c2.getType(), NodeType.ELEMENT);
    assertEquals(c2.getName(), "e3");

    final AbstractNode c5 = e.getSubnodes().getChild(2).getContent();
    assertNotNull(c5);
    assertEquals(c5.getType(), NodeType.ELEMENT);
    assertEquals(c5.getName(), "e6");

    final AbstractNode c6 = e.getSubnodes().getChild(3).getContent();
    assertNotNull(c6);
    assertEquals(c6.getType(), NodeType.ELEMENT);
    assertEquals(c6.getName(), "e6");
  }

  @Test
  public void testKleeneProcessMoreRulesMoreChildren() {
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final AbstractNode rule1 = new Element(null, "e1", null, getSubnodes1());
    rules.add(rule1);

    final AbstractNode rule2 = new Element(null, "ehm1", null, getSubnodes2());
    rules.add(rule2);

    final List<AbstractNode> result = new SimpleKP().kleeneProcess(rules);
    assertEquals(result.size(), 2);
    final AbstractNode n0 = result.get(0);
    assertEquals(n0.getType(), NodeType.ELEMENT);
    final Element e0 = (Element) n0;
    assertNotNull(e0.getSubnodes());
    assertEquals(e0.getSubnodes().getType(), RegexpType.CONCATENATION);
    assertEquals(e0.getSubnodes().getChildren().size(), 3);

    final AbstractNode n1 = result.get(1);
    assertEquals(n1.getType(), NodeType.ELEMENT);
    final Element e1 = (Element) n1;
    assertNotNull(e1.getSubnodes());
    assertEquals(e1.getSubnodes().getType(), RegexpType.CONCATENATION);
    assertEquals(e1.getSubnodes().getChildren().size(), 2);

    final AbstractNode c2 = e0.getSubnodes().getChild(0).getChild(0).getContent();
    assertEquals(RegexpType.KLEENE, e0.getSubnodes().getChild(0).getType());
    assertNotNull(c2);
    assertEquals(c2.getType(), NodeType.ELEMENT);
    assertEquals(c2.getName(), "e3");

    final AbstractNode c5 = e0.getSubnodes().getChild(1).getContent();
    assertNotNull(c5);
    assertEquals(c5.getType(), NodeType.ELEMENT);
    assertEquals(c5.getName(), "e6");

    final AbstractNode c6 = e0.getSubnodes().getChild(1).getContent();
    assertNotNull(c6);
    assertEquals(c6.getType(), NodeType.ELEMENT);
    assertEquals(c6.getName(), "e6");

    final AbstractNode c10 = e1.getSubnodes().getChild(0).getChild(0).getContent();
    assertEquals(RegexpType.KLEENE, e1.getSubnodes().getChild(0).getType());
    assertNotNull(c10);
    assertEquals(c10.getType(), NodeType.ELEMENT);
    assertEquals(c10.getName(), "e3");

    final AbstractNode c11 = e1.getSubnodes().getChild(1).getChild(0).getContent();
    assertEquals(RegexpType.KLEENE, e1.getSubnodes().getChild(1).getType());
    assertNotNull(c11);
    assertEquals(c11.getType(), NodeType.ELEMENT);
    assertEquals(c11.getName(), "e6");
  }

  @Test
  public void testKleeneProcessEpic() {
    System.out.println("testKleeneProcessEpic");
    final List<AbstractNode> rules = new ArrayList<AbstractNode>();

    final AbstractNode rule1 = new Element(null, "rule1", null, getSubnodes0());
    rules.add(rule1);

    final Regexp<AbstractNode> subnodes2 = Regexp.getAlternation(Arrays.asList(
            getSubnodes1(),
            getSubnodes2()
            ));
    final AbstractNode rule2 = new Element(null, "rule2", null, subnodes2);
    rules.add(rule2);


    final List<AbstractNode> result = new SimpleKP().kleeneProcess(rules);

    assertEquals(result.size(), 2);
    final AbstractNode n1 = result.get(0);
    assertEquals(n1.getType(), NodeType.ELEMENT);
    final Element e1 = (Element) n1;
    assertNotNull(e1.getSubnodes());
    assertEquals(e1.getSubnodes().getType(), RegexpType.CONCATENATION);
    assertEquals(e1.getSubnodes().getChildren().size(), 4);

    final AbstractNode c1 = e1.getSubnodes().getChild(0).getContent();
    assertNotNull(c1);
    assertEquals(c1.getType(), NodeType.ELEMENT);
    assertEquals(c1.getName(), "e2");

    final AbstractNode c2 = e1.getSubnodes().getChild(1).getChild(0).getContent();
    assertEquals(RegexpType.KLEENE, e1.getSubnodes().getChild(1).getType());
    assertNotNull(c2);
    assertEquals(c2.getType(), NodeType.ELEMENT);
    assertEquals(c2.getName(), "e3");

    final AbstractNode c5 = e1.getSubnodes().getChild(2).getContent();
    assertNotNull(c5);
    assertEquals(c5.getType(), NodeType.ELEMENT);
    assertEquals(c5.getName(), "e6");

    final AbstractNode c6 = e1.getSubnodes().getChild(3).getContent();
    assertNotNull(c6);
    assertEquals(c6.getType(), NodeType.ELEMENT);
    assertEquals(c6.getName(), "e6");

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

    assertEquals("e3", ch1.getChild(0).getChild(0).getContent().getName());
    assertEquals("e6", ch1.getChild(1).getContent().getName());
    assertEquals("e6", ch1.getChild(2).getContent().getName());

    assertEquals("e3", ch2.getChild(0).getChild(0).getContent().getName());
    assertEquals("e6", ch2.getChild(1).getChild(0).getContent().getName());
  }

  private static Regexp<AbstractNode> getSubnodes0() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode)new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))
            ));
  }

  private static Regexp<AbstractNode> getSubnodes1() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))
            ));
  }

  private static Regexp<AbstractNode> getSubnodes2() {
    return Regexp.getConcatenation(Arrays.asList(
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e3", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation())),
            Regexp.getToken((AbstractNode)new Element(null, "e6", null, Regexp.<AbstractNode>getConcatenation()))
            ));
  }
}
