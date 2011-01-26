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

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
@SuppressWarnings("PMD.SystemPrintln")
public class DOMHandlerTest {

  private static final char NL = '\n';

  private List<Element> make(final InputStream stream) {
    final DOMHandler handler = new DOMHandler();
    handler.parse(stream);
    return handler.getRules();
  }

  @Test
  public void testProcessNull() {
    System.out.println("processNull");
    final List<Element> expResult = new ArrayList<Element>(0);
    final List<Element> result = make(null);
    assertEquals(expResult, result);
  }
  
  @Test
  public void testProcessEmpty() {
    System.out.println("processEmpty");
    final List<Element> expResult = new ArrayList<Element>(0);
    final List<Element> result = make(new ByteArrayInputStream("".getBytes()));
    assertEquals(expResult, result);
  }

  @Test
  public void testProcessEmpty2() {
    System.out.println("processEmpty2");
    final InputStream s = new ByteArrayInputStream("<!-- Inferred on Sat Jul 24 17:53:09 CEST 2010 by Trivial IG Generator, Modular Simplifier, Trivial DTD exporter -->\n\n\n".getBytes());
    final List<Element> expResult = new ArrayList<Element>(0);
    final List<Element> result = make(s);
    assertEquals(expResult, result);
  }

  private static final String ZOO =
          "<!ELEMENT zoos EMPTY>" + NL
          + "<!ELEMENT animal EMPTY>" + NL
          + "<!ELEMENT zoo (animal*) >" + NL;
}