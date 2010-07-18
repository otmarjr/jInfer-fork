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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class FileUtilsTest {

  @Test(expected = NullPointerException.class)
  public void testGetExtensionNull() {
    System.out.println("getExtensionNull");
    FileUtils.getExtension(null);
  }

  @Test
  public void testGetExtension1() {
    System.out.println("getExtension1");
    String fileName = "file.ext";
    String expResult = "ext";
    String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetExtension2() {
    System.out.println("getExtension2");
    String fileName = "file.ext1.ext2";
    String expResult = "ext2";
    String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetExtension3() {
    System.out.println("getExtension3");
    String fileName = "noextension";
    String expResult = "noextension";
    String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetExtension4() {
    System.out.println("getExtension4");
    String fileName = "weirdfile.";
    String expResult = "";
    String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }
}
