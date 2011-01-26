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
package cz.cuni.mff.ksi.jinfer.twostep.cleaning.nestedconcatenation;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CleanerNestedConcatenation.
 *
 * @author anti
 */
@SuppressWarnings("PMD.SystemPrintln")
public class CleanerNestedConcatenationTest {

  /**
   * Test of cleanRegularExpression method, of class CleanerNestedConcatenation.
   */
  @Test
  public void testCleanRegularExpression() {
    System.out.println("cleanRegularExpression");
    final Regexp<String> r7 = Regexp.<String>getMutable();
    r7.setInterval(RegexpInterval.getOnce());
    r7.setType(RegexpType.TOKEN);
    r7.setContent("b");
    r7.setImmutable();

    final Regexp<String> r6 = Regexp.<String>getMutable();
    r6.setInterval(RegexpInterval.getOnce());
    r6.setType(RegexpType.TOKEN);
    r6.setContent("a");
    r6.setImmutable();

    final Regexp<String> r5 = Regexp.<String>getMutable();
    r5.setInterval(RegexpInterval.getOnce());
    r5.setType(RegexpType.TOKEN);
    r5.setContent("c");
    r5.setImmutable();

    final Regexp<String> r4 = Regexp.<String>getMutable();
    r4.setInterval(RegexpInterval.getOnce());
    r4.setType(RegexpType.CONCATENATION);
    r4.addChild(r6);
    r4.addChild(r7);
    r4.setImmutable();

    final Regexp<String> r3 = Regexp.<String>getMutable();
    r3.setInterval(RegexpInterval.getOnce());
    r3.setType(RegexpType.TOKEN);
    r3.setContent("d");
    r3.setImmutable();

    final Regexp<String> r2 = Regexp.<String>getMutable();
    r2.setInterval(RegexpInterval.getOnce());
    r2.setType(RegexpType.CONCATENATION);
    r2.addChild(r4);
    r2.addChild(r5);
    r2.setImmutable();

    final Regexp<String> r1 = Regexp.<String>getMutable();
    r1.setInterval(RegexpInterval.getOnce());
    r1.setType(RegexpType.CONCATENATION);
    r1.addChild(r2);
    r1.addChild(r3);
    r1.setImmutable();

    final Regexp<String> regexp = r1;
    final RegularExpressionCleaner<String> instance =
            new NestedConcatenationFactory().<String>create();
    final Regexp<String> expResult = Regexp.<String>getMutable();
    expResult.getChildren().add(r6);
    expResult.getChildren().add(r7);
    expResult.getChildren().add(r5);
    expResult.getChildren().add(r3);
    expResult.setType(RegexpType.CONCATENATION);
    expResult.setInterval(RegexpInterval.getOnce());
    expResult.setImmutable();
    final Regexp<String> result = instance.cleanRegularExpression(regexp);
    assertEquals("(a\n,b\n,c\n,d)", result.toString());
    assertEquals(expResult.toString(), result.toString());
  }
}
