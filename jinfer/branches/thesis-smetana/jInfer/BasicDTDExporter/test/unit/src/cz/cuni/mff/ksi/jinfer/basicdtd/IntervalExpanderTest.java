/*
 *  Copyright (C) 2011 anti
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
package cz.cuni.mff.ksi.jinfer.basicdtd;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of {@link IntervalExpander}
 *
 * @author anti
 */
public class IntervalExpanderTest {

  public IntervalExpanderTest() {
  }

  private String comboToString(Regexp<AbstractStructuralNode> r, String delim) {
    final StringBuilder sb = new StringBuilder("(");
    for (Regexp<AbstractStructuralNode> rr : r.getChildren()) {
      sb.append(toTestString(rr));
      sb.append(delim);
    }
    sb.append(")");
    sb.append(r.getInterval().toString());
    return sb.toString();
  }

  private String toTestString(Regexp<AbstractStructuralNode> r) {
    switch (r.getType()) {
      case LAMBDA:
        return "lambda";
      case TOKEN:
        return r.getContent().getName() + r.getInterval().toString();
      case CONCATENATION:
        return comboToString(r, ",");
      case ALTERNATION:
        return comboToString(r, "|");
      case PERMUTATION:
        return comboToString(r, "&");
    }
    return "";
  }

  @Test
  public void testExpandIntervalsElement1() {
    System.out.println("expandIntervalsElement 1,1");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(1, 1));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("b", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElement2() {
    System.out.println("expandIntervalsElement 0,1");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(0, 1));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("b?", toTestString(result));
  }

  /**
   * Test of expandIntervalsElement method, of class IntervalExpander.
   */
  @Test
  public void testExpandIntervalsElement3() {
    System.out.println("expandIntervalsElement 1,inf");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getUnbounded(1));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("b+", toTestString(result));
  }

  /**
   * Test of expandIntervalsElement method, of class IntervalExpander.
   */
  @Test
  public void testExpandIntervalsElement4() {
    System.out.println("expandIntervalsElement 0,inf");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getUnbounded(0));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("b*", toTestString(result));
  }

  /**
   * Test of expandIntervalsElement method, of class IntervalExpander.
   */
  @Test
  public void testExpandIntervalsElement5() {
    System.out.println("expandIntervalsElement 1,2");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(1, 2));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b,b?,)", toTestString(result));
  }

  /**
   * Test of expandIntervalsElement method, of class IntervalExpander.
   */
  @Test
  public void testExpandIntervalsElement6() {
    System.out.println("expandIntervalsElement 1,3");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(1, 3));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b,b?,b?,)", toTestString(result));
  }

  /**
   * Test of expandIntervalsElement method, of class IntervalExpander.
   */
  @Test
  public void testExpandIntervalsElement7() {
    System.out.println("expandIntervalsElement 2,5");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(2, 5));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b,b,b?,b?,b?,)", toTestString(result));
  }

  /**
   * Test of expandIntervalsElement method, of class IntervalExpander.
   */
  @Test
  public void testExpandIntervalsElement8() {
    System.out.println("expandIntervalsElement 2,inf");

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.TOKEN);
    treeBase.getSubnodes().setContent(b);
    treeBase.getSubnodes().setInterval(RegexpInterval.getUnbounded(2));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b,b+,)", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt1() {
    System.out.println("expandIntervalsElementAlt 1,1");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(1, 1));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b|c|)", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt2() {
    System.out.println("expandIntervalsElementAlt 0,1");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(0, 1));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b|c|)?", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt3() {
    System.out.println("expandIntervalsElementAlt 1,inf");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getUnbounded(1));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b|c|)+", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt4() {
    System.out.println("expandIntervalsElementAlt 0,inf");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getUnbounded(0));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("(b|c|)*", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt5() {
    System.out.println("expandIntervalsElementAlt 1,2");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(1, 2));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("((b|c|),(b|c|)?,)", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt6() {
    System.out.println("expandIntervalsElementAlt 1,3");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(1, 3));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("((b|c|),(b|c|)?,(b|c|)?,)", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt7() {
    System.out.println("expandIntervalsElementAlt 2,5");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(2, 5));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("((b|c|),(b|c|),(b|c|)?,(b|c|)?,(b|c|)?,)", toTestString(result));
  }

  @Test
  public void testExpandIntervalsElementAlt8() {
    System.out.println("expandIntervalsElementAlt 2,inf");

    final Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    final Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    final Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    final Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    final Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getUnbounded(2));

    final IntervalExpander instance = new IntervalExpander();
    final Regexp<AbstractStructuralNode> result = instance.expandIntervalsRegexp(treeBase.getSubnodes());

    assertEquals("((b|c|),(b|c|)+,)", toTestString(result));
  }
}