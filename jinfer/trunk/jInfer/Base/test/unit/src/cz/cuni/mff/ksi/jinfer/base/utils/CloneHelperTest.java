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
package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import java.util.Map;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class CloneHelperTest {

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull() {
    System.out.println("cloneRulesNull");
    new CloneHelper().cloneRules(null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull2() {
    System.out.println("cloneRulesNull2");
    new CloneHelper().cloneRules(Arrays.<Element>asList(null), null);
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull3() {
    System.out.println("cloneRulesNull3");
    new CloneHelper().cloneRules(
            Arrays.<Element>asList(
              TestUtils.getElement("e", Regexp.<AbstractStructuralNode>getLambda()), null),
            null);
  }

  @Test
  public void testCloneRules() {
    System.out.println("cloneRules");
    final Map<String, Object> m = new HashMap<String, Object>();
    m.put("one", Integer.valueOf(1));
    m.put("two", Integer.valueOf(2));

    final List<Regexp<AbstractStructuralNode>> altChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    altChildren.add(Regexp.getToken(
            (AbstractStructuralNode) TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getLambda())));

    final List<Regexp<AbstractStructuralNode>> children = new ArrayList<Regexp<AbstractStructuralNode>>();
    children.add(Regexp.getAlternation(altChildren));
    children.add(Regexp.getToken((AbstractStructuralNode) TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getLambda())));

    final Element e = new Element(TestUtils.EMPTY_CONTEXT, "e", m, Regexp.getConcatenation(children), Collections.<Attribute>emptyList());
    final List<Element> l = new ArrayList<Element>(1);
    l.add(e);
    final List<Element> result = new CloneHelper().cloneRules(l, null);

    assertEquals(1, result.size());
    assert (l != result);
    final Element other = result.get(0);
    assert (e != other);
    assert (e.getSubnodes() != other.getSubnodes());
    assert (e.getSubnodes().getChild(0) != other.getSubnodes().getChild(0));
    assert (e.getSubnodes().getChild(0).getChild(0) != other.getSubnodes().getChild(0).getChild(0));
    assert (e.getSubnodes().getChild(1) != other.getSubnodes().getChild(1));
  }

  @Test
  public void testEqualBranch() {
    System.out.println("equal branch");

    final Element root = TestUtils.getElement("root", Regexp.<AbstractStructuralNode>getMutable());
    final Element e1 = TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getMutable());
    final Element e2 = TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getMutable());
    final Element same = TestUtils.getElement("same");

    root.getSubnodes().setType(RegexpType.CONCATENATION);
    root.getSubnodes().setInterval(RegexpInterval.getKleeneCross());
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e1));
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e2));
    root.getSubnodes().setImmutable();

    e1.getSubnodes().setType(RegexpType.CONCATENATION);
    e1.getSubnodes().setInterval(RegexpInterval.getOptional());
    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(same));
    e1.getSubnodes().setImmutable();
    e2.getSubnodes().setType(RegexpType.CONCATENATION);
    e2.getSubnodes().setInterval(RegexpInterval.getKleeneStar());
    e2.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(same));
    e2.getSubnodes().setImmutable();

    final String one = "one";
    final String two = "two";
    final List<String> prefix = new ArrayList<String>();
    prefix.add(one);
    prefix.add(two);
    final Element cloned = new CloneHelper().cloneElement(root, prefix);
    assertTrue("cloned element is an Element", cloned.isElement());
    assertFalse("cloned element is immutable", cloned.isMutable());
    final List<AbstractStructuralNode> tokens = cloned.getSubnodes().getTokens();
    assertEquals("cloned element has 2 tokens", tokens.size(), 2);
    assertTrue("cloned e1 is an element", tokens.get(0).isElement());
    assertTrue("cloned e2 is an element", tokens.get(1).isElement());

    final Element ce0 = (Element) tokens.get(0);
    assertEquals("ce0 size has 1 token", ce0.getSubnodes().getTokens().size(), 1);
    assertFalse("ce0 element is immutable", ce0.isMutable());
    final Element ce1 = (Element) tokens.get(1);
    assertEquals("ce1 size has 1 token", ce1.getSubnodes().getTokens().size(), 1);
    assertFalse("ce1 element is immutable", ce1.isMutable());
    
    assertEquals("b1/same == b2/same", ce0.getSubnodes().getTokens().get(0), ce1.getSubnodes().getTokens().get(0));
    assertTrue("b1/same equals b2/same", ce0.getSubnodes().getTokens().get(0).equals(ce1.getSubnodes().getTokens().get(0)));
  }

  @Test
  public void testNonequalBranch() {
    System.out.println("Nonequal branch");

    final Element root = TestUtils.getElement("root", Regexp.<AbstractStructuralNode>getMutable());
    final Element e1 = TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getMutable());
    final Element e2 = TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getMutable());
    final Element s1 = TestUtils.getElement("same");
    final Element s2 = TestUtils.getElement("same");

    root.getSubnodes().setType(RegexpType.CONCATENATION);
    root.getSubnodes().setInterval(RegexpInterval.getKleeneCross());
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e1));
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e2));
    root.getSubnodes().setImmutable();

    e1.getSubnodes().setType(RegexpType.CONCATENATION);
    e1.getSubnodes().setInterval(RegexpInterval.getOptional());
    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(s1));
    e1.getSubnodes().setImmutable();
    e2.getSubnodes().setType(RegexpType.CONCATENATION);
    e2.getSubnodes().setInterval(RegexpInterval.getKleeneStar());
    e2.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(s2));
    e2.getSubnodes().setImmutable();

    final String one = "one";
    final String two = "two";
    final List<String> prefix = new ArrayList<String>();
    prefix.add(one);
    prefix.add(two);
    final Element cloned = new CloneHelper().cloneElement(root, prefix);
    assertTrue("cloned element is an Element", cloned.isElement());
    assertFalse("cloned element is immutable", cloned.isMutable());
    final List<AbstractStructuralNode> tokens = cloned.getSubnodes().getTokens();
    assertEquals("cloned element has 2 tokens", tokens.size(), 2);
    assertTrue("cloned e1 is an element", tokens.get(0).isElement());
    assertTrue("cloned e2 is an element", tokens.get(1).isElement());

    final Element ce0 = (Element) tokens.get(0);
    assertEquals("ce0 size has 1 token", ce0.getSubnodes().getTokens().size(), 1);
    assertFalse("ce0 element is immutable", ce0.isMutable());
    final Element ce1 = (Element) tokens.get(1);
    assertEquals("ce1 size has 1 token", ce1.getSubnodes().getTokens().size(), 1);
    assertFalse("ce1 element is immutable", ce1.isMutable());

    assertNotSame("b1/same == b2/same", ce0.getSubnodes().getTokens().get(0), ce1.getSubnodes().getTokens().get(0));
    assertFalse("b1/same equals b2/same", ce0.getSubnodes().getTokens().get(0).equals(ce1.getSubnodes().getTokens().get(0)));
  }

  @Test
  public void testCycle() {
    System.out.println("cycle");

    final Element e1 = TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getMutable());
    final Element e2 = TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getMutable());
    e1.getSubnodes().addChild(
            Regexp.<AbstractStructuralNode>getToken(e2)
            );
    e1.getSubnodes().setInterval(RegexpInterval.getOnce());
    e1.getSubnodes().setType(RegexpType.CONCATENATION);
    e1.getSubnodes().setImmutable();
    e2.getSubnodes().addChild(
            Regexp.<AbstractStructuralNode>getToken(e1)
            );
    e2.getSubnodes().setInterval(RegexpInterval.getOnce());
    e2.getSubnodes().setType(RegexpType.CONCATENATION);
    e2.getSubnodes().setImmutable();


    final List<Element> l = new ArrayList<Element>(1);
    l.add(e1);
    final List<Element> result = new CloneHelper().cloneRules(l, null);

    assertEquals(1, result.size());
    final Element e1o = result.get(0);
    assertEquals(e1.getName(), e1o.getName());
    final Element e2o = (Element) e1o.getSubnodes().getChild(0).getContent();
    assertEquals(e2.getName(), e2o.getName());
    final Element e1oo = (Element) e2o.getSubnodes().getChild(0).getContent();
    assert (e1o != e1);
    assert (e2o != e2);
    assert (e1o == e1oo);
  }

  @Test
  public void testCycleTokens() {
    System.out.println("cycleTokens");

    final Element e1 = TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getMutable());
    final Element e2 = TestUtils.getElement("e2", Regexp.<AbstractStructuralNode>getToken(e1));
    e1.getSubnodes().setContent(e2);
    e1.getSubnodes().setInterval(RegexpInterval.getOnce());
    e1.getSubnodes().setType(RegexpType.TOKEN);
    e1.getSubnodes().setImmutable();

    final List<Element> l = new ArrayList<Element>(1);
    l.add(e1);
    final List<Element> result = new CloneHelper().cloneRules(l, null);

    assertEquals(1, result.size());
    final Element e1o = result.get(0);
    assertEquals(e1.getName(), e1o.getName());
    final Element e2o = (Element) e1o.getSubnodes().getContent();
    assertEquals(e2.getName(), e2o.getName());
    final Element e1oo = (Element) e2o.getSubnodes().getContent();
    assert (e1o != e1);
    assert (e2o != e2);
    assert (e1o == e1oo);
  }

  @Test
  public void testAttributesOne() {
    System.out.println("attributesOne");

    final Element e1 = Element.getMutable();
    e1.setName("test");
    e1.getSubnodes().setType(RegexpType.LAMBDA);
    final Attribute a1 = TestUtils.getAttribute("id");
    e1.getAttributes().add(a1);

    final List<Element> l = new ArrayList<Element>(1);
    l.add(e1);
    final List<Element> result = new CloneHelper().cloneRules(l, null);

    assertEquals(1, result.size());
    final Element e1o = result.get(0);
    assertEquals(e1.getName(), e1o.getName());
    assert (e1o != e1);
    assertEquals(1, e1o.getAttributes().size());
    assertEquals(e1.getAttributes().get(0).getName(), e1o.getAttributes().get(0).getName());
    assert (e1.getAttributes().get(0) != e1o.getAttributes().get(0));
  }

  @Test
  public void testAttributesTwo() {
    System.out.println("attributesTwo");

    final Element e1 = Element.getMutable();
    e1.setName("test");
    e1.getSubnodes().setType(RegexpType.LAMBDA);

    final Attribute a1 = TestUtils.getAttribute("id");
    e1.getAttributes().add(a1);

    final Element e2 = Element.getMutable();
    e2.setName("test2");
    e2.getSubnodes().setType(RegexpType.LAMBDA);

    final Attribute a2 = TestUtils.getAttribute("id2");
    e2.getAttributes().add(a2);

    final Attribute a3 = TestUtils.getAttribute("id3");
    e1.getAttributes().add(a3);
    e2.getAttributes().add(a3);

    final List<Element> l = new ArrayList<Element>(1);
    l.add(e1);
    l.add(e2);
    final List<Element> result = new CloneHelper().cloneRules(l, null);

    assertEquals(2, result.size());
    final Element e1o = result.get(0);
    assertEquals(e1.getName(), e1o.getName());
    assert (e1o != e1);

    final Element e2o = result.get(1);
    assertEquals(e2.getName(), e2o.getName());
    assert (e2o != e2);

    assertEquals(2, e1o.getAttributes().size());
    assertEquals(2, e2o.getAttributes().size());
    assertEquals(e1.getAttributes().get(0).getName(), e1o.getAttributes().get(0).getName());
    assertEquals(e1.getAttributes().get(1).getName(), e1o.getAttributes().get(1).getName());
    assertEquals(e2.getAttributes().get(0).getName(), e2o.getAttributes().get(0).getName());
    assertEquals(e2.getAttributes().get(1).getName(), e2o.getAttributes().get(1).getName());
    assert (e1.getAttributes().get(0) != e1o.getAttributes().get(0));
    assert (e1.getAttributes().get(1) != e1o.getAttributes().get(1));
    assert (e2.getAttributes().get(0) != e2o.getAttributes().get(0));
    assert (e2.getAttributes().get(1) != e2o.getAttributes().get(1));
  }
}
