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

package cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.nestedconcatenation;

import cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.RegularExpressionCleaner;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class CleanerNestedConcatenationFactoryTest {

  public CleanerNestedConcatenationFactoryTest() {
  }

  /**
   * Test of create method, of class CleanerNestedConcatenationFactory.
   */
  @Test
  public void testCreate() {
    System.out.println("create");
    CleanerNestedConcatenationFactory instance = new CleanerNestedConcatenationFactory();
    RegularExpressionCleaner<String> result = instance.<String>create();
    assertNotNull(result);
  }

  /**
   * Test of getName method, of class CleanerNestedConcatenationFactory.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    CleanerNestedConcatenationFactory instance = new CleanerNestedConcatenationFactory();
    String expResult = "RegularExpressionCleanerNestedConcatenation";
    String result = instance.getName();
    assertEquals(expResult, result);
  }

  /**
   * Test of getModuleDescription method, of class CleanerNestedConcatenationFactory.
   */
  @Test
  public void testGetModuleDescription() {
    System.out.println("getModuleDescription");
    CleanerNestedConcatenationFactory instance = new CleanerNestedConcatenationFactory();
    String expResult = "RegularExpressionCleanerNestedConcatenation";
    String result = instance.getModuleDescription();
    assertEquals(expResult, result);
  }

  /**
   * Test of getCapabilities method, of class CleanerNestedConcatenationFactory.
   */
  @Test
  public void testGetCapabilities() {
    System.out.println("getCapabilities");
    CleanerNestedConcatenationFactory instance = new CleanerNestedConcatenationFactory();
    List<String> result = instance.getCapabilities();
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  /**
   * Test of getUserModuleDescription method, of class CleanerNestedConcatenationFactory.
   */
  @Test
  public void testGetUserModuleDescription() {
    System.out.println("getUserModuleDescription");
    CleanerNestedConcatenationFactory instance = new CleanerNestedConcatenationFactory();
    String expResult = "RegularExpressionCleanerNestedConcatenation";
    String result = instance.getUserModuleDescription();
    assertNotNull(result);
    assertFalse(expResult.equals(result));
    assertFalse(result.isEmpty());
  }
}