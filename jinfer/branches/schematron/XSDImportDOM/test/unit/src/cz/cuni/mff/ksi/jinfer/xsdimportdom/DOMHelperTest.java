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

import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDTag;
import org.junit.Test;
import org.openide.util.NbBundle;
import static org.junit.Assert.*;

/**
 *
 * @author reseto
 */
@SuppressWarnings("PMD.SystemPrintln")
public class DOMHelperTest {

  @Test
  @SuppressWarnings("PMD.ForeignClassBundleCode")
  public void testErrorNested() {
    System.out.println("test error nested");
    final String result = DOMHelper.errorWrongNested(XSDTag.ELEMENT, XSDTag.ELEMENT);
    final String expected = NbBundle.getMessage(DOMHelper.class, "Error.WrongTagsNested", XSDTag.ELEMENT.toString(), XSDTag.ELEMENT.toString());
    assertEquals(expected, result);
    System.out.println("result: " + result);
  }
}