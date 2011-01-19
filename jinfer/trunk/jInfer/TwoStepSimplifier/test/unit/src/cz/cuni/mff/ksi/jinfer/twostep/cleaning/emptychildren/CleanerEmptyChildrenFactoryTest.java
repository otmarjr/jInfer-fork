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

package cz.cuni.mff.ksi.jinfer.twostep.cleaning.emptychildren;

import cz.cuni.mff.ksi.jinfer.twostep.cleaning.RegularExpressionCleaner;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CleanerEmptyChildrenFactory.
 *
 * @author anti
 */
public class CleanerEmptyChildrenFactoryTest {

  public CleanerEmptyChildrenFactoryTest() {
  }

  /**
   * Test of create method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testCreate() {
    System.out.println("create");
    EmptyChildrenFactory instance = new EmptyChildrenFactory();
    RegularExpressionCleaner<String> result = instance.create();
    assertNotNull(result);
  }

  /**
   * Test of getName method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    EmptyChildrenFactory instance = new EmptyChildrenFactory();
    String expResult = "RegularExpressionCleanerEmptyChildren";
    String result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of getCapabilities method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetCapabilities() {
    System.out.println("getCapabilities");
    EmptyChildrenFactory instance = new EmptyChildrenFactory();
    List<String> result = instance.getCapabilities();
    assertTrue(result.isEmpty());
  }

  /**
   * Test of getModuleDescription method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetModuleDescription() {
    System.out.println("getModuleDescription");
    EmptyChildrenFactory instance = new EmptyChildrenFactory();
    String expResult = "Regular Expression Cleaner Empty Children";
    String result = instance.getModuleDescription();
    assertEquals(expResult, result);
  }

  /**
   * Test of getUserModuleDescription method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetUserModuleDescription() {
    System.out.println("getUserModuleDescription");
    EmptyChildrenFactory instance = new EmptyChildrenFactory();
    String expResult = "RegularExpressionCleanerEmptyChildren";
    String result = instance.getUserModuleDescription();
    assertFalse(expResult.equals(result));
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }
}