/*
 *  Copyright (C) 2010 anti
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

package cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.emptychildren;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class CleanerEmptyChildrenTest {

    public CleanerEmptyChildrenTest() {
    }

  private Regexp<String> createRegexp() {
    Regexp<String> r1= Regexp.<String>getMutable();
    r1.setInterval(RegexpInterval.getOnce());
    r1.setType(RegexpType.CONCATENATION);
    r1.setImmutable();

    Regexp<String> r3= Regexp.<String>getMutable();
    r3.setInterval(RegexpInterval.getOnce());
    r3.setType(RegexpType.TOKEN);
    r3.setContent("a");
    r3.setImmutable();

    Regexp<String> r2= Regexp.<String>getMutable();
    r2.setInterval(RegexpInterval.getOnce());
    r2.setType(RegexpType.CONCATENATION);
    r2.addChild(r1);
    r2.addChild(r1);
    r2.addChild(r3);
    r2.addChild(r1);
    r2.addChild(r1);
    r2.setImmutable();

    return r2;
  }

  /**
   * Test of cleanRegularExpression method, of class CleanerEmptyChildren.
   */
  @Test
  public void testCleanRegularExpression() {
    System.out.println("cleanRegularExpression");
    Regexp<String> regexp = createRegexp();
    CleanerEmptyChildren<String> instance = new CleanerEmptyChildren<String>();
    Regexp<String> result = instance.cleanRegularExpression(regexp);
    assertEquals("(a)", result.toString());
  }

}