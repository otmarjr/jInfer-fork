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
@SuppressWarnings({"PMD.SystemPrintln", "PMD.CompareObjectsWithEquals", "PMD.DataflowAnomalyAnalysis"})
public class CloneHelperTest {

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull() {
    System.out.println("cloneRulesNull");
    new CloneHelper().cloneGrammar(null);
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull2() {
    System.out.println("cloneRulesNull2");
    new CloneHelper().cloneGrammar(Arrays.<Element>asList((Element)null));
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull3() {
    System.out.println("cloneRulesNull3");
    new CloneHelper().cloneGrammar(
            Arrays.<Element>asList(
              TestUtils.getElement("e", Regexp.<AbstractStructuralNode>getLambda()), null));
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
    final List<Element> result = new CloneHelper().cloneGrammar(l);

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
    // test if one instance of an element in two regexps is cloned as the same instance

    final Element root = TestUtils.getElement("root", Regexp.<AbstractStructuralNode>getMutable());
    final Element e0 = TestUtils.getElement("e0", Regexp.<AbstractStructuralNode>getMutable());
    final Element e1 = TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getMutable());
    final Element same = TestUtils.getElement("same");

    root.getSubnodes().setType(RegexpType.CONCATENATION);
    root.getSubnodes().setInterval(RegexpInterval.getKleeneCross());
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e0));
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e1));
    root.getSubnodes().setImmutable();

    e0.getSubnodes().setType(RegexpType.CONCATENATION);
    e0.getSubnodes().setInterval(RegexpInterval.getOptional());
    e0.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(same));
    e0.getSubnodes().setImmutable();
    e1.getSubnodes().setType(RegexpType.CONCATENATION);
    e1.getSubnodes().setInterval(RegexpInterval.getKleeneStar());
    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(same));
    e1.getSubnodes().setImmutable();

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
    assertTrue("cloned e0 is an element", tokens.get(0).isElement());
    assertTrue("cloned e1 is an element", tokens.get(1).isElement());

    final Element ce0 = (Element) tokens.get(0);
    assertEquals("ce0 size has 1 token", ce0.getSubnodes().getTokens().size(), 1);
    assertFalse("ce0 element is immutable", ce0.isMutable());
    final Element ce1 = (Element) tokens.get(1);
    assertEquals("ce1 size has 1 token", ce1.getSubnodes().getTokens().size(), 1);
    assertFalse("ce1 element is immutable", ce1.isMutable());

    assertTrue("ce0/same is an Element", ce0.getSubnodes().getTokens().get(0).isElement());
    assertTrue("ce1/same is an Element", ce1.getSubnodes().getTokens().get(0).isElement());
    final Element ce0s = (Element) ce0.getSubnodes().getTokens().get(0);
    final Element ce1s = (Element) ce1.getSubnodes().getTokens().get(0);
    assertFalse("the same element is immutable", ce0s.isMutable());
    assertTrue("ce0/same.name equals ce1/same.name", ce0s.getName().equals(ce1s.getName()));

    // these two references must the same
    assertSame("ce0/same == ce1/same", ce0s, ce1s);
    
    // but the elements must pass any implementation of equals,
    // because it's the "same" instance
    assertEquals("ce0/same equals ce1/same", ce0s, ce1s);

    // test prefix (there is no real context, but prefix should be there)
    assertEquals(prefix, cloned.getContext());
    assertEquals(prefix, ce0.getContext());
    assertEquals(prefix, ce1.getContext());
    assertEquals(prefix, ce0s.getContext());
    assertEquals(prefix, ce1s.getContext());
  }

  @Test
  public void testNonequalBranch() {
    System.out.println("Nonequal branch");
    // test if two elements with same names, but distinct instances are cloned as two instances

    final Element root = TestUtils.getElement("root", Regexp.<AbstractStructuralNode>getMutable());
    final Element e0 = TestUtils.getElement("e0", Regexp.<AbstractStructuralNode>getMutable());
    final Element e1 = TestUtils.getElement("e1", Regexp.<AbstractStructuralNode>getMutable());
    final Element s0 = TestUtils.getElement("same");
    final Element s1 = TestUtils.getElement("same");

    root.getSubnodes().setType(RegexpType.CONCATENATION);
    root.getSubnodes().setInterval(RegexpInterval.getKleeneCross());
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e0));
    root.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(e1));
    root.getSubnodes().setImmutable();

    e0.getSubnodes().setType(RegexpType.CONCATENATION);
    e0.getSubnodes().setInterval(RegexpInterval.getOptional());
    e0.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(s0));
    e0.getSubnodes().setImmutable();
    e1.getSubnodes().setType(RegexpType.CONCATENATION);
    e1.getSubnodes().setInterval(RegexpInterval.getKleeneStar());
    e1.getSubnodes().addChild(Regexp.<AbstractStructuralNode>getToken(s1));
    e1.getSubnodes().setImmutable();

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
    assertTrue("cloned e0 is an element", tokens.get(0).isElement());
    assertTrue("cloned e1 is an element", tokens.get(1).isElement());

    final Element ce0 = (Element) tokens.get(0);
    assertEquals("ce0 size has 1 token", ce0.getSubnodes().getTokens().size(), 1);
    assertFalse("ce0 element is immutable", ce0.isMutable());
    final Element ce1 = (Element) tokens.get(1);
    assertEquals("ce1 size has 1 token", ce1.getSubnodes().getTokens().size(), 1);
    assertFalse("ce1 element is immutable", ce1.isMutable());

    assertTrue("ce0/same is an Element", ce0.getSubnodes().getTokens().get(0).isElement());
    assertTrue("ce1/same is an Element", ce1.getSubnodes().getTokens().get(0).isElement());
    final Element ce0s = (Element) ce0.getSubnodes().getTokens().get(0);
    final Element ce1s = (Element) ce1.getSubnodes().getTokens().get(0);
    assertFalse("s0 element is immutable", ce0s.isMutable());
    assertFalse("s1 same element is immutable", ce1s.isMutable());
    assertTrue("ce0/same.name equals ce1/same.name", ce0s.getName().equals(ce1s.getName()));

    assertNotSame("ce0/same != ce1/same", ce0s, ce1s);

    // test prefix (there is no real context, but prefix should be there)
    assertEquals(prefix, cloned.getContext());
    assertEquals(prefix, ce0.getContext());
    assertEquals(prefix, ce1.getContext());
    assertEquals(prefix, ce0s.getContext());
    assertEquals(prefix, ce1s.getContext());
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
    final List<Element> result = new CloneHelper().cloneGrammar(l);

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
    final List<Element> result = new CloneHelper().cloneGrammar(l);

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
    final List<Element> result = new CloneHelper().cloneGrammar(l);

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
    final List<Element> result = new CloneHelper().cloneGrammar(l);

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

  @Test
  public void testGetPrefixedContextEmptySource() {
    System.out.println("prefixed context empty source context");

    final Element e = new Element(TestUtils.EMPTY_CONTEXT, "e", TestUtils.EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(), Collections.<Attribute>emptyList());

    final String one = "one";
    final String two = "two";
    final List<String> prefix = new ArrayList<String>();
    prefix.add(one);
    prefix.add(two);
    final List<String> res = CloneHelper.getPrefixedContext(e, prefix);
    assertEquals(prefix, res);
  }

  @Test
  public void testGetPrefixedContextEmptyPrefix() {
    System.out.println("prefixed context empty prefix parameter");

    final String one = "one";
    final String two = "two";
    final List<String> prefix = new ArrayList<String>();
    prefix.add(one);
    prefix.add(two);
    final Element e = new Element(prefix, "e", TestUtils.EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(), Collections.<Attribute>emptyList());

    final List<String> res = CloneHelper.getPrefixedContext(e, TestUtils.EMPTY_CONTEXT);
    assertEquals(prefix, res);
  }

  @Test
  public void testGetPrefixedContextBothEmpty() {
    System.out.println("prefixed context empty list");

    final List<String> prefix = TestUtils.EMPTY_CONTEXT;
    final Element e = new Element(prefix, "e", TestUtils.EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(), Collections.<Attribute>emptyList());

    final List<String> res = CloneHelper.getPrefixedContext(e, prefix);
    assertEquals(prefix, res);
  }

  @Test
  public void testGetPrefixedContextNullPrefix() {
    System.out.println("prefixed context null prefix parameter");

    final String one = "one";
    final String two = "two";
    final List<String> prefix = new ArrayList<String>();
    prefix.add(one);
    prefix.add(two);
    final Element e = new Element(prefix, "e", TestUtils.EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(), Collections.<Attribute>emptyList());

    final List<String> res = CloneHelper.getPrefixedContext(e, null);
    assertEquals(prefix, res);
  }

  @Test
  public void testGetPrefixedContext() {
    System.out.println("prefixed context normal");

    final String one = "one";
    final List<String> prefix1 = new ArrayList<String>();
    prefix1.add(one);
    final String two = "two";
    final List<String> prefix2 = new ArrayList<String>();
    prefix1.add(two);

    final Element e = new Element(prefix2, "e", TestUtils.EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(), Collections.<Attribute>emptyList());

    final List<String> prefix = new ArrayList<String>();
    prefix.add(one);
    prefix.add(two);
    final List<String> res = CloneHelper.getPrefixedContext(e, prefix1);
    assertEquals(prefix, res);
  }
}
