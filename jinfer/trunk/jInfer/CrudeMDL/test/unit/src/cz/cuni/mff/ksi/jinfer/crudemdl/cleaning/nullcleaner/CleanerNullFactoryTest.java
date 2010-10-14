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

package cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.nullcleaner;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class CleanerNullFactoryTest {

    public CleanerNullFactoryTest() {
    }

  /**
   * Test of getName method, of class CleanerNullFactory.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    CleanerNullFactory instance = new CleanerNullFactory();
    String expResult = "RegularExpressionCleanerNull";
    String result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of getModuleDescription method, of class CleanerNullFactory.
   */
  @Test
  public void testGetModuleDescription() {
    System.out.println("getModuleDescription");
    CleanerNullFactory instance = new CleanerNullFactory();
    String expResult = "RegularExpressionCleanerNull";
    String result = instance.getModuleDescription();
    assertFalse(expResult.equals(result));
    assertFalse(result.isEmpty());
  }

  /**
   * Test of getCapabilities method, of class CleanerNullFactory.
   */
  @Test
  public void testGetCapabilities() {
    System.out.println("getCapabilities");
    CleanerNullFactory instance = new CleanerNullFactory();
    List<String> result = instance.getCapabilities();
    assertTrue(result.isEmpty());
  }

  /**
   * Test of getDisplayModuleDescription method, of class CleanerNullFactory.
   */
  @Test
  public void testGetDisplayModuleDescription() {
    System.out.println("getDisplayModuleDescription");
    CleanerNullFactory instance = new CleanerNullFactory();
    String expResult = "RegularExpressionCleanerNull";
    String result = instance.getDisplayModuleDescription();
    assertFalse(expResult.equals(result));
    assertFalse(result.isEmpty());
  }
}