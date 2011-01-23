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

  @Test(expected = RuntimeException.class)
  public void testProcessNull() {
    System.out.println("processNull");
    final List<Element> expResult = new ArrayList<Element>(0);
    final DOMHandler handler = new DOMHandler();
    handler.parse(null);
    final List<Element> result = handler.getRules();
    assertEquals(expResult, result);
  }
  
  @Test(expected = RuntimeException.class)
  public void testProcessEmpty() {
    System.out.println("processEmpty");
    final List<Element> expResult = new ArrayList<Element>(0);
    final DOMHandler handler = new DOMHandler();
    final InputStream s = new ByteArrayInputStream("".getBytes());
    handler.parse(s);
    final List<Element> result = handler.getRules();
    assertEquals(expResult, result);
  }
}