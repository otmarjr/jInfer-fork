/*
 *  Copyright (C) 2011 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimportdom;

import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
public class DOMParserTest {

  private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private static final String SCHEMA_B = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">";
  private static final String SCHEMA_E = "</xs:schema>";
  private static final char NL = '\n';

  private List<Element> make(final InputStream stream) throws InterruptedException {
    final DOMParser parser = new DOMParser();
    return parser.parse(stream);
  }

  @Test
  public void testGetName() {
    assertEquals("DOMParser", new DOMParser().getName());
  }

  @Test
  public void testDOMNull() throws InterruptedException {
    System.out.println("DOM null");
    final List<Element> expResult = new ArrayList<Element>(0);
    final List<Element> result = make(null);
    assertEquals(expResult, result);
  }

  private static final String EMPTY1 =
    XML_HEAD + NL
    + SCHEMA_B + NL
    + SCHEMA_E;

  @Test
  public void testDOMEmpty() throws InterruptedException {
    System.out.println("DOM empty schema");
    final List<Element> expResult = new ArrayList<Element>(0);
    final List<Element> result = make(new ByteArrayInputStream(EMPTY1.getBytes()));
    assertEquals(expResult, result);
  }

  private static final String EMPTY2 =
    XML_HEAD + NL
    + "<!-- Inferred on ... -->" + NL
    + SCHEMA_B + NL
    + SCHEMA_E;

  @Test
  public void testDOMEmpty2() throws InterruptedException {
    System.out.println("DOM empty comment");
    final InputStream s = new ByteArrayInputStream(EMPTY2.getBytes());
    final List<Element> expResult = new ArrayList<Element>(0);
    final List<Element> result = make(s);
    assertEquals(expResult, result);
  }

  private static final String TOP_REF =
    XML_HEAD + NL
    + SCHEMA_B + NL
    + "<xs:element ref=\"one\"/>" + NL
    + SCHEMA_E;

  @Test(expected = XSDException.class)
  public void testDOMRefTop() throws InterruptedException {
    System.out.println("DOM top element with ref");
    final InputStream s = new ByteArrayInputStream(TOP_REF.getBytes());
    final List<Element> expResult = new ArrayList<Element>(0);
    final List<Element> result = make(s);
    assertEquals(expResult, result);
  }

  private static final String TEST1 =
    XML_HEAD + NL
    + SCHEMA_B + NL
    + "<xs:element name=\"one\"/>" + NL
    + SCHEMA_E;

  private static final String[] TEST1_RES = {
    "one: ELEMENT\nλ"
  };

  @Test
  public void testDOM1() throws InterruptedException {
    System.out.println("DOM normal element");
    final InputStream s = new ByteArrayInputStream(TEST1.getBytes());
    final List<Element> result = make(s);
    assertEquals(TEST1_RES.length, result.size());
    for (int i = 0; i < result.size(); i++) {
      System.out.println(i + " " + result.get(i).toString());
      assertEquals("Iteration " + i, TEST1_RES[i], result.get(i).toString());
    }
  }
}