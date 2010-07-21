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

import java.util.Map;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.NodeType;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
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

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull() {
    System.out.println("cloneRulesNull");
    new CloneHelper().cloneRules(null);
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull2() {
    System.out.println("cloneRulesNull2");
    new CloneHelper().cloneRules(Arrays.<AbstractNode>asList(null));
  }

  @Test(expected = NullPointerException.class)
  public void testCloneRulesNull3() {
    System.out.println("cloneRulesNull3");
    new CloneHelper().cloneRules(Arrays.<AbstractNode>asList(new Element(null, "e", null, null), null));
  }

  @Test
  public void testCloneRules() {
    System.out.println("cloneRules");
    final Map<String, Object> m = new HashMap<String, Object>();
    m.put("one", Integer.valueOf(1));
    m.put("two", Integer.valueOf(2));

    final List<Regexp<AbstractNode>> altChildren = new ArrayList<Regexp<AbstractNode>>();
    altChildren.add(Regexp.getToken((AbstractNode) new Element(null, "e1", null, Regexp.<AbstractNode>getConcatenation())));

    final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>();
    children.add(Regexp.getAlternation(altChildren));
    children.add(Regexp.getToken((AbstractNode) new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation())));

    final Element e = new Element(null, "e", m, Regexp.getConcatenation(children));
    final List<AbstractNode> l = new ArrayList<AbstractNode>(1);
    l.add(e);
    final List<AbstractNode> result = new CloneHelper().cloneRules(l);

    assertEquals(1, result.size());
    assert (l != result);
    final Element other = (Element) result.get(0);
    assert (e != other);
    assert (e.getSubnodes() != other.getSubnodes());
    assert (e.getSubnodes().getChild(0) != other.getSubnodes().getChild(0));
    assert (e.getSubnodes().getChild(0).getChild(0) != other.getSubnodes().getChild(0).getChild(0));
    assert (e.getSubnodes().getChild(1) != other.getSubnodes().getChild(1));
  }

  @Test(expected = NullPointerException.class)
  public void testCloneClustersNull1() {
    System.out.println("cloneClustersNull1");
    new CloneHelper().cloneClusters(null);
  }

  @Test(expected = NullPointerException.class)
  public void testCloneClustersNull2() {
    System.out.println("cloneClustersNull2");
    final List<Pair<AbstractNode, List<AbstractNode>>> clusters = new ArrayList<Pair<AbstractNode, List<AbstractNode>>>();
    clusters.add(null);
    new CloneHelper().cloneClusters(clusters);
  }

  @Test(expected = NullPointerException.class)
  public void testCloneClustersNull3() {
    System.out.println("cloneClustersNull3");
    final List<Pair<AbstractNode, List<AbstractNode>>> clusters = new ArrayList<Pair<AbstractNode, List<AbstractNode>>>();
    clusters.add(new Pair<AbstractNode, List<AbstractNode>>(null, null));
    new CloneHelper().cloneClusters(clusters);
  }

  @Test
  public void testCloneClustersSmall() {
    System.out.println("cloneClustersSmall");

    final List<AbstractNode> l = new ArrayList<AbstractNode>(1);
    l.add(null);

    final List<Pair<AbstractNode, List<AbstractNode>>> clusters = new ArrayList<Pair<AbstractNode, List<AbstractNode>>>();
    clusters.add(new Pair<AbstractNode, List<AbstractNode>>(null, l));
    final List<Pair<AbstractNode, List<AbstractNode>>> result = new CloneHelper().cloneClusters(clusters);
    assertEquals(1, result.size());
    assertEquals(null, result.get(0).getFirst());
    assertEquals(null, result.get(0).getSecond().get(0));
  }

  @Test
  public void testCloneClusters() {
    System.out.println("cloneClusters");

    final Map<String, Object> m = new HashMap<String, Object>();
    m.put("one", Integer.valueOf(1));
    m.put("two", Integer.valueOf(2));

    final List<Regexp<AbstractNode>> altChildren = new ArrayList<Regexp<AbstractNode>>();
    altChildren.add(Regexp.getToken((AbstractNode) new Element(null, "e1", null, Regexp.<AbstractNode>getConcatenation())));

    final List<Regexp<AbstractNode>> children = new ArrayList<Regexp<AbstractNode>>();
    children.add(Regexp.getAlternation(altChildren));
    children.add(Regexp.getToken((AbstractNode) new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation())));

    final Element e = new Element(null, "e", m, Regexp.getConcatenation(children));
    final List<AbstractNode> l = new ArrayList<AbstractNode>(1);
    l.add(e);

    final List<Pair<AbstractNode, List<AbstractNode>>> clusters = new ArrayList<Pair<AbstractNode, List<AbstractNode>>>();
    clusters.add(new Pair<AbstractNode, List<AbstractNode>>(e, l));
    final List<Pair<AbstractNode, List<AbstractNode>>> result = new CloneHelper().cloneClusters(clusters);

    assertEquals(1, result.size());
    assert (clusters != result);
    final List<AbstractNode> otherList = result.get(0).getSecond();
    assert (l != otherList);
    final Element other = (Element) otherList.get(0);
    assert (e != other);
    assert (e != result.get(0).getSecond());
    assert (e.getSubnodes() != other.getSubnodes());
    assert (e.getSubnodes().getChild(0) != other.getSubnodes().getChild(0));
    assert (e.getSubnodes().getChild(0).getChild(0) != other.getSubnodes().getChild(0).getChild(0));
    assert (e.getSubnodes().getChild(1) != other.getSubnodes().getChild(1));
  }

  @Test
  public void testCycle() {
    System.out.println("cycle");

    final Element e1 = new Element(null, "e1", null, Regexp.<AbstractNode>getConcatenation());
    final Element e2 = new Element(null, "e2", null, Regexp.<AbstractNode>getConcatenation());
    e1.getSubnodes().addChild(Regexp.<AbstractNode>getToken(e2));
    e2.getSubnodes().addChild(Regexp.<AbstractNode>getToken(e1));

    final List<AbstractNode> l = new ArrayList<AbstractNode>(1);
    l.add(e1);
    final List<AbstractNode> result = new CloneHelper().cloneRules(l);

    assertEquals(1, result.size());
    final Element e1o = (Element) result.get(0);
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

    final Element e1 = new Element(null, "e1", null, Regexp.<AbstractNode>getToken(null));
    final Element e2 = new Element(null, "e2", null, Regexp.<AbstractNode>getToken(null));
    e1.getSubnodes().setContent(e2);
    e2.getSubnodes().setContent(e1);

    final List<AbstractNode> l = new ArrayList<AbstractNode>(1);
    l.add(e1);
    final List<AbstractNode> result = new CloneHelper().cloneRules(l);

    assertEquals(1, result.size());
    final Element e1o = (Element) result.get(0);
    assertEquals(e1.getName(), e1o.getName());
    final Element e2o = (Element) e1o.getSubnodes().getContent();
    assertEquals(e2.getName(), e2o.getName());
    final Element e1oo = (Element) e2o.getSubnodes().getContent();
    assert (e1o != e1);
    assert (e2o != e2);
    assert (e1o == e1oo);
  }
}
