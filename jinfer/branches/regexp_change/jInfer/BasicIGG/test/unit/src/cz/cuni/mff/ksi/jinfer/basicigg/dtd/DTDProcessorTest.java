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
package cz.cuni.mff.ksi.jinfer.basicigg.dtd;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class DTDProcessorTest {

  private static final char NL = '\n';

  @Test(expected = RuntimeException.class)
  public void testProcessNull() {
    System.out.println("processNull");
    final List<AbstractNode> expResult = new ArrayList<AbstractNode>(0);
    final List<AbstractNode> result = new DTDProcessor().process(null);
    assertEquals(expResult, result);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessEmpty() {
    System.out.println("processEmpty");
    final InputStream s = new ByteArrayInputStream("".getBytes());
    final List<AbstractNode> expResult = new ArrayList<AbstractNode>(0);
    final List<AbstractNode> result = new DTDProcessor().process(s);
    assertEquals(expResult, result);
  }

  @Test
  public void testProcessEmpty2() {
    System.out.println("processEmpty2");
    final InputStream s = new ByteArrayInputStream("<!-- Inferred on Sat Jul 24 17:53:09 CEST 2010 by Trivial IG Generator, Modular Simplifier, Trivial DTD exporter -->\n\n\n".getBytes());
    final List<AbstractNode> expResult = new ArrayList<AbstractNode>(0);
    final List<AbstractNode> result = new DTDProcessor().process(s);
    assertEquals(expResult, result);
  }
  private static final String ZOO =
          "<!ELEMENT zoos EMPTY>" + NL
          + "<!ELEMENT animal EMPTY>" + NL
          + "<!ELEMENT zoo (animal*) >" + NL;
  private static final String[] ZOO_RESULTS = {
    "zoos: ELEMENT\n()",
    "zoo: ELEMENT\n(animal: ELEMENT\n(),)",
    "animal: ELEMENT\n()"};

  @Test
  public void testProcess() {
    System.out.println("process");
    final InputStream s = new ByteArrayInputStream(ZOO.getBytes());
    final List<AbstractNode> result = new DTDProcessor().process(s);
    assertEquals(ZOO_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, ZOO_RESULTS[i], result.get(i).toString());
    }
  }
  private static final String ATTRIBUTES =
          "<!ELEMENT elem EMPTY>" + NL
          + "<!ATTLIST elem " + NL
          + "	id (5|6|7) #REQUIRED" + NL
          + "	name (a) \"a\"" + NL
          + "	clas (prima|sekunda) #IMPLIED>" + NL
          + "<!ELEMENT elements ((elem)*)>" + NL;
  private static final String[] ATTR_RESULTS = {
    "elem: ELEMENT\n(name: ATTRIBUTE\nnull: ,clas: ATTRIBUTE\nnull: ,id: ATTRIBUTE\nnull: ,)",
    "elements: ELEMENT\n(elem: ELEMENT\n(),)"};

  @Test
  public void testProcessAttrs() {
    System.out.println("processAttrs");
    final InputStream s = new ByteArrayInputStream(ATTRIBUTES.getBytes());
    final List<AbstractNode> result = new DTDProcessor().process(s);
    assertEquals(ATTR_RESULTS.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      assertEquals("Iteration " + i, ATTR_RESULTS[i], result.get(i).toString());
    }
  }
}
