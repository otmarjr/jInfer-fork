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

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class CollectionToStringTest {

  @Test(expected=NullPointerException.class)
  public void testColToStringEmpty() {
    System.out.println("colToStringEmpty");
    CollectionToString.colToString(null, ",", null);
  }

  @Test
  public void testColToStringOne() {
    System.out.println("colToStringOne");
    final String res = CollectionToString.colToString(Arrays.asList("lala"), ",", CollectionToString.IDEMPOTENT);
    Assert.assertEquals("(lala)", res);
  }

  @Test
  public void testColToStringTwo() {
    System.out.println("colToStringTwo");
    final String res = CollectionToString.colToString(Arrays.asList("lala", "baf"), ",", CollectionToString.IDEMPOTENT);
    Assert.assertEquals("(lala,baf)", res);
  }

  @Test
  public void testColToStringTwoDifferent() {
    System.out.println("colToStringTwoDifferent");
    final String res = CollectionToString.colToString(Arrays.asList("lala ", "baf"), " ", CollectionToString.IDEMPOTENT);
    Assert.assertEquals("(lala  baf)", res);
  }

}