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

package cz.cuni.mff.ksi.jinfer.twostep.cleaning.cleanernull;

import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CleanerNullFactory.
 *
 * @author anti
 */
public class CleanerNullFactoryTest {

  public CleanerNullFactoryTest() {
  }

  /**
   * Test of create method, of class CleanerNullFactory.
   */
  @Test
  public void testCreate() {
    System.out.println("create");
    NullFactory instance = new NullFactory();
    RegularExpressionCleaner<String> result = instance.<String>create();
    assertNotNull(result);
  }

  /**
   * Test of getUserModuleDescription method, of class CleanerNullFactory.
   */
  @Test
  public void testGetUserModuleDescription() {
    System.out.println("getUserModuleDescription");
    NullFactory instance = new NullFactory();
    String result = instance.getUserModuleDescription();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of getName method, of class CleanerNullFactory.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    NullFactory instance = new NullFactory();
    String expResult = "TwoStepRegularExpressionCleanerNull";
    String result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of getModuleDescription method, of class CleanerNullFactory.
   */
  @Test
  public void testGetModuleDescription() {
    System.out.println("getModuleDescription");
    NullFactory instance = new NullFactory();
    String expResult = "Null";
    String result = instance.getModuleDescription();
    assertEquals(expResult, result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of getCapabilities method, of class CleanerNullFactory.
   */
  @Test
  public void testGetCapabilities() {
    System.out.println("getCapabilities");
    NullFactory instance = new NullFactory();
    List<String> result = instance.getCapabilities();
    assertTrue(result.isEmpty());
  }

  /**
   * Test of getUserModuleDescription method, of class CleanerNullFactory.
   */
  @Test
  public void testGetDisplayModuleDescription() {
    System.out.println("getDisplayModuleDescription");
    NullFactory instance = new NullFactory();
    String expResult = "RegularExpressionCleanerNull";
    String result = instance.getUserModuleDescription();
    assertFalse(expResult.equals(result));
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }
}