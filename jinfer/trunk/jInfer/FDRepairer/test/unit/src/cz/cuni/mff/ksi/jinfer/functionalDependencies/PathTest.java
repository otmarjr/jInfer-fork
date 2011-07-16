/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sviro
 */
public class PathTest {

  public PathTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPathNull() {
    Path path = new Path(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPathEmpty() {
    Path path = new Path("");
  }

  /**
   * Test of isStringPath method, of class Path.
   */
  @Test
  public void testIsStringPath() {
    Path instance = new Path("//bib/text()");
    boolean expResult = true;
    boolean result = instance.isStringPath();
    assertEquals(expResult, result);
  }

  /**
   * Test of isStringPath method, of class Path.
   */
  @Test
  public void testIsStringPath2() {
    Path instance = new Path("//bib/@ano");
    boolean expResult = true;
    boolean result = instance.isStringPath();
    assertEquals(expResult, result);
  }

  /**
   * Test of isElementPath method, of class Path.
   */
  @Test
  public void testIsElementPath() {
    Path instance = new Path("//bib");
    boolean expResult = true;
    boolean result = instance.isElementPath();
    assertEquals(expResult, result);
  }

  /**
   * Test of getPathValue method, of class Path.
   */
  @Test
  public void testGetPathValue() {
    Path instance = new Path("//bib");
    String expResult = "//bib";
    String result = instance.getPathValue();
    assertEquals(expResult, result);
  }
}
