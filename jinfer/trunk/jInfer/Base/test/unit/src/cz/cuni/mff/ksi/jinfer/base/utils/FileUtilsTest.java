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
    final String fileName = "file.ext";
    final String expResult = "ext";
    final String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetExtension2() {
    System.out.println("getExtension2");
    final String fileName = "file.ext1.ext2";
    final String expResult = "ext2";
    final String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetExtension3() {
    System.out.println("getExtension3");
    final String fileName = "noextension";
    final String expResult = "noextension";
    final String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }

  @Test
  public void testGetExtension4() {
    System.out.println("getExtension4");
    final String fileName = "weirdfile.";
    final String expResult = "";
    final String result = FileUtils.getExtension(fileName);
    assertEquals(expResult, result);
  }
}
