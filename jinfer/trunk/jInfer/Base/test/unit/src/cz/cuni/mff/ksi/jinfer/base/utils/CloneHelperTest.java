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
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class CloneHelperTest {

  private static final List<String> EMPTY_CONTEXT = new ArrayList<String>(0);
  private static final Map<String, Object> EMPTY_METADATA = new HashMap<String, Object>();

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull() {
    System.out.println("cloneRulesNull");
    new CloneHelper().cloneRules(null);
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull2() {
    System.out.println("cloneRulesNull2");
    new CloneHelper().cloneRules(Arrays.<Element>asList(null));
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull3() {
    System.out.println("cloneRulesNull3");
    new CloneHelper().cloneRules(Arrays.<Element>asList(new Element(EMPTY_CONTEXT, "e", EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(),
            new ArrayList<Attribute>(0)), null));
  }

  @Test
  public void testCloneRules() {
    System.out.println("cloneRules");
    final Map<String, Object> m = new HashMap<String, Object>();
    m.put("one", Integer.valueOf(1));
    m.put("two", Integer.valueOf(2));

    final List<Regexp<AbstractStructuralNode>> altChildren = new ArrayList<Regexp<AbstractStructuralNode>>();
    altChildren.add(Regexp.getToken(
            (AbstractStructuralNode) new Element(EMPTY_CONTEXT, "e1", EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(),
            new ArrayList<Attribute>(0))));

    final List<Regexp<AbstractStructuralNode>> children = new ArrayList<Regexp<AbstractStructuralNode>>();
    children.add(Regexp.getAlternation(altChildren));
    children.add(Regexp.getToken((AbstractStructuralNode) new Element(EMPTY_CONTEXT, "e2", EMPTY_METADATA, Regexp.<AbstractStructuralNode>getLambda(), new ArrayList<Attribute>(0))));

    final Element e = new Element(EMPTY_CONTEXT, "e", m, Regexp.getConcatenation(children), new ArrayList<Attribute>(0));
    final List<Element> l = new ArrayList<Element>(1);
    l.add(e);
    final List<Element> result = new CloneHelper().cloneRules(l);

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
  public void testCycle() {
    System.out.println("cycle");

    final Element e1 = new Element(EMPTY_CONTEXT, "e1", EMPTY_METADATA, Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e2 = new Element(EMPTY_CONTEXT, "e2", EMPTY_METADATA, Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
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
    final List<Element> result = new CloneHelper().cloneRules(l);

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

    final Element e1 = new Element(EMPTY_CONTEXT, "e1", EMPTY_METADATA, Regexp.<AbstractStructuralNode>getMutable(), new ArrayList<Attribute>(0));
    final Element e2 = new Element(EMPTY_CONTEXT, "e2", EMPTY_METADATA, Regexp.<AbstractStructuralNode>getToken(e1), new ArrayList<Attribute>(0));
    e1.getSubnodes().setContent(e2);
    e1.getSubnodes().setInterval(RegexpInterval.getOnce());
    e1.getSubnodes().setType(RegexpType.TOKEN);
    e1.getSubnodes().setImmutable();

    final List<Element> l = new ArrayList<Element>(1);
    l.add(e1);
    final List<Element> result = new CloneHelper().cloneRules(l);

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
    final Attribute a1 = new Attribute(EMPTY_CONTEXT, "id", EMPTY_METADATA, null, EMPTY_CONTEXT);
    e1.getAttributes().add(a1);

    final List<Element> l = new ArrayList<Element>(1);
    l.add(e1);
    final List<Element> result = new CloneHelper().cloneRules(l);

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

    final Attribute a1 = new Attribute(EMPTY_CONTEXT, "id", EMPTY_METADATA, null, EMPTY_CONTEXT);
    e1.getAttributes().add(a1);

    final Element e2 = Element.getMutable();
    e2.setName("test2");
    e2.getSubnodes().setType(RegexpType.LAMBDA);

    final Attribute a2 = new Attribute(EMPTY_CONTEXT, "id2", EMPTY_METADATA, null, EMPTY_CONTEXT);
    e2.getAttributes().add(a2);

    final Attribute a3 = new Attribute(EMPTY_CONTEXT, "id3", EMPTY_METADATA, null, EMPTY_CONTEXT);
    e1.getAttributes().add(a3);
    e2.getAttributes().add(a3);

    final List<Element> l = new ArrayList<Element>(1);
    l.add(e1);
    l.add(e2);
    final List<Element> result = new CloneHelper().cloneRules(l);

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
