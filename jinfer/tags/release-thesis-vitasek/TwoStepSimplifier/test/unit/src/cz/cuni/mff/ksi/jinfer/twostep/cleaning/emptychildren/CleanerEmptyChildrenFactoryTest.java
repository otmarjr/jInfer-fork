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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CleanerEmptyChildrenFactory.
 *
 * @author anti
 */
@SuppressWarnings("PMD.SystemPrintln")
public class CleanerEmptyChildrenFactoryTest {

  /**
   * Test of create method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testCreate() {
    System.out.println("create");
    final EmptyChildrenFactory instance = new EmptyChildrenFactory();
    final RegularExpressionCleaner<String> result = instance.create();
    assertNotNull(result);
  }

  /**
   * Test of getName method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    final EmptyChildrenFactory instance = new EmptyChildrenFactory();
    final String expResult = "TwoStepRegularExpressionCleanerEmptyChildren";
    final String result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of getCapabilities method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetCapabilities() {
    System.out.println("getCapabilities");
    final EmptyChildrenFactory instance = new EmptyChildrenFactory();
    final List<String> result = instance.getCapabilities();
    assertTrue(result.isEmpty());
  }

  /**
   * Test of getModuleDescription method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetModuleDescription() {
    System.out.println("getModuleDescription");
    final EmptyChildrenFactory instance = new EmptyChildrenFactory();
    final String expResult = "Empty Children";
    final String result = instance.getModuleDescription();
    assertEquals(expResult, result);
  }

  /**
   * Test of getUserModuleDescription method, of class CleanerEmptyChildrenFactory.
   */
  @Test
  public void testGetUserModuleDescription() {
    System.out.println("getUserModuleDescription");
    final EmptyChildrenFactory instance = new EmptyChildrenFactory();
    final String expResult = "RegularExpressionCleanerEmptyChildren";
    final String result = instance.getUserModuleDescription();
    assertFalse(expResult.equals(result));
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }
}